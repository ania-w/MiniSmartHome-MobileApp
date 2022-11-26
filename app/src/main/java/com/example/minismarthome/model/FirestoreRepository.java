package com.example.minismarthome.model;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.model.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


public class FirestoreRepository {

    CollectionReference roomsCollection;
    CollectionReference roomDimmerDataCollection;
    CollectionReference roomSensorDataCollection;
    CollectionReference sensorsCollection;
    CollectionReference dimmersCollection;

    List<Room> rooms = new ArrayList<>();
    List<Device> sensors = new ArrayList<>();
    List<DimmerData> dimmerData = new ArrayList<>();
    List<Dimmer> dimmers = new ArrayList<>();

    public FirestoreRepository() {
        FirebaseFirestore database = FirebaseFirestore.getInstance();

        this.roomsCollection = database.collection("rooms");
        this.roomDimmerDataCollection = database.collection("room_dimmer_data");
        this.roomSensorDataCollection = database.collection("room_sensor_data");
        this.sensorsCollection = database.collection("sensors");
        this.dimmersCollection = database.collection("dimmers");

        setOnDimmerUpdateListener();
        setOnSensorUpdateListener();
        setOnRoomsUpdateListener();
        setOnSensorDataUpdateListener();
        setOnDimmerDataUpdateListener();
    }

    private void setOnDimmerDataUpdateListener() {
        roomDimmerDataCollection.addSnapshotListener((snapshots, e) -> {

            if (isErr(snapshots, e) && snapshots!=null) return;

            dimmerData = snapshots.toObjects(DimmerData.class);

            dimmerData.forEach(dd -> {
                Set<String> dimmer_ids=dd.getLight_intensity_map().keySet();
                List<Dimmer> dimm = dimmers.stream().filter(d -> dimmer_ids.contains(d.getId())).collect(Collectors.toList());
                rooms.stream().filter(r -> r.getId().equals(dd.getRoom_id())).forEach(rr -> rr.setDimmers(dimm));
            });
        });
    }

    private void setOnSensorUpdateListener() {
        sensorsCollection.addSnapshotListener((snapshots, e) -> {
            if (isErr(snapshots, e) && snapshots!=null) return;

            sensors = snapshots.getDocuments().stream().map(this::getSensorForDocument).collect(Collectors.toList());
        });
    }

    public void setOnDimmerUpdateListener(){
        dimmersCollection.addSnapshotListener((snapshots, e) -> {
            if (isErr(snapshots, e) && snapshots != null) return;

            dimmers = snapshots.toObjects(Dimmer.class);
            dimmerData.forEach(dd -> {
                Set<String> dimmer_ids=dd.getLight_intensity_map().keySet();
                List<Dimmer> dimm = dimmers.stream().filter(d -> dimmer_ids.contains(d.getId())).collect(Collectors.toList());
                rooms.stream().filter(r -> r.getId().equals(dd.getRoom_id())).forEach(rr -> rr.setDimmers(dimm));
            });

        });
    }

    private void setOnSensorDataUpdateListener() {

        roomSensorDataCollection.addSnapshotListener((snapshots, e) -> {

            if (isErr(snapshots, e)) return;

            for (DocumentChange dc : snapshots.getDocumentChanges()) {
                String id = (String) dc.getDocument().getData().get("room_id");
                rooms.stream().filter(room -> room.getId().equals(id)).findFirst().get().setSensor_data((Map<String, Double>) dc.getDocument().getData().get("data"));
            }
        });
    }

    private void setOnRoomsUpdateListener() {

        roomsCollection.addSnapshotListener((snapshots, e) -> {

            if (isErr(snapshots, e)) return;

            for (DocumentChange dc : snapshots.getDocumentChanges()) {
                RoomDTO updatedRoom = dc.getDocument().toObject(RoomDTO.class);
                String id = dc.getDocument().getId();
                switch (dc.getType()) {
                    case MODIFIED:
                        rooms = rooms.stream().map(r -> r.getId().equals(id) ? new Room(updatedRoom) : r).collect(Collectors.toList());
                        break;
                    case ADDED:
                        if (rooms.stream().noneMatch(r -> r.getId().equals(id)))
                            rooms.add(new Room(updatedRoom));
                        break;
                    case REMOVED:
                        Room toBeRemoved = rooms.stream().filter(r -> r.getId().equals(id)).findFirst().get();
                        rooms.remove(toBeRemoved);
                }
            }
        });
    }

    public boolean isErr(QuerySnapshot snapshots, FirebaseFirestoreException e) {
        if (e != null || snapshots == null) {
            System.err.println("Listen failed: " + e);
            return true;
        }
        return false;
    }

    private Device getSensorForDocument(DocumentSnapshot document) {

        String name = document.get("class_name", String.class);

        switch (name) {
            case "SGP30":
                return document.toObject(SGP30.class);
            case "AM2301":
                return document.toObject(AM2301.class);
        }

        return null;
    }


    public List<Room> getRooms(){
        return rooms;
    }

    public static void updateDimmerData(String id,String dataDestinationId, Integer value){
        FirebaseFirestore database = FirebaseFirestore.getInstance();

        database.collection("room_dimmer_data").document(dataDestinationId).get().addOnSuccessListener(task -> {
                Map<String,Integer> map = (Map<String, Integer>) task.get("light_intensity_map");
                map.put(id,value);
                database.collection("room_dimmer_data").document(dataDestinationId).update("light_intensity_map",map).addOnSuccessListener(task_update -> {});
        });
    }

}

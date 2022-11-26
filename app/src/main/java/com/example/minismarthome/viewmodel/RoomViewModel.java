package com.example.minismarthome.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.minismarthome.model.Dimmer;
import com.example.minismarthome.model.FirestoreRepository;
import com.example.minismarthome.model.Room;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class RoomViewModel extends AndroidViewModel {

    public MutableLiveData<List<Room>> roomsLiveData=new MutableLiveData<>();
    List<Room> rooms=new ArrayList<>();
    public MutableLiveData<Dimmer> dimmer=new MutableLiveData<>();

    static FirestoreRepository repository=new FirestoreRepository();
    ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    public RoomViewModel(@NonNull Application application) {
        super(application);

        try {
            executor.scheduleAtFixedRate(this::refresh, 0, 5, TimeUnit.SECONDS);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void updateDimmerData(String id, String dataDestinationId, Integer lightIntensity){
        FirestoreRepository.updateDimmerData(id, dataDestinationId, lightIntensity);
    }

    public void refresh(){
        try {
            rooms=repository.getRooms();

            roomsLiveData.postValue(rooms);


        } catch (Exception e){
            e.printStackTrace();
        }
    }

}

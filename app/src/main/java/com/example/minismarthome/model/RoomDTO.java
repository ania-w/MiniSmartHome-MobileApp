package com.example.minismarthome.model;

public class RoomDTO {
    private String id;
    private String name;
    private String room_dimmer_data_id;
    private String room_sensor_data_id;

    public RoomDTO() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoom_dimmer_data_id() {
        return room_dimmer_data_id;
    }

    public void setRoom_dimmer_data_id(String room_dimmer_data_id) {
        this.room_dimmer_data_id = room_dimmer_data_id;
    }

    public String getRoom_sensor_data_id() {
        return room_sensor_data_id;
    }

    public void setRoom_sensor_data_id(String room_sensor_data_id) {
        this.room_sensor_data_id = room_sensor_data_id;
    }
}

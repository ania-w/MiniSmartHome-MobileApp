package com.example.minismarthome.model;

import android.util.ArrayMap;

import java.util.List;
import java.util.Map;

public class Room {
    private String name;
    private String id;
    private Map<String,Double> sensor_data;
    private List<Dimmer> dimmers;
    private static Map<String,String>  units=new ArrayMap<>();

    static {
        units.put("temperature","C");
        units.put("humidity","%");
        units.put("co2"," ppm");
        units.put("tvoc"," ppb");
    }

    public Room(RoomDTO roomDTO) {
        this.id=roomDTO.getId();
        this.name=roomDTO.getName();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<String, Double> getSensor_data() {
        return sensor_data;
    }

    public void setSensor_data(Map<String, Double> sensor_data) {
        this.sensor_data = sensor_data;
    }

    public List<Dimmer> getDimmers() {
        return dimmers;
    }
//TODO string builder
    public void setDimmers(List<Dimmer> dimmers) {
        this.dimmers = dimmers;
    }

    public String getTemperature(){
        return String.format(java.util.Locale.US,
                "%.1f",
                sensor_data.get("temperature")) +
                units.get("temperature");
    }

    public String getHumidity(){
        return String.format(java.util.Locale.US,
                "%.1f",
                sensor_data.get("humidity")) + units.get("humidity");
    }

    public String getCo2(){
        return String.format(java.util.Locale.US,
                "%.0f",
                sensor_data.get("co2")) + " " + units.get("co2");
    }

    public String getTvoc(){
        return String.format(java.util.Locale.US,
                "%.0f",
                sensor_data.get("tvoc")) + " " + units.get("tvoc");
    }
}

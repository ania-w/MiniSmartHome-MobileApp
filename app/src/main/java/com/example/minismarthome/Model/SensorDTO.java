package com.example.minismarthome.Model;

import android.util.ArrayMap;

import java.util.Map;

public class SensorDTO {
    Map<String,Number> data=new ArrayMap<>();
    public static Map<String,String>  units=new ArrayMap<>();

    static {
        units.put("temperature","C");
        units.put("humidity","%");
        units.put("co2"," ppm");
        units.put("tvoc"," ppb");
    }

    public SensorDTO(Map<String, Number> data) {
        this.data = data;
    }

    public Map<String, Number> getData() {
        return data;
    }
}

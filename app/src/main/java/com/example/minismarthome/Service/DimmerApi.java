package com.example.minismarthome.Service;

import com.example.minismarthome.Model.DimmerRequest;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.Query;


public interface DimmerApi {
    @POST("/api/dimmer/set")
    Call<Object> setLightIntensity (@Body DimmerRequest dimmer);
    @GET("/api/device/state")
    Call<Object> getState ();
}

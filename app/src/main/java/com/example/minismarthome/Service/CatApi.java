package com.example.minismarthome.Service;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Query;


public interface CatApi {
    String URL = "https://api.thecatapi.com/";
    @GET("v1/images/search")
    Call<List<Object>> getRandomCat(@HeaderMap Map<String, String> api_key,@Query("JSON") String params);
}

package com.example.minismarthome.Service;

import android.content.Context;

import com.example.minismarthome.Model.DimmerRequest;
import com.google.gson.Gson;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DimmerApiService {

    private DimmerApi api;
    private String ip;

    public DimmerApiService(){
    }

    private void initializeApi()
    {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(ip)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(DimmerApi.class);
    }

    public void setIp(String ip)
    {
        this.ip="http://"+ip+"/";
        initializeApi();
    }

    public void setLightIntensity(int lightIntensity) throws ExecutionException, InterruptedException, TimeoutException {
        CompletableFuture<String> completableFuture=new CompletableFuture<>();

        Call<Object> call = api.setLightIntensity(new DimmerRequest(lightIntensity));

        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                completableFuture.complete("yeey");
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
            }
        });
        completableFuture.get(100, TimeUnit.MILLISECONDS);
    }


}

package com.example.minismarthome.Service;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.minismarthome.config.Util;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CatApiService {
    private final String apiKey;
    private String currentImageUrl = null;
    private final CatApi catApi;
    Gson gson = new Gson();

    public CatApiService(Context context) throws IOException {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(CatApi.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        catApi = retrofit.create(CatApi.class);
        apiKey= Util.getProperty("apiKey",context);
    }

    public String getCurrentImageUrl() {
        return currentImageUrl;
    }

    public void generateImage() throws ExecutionException, InterruptedException {
        CompletableFuture<String> completableFuture=new CompletableFuture<>();
        Map<String, String> map = new HashMap<>();
        map.put("x-api-key", apiKey);
        Call<List<Object>> call = catApi.getRandomCat(map, gson.toJson(new request()));

        call.enqueue(new Callback<List<Object>>() {
            @Override
            public void onResponse(Call<List<Object>> call, Response<List<Object>> response) {
                LinkedTreeMap<String, String> map = (LinkedTreeMap) response.body().get(0);
                currentImageUrl = map.get("url");
                completableFuture.complete(currentImageUrl);
            }

            @Override
            public void onFailure(Call<List<Object>> call, Throwable t) {
            }
        });
        completableFuture.get();
    }

    static class request {
        Params params;

        request() {
            this.params = new Params();
        }

        static class Params {
            public int limit = 1;
            public String size = "full";
        }

    }

}

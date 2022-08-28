package com.example.minismarthome.Service;

import android.content.Context;
import android.content.Intent;

import com.example.minismarthome.Model.SensorDTO;
import com.example.minismarthome.config.Util;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Scope;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.ValueRange;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GoogleApiService {
    private static final Scope SCOPES = new Scope(SheetsScopes.SPREADSHEETS);
    private static String APPLICATION_NAME;
    public static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

    private boolean signedIn = false;
    String spreadsheetId;
    String dimmerSheetName;
    String sensorSheetName;
    GoogleAccountCredential credential;

    private Context context;
    Sheets service;

    SensorDTO  sensorData;

    public String getSensorData(String key) {
        return (sensorData != null) ?
                String.format(java.util.Locale.US,
                        "%.1f",
                        sensorData.getData().get(key)) + sensorData.units.get(key)
                : "--";
    }


    public GoogleApiService(Context context) throws IOException {
        this.context = context;
        spreadsheetId= Util.getProperty("spreadsheetId",context);
        APPLICATION_NAME= Util.getProperty("APPLICATION_NAME",context);
        sensorSheetName=Util.getProperty("sensorSheetName",context);
        dimmerSheetName=Util.getProperty("dimmerSheetName",context);
    }

    public boolean isLoggedIn() throws ApiException {

        if (GoogleSignIn.getLastSignedInAccount(context) == null) {
            return false;
        } else {
            createCredentials();
            return true;
        }
    }

    public void createCredentials() throws ApiException {
        credential = GoogleAccountCredential.usingOAuth2(context, Collections.singletonList(SheetsScopes.SPREADSHEETS));
        credential.setSelectedAccount(GoogleSignIn.getLastSignedInAccount(context).getAccount());

        service = new Sheets.Builder(new NetHttpTransport(), JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();

        signedIn = true;
    }

    public void updateSensorDataAtFixedRate() {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(() -> {
            try {
                if (signedIn) getSensorDataFromGoogleSheets();
            } catch (Exception e) {
                e.printStackTrace();
            }
        },0,3,TimeUnit.SECONDS);
    }

    private void getSensorDataFromGoogleSheets() {

        try {
            List<List<Object>> values = service.spreadsheets().values()
                    .get(spreadsheetId, sensorSheetName)
                    .execute().getValues();

            List<Map<String, Number>> maps = new ArrayList<>();
            for (List<Object> object : values)
                maps.add(JSON_FACTORY.fromString(object.get(5).toString(), Map.class));


                sensorData = new SensorDTO(buildMap(maps));


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private <E, T> Map<E, T> buildMap(List<Map<E, T>> maps) {
        return maps.stream().reduce((firstMap, secondMap) -> Stream.concat(firstMap.entrySet().stream(), secondMap.entrySet().stream())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))).get();
    }

    public void setLightIntensity(Integer lightIntensity) {
        if (signedIn) {
            setLightIntensityInGoogleSheets(lightIntensity);
        }
    }


    private void setLightIntensityInGoogleSheets(Integer lightIntensity) {
        try {
            List<List<Object>> values = Arrays.asList(Arrays.asList(lightIntensity));

            service.spreadsheets().values().update(spreadsheetId, dimmerSheetName, new ValueRange().setValues(values))
                    .setValueInputOption("RAW")
                    .execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Intent getSignInIntent() {

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestScopes(SCOPES)
                .build();

        return GoogleSignIn.getClient(context, gso).getSignInIntent();
    }
}

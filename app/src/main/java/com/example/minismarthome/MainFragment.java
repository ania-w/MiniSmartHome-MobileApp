package com.example.minismarthome;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.minismarthome.Service.CatApiService;
import com.example.minismarthome.Service.DimmerApiService;
import com.example.minismarthome.Service.GoogleApiService;
import com.example.minismarthome.config.Util;
import com.example.minismarthome.databinding.FragmentMainBinding;
import com.google.android.gms.common.api.ApiException;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainFragment extends Fragment {
    private FragmentMainBinding binding;

    GoogleApiService googleApiService;
    CatApiService catApiService;
    DimmerApiService dimmerLocalApiService;
    ExecutorService executor= Executors.newFixedThreadPool(3);
    SharedPreferences sharedPreferences;

    private View options;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        binding = FragmentMainBinding.inflate(inflater, container, false);

       return binding.getRoot();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        sharedPreferences = this.getActivity().getSharedPreferences("config", Context.MODE_PRIVATE);

        try {
            googleApiService=new GoogleApiService(this.getActivity());
            catApiService=new CatApiService(this.getActivity());
            dimmerLocalApiService=new DimmerApiService();
            setDimmerConfiguration();
            if(!googleApiService.isLoggedIn())
                logIntoGoogleFromActivity();
        } catch (Exception e) {
            e.printStackTrace();
        }

        googleApiService.updateSensorDataAtFixedRate();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loadCatto();

        updateUIFromGoogleSheets();

        setUpLightControlButtons();


        options = getLayoutInflater().inflate(R.layout.options_fragment,null);
        options.setVisibility(View.GONE);

        binding.catto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadCatto();
            }
        });


    }

    private void setDimmerConfiguration()  {
        try{
            String defaultPreferences=Util.getProperty("dimmer.ip",this.getActivity());
            dimmerLocalApiService.setIp(sharedPreferences.getString("dimmer.ip",defaultPreferences));
        }catch (Exception e){e.printStackTrace();}
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Do something that differs the Activity's menu here
        super.onCreateOptionsMenu(menu, inflater);
    }

     @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.options){
            try {
                NavHostFragment.findNavController(MainFragment.this)
                        .navigate(R.id.action_main_to_options);
            } catch (IllegalArgumentException ignored) {}
            return true;
        }
        else return false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void setUpLightControlButtons() {
        List<Button> buttons=new ArrayList<>();

        buttons.add(binding.li0);
        buttons.add(binding.li15);
        buttons.add(binding.li35);
        buttons.add(binding.li60);
        buttons.add(binding.li100);

        for (Button button: buttons) {

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Integer lightIntensity=Integer.parseInt(
                            getResources().getResourceEntryName(button.getId()).substring(2));
                    executor.execute(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Boolean isDimmerAccesedLocally=
                                        sharedPreferences.getBoolean("dimmer.locally",
                                                Boolean.parseBoolean(Util.getProperty("dimmer.locally",MainFragment.this.getActivity())));

                                if(isDimmerAccesedLocally){
                                    googleApiService.setLightIntensity(-1);
                                    dimmerLocalApiService.setLightIntensity(lightIntensity);
                                }
                                else
                                    googleApiService.setLightIntensity(lightIntensity);
                            } catch (Exception e) { e.printStackTrace(); }
                        }
                    });

                }
            });

        }

    }

    private void loadCatto() {
        executor.execute(() -> {
            try {
                catApiService.generateImage();
            } catch (Exception ignored) {}
                this.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Picasso.get().load(catApiService.getCurrentImageUrl()).into(binding.catto);
                    }
                });
        });
    }

    private void logIntoGoogleFromActivity() {
        Intent signInIntent = googleApiService.getSignInIntent();

        ActivityResultLauncher<Intent> startActivityIntent = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        try {
                            googleApiService.createCredentials();
                        } catch (ApiException e) {
                            e.printStackTrace();
                        }
                    }
                });

        startActivityIntent.launch(signInIntent);
    }

    private void updateUIFromGoogleSheets() {

        executor.execute(new Runnable() {
            @Override
            public void run() {
                boolean interrupted=false;
                while (!interrupted) {
                    String tempString = googleApiService.getSensorData("temperature");
                    String humString = googleApiService.getSensorData("humidity");

                    binding.co2TV.setText(googleApiService.getSensorData("co2"));
                    binding.tvocTV.setText(googleApiService.getSensorData("tvoc"));

                    if (!tempString.equals("101.0C")) {
                        binding.temperatureTV.setText(tempString);
                        binding.humidityTV.setText(humString);
                    }

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        interrupted=true;
                    }
                }
            }
        });
    }

}

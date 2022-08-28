package com.example.minismarthome;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.minismarthome.config.Util;
import com.example.minismarthome.databinding.OptionsFragmentBinding;

import java.io.IOException;


public class OptionsFragment extends Fragment {
    private OptionsFragmentBinding binding;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor myEdit;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = OptionsFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sharedPreferences = this.getActivity().getSharedPreferences("config", Context.MODE_PRIVATE);
        myEdit = sharedPreferences.edit();

        try {
            binding.checkBox.setChecked(sharedPreferences.getBoolean("dimmer.locally",
                    Boolean.parseBoolean(Util.getProperty("dimmer.locally",this.getActivity()))));
            binding.ip.setText(sharedPreferences.getString("dimmer.ip",
                    Util.getProperty("dimmer.ip",this.getActivity())));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onPause() {
        super.onPause();

        myEdit.putString("dimmer.ip", binding.ip.getText().toString());
        myEdit.putBoolean("dimmer.locally", binding.checkBox.isChecked());
        myEdit.apply();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

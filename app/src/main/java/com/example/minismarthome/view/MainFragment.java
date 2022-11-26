package com.example.minismarthome.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.example.minismarthome.databinding.FragmentRoomsBinding;
import com.example.minismarthome.viewmodel.RoomViewModel;

import java.util.ArrayList;


public class MainFragment extends Fragment {

    private FragmentRoomsBinding binding;
    private RoomViewModel viewModel;
    private RecyclerView recyclerView;
    private RoomAdapter adapter;

      public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(RoomViewModel.class);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter=new RoomAdapter(new ArrayList<>(),getContext());

        adapter.setHasStableIds(true);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(null);
        observeViewModel();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRoomsBinding.inflate(inflater, container, false);
        recyclerView=binding.roomsList;
        return binding.getRoot();
    }

    private void observeViewModel() {
        viewModel.roomsLiveData.observe(getViewLifecycleOwner(), rooms -> {
            if (rooms != null) {
                recyclerView.setVisibility(View.VISIBLE);
                binding.listError.setVisibility(View.GONE);
                binding.loadingView.setVisibility(View.GONE);
                adapter.updateList(rooms);
            }
        });

    }
}
package com.example.minismarthome.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.example.minismarthome.R;
import com.example.minismarthome.databinding.RoomBinding;
import com.example.minismarthome.model.Room;

import java.util.ArrayList;
import java.util.List;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.RoomViewHolder>{

    private List<Room> rooms;
    private Context ctx;
    private RecyclerView recyclerView;
    DimmerAdapter dimmerAdapter = new DimmerAdapter(new ArrayList<>());

    public RoomAdapter(List<Room> rooms, Context ctx) {
        this.rooms=rooms;
        this.ctx=ctx;
    }

    public void updateList(List<Room> newList) {
        updateRooms(newList);
        notifyDataSetChanged();
    }

    private void updateRooms(List<Room> rooms) {
        this.rooms=rooms;
    }

    @NonNull
    @Override
    public RoomAdapter.RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        RoomBinding view = DataBindingUtil.inflate(inflater, R.layout.room, parent, false);
        return new RoomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomAdapter.RoomViewHolder holder, int position) {
        dimmerAdapter.setDimmers(rooms.get(position).getDimmers());

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ctx);
        this.recyclerView=holder.dimmerRecyclerView;
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(null);

        recyclerView.setAdapter(dimmerAdapter);

        holder.itemView.setRoom(rooms.get(position));
    }

    @Override
    public long getItemId(int position) {
        return rooms.get(position).getId().hashCode();
    }

    @Override
    public int getItemCount() {
        return rooms.size();
    }

    public class RoomViewHolder extends RecyclerView.ViewHolder{

        public RecyclerView dimmerRecyclerView;
        public RoomBinding itemView;

        public RoomViewHolder(@NonNull RoomBinding binding) {
            super(binding.getRoot());
            dimmerRecyclerView = binding.recyclerView;
            this.itemView = binding;
        }


    }

}

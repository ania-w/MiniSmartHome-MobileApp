package com.example.minismarthome.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.minismarthome.R;
import com.example.minismarthome.databinding.DimmerBinding;
import com.example.minismarthome.databinding.RoomBinding;
import com.example.minismarthome.model.Dimmer;
import com.example.minismarthome.model.Room;
import com.example.minismarthome.viewmodel.RoomViewModel;

import java.util.ArrayList;
import java.util.List;

public class DimmerAdapter extends RecyclerView.Adapter<DimmerAdapter.DimmerViewHolder>{

    private List<Dimmer> dimmers;

    public DimmerAdapter(List<Dimmer> dimmers) {
        this.dimmers=dimmers;
    }

    public List<Dimmer> getDimmers() {
        return dimmers;
    }

    public void setDimmers(List<Dimmer> dimmers) {
        this.dimmers = dimmers;
    }

    @NonNull
    @Override
    public DimmerAdapter.DimmerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        DimmerBinding view = DataBindingUtil.inflate(inflater, R.layout.dimmer, parent, false);
        return new DimmerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DimmerAdapter.DimmerViewHolder holder, int position) {
        holder.itemView.setDimmer(dimmers.get(position));
    }

    @Override
    public long getItemId(int position) {
        return dimmers.get(position).getId().hashCode();
    }


    @Override
    public int getItemCount() {
        return dimmers.size();
    }

    public class DimmerViewHolder extends RecyclerView.ViewHolder{

        DimmerBinding itemView;
        Button button0;
        Button button25;
        Button button70;
        Button button100;

        public DimmerViewHolder(@NonNull DimmerBinding binding) {
            super(binding.getRoot());

            button0=binding.button;
            button25=binding.button2;
            button70=binding.button3;
            button100=binding.button4;

            button0.setOnClickListener(this::onClickMethod);
            button25.setOnClickListener(this::onClickMethod);
            button70.setOnClickListener(this::onClickMethod);
            button100.setOnClickListener(this::onClickMethod);

            this.itemView = binding;
        }

        public void onClickMethod(View view){
            Button button = (Button) view;
            Integer lightIntensity = Integer.valueOf(button.getText().toString());

            String id = itemView.getDimmer().getId();
            String dataDestinationId=itemView.getDimmer().getData_destination_id();

            RoomViewModel.updateDimmerData(id,dataDestinationId,lightIntensity);
        }

    }

}

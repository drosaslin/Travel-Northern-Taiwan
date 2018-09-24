package com.example.android.trip_organizer;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.travelnortherntaiwan.R;

import java.util.ArrayList;
import java.util.HashMap;


public class TripsAdapter extends RecyclerView.Adapter<TripsAdapter.ViewHolder> {
    ArrayList<TripBasicInfo> DataList;

    public TripsAdapter(ArrayList<TripBasicInfo> newTripList) {
        DataList = newTripList;
    }

    @NonNull
    @Override
    public TripsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trips_template, parent, false);
        return new TripsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TripsAdapter.ViewHolder holder, int position) {
        String name = DataList.get(position).getName();
        String region = "Region: " + DataList.get(position).getRegion();
        //String date;

//        if(DataList.get(position).getFromDate().equals("")){
//            date =
//        }

        holder.cardName.setText(name);
        holder.cardRegion.setText(region);
    }

    @Override
    public int getItemCount() {
        return DataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView cardName;
        TextView cardDate;
        TextView cardRegion;

        public ViewHolder(View itemView) {
            super(itemView);
            cardName = (itemView).findViewById(R.id.tripName);
            //cardDate = (itemView).findViewById(R.id.tripDate);
            cardRegion = (itemView).findViewById(R.id.tripRegion);
        }
    }
}

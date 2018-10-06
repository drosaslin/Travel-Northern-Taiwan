package com.example.android.trip_organizer;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.android.travelnortherntaiwan.R;

import java.util.ArrayList;
import java.util.HashMap;


public class TripsAdapter extends RecyclerView.Adapter<TripsAdapter.ViewHolder> {
    ArrayList<TripBasicInfo> DataList;
    Context context;

    public TripsAdapter(ArrayList<TripBasicInfo> newTripList, Context newContext) {
        DataList = newTripList;
        context = newContext;
    }

    @NonNull
    @Override
    public TripsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trips_template, parent, false);
        return new TripsAdapter.ViewHolder(view);
    }

    public void clearData() {
        DataList.clear();
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(TripsAdapter.ViewHolder holder, int position) {
        String name = DataList.get(position).getName();
        String region = "Region: " + DataList.get(position).getRegion();
        holder.cardName.setText(name);
        holder.cardRegion.setText(region);
//        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return DataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView cardName;
        TextView cardDate;
        TextView cardRegion;
        RelativeLayout relativeLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            relativeLayout = (itemView).findViewById(R.id.my_trips_list);
            cardName = (itemView).findViewById(R.id.tripName);
            //cardDate = (itemView).findViewById(R.id.tripDate);
            cardRegion = (itemView).findViewById(R.id.tripRegion);
        }
    }
}

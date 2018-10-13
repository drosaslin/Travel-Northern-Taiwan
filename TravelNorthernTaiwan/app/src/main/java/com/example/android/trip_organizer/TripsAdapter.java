package com.example.android.trip_organizer;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.android.travelnortherntaiwan.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;


public class TripsAdapter extends RecyclerView.Adapter<TripsAdapter.ViewHolder> {
    ArrayList<TripBasicInfo> DataList;
    Context context;
    private DatabaseReference mRootReference;

    public TripsAdapter(ArrayList<TripBasicInfo> newTripList, Context newContext) {
        DataList = newTripList;
        context = newContext;
        mRootReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://travel-northern-taiwan.firebaseio.com/");
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
    public void onBindViewHolder(TripsAdapter.ViewHolder holder, final int position) {
        String name = DataList.get(position).getName();
        String region = "Region: " + DataList.get(position).getRegion();
        holder.cardName.setText(name);
        holder.cardRegion.setText(region);
        holder.tripCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ShowInfoActivity.class);
                intent.putExtra("tripKey", DataList.get(position).getKey());
                context.startActivity(intent);
            }
        });

        //Deleting a card
        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRootReference.child("BasicTripInfo").child(DataList.get(position).getKey()).removeValue();
                mRootReference.child("Itinerary").child(DataList.get(position).getKey()).removeValue();
                mRootReference.child("ExpensesByTrip").child(DataList.get(position).getKey()).removeValue();
            }
        });
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
        CardView tripCard;
        ImageView deleteBtn;
//        RelativeLayout relativeLayout;

        public ViewHolder(View itemView) {
            super(itemView);
//            relativeLayout = (itemView).findViewById(R.id.my_trips_list);
            cardName = (itemView).findViewById(R.id.tripName);
            //cardDate = (itemView).findViewById(R.id.tripDate);
            cardRegion = (itemView).findViewById(R.id.tripRegion);
            tripCard = (itemView).findViewById(R.id.trip_card);
            deleteBtn = (itemView).findViewById(R.id.deleteBtn);
        }
    }
}

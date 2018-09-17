package com.example.android.map;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.travelnortherntaiwan.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by David Rosas on 8/23/2018.
 */

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.LocationsViewHolder> {
    private final String GOOGLE_API_KEY = "AIzaSyCc4acsOQV7rnQ92weHYKO14fvL9wkRpKc";
    private ArrayList<Results> locations;
    private LocationsListFragment locationsListFragment;

    public LocationAdapter(ArrayList<Results> newLocations) {
        locations = newLocations;
    }

    @NonNull
    @Override
    public LocationAdapter.LocationsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.map_location_info, parent, false);
        return new LocationAdapter.LocationsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final LocationAdapter.LocationsViewHolder holder, final int position) {
        if(locations.get(position).getPhotos() != null) {
            Photos photo = locations.get(position).getPhotos().get(0);
            if (photo != null) {
                String reference = photo.getPhoto_reference();
                String url = "https://maps.googleapis.com/maps/api/place/photo?&maxwidth=500&photoreference=" + reference + "&key=" + GOOGLE_API_KEY;
                Picasso.get().load(url).into(holder.placePhoto);
            }
        }

        holder.placeName.setText(locations.get(position).getName() + " " + position);
        holder.placeRating.setText(locations.get(position).getRating());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notifyListener(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return locations.size();
    }

    public void addNewData(ArrayList<Results> newResults) {
        locations.addAll(newResults);
        notifyDataSetChanged();
    }

    public void clearData() {
        locations.clear();
        notifyDataSetChanged();
    }

    public void setListener(LocationsListFragment newLocationsListFragment) {
        locationsListFragment = newLocationsListFragment;
    }

    public void notifyListener(int position) {
        locationsListFragment.updateActivity(locations.get(position).getPlace_id());
    }

    public class LocationsViewHolder extends RecyclerView.ViewHolder {
        TextView placeName;
        TextView placeRating;
        ImageView placePhoto;

        public LocationsViewHolder(View itemView) {
            super(itemView);

            placeName = (itemView).findViewById(R.id.place_name);
            placeRating = (itemView).findViewById(R.id.place_rating);
            placePhoto = (itemView).findViewById(R.id.place_image);
        }
    }
}

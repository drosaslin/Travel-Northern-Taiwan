package com.example.android.locations_info;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.map.Photos;
import com.example.android.map.Results;
import com.example.android.travelnortherntaiwan.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by David Rosas on 8/23/2018.
 */

public class LocationListAdapter extends RecyclerView.Adapter<LocationListAdapter.LocationsViewHolder> {
    private final String GOOGLE_API_KEY = "AIzaSyCc4acsOQV7rnQ92weHYKO14fvL9wkRpKc";
    private final String ADD_TAG = "add";
    private final String DELETE_TAG = "delete";

    private Context context;
    private ArrayList<Results> locations;
    private LocationsListFragment locationsListFragment;
    private String tripKey;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference mRootReference;
    LinkedHashMap<String, String> itinerary;
    HashMap<Integer, Boolean> buttonState;

    public LocationListAdapter(ArrayList<Results> newLocations, Context newContext, String newTripKey) {
        locations = newLocations;
        context = newContext;
        tripKey = newTripKey;
        buttonState = new HashMap<>();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        mRootReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://travel-northern-taiwan.firebaseio.com");

        setDatabase();
    }

    @NonNull
    @Override
    public LocationListAdapter.LocationsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.map_location_list_template, parent, false);
        return new LocationListAdapter.LocationsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final LocationListAdapter.LocationsViewHolder holder, final int position) {
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

        holder.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateItinerary(position, view);
                String tag = (String)view.getTag();
                String color = (tag.equals(ADD_TAG)) ? "#FF336A" : "#3335FF";
                holder.addButton.setBackgroundColor(Color.parseColor("#3335FF"));
            }
        });

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

    private void updateItinerary(int position, View view){
        String tag = (String) view.getTag();
        if(tag.equals(ADD_TAG)) {
            Log.d("UPDATING", tag);
            addToItinerary(position);
            view.setTag(DELETE_TAG);
            ((FloatingActionButton)view).setRippleColor(Color.parseColor("#FF336A"));
        }
        else {
            Log.d("UPDATING", tag);
            deleteFromItinerary(position);
            view.setTag(ADD_TAG);
            ((FloatingActionButton)view).setRippleColor(Color.parseColor("#3335FF"));
        }

        Log.d("TAGLOG", tag);
        String message = (tag.equals(ADD_TAG)) ? "Destination Added": "Destination Deleted";
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    private void addToItinerary(int position) {
        for (String trip : itinerary.keySet()) {
            if (itinerary.get(trip).equals("")) {
                itinerary.put(trip, locations.get(position).getId());
                updateDatabase();
                break;
            }
        }
    }

    private void deleteFromItinerary(int position) {
        for(String trip : itinerary.keySet()) {
            if (itinerary.get(trip).equals(locations.get(position).getId())) {
                itinerary.put(trip, "");
                reorderItinerary();
                updateDatabase();
                break;
            }
        }
    }

    private void updateDatabase() {
        for (String trip : itinerary.keySet()) {
            mRootReference.child("Itinerary").child(tripKey).child(trip).setValue(itinerary.get(trip));
        }
    }

    public void reorderItinerary() {
        int size = itinerary.size();
        for(int n = 0; n < size; n++) {
            if(n < size - 1) {
                String index = Integer.toString(n);
                String nextIndex = Integer.toString(n + 1);
                if(itinerary.get(index).equals("")) {
                    itinerary.put(index, itinerary.get(nextIndex));
                    itinerary.put(nextIndex, "");
                }
            }
        }
    }

    private void setDatabase() {
        itinerary = new LinkedHashMap<>();

        for(int n = 0; n < 10; n++) {
            itinerary.put(Integer.toString(n), "");
        }

        mRootReference.child("Itinerary").child(tripKey).setValue(itinerary);
    }

    public class LocationsViewHolder extends RecyclerView.ViewHolder {
        TextView placeName;
        TextView placeRating;
        ImageView placePhoto;
        FloatingActionButton addButton;

        public LocationsViewHolder(View itemView) {
            super(itemView);

            placeName = itemView.findViewById(R.id.place_name);
            placeRating = itemView.findViewById(R.id.place_rating);
            placePhoto = itemView.findViewById(R.id.place_image);
            addButton = itemView.findViewById(R.id.add_trip_button);
        }
    }
}

package com.example.android.locations_info;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.android.map.Photos;
import com.example.android.travelnortherntaiwan.R;
import com.example.android.travelnortherntaiwan.SingletonRequestQueue;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

public class LocationDetailsFragment extends Fragment {

    private final String GOOGLE_API_KEY = "AIzaSyCc4acsOQV7rnQ92weHYKO14fvL9wkRpKc";
    private String placeId;
    private String tripKey;
    private RequestQueue queue;
    private LocationDetailsResponse placeDetails;
    private PagerAdapter adapter;
    private TextView placeName;
    private TextView placeAddress;
    private TextView placeOpeningHours;
    private TextView placePhone;
    private TextView placeFee;
    private TabLayout tabLayout;
    private CheckBox addTripButton;
    private ViewPager pager;
    private TripDestinations destinations;
    private FirebaseAuth mAuth;
    private DatabaseReference mRootReference;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.map_fragment_location_details, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = getArguments();
        tripKey = (String) bundle.get("tripKey");
        Log.d("FRAGMENTKEY", tripKey);
        queue = SingletonRequestQueue.getInstance(getActivity()).getRequestQueue();
        destinations = TripDestinations.getInstance();

        mAuth = FirebaseAuth.getInstance();
        mRootReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://travel-northern-taiwan.firebaseio.com");

        tabLayout = getView().findViewById(R.id.details_tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Reviews"));
        tabLayout.addTab(tabLayout.newTab().setText("Photos"));
        tabLayout.setTabGravity(tabLayout.GRAVITY_FILL);

        placeName = getView().findViewById(R.id.place_name);
        placeAddress = getView().findViewById(R.id.place_address);
        placeOpeningHours = getView().findViewById(R.id.place_opening_hours);
        placePhone = getView().findViewById(R.id.place_phone_number);
        placeFee = getView().findViewById(R.id.place_entrance_fee);
        addTripButton = getView().findViewById(R.id.details_add_trip_button);
        addTripButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateItinerary();
            }
        });

        pager = getView().findViewById(R.id.view_pager);

        setTabLayout();
        apiCallPlaceDetails();
    }

    private void setTripButton() {
        if(destinations.getDestinations() != null) {
            for (String destination : destinations.getDestinations()) {
                if (destination.equals(placeDetails.getResult().getPlace_id())) {
                    addTripButton.setChecked(true);
                    break;
                }
            }
        }
    }

    private void setTabLayout() {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    public void setPlaceId(String newPlaceId) {
        placeId = newPlaceId;
    }

    private void updateUI(String response) {
        placeDetails = new Gson().fromJson(response, LocationDetailsResponse.class);
        setTripButton();
        Log.d("DETALLES", placeDetails.toString());

        adapter = new PagerAdapter(getChildFragmentManager(), tabLayout.getTabCount(), placeDetails);
        pager.setAdapter(adapter);
        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        //set all layout's view if their corresponding values are not null
        if(placeDetails.getResult().getName() != null) {
            placeName.setText(placeDetails.getResult().getName());
        }
        if(placeDetails.getResult().getFormatted_address() != null) {
            placeAddress.setText(placeDetails.getResult().getFormatted_address());
        }
        if(placeDetails.getResult().getOpening_hours() != null) {
            placeOpeningHours.setText(placeDetails.getResult().getOpening_hours().getOpen_now());
        }
        if(placeDetails.getResult().getFormatted_phone_number() != null) {
            placePhone.setText(placeDetails.getResult().getFormatted_phone_number());
        }
    }

    private void updateItinerary(){
        if(addTripButton.isChecked()) {
            addToItinerary();
//            locationsListFragment.updateMap(locations.get(position).getGeometry().getLocation(), true);
        }
        else {
            deleteFromItinerary();
//            locationsListFragment.updateMap(locations.get(position).getGeometry().getLocation(), false);
        }

        String message = (addTripButton.isChecked()) ? "Destination Added": "Destination Deleted";
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    private void addToItinerary() {
        destinations.addDestination(placeDetails.getResult().getId());
        updateDatabase();
    }

    private void deleteFromItinerary() {
        destinations.deleteDestination(placeDetails.getResult().getId());
        updateDatabase();
    }

    private void updateDatabase() {
        //updates the data in the database based on the items inside tripDestinations
        int arraySize = destinations.getDestinations().size();
        for(int n = 0; n < 10; n++ ) {
            //inserting all destinations added. Insert a blank character to all indexes without destinations
            if(n < arraySize) {
                mRootReference.child("Itinerary").child(tripKey).child(Integer.toString(n)).setValue(destinations.getDestination(n));
            }
            else {
                mRootReference.child("Itinerary").child(tripKey).child(Integer.toString(n)).setValue("");
            }
        }
    }

    private void apiCallPlaceDetails() {
        String url = "https://maps.googleapis.com/maps/api/place/details/json?placeid=" + placeId + "&fields=price_level,name,rating,formatted_address,formatted_phone_number,geometry,icon,id,opening_hours,photos,place_id,plus_code,rating,reviews&key=" + GOOGLE_API_KEY;
        Log.d("PLACEURL1", url);
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("Response", response);
                        updateUI(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Find", "Fail");
            }
        });

        queue.add(stringRequest);
    }
}

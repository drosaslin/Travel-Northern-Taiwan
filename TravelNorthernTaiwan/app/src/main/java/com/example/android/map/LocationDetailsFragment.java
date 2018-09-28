package com.example.android.map;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.android.travelnortherntaiwan.R;
import com.example.android.travelnortherntaiwan.SingletonRequestQueue;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

public class LocationDetailsFragment extends Fragment {

    private final String GOOGLE_API_KEY = "AIzaSyCc4acsOQV7rnQ92weHYKO14fvL9wkRpKc";
    private String placeId;
    private RequestQueue queue;
    private LocationDetailsResponse placeDetails;
    private PagerAdapter adapter;
    private TextView placeName;
    private TextView placeAddress;
    private TextView placeOpeningHours;
    private TextView placePhone;
    private TextView placeFee;
    private ImageView placeImage;
    private TabLayout tabLayout;
    private ViewPager pager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.map_fragment_location_details, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        queue = SingletonRequestQueue.getInstance(getActivity()).getRequestQueue();

        tabLayout = getView().findViewById(R.id.details_tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Reviews"));
        tabLayout.addTab(tabLayout.newTab().setText("Photos"));
        tabLayout.setTabGravity(tabLayout.GRAVITY_FILL);

        placeName = getView().findViewById(R.id.place_name);
        placeAddress = getView().findViewById(R.id.place_address);
        placeOpeningHours = getView().findViewById(R.id.place_opening_hours);
        placePhone = getView().findViewById(R.id.place_phone_number);
        placeFee = getView().findViewById(R.id.place_entrance_fee);
        placeImage = getView().findViewById(R.id.place_image);

        pager = getView().findViewById(R.id.view_pager);
        adapter = new PagerAdapter(getChildFragmentManager(), tabLayout.getTabCount());
        pager.setAdapter(adapter);
        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        setTabLayout();
        apiCallPlaceDetails();
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
//        placeImage.setImageResource();
        if(placeDetails.getResult().getPhotos() != null) {
            Photos photo = placeDetails.getResult().getPhotos().get(0);
            if (photo != null) {
                String reference = photo.getPhoto_reference();
                String url = "https://maps.googleapis.com/maps/api/place/photo?&maxwidth=200&photoreference=" + reference + "&key=" + GOOGLE_API_KEY;
                Picasso.get().load(url).into(placeImage);
            }
        }
    }

    private void apiCallPlaceDetails() {
        String url = "https://maps.googleapis.com/maps/api/place/details/json?placeid=" + placeId + "&fields=price_level,name,rating,formatted_address,formatted_phone_number,geometry,icon,id,opening_hours,photos,place_id,plus_code,rating,reviews&key=" + GOOGLE_API_KEY;

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

//    @Override
//    public void onFragmentInteraction(Uri uri) {
//        //do something
//    }
}

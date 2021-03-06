package com.example.android.mapproject;

import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener,
        LocationsList.OnLocationPressedListener {

    private final String GOOGLE_API_KEY = "AIzaSyCc4acsOQV7rnQ92weHYKO14fvL9wkRpKc";
    private GoogleMap mMap;
    private LocationsResponse locationsResponse;
    private RequestQueue queue;
    private HashMap<String, ArrayList<String>> places;
    private LatLng taipei;
    private TextView food;
    private TextView shopping;
    private TextView nightlife;
    private TextView history;
    private LocationsList locationsList;
    private LocationDetails locationDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        setPlaces();
        taipei = new LatLng(25.0330, 121.5654);

        queue = SingletonRequestQueue.getInstance(this).getRequestQueue();

        locationsList = new LocationsList();
        locationDetails = new LocationDetails();

        food = findViewById(R.id.food);
        shopping = findViewById(R.id.shopping);
        nightlife = findViewById(R.id.nightlife);
        history = findViewById(R.id.history);

        food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String key = getResources().getResourceEntryName(view.getId());
////                String interests = setMarkers(interest);
                if(mMap != null) {
                    //clear map markers
                    mMap.clear();
                }
                //clear locationslist recycler view
                locationsList.clearData();
                for(String interest : places.get(key)) {
                    String url1 = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=25.0323,121.5735&radius=7700&language=en&type=" + interest + "&fields=rating&key=" + GOOGLE_API_KEY;
                    String url2 = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=25.1368,121.5474&radius=7700&language=en&type=" + interest + "&fields=rating&key=" + GOOGLE_API_KEY;
                    apiCallPlaceOfInterest(interest, url1);
                    apiCallPlaceOfInterest(interest, url2);
                }
            }
        });

        shopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String key = getResources().getResourceEntryName(view.getId());
//                String interests = setMarkers(interest);
                if(mMap != null) {
                    mMap.clear();
                }
                locationsList.clearData();
                for(String interest : places.get(key)) {
                    String url1 = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=25.0323,121.5735&radius=7700&language=en&type=" + interest + "&fields=rating&key=" + GOOGLE_API_KEY;
                    String url2 = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=25.1368,121.5474&radius=7700&language=en&type=" + interest + "&fields=rating&key=" + GOOGLE_API_KEY;
                    apiCallPlaceOfInterest(interest, url1);
                    apiCallPlaceOfInterest(interest, url2);
                }
            }
        });

        nightlife.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String key = getResources().getResourceEntryName(view.getId());
//                String interests = setMarkers(interest);
                if(mMap != null) {
                    mMap.clear();
                }
                locationsList.clearData();
                for(String interest : places.get(key)) {
                    String url1 = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=25.0323,121.5735&radius=7700&language=en&type=" + interest + "&fields=rating&key=" + GOOGLE_API_KEY;
                    String url2 = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=25.1368,121.5474&radius=7700&language=en&type=" + interest + "&fields=rating&key=" + GOOGLE_API_KEY;
                    apiCallPlaceOfInterest(interest, url1);
                    apiCallPlaceOfInterest(interest, url2);
                }
            }
        });

        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String key = getResources().getResourceEntryName(view.getId());
//                String interests = setMarkers(interest);
                locationsList.clearData();
                if(mMap != null) {
                    mMap.clear();
                }
                for(String interest : places.get(key)) {
                    String url1 = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=25.0323,121.5735&radius=7700&language=en&type=" + interest + "&fields=rating&key=" + GOOGLE_API_KEY;
                    String url2 = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=25.1368,121.5474&radius=7700&language=en&type=" + interest + "&fields=rating&key=" + GOOGLE_API_KEY;
                    apiCallPlaceOfInterest(interest, url1);
                    apiCallPlaceOfInterest(interest, url2);
                }
            }
        });

        //call the food onClickListener so that the food locations are displayed by default
        food.performClick();

        //display the locations list fragment in the slide up panel
        getSupportFragmentManager().beginTransaction().replace(R.id.locations_container, locationsList).commit();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng taipei = new LatLng(25.0330, 121.5654);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(taipei, 15));
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
//        if(recycler.getAlpha() == 1) {
//            recycler.setAlpha(0);
//        }
//        else {
//            recycler.setAlpha(1);
//        }

//        getSupportFragmentManager().beginTransaction().replace(R.id.more_info_container, new LocationDetails()).commit();
//        frameLayout.bringToFront();
//        frameLayout.invalidate();

        return false;
    }

    private void updateMap(String response) {
        locationsResponse = new Gson().fromJson(response, LocationsResponse.class);

        for(Results results : locationsResponse.getResults()) {
            LatLng coordinates = results.getGeometry().getLocation().getLatLng();
            mMap.addMarker(new MarkerOptions().position(coordinates).title(results.getName()));
            mMap.setOnMarkerClickListener(this);
        }

//        if(locationsResponse.getNext_page_token() != null) {
//            Handler handler = new Handler();
//            handler.postDelayed(new Runnable() {
//                public void run() {
//                    String token = locationsResponse.getNext_page_token();
//                    apiCallNextToken(token);
//                }
//            }, 1500);
//        }

        //update location list fragment's recycler
        locationsList.updateData(locationsResponse.getResults());
    }

    private void getMoreResults() {
        if(locationsResponse.getNext_page_token() != null) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    String token = locationsResponse.getNext_page_token();
                    Log.i("TOKEN", token);
                    apiCallNextToken(token);
                }
            }, 5000);
        }
    }

    public void apiCallPlaceOfInterest(String interest, String url) {
        String location = Double.toString(taipei.latitude) + "," + Double.toString(taipei.longitude);

//        String url = "https://api.tomtom.com/search/2/search/restaurant.json?key=TSxftk4mgaKIMAQQ1Dt3Yv2lcJklkLxU&lat=25.0330&lon=121.5654&radius=1000&limit=10";
//        String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + location + "&radius=7700&language=en&type=" + interest + "&fields=rating&key=" + GOOGLE_API_KEY;
//        String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?&locationbias=rectangle:25.017845,121.492740|25.065567,121.602192&language=en&type=" + interest + "&fields=rating&key=" + GOOGLE_API_KEY;
//        String url = "https://maps.googleapis.com/maps/api/place/textsearch/json?query=" + interest + "&key=AIzaSyCc4acsOQV7rnQ92weHYKO14fvL9wkRpKc";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("Response", response);
                        updateMap(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Find", "Fail");
            }
        });

        queue.add(stringRequest);
    }

    public void apiCallNextToken(String token) {
        String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?pagetoken=" + token + "&key=" + GOOGLE_API_KEY;

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("Response", response);
                        updateMap(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Find", "Fail");
            }
        });

        queue.add(stringRequest);
    }

    private void setPlaces() {
        places = new HashMap<>();
        places.put("food", new ArrayList<>(Arrays.asList("restaurant", "meal_takeaway", "bakery", "cafe")));
        places.put("shopping", new ArrayList<>(Arrays.asList("clothing_store", "department_store", "shoe_store", "shopping_mall", "store")));
        places.put("nightlife", new ArrayList<>(Arrays.asList("bar", "movie_theater", "night_club")));
        places.put("history", new ArrayList<>(Arrays.asList("library", "museum")));
    }

    @Override
    public void onLocationPressed(String locationId) {
        /*set the location id in the location details' fragment and put the locations
          details fragment in front of the locations list fragment*/
        locationDetails.setPlaceId(locationId);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.locations_container, locationDetails);
        fragmentTransaction.addToBackStack("locationDetailsStack");
        fragmentTransaction.commit();
    }
}

package com.example.android.mapproject;

import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
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

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private final String TOMTOM_API_KEY = "TSxftk4mgaKIMAQQ1Dt3Yv2lcJklkLxU";
    private final String GOOGLE_API_KEY = "AIzaSyCc4acsOQV7rnQ92weHYKO14fvL9wkRpKc";
    private GoogleMap mMap;
    private GoogleApiClient client;
    private LocationsResponse locationsResponse;
    private RequestQueue queue;
    private HashMap<String, ArrayList<String>> places;
    private LatLng taipei;
    private TextView food;
    private TextView shopping;
    private TextView nightlife;
    private TextView history;
    private int count = 0;

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

        food = findViewById(R.id.food);
        shopping = findViewById(R.id.shopping);
        nightlife = findViewById(R.id.nightlife);
        history = findViewById(R.id.history);

        food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String key = getResources().getResourceEntryName(view.getId());
////                String interests = setMarkers(interest);
                for(String interest : places.get(key)) {
                    mMap.clear();
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
                for(String interest : places.get(key)) {
                    mMap.clear();
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
                for(String interest : places.get(key)) {
                    mMap.clear();
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
                for(String interest : places.get(key)) {
                    mMap.clear();
                    String url1 = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=25.0323,121.5735&radius=7700&language=en&type=" + interest + "&fields=rating&key=" + GOOGLE_API_KEY;
                    String url2 = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=25.1368,121.5474&radius=7700&language=en&type=" + interest + "&fields=rating&key=" + GOOGLE_API_KEY;
                    apiCallPlaceOfInterest(interest, url1);
                    apiCallPlaceOfInterest(interest, url2);
                }
            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng taipei = new LatLng(25.0330, 121.5654);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(taipei, 15));
    }

    private void updateMap(String response) {
        locationsResponse = new Gson().fromJson(response, LocationsResponse.class);

        for(Results results : locationsResponse.getResults()) {
            LatLng coordinates = results.getGeometry().getLocation().getLatLng();
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
        queue = SingletonRequestQueue.getInstance(this).getRequestQueue();
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
        queue = SingletonRequestQueue.getInstance(this).getRequestQueue();
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

    private void setMarkers(String key) {
//        for(String place : places.get(key)) {
//            Handler handler = new Handler();
//            handler.postDelayed(new Runnable() {
//                public void run() {
//                    String token = locationsResponse.getNext_page_token();
//                    apiCallPlaceOfInterest(place);
//                }
//            }, 2000);
//        }
    }

    private void setPlaces() {
        places = new HashMap<>();
        places.put("food", new ArrayList<>(Arrays.asList("restaurant", "meal_takeaway", "bakery", "cafe")));
        places.put("shopping", new ArrayList<>(Arrays.asList("clothing_store", "department_store", "shoe_store", "shopping_mall", "store")));
        places.put("nightlife", new ArrayList<>(Arrays.asList("bar", "movie_theater", "night_club")));
        places.put("history", new ArrayList<>(Arrays.asList("library", "museum")));
    }
}

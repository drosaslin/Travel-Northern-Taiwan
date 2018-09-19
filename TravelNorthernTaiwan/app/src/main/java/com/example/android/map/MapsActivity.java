package com.example.android.map;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.android.travelnortherntaiwan.R;
import com.example.android.travelnortherntaiwan.SingletonRequestQueue;
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
        LocationsListFragment.OnLocationPressedListener {

    private final String GOOGLE_API_KEY = "AIzaSyCc4acsOQV7rnQ92weHYKO14fvL9wkRpKc";
    private ArrayList<TextView> activityList;
    private TextView food;
    private TextView shopping;
    private TextView nightlife;
    private TextView history;
    private GoogleMap mMap;
    private LocationsResponse locationsResponse;
    private RequestQueue queue;
    private HashMap<String, ArrayList<String>> places;
    private HashMap<String, ArrayList<ArrayList<String>>> coordinates;
    private HashMap<String, LatLng> regionLocation;
    private HashMap<String, Integer> radius;
    private LocationsListFragment locationsListFragment;
    private LocationDetailsFragment locationDetailsFragment;
    private String region;
    private LatLng regionCoordinates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_activity_main);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //getting the instance of the request queue
        queue = SingletonRequestQueue.getInstance(this).getRequestQueue();

        //setting up all necessary data
        setActivities();
        setCoordinates();
        setRegionLocation();
        setRegion();

        //creating instances of the fragments
        locationsListFragment = new LocationsListFragment();
        locationDetailsFragment = new LocationDetailsFragment();

        //setting up activity's views
        food = findViewById(R.id.food);
        shopping = findViewById(R.id.shopping);
        nightlife = findViewById(R.id.nightlife);
        history = findViewById(R.id.history);

        //storing the views in an list to prevent duplicate code with onclick events
        activityList = new ArrayList<>();
        activityList.add(food);
        activityList.add(shopping);
        activityList.add(nightlife);
        activityList.add(history);

        //setting up on click events
        for(TextView activity : activityList) {
            activity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cleanView();
                    performApiCalls(view);
                }
            });
        }

        //call the food onClickListener so that the food locations are displayed by default
        activityList.get(0).performClick();

        //display the locations list fragment in the slide up panel
        getSupportFragmentManager().beginTransaction().replace(R.id.locations_container, locationsListFragment).commit();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(regionCoordinates , 11));
    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        return false;
    }

    private void updateMap(String response) {
        locationsResponse = new Gson().fromJson(response, LocationsResponse.class);

        //add the markers for every place returned from the api call
        for(Results results : locationsResponse.getResults()) {
            LatLng coordinates = results.getGeometry().getLocation().getLatLng();
            mMap.addMarker(new MarkerOptions().position(coordinates).title(results.getName()));
            mMap.setOnMarkerClickListener(this);
        }

        //update location list fragment's recycler
        locationsListFragment.updateData(locationsResponse.getResults());
    }

//    private void getMoreResults() {
//        if(locationsResponse.getNext_page_token() != null) {
//            Handler handler = new Handler();
//            handler.postDelayed(new Runnable() {
//                public void run() {
//                    String token = locationsResponse.getNext_page_token();
//                    Log.i("TOKEN", token);
//                    apiCallNextToken(token);
//                }
//            }, 5000);
//        }
//    }

    private void performApiCalls(View view) {
        String key = getResources().getResourceEntryName(view.getId());

        //call the google places api for all activities related to the user's choice on its respective region
        for(String interest : places.get(key)) {
            Log.i("REGION", region);
            for(ArrayList<String> list : coordinates.get(region)) {
                String latLng = list.get(0);
                String radius = list.get(1);
                String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + latLng + "&radius=" + radius + "&language=en&type=" + interest + "&fields=rating&key=" + GOOGLE_API_KEY;
                Log.i("URL", url);
                apiCallPlaceOfInterest(interest, url);
            }
        }
    }

    private void apiCallPlaceOfInterest(String interest, String url) {
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

//    private void apiCallNextToken(String token) {
//        String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?pagetoken=" + token + "&key=" + GOOGLE_API_KEY;
//
//        // Request a string response from the provided URL.
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        Log.i("Response", response);
//                        updateMap(response);
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.d("Find", "Fail");
//            }
//        });
//
//        queue.add(stringRequest);
//    }

    private void cleanView() {
        //clear all the markers from the map and items from the locations list

        if(mMap != null) {
            mMap.clear();
        }

        locationsListFragment.clearData();
    }

    private void setActivities() {
        places = new HashMap<>();
        places.put("food", new ArrayList<>(Arrays.asList("restaurant", "meal_takeaway", "bakery", "cafe")));
        places.put("shopping", new ArrayList<>(Arrays.asList("clothing_store", "department_store", "shoe_store", "shopping_mall", "store")));
        places.put("nightlife", new ArrayList<>(Arrays.asList("bar", "movie_theater", "night_club")));
        places.put("history", new ArrayList<>(Arrays.asList("library", "museum")));
    }

    private void setCoordinates() {
        coordinates = new HashMap<>();

        coordinates.put("Taipei", new ArrayList<ArrayList<String>>());
        coordinates.get("Taipei").add(new ArrayList<String>());
        coordinates.get("Taipei").get(0).add("25.0323,121.5735");
        coordinates.get("Taipei").get(0).add("7700");
        coordinates.get("Taipei").add(new ArrayList<String>());
        coordinates.get("Taipei").get(1).add("25.1368,121.5474");
        coordinates.get("Taipei").get(1).add("7700");

        coordinates.put("Keelung", new ArrayList<ArrayList<String>>());
        coordinates.get("Keelung").add(new ArrayList<String>());
        coordinates.get("Keelung").get(0).add("25.1258,121.7176");
        coordinates.get("Keelung").get(0).add("8899");

//        coordinates.get("Taipei").add(new LatLng(25.0323,121.5735));
//        coordinates.get("Taipei").add(new LatLng(25.1368,121.5474));

//        coordinates.put("New Taipei", new ArrayList<LatLng>());
//
//        coordinates.put("Keelung", new ArrayList<LatLng>());
//        coordinates.get("Keelung").add(new LatLng(25.1258,121.7176));

//        coordinatesList.add(new LatLng(25.1258,121.7176));
//        coordinates.put("Keelung", coordinatesList);
//        coordinatesList.clear();
//        coordinates.put("New Taipei", new LatLng(25.016969, 121.462988));
//        coordinates.put("Keelung", new LatLng(25.12825, 121.7419));
//        coordinates.put("Yilan", new LatLng(24.757, 121.753));
//        coordinates.put("Hsinchu", new LatLng(24.80361, 120.96861));
//        coordinates.put("Taoyuan", new LatLng(24.99368, 121.29696));
    }

    private void setRegionLocation() {
        //set the coordinate to which the map will zoom at when the respective region is chosen
        regionLocation = new HashMap<>();
        regionLocation.put("Taipei", new LatLng(25.0330, 121.5654));
        regionLocation.put("New Taipei", new LatLng(25.016969, 121.462988));
        regionLocation.put("Keelung", new LatLng(25.12825, 121.7419));
        regionLocation.put("Yilan", new LatLng(24.757, 121.753));
        regionLocation.put("Hsinchu", new LatLng(24.80361, 120.96861));
        regionLocation.put("Taoyuan", new LatLng(24.99368, 121.29696));
    }

    private void setRegion() {
        Bundle bundle = getIntent().getExtras();

        if(bundle != null) {
            region = bundle.getString("region");
            regionCoordinates = regionLocation.get(region);
        }
        else {
            region = "";
        }
    }

    @Override
    public void onLocationPressed(String locationId) {
        /*set the location id in the location details' fragment and put the locations
          details fragment in front of the locations list fragment*/
        locationDetailsFragment.setPlaceId(locationId);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.locations_container, locationDetailsFragment);
        fragmentTransaction.addToBackStack("locationDetailsStack");
        fragmentTransaction.commit();
    }
}

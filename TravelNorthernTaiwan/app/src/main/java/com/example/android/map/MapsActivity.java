package com.example.android.map;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.android.locations_info.LocationDetailsFragment;
import com.example.android.locations_info.LocationsListFragment;
import com.example.android.locations_info.LocationsResponse;
import com.example.android.travelnortherntaiwan.R;
import com.example.android.travelnortherntaiwan.SingletonRequestQueue;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.gson.Gson;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class MapsActivity extends FragmentActivity implements
        OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener,
        LocationsListFragment.OnLocationPressedListener {

    private final String GOOGLE_API_KEY = "AIzaSyCc4acsOQV7rnQ92weHYKO14fvL9wkRpKc";
    private String tripKey;
    private ArrayList<TextView> activityList;
    private TextView food;
    private TextView shopping;
    private TextView nightlife;
    private TextView history;
    private TabLayout activitiesTab;
    private GoogleMap mMap;
    private LocationsResponse locationsResponse;
    private RequestQueue queue;
    private ClusterManager<MyItem> mClusterManager;
    private HashMap<String, ArrayList<String>> activities;
    private HashMap<String, ArrayList<ArrayList<String>>> coordinates;
    private HashMap<String, LatLng> regionLocation;
    private LocationsListFragment locationsListFragment;
    private String region;
    private LatLng regionCoordinates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_activity_main);

        tripKey = getIntent().getExtras().getString("tripKey");

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

        //setting up activity's views
        activitiesTab = findViewById(R.id.activities_tab);
        activitiesTab.setTabMode(TabLayout.MODE_SCROLLABLE);

        activitiesTab.addTab(activitiesTab.newTab().setText("food"));
        activitiesTab.addTab(activitiesTab.newTab().setText("shopping"));
        activitiesTab.addTab(activitiesTab.newTab().setText("history"));
        activitiesTab.addTab(activitiesTab.newTab().setText("nightlife"));

        activitiesTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                performApiCalls((String)tab.getText());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                cleanView();
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        activitiesTab.getTabAt(1).select();
        activitiesTab.getTabAt(0).select();

        //display the locations list fragment in the slide up panel
        Bundle bundle = new Bundle();
        bundle.putString("tripKey", tripKey);
        locationsListFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.locations_container, locationsListFragment).commit();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(regionCoordinates , 11));
        setUpClusterer();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Toast.makeText(getApplicationContext(), "toast", Toast.LENGTH_SHORT).show();
        return false;
    }

    private void updateMap(String response) {
        locationsResponse = new Gson().fromJson(response, LocationsResponse.class);

        //add the markers for every place returned from the api call
        for(Results results : locationsResponse.getResults()) {
            LatLng coordinates = results.getGeometry().getLocation().getLatLng();
            MyItem item = new MyItem(coordinates.latitude, coordinates.longitude, results.getName(), results.getRating());
            mClusterManager.addItem(item);
        }

        mClusterManager.cluster();
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

    private void performApiCalls(String key) {
        //call the google activities api for all activities related to the user's choice on its respective region
        for(String interest : activities.get(key)) {
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
        if(mClusterManager != null) {
            mClusterManager.clearItems();
        }

        locationsListFragment.clearData();
    }

    private void setActivities() {
        activities = new HashMap<>();
        activities.put("food", new ArrayList<>(Arrays.asList("restaurant", "meal_takeaway", "bakery", "cafe")));
        activities.put("shopping", new ArrayList<>(Arrays.asList("clothing_store", "department_store", "shoe_store", "shopping_mall", "store")));
        activities.put("nightlife", new ArrayList<>(Arrays.asList("bar", "movie_theater", "night_club")));
        activities.put("history", new ArrayList<>(Arrays.asList("library", "museum")));
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

        coordinates.put("Hsinchu", new ArrayList<ArrayList<String>>());
        coordinates.get("Hsinchu").add(new ArrayList<String>());
        coordinates.get("Hsinchu").get(0).add("24.7761,120.9522");
        coordinates.get("Hsinchu").get(0).add("6692");
        coordinates.get("Hsinchu").add(new ArrayList<String>());
        coordinates.get("Hsinchu").get(1).add("24.8935,120.9942");
        coordinates.get("Hsinchu").get(1).add("7515");
        coordinates.get("Hsinchu").add(new ArrayList<String>());
        coordinates.get("Hsinchu").get(2).add("24.7002,121.0560");
        coordinates.get("Hsinchu").get(2).add("7515");
        coordinates.get("Hsinchu").add(new ArrayList<String>());
        coordinates.get("Hsinchu").get(3).add("24.8093,121.1027");
        coordinates.get("Hsinchu").get(3).add("8616");
        coordinates.get("Hsinchu").add(new ArrayList<String>());
        coordinates.get("Hsinchu").get(4).add("24.6017,121.1240");
        coordinates.get("Hsinchu").get(4).add("6692");
        coordinates.get("Hsinchu").add(new ArrayList<String>());
        coordinates.get("Hsinchu").get(5).add("24.7127,121.2201");
        coordinates.get("Hsinchu").get(5).add("8640");
        coordinates.get("Hsinchu").add(new ArrayList<String>());
        coordinates.get("Hsinchu").get(6).add("24.5505,121.2846");
        coordinates.get("Hsinchu").get(6).add("11484");

        coordinates.put("Taoyuan", new ArrayList<ArrayList<String>>());
        coordinates.get("Taoyuan").add(new ArrayList<String>());
        coordinates.get("Taoyuan").get(0).add("24.9838,120.0671");
        coordinates.get("Taoyuan").get(0).add("6269");
        coordinates.get("Taoyuan").add(new ArrayList<String>());
        coordinates.get("Taoyuan").get(1).add("25.0280,121.1543");
        coordinates.get("Taoyuan").get(1).add("6269");
        coordinates.get("Taoyuan").add(new ArrayList<String>());
        coordinates.get("Taoyuan").get(2).add("25.0761,121.2519");
        coordinates.get("Taoyuan").get(2).add("5709");
        coordinates.get("Taoyuan").add(new ArrayList<String>());
        coordinates.get("Taoyuan").get(3).add("25.0274,121.3483");
        coordinates.get("Taoyuan").get(3).add("6269");
        coordinates.get("Taoyuan").add(new ArrayList<String>());
        coordinates.get("Taoyuan").get(4).add("24.9210,121.1584");
        coordinates.get("Taoyuan").get(4).add("6269");
        coordinates.get("Taoyuan").add(new ArrayList<String>());
        coordinates.get("Taoyuan").get(5).add("24.9764,121.2649");
        coordinates.get("Taoyuan").get(5).add("6269");
        coordinates.get("Taoyuan").add(new ArrayList<String>());
        coordinates.get("Taoyuan").get(6).add("24.8481,121.2017");
        coordinates.get("Taoyuan").get(6).add("4324");
        coordinates.get("Taoyuan").add(new ArrayList<String>());
        coordinates.get("Taoyuan").get(7).add("24.8755,121.2734");
        coordinates.get("Taoyuan").get(7).add("5983");
        coordinates.get("Taoyuan").add(new ArrayList<String>());
        coordinates.get("Taoyuan").get(8).add("24.7856,121.3523");
        coordinates.get("Taoyuan").get(8).add("8794");
        coordinates.get("Taoyuan").add(new ArrayList<String>());
        coordinates.get("Taoyuan").get(9).add("24.6667,121.4038");
        coordinates.get("Taoyuan").get(9).add("6920");

        coordinates.put("Yilan", new ArrayList<ArrayList<String>>());
        coordinates.get("Yilan").add(new ArrayList<String>());
        coordinates.get("Yilan").get(0).add("24.4729,121.5203");
        coordinates.get("Yilan").get(0).add("17766");
        coordinates.get("Yilan").add(new ArrayList<String>());
        coordinates.get("Yilan").get(1).add("24.4126,121.7675");
        coordinates.get("Yilan").get(1).add("11706");
        coordinates.get("Yilan").add(new ArrayList<String>());
        coordinates.get("Yilan").get(2).add("24.6013,121.7661");
        coordinates.get("Yilan").get(2).add("11000");
        coordinates.get("Yilan").add(new ArrayList<String>());
        coordinates.get("Yilan").get(3).add("24.6837,121.6199");
        coordinates.get("Yilan").get(3).add("8491");
        coordinates.get("Yilan").add(new ArrayList<String>());
        coordinates.get("Yilan").get(4).add("24.7682,121.7506");
        coordinates.get("Yilan").get(4).add("8794");
        coordinates.get("Yilan").add(new ArrayList<String>());
        coordinates.get("Yilan").get(5).add("24.8643,121.8148");
        coordinates.get("Yilan").get(5).add("4767");
        coordinates.get("Yilan").add(new ArrayList<String>());
        coordinates.get("Yilan").get(6).add("24.9264,121.9148");
        coordinates.get("Yilan").get(6).add("6905");

        coordinates.put("New Taipei", new ArrayList<ArrayList<String>>());
        coordinates.get("New Taipei").add(new ArrayList<String>());
        coordinates.get("New Taipei").get(0).add("245.1191,121.3360");
        coordinates.get("New Taipei").get(0).add("4892");
        coordinates.get("New Taipei").add(new ArrayList<String>());
        coordinates.get("New Taipei").get(1).add("25.1474,121.4222");
        coordinates.get("New Taipei").get(1).add("4559");
        coordinates.get("New Taipei").add(new ArrayList<String>());
        coordinates.get("New Taipei").get(2).add("25.2141,121.4782");
        coordinates.get("New Taipei").get(2).add("5537");
        coordinates.get("New Taipei").add(new ArrayList<String>());
        coordinates.get("New Taipei").get(3).add("25.2566,121.5695");
        coordinates.get("New Taipei").get(3).add("5537");
        coordinates.get("New Taipei").add(new ArrayList<String>());
        coordinates.get("New Taipei").get(4).add("25.1849,121.6333");
        coordinates.get("New Taipei").get(4).add("5537");
        coordinates.get("New Taipei").add(new ArrayList<String>());
        coordinates.get("New Taipei").get(5).add("25.1135,121.6261");
        coordinates.get("New Taipei").get(5).add("2270");
        coordinates.get("New Taipei").add(new ArrayList<String>());
        coordinates.get("New Taipei").get(6).add("25.0424,121.6652");
        coordinates.get("New Taipei").get(6).add("5786");
        coordinates.get("New Taipei").add(new ArrayList<String>());
        coordinates.get("New Taipei").get(7).add("25.02910,121.7591");
        coordinates.get("New Taipei").get(7).add("3754");
        coordinates.get("New Taipei").add(new ArrayList<String>());
        coordinates.get("New Taipei").get(8).add("25.1028,121.8134");
        coordinates.get("New Taipei").get(8).add("5184");
        coordinates.get("New Taipei").add(new ArrayList<String>());
        coordinates.get("New Taipei").get(9).add("25.1139,121.9035");
        coordinates.get("New Taipei").get(9).add("3730");
        coordinates.get("New Taipei").add(new ArrayList<String>());
        coordinates.get("New Taipei").get(10).add("25.0083,121.9745");
        coordinates.get("New Taipei").get(10).add("2966");
        coordinates.get("New Taipei").add(new ArrayList<String>());
        coordinates.get("New Taipei").get(11).add("25.0268,121.8746");
        coordinates.get("New Taipei").get(11).add("7609");
        coordinates.get("New Taipei").add(new ArrayList<String>());
        coordinates.get("New Taipei").get(12).add("24.9465,121.7606");
        coordinates.get("New Taipei").get(12).add("7609");
        coordinates.get("New Taipei").add(new ArrayList<String>());
        coordinates.get("New Taipei").get(13).add("24.8862,121.6353");
        coordinates.get("New Taipei").get(13).add("8491");
        coordinates.get("New Taipei").add(new ArrayList<String>());
        coordinates.get("New Taipei").get(14).add("24.7740,121.5062");
        coordinates.get("New Taipei").get(14).add("9860");
        coordinates.get("New Taipei").add(new ArrayList<String>());
        coordinates.get("New Taipei").get(15).add("24.8955,121.5162");
        coordinates.get("New Taipei").get(15).add("4761");
        coordinates.get("New Taipei").add(new ArrayList<String>());
        coordinates.get("New Taipei").get(16).add("24.9172,121.3984");
        coordinates.get("New Taipei").get(16).add("8491");
        coordinates.get("New Taipei").add(new ArrayList<String>());
        coordinates.get("New Taipei").get(17).add("24.9810,121.4988");
        coordinates.get("New Taipei").get(17).add("5176");
        coordinates.get("New Taipei").add(new ArrayList<String>());
        coordinates.get("New Taipei").get(18).add("25.0020,121.4243");
        coordinates.get("New Taipei").get(18).add("2769");
        coordinates.get("New Taipei").add(new ArrayList<String>());
        coordinates.get("New Taipei").get(19).add("25.0694,121.4408");
        coordinates.get("New Taipei").get(19).add("6076");
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
        LocationDetailsFragment fragment = new LocationDetailsFragment();
        fragment.setPlaceId(locationId);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_bottom, R.anim.exit_from_top, R.anim.enter_from_bottom, R.anim.exit_from_top);
        fragmentTransaction.add(R.id.locations_container, fragment);
        fragmentTransaction.addToBackStack("locationDetailsStack");
        fragmentTransaction.commit();
    }

    private void setUpClusterer() {
        // Initialize the manager with the context and the map.
        // (Activity extends context, so we can pass 'this' in the constructor.)
        mClusterManager = new ClusterManager<>(this, mMap);
        DefaultClusterRenderer<MyItem> renderer = new DefaultClusterRenderer<MyItem>(getApplicationContext(), mMap, mClusterManager);
        renderer.setMinClusterSize(10);
        mClusterManager.setRenderer(renderer);

        // Point the map's listeners at the listeners implemented by the cluster
        // manager.
        mMap.setOnCameraIdleListener(mClusterManager);
        mMap.setOnMarkerClickListener(mClusterManager);;

        mClusterManager.setOnClusterClickListener(new ClusterManager.OnClusterClickListener<MyItem>() {
                    @Override
                    public boolean onClusterClick(final Cluster<MyItem> cluster) {
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                                cluster.getPosition(), (float) Math.floor(mMap
                                        .getCameraPosition().zoom + 1)), 300,
                                null);
                        return true;
                    }
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public class MyItem implements ClusterItem {
        private final LatLng mPosition;
        private final String mTitle;
        private final String mSnippet;

        public MyItem(double lat, double lng, String title, String snippet) {
            mPosition = new LatLng(lat, lng);
            mTitle = title;
            mSnippet = snippet;
        }

        @Override
        public LatLng getPosition() {
            return mPosition;
        }

        @Override
        public String getTitle() {
            return mTitle;
        }

        @Override
        public String getSnippet() {
            return mSnippet;
        }
    }
}

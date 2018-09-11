package com.example.android.travelnortherntaiwan;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class WeatherMain extends android.support.v4.app.Fragment implements Serializable {
    private static final String WEATHER_KEY = "db4321093bdd7e123918dc6fa6e9c1e3";
    private static final String GOOGLE_KEY = "AIzaSyB90nYIuqGFdjpxYP_EGlgacRKYROXyUtc";
    private int requestsFinished;
    private double latitude;
    private double longitude;
    private ProgressBar progressBar;
    private Map<String, String> coordinates;
    private ArrayList<WeatherData> weatherData;
    private RequestQueue queue;
    private RecyclerView recycler;
    private SummaryWeatherAdapter adapter;
    private FusedLocationProviderClient locationProviderClient;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.weather_activity_main, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        requestsFinished = 0;
        latitude = 0.0;
        longitude = 0.0;

//        progressBar = getView().findViewById(R.id.loading_circle);
        recycler = getView().findViewById(R.id.provinces_recycler);
        recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        weatherData = new ArrayList<>();
        coordinates = new LinkedHashMap<>();

        setProvinces();
        populateData();

        progressBar = new ProgressBar(getActivity(), null, android.R.attr.progressBarStyleLarge);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(100,100);
        LinearLayout layout = getView().findViewById(R.id.weather_main);
        layout.addView(progressBar,params);
        progressBar.setVisibility(View.VISIBLE);  //To show ProgressBar
    }

    public void populateData() {
        for(String province : coordinates.keySet()) {
            setWeather(province);
        }
    }

    public void setWeather(String province) {
        queue = com.example.android.travelnortherntaiwan.SingletonRequestQueue.getInstance(getActivity()).getRequestQueue();

        String url = "https://api.darksky.net/forecast/" + WEATHER_KEY + "/" + coordinates.get(province) + "?units=si";
        Log.d("Find", province);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Log.i("Response", response);
                        updateGUI(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Find", "Fail");
            }
        });

        queue.add(stringRequest);
    }

    public void updateGUI(String response) {
        requestsFinished++;
        weatherData.add(new Gson().fromJson(response, WeatherData.class));
        weatherData.get(weatherData.size() - 1).unixToDate();

        if(requestsFinished == coordinates.size()) {
            setProvincesNames();
            progressBar.setVisibility(View.GONE);     // To Hide ProgressBar
//            progressBar.setVisibility(View.GONE);
            adapter = new SummaryWeatherAdapter(weatherData, getActivity());
            recycler.setAdapter(adapter);

            requestsFinished = 0;

//            for(WeatherData weather : weatherData)
//                weather.showData();
        }
    }

    public void setProvincesNames() {
        for(WeatherData weather : weatherData) {
            for(Map.Entry<String, String> entry : coordinates.entrySet()) {
                //set the names of the provinces if the coordinates match
                if(entry.getValue().contains(Double.toString(weather.getLatitude())) &&
                        entry.getValue().contains(Double.toString(weather.getLongitude()))) {
                    Log.i("String", entry.getValue() + "-" + entry.getKey());
                    weather.setCity(entry.getKey());
                }
            }
        }
    }

    public void setProvinces() {
//        String latlng = getDeviceLocation();
//        String city = getCurrentCity();

        //latitude and longitude of the provinces
        coordinates.put("City", "25.2341,122.3213");
        coordinates.put("Taipei", "25.0330,121.5654");
        coordinates.put("New Taipei", "25.0170,121.4628");
        coordinates.put("Keelung", "25.1276,121.7392");
        coordinates.put("Hsinchu", "24.8138,120.9675");
        coordinates.put("Taoyuan", "24.9936,121.3010");
        coordinates.put("Yilan", "24.7021,121.7378");
    }

    private String getDeviceLocation() {
        locationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

        try {
            Task location = locationProviderClient.getLastLocation();
            location.addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if(task.isSuccessful()) {
                        Location currentLocation = (Location) task.getResult();
                        latitude = currentLocation.getLatitude();
                        longitude = currentLocation.getLongitude();
                        Log.d("Location", Double.toString(latitude) + " " + Double.toString(longitude));
                    }
                    else {
                        Log.d("Location", "Unsuccessfull");
                    }
                }
            });
        }
        catch (SecurityException e) {
            Log.d("Location", "Exception");
        }

        return Double.toString(latitude) + "," + Double.toString(longitude);
    }

    private String getCurrentCity() {
        String city;
        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
        List<Address> addresses = null;

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        city = "HI";
        Log.d("Index", Integer.toString(addresses.size()));
        //city = (addresses != null) ? addresses.get(0).getLocality() : "City";

        return city;
    }
}

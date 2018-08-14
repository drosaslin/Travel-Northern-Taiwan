package com.example.android.weatherproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class MainActivity extends AppCompatActivity {
    private static final String WEATHER_KEY = "db4321093bdd7e123918dc6fa6e9c1e3";
    private static final String GOOGLE_KEY = "AIzaSyB90nYIuqGFdjpxYP_EGlgacRKYROXyUtc";
    private int requestsFinished;
    private Map<String, String> coordinates;
    private ArrayList<WeatherData> weatherData;
    private RequestQueue queue;
    private RecyclerView recycler;
    private SummaryWeatherAdapter adapter;

    /*
    Taipei lat=25.0330, long=121.5654
    New Taipei lat=25.0170, long=121.4628
    Keelung lat=25.1276, long=121.7392
    Hsinchu lat=24.8138, long=120.9675
    Taoyuan lat=24.9936, long=121.3010
    Yilan lat=24.7021, long=121.7378
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestsFinished = 0;
        recycler = findViewById(R.id.provinces_recycler);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        queue = Volley.newRequestQueue(this);
        weatherData = new ArrayList<>();
        coordinates = new LinkedHashMap<>();

        setProvinces();
        populateData();
//        callGoogleApi();
    }

//    public void callGoogleApi() {
//        String url ="https://maps.googleapis.com/maps/api/geocode/json?latlng=25.0330,121.5654&key=" + GOOGLE_KEY;
//
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        Log.i("Google", response);
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.d("Google", "Fail");
//            }
//        });
//
//        queue.add(stringRequest);
//    }

    public void populateData() {
        for(String province : coordinates.keySet()) {
            callApi(province);
        }
    }

    public void callApi(String province) {
        String url = "https://api.darksky.net/forecast/" + WEATHER_KEY + "/" + coordinates.get(province) + "?units=si";
//        String url = "http://api.openweathermap.org/data/2.5/forecast?&units=metric&q=" + province + "&appid=" + API_KEY;
        Log.d("Find", province);

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

    public void updateUI(String response) {
        requestsFinished++;
        weatherData.add(new Gson().fromJson(response, WeatherData.class));
        weatherData.get(weatherData.size() - 1).showData();

        if(requestsFinished == coordinates.size()) {
//            orderProvinces();
            adapter = new SummaryWeatherAdapter(weatherData);
            recycler.setAdapter(adapter);
        }
    }

    public void orderProvinces() {
        int length = coordinates.size();
        for(int n = 0; n < length; n++) {
            for(int i = n; i < length; i++) {
                if(weatherData.get(i).getCity().equals(coordinates.get(n))) {
                    Collections.swap(weatherData, n, i);
                    break;
                }
            }
        }
    }

    public void setProvinces() {
        //latitude and longitude of the provinces
        coordinates.put("Taipei", "25.0330,121.5654");
        coordinates.put("New Taipei", "25.0170,121.4628");
        coordinates.put("Keelung", "25.1276,121.7392");
        coordinates.put("Hsinchu", "24.8138,120.9675");
        coordinates.put("Taoyuan", "24.9936,121.3010");
        coordinates.put("Yilan", "24.7021,121.7378");
    }
}

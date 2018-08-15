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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestsFinished = 0;
        recycler = findViewById(R.id.provinces_recycler);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        queue = SingletonRequestQueue.getInstance(this).getRequestQueue();
        weatherData = new ArrayList<>();
        coordinates = new LinkedHashMap<>();

        setProvinces();
        populateData();
    }

    public void populateData() {
        for(String province : coordinates.keySet()) {
            setWeather(province);
        }
    }

    public void setWeather(String province) {
        String url = "https://api.darksky.net/forecast/" + WEATHER_KEY + "/" + coordinates.get(province) + "?units=si";
        Log.d("Find", province);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Log.i("Response", response);
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

        if(requestsFinished == coordinates.size()) {
            setProvincesNames();
            adapter = new SummaryWeatherAdapter(weatherData);
            recycler.setAdapter(adapter);

            for(WeatherData weather : weatherData)
                weather.showData();
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
        //latitude and longitude of the provinces
        coordinates.put("Taipei", "25.0330,121.5654");
        coordinates.put("New Taipei", "25.0170,121.4628");
        coordinates.put("Keelung", "25.1276,121.7392");
        coordinates.put("Hsinchu", "24.8138,120.9675");
        coordinates.put("Taoyuan", "24.9936,121.3010");
        coordinates.put("Yilan", "24.7021,121.7378");
    }
}

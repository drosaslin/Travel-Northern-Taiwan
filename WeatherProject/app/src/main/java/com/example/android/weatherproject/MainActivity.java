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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class MainActivity extends AppCompatActivity {
    private static final String API_KEY = "53db770470fd9d7b80a82d800c41fecc";
    private int requestsFinished;
    private ArrayList<String> provinces;
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
        queue = Volley.newRequestQueue(this);
        weatherData = new ArrayList<>();
        provinces = new ArrayList<>();
        setProvinces();
        populateData();
    }

    public void populateData() {
        for(String province : provinces) {
            callApi(province);
        }
    }

    public void callApi(String province) {
        String url = "http://api.openweathermap.org/data/2.5/forecast?&units=metric&q=" + province + "&appid=" + API_KEY;
        Log.d("Find", province);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
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

        if(requestsFinished == provinces.size()) {
            adapter = new SummaryWeatherAdapter(weatherData);
            recycler.setAdapter(adapter);
        }
    }

    public void setProvinces() {
        provinces.add("Taipei");
        provinces.add("Keelung");
        provinces.add("Hsinchu");
        provinces.add("Taoyuan");
        provinces.add("Yilan");
    }
}

package com.example.android.weather;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Map;

public class Weather extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerAdapter recyclerAdapter;
    private RequestQueue queue;
    private WeatherData weatherData;
    private Button taipeiBtn;
    private Button keelungBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        weatherData = new WeatherData();
        taipeiBtn = findViewById(R.id.taipei_button);
        keelungBtn = findViewById(R.id.keelung_button);
        queue = Volley.newRequestQueue(this);

        taipeiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callApi(v);
            }
        });
        keelungBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callApi(v);
            }
        });
    }

    public void callApi(View button) {
        String city = ((Button) button).getText().toString();
        String url = "http://api.openweathermap.org/data/2.5/forecast?&units=metric&q=" + city + "&appid=53db770470fd9d7b80a82d800c41fecc";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        jsonToObject(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                text.setText("That didn't work!");
            }
        });

        queue.add(stringRequest);
    }

    public void jsonToObject(String data) {
        weatherData = new Gson().fromJson(data, WeatherData.class);
        weatherData.showData();

        recyclerAdapter = new RecyclerAdapter(weatherData.getForecast(), weatherData.getCity().getName());
        recyclerView.setAdapter(recyclerAdapter);
    }
}

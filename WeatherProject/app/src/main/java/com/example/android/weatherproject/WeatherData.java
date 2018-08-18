package com.example.android.weatherproject;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by David Rosas on 8/8/2018.
 */

public class WeatherData {
    private double latitude;
    private double longitude;
    private String city;
    private Forecast currently;
    private HourlyWeather hourly;

    public WeatherData() {
        latitude = 0.0;
        longitude = 0.0;
        city = "City";
        currently = new Forecast();
        hourly = new HourlyWeather();
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getCity() {
        return city;
    }

    public Forecast getCurrently() {
        return currently;
    }

    public HourlyWeather getHourly() {
        return hourly;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setCurrently(Forecast currently) {
        this.currently = currently;
    }

    public void setHourly(HourlyWeather hourly) {
        this.hourly = hourly;
    }

    public void showData() {
        Log.i("Data", "province: " + city);
        Log.i("Data", "coordinates: " + Double.toString(latitude) + ", " + Double.toString(longitude));
        currently.showData();
        //hourly.showData();
    }
}

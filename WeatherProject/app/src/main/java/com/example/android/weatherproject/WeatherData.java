package com.example.android.weatherproject;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by David Rosas on 8/8/2018.
 */

public class WeatherData {
    private ArrayList<Forecast> list;
    private City city;
    private String country;
    private String cod;
    private String message;
    private int cnt;

    public WeatherData() {
        list = new ArrayList<>();
        city = new City();
        country = "";
        cod = "";
        message = "";
        cnt = 0;
    }

    public City getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public String getCod() {
        return cod;
    }

    public String getMessage() {
        return message;
    }

    public int getCnt() {
        return cnt;
    }

    public ArrayList<Forecast> getForecast() {
        return list;
    }

    public void showData() {
        Log.i("Data", "id: " + Integer.toString(city.getId()));
        Log.i("Data", "city: " + city.getName());
        Log.i("Data", "country: " + country);
        Log.i("Data", "cod: " + cod);
        Log.i("Data", "message: " + message);
        Log.i("Data", "cnt: " + Integer.toString(cnt));
        Log.i("Data", "forecast: " + Integer.toString(list.size()));

        for(int n = 0; n < 5; n++) {
            list.get(n).showData();
        }
    }
}

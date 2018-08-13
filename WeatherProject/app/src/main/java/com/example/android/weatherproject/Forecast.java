package com.example.android.weatherproject;

import android.util.Log;

/**
 * Created by David Rosas on 8/8/2018.
 */

public class Forecast {
    private int dt;
    private String dt_txt;
    private Temperature main;
    private Clouds clouds;
    private Wind wind;
    private Sys sys;

    public Forecast() {
        main = new Temperature();
        clouds = new Clouds();
        wind = new Wind();
        sys = new Sys();
        dt = 0;
        dt_txt = "";
    }

    public int getDt() {
        return dt;
    }

    public String getDt_txt() {
        return dt_txt;
    }

    public Temperature getTemperature() {
        return main;
    }

    public Clouds getClouds() {
        return clouds;
    }

    public Wind getWind() {
        return wind;
    }

    public Sys getSys() {
        return sys;
    }

    public void showData() {
        Log.i("Data", "time: " + dt_txt);
        Log.i("Data", "temp: " + main.getTemp());
        Log.i("Data", "min_temp: " + main.getTemp_min());
        Log.i("Data", "max_temp: " + main.getTemp_max());
        Log.i("Data", "humidity: " + main.getHumidity());
    }
}

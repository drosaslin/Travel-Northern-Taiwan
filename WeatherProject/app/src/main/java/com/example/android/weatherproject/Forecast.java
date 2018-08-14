package com.example.android.weatherproject;

import android.util.Log;

/**
 * Created by David Rosas on 8/8/2018.
 */

public class Forecast {
    private int time;
    private float precipProbability;
    private float temperature;
    private String precipType;
    private String summary;
    private String icon;

    public Forecast() {
        time = 0;
        precipProbability = 0;
        temperature = 0;
        precipType = "";
        summary = "";
        icon = "";
    }

    public int getTime() {
        return time;
    }

    public float getPrecipProbability() {
        return precipProbability;
    }

    public float getTemperature() {
        return temperature;
    }

    public String getPrecipType() {
        return precipType;
    }

    public String getSummary() {
        return summary;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public void setPrecipProbability(float precipProbability) {
        this.precipProbability = precipProbability;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    public void setPrecipType(String precipType) {
        this.precipType = precipType;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void showData() {
        Log.i("Data", "time: " + Integer.toString(time));
        Log.i("Data", "precipitation: " + Float.toString(precipProbability));
        Log.i("Data", "temperature: " + Float.toString(temperature));
        Log.i("Data", "precipitation type: " + precipType);
        Log.i("Data", "summary: " + summary);
        Log.i("Data", "icon: " + icon);
    }
}

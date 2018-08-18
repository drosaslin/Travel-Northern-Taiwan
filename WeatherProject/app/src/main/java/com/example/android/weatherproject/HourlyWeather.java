package com.example.android.weatherproject;

import java.util.ArrayList;

class HourlyWeather {
    private String summary;
    private String icon;
    private ArrayList<Forecast> data;

    public HourlyWeather() {
        summary = "";
        icon = "";
        data = new ArrayList<>();
    }

    public String getSummary() {
        return summary;
    }

    public String getIcon() {
        return icon;
    }

    public ArrayList<Forecast> getData() {
        return data;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setData(ArrayList<Forecast> data) {
        this.data = data;
    }

    public void showData() {
        for(Forecast forecast : data) {
            forecast.showData();
        }
    }
}

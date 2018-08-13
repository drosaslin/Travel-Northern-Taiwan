package com.example.android.weatherproject;

/**
 * Created by David Rosas on 8/8/2018.
 */

public class Temperature {
    private double temp;
    private double temp_min;
    private double temp_max;
    private double pressure;
    private double sea_level;
    private double grnd_level;
    private double temp_kf;
    private int humidity;

    public Temperature() {
        temp = 0;
        temp_max = 0;
        temp_min = 0;
        pressure = 0;
        sea_level = 0;
        grnd_level = 0;
        temp_kf = 0;
        humidity = 0;
    }

    public double getTemp() {
        return temp;
    }

    public double getTemp_min() {
        return temp_min;
    }

    public double getTemp_max() {
        return temp_max;
    }

    public double getPressure() {
        return pressure;
    }

    public double getSea_level() {
        return sea_level;
    }

    public double getGrnd_level() {
        return grnd_level;
    }

    public double getTemp_kf() {
        return temp_kf;
    }

    public int getHumidity() {
        return humidity;
    }
}

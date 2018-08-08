package com.example.android.weather;

/**
 * Created by David Rosas on 8/8/2018.
 */

public class Coordinates {
    private double longitude;
    private double latitude;

    public Coordinates() {
        longitude = 0;
        latitude = 0;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLongitude(double newLong) {
        longitude = newLong;
    }

    public void setLatitude(double newLat) {
        latitude = newLat;
    }
}

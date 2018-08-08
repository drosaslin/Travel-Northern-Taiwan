package com.example.android.weather;

/**
 * Created by David Rosas on 8/8/2018.
 */

public class City {
    private int id;
    private String name;
    private Coordinates coord;

    public City() {
        id = 0;
        name = "";
        coord = new Coordinates();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(int newId) {
        id = newId;
    }

    public void setName(String newName) {
        name = newName;
    }
}

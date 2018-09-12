package com.example.android.travelnortherntaiwan;

/**
 * Created by David Rosas on 9/5/2018.
 */

public class Geometry {
    private Location location;

    public Location getLocation ()
    {
        return location;
    }

    public void setLocation (Location location)
    {
        this.location = location;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [viewport = location = "+location+"]";
    }
}

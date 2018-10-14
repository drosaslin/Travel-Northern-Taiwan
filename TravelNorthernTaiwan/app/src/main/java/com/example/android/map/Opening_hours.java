package com.example.android.map;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by David Rosas on 9/5/2018.
 */

public class Opening_hours implements Parcelable {
    private String open_now;

    protected Opening_hours(Parcel in) {
        open_now = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(open_now);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Opening_hours> CREATOR = new Creator<Opening_hours>() {
        @Override
        public Opening_hours createFromParcel(Parcel in) {
            return new Opening_hours(in);
        }

        @Override
        public Opening_hours[] newArray(int size) {
            return new Opening_hours[size];
        }
    };

    public String getOpen_now ()
    {
        return open_now;
    }

    public void setOpen_now (String open_now)
    {
        this.open_now = open_now;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [open_now = "+open_now+"]";
    }

}

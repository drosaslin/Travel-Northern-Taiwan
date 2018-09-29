package com.example.android.locations_info;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by David Rosas on 9/16/2018.
 */

public class LocationDetailsResponse implements Parcelable {
    private Result result;

    private String[] html_attributions;

    private String status;

    protected LocationDetailsResponse(Parcel in) {
        html_attributions = in.createStringArray();
        status = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(html_attributions);
        dest.writeString(status);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<LocationDetailsResponse> CREATOR = new Creator<LocationDetailsResponse>() {
        @Override
        public LocationDetailsResponse createFromParcel(Parcel in) {
            return new LocationDetailsResponse(in);
        }

        @Override
        public LocationDetailsResponse[] newArray(int size) {
            return new LocationDetailsResponse[size];
        }
    };

    public Result getResult ()
    {
        return result;
    }

    public void setResult (Result result)
    {
        this.result = result;
    }

    public String[] getHtml_attributions ()
    {
        return html_attributions;
    }

    public void setHtml_attributions (String[] html_attributions)
    {
        this.html_attributions = html_attributions;
    }

    public String getStatus ()
    {
        return status;
    }

    public void setStatus (String status)
    {
        this.status = status;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [result = "+result+", html_attributions = "+html_attributions+", status = "+status+"]";
    }
}

package com.example.android.trip_organizer;

public class TripBasicInfo {
    String Name;
    String ToDate;
    String FromDate;
    String Region;
    String Key;

    public TripBasicInfo() {
        Name = "";
        ToDate = "";
        FromDate = "";
        Region = "";
        Key = "";
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getToDate() {
        return ToDate;
    }

    public void setToDate(String toDate) {
        ToDate = toDate;
    }

    public String getFromDate() {
        return FromDate;
    }

    public void setFromDate(String fromDate) {
        FromDate = fromDate;
    }

    public String getRegion() {
        return Region;
    }

    public void setRegion(String region) {
        Region = region;
    }

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }
}

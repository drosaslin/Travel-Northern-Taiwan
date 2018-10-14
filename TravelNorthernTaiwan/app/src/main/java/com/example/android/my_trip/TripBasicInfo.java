package com.example.android.my_trip;

import java.io.Serializable;

public class TripBasicInfo implements Serializable {
    String Name;
    String ToDate;
    String FromDate;
    String Region;
    String Key;
    Double Budget;

    public TripBasicInfo() {
        Name = "";
        ToDate = "";
        FromDate = "";
        Region = "";
        Key = "";
        Budget = Double.valueOf(0);
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

    public Double getBudget(){return Budget;}

    public void setBudget(Double budget){Budget = budget;}
}

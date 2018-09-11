package com.example.android.travelnortherntaiwan;

public class TripInformation {
   private String tripName, toDate, fromDate;
   private Double budget;

   TripInformation(){
        tripName = "none";
        toDate = "none";
        fromDate = "none";
        budget = 0.0;
   }

    public String getTripName() {
        return tripName;
    }

    public void setTripName(String tripName) {
        this.tripName = tripName;
    }

    public Double getBudget() {
        return budget;
    }

    public void setBudget(Double budget) {
        this.budget = budget;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }
}

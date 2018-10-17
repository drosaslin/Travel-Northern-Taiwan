package com.example.android.my_trip;

import java.io.Serializable;

public class Task implements Serializable{
    String tripKey;
    String isDone;
    String task;

    public String getIsDone() {
        return isDone;
    }

    public void setIsDone(String isDone) {
        this.isDone = isDone;
    }

    public Task(){
        tripKey = "";
        task = "";
        isDone = "0";
    }

    public String getTripKey() {
        return tripKey;
    }

    public void setTripKey(String tripKey) {
        this.tripKey = tripKey;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

}

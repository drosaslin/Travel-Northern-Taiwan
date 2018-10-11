package com.example.android.travelnortherntaiwan;

/**
 * Created by David Rosas on 10/9/2018.
 */

public class Messenger {
    private static Messenger singletonInstance = null;
    private int count = 0;

    private Messenger() {

    }

    public static Messenger getInstance() {
        if(singletonInstance == null) {
            singletonInstance = new Messenger();
        }

        return singletonInstance;
    }

    public int getCount() {
        return count;
    }

    public void addCount() {
        count++;
    }

    public void setCount(int count) {
        this.count = count;
    }
}

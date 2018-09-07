package com.example.android.mapproject;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by David Rosas on 8/14/2018.
 */

public class SingletonRequestQueue {
    private static SingletonRequestQueue mInstance;
    private RequestQueue requestQueue;

    private SingletonRequestQueue(Context context) {
        requestQueue = Volley.newRequestQueue(context.getApplicationContext());
    }

    //by being synchronized, we are making the thread scheduler non-preemptive(threads can't be interrupted)
    public static synchronized SingletonRequestQueue getInstance(Context context) {
        if(mInstance == null) {
            mInstance = new SingletonRequestQueue(context);
        }

        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        return requestQueue;
    }
}

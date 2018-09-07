package com.example.android.mapproject;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.RequestQueue;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class MainActivity extends AppCompatActivity {

    private TextView latitude;
    private TextView longitude;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private RequestQueue queue;
    private static final int MY_PERMISSION_REQUEST_FINE_LOCATION = 101;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startActivity(new Intent(this, MapsActivity.class));
        finish();
        //initialuse View
//        latitude = (TextView) findViewById(R.id.latitude);
//        longitude = (TextView) findViewById(R.id.longitude);
//
//        locationRequest = new LocationRequest();
//        locationRequest.setInterval(7500); //use a value fo about 10 to 15s for a real app
//        locationRequest.setFastestInterval(5000);
//        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
//
//        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
//                @Override
//                public void onSuccess(Location location) {
//                    if (location != null) {
//                        latitude.setText(String.valueOf(location.getLatitude()));
//                        longitude.setText(String.valueOf(location.getLongitude()));
//                    } else {
//                        latitude.setText("QUACK");
//                        longitude.setText("QUACK");
//                    }
//
//                }
//            });
//        } else {
//            // request permissions
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSION_REQUEST_FINE_LOCATION);
//            }
//        }
//
//        locationCallback = new LocationCallback() {
//            @Override
//            public void onLocationResult(LocationResult locationResult) {
//                super.onLocationResult(locationResult);
//                for (Location location : locationResult.getLocations()) {
//                    //Update UI with location data
//                    if (location != null) {
//                        latitude.setText(String.valueOf(location.getLatitude()));
//                        longitude.setText(String.valueOf(location.getLongitude()));
//                    } else {
//                        latitude.setText("QUACK");
//                        longitude.setText("QUACK");
//                    }
//                }
//            }
//        };
//
//        startLocationUpdates();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case MY_PERMISSION_REQUEST_FINE_LOCATION:

//                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    //permission was granted do nothing and carry on
//
//                } else {
//                    Toast.makeText(getApplicationContext(), "This app requires location permissions to be granted", Toast.LENGTH_SHORT).show();
//                    finish();
//                }

                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        startLocationUpdates();
    }

    private void startLocationUpdates() {
        //if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
        //} else {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSION_REQUEST_FINE_LOCATION);
//            }
//        }

    }

    @Override
    protected void onPause() {
        super.onPause();
//        stopLocationUpdates();
    }

    private void stopLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }
}
package com.example.android.my_trip;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.android.locations_info.LocationDetailsResponse;
import com.example.android.map.Location;
import com.example.android.map.RegionsCoordinates;
import com.example.android.travelnortherntaiwan.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class MyTripMap extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private TripBasicInfo myTrip;
    private LocationDetailsResponse destinationsDetails;
//    private ArrayList<LocationDetailsResponse> destinationsDetails;
    private RegionsCoordinates regionsCoordinates;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_trip_map);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.my_trip_map);
        mapFragment.getMapAsync(this);

        regionsCoordinates = new RegionsCoordinates();

//        Bundle data = getIntent().getExtras();
        destinationsDetails = (LocationDetailsResponse) getIntent().getExtras().get("destinationsDetails");
        myTrip = (TripBasicInfo)getIntent().getExtras().get("basicInfo");
        Log.e("DEBUGING", destinationsDetails.toString());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(regionsCoordinates.getRegionCoordinates(myTrip.getRegion()), 11));
    }
}

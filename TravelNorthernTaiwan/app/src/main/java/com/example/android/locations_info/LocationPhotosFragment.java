package com.example.android.locations_info;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.travelnortherntaiwan.R;

public class LocationPhotosFragment extends Fragment {
    LocationDetailsResponse data;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.map_fragment_location_photos, container, false);
    }

    public void setData(LocationDetailsResponse newData) {
        data = newData;
    }
}

package com.example.android.map;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.travelnortherntaiwan.R;

import java.util.ArrayList;

public class LocationsListFragment extends Fragment {
    private RecyclerView recycler;
    private LocationAdapter adapter;
    private OnLocationPressedListener onLocationPressedListener;

    public interface OnLocationPressedListener {
        public void onLocationPressed(String locationId);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.map_fragment_locations_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new LocationAdapter(new ArrayList<Results>());
        adapter.setListener(this);
        recycler = getView().findViewById(R.id.locations_recycler);
        recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    public void updateData(ArrayList<Results> newData) {
        //add new data to the locations list
        if(adapter != null) {
            adapter.addNewData(newData);
            recycler.setAdapter(adapter);
        }
    }

    public void clearData() {
        //crear the data from the locations list
        if(adapter != null) {
            adapter.clearData();
        }
    }

    public void updateActivity(String locationId) {
        onLocationPressedListener.onLocationPressed(locationId);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Activity activity = (Activity) context;

        try {
            onLocationPressedListener = (OnLocationPressedListener) activity;
        }
        catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + "must override onLocationPressed method");
        }
    }
}

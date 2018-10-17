package com.example.android.locations_info;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.example.android.map.Location;
import com.example.android.map.Results;
import com.example.android.travelnortherntaiwan.R;

import java.util.ArrayList;

public class LocationsListFragment extends Fragment {
    private RecyclerView recycler;
    private LocationListAdapter adapter;
    private OnLocationPressedListener onLocationPressedListener;
    private OnLocationAddedListener onLocationAddedListener;
    private OnLocationDeletedListener onLocationDeletedListener;

    public interface OnLocationPressedListener {
        void onLocationPressed(String locationId, Location location, int position);
    }

    public interface  OnLocationAddedListener {
        void onLocationAdded(Location location);
    }

    public interface OnLocationDeletedListener {
        void onLocationDeleted(Location location);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.map_fragment_locations_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = getArguments();
        String tripKey = (String) bundle.get("tripKey");
        Boolean newTrip = (Boolean) bundle.get("newTrip");

        adapter = new LocationListAdapter(new ArrayList<Results>(), getActivity(), tripKey, newTrip);
        adapter.setListener(this);
        recycler = getView().findViewById(R.id.locations_recycler);
        recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
    }


    public void recyclerItemUpdate(int position) {
        Log.d("TESTING", "2");
        recycler.getLayoutManager().getChildAt(position).findViewById(R.id.add_trip_button);
        adapter.notifyDataSetChanged();
    }

    public void updateData(ArrayList<Results> newData) {
        //add new arraylist data to the locations list
        if(adapter != null) {
            adapter.addNewData(newData);
            recycler.setAdapter(adapter);
        }
    }

    public void updateData(Results newData) {
        //add new data to the locations list
        if(adapter != null) {
            ArrayList<Results> results = new ArrayList<>();
            results.add(newData);
            adapter.addNewData(results);
            recycler.setAdapter(adapter);
        }
    }

    public void clearData() {
        //crear the data from the locations list
        if(adapter != null) {
            adapter.clearData();
        }
    }

    public void updateActivity(String locationId, Location location, int position) {
        onLocationPressedListener.onLocationPressed(locationId, location, position);
    }

    public void updateMap(Location location, boolean add) {
        if(add) {
            onLocationAddedListener.onLocationAdded(location);
        }
        else {
            onLocationDeletedListener.onLocationDeleted(location);
        }
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

        try {
            onLocationAddedListener = (OnLocationAddedListener) activity;
        }
        catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + "must override onLocationPressed method");
        }

        try {
            onLocationDeletedListener = (OnLocationDeletedListener) activity;
        }
        catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + "must override onLocationPressed method");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        recycler = null;
        adapter.finish();
    }
}

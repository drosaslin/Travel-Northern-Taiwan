package com.example.android.trip_organizer;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.example.android.travelnortherntaiwan.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TripsActivity extends android.support.v4.app.Fragment {
    private FloatingActionButton addTripBtn;

    private RecyclerView mRecyclerView;
    private TripsAdapter mAdapter;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference mRootReference;

    private ArrayList<TripBasicInfo> DataList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_trips, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        DataList = new ArrayList<>();

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        mRootReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://travel-northern-taiwan.firebaseio.com/BasicTripInfo");
        mRecyclerView = getView().findViewById(R.id.my_trips_list);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView = getView().findViewById(R.id.my_trips_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        Log.d("ON CREATE", "on create");
        mRootReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                clearCards();
                showData(dataSnapshot);
                mAdapter = new TripsAdapter(DataList, getActivity());
                mRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        addTripBtn = getView().findViewById(R.id.add_trip_button);
        addTripBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ChooseRegionActivity.class));
            }
        });
    }

    private void showData(DataSnapshot dataSnapshot) {
        for(DataSnapshot ds : dataSnapshot.getChildren()){//gets the tripkey
            String key = ds.getKey();
            Log.d("CURRENTUSER", ds.child(key).toString());
            if(ds.child("Author").getValue().toString().equals(currentUser.getUid())){
                TripBasicInfo tInfo = new TripBasicInfo();
                tInfo.setName(ds.child("TripName").getValue().toString());
                tInfo.setKey(ds.getKey());

                if(ds.child("Region").getValue()!= null){
                    tInfo.setRegion(ds.child("Region").getValue().toString());
                }else{
                    tInfo.setRegion("");
                }

                tInfo.setToDate(ds.child("To").getValue().toString());
                tInfo.setFromDate(ds.child("From").getValue().toString());

                Log.d("test", "key " + tInfo.getKey());
                Log.d("test", "name " + tInfo.getName());
                Log.d("test", "regoin " + tInfo.getRegion());
                Log.d("test", "to " + tInfo.getToDate());
                Log.d("test", "from " + tInfo.getFromDate());


                DataList.add(tInfo);
            }

        }

        Log.d("ARRAY SIZE", "size = " + DataList.size());
    }

    private void clearCards(){
        if(mAdapter != null) {
            mAdapter.clearData();
        }
    }
}

package com.example.android.trip_organizer;

import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.travelnortherntaiwan.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.HashMap;

public class ShowInfoActivity extends AppCompatActivity{
    private EditText mTripName;
    private EditText mToDate;
    private EditText mFromDate;
    private EditText mBudget;
    private TextView mRegion;

    private Button manageBudgetBtn;
    private Button saveInfoBtn;
    private FloatingActionButton mapBtn;

    private String currentTripKey;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference mBasicInfoRef;
    private DatabaseReference mItineraryRef;

    private TripBasicInfo infoToDisplay;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_info);
        Log.d("HEREEE", "CRIEITED");
        currentTripKey = getIntent().getExtras().getString("tripKey");
        String refUrl = "https://travel-northern-taiwan.firebaseio.com/";
        Toast.makeText(getApplicationContext(), "CREATED", Toast.LENGTH_SHORT).show();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        //Database references for the basic trip information and the itinerary
        mBasicInfoRef = FirebaseDatabase.getInstance().getReferenceFromUrl(refUrl + "BasicTripInfo");
        mItineraryRef = FirebaseDatabase.getInstance().getReferenceFromUrl(refUrl + "Itinerary");

        mTripName = (EditText)findViewById(R.id.tripName);
        mToDate = (EditText)findViewById(R.id.toDate);
        mFromDate = (EditText)findViewById(R.id.fromDate);
        mBudget = (EditText)findViewById(R.id.budget);
        mRegion = (TextView) findViewById(R.id.regionField);

        manageBudgetBtn = (Button)findViewById(R.id.manageBudget);
        saveInfoBtn = (Button)findViewById(R.id.saveChanges);
        mapBtn = (FloatingActionButton)findViewById(R.id.map_button);

        infoToDisplay = new TripBasicInfo();

        mBasicInfoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //clearCards();
                getBasicInfo(dataSnapshot);
                displayInfo(infoToDisplay);
                /*mAdapter = new TripsAdapter(DataList, getActivity());
                mRecyclerView.setAdapter(mAdapter);*/
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        manageBudgetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ShowInfoActivity.this, BudgetManagerActivity.class));
            }
        });
    }

    private void getBasicInfo(DataSnapshot dataSnapshot) {
        //DataSnapshot ds = dataSnapshot.getChildren();
        /*for(DataSnapshot ds : dataSnapshot.getChildren()){//gets the trip key
            if(ds.child("Author").getValue()!=null && ds.child("Author").getValue().equals(currentUser.getUid())){
                infoToDisplay.setName(ds.child("TripName").getValue().toString());
                infoToDisplay.setKey(ds.getKey());
                infoToDisplay.setRegion(ds.child("Region").getValue().toString());
                infoToDisplay.setToDate(ds.child("To").getValue().toString());
                infoToDisplay.setFromDate(ds.child("From").getValue().toString());
                infoToDisplay.setBudget(Double.parseDouble(ds.child("Budget").getValue().toString()));

                Log.d("test", "key " + infoToDisplay.getKey());
                Log.d("test", "name " + infoToDisplay.getName());
                Log.d("test", "region " + infoToDisplay.getRegion());
                Log.d("test", "to " + infoToDisplay.getToDate());
                Log.d("test", "from " + infoToDisplay.getFromDate());
                Log.d("test", "budget" + infoToDisplay.getBudget().toString());

            }
        }*/
    }

    private void displayInfo(TripBasicInfo infoToDisplay){
        mTripName.setText(infoToDisplay.getName(), TextView.BufferType.EDITABLE);
        mToDate.setText(infoToDisplay.getToDate(), TextView.BufferType.EDITABLE);
        mFromDate.setText(infoToDisplay.getFromDate(), TextView.BufferType.EDITABLE);
        mBudget.setText(infoToDisplay.getBudget().toString(), TextView.BufferType.EDITABLE);
        mRegion.setText(mRegion.getText() + infoToDisplay.getRegion());
    }
}

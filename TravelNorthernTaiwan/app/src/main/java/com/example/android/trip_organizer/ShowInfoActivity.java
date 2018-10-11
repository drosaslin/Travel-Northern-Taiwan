package com.example.android.trip_organizer;

import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.service.autofill.SaveInfo;
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

public class ShowInfoActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{
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

    private boolean isToDateFocused = false;
    private DatePickerDialog fromDatepicker , toDatepicker;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_info);
        currentTripKey = getIntent().getExtras().getString("tripKey");
        String refUrl = "https://travel-northern-taiwan.firebaseio.com/";
        Toast.makeText(getApplicationContext(), "CREATED", Toast.LENGTH_SHORT).show();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        //Database references for the basic trip information and the itinerary
        mBasicInfoRef = FirebaseDatabase.getInstance().getReferenceFromUrl(refUrl + "BasicTripInfo/" + currentTripKey);
        mItineraryRef = FirebaseDatabase.getInstance().getReferenceFromUrl(refUrl + "Itinerary/" + currentTripKey);

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
                GetBasicInfo(dataSnapshot);
                DisplayInfo(infoToDisplay);
                /*mAdapter = new TripsAdapter(DataList, getActivity());
                mRecyclerView.setAdapter(mAdapter);*/
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mFromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isToDateFocused = false;
                DialogFragment fromDatePicker = new DatePickerFragment();
                fromDatePicker.show(getFragmentManager(), "date picker");
            }
        });

        mToDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isToDateFocused = true;
                DialogFragment toDatePicker = new DatePickerFragment();
                toDatePicker.show(getFragmentManager(), "date picker");
            }
        });

        manageBudgetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ShowInfoActivity.this, BudgetManagerActivity.class));
            }
        });

        saveInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SaveInfo();
            }
        });
    }

    private void GetBasicInfo(DataSnapshot ds) {
        infoToDisplay.setKey(ds.getKey());
        infoToDisplay.setName(ds.child("TripName").getValue().toString());
        infoToDisplay.setRegion(ds.child("Region").getValue().toString());
        infoToDisplay.setToDate(ds.child("To").getValue().toString());
        infoToDisplay.setFromDate(ds.child("From").getValue().toString());
        infoToDisplay.setBudget(Double.parseDouble(ds.child("Budget").getValue().toString()));
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        /*Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH,month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        //Month starts from 0, so it has to be added +1 to show the correct date
        if(isToDateFocused){
            mToDate.setText(new StringBuilder().append(year).append("/").append(month+1).append("/").append(dayOfMonth)); //yyyy/MM/dd
        }else{
            mFromDate.setText(new StringBuilder().append(year).append("/").append(month+1).append("/").append(dayOfMonth)); //yyyy/MM/dd
        }*/
    }

    private void DisplayInfo(TripBasicInfo infoToDisplay){
        mTripName.setText(infoToDisplay.getName(), TextView.BufferType.EDITABLE);
        mToDate.setText(infoToDisplay.getToDate(), TextView.BufferType.EDITABLE);
        mFromDate.setText(infoToDisplay.getFromDate(), TextView.BufferType.EDITABLE);
        mBudget.setText(infoToDisplay.getBudget().toString(), TextView.BufferType.EDITABLE);
        mRegion.setText("Region: " + infoToDisplay.getRegion());
    }

    private void SaveInfo(){
        mBasicInfoRef.child("TripName").setValue(mTripName.getText().toString());
        mBasicInfoRef.child("To").setValue(mToDate.getText().toString());
        mBasicInfoRef.child("From").setValue(mFromDate.getText().toString());
        Toast.makeText(getApplicationContext(), "Changes saved", Toast.LENGTH_SHORT).show();
    }

}

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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.android.locations_info.LocationDetailsResponse;
import com.example.android.travelnortherntaiwan.R;
import com.example.android.travelnortherntaiwan.SingletonRequestQueue;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.transferwise.sequencelayout.SequenceLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class ShowInfoActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{
    private final String GOOGLE_API_KEY = "AIzaSyCc4acsOQV7rnQ92weHYKO14fvL9wkRpKc";

    private RequestQueue queue;

    private SequenceLayout sequenceLayout;
    private MyAdapter sequenceAdapter;
    private ArrayList<MyAdapter.MyItem> myItemList;

    private EditText mTripName;
    private EditText mToDate;
    private EditText mFromDate;
    private TextView mRest;
    private TextView mRegion;

    private Button manageBudgetBtn;
    private Button saveInfoBtn;
    private FloatingActionButton mapBtn;

    private String currentTripKey;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference mBasicInfoRef;
    private DatabaseReference mItineraryRef;
    private DatabaseReference mBudgetRef;

    private TripBasicInfo infoToDisplay;

    private boolean isToDateFocused = false;
    private DatePickerDialog fromDatepicker , toDatepicker;

    private float Budget, Accommodation, Food, Shopping, Souvenirs, Tickets, Others;

    int counter = 0;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_info);
        currentTripKey = getIntent().getExtras().getString("tripKey");
        String refUrl = "https://travel-northern-taiwan.firebaseio.com/";
        Toast.makeText(getApplicationContext(), "CREATED", Toast.LENGTH_SHORT).show();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        queue = SingletonRequestQueue.getInstance(getApplicationContext()).getRequestQueue();
        sequenceLayout = findViewById(R.id.sequence_layout);
        myItemList = new ArrayList<>();

        //Database references for the basic trip information and the itinerary
        mBasicInfoRef = FirebaseDatabase.getInstance().getReferenceFromUrl(refUrl + "BasicTripInfo/" + currentTripKey);
        mItineraryRef = FirebaseDatabase.getInstance().getReferenceFromUrl(refUrl + "Itinerary/" + currentTripKey);
        mBudgetRef = FirebaseDatabase.getInstance().getReferenceFromUrl(refUrl + "ExpensesByTrip/" + currentTripKey);

        mTripName = (EditText)findViewById(R.id.tripName);
        mToDate = (EditText)findViewById(R.id.toDate);
        mFromDate = (EditText)findViewById(R.id.fromDate);
        mRest = (TextView) findViewById(R.id.rest);
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

        mBudgetRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                GetBudget(dataSnapshot);
                DisplayRest();
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

        mItineraryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                GetItinerary(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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
                Intent intent = new Intent(ShowInfoActivity.this, BudgetManagerActivity.class);
                intent.putExtra("tripKey", currentTripKey);
                startActivity(intent);
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

    private void GetItinerary(DataSnapshot ds) {
        ArrayList<String> placeIds = new ArrayList<>();
        for(DataSnapshot dataSnapshot : ds.getChildren()){
            if(dataSnapshot.getValue()!=null && !dataSnapshot.getValue().equals("")){
                placeIds.add(dataSnapshot.getValue().toString());
            }
        }

        int size = placeIds.size();
        for(int n = 0; n < size; n++) {
            Log.i("PLACESIDDD", placeIds.get(n));
            apiCallPlaceDetails(placeIds.get(n), size);
        }
    }

    private void GetBudget(DataSnapshot ds) {
        Budget        = Float.parseFloat(ds.child("Budget").getValue().toString());
        Accommodation = Float.parseFloat(ds.child("Accommodation").getValue().toString());
        Food          = Float.parseFloat(ds.child("Food").getValue().toString());
        Shopping      = Float.parseFloat(ds.child("Shopping").getValue().toString());
        Souvenirs     = Float.parseFloat(ds.child("Souvenirs").getValue().toString());
        Tickets       = Float.parseFloat(ds.child("Tickets").getValue().toString());
        Others        = Float.parseFloat(ds.child("Others").getValue().toString());
        Log.d("GetBudget", "budget -> " + Budget);
        Log.d("GetBudget", "Accommodation -> " + Accommodation);
        Log.d("GetBudget", "Food -> " + Food);
        Log.d("GetBudget", "Shopping -> " + Shopping);
        Log.d("GetBudget", "Souvenirs -> " + Souvenirs);
        Log.d("GetBudget", "Tickets -> " + Tickets);
        Log.d("GetBudget", "Others -> " + Others);
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
        mRegion.setText("Region: " + infoToDisplay.getRegion());
    }

    private void DisplayRest(){
        float restF = Budget-(Accommodation + Food + Shopping + Souvenirs + Tickets + Others);
        String rest = Float.toString(restF);
        mRest.setText(rest);
        Log.d("GetBudget", "rest -> " + rest);
    }

    private void SaveInfo(){
        mBasicInfoRef.child("TripName").setValue(mTripName.getText().toString());
        mBasicInfoRef.child("To").setValue(mToDate.getText().toString());
        mBasicInfoRef.child("From").setValue(mFromDate.getText().toString());
        Toast.makeText(getApplicationContext(), "Changes saved", Toast.LENGTH_SHORT).show();
    }

    private void apiCallPlaceDetails(String placeId, final int size) {
        String url = "https://maps.googleapis.com/maps/api/place/details/json?placeid=" + placeId + "&fields=price_level,name,rating,formatted_address,formatted_phone_number,geometry,icon,id,opening_hours,photos,place_id,plus_code,rating,reviews&key=" + GOOGLE_API_KEY;
        Log.d("PLACEULR", url);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("Response", response);
                        addToItineraryList(response, size);
//                        myItemList.add(new MyAdapter.MyItem(true, ))
//                        updateUI(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Find", "Fail");
            }
        });

        queue.add(stringRequest);
    }

    private void addToItineraryList(String response, int size) {
        LocationDetailsResponse placeDetails = new Gson().fromJson(response, LocationDetailsResponse.class);

        myItemList.add(new MyAdapter.MyItem(false, "", placeDetails.getResult().getName(), ""));

        counter++;
        if(counter == size) {
            myItemList.get(myItemList.size() - 1).setActive(true);
            sequenceAdapter = new MyAdapter(myItemList);
            sequenceLayout.setAdapter(sequenceAdapter);
            counter = 0;
        }
    }
}
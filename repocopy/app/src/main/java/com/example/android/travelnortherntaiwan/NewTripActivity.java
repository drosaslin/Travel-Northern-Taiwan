package com.example.android.travelnortherntaiwan;

import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Intent;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.HashMap;

public class NewTripActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    //create function to check if the trip name from the same user already exists
    //date verification or datepicker restriction

    private Button mNextBtn;

    private EditText mTripName;
    private EditText mToDate;
    private EditText mFromDate;
    private EditText mBudget;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference mRootReference;

    private DatePickerDialog fromDatepicker , toDatepicker;

    private boolean isToDateFocused = false;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_trip);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        mTripName = (EditText) findViewById(R.id.tripName);
        mToDate = (EditText) findViewById(R.id.toDate);
        mFromDate = (EditText) findViewById(R.id.fromDate);
        mBudget = (EditText) findViewById(R.id.budget);
        mNextBtn = (Button) findViewById(R.id.next_btn);

        //Get db reference
        mRootReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://travel-northern-taiwan.firebaseio.com");

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

        mNextBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                attemptNewTrip();
            }
        });
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH,month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        if(isToDateFocused){
            mToDate.setText(new StringBuilder().append(year).append("/").append(month).append("/").append(dayOfMonth)); //yyyy/MM/dd
        }else{
            mFromDate.setText(new StringBuilder().append(year).append("/").append(month).append("/").append(dayOfMonth)); //yyyy/MM/dd
        }
    }

    protected void onPause() {
        super.onPause();
        NewTripActivity.this.finish();
    }

    public void attemptNewTrip(){
        String tripName = mTripName.getText().toString().trim();
        String userName = currentUser.getUid();
        String toDate = mToDate.getText().toString().trim();//check datepicker
        String fromDate = mFromDate.getText().toString().trim();
        String budget = mBudget.getText().toString().trim();

        if(!isValidInput(tripName, toDate, fromDate, budget)){
            return;
        }

        //obtaining a randomly generated key for trip
        String currentKey = mRootReference.push().getKey();

        //Declaring hashmaps for BasicTripInfo and ExpensesByTrip
        HashMap<String, String> infoMap =  new HashMap<String, String>();
        HashMap<String, Double> expensesMap =  new HashMap<String, Double>();

        infoMap.put("TripName", tripName);
        infoMap.put("From", fromDate);
        infoMap.put("To", toDate);
        infoMap.put("Author", userName);

        expensesMap.put("Budget",stringToDouble(budget));
        expensesMap.put("Hotel",Double.valueOf(0));
        expensesMap.put("Tickets",Double.valueOf(0));
        expensesMap.put("Souvenirs", Double.valueOf(0));
        expensesMap.put("Food",Double.valueOf(0));
        expensesMap.put("Others",Double.valueOf(0));

        //sending the data to the firebase database
        mRootReference.child("BasicTripInfo").child(currentKey).setValue(infoMap);
        mRootReference.child("ExpensesByTrip").child(currentKey).setValue(expensesMap);


        //move later to another function
        //sending the user to another view and passing the current trip parameter to the view
        Intent selectRegion = new Intent(NewTripActivity.this,HomePageActivity.class);
        selectRegion.putExtra("tripKey", currentKey);
        Log.d("test","key = " + currentKey);
        startActivity(selectRegion);
    }



    //display error if trip name is null or trip already exists
    private boolean isValidInput(String tripName, String toDate, String fromDate, String budget){
        String[] toDateToken = toDate.split("/"), fromDateToken = fromDate.split("/");
        if(TextUtils.isEmpty(tripName)){
            Toast.makeText(getApplicationContext(), "Please enter a name for your trip", Toast.LENGTH_SHORT).show();
            return false;
        }
// else if (!toDate.isEmpty() && (Integer.parseInt(toDateToken[0]) < Integer.parseInt(fromDateToken[0]))){
//            Toast.makeText(getApplicationContext(), "Please check your dates", Toast.LENGTH_SHORT).show();
//            return false;
//        }else if (!toDate.isEmpty() && Integer.parseInt(toDateToken[0]) == Integer.parseInt(fromDateToken[0]) &&
//                Integer.parseInt(toDateToken[1]) > Integer.parseInt(fromDateToken[1])){
//            Toast.makeText(getApplicationContext(), "Please check your dates", Toast.LENGTH_SHORT).show();
//            return false;
//        }else if (!toDate.isEmpty() && Integer.parseInt(toDateToken[0]) == Integer.parseInt(fromDateToken[0]) &&
//                Integer.parseInt(toDateToken[1]) == Integer.parseInt(fromDateToken[1]) &&
//                Integer.parseInt(toDateToken[2]) > Integer.parseInt(fromDateToken[2])){
//            Toast.makeText(getApplicationContext(), "Please check your dates", Toast.LENGTH_SHORT).show();
//            return false;
//        }
        return true;
    }

    private Double stringToDouble(String budgetString){
        Double newValue = Double.valueOf(0);
        if(budgetString != null && budgetString.length() > 0){
            try{
                return Double.parseDouble(budgetString);
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Please enter a valid budget", Toast.LENGTH_SHORT).show();
            }
        }
        return newValue;
    }
}

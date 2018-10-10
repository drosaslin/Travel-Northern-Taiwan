package com.example.android.trip_organizer;

import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.travelnortherntaiwan.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class NewTripActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    //create function to check if the trip name from the same user already exists
    //date verification or datepicker restriction
    //change imageview and textview
    //switch views
    private String currentTripKey;
    private String currentRegion;

    private Button mNextBtn;

    private EditText mTripName;
    private EditText mToDate;
    private EditText mFromDate;
    private EditText mBudget;
    private TextView mRegion;
    private ImageView mRegionImg;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference mRootReference;

    private DatePickerDialog fromDatepicker , toDatepicker;

    private boolean isToDateFocused = false;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        //obtaining the current trip's key
        currentRegion = getIntent().getStringExtra("region");
        currentTripKey = getIntent().getStringExtra("tripKey");
        //String currentRegion = getIntent().getStringExtra("region");

        setContentView(R.layout.activity_new_trip);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        mTripName = (EditText) findViewById(R.id.tripName);
        mToDate = (EditText) findViewById(R.id.toDate);
        mFromDate = (EditText) findViewById(R.id.fromDate);
        mBudget = (EditText) findViewById(R.id.budget);
        mNextBtn = (Button) findViewById(R.id.next_btn);
        mRegionImg = (ImageView) findViewById(R.id.regionImage);
        mRegion = (TextView) findViewById(R.id.regionField);

        //Setting region name in view
        mRegion.setText(mRegion.getText() + currentRegion);

        //fix this
        if(currentRegion.equals("Yilan")){
            mRegionImg.setImageDrawable(getResources().getDrawable(R.drawable.main_page_yilan, getApplicationContext().getTheme()));
        }else if(currentRegion.equals("Taipei")){
            mRegionImg.setImageDrawable(getResources().getDrawable(R.drawable.main_page_taipei, getApplicationContext().getTheme()));
        }else if(currentRegion.equals("New Taipei")){
            mRegionImg.setImageDrawable(getResources().getDrawable(R.drawable.main_page_newtaipei, getApplicationContext().getTheme()));
        }else if(currentRegion.equals("Hsinchu")){
            mRegionImg.setImageDrawable(getResources().getDrawable(R.drawable.main_page_hsinchu, getApplicationContext().getTheme()));
        }else if(currentRegion.equals("Taoyuan")){
            mRegionImg.setImageDrawable(getResources().getDrawable(R.drawable.main_page_taoyuan, getApplicationContext().getTheme()));
        }else{
            mRegionImg.setImageDrawable(getResources().getDrawable(R.drawable.main_page_keelung, getApplicationContext().getTheme()));
        }


        //Get db reference
        String url = "https://travel-northern-taiwan.firebaseio.com/";
        mRootReference = FirebaseDatabase.getInstance().getReferenceFromUrl(url);

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

//    protected void onPause() {
//        super.onPause();
//        NewTripActivity.this.finish();
//    }

    public void attemptNewTrip(){
        String tripName = mTripName.getText().toString().trim();
        String userName = currentUser.getUid();
        String toDate = mToDate.getText().toString().trim();//check datepicker
        String fromDate = mFromDate.getText().toString().trim();
        String budget = mBudget.getText().toString().trim();

        DatabaseReference budgetReference = mRootReference.child("ExpensesByTrip");
        DatabaseReference basicInfoReference = mRootReference.child("BasicTripInfo");

        if(!isValidInput(tripName, toDate, fromDate, budget)){
            return;
        }

        Log.d("CURRENT TRIP", " is " + currentTripKey);
        //add region
        basicInfoReference.child(currentTripKey).child("TripName").setValue(tripName);
        basicInfoReference.child(currentTripKey).child("From").setValue(fromDate);
        basicInfoReference.child(currentTripKey).child("To").setValue(toDate);
        basicInfoReference.child(currentTripKey).child("Budget").setValue(budget);
        basicInfoReference.child(currentTripKey).child("Author").setValue(userName);
        basicInfoReference.child(currentTripKey).child("Region").setValue(currentRegion);

        budgetReference.child(currentTripKey).child("Budget").setValue(stringToDouble(budget));
        budgetReference.child(currentTripKey).child("Accommodation").setValue(Double.valueOf(0));
        budgetReference.child(currentTripKey).child("Tickets").setValue(Double.valueOf(0));
        budgetReference.child(currentTripKey).child("Souvenirs").setValue(Double.valueOf(0));
        budgetReference.child(currentTripKey).child("Food").setValue(Double.valueOf(0));
        budgetReference.child(currentTripKey).child("Others").setValue(Double.valueOf(0));
        budgetReference.child(currentTripKey).child("Shopping").setValue(Double.valueOf(0));

        //move later to another function
        //sending the user to another view and passing the current trip parameter to the view
        Intent mapActivity = new Intent(this,ShowInfoActivity.class);
        mapActivity.putExtra("tripKey", currentTripKey);
        mapActivity.putExtra("region", currentRegion);
        Log.d("test","key = " + currentTripKey);
        startActivity(mapActivity);
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
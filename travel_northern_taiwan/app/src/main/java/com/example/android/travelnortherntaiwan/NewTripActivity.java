package com.example.android.travelnortherntaiwan;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class NewTripActivity extends AppCompatActivity {
    private Button NextBtn;
    private EditText tripName;
    private EditText toDate;
    private EditText fromDate;
    private EditText budget;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference mTripRef = mRef.child("tripInfo");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_trip);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        //tripDB = FirebaseDatabase.getInstance();
        tripName = findViewById(R.id.tripName);
        toDate = findViewById(R.id.toDate);
        fromDate = findViewById(R.id.fromDate);
        budget = findViewById(R.id.budget);
        NextBtn = findViewById(R.id.next_btn);

        NextBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                attemptNewTrip();
            }
        });
    }


//    protected void onStart() {
//        super.onStart();
//        if(tripName.getText().toString() != null) {
//            startActivity(new Intent(this, HomePageActivity.class));
//        }

//    }


    private void attemptNewTrip(){
        final String newTripText = tripName.getText().toString().trim();
        final String toDateText = toDate.getText().toString().trim();
        final String fromDateText = fromDate.getText().toString().trim();
        final String budgetText = budget.getText().toString().trim();
        currentUser = mAuth.getCurrentUser();

        //Add more verification functionality to other fields
        if(TextUtils.isEmpty(newTripText)) {
            //NextBtn.setError("Trip name cannot be empty");
            Toast.makeText(getApplicationContext(), "Trip name cannot be empty", Toast.LENGTH_SHORT).show();
        }else{
            //creating a trip
            //DatabaseReference tripRef = tripDB.getReference("trip");
            mRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String text = dataSnapshot.getValue(String.class);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            NextBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Map<String,Object> taskMap = new HashMap<>();
                    taskMap.put("tripName", newTripText);
                    taskMap.put("toDate",toDateText);
                    taskMap.put("fromDate",fromDateText);
                    taskMap.put("budget",budgetText);
                    //taskMap.put("author", currentUser.getUid());
                    mTripRef.UpdateChildrenAsync(taskMap);
                    //mTripRef.setValue(newTripText);
                }
            });

        }
    }


//    public boolean onNavigationItemSelected(MenuItem item) {
//        // Handle navigation view item clicks here.
//        int id = item.getItemId();
//        Intent intent = null;
//
//        if (id == R.id.nav_home) {
//            intent = new Intent(this, HomePageActivity.class);
//        } else if (id == R.id.nav_plan) {
//            //intent = new Intent(this, HomePageActivity.class);
//        } else if (id == R.id.nav_language) {
//            intent = new Intent(this, LanguageActivity.class);
//        } else if (id == R.id.nav_weather) {
//            //intent = new Intent(this, HomePageActivity.class);
//        }
//        else if (id==R.id.nav_log_out) {
//            intent = new Intent(this, HomePageActivity.class);
//        }else if (id==R.id.nav_about){
//            intent = new Intent(this, HomePageActivity.class);
//        }
//
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);
//        return true;
//    }

}

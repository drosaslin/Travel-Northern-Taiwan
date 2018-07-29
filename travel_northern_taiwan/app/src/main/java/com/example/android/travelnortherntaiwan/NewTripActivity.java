package com.example.android.travelnortherntaiwan;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseUser;

public class NewTripActivity extends AppCompatActivity {
    private Button NextBtn;
    private EditText TripName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_trip);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        NextBtn = findViewById(R.id.next_btn);
        NextBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                attemptNewTrip();
            }
        });
    }


    protected void onStart() {
        super.onStart();
        if(TripName.getText().toString() != null) {
            startActivity(new Intent(this, HomePageActivity.class));
        }
    }


    private void attemptNewTrip(){
        String strTripName = TripName.getText().toString();

        if(strTripName.isEmpty()) {
            NextBtn.setError("Trip name cannot be empty");
            return;
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

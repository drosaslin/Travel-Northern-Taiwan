package com.example.android.travelnortherntaiwan;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class PlanActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference mRootReference;
    private String userId;
    private ListView mlistView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan);

        mlistView = (ListView) findViewById(R.id.listView);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        userId = currentUser.getUid();

        mRootReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://travel-northern-taiwan.firebaseio.com/TripInfoByUser");

        mRootReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                showData(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showData(DataSnapshot dataSnapshot) {
        String tmpChild = "none";
        int x = 0;
        Log.d("test","json: " + dataSnapshot.child(userId).getValue()); //test merge???
//        Log.d("test", "USER " + userId);
        for(DataSnapshot ds : dataSnapshot.getChildren()){//gets the userId
            x++;
            TripInformation tInfo = new TripInformation();
            //tmpChild = ds.child(userId).getValue().toString();
            //tmpChild = ds.child(userId).getValue(TripInformation.class).getTripName();

            //tInfo.setTripName(ds.child(userId).getValue(TripInformation.class).getTripName());
//            tInfo.setBudget(ds.child(userId).child(tmpChild).getValue(TripInformation.class).getBudget());
//            tInfo.setToDate(ds.child(userId).child(tmpChild).getValue(TripInformation.class).getToDate());
//            tInfo.setFromDate(ds.child(userId).child(tmpChild).getValue(TripInformation.class).getFromDate());

            //Log.d("test","name: inloop " + ds.child("gggg").getValue());//usar como referencia
            Log.d("test","2 " + ds.getValue());//tiene todos los trips creados por el usuario
            //Log.d("test", String.valueOf(ds.getChildrenCount()));//cantidad de trips creados
            //Log.d("user #", String.valueOf(x) + " : " + ds.getKey()); //obtiene el nombre de los usuarios
            //Log.d("test","budget:" + tInfo.getBudget());
            //Log.d("test","to:" + tInfo.getToDate());
            //Log.d("test","from:" + tInfo.getFromDate());
        }
    }

    protected void onPause() {
        super.onPause();
        PlanActivity.this.finish();
    }


}

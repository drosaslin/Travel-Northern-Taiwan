package com.example.android.trip_organizer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.android.map.MapsActivity;
import com.example.android.travelnortherntaiwan.Messenger;
import com.example.android.travelnortherntaiwan.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ChooseRegionActivity extends AppCompatActivity {
    CardView taipeiCard, newTaipeiCard, keelungCard, yilanCard, hsinchuCard, taoyuanCard;
    ArrayList<CardView> cards;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference mRootReference;
    private Messenger messenger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        //obtaining the current trip's key
        final String currentTripKey = getIntent().getExtras().getString("tripKey");
        Log.d("test2", currentTripKey);

        messenger = Messenger.getInstance();

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        mRootReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://travel-northern-taiwan.firebaseio.com/BasicTripInfo");

        taipeiCard = findViewById(R.id.taipeiCard);
        newTaipeiCard = findViewById(R.id.newTaipeiCard);
        keelungCard = findViewById(R.id.keelungCard);
        yilanCard = findViewById(R.id.yilanCard);
        hsinchuCard = findViewById(R.id.hsinchuCard);
        taoyuanCard = findViewById(R.id.taoyuanCard);

        cards = new ArrayList<>();
        cards.add(taipeiCard);
        cards.add(newTaipeiCard);
        cards.add(keelungCard);
        cards.add(yilanCard);
        cards.add(hsinchuCard);
        cards.add(taoyuanCard);

        final Intent mapsActivity = new Intent(ChooseRegionActivity.this, MapsActivity.class);

        for(CardView card : cards) {
            card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    messenger.addCount();
                    String tag = (String) view.getTag();
                    mapsActivity.putExtra("region", tag);
                    mapsActivity.putExtra("tripKey", currentTripKey);
                    mRootReference.child(currentTripKey).child("Region").setValue(tag);
                    startActivity(mapsActivity);
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(this, Integer.toString(messenger.getCount()), Toast.LENGTH_SHORT).show();
        if(messenger.getCount() == 3) {
            messenger.addCount();
            finish();
        }
    }
}

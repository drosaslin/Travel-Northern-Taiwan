package com.example.android.trip_organizer;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.android.travelnortherntaiwan.R;

public class BudgetManagerActivity extends AppCompatActivity {

    /*private SectionsStatePagerAdapter mSectionsStatePagerAdapter;
    private ViewPager mViewPager;*/
    private String currentTripKey;
    private Button btnNavSecondActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget_manager);
        getSupportActionBar().setTitle("Budget Manager");
        btnNavSecondActivity = (Button)findViewById(R.id.btnNavSecondActivity);

        currentTripKey = getIntent().getExtras().getString("tripKey");
        Log.d("secondActivity", "currentTripKey -> " + currentTripKey);
        btnNavSecondActivity.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(BudgetManagerActivity.this, SecondActivity.class);
                intent.putExtra("tripKey", currentTripKey);
                startActivity(intent);
            }
        });
        /*mSectionsStatePagerAdapter = new SectionsStatePagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager)findViewById(R.id.container);*/
        //setup the pager
        /*setupViewPager(mViewPager);*/
    }

    /*private void setupViewPager(ViewPager viewPager){
        SectionsStatePagerAdapter adapter = new SectionsStatePagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new Fragment1(), "Fragment1");
        adapter.addFragment(new Fragment2(), "Fragment2");
        adapter.addFragment(new Fragment3(), "Fragment3");
        viewPager.setAdapter(adapter);
    }

    public void setViewPager(int fragmentNumber){
        mViewPager.setCurrentItem(fragmentNumber);
    }*/
}

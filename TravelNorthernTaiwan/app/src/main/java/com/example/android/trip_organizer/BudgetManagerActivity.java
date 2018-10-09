package com.example.android.trip_organizer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.android.travelnortherntaiwan.R;

public class BudgetManagerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget_manager);
        getSupportActionBar().setTitle("Budget Manager");
    }
}

package com.example.android.travelnortherntaiwan;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class HomePageActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    CardView taipeiCard, newTaipeiCard, keelungCard, yilanCard, hsinchuCard, taoyuanCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        taipeiCard = findViewById(R.id.taipeiCard);
        newTaipeiCard = findViewById(R.id.newTaipeiCard);
        keelungCard = findViewById(R.id.keelungCard);
        yilanCard = findViewById(R.id.yilanCard);
        hsinchuCard = findViewById(R.id.hsinchuCard);
        taoyuanCard = findViewById(R.id.taoyuanCard);

        taipeiCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"taipei", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(HomePageActivity.this, PlanActivity.class));
            }
        });

        newTaipeiCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"new taipei", Toast.LENGTH_SHORT).show();
            }
        });

        keelungCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"keelung", Toast.LENGTH_SHORT).show();
            }
        });

        yilanCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"yilan", Toast.LENGTH_SHORT).show();
            }
        });

        hsinchuCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"hsinchu", Toast.LENGTH_SHORT).show();
            }
        });

        taoyuanCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"taoyuan", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent intent = null;

        if (id == R.id.nav_home) {
            intent = new Intent(this, HomePageActivity.class);
        } else if (id == R.id.nav_plan) {
            intent = new Intent(this, NewTripActivity.class);
        } else if (id == R.id.nav_language) {
            intent = new Intent(this, LanguageActivity.class);
        } else if (id == R.id.nav_weather) {
            //intent = new Intent(this, HomePageActivity.class);
        }else if (id==R.id.nav_log_out) {
            intent = new Intent(this, HomePageActivity.class);
        }else if (id==R.id.nav_about){
            intent = new Intent(this, HomePageActivity.class);
        }
        return true;
    }
}

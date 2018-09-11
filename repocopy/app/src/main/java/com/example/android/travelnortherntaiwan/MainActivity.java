package com.example.android.travelnortherntaiwan;

import android.content.Intent;
import android.nfc.Tag;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
//    private Button button, button2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
        currentUser = mAuth.getCurrentUser();
        if(currentUser != null) {
            //open the home page directly if a user is already logged in
            MainActivity.this.finish();
            startActivity(new Intent(this, PlanActivity.class));
        }
    }

    public void openActivity(View button) {
        int id = button.getId();

        if(id == R.id.log_in_btn) {
            startActivity(new Intent(this, LoginActivity.class));
        }
        else if(id == R.id.sign_up_btn) {
            startActivity(new Intent(this, SignupActivity.class));
        }
        else if(id == R.id.language_btn) {
            startActivity(new Intent(this, NewTripActivity.class));
        }
    }
}

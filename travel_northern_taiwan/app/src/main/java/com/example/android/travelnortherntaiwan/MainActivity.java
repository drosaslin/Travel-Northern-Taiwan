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
        Log.d("TESTING", "first test");

        mAuth = FirebaseAuth.getInstance();

//        button = findViewById(R.id.temp);
//        button2 = findViewById(R.id.logout);
//
//        button2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(MainActivity.this, "signed out", Toast.LENGTH_LONG).show();
//                mAuth.signOut();
//            }
//        });
//
//        button.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view) {
//                if(currentUser !=  null)
//                    Toast.makeText(MainActivity.this, "user currently logged in", Toast.LENGTH_LONG).show();
//                else
//                    Toast.makeText(MainActivity.this, "no user logged", Toast.LENGTH_LONG).show();
//            }
//        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        currentUser = mAuth.getCurrentUser();
    }

    public void openActivity(View button) {
        String buttonName = (((Button) button).getText().toString());

        Log.d("TESTING", "second test");
        if(buttonName.equals("Log In")) {
            startActivity(new Intent(this, LoginActivity.class));
        }
        else if(buttonName.equals("Sign Up")) {
            startActivity(new Intent(this, SignupActivity.class));
        }
        else if(buttonName.equals("Language")) {
            startActivity(new Intent(this, LanguageActivity.class));
        }
    }
}

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

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("TESTING", "first test");
    }

    public void openActivity(View button) {
        String buttonName = (((Button) button).getText().toString());

        Log.d("TESTING", "second test");
        if(buttonName.equals("Log In")) {
            startActivity(new Intent(this, HomePageActivity.class));
        }
        else if(buttonName.equals("Sign Up")) {
            startActivity(new Intent(this, SignupActivity.class));
        }
        else if(buttonName.equals("Language")) {
            startActivity(new Intent(this, LanguageActivity.class));
        }
    }
}

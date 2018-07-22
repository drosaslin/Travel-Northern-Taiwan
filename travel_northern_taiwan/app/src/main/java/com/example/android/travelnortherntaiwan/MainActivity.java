package com.example.android.travelnortherntaiwan;

import android.content.Intent;
import android.nfc.Tag;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void openActivity(View button) {
        Intent intent = null;
        String buttonName = (((Button) button).getText().toString());

        if(buttonName.equals("Log In"))
            intent = new Intent(this, HomePageActivity.class);
        else if(buttonName.equals("Sign Up"))
            intent = new Intent(this, SignupActivity.class);
        else if(buttonName.equals("Language"))
            intent = new Intent(this, LanguageActivity.class);

        if(intent != null)
            startActivity(intent);
    }
}

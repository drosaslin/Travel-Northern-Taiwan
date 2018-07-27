package com.example.android.travelnortherntaiwan;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private Button SignInBtn;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Log.e("LOGIN", "part1");

        mAuth = FirebaseAuth.getInstance();

        Log.e("LOGIN", "part2");

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        SignInBtn = findViewById(R.id.sign_in_btn);

        SignInBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                attemptSignIn();
            }
        });

        Log.e("LOGIN", "part3");
    }

    @Override
    protected void onStart() {
        super.onStart();

        //Check if the user is signed in and update UI accordingly
        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null) {
            LoginActivity.this.finish();
            startActivity(new Intent(this, LanguageActivity.class));
        }
    }

    private void attemptSignIn() {
        String emailText = email.getText().toString().trim();
        String passwordText = password.getText().toString().trim();

        if(!isCorrectForm(emailText, passwordText)) {
            return;
        }

        mAuth.signInWithEmailAndPassword(emailText, passwordText)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            LoginActivity.this.finish();
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        }
                        else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private boolean isCorrectForm(String email, String password) {
        if(TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Please enter your email", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Please enter your password", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(password.length() < 6) {
            Toast.makeText(getApplicationContext(), "Password length must be greater than six", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

}


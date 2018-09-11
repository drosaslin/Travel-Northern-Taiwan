package com.example.android.travelnortherntaiwan;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

public class SignupActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private EditText rptPassword;
    private Button signUpBtn;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        rptPassword = findViewById(R.id.rpt_password);
        signUpBtn = findViewById(R.id.sign_up_button);

        signUpBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                attemptSignUp();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        SignupActivity.this.finish();
    }

    public void attemptSignUp() {
        String emailText = email.getText().toString().trim();
        String passwordText = password.getText().toString().trim();
        String rptPassText = rptPassword.getText().toString().trim();

        if(!isCorrectForm(emailText, passwordText, rptPassText)) {
            return;
        }

        mAuth.createUserWithEmailAndPassword(emailText, passwordText)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(SignupActivity.this, "Authentication Success.", Toast.LENGTH_SHORT).show();
                            Log.d("TAG", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            startActivity(new Intent(SignupActivity.this, MainActivity.class));
                        }
                        else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignupActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private boolean isCorrectForm(String email, String password, String rptPassword) {
        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(rptPassword)) {
            Toast.makeText(getApplicationContext(), "Please fill all the fields", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!password.equals(rptPassword)) {
            Toast.makeText(getApplicationContext(), "Passwords don't match", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(password.length() < 6) {
            Toast.makeText(getApplicationContext(), "Password length must be greater than six", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

}


package com.example.android.travelnortherntaiwan;

import android.content.Context;
import android.icu.util.Output;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by David Rosas on 7/20/2018.
 */

public class BackgroundTask extends AsyncTask<String, Void, Void> {
    public Context ctx;

    BackgroundTask(Context newCtx) {
        ctx = newCtx;
    }

    @Override
    protected Void doInBackground(String... params) {
        String username = params[0];
        String email = params[1];
        String password = params[2];
        String signupUrl = "http://127.0.0.1/travelnortherntaiwan/sign_up.php";

        Log.d("FindingError", "settign up data");

        try {
            URL url = new URL(signupUrl);
            HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
            httpConnection.setDoInput(true);
            httpConnection.setRequestMethod("POST");

            OutputStream os = httpConnection.getOutputStream();
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));

            String data = URLEncoder.encode("userName", "UTF-8") + " = " + URLEncoder.encode(username, "UTF-8") + "&" +
                          URLEncoder.encode("email", "UTF-8") + " = " + URLEncoder.encode(email, "UTF-8") + "&" +
                          URLEncoder.encode("password", "UTF-8") + " = " + URLEncoder.encode(password, "UTF-8");

            bw.write(data);
            bw.flush();
            bw.close();
            os.close();

            InputStream is = httpConnection.getInputStream();
            is.close();
            Log.d("FindingError", "Connected");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onProgressUpdate(Void... voids) {
        super.onProgressUpdate();
    }
}

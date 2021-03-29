package ca.mohawk.kolar;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;


import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;



import javax.net.ssl.HttpsURLConnection;

public class GetRequestTask extends AsyncTask<String, Void, String> {

    // https://mkyong.com/java/how-to-send-http-request-getpost-in-java/

    public static String TAG = "==GetRequestTask==";

    String results = "";
    String requestType = "";

    @Override
    protected String doInBackground(String... params) {
        Log.d(TAG, "doInBackground()");

        Log.d(TAG, "Starting Background Task");


        try {

            // Get request information
            String url = params[0];
            requestType = params[1];

            // Setup Http client
            HttpsURLConnection httpClient = (HttpsURLConnection) new URL(url).openConnection();

            // Handle Response
            int responseCode = httpClient.getResponseCode();

            // Log response
            Log.d(TAG, url);
            Log.d(TAG, String.valueOf(responseCode));

            // Read response
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(httpClient.getInputStream()));

            String line;
            StringBuilder response = new StringBuilder();

            while ((line = in.readLine()) != null) {
                response.append(line);
            }

            //print result
            Log.d(TAG, response.toString());
            results = response.toString();

        } catch (IOException ex) {
            Log.d(TAG, "Caught Exception: " + ex);
        }

        return results;
    }


    protected void onPostExecute(String result) {
        Log.d(TAG, "onPostExecute()");

        // Parse result based on request type
        Gson gson = new Gson();

        switch(requestType) {
            case "AllMounts":
                AllMountRequestResult allMountRequestResult = gson.fromJson(result, AllMountRequestResult.class);
                MainActivity.instance.DisplayMounts(allMountRequestResult.mounts);
                break;
            default:
                Log.d(TAG, "Unknown request type.");
                return;
        }
        TokenResult tokenResult = gson.fromJson(result, TokenResult.class);


    }
}

package ca.mohawk.kolar.api;

import android.os.AsyncTask;
import android.util.Log;


import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;



import javax.net.ssl.HttpsURLConnection;

import ca.mohawk.kolar.activity.DetailActivity;
import ca.mohawk.kolar.models.MountDetailResult;
import ca.mohawk.kolar.fragments.MountDatabaseFragment;
import ca.mohawk.kolar.models.AllMountRequestResult;
import ca.mohawk.kolar.models.CreatureDisplayResult;

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

        try {
            if (requestType.equals(GetRequestType.AllMounts.toString())) {
                AllMountRequestResult allMountRequestResult = gson.fromJson(result, AllMountRequestResult.class);
                MountDatabaseFragment.instance.DisplayMounts(allMountRequestResult.mounts, null);
                return;
            }

            if (requestType.equals(GetRequestType.MountDetail.toString())) {
                MountDetailResult mountDetailResult = gson.fromJson(result, MountDetailResult.class);
                DetailActivity.instance.SetMountDetails(mountDetailResult);
                return;
            }

            if (requestType.equals(GetRequestType.CreatureImage.toString())) {
                CreatureDisplayResult creatureDisplayResult = gson.fromJson(result, CreatureDisplayResult.class);

                // Start image download task
                DownloadImageTask dl = new DownloadImageTask();
                dl.execute(creatureDisplayResult.assets[0].value);
            }
        } catch (Exception e) {
            Log.d(TAG, "Exception occurred during PostExecute get task - " + e.getMessage());

            if (requestType.equals(GetRequestType.AllMounts.toString())) {
                MountDatabaseFragment.instance.SetStatus("Error occurred fetching mounts. Check your internet connection and reload app to try again.", false, true);
            }
        }


    }
}
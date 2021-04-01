package ca.mohawk.kolar.api;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;


import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;



import javax.net.ssl.HttpsURLConnection;

import ca.mohawk.kolar.activity.LoadingActivity;
import ca.mohawk.kolar.R;
import ca.mohawk.kolar.models.TokenResult;

/**
 * Class used to create POST request for token from Blizzard API
 */
public class RequestTokenTask extends AsyncTask<Context, Void, String> {
    public static String TAG = "==APITask==";

    // Client ID and Secret saved and only associated with requesting valid token
    final private String client_id = "67abe6658d094665adde7f9d613d9af9";
    final private String client_secret = "8KDAdbzKPYhPSjhbkvOu0g7SxWQqRzJe";

    // Save context in order to save to shared preferences. Only leaked during app startup
    Context context;

    // Results to be saved here
    String results = "";

    /**
     * Makes POST request for token from Blizzard API
     * @param params
     * @return
     */
    @Override
    protected String doInBackground(Context... params) {
        Log.d(TAG, "doInBackground()");

        // Retrieve context from params
        context = params[0];

        try {
            // Setup Http client
            String url = "https://us.battle.net/oauth/token";
            HttpsURLConnection httpClient = (HttpsURLConnection) new URL(url).openConnection();

            // Add headers
            httpClient.setRequestMethod("POST");
            String urlParameters = context.getString(R.string.api_tokenRequest, client_id, client_secret);

            // Set timeouts. Shorter to provide easier means of accessing offline mode if required
            httpClient.setConnectTimeout(10000);
            httpClient.setReadTimeout(10000);

            // Send POST request
            httpClient.setDoOutput(true);
            try (DataOutputStream wr = new DataOutputStream(httpClient.getOutputStream())) {
                wr.writeBytes(urlParameters);
                wr.flush();
            }
            httpClient.connect();

            // Handle Response
            int responseCode = httpClient.getResponseCode();

            // Read response
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(httpClient.getInputStream()));

            String line;
            StringBuilder response = new StringBuilder();

            while ((line = in.readLine()) != null) {
                response.append(line);
            }

            //Print result
            Log.d(TAG, response.toString());
            results = response.toString();

        } catch (IOException ex) {
            Log.d(TAG, "Caught Exception: " + ex);
        }

        // Finally, return results
        return results;
    }

    /**
     * Interprets result from token request
     * @param result
     */
    protected void onPostExecute(String result) {
        Log.d(TAG, "onPostExecute()");

        if (!results.equals("")) {
            // Get token from result
            Gson gson = new Gson();
            TokenResult tokenResult = gson.fromJson(result, TokenResult.class);

            // Store result in shared preferences
            SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.sharedPreferences_file), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(context.getString(R.string.sharedPreferences_token_key), tokenResult.access_token);
            editor.apply();

            // Place app into online mode and transition to main activity
            LoadingActivity.instance.SuccessfulLogin();
        } else {

            // Place app into offline mode due to invalid token
            LoadingActivity.instance.UnsuccessfulLogin();
        }


    }
}

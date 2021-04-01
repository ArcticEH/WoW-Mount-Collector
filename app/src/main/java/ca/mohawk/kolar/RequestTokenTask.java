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

public class RequestTokenTask extends AsyncTask<Context, Void, String> {

    public static String TAG = "==APITask==";

    final private String client_id = "67abe6658d094665adde7f9d613d9af9";
    final private String client_secret = "8KDAdbzKPYhPSjhbkvOu0g7SxWQqRzJe";

    Context context;
    String results = "";

    @Override
    protected String doInBackground(Context... params) {
        Log.d(TAG, "doInBackground()");

        Log.d(TAG, "Starting Background Task");
        context = params[0];

        try {
            // Setup Http client
            String url = "https://us.battle.net/oauth/token";
            HttpsURLConnection httpClient = (HttpsURLConnection) new URL(url).openConnection();

            // Add headers
            httpClient.setRequestMethod("POST");
            String urlParameters = context.getString(R.string.api_tokenRequest, client_id, client_secret);

            httpClient.setConnectTimeout(1000);
            httpClient.setReadTimeout(1000);
            httpClient.getReadTimeout();

            // Send POST request
            httpClient.setDoOutput(true);



            try (DataOutputStream wr = new DataOutputStream(httpClient.getOutputStream())) {
                wr.writeBytes(urlParameters);
                wr.flush();
            }

            httpClient.connect();

            // Handle Response
            int responseCode = httpClient.getResponseCode();

            // Log response
            Log.d(TAG, url);
            Log.d(TAG, urlParameters);
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

        if (!results.equals("")) {
            // Get token from result
            Gson gson = new Gson();
            TokenResult tokenResult = gson.fromJson(result, TokenResult.class);

            // Store result in shared preferences
            SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.sharedPreferences_file), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(context.getString(R.string.sharedPreferences_token_key), tokenResult.access_token);
            editor.apply();

            LoadingActivity.instance.SuccessfulLogin();
        } else {
            LoadingActivity.instance.UnsuccessfulLogin();
        }


    }
}

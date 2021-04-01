package ca.mohawk.kolar.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import ca.mohawk.kolar.R;
import ca.mohawk.kolar.api.RequestTokenTask;
import ca.mohawk.kolar.fragments.MountDatabaseFragment;

/**
 * Loading activity used to request token from Blizzard API and determine if app will be in offload mode
 */
public class LoadingActivity extends AppCompatActivity {
    public static String TAG = "==LoadingActivity==";

    // Instance referred to by token request
    public static LoadingActivity instance;

    /**
     * Requests token from Blizzard API
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        Log.d(TAG, "onCreate()");

        // Set instance when new activity is created
        instance = this;

        // Send out token request
        RequestTokenTask requestTokenTask = new RequestTokenTask();
        requestTokenTask.execute(this);

    }

    /**
     * Functions below separated due to probability of different configuration depending on app
     * state
     */

    /**
     * Called from token request upon successful retrieval of token. Puts app into online mode
     */
    public void SuccessfulLogin() {
        Log.d(TAG, "SuccessfulLogin()");

        // Put app into online mode
        MountDatabaseFragment.offlineMode = false;

        // Start main activity
        Intent myIntent = new Intent(this, NavigationViewActivity.class);
        startActivity(myIntent);

    }

    /**
     * Called from token request upon unsuccessful retrieval of token. Puts app into offline mode.
     */
    public void UnsuccessfulLogin() {
        Log.d(TAG, "UnsuccessfulLogin()");

        // Put app into offline mode
        MountDatabaseFragment.offlineMode = true;

        // Start main activity
        Intent myIntent = new Intent(this, NavigationViewActivity.class);
        startActivity(myIntent);
    }
}
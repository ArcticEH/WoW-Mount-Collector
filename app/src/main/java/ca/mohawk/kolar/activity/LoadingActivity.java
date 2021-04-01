package ca.mohawk.kolar.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import ca.mohawk.kolar.R;
import ca.mohawk.kolar.api.RequestTokenTask;
import ca.mohawk.kolar.fragments.MountDatabaseFragment;

public class LoadingActivity extends AppCompatActivity {
    public static String TAG = "==LoadingActivity==";

    public static LoadingActivity instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        instance = this;

        RequestTokenTask requestTokenTask = new RequestTokenTask();
        requestTokenTask.execute(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public void SuccessfulLogin() {
        // Request token and wait for result before moving on
        MountDatabaseFragment.offlineMode = false;
        Intent myIntent = new Intent(this, NavigationViewActivity.class);
        startActivity(myIntent);

    }

    public void UnsuccessfulLogin() {
        MountDatabaseFragment.offlineMode = true;
        Intent myIntent = new Intent(this, NavigationViewActivity.class);
        startActivity(myIntent);
    }
}
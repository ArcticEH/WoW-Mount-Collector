package ca.mohawk.kolar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import java.util.concurrent.ExecutionException;

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

    }

    public void UnsuccessfulLogin() {
        MountDatabaseFragment.offlineMode = true;
        Intent myIntent = new Intent(this, NavigationViewActivity.class);
        startActivity(myIntent);
    }
}
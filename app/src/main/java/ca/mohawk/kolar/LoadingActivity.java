package ca.mohawk.kolar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.navigation.NavigationView;

import java.util.concurrent.ExecutionException;

public class LoadingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        // Request token and wait for result before moving on
        RequestTokenTask requestTokenTask = new RequestTokenTask();
        requestTokenTask.execute(this);
        try {
            requestTokenTask.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Start main activity once we have a token
        Intent myIntent = new Intent(this, NavigationViewActivity.class);
        startActivity(myIntent);


    }
}
package ca.mohawk.kolar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    public static String TAG = "==MainActivity";
    String token = "";
    public static MainActivity instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set main activity to access from requests
        instance = this;

        // Request list info
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.sharedPreferences_file), Context.MODE_PRIVATE);
        String token = sharedPreferences.getString(getString(R.string.sharedPreferences_token_key), "");
        Log.d(TAG, "Token - " + token);

        GetRequestTask getRequestTask = new GetRequestTask();
        getRequestTask.execute("https://us.api.blizzard.com/data/wow/mount/index?namespace=static-us&locale=en_US&access_token=" +
                token
                , "AllMounts");

    }

    //https://guides.codepath.com/android/Using-an-ArrayAdapter-with-ListView
    public void DisplayMounts(MountResult[] mounts) {
        Log.d(TAG, "DisplayMounts()");

        MountAdapter adapter = new MountAdapter(this, Arrays.asList(mounts));
        ListView mountList = findViewById(R.id.MountListView);
        mountList.setAdapter(adapter);
    }
}
package ca.mohawk.kolar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {

    public static String TAG = "==DetailActivity==";
    public static DetailActivity instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Set instance to be called from api
        instance = this;

        // Enable back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Get mount ID
        int mountId = getIntent().getIntExtra("mountId", 6);

        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.sharedPreferences_file), Context.MODE_PRIVATE);
        String token = sharedPreferences.getString(getString(R.string.sharedPreferences_token_key), "");

        // Make request for detailed mount info
        GetRequestTask getRequestTask = new GetRequestTask();
        getRequestTask.execute("https://us.api.blizzard.com/data/wow/mount/" + Integer.toString(mountId) + "?namespace=static-us&locale=en_US&access_token=" +
                        token
                , "MountDetail");

    }

    public void SetMountDetails(MountDetailResult mountDetailResult) {
        TextView mountNameTextView = findViewById(R.id.MountNameDetailTextView);
        TextView mountDescriptionTextView = findViewById(R.id.MountDescriptionTextView);
        mountNameTextView.setText(mountDetailResult.name);
        mountDescriptionTextView.setText(mountDetailResult.description);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Set in manifest what parent activity is
        NavUtils.navigateUpFromSameTask(this);
        return super.onOptionsItemSelected(item);
    }
}
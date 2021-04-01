package ca.mohawk.kolar.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import ca.mohawk.kolar.R;
import ca.mohawk.kolar.api.GetRequestTask;
import ca.mohawk.kolar.api.GetRequestType;
import ca.mohawk.kolar.database.MyDbHelper;
import ca.mohawk.kolar.models.MountDetailResult;

/**
 * Detail activity used to display specific mount details from Blizzard API
 */
public class DetailActivity extends AppCompatActivity {
    public static String TAG = "==DetailActivity==";

    // Instance referred to by API
    public static DetailActivity instance;

    // Class variable to determine whether to include add or remove button
    private Boolean IsInCollection;

    // Caches
    MyDbHelper mydbhelper = new MyDbHelper(this);
    SharedPreferences sharedPreferences;

    // Mount Info
    private int mountId;
    private String mountName;
    private String mountDescription;

    /**
     * Instantiate views and make mount detail information requests from Blizzard API
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Log.d(TAG, "onCreate()");

        // Set instance to be called from api
        instance = this;

        // Enable back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Get mount ID from intent
        mountId = getIntent().getIntExtra(getString(R.string.detail_intent_mountId), 6);

        // Get token from shared preferences
        sharedPreferences = getSharedPreferences(getString(R.string.sharedPreferences_file), Context.MODE_PRIVATE);
        String token = sharedPreferences.getString(getString(R.string.sharedPreferences_token_key), "");

        // Make request for detailed mount info
        GetRequestTask getRequestTask = new GetRequestTask();
        getRequestTask.execute(getString(R.string.api_mountDetail, Integer.toString(mountId), token)
                , GetRequestType.MountDetail.toString());

        // Check if mount is in database
        SQLiteDatabase db = mydbhelper.getReadableDatabase();
        String[] projection = {MyDbHelper.ID, MyDbHelper.MOUNT_ID};
        Cursor c = db.query(MyDbHelper.MOUNT_TABLE, projection, MyDbHelper.MOUNT_ID + " = " + mountId, null, null, null, null);
        if (c.getCount() > 0) {
            // Mount is in database
            c.moveToFirst();
            IsInCollection = true;
        } else {
            // Mount is not in database
            IsInCollection = false;
        }
        c.close();

        // Style button according to IsInCollection state
        Button collectionButton = findViewById(R.id.CollectionButton);
        if (IsInCollection) {
            collectionButton.setText(R.string.detail_RemoveFromCollection);
        } else {
            collectionButton.setText(R.string.detail_addToCollection);
        }

        // Set on click listener
        collectionButton.setOnClickListener(this::onClickAddOrRemoveFromCollection);
    }

    /**
     * Used to set views associated with mount information
     * @param mountDetailResult Result model associated with this setter
     */
    public void SetMountDetails(MountDetailResult mountDetailResult) {
        Log.d(TAG, "SetMountDetails()");

        // Set text view details from mount detail result
        TextView mountNameTextView = findViewById(R.id.MountNameDetailTextView);
        TextView mountDescriptionTextView = findViewById(R.id.MountDescriptionTextView);
        mountNameTextView.setText(mountDetailResult.name);
        mountDescriptionTextView.setText(mountDetailResult.description);

        // Make new request for image with creature media id received
        String token = sharedPreferences.getString(getString(R.string.sharedPreferences_token_key), "");
        GetRequestTask getRequestTask = new GetRequestTask();
        getRequestTask.execute(getString(R.string.api_creatureDisplay, Integer.toString(mountDetailResult.creature_displays[0].id), token),
                GetRequestType.CreatureImage.toString());
    }

    /**
     * Used to set mount image view
     * @param imageBitMap The bitmap of the mount image
     */
    public void SetMountImage(Bitmap imageBitMap) {
        // Set mount image view to image crated with bitmap
        ImageView imageView = findViewById(R.id.MountDetailImageView);
        imageView.setImageBitmap(imageBitMap);
    }

    /**
     * onClick for when add or remove button is selected
     * @param view
     */
    public void onClickAddOrRemoveFromCollection(View view) {
        Log.d(TAG, "onClickAddOrRemoveFromCollection()");

        // Acquire database for further action
        SQLiteDatabase db = mydbhelper.getReadableDatabase();

        // Check current collection state
        if (IsInCollection) {
            // Remove from collection
            db.delete(MyDbHelper.MOUNT_TABLE, MyDbHelper.MOUNT_ID + " = " + mountId, null);
            IsInCollection = false;
        } else {
            // Add to collection
            ContentValues values = new ContentValues();
            values.put(MyDbHelper.NAME, mountName);
            values.put(MyDbHelper.DESCRIPTION, mountDescription);
            values.put(MyDbHelper.MOUNT_ID, mountId);
            values.put(MyDbHelper.DATE_ADDED, new SimpleDateFormat(getString(R.string.helper_dateFormat)).format(new Date())); // Add current date
            long rowId = db.insert(MyDbHelper.MOUNT_TABLE, null, values);
            IsInCollection = true;
        }

        // Set button styling
        Button collectionButton = findViewById(R.id.CollectionButton);
        if (IsInCollection) {
            collectionButton.setText(R.string.detail_RemoveFromCollection);
        } else {
            collectionButton.setText(R.string.detail_addToCollection);
        }
    }

    /**
     * When options is selected return to parent activity
     * * This prevents the original activity from being reloaded
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected");

        // Utilize default back functionality
        onBackPressed();
        return true;
    }
}
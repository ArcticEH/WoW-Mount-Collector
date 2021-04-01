package ca.mohawk.kolar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DetailActivity extends AppCompatActivity {

    public static String TAG = "==DetailActivity==";
    public static DetailActivity instance;
    private Boolean IsInCollection;


    MyDbHelper mydbhelper = new MyDbHelper(this);

    SharedPreferences sharedPreferences;

    // Mount Info
    private int mountId;
    private String mountName;
    private String mountDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Set instance to be called from api
        instance = this;

        // Enable back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Get mount ID
        mountId = getIntent().getIntExtra(getString(R.string.detail_intent_mountId), 6);

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
            // It is saved
            c.moveToFirst();
            IsInCollection = true;
        } else {
            // It is not saved
            IsInCollection = false;
        }

        c.close();

        // Set button text
        Button collectionButton = findViewById(R.id.CollectionButton);
        if (IsInCollection) {
            collectionButton.setText(R.string.detail_RemoveFromCollection);
        } else {
            collectionButton.setText(R.string.detail_addToCollection);
        }

        // Set on click listener
        collectionButton.setOnClickListener(this::onClickAddOrRemoveFromCollection);
    }

    public void SetMountDetails(MountDetailResult mountDetailResult) {
        // Set text view details from mount detail result
        TextView mountNameTextView = findViewById(R.id.MountNameDetailTextView);
        TextView mountDescriptionTextView = findViewById(R.id.MountDescriptionTextView);
        mountName = mountDetailResult.name;
        mountNameTextView.setText(mountName);
        mountDescription = mountDetailResult.description;
        mountDescriptionTextView.setText(mountDescription);

        // Make new request for image with creature media id received
        String token = sharedPreferences.getString(getString(R.string.sharedPreferences_token_key), "");
        GetRequestTask getRequestTask = new GetRequestTask();
        getRequestTask.execute(getString(R.string.api_creatureDisplay, Integer.toString(mountDetailResult.creature_displays[0].id), token),
                GetRequestType.CreatureImage.toString());
    }

    public void SetMountImage(Bitmap imageBitMap) {
        ImageView imageView = findViewById(R.id.MountDetailImageView);
        imageView.setImageBitmap(imageBitMap);
    }

    public void onClickAddOrRemoveFromCollection(View view) {
        SQLiteDatabase db = mydbhelper.getReadableDatabase();

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
            values.put(MyDbHelper.DATE_ADDED, new SimpleDateFormat("dd-MM-yyyy").format(new Date())); // Add current date

            long rowId = db.insert(MyDbHelper.MOUNT_TABLE, null, values);

            IsInCollection = true;
        }

        // Set button text
        Button collectionButton = findViewById(R.id.CollectionButton);
        if (IsInCollection) {
            collectionButton.setText(R.string.detail_RemoveFromCollection);
        } else {
            collectionButton.setText(R.string.detail_addToCollection);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Set in manifest what parent activity is
        onBackPressed();
        return true;
        //return super.onOptionsItemSelected(item);

    }
}
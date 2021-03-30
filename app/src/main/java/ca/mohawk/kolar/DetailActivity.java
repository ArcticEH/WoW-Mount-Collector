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
    private Bitmap mountImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Set instance to be called from api
        instance = this;

        // Enable back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Get mount ID
        mountId = getIntent().getIntExtra("mountId", 6);

        Boolean useApi = getIntent().getBooleanExtra("useAPI", false);
        if (useApi) {
            sharedPreferences = getSharedPreferences(getString(R.string.sharedPreferences_file), Context.MODE_PRIVATE);
            String token = sharedPreferences.getString(getString(R.string.sharedPreferences_token_key), "");

            // Make request for detailed mount info
            GetRequestTask getRequestTask = new GetRequestTask();
            getRequestTask.execute("https://us.api.blizzard.com/data/wow/mount/" + Integer.toString(mountId) + "?namespace=static-us&locale=en_US&access_token=" +
                            token
                    , "MountDetail");
        }

        // Check if mount is in database
        // TODO: Check if in database to determine what button to display
        SQLiteDatabase db = mydbhelper.getReadableDatabase();
        String[] projection = { mydbhelper.ID, mydbhelper.NAME, mydbhelper.MOUNT_ID, mydbhelper.IMAGE};

        Cursor c = db.query(MyDbHelper.MOUNT_TABLE, projection, mydbhelper.MOUNT_ID + " = " + mountId, null, null, null, null);
        if (c.getCount() > 0) {
            // It is saved
            c.moveToFirst();
            IsInCollection = true;
        } else {
            // It is not saved
            IsInCollection = false;
        }

        // Set button text
        Button collectionButton = findViewById(R.id.CollectionButton);
        if (IsInCollection) {
            collectionButton.setText("Remove from Collection");
        } else {
            collectionButton.setText("Add to Collection");
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
        getRequestTask.execute("https://us.api.blizzard.com/data/wow/media/creature-display/" + mountDetailResult.creature_displays[0].id + "?namespace=static-us&locale=en_US&access_token="
                + token,
                "CreatureImage");
    }

    public void SetMountImage(Bitmap imageBitMap) {
        ImageView imageView = findViewById(R.id.MountDetailImageView);
        imageView.setImageBitmap(imageBitMap);
        mountImage = imageBitMap;

        // TODO: This is where the add to collection button should actually be enabled for proper saving
    }

    public void onClickAddOrRemoveFromCollection(View view) {
        SQLiteDatabase db = mydbhelper.getReadableDatabase();

        if (IsInCollection) {
            // Remove from collection
            db.delete(mydbhelper.MOUNT_TABLE, mydbhelper.MOUNT_ID + " = " + mountId, null);
            IsInCollection = false;
        } else {
            // Add to collection
            ContentValues values = new ContentValues();
            values.put(mydbhelper.NAME, mountName);
            values.put(mydbhelper.DESCRIPTION, mountDescription);
            values.put(mydbhelper.MOUNT_ID, mountId);
            values.put(mydbhelper.IMAGE, String.valueOf(mountImage));

            long rowId = db.insert(mydbhelper.MOUNT_TABLE, null, values);
            Log.d(TAG, "Mount Added - " + rowId);

            IsInCollection = true;
        }

        // Set button text
        Button collectionButton = findViewById(R.id.CollectionButton);
        if (IsInCollection) {
            collectionButton.setText("Remove from Collection");
        } else {
            collectionButton.setText("Add to Collection");
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
package ca.mohawk.kolar.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * DbHelper class used to create SqlLite database and provide easy access to db information
 */
public class MyDbHelper extends SQLiteOpenHelper {
    public static final String TAG = "==MyDbHelper==";

    // Database file and table
    public static final String DATABASE_FILE_NAME = "CollectionDatabase.db"; // Hard coded as this is only database used in app
    public static final int DATABASE_VERSION = 1;
    public static final String MOUNT_TABLE = "MountCollection";
    public static final String ID = "_id";
    public static final String MOUNT_ID = "mount_id";
    public static final String DESCRIPTION = "mount_description";
    public static final String DATE_ADDED = "date_added";
    public static final String NAME = "name";

    // Create SQL script
    private static final String SQL_CREATE =
            "CREATE TABLE " + MOUNT_TABLE + " ( " + ID + " INTEGER PRIMARY KEY, " +
                    MOUNT_ID + " INTEGER, " + DATE_ADDED + " TEXT, " + NAME + " TEXT, " + DESCRIPTION + " TEXT) ";

    /**
     * Constructor
     */
    public MyDbHelper(Context context) {
        super(context, DATABASE_FILE_NAME, null, DATABASE_VERSION);
        Log.d(TAG, "MyDbHelper()");
    }

    /**
     * Creates the SQLite database using create script above
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "onCreate() " + SQL_CREATE);

        // Execute creation script
        db.execSQL(SQL_CREATE);
    }

    /**
     * Required for interface but currently unused
     * Used for migrations if in production
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Nothing implemented
    }
}
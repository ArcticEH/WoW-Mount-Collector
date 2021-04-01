package ca.mohawk.kolar;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MyDbHelper extends SQLiteOpenHelper {
    public static final String TAG = "==MyDbHelper==";

    /** collection of DataBase field names **/
    public static final String DATABASE_FILE_NAME = "CollectionDatabase.db";
    public static final int DATABASE_VERSION = 1;
    public static final String MOUNT_TABLE = "MountCollection";
    public static final String ID = "_id";
    public static final String MOUNT_ID = "mount_id";
    public static final String DESCRIPTION = "mount_description";
    public static final String DATE_ADDED = "date_added";
    public static final String NAME = "name";

    private static final String SQL_CREATE =
            "CREATE TABLE " + MOUNT_TABLE + " ( " + ID + " INTEGER PRIMARY KEY, " +
                    MOUNT_ID + " INTEGER, " + DATE_ADDED + " TEXT, " + NAME + " TEXT, " + DESCRIPTION + " TEXT) ";

    public MyDbHelper(Context context) {
        super(context, DATABASE_FILE_NAME, null, DATABASE_VERSION);
        Log.d(TAG, "constructor");

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "onCreate " + SQL_CREATE);
        db.execSQL(SQL_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Nothing implemented
    }
}
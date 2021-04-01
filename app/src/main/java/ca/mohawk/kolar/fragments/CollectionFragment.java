package ca.mohawk.kolar.fragments;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import ca.mohawk.kolar.adapters.CollectionAdapter;
import ca.mohawk.kolar.database.MyDbHelper;
import ca.mohawk.kolar.R;
import ca.mohawk.kolar.models.CollectionModel;

/**
 * Collection fragment used to display mont collection in a list view and provide easy means
 * of removing mounts
 */
public class CollectionFragment extends Fragment {
    public static String TAG = "==CollectionFragment==";

    // Cache
    MyDbHelper mydbhelper;

    /**
     * constructor
     */
    public CollectionFragment() {
        Log.d(TAG, "CollectionFragment()");
        // Required empty public constructor
    }


    /**
     * Configure listview by accessing mount information from sqlite database
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView()");

        // Inflate the view
        View view = inflater.inflate(R.layout.fragment_collection, container, false);

        // insantiate mydbhelper
        mydbhelper = new MyDbHelper(getContext());

        // Get database instance
        SQLiteDatabase db = mydbhelper.getReadableDatabase();

        // Define fields we want
        String[] projection = {mydbhelper.ID, mydbhelper.NAME, mydbhelper.MOUNT_ID, mydbhelper.DATE_ADDED};

        // Get data and return cursor
        ArrayList<CollectionModel> collectionModels = new ArrayList<CollectionModel>();
        Cursor myCursor = db.query(mydbhelper.MOUNT_TABLE, projection, null, null, null, null, null);
        for(myCursor.moveToFirst(); !myCursor.isAfterLast(); myCursor.moveToNext()) {
            CollectionModel collectionModel = new CollectionModel();
            collectionModel.MountId = myCursor.getInt(myCursor.getColumnIndex(mydbhelper.MOUNT_ID));
            collectionModel.name = myCursor.getString(myCursor.getColumnIndex(mydbhelper.NAME));
            collectionModel.Date = myCursor.getString(myCursor.getColumnIndex(mydbhelper.DATE_ADDED));
            collectionModels.add(collectionModel);
        }

        // Get listview to set values
        ListView listView = view.findViewById(R.id.MountListView);
        listView.setDividerHeight(7);
        listView.setHeaderDividersEnabled(true);

        // Set empty text to display
        TextView textView = view.findViewById(R.id.ListViewStatusTextView);
        listView.setEmptyView(textView);

        // Set adapter
        ListAdapter adapter = new CollectionAdapter(getContext(), collectionModels);
        listView.setAdapter(adapter);

        // Finally, return view
        return view;
    }
}
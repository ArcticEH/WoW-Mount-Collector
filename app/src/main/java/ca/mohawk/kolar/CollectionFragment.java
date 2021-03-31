package ca.mohawk.kolar;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CollectionFragment extends Fragment {

    MyDbHelper mydbhelper;

    public CollectionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_collection, container, false);

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
        listView.setDividerHeight(10);
        listView.setHeaderDividersEnabled(true);

        // Set empty text to display
        TextView textView = view.findViewById(R.id.ListViewStatusTextView);
        listView.setEmptyView(textView);

        // Set adapter
        ListAdapter adapter = new CollectionAdapter(getContext(), collectionModels);
        listView.setAdapter(adapter);


        return view;
    }
}
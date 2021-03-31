package ca.mohawk.kolar;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class CollectionAdapter extends ArrayAdapter<CollectionModel> {

    public static String TAG = "==CollectionAdapter==";

    List list;


    public CollectionAdapter(@NonNull Context context, @NonNull List<CollectionModel> objects) {
        super(context, 0, objects);

        list = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Get data for this position
        CollectionModel collectionModel = getItem(position);

        // Check if view is already being reused, otherwise inflate
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.mount_collection_layout, parent, false);
        }

        // Lookup view for mount info
        TextView mountId = convertView.findViewById(R.id.IdTextView);
        TextView mountName = convertView.findViewById(R.id.MountNameTextView);

        // Set text
        mountId.setText(Integer.toString(collectionModel.MountId));
        mountName.setText(collectionModel.name);



        // Set on click for delete item
        convertView.findViewById(R.id.DeleteButton).setOnClickListener(v -> {
            Log.d(TAG, String.valueOf(collectionModel.MountId));
            // Remove from collection
            // Get database instance
            MyDbHelper mydbhelper = new MyDbHelper(getContext());
            SQLiteDatabase db = mydbhelper.getReadableDatabase();
            db.delete(mydbhelper.MOUNT_TABLE, mydbhelper.MOUNT_ID + " = " + collectionModel.MountId, null);

            list.remove(position);
            notifyDataSetChanged();
        });

        return convertView;
    }


}

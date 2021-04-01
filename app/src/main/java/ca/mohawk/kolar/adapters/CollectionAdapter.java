package ca.mohawk.kolar.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import ca.mohawk.kolar.R;
import ca.mohawk.kolar.database.MyDbHelper;
import ca.mohawk.kolar.models.CollectionModel;

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
        TextView dateAdded = convertView.findViewById(R.id.DateAddedTextView);

        // Set text
        mountId.setText(Integer.toString(collectionModel.MountId));
        mountName.setText(collectionModel.name);

        dateAdded.setText(getContext().getString(R.string.collectionAdapter_dateAdded, collectionModel.Date));

        // Set on click for delete item
        convertView.findViewById(R.id.DeleteButton).setOnClickListener(v -> {
            Log.d(TAG, String.valueOf(collectionModel.MountId));

            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case DialogInterface.BUTTON_POSITIVE:
                            // Get database instance
                            MyDbHelper mydbhelper = new MyDbHelper(getContext());
                            SQLiteDatabase db = mydbhelper.getReadableDatabase();

                            // Remove from collection
                            db.delete(mydbhelper.MOUNT_TABLE, mydbhelper.MOUNT_ID + " = " + collectionModel.MountId, null);

                            // Remove from list and notify of refresh
                            list.remove(position);
                            notifyDataSetChanged();
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            //No button clicked
                            break;
                    }
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage(R.string.collectionAdapter_deleteAlert).setPositiveButton(R.string.collectionAdapter_yes, dialogClickListener)
                    .setNegativeButton(R.string.collectionAdapter_no, dialogClickListener).show();




        });

        return convertView;
    }


}
package ca.mohawk.kolar.adapters;

import android.content.Context;
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
import ca.mohawk.kolar.models.MountResult;

/**
 * /**
 *  * ListView adapter used to display custom mount result layout
 *  */
public class MountAdapter extends ArrayAdapter<MountResult> {
    public static String TAG = "==MountAdapter==";

    /**
     * Constructor
     */
    public MountAdapter(@NonNull Context context, @NonNull List<MountResult> objects) {
        super(context, 0, objects);
        Log.d(TAG, "MountAdapter()");
    }

    /**
     * Used to get mount result model associated with a position in listview
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Log.d(TAG, "getView()");

        // Get data for this position
        MountResult mountResult = getItem(position);

        // Check if view is already being reused, otherwise inflate
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.mount_result_layout, parent, false);
        }

        // Lookup view for mount info
        TextView mountId = convertView.findViewById(R.id.IdTextView);
        TextView mountName = convertView.findViewById(R.id.MountNameTextView);

        // Set text
        mountId.setText(Integer.toString(mountResult.id));
        mountName.setText(mountResult.name);

        // Return configured view
        return convertView;
    }
}

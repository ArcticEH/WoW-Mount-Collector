package ca.mohawk.kolar.fragments;

import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import ca.mohawk.kolar.R;

/**
 * Provides filtering of mounts displayed in database
 */
public class FilterFragment extends DialogFragment {
    public static String TAG = "==FilterFragment==";

    /**
     * Constructor
     */
    public FilterFragment() {
        Log.d(TAG, "FilterFragment()");
        // Required empty public constructor
    }

    /**
     * Configures the view by setting on click handers
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_filter, container, false);

        // Add on click listeners
        Button filterButton = view.findViewById(R.id.EnterFilterButton);
        filterButton.setOnClickListener(this::onClickOk);
        Button cancelButton = view.findViewById(R.id.CancelFilterButton);
        cancelButton.setOnClickListener(this::onClickCancel);

        return view;
    }

    /**
     * onClick handler for ok. Filter results
     * @param view
     */
    public void onClickOk(View view) {
        Log.d(TAG, "onClickOk()");
        // Get text to filter by
        TextView filterTextView = getView().findViewById(R.id.FilterEditText);
        String mountName = filterTextView.getText().toString();

        // Call database fragment below to filter results
        MountDatabaseFragment.instance.DisplayMounts(null, mountName.toLowerCase());

        // Dismiss when done
        dismiss();
    }

    /**
     * onClick handler for cancel. Display all mounts
     * @param view
     */
    public void onClickCancel(View view) {
        Log.d(TAG, "onClickCancel()");

        // Set default display mounts
        MountDatabaseFragment.instance.DisplayMounts(null, null);

        // Dismiss when done
        dismiss();
    }
}
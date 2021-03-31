package ca.mohawk.kolar;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


public class FilterFragment extends DialogFragment {

    public static String tag = "==FilterFragment==";

    public FilterFragment() {
        // Required empty public constructor
    }


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

    public void onClickOk(View view) {
        Log.d(tag, "onOk");

        // Get text to filter by
        TextView filterTextView = getView().findViewById(R.id.FilterEditText);
        String mountName = filterTextView.getText().toString();

        Log.d(tag, mountName);

        // Get token
        MountDatabaseFragment.instance.DisplayMounts(null, mountName.toLowerCase());

        // Dismiss when done
        dismiss();
    }

    public void onClickCancel(View view) {
        // Set default display mounts
        MountDatabaseFragment.instance.DisplayMounts(null, null);

        // Dismiss when done
        dismiss();
    }
}
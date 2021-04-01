package ca.mohawk.kolar.fragments;

import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ca.mohawk.kolar.activity.DetailActivity;
import ca.mohawk.kolar.api.GetRequestTask;
import ca.mohawk.kolar.api.GetRequestType;
import ca.mohawk.kolar.adapters.MountAdapter;
import ca.mohawk.kolar.R;
import ca.mohawk.kolar.models.MountResult;

/**
 * Fragment used to request all mounts from Blizzard API
 */
public class MountDatabaseFragment extends Fragment {
    public static String TAG = "==MountDatabaseFragment==";

    // Set instance for API requests
    public static MountDatabaseFragment instance;

    // Used to set if app is in offline mode. This is the only page that is affected
    public static boolean offlineMode = false;

    // Store all mounts for easy filtering
    public MountResult[] allMounts;

    /**
     * Configures view to be displayed. Configure based on online mode and make request for all
     * mounts if in online mode
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreateView()");

        // Inflate the layout
        View view = inflater.inflate(R.layout.fragment_mount_database, container, false);

        // Set main activity to access from requests
        instance = this;

        // Configure offline mode and do not move further to make requests
        if (offlineMode) {
            TextView textView = view.findViewById(R.id.ListViewStatusTextView);
            textView.setText(getContext().getString(R.string.db_status_offlineMode));
            Button filterButton = view.findViewById(R.id.FilterButton);
            filterButton.setEnabled(false);
            TextView filterTextView = view.findViewById(R.id.FilteredTextView);
            filterTextView.setText("");
            return view;
        }


        // Request list info
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getString(R.string.sharedPreferences_file), Context.MODE_PRIVATE);
        String token = sharedPreferences.getString(getString(R.string.sharedPreferences_token_key), "");

        // Make reqeust for all mounts
        GetRequestTask getRequestTask = new GetRequestTask();
        getRequestTask.execute(getContext().getString(R.string.api_allMounts, token)
                , GetRequestType.AllMounts.toString());

        // Set listeners
        view.findViewById(R.id.FilterButton).setOnClickListener(this::onClickFilter);
        ListView mountListView = view.findViewById(R.id.MountListView);
        mountListView.setOnItemClickListener(this::onItemClick);

        // Style listview
        mountListView.setDividerHeight(7);

        // Finally, return view
        return view;
    }


    /**
     * Displays all mounts in the listview with a filter. Sets mounts if this is the first request.
     * @param mounts The mounts to be displayed. Only set on first request
     * @param filter The filter being applied
     */
    public void DisplayMounts(MountResult[] mounts, String filter) {
        Log.d(TAG, "DisplayMounts()");

        try {
            // Remove display status
            TextView listViewStatusTextView = getActivity().findViewById(R.id.ListViewStatusTextView);
            listViewStatusTextView.setVisibility(View.GONE);

            // Set all mounts here if this is the first request
            if (allMounts == null) {
                allMounts = mounts;
            }

            // Set up list as mount adapter uses a list
            List<MountResult> mountsToDisplay;

            // If filter is provided
            TextView filterTextView = getActivity().findViewById(R.id.FilteredTextView);
            if (filter != null && !filter.equals("")) {
                filterTextView.setText(getString(R.string.db_filteredTextView, filter.toUpperCase()));
                mountsToDisplay = new ArrayList<MountResult>();
                for (MountResult mr: allMounts) {
                    if (mr.name.toLowerCase().contains(filter) || Integer.toString(mr.id).contains(filter))
                        mountsToDisplay.add(mr);
                }
            } else {
                filterTextView.setText(getString(R.string.db_filteredTextView, "NONE"));
                mountsToDisplay = Arrays.asList(allMounts);
            }

            // If filter leaves no mounts then display status message
            if (mountsToDisplay.size() == 0) {
                SetStatus(getContext().getString(R.string.db_status_noMounts), false, false);
            }

            // Use custom adapter to display mounts
            MountAdapter adapter = new MountAdapter(getContext(), mountsToDisplay);
            ListView mountList = getActivity().findViewById(R.id.MountListView);
            mountList.setAdapter(adapter);
        } catch (Exception ex) {
            Log.d(TAG, "Exception occurred.");
        }

    }

    /**
     * Used to set listview status
     * @param text The text to display
     * @param hidden Whether or not status should be displayed
     * @param disableButton Whether or not filter button should be disabled
     */
    public void SetStatus(String text, Boolean hidden, Boolean disableButton) {
        Log.d(TAG, "SetStatus()");

        // Get status text view to set
        TextView statusTextView = getActivity().findViewById(R.id.ListViewStatusTextView);

        // Set text
        statusTextView.setText(text);

        // Set visibility based on hidden parameter
        if (hidden) {
            statusTextView.setVisibility(View.GONE);
        } else {
            statusTextView.setVisibility(View.VISIBLE);
        }

        // Set filter button disabled based on disabled parameter
        if (disableButton) {
            TextView filterTextView = getActivity().findViewById(R.id.FilteredTextView);
            filterTextView.setText("");
            getActivity().findViewById(R.id.FilterButton).setEnabled(false);
        } else {
            getActivity().findViewById(R.id.FilterButton).setEnabled(true);
        }

    }

    /**
     * onClick handler for filter button. Displays filter dialogue fragment
     * @param view
     */
    public void onClickFilter(View view) {
        Log.d(TAG, "onClickFilter()");

        // Create filter dialog fragment and show
        FilterFragment loginDialogFragment = new FilterFragment();
        loginDialogFragment.show(getFragmentManager(), null);
    }

    /**
     * onClick handler for list view item
     */
    public void onItemClick(AdapterView parent, View v, int position, long id) {
        Log.d(TAG, "onItemClick()");

        // Create intent for detailed mount view
        Intent detailViewIntent = new Intent(getActivity(), DetailActivity.class);

        // Put mount ID in intent to let detail view know which mount to display
        TextView mountIdTextView = v.findViewById(R.id.IdTextView);
        detailViewIntent.putExtra(getString(R.string.detail_intent_mountId), Integer.parseInt(mountIdTextView.getText().toString()));

        // Start activity. For result prevents this activity from being dismissed.
        startActivityForResult(detailViewIntent, 0);
    }


}
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

public class MountDatabaseFragment extends Fragment {
    public static String TAG = "==MountDatabaseFragment==";

    public static MountDatabaseFragment instance;

    public static boolean offlineMode = false;

    public MountResult[] allMounts;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = inflater.inflate(R.layout.mount_database, container, false);

        // Set main activity to access from requests
        instance = this;

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


        GetRequestTask getRequestTask = new GetRequestTask();
        getRequestTask.execute(getContext().getString(R.string.api_allMounts, token)
                , GetRequestType.AllMounts.toString());

        // Set listeners
        view.findViewById(R.id.FilterButton).setOnClickListener(this::onClickFilter);
        ListView mountListView = view.findViewById(R.id.MountListView);
        mountListView.setOnItemClickListener(this::onItemClick);

        mountListView.setDividerHeight(7);

        return view;
    }


    //https://guides.codepath.com/android/Using-an-ArrayAdapter-with-ListView
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

            if (mountsToDisplay.size() == 0) {
                SetStatus(getContext().getString(R.string.db_status_noMounts), false, false);
            }

            MountAdapter adapter = new MountAdapter(getContext(), mountsToDisplay);
            ListView mountList = getActivity().findViewById(R.id.MountListView);
            mountList.setAdapter(adapter);
        } catch (Exception ex) {
            Log.d(TAG, "Exception occurred.");
        }

    }

    public void SetStatus(String text, Boolean hidden, Boolean disableButton) {
        TextView statusTextView = getActivity().findViewById(R.id.ListViewStatusTextView);
        statusTextView.setText(text);
        if (hidden) {
            statusTextView.setVisibility(View.GONE);
        } else {
            statusTextView.setVisibility(View.VISIBLE);
        }

        if (disableButton) {
            TextView filterTextView = getActivity().findViewById(R.id.FilteredTextView);
            filterTextView.setText("");
            getActivity().findViewById(R.id.FilterButton).setEnabled(false);
        } else {
            getActivity().findViewById(R.id.FilterButton).setEnabled(true);
        }

    }

    public void onClickFilter(View view) {
        FilterFragment loginDialogFragment = new FilterFragment();
        loginDialogFragment.show(getFragmentManager(), null);
    }

    public void onItemClick(AdapterView parent, View v, int position, long id) {
        Intent detailViewIntent = new Intent(getActivity(), DetailActivity.class);
        TextView mountIdTextView = v.findViewById(R.id.IdTextView);
        detailViewIntent.putExtra(getString(R.string.detail_intent_mountId), Integer.parseInt(mountIdTextView.getText().toString()));
        startActivityForResult(detailViewIntent, 0);
    }


}
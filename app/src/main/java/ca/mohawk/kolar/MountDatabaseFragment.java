package ca.mohawk.kolar;

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
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MountDatabaseFragment extends Fragment {
    public static String TAG = "==MountDatabaseFragment==";
    String token = "";
    public static MountDatabaseFragment instance;

    public MountResult[] allMounts;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = inflater.inflate(R.layout.activity_main, container, false);

        // Set main activity to access from requests
        instance = this;

        // Request list info
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getString(R.string.sharedPreferences_file), Context.MODE_PRIVATE);
        String token = sharedPreferences.getString(getString(R.string.sharedPreferences_token_key), "");
        Log.d(TAG, "Token - " + token);

        GetRequestTask getRequestTask = new GetRequestTask();
        getRequestTask.execute("https://us.api.blizzard.com/data/wow/mount/index?namespace=static-us&locale=en_US&access_token=" +
                token
                , "AllMounts");

        // Set listeners
        view.findViewById(R.id.FilterButton).setOnClickListener(this::onClickFilter);
        ListView mountListView = view.findViewById(R.id.MountListView);
        mountListView.setOnItemClickListener(this::onItemClick);

        return view;
    }


    //https://guides.codepath.com/android/Using-an-ArrayAdapter-with-ListView
    public void DisplayMounts(MountResult[] mounts, String filter) {
        Log.d(TAG, "DisplayMounts()");

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
            filterTextView.setText(getString(R.string.actOne_filteredTextView, filter.toUpperCase()));
            mountsToDisplay = new ArrayList<MountResult>();
            for (MountResult mr: allMounts) {
                if (mr.name.toLowerCase().contains(filter))
                    mountsToDisplay.add(mr);
            }
        } else {
            filterTextView.setText(getString(R.string.actOne_filteredTextView, "NONE"));
            mountsToDisplay = Arrays.asList(allMounts);
        }

        if (mountsToDisplay.size() == 0) {
            listViewStatusTextView.setText("No items to display.");
            listViewStatusTextView.setVisibility(View.VISIBLE);
        }

        MountAdapter adapter = new MountAdapter(getContext(), mountsToDisplay);
        ListView mountList = getActivity().findViewById(R.id.MountListView);
        mountList.setAdapter(adapter);
    }

    public void onClickFilter(View view) {
        FilterFragment loginDialogFragment = new FilterFragment();
        loginDialogFragment.show(getFragmentManager(), null);
    }

    public void onItemClick(AdapterView parent, View v, int position, long id) {
        Intent detailViewIntent = new Intent(getActivity(), DetailActivity.class);
        TextView mountIdTextView = v.findViewById(R.id.IdTextView);
        detailViewIntent.putExtra("mountId", Integer.parseInt(mountIdTextView.getText().toString()));
        detailViewIntent.putExtra("useAPI", true);
        startActivityForResult(detailViewIntent, 0);
    }


}
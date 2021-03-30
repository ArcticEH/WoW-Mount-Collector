package ca.mohawk.kolar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public static String TAG = "==MainActivity";
    String token = "";
    public static MainActivity instance;

    DrawerLayout navigationDrawer;
    NavigationView navigationView;

    public MountResult[] allMounts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set main activity to access from requests
        instance = this;

        // Request list info
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.sharedPreferences_file), Context.MODE_PRIVATE);
        String token = sharedPreferences.getString(getString(R.string.sharedPreferences_token_key), "");
        Log.d(TAG, "Token - " + token);

        GetRequestTask getRequestTask = new GetRequestTask();
        getRequestTask.execute("https://us.api.blizzard.com/data/wow/mount/index?namespace=static-us&locale=en_US&access_token=" +
                token
                , "AllMounts");

        // Set listeners
        findViewById(R.id.FilterButton).setOnClickListener(this::onClickFilter);
        ListView mountListView = findViewById(R.id.MountListView);
        mountListView.setOnItemClickListener(this::onItemClick);

        // Setup the navigation bar
        SetupNavigationBar();

    }

    private void SetupNavigationBar() {
        // Access drawer
        navigationDrawer = (DrawerLayout)
                findViewById(R.id.drawer_layout);

        // Enable display of home button
        ActionBar myActionBar = getSupportActionBar();
        myActionBar.setDisplayHomeAsUpEnabled(true);

        // Add drawer toggle listener
        ActionBarDrawerToggle myactionbartoggle = new
                ActionBarDrawerToggle(this, navigationDrawer,
                (R.string.open), (R.string.close));
        navigationDrawer.addDrawerListener(myactionbartoggle);
        myactionbartoggle.syncState();

        // Set up callback for when drawer item is selected
        navigationView = (NavigationView)
                findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Set first item on startup
        onNavigationItemSelected(navigationView.getMenu().getItem(0));
    }

    //https://guides.codepath.com/android/Using-an-ArrayAdapter-with-ListView
    public void DisplayMounts(MountResult[] mounts, String filter) {
        Log.d(TAG, "DisplayMounts()");

        // Remove display status
        TextView listViewStatusTextView = findViewById(R.id.ListViewStatusTextView);
        listViewStatusTextView.setVisibility(View.GONE);

        // Set all mounts here if this is the first request
        if (allMounts == null) {
            allMounts = mounts;
        }

        // Set up list as mount adapter uses a list
        List<MountResult> mountsToDisplay;

        // If filter is provided
        TextView filterTextView = findViewById(R.id.FilteredTextView);
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

        MountAdapter adapter = new MountAdapter(this, mountsToDisplay);
        ListView mountList = findViewById(R.id.MountListView);
        mountList.setAdapter(adapter);
    }

    public void onClickFilter(View view) {
        FilterFragment loginDialogFragment = new FilterFragment();
        loginDialogFragment.show(getSupportFragmentManager(), null);
    }

    public void onItemClick(AdapterView parent, View v, int position, long id) {
        Intent detailViewIntent = new Intent(this, DetailActivity.class);
        TextView mountIdTextView = v.findViewById(R.id.IdTextView);
        detailViewIntent.putExtra("mountId", Integer.parseInt(mountIdTextView.getText().toString()));
        detailViewIntent.putExtra("useAPI", true);
        startActivityForResult(detailViewIntent, 0);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Set selected menu items
        int size = navigationView.getMenu().size();
        for (int i = 0; i < size; i++) {
            MenuItem currItem = navigationView.getMenu().getItem(i);
            if (currItem.equals(item))
                currItem.setChecked(true);
            else
                currItem.setChecked(false);
        }

        // Close menu drawer
        navigationDrawer.closeDrawers();
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Find out the current state of the drawer (open or closed)
        boolean isOpen = navigationDrawer.isDrawerOpen(GravityCompat.START);
        // Handle item selection
        switch (item.getItemId()) {
            case android.R.id.home:
                // Home button - open or close the drawer
                if (isOpen == true) {
                    navigationDrawer.closeDrawer(GravityCompat.START);
                } else {
                    navigationDrawer.openDrawer(GravityCompat.START);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
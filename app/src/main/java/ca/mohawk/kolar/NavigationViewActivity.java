package ca.mohawk.kolar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

public class NavigationViewActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    FragmentManager fragmentManager;

    DrawerLayout navigationDrawer;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_view);

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


        // Set fragment manager
        fragmentManager = getSupportFragmentManager();

        // Select first menu item
        onNavigationItemSelected(navigationView.getMenu().getItem(0));

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

        // Check which activity to display
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Insert fragments top fragment will show and where to display them
        Fragment fragmentToDisplay;
        switch (item.getItemId()) {
            case R.id.MountDataMenuItem:
                fragmentToDisplay = new MountDatabaseFragment();
                break;
            case R.id.CollectionMenuItem:
                fragmentToDisplay = new CollectionFragment();
                break;
            default:
                fragmentToDisplay = new MountDatabaseFragment();

        }

        fragmentTransaction.replace(R.id.FrameLayout, fragmentToDisplay);
        fragmentTransaction.commit();


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
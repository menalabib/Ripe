package ripe.ripe;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.view.MenuItem;

import ripe.ripe.NavFragments.FeedFragment;
import ripe.ripe.NavFragments.GroupsFragment;
import ripe.ripe.NavFragments.LeaderboardFragment;
import ripe.ripe.NavFragments.ProfileFragment;

public class NavActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}
        setContentView(R.layout.activity_nav);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setItemIconTintList(null);
        navView.setOnNavigationItemSelectedListener(this);

        loadFragment(new FeedFragment());
    }

    private boolean loadFragment(android.support.v4.app.Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        android.support.v4.app.Fragment fragment = null;

        switch (menuItem.getItemId()) {
            case R.id.navigation_feed:
                fragment = new FeedFragment();
                break;
            case R.id.navigation_profile:
                fragment = new ProfileFragment();
                break;
            case R.id.navigation_leaderboard:
                fragment = new LeaderboardFragment();
                break;
            case R.id.navigation_groups:
                fragment = new GroupsFragment();
                break;
            case R.id.navigation_upload:
                break;
        }
        return loadFragment(fragment);
    }
}

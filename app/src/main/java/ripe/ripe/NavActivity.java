package ripe.ripe;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.view.MenuItem;
import android.widget.TextView;

import com.facebook.login.Login;

public class NavActivity extends AppCompatActivity {
    private TextView mTextMessage;
    Fragment currentFragment = null;
    FragmentTransaction ft;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_feed:
                    mTextMessage.setText(R.string.title_feed);
                    switchToProfile();
                    return true;
                case R.id.navigation_upload:
                    mTextMessage.setText(R.string.title_upload);
                    switchToProfile();
                    return true;
                case R.id.navigation_leaderboard:
                    mTextMessage.setText(R.string.title_leaderboard);
                    switchToProfile();
                    return true;
                case R.id.navigation_profile:
                    mTextMessage.setText(R.string.title_profile);
                    switchToProfile();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav);
        Intent intent = getIntent();
        String name = intent.getStringExtra(LoginActivity.FB_NAME);
        String email = intent.getStringExtra(LoginActivity.FB_EMAIL);
        String image = intent.getStringExtra(LoginActivity.FB_IMAGE);
        System.out.println("NAV: " + name);
        System.out.println("NAV: " + email);

        ft = getFragmentManager().beginTransaction();
        currentFragment = new profileFragment();
        ft.replace(R.id.profile_fragment, currentFragment);
        ft.addToBackStack(null).commit();

        BottomNavigationView navView = findViewById(R.id.nav_view);
        mTextMessage = findViewById(R.id.message);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

//    public void switchToFeed() {
//        FragmentManager manager = getSupportFragmentManager();
//        manager.beginTransaction().replace(R.id.your_fragment_layout_name, feedFragment()).commit();
//    }
//
//    public void switchToUpload() {
//        FragmentManager manager = getSupportFragmentManager();
//        manager.beginTransaction().replace(R.id.your_fragment_layout_name, new uploadFragment()).commit();
//    }
//
//    public void switchToLeaderboard() {
//        FragmentManager manager = getSupportFragmentManager();
//        manager.beginTransaction().replace(R.id.your_fragment_layout_name, new leaderboardFragment()).commit();
//    }

    public void switchToProfile() {
        getFragmentManager().beginTransaction().replace(R.id.profile_fragment, new profileFragment()).addToBackStack(null).commit();
    }

}

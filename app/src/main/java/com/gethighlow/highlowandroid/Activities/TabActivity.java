package com.gethighlow.highlowandroid.Activities;

import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.gethighlow.highlowandroid.Activities.Fragments.Home;
import com.gethighlow.highlowandroid.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class TabActivity extends AppCompatActivity implements Home.OnFragmentInteractionListener, Profile.OnFragmentInteractionListener {
    private FrameLayout frameLayout;
    private Home home;
    private Profile profile;
    private Fragment current;
    private ActionBar actionBar;

    private FragmentManager fragmentManager;

    public void onHomeFragmentInteraction(Uri uri) {

    }

    public void onProfileFragmentInteraction(Uri uri) {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.tab_activity);

        frameLayout = findViewById(R.id.fragments);

        home = new Home();
        home.setAppCompatActivity(this);
        profile = new Profile();
        current = home;

        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragments, current);
        fragmentTransaction.commit();

        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(this.onNavigationItemSelectedListener);

        actionBar = getSupportActionBar();

        actionBar.setTitle("Home");
    }

    private void setTab(Fragment fragment) {
        current = fragment;
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragments, current);
        fragmentTransaction.commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch(item.getItemId()) {
                case R.id.navigation_home:
                    setTab(home);
                    actionBar.setTitle("Home");
                    break;
                case R.id.navigation_profile:
                    setTab(profile);
                    actionBar.setTitle("Profile");
                    break;
            }

            return true;
        }
    };

}

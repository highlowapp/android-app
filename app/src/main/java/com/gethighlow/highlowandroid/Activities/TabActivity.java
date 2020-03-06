package com.gethighlow.highlowandroid.Activities;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentController;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.gethighlow.highlowandroid.Activities.Tabs.Fragments.Home;
import com.gethighlow.highlowandroid.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class TabActivity extends AppCompatActivity implements Home.OnFragmentInteractionListener, Profile.OnFragmentInteractionListener {
    private FrameLayout frameLayout;
    private Home home;
    private Profile profile;
    private Fragment current;
    private Feed feed;
    private Fragment.SavedState homeState;
    private Fragment.SavedState profileState;
    private Fragment.SavedState feedState;

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


        home = new Home();
        home.setAppCompatActivity(this);
        profile = new Profile();
        feed = new Feed();

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

    private void setTab(String tab) {
        switch (tab) {
            case "home":
                current = home;
                current.setInitialSavedState(homeState);
                break;
            case "profile":
                current = profile;
                current.setInitialSavedState(profileState);
                break;
            case "feed":
                current = feed;
                current.setInitialSavedState(feedState);
                break;
        }

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragments, current);
        fragmentTransaction.commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch(item.getItemId()) {
                case R.id.navigation_home:
                    if (current == profile) {
                        profileState = fragmentManager.saveFragmentInstanceState(profile);
                    } else if (current == feed) {
                        feedState = fragmentManager.saveFragmentInstanceState(feed);
                    }
                    setTab("home");
                    actionBar.setTitle("Home");
                    break;
                case R.id.navigation_profile:
                    if (current == home) {
                        homeState = fragmentManager.saveFragmentInstanceState(home);
                    } else if (current == feed) {
                        feedState = fragmentManager.saveFragmentInstanceState(feed);
                    }
                    setTab("profile");
                    actionBar.setTitle("Profile");
                    break;
                case R.id.navigation_feed:
                    if (current == profile) {
                        profileState = fragmentManager.saveFragmentInstanceState(profile);
                    } else if (current == home) {
                        homeState = fragmentManager.saveFragmentInstanceState(home);
                    }
                    setTab("feed");
                    actionBar.setTitle("Feed");
            }

            return true;
        }
    };

}

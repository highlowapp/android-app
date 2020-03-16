package com.gethighlow.highlowandroid.Activities;

import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.gethighlow.highlowandroid.Activities.Tabs.Fragments.Home;
import com.gethighlow.highlowandroid.R;
import com.gethighlow.highlowandroid.model.Services.NotificationsService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class TabActivity extends AppCompatActivity implements Home.OnFragmentInteractionListener, Profile.OnFragmentInteractionListener {
    private FrameLayout frameLayout;
    private Home home;
    private Profile profile;
    private int current = R.id.navigation_home;
    private Feed feed;
    private Map<Integer, Fragment.SavedState> savedStateMap = new HashMap<>();
    private Map<Integer, Fragment> fragments = new HashMap<>();
    private MenuItem calendarView;

    private Map<Integer, String> titles = new HashMap<Integer, String>() {{
        put(R.id.navigation_home, "Home");
        put(R.id.navigation_profile, "Profile");
        put(R.id.navigation_feed, "Feed");
        put(R.id.navigation_about, "About");
    }};

    private ActionBar actionBar;

    private FragmentManager fragmentManager;

    public void onHomeFragmentInteraction(Uri uri) {

    }

    public void onProfileFragmentInteraction(Uri uri) {

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("current", current);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.tab_activity);

        if (savedInstanceState != null && savedInstanceState.containsKey("current")) {
            current = savedInstanceState.getInt("current");
        }

        fragments.put(R.id.navigation_home, new Home());
        fragments.put(R.id.navigation_profile, new Profile());
        fragments.put(R.id.navigation_feed, new Feed());
        fragments.put(R.id.navigation_about, new About());

        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        for (Fragment fragment: fragments.values()) {
            fragmentTransaction.add(R.id.fragments, fragment);
            fragmentTransaction.hide(fragment);
        }
        fragmentTransaction.show(fragments.get(current));
        fragmentTransaction.commit();

        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(this.onNavigationItemSelectedListener);

        actionBar = getSupportActionBar();

        actionBar.setTitle(titles.get(current));

        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter("com.gethighlow.com_logout"));

        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String token = task.getResult().getToken();

                NotificationsService.shared().register(token, notificationsRegisterResponse -> {
                    Log.w("Debug", "REGISTERED NOTIFICATIONS");
                }, error -> {
                    Log.w("Debug", "ERROR: " + error);
                });
            }
        });
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            finish();
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        calendarView = menu.getItem(0);

        if (current == R.id.navigation_home) {
            calendarView.setVisible(true);
        } else {
            calendarView.setVisible(false);
        }
        return super.onCreateOptionsMenu(menu);
    }

    private void setTab(int itemId) {
        if (itemId != current) {
            Fragment newFragment = fragments.get(itemId);
            //newFragment.setInitialSavedState(savedStateMap.get(itemId));
            current = itemId;
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            for (Fragment fragment: fragments.values()) {
                fragmentTransaction.hide(fragment);
            }
            fragmentTransaction.show(fragments.get(itemId));
            fragmentTransaction.commit();
            actionBar.setTitle(titles.get(current));

            if (current == R.id.navigation_home) {
                calendarView.setVisible(true);
            } else {
                calendarView.setVisible(false);
            }
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            savedStateMap.put(item.getItemId(), fragmentManager.saveFragmentInstanceState(fragments.get(current)));
            setTab(item.getItemId());
            return true;
        }
    };


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.openCalendar) {
            DatePickerDialog datePickerDialog = new DatePickerDialog(this);
            datePickerDialog.setOnDateSetListener(dateChangeListener);
            datePickerDialog.show();
        }

        return super.onOptionsItemSelected(item);
    }

    private DatePickerDialog.OnDateSetListener dateChangeListener = (datePicker, i, i1, i2) -> {
        ((Home) fragments.get(R.id.navigation_home)).setDate( LocalDate.of(i, i1 + 1, i2) );
    };
}

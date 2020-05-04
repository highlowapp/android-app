package com.gethighlow.highlowandroid.Activities.Other;

import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.SupportMenuInflater;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.gethighlow.highlowandroid.Activities.Tabs.Feed.Feed;
import com.gethighlow.highlowandroid.Activities.Tabs.Home.Home;
import com.gethighlow.highlowandroid.Activities.Tabs.Home.ViewHighLowActivity;
import com.gethighlow.highlowandroid.Activities.Tabs.About.About;
import com.gethighlow.highlowandroid.Activities.Tabs.Profile.FriendsActivity;
import com.gethighlow.highlowandroid.Activities.Tabs.Profile.Profile;
import com.gethighlow.highlowandroid.R;
import com.gethighlow.highlowandroid.model.Services.SetActivityTheme;
import com.gethighlow.highlowandroid.model.util.Consumer;
import com.gethighlow.highlowandroid.model.Responses.NotificationsRegisterResponse;
import com.gethighlow.highlowandroid.model.Services.NotificationsService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.threeten.bp.LocalDate;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class TabActivity extends AppCompatActivity implements Home.OnFragmentInteractionListener, Profile.OnFragmentInteractionListener {
    private FrameLayout frameLayout;
    private Home home;
    private Profile profile;
    private int current = R.id.navigation_home;
    private Feed feed;
    private Map<Integer, Fragment.SavedState> savedStateMap = new HashMap<Integer, Fragment.SavedState>();
    private Map<Integer, Fragment> fragments = new HashMap<Integer, Fragment>();
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
        Context context = getApplicationContext();
        String theme = SetActivityTheme.getTheme(context);

        if(theme.equals("light")){
            setTheme(R.style.LightTheme);
        }else{
            setTheme(R.style.DarkTheme);
        }



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

        bottomNavigationViewColor(context, bottomNavigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener(this.onNavigationItemSelectedListener);

        actionBar = getSupportActionBar();

        actionBar.setTitle(titles.get(current));

        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter("com.gethighlow.com_logout"));
        LocalBroadcastManager.getInstance(context).registerReceiver(themeReceiver, new IntentFilter("theme-updated"));
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                if (task.isSuccessful()) {
                    String token = task.getResult().getToken();
                    NotificationsService.shared().register(token, new Consumer<NotificationsRegisterResponse>() {
                        @Override
                        public void accept(NotificationsRegisterResponse notificationsRegisterResponse) {
                        }
                    }, new Consumer<String>() {
                        @Override
                        public void accept(String error) {

                        }
                    });
                }
            }
        });


        if (getIntent().getExtras() != null) {
            Bundle extras = getIntent().getExtras();
            if (extras.containsKey("highlowid")) {
                viewHighLow(extras.getString("highlowid"));
            } else if (extras.containsKey("uid")) {
                viewFriends(extras.getString("uid"));
            }
        }
    }

    private void viewHighLow(String highlowid) {
        Intent starter = new Intent(this, ViewHighLowActivity.class);
        starter.putExtra("highlowid", highlowid);
        startActivity(starter);
    }

    private void viewFriends(String uid) {
        Intent starter = new Intent(this, FriendsActivity.class);
        starter.putExtra("uid", uid);
        startActivity(starter);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            finish();
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /** This is where I'm trying to set the theme of the calendar **/
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
            LocalDate currDate = LocalDate.now();
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, R.style.DatePickerTheme, dateChangeListener, currDate.getYear(), currDate.getMonth().getValue() - 1, currDate.getDayOfMonth());
            datePickerDialog.show();
        }

        return super.onOptionsItemSelected(item);
    }

    private DatePickerDialog.OnDateSetListener dateChangeListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            ((Home) fragments.get(R.id.navigation_home)).setDate(LocalDate.of(i, i1 + 1, i2));
        }
    };


    private BroadcastReceiver themeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
                BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);
                bottomNavigationViewColor(context, bottomNavigationView);
                recreate();
        }
    };


    private void bottomNavigationViewColor(Context context, BottomNavigationView bottomNavigationView){

        String theme = SetActivityTheme.getTheme(context);

        if(theme.equals("light")) {
            bottomNavigationView.setBackgroundColor(getResources().getColor(R.color.very_light_gray));
            setTheme(R.style.LightTheme);

        } else if(theme.equals("dark")){
            bottomNavigationView.setBackgroundColor(getResources().getColor(R.color.very_dark_gray));
            setTheme(R.style.DarkTheme);
        }

    }
}

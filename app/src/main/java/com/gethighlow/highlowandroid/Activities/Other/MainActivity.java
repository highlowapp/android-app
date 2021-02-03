package com.gethighlow.highlowandroid.Activities.Other;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.gethighlow.highlowandroid.Activities.Authentication.SignInActivity;
import com.gethighlow.highlowandroid.Activities.Tabs.Home.AgreementsActivity;
import com.gethighlow.highlowandroid.Activities.Tabs.Home.InterestsPitchActivity;
import com.gethighlow.highlowandroid.model.Managers.HighLowManager;
import com.gethighlow.highlowandroid.model.Managers.ImageManager;
import com.gethighlow.highlowandroid.model.Managers.UserManager;
import com.gethighlow.highlowandroid.model.Services.APIService;
import com.gethighlow.highlowandroid.model.Services.AuthService;
import com.gethighlow.highlowandroid.model.util.PremiumStatusListener;
import com.revenuecat.purchases.Purchases;

public class MainActivity extends Activity {
    private String highlowid;
    private String uid;

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "High/Low";
            String description = "Notifications for friend requests, new feed items, comments, and other events in the High/Low app.";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("com.gethighlow.notifications", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Attach the shared APIService to the context
        APIService.shared().attachToContext(this);
        AuthService.shared().attachToContext(this);
        UserManager.shared().attachToContext(this);
        HighLowManager.shared().attachToContext(this);
        ImageManager.shared().attachToContext(this);

        SharedPreferences sharedPreferences = this.getSharedPreferences("com.gethighlow.SharedPref", Context.MODE_PRIVATE);
        String accessToken = sharedPreferences.getString("access", "none");

        if (accessToken.equals("none") || accessToken.equals("")) {
            isNotAuthenticated();
        } else {
            createNotificationChannel();
            isAuthenticated();
        }

        finish();
    }

    public void switchToAuth() {
        Intent openAuth = new Intent(this, SignInActivity.class);
        this.startActivity(openAuth);
    }

    public void showTutorial() {
        Intent openTutorial = new Intent(this, TutorialActivity.class);
        startActivity(openTutorial);
    }

    public void isNotAuthenticated() {
        SharedPreferences sharedPreferences = getSharedPreferences("com.gethighlow.SharedPref", Context.MODE_PRIVATE);
        boolean hasReceivedTutorial = sharedPreferences.getBoolean("hasReceivedTutorial", false);

        if (hasReceivedTutorial) {
            switchToAuth();
        }
        else {
            showTutorial();
        }
    }

    public void switchToMain() {
        Intent openMain = new Intent(this, TabActivity.class);

        if (getIntent().getExtras() != null) {
            Bundle extras = getIntent().getExtras();
            if (extras.containsKey("highlowid")) {
                openMain.putExtra("highlowid", extras.getString("highlowid"));
            }
            if (extras.containsKey("uid")) {
                openMain.putExtra("uid", extras.getString("uid"));
            }

        }

        this.startActivity(openMain);
    }

    public void isAuthenticated() {
        SharedPreferences sharedPreferences = getSharedPreferences("com.gethighlow.SharedPref", Context.MODE_PRIVATE);
        boolean hasAgreed = sharedPreferences.getBoolean("hasAgreed", false);
        boolean hasSeenPitch = sharedPreferences.getBoolean("hasSeenPitch", false);

        Purchases.setDebugLogsEnabled(true);

        //Configure purchases with the current user's uid
        Purchases.configure(this, "YGfUbzNDybIfScxtFhNVJrUMHcApwIxz", AuthService.shared().getUid());

        //Set our UpdatedPurchaserInfoListener
        Purchases.getSharedInstance().setUpdatedPurchaserInfoListener( PremiumStatusListener.shared() );

        if (!hasAgreed) {
            showAgreements();
        }
        else if (!hasSeenPitch) {
            showPitch();
        }
        else {
            switchToMain();
        }
    }

    private void showAgreements() {
        Intent agreements = new Intent(this, AgreementsActivity.class);
        this.startActivity(agreements);
    }
    private void showPitch() {
        Intent pitch = new Intent(this, InterestsPitchActivity.class);
        this.startActivity(pitch);
    }
}

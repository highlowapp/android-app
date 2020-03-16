package com.gethighlow.highlowandroid.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.gethighlow.highlowandroid.Activities.Authentication.SignInActivity;
import com.gethighlow.highlowandroid.model.Managers.HighLowManager;
import com.gethighlow.highlowandroid.model.Managers.ImageManager;
import com.gethighlow.highlowandroid.model.Managers.UserManager;
import com.gethighlow.highlowandroid.model.Services.APIService;
import com.gethighlow.highlowandroid.model.Services.AuthService;

public class MainActivity extends Activity {
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
            //switchToAuth();
        } else {
            switchToMain();
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
        Boolean hasReceivedTutorial = sharedPreferences.getBoolean("hasReceivedTutorial", false);

        if (hasReceivedTutorial) {
            switchToAuth();
        }
        else {
            showTutorial();
        }
    }

    public void switchToMain() {
        Intent openMain = new Intent(this, TabActivity.class);
        this.startActivity(openMain);
    }

    public void isAuthenticated() {
        switchToMain();
    }
}

package com.gethighlow.highlowandroid.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.gethighlow.highlowandroid.Activities.Authentication.SignInActivity;
import com.gethighlow.highlowandroid.Activities.TabActivity;
import com.gethighlow.highlowandroid.model.Managers.HighLowManager;
import com.gethighlow.highlowandroid.model.Managers.ImageManager;
import com.gethighlow.highlowandroid.model.Managers.UserManager;
import com.gethighlow.highlowandroid.model.Services.APIService;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Attach the shared APIService to the context
        APIService.shared().attachToContext(this);
        UserManager.shared().attachToContext(this);
        HighLowManager.shared().attachToContext(this);
        ImageManager.shared().attachToContext(this);

        SharedPreferences sharedPreferences = this.getSharedPreferences("com.gethighlow.SharedPref", Context.MODE_PRIVATE);
        String accessToken = sharedPreferences.getString("access", "none");

        if (accessToken.equals("none")) {
            switchToAuth();
        } else {
            switchToMain();
        }
    }

    public void switchToAuth() {
        Intent openAuth = new Intent(this, SignInActivity.class);
        this.startActivity(openAuth);
    }

    public void switchToMain() {
        Intent openMain = new Intent(this, TabActivity.class);
        this.startActivity(openMain);
    }
}

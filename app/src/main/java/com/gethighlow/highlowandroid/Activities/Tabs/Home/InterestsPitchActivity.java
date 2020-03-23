package com.gethighlow.highlowandroid.Activities.Tabs.Home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.gethighlow.highlowandroid.Activities.Tabs.Profile.EditInterestsActivity;
import com.gethighlow.highlowandroid.CustomViews.BaseComponents.HLButton;
import com.gethighlow.highlowandroid.CustomViews.BaseComponents.HLButtonDelegate;
import com.gethighlow.highlowandroid.R;
import com.gethighlow.highlowandroid.model.Services.APIService;

public class InterestsPitchActivity extends AppCompatActivity implements HLButtonDelegate {
    private HLButton absolutely;
    private int REQUEST_CODE = 80;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.interests_pitch_activity);

        absolutely = findViewById(R.id.absolutely);

        absolutely.delegate = this;
    }

    public void decline(View view) {
        SharedPreferences.Editor editor = getSharedPreferences("com.gethighlow.SharedPref", Context.MODE_PRIVATE).edit();
        editor.putBoolean("hasSeenPitch", true);
        editor.apply();
        finish();
        APIService.shared().isAuthenticated();
    }

    @Override
    public void onButtonClick(View view) {
        SharedPreferences.Editor editor = getSharedPreferences("com.gethighlow.SharedPref", Context.MODE_PRIVATE).edit();
        editor.putBoolean("hasSeenPitch", true);
        editor.apply();
        Intent starter = new Intent(this, EditInterestsActivity.class);
        startActivityForResult(starter, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        decline(null);
    }
}

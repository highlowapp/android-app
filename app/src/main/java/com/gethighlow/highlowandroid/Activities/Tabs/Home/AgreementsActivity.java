package com.gethighlow.highlowandroid.Activities.Tabs.Home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.gethighlow.highlowandroid.CustomViews.BaseComponents.HLButton;
import com.gethighlow.highlowandroid.CustomViews.BaseComponents.HLButtonDelegate;
import com.gethighlow.highlowandroid.R;
import com.gethighlow.highlowandroid.model.Services.APIService;
import com.gethighlow.highlowandroid.model.Services.AuthService;

public class AgreementsActivity extends AppCompatActivity implements HLButtonDelegate {
    private HLButton acceptButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.agreements_activity);

        acceptButton = findViewById(R.id.accept);

        acceptButton.delegate = this;
    }

    public void decline(View view) {
        finish();
        AuthService.shared().logOut();
    }

    public void openTermsOfService(View view) {
        openURL("https://gethighlow.com/eula");
    }

    public void openPrivacyPolicy(View view) {
        openURL("https://gethighlow.com/eula");
    }

    private void openURL(String url) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    @Override
    public void onButtonClick(View view) {
        SharedPreferences.Editor editor = getSharedPreferences("com.gethighlow.SharedPref", Context.MODE_PRIVATE).edit();
        editor.putBoolean("hasAgreed", true);
        editor.apply();
        finish();
        APIService.shared().isAuthenticated();
    }
}

package com.gethighlow.highlowandroid.Activities.Tabs.Home;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.gethighlow.highlowandroid.CustomViews.Other.HighLowView;
import com.gethighlow.highlowandroid.R;
import com.gethighlow.highlowandroid.model.Managers.HighLowManager;

public class ViewHighLowActivity extends AppCompatActivity {

    private HighLowView highLowView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_high_low_activity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        highLowView = findViewById(R.id.highlowview);

        if (getIntent().getExtras() == null || !getIntent().getExtras().containsKey("highlowid")) return;

        String highlowid = getIntent().getExtras().getString("highlowid");

        HighLowManager.shared().getHighLow(highlowid, highLowLiveData -> {
            highLowView.attachToLiveData(this, highLowLiveData);
        }, error -> {

        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        finish();
        return true;
    }
}

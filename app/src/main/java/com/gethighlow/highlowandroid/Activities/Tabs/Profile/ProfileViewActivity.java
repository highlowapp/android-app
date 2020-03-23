package com.gethighlow.highlowandroid.Activities.Tabs.Profile;

import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.gethighlow.highlowandroid.Activities.Tabs.Profile.Profile;
import com.gethighlow.highlowandroid.R;

public class ProfileViewActivity extends AppCompatActivity implements Profile.OnFragmentInteractionListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_view_activity);

        getSupportActionBar().setTitle("Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Profile fragment = new Profile();
        String uid = getIntent().getStringExtra("uid");
        fragment.setUser(uid);
        ft.replace(R.id.profile_fragment, fragment);
        ft.commit();
    }

    @Override
    public void onProfileFragmentInteraction(Uri uri) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
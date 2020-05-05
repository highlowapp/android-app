package com.gethighlow.highlowandroid.Activities.Tabs.About;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;

import com.gethighlow.highlowandroid.R;
import com.gethighlow.highlowandroid.model.Services.SetActivityTheme;


import androidx.appcompat.app.AppCompatActivity;


import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.appcompat.widget.SwitchCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;


public class SetTheme extends AppCompatActivity {
    private SwitchCompat darkThemeSwitch;
    private AppCompatRadioButton lightTheme, darkTheme, autoTheme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SetActivityTheme.setTheme(this);



        setContentView(R.layout.set_theme_layout);

        getSupportActionBar().setTitle("Dark Theme");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        lightTheme = findViewById(R.id.lightTheme);
        darkTheme = findViewById(R.id.darkTheme);
        autoTheme = findViewById(R.id.autoTheme);

        lightTheme.setOnClickListener(setLightTheme);
        darkTheme.setOnClickListener(setDarkTheme);
        autoTheme.setOnClickListener(setAutoTheme);

        /**@Caleb can you please run this code on an Android Emulator with
         * API level lower than 29 to make sure this works properly? */
        if (android.os.Build.VERSION.SDK_INT >= 29){
            autoTheme.setVisibility(View.VISIBLE);
        } else{
            autoTheme.setVisibility(View.INVISIBLE);
        }



    }

    View.OnClickListener setLightTheme = new View.OnClickListener() {
        @Override
        public void onClick(View view) {


            //Update the shared preference with the current theme and broadcast an intent for all the activities to listen to
            String key = "current-theme";
            String value = "light";

            //Remove the current key in the shared preference so when we add a new key there won't be any conflict
            removeSharedPreferencesValues(key);
            //Update the empty shared preference with the new value
            editSharedPreferences(key, value);

            Intent newIntent = new Intent("theme-updated");
            LocalBroadcastManager.getInstance(SetTheme.this).sendBroadcast(newIntent);

            recreate();



        }
    };

    View.OnClickListener setDarkTheme = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            //Update the shared preference with the current theme and broadcast an intent for all the activities to listen to

            String key = "current-theme";
            String value = "dark";

            //Remove the current key in the shared preference so when we add a new key there won't be any conflict
            removeSharedPreferencesValues(key);
            //Update the empty shared preference with the new value
            editSharedPreferences(key, value);

            Intent newIntent = new Intent("theme-updated");
            LocalBroadcastManager.getInstance(SetTheme.this).sendBroadcast(newIntent);

            recreate();




        }
    };

    View.OnClickListener setAutoTheme = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //Update the shared preference with the current theme and broadcast an intent for all the activities to listen to

            String key = "current-theme";
            String value = "auto";

            //Remove the current key in the shared preference so when we add a new key there won't be any conflict
            removeSharedPreferencesValues(key);
            //Update the empty shared preference with the new value
            editSharedPreferences(key, value);

            Intent newIntent = new Intent("theme-updated");
            LocalBroadcastManager.getInstance(SetTheme.this).sendBroadcast(newIntent);

            recreate();


        }
    };

    private void editSharedPreferences(String key, String value ){

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        sharedPref.edit().putString(key, value).apply();
        recreate();

    }

    private void removeSharedPreferencesValues(String key){
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove(key);
        editor.apply();
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

package com.gethighlow.highlowandroid.model.Services;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceManager;

import com.gethighlow.highlowandroid.R;

import androidx.appcompat.app.AppCompatDelegate;

public class SetActivityTheme {
    public static String getTheme(Context context){
        int currentNightMode = context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;

        //Make "auto" the default theme
        String currentTheme = getCurrentTheme(context);


        if (currentTheme.equals("light")) {

            return "light";

        }else if(currentTheme.equals("dark")) {

            return "dark";

        }else if(currentTheme.equals("auto")) {

            if (currentNightMode == Configuration.UI_MODE_NIGHT_NO) {
                return "light";
            } else if (currentNightMode == Configuration.UI_MODE_NIGHT_YES) {
                return "dark";
            }
        }
        return currentTheme;

    }

    public static void setTheme(Context context){
        String theme = getTheme(context);
        if (theme.equals("light")) {
            context.setTheme(R.style.LightTheme);
        } else if(theme.equals("dark")){
            context.setTheme(R.style.DarkTheme);
        }
    }

    //Code that makes "auto" the default theme
    private static String getCurrentTheme(Context context){
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        if (android.os.Build.VERSION.SDK_INT >= 29){
            String currentTheme = sharedPref.getString("current-theme", "auto");
            return currentTheme;
        } else{
            String currentTheme = sharedPref.getString("current-theme", "light");
            return currentTheme;
        }
    }

}

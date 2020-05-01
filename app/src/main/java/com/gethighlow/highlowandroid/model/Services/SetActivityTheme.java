package com.gethighlow.highlowandroid.model.Services;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.gethighlow.highlowandroid.R;

public class SetActivityTheme {
    public static String getTheme(Context context){

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);

        String currentTheme = sharedPref.getString("current-theme", "light");

        if (currentTheme.equals("light"))

            return "light";
        else if(currentTheme.equals("dark"))
            return "dark";
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

}

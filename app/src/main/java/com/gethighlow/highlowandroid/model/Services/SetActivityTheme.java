package com.gethighlow.highlowandroid.model.Services;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;

import com.gethighlow.highlowandroid.R;

public class SetActivityTheme {
    public static String getTheme(Context context){

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);

        String currentTheme = sharedPref.getString("current-theme", "light");

        if (currentTheme.equals("light"))
            //context.setTheme(R.style.LightTheme);
            return "light";
        else if(currentTheme.equals("dark"))
            //context.setTheme(R.style.DarkTheme);
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

    public static LayoutInflater setFragmentTheme(LayoutInflater inflater, Context context){
        String currentTheme = getTheme(context);
        final ContextThemeWrapper theme = new ContextThemeWrapper(context, Integer.parseInt(currentTheme));
        LayoutInflater localInflater = inflater.cloneInContext(theme);

        return localInflater;

    }
}

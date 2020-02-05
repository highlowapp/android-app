package com.gethighlow.highlowandroid.Activities.Fragments;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.gethighlow.highlowandroid.thehayro.view.InfinitePagerAdapter;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static java.time.temporal.ChronoUnit.DAYS;

public class HomePagerAdapter extends InfinitePagerAdapter<LocalDate> {
    private Context context;

    public HomePagerAdapter(Context context, LocalDate current) {
        super(current);

        this.context = context;
    }

    public LocalDate getNextIndicator() {
        LocalDate currentDate = getCurrentIndicator().plusDays(1);
        return currentDate;
    }


    public LocalDate getPreviousIndicator() {
        LocalDate currentDate = getCurrentIndicator().plusDays(-1);
        return currentDate;
    }

    public ViewGroup instantiateItem(LocalDate indicator) {
        LinearLayout.LayoutParams newLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        HomeViewLayout homeViewLayout = new HomeViewLayout(context, null, indicator);
        homeViewLayout.setLayoutParams(newLayoutParams);
        homeViewLayout.setDate(indicator);
        return homeViewLayout;
    }
}

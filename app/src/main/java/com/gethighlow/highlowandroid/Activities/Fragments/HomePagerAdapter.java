package com.gethighlow.highlowandroid.Activities.Fragments;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
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
    private Fragment fragment;

    public HomePagerAdapter(Fragment fragment, LocalDate current) {
        super(current);

        this.fragment = fragment;
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
        HomeViewLayout homeViewLayout = new HomeViewLayout(fragment, null, indicator);
        homeViewLayout.setLayoutParams(newLayoutParams);
        homeViewLayout.setDate(indicator);
        return homeViewLayout;
    }
}

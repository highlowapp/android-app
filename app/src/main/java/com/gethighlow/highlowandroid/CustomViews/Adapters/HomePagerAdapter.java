package com.gethighlow.highlowandroid.CustomViews.Adapters;

import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import com.gethighlow.highlowandroid.Activities.Tabs.Home.HomeViewLayout;
import com.gethighlow.highlowandroid.thehayro.view.InfinitePagerAdapter;

import org.threeten.bp.LocalDate;

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

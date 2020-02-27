package com.gethighlow.highlowandroid.Activities.Tabs.Fragments;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gethighlow.highlowandroid.CustomViews.HighLowView;
import com.gethighlow.highlowandroid.CustomViews.HighLowViewDelegate;
import com.gethighlow.highlowandroid.R;
import com.gethighlow.highlowandroid.model.Managers.HighLowManager;
import com.gethighlow.highlowandroid.model.Managers.LiveDataModels.HighLowLiveData;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeViewLayout extends LinearLayout {
    private HighLowLiveData highLow;
    private Fragment fragment;
    private TextView textView;
    private HighLowView highLowView;

    public HomeViewLayout(Fragment fragment, AttributeSet attributeSet, LocalDate localDate) {
        super(fragment.getContext(), attributeSet);

        setup(fragment, attributeSet, localDate);
    }

    private void setup(Fragment fragment, @Nullable AttributeSet attributeSet, LocalDate localDate) {
        LayoutInflater inflater = (LayoutInflater) fragment.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.home_view_layout, this, true);
        this.fragment = fragment;
        this.setOrientation(LinearLayout.VERTICAL);
        this.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
        highLowView = findViewById(R.id.highlowview);
    }

    private String prettyFormat(LocalDate localDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return formatter.format(localDate);
    }

    public void setDate(LocalDate localDate) {
        HighLowManager.shared().getHighLowByDate(prettyFormat(localDate), highLowLiveData -> {
            highLow = highLowLiveData;
            highLowView.attachToLiveData(fragment, highLowLiveData);
            }, error -> {
            Log.w("ERROR", error);
        });
    }
}

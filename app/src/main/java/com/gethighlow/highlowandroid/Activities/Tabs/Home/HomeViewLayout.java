package com.gethighlow.highlowandroid.Activities.Tabs.Home;


import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.gethighlow.highlowandroid.CustomViews.Other.HighLowView;
import com.gethighlow.highlowandroid.R;
import com.gethighlow.highlowandroid.model.util.Consumer;
import com.gethighlow.highlowandroid.model.Managers.HighLowManager;
import com.gethighlow.highlowandroid.model.Managers.LiveDataModels.HighLowLiveData;

import org.threeten.bp.LocalDate;
import org.threeten.bp.format.DateTimeFormatter;

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

    public void setHighlowid(String highlowid) {
        HighLowManager.shared().getHighLow(highlowid, new Consumer<HighLowLiveData>() {
            @Override
            public void accept(HighLowLiveData highLowLiveData) {
                highLow = highLowLiveData;
                highLowView.attachToLiveData(fragment, highLowLiveData);
            }
        }, new Consumer<String>() {
            @Override
            public void accept(String s) {

            }
        });
    }

    public void setDate(LocalDate localDate) {
        HighLowManager.shared().getHighLowByDate(prettyFormat(localDate), new Consumer<HighLowLiveData>() {
            @Override
            public void accept(HighLowLiveData highLowLiveData) {
                highLow = highLowLiveData;
                highLowView.setDate(localDate);
                highLowView.attachToLiveData(fragment, highLowLiveData);
            }
        }, new Consumer<String>() {
            @Override
            public void accept(String error) {
            }
        });
    }
}

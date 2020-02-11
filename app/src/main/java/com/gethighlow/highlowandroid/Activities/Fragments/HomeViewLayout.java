package com.gethighlow.highlowandroid.Activities.Fragments;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.viewpager.widget.ViewPager;

import android.speech.tts.TextToSpeech;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gethighlow.highlowandroid.CustomViews.HighLowView;
import com.gethighlow.highlowandroid.CustomViews.HighLowViewDelegate;
import com.gethighlow.highlowandroid.R;
import com.gethighlow.highlowandroid.model.Managers.HighLowManager;
import com.gethighlow.highlowandroid.model.Managers.LiveDataModels.HighLowLiveData;
import com.gethighlow.highlowandroid.model.Resources.HighLow;

import org.w3c.dom.Text;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeViewLayout extends LinearLayout implements HighLowViewDelegate {
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
        highLowView.delegate = this;
    }

    private String prettyFormat(LocalDate localDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return formatter.format(localDate);
    }

    public void setDate(LocalDate localDate) {
        HighLowManager.shared().getHighLowByDate(prettyFormat(localDate), highLowLiveData -> {
            highLow = highLowLiveData;
            highLow.observe(fragment, onHighLowUpdate);
            highLowView.loadHighLow(highLow.getHighLow());
            highLow.test();
        }, error -> {
        });
    }

    private final Observer<HighLow> onHighLowUpdate = new Observer<HighLow>() {
        @Override
        public void onChanged(HighLow highLow) {
            highLowView.loadHighLow(highLow);
        }
    };

    public  void willAddHigh(String highlowid) {
        Log.w("Debug", "Adding High");
    }
    public void willAddLow(String highlowid) {

    }
    public void willEditHigh(String highlowid) {

    }
    public void willEditLow(String highlowid) {

    }
    public void willLike(String highlowid) {
        Log.w("Debug", "Like");
    }
    public void willUnLike(String highlowid) {

    }
    public void willFlag(String highlowid) {

    }
    public void willUnFlag(String highlowid) {

    }
}

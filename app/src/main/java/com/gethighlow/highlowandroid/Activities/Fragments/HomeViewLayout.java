package com.gethighlow.highlowandroid.Activities.Fragments;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.speech.tts.TextToSpeech;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gethighlow.highlowandroid.R;

import java.time.LocalDate;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeViewLayout extends LinearLayout {

    public HomeViewLayout(Context context, AttributeSet attributeSet, LocalDate localDate) {
        super(context, attributeSet);

        setup(context, attributeSet, localDate);
    }

    private void setup(Context context, @Nullable AttributeSet attributeSet, LocalDate localDate) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.home_view_layout, this, true);
        this.setOrientation(LinearLayout.VERTICAL);
        this.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
        setDate(localDate);
    }

    public void setDate(LocalDate localDate) {
        TextView textView = findViewById(R.id.fragment_date);
        textView.setText(localDate.toString());
    }

}

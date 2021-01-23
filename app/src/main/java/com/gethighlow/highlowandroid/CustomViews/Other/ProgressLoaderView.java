package com.gethighlow.highlowandroid.CustomViews.Other;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;
import com.gethighlow.highlowandroid.R;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import io.alterac.blurkit.BlurLayout;

public class ProgressLoaderView extends LinearLayout {

    private TextView loaderLabel;
    private LinearProgressIndicator progressIndicator;
    private Button skip;
    private LinearLayout progressLoaderViewRoot;
    private ViewGroup container;
    private BlurLayout blurLayout;

    public ProgressLoaderView(Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
        setup(context, attributeSet);
    }

    public void setup(Context context, @Nullable AttributeSet attributeSet) {

        //Create an inflater
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //Inflate our view
        inflater.inflate(R.layout.progress_loader, this, true);

        //Get the views we need
        progressIndicator = findViewById(R.id.progressIndicator);
        loaderLabel = findViewById(R.id.loaderLabel);
        skip = findViewById(R.id.skip);
        progressLoaderViewRoot = findViewById(R.id.progressLoaderViewRoot);
        blurLayout = findViewById(R.id.blurLayout);

        //Set the progress indicator color
        progressIndicator.setIndicatorColor( getResources().getColor(R.color.colorPrimary) );

        //Set the indicator to indeterminate
        progressIndicator.setIndeterminate(true);

        //Start invisible
        this.stopLoading();

        //Initially remove the skip button
        setSkipping(false);

    }

    public void setView(ViewGroup view) {

        //Set our container
        container = view;

    }

    public void startLoading() {

        try {
            //Add the blurring effect, set the visibility
            setVisibility(View.VISIBLE);
            blurLayout.startBlur();
        } catch (Exception error) {
            Log.e("Error", error.getLocalizedMessage());
        }

    }

    public void stopLoading() {

        try {
            //Set the visibility to gone and remove the blur
            setVisibility(View.GONE);
            blurLayout.pauseBlur();
        } catch (Exception error) {
            Log.e("Error", error.getLocalizedMessage());
        }

    }

    public void setSkipping(boolean state) {

        //Hide the skip button if it is set to false
        skip.setVisibility( state ? View.VISIBLE : View.GONE );

    }

    public void setTitle(String title) {

        //Set the title
        loaderLabel.setText(title);

    }

    public void setSkipOnClickListener(OnClickListener listener) {

        //Set the onClickListener for the skip button
        skip.setOnClickListener(listener);

    }

}

package com.gethighlow.highlowandroid.CustomViews.BaseComponents;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.gethighlow.highlowandroid.R;

public class HLButton extends RelativeLayout implements View.OnClickListener {

    private TextView textView;
    private ProgressBar progressBar;
    private Button button;
    private ImageView icon;

    public HLButton(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);

        setup(context, attributeSet);
    }

    public HLButton(Context context) {
        super(context);
        setup(context, null);
    }

    private void setup(Context context, @Nullable AttributeSet attributeSet) {
        if (attributeSet != null) {
            TypedArray a = context.obtainStyledAttributes(attributeSet, R.styleable.HLButton, 0, 0);

            String theme = a.getString(R.styleable.HLButton_buttonTheme);
            String onClick = a.getString(R.styleable.HLButton_android_onClick);
            int layout = R.layout.hl_button;

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            inflater.inflate(layout, this, true);
            textView = findViewById(R.id.textView);
            progressBar = findViewById(R.id.progressBar);
            icon = findViewById(R.id.icon);
            icon.setVisibility(View.INVISIBLE);
            button = findViewById(R.id.button);
            button.setOnClickListener(this);
            String title = a.getString(R.styleable.HLButton_android_text);
            if (title != null) {
                textView.setText(title);
            }

            String iconStr = a.getString(R.styleable.HLButton_buttonIcon);
            if (iconStr != null) {
                icon.setVisibility(View.VISIBLE);
            }

            if ("pink".equals(theme)) {
                this.setBackgroundResource(R.drawable.plain_button_background);
            } else if ("white".equals(theme)) {
                this.setBackgroundResource(R.drawable.white_button_background);
                this.textView.setTextColor(ContextCompat.getColor(this.getContext(), R.color.colorPrimary));
                this.progressBar.setIndeterminateTintList(ColorStateList.valueOf(ContextCompat.getColor(this.getContext(), R.color.colorPrimary)));
            } else {
                this.setBackgroundResource(R.drawable.gradient_button_background);
            }
            textView.bringToFront();

            progressBar.setVisibility(View.INVISIBLE);
        } else {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            inflater.inflate(R.layout.hl_button, this, true);
        }

    }

    public void startLoading() {
        progressBar.setVisibility(View.VISIBLE);
        textView.setVisibility(View.INVISIBLE);
    }

    public void stopLoading() {
        progressBar.setVisibility(View.INVISIBLE);
        textView.setVisibility(View.VISIBLE);
    }

    public HLButtonDelegate delegate;

    @Override
    public void onClick(View view) {
        if (delegate != null) {
            delegate.onButtonClick(this);
        }
    }

}


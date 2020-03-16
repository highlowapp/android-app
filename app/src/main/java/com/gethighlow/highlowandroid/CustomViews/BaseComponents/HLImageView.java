package com.gethighlow.highlowandroid.CustomViews.BaseComponents;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

import com.gethighlow.highlowandroid.R;

public class HLImageView extends AppCompatImageView {
    public HLImageView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);

        setup(context, attributeSet);
    }

    public void setup(Context context, AttributeSet attributeSet) {
        this.setBackgroundResource(R.color.very_light_gray);
    }
}

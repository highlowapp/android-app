package com.gethighlow.highlowandroid.CustomViews;

import android.view.View;

import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.gethighlow.highlowandroid.model.Managers.LiveDataModels.HighLowLiveData;

public class HighLowViewHolder extends RecyclerView.ViewHolder {
    private HighLowView highLowView;
    private LifecycleOwner lifecycleOwner;

    public HighLowViewHolder(View view) {
        super(view);

        highLowView = (HighLowView) view;
    }

    public void attachToLifecycleOwner(LifecycleOwner lifecycleOwner) {
        this.lifecycleOwner = lifecycleOwner;
    }

    public void setHighLow(HighLowLiveData highLowLiveData) {
        highLowView.attachToLiveData(lifecycleOwner, highLowLiveData);
    }
}

package com.gethighlow.highlowandroid.CustomViews;

import android.content.Intent;
import android.media.Image;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.gethighlow.highlowandroid.Activities.ProfileViewActivity;
import com.gethighlow.highlowandroid.R;
import com.gethighlow.highlowandroid.model.Managers.LiveDataModels.UserLiveData;
import com.gethighlow.highlowandroid.model.Resources.Interest;
import com.gethighlow.highlowandroid.model.Resources.User;
import com.gethighlow.highlowandroid.model.Services.AuthService;

public class InterestViewHolder extends RecyclerView.ViewHolder {
    private TextView titleView;
    private ImageView addButton;
    private int position;
    private Interest interest;
    public InterestViewHolderDelegate delegate;

    LifecycleOwner lifecycleOwner;

    public InterestViewHolder(View view) {
        super(view);

        titleView = view.findViewById(R.id.interestTitle);
        addButton = view.findViewById(R.id.addButton);

        addButton.setOnClickListener(addInterest);
    }

    View.OnClickListener addInterest = view -> {
        if (delegate != null) {
            delegate.addInterest(interest, position);
        }
    };

    public void setPosition(int pos) {
        position = pos;
    }

    public void attachToLifecycleOwner(LifecycleOwner lifecycleOwner) {
        this.lifecycleOwner = lifecycleOwner;
    }

    public void setInterest(Interest interest) {
        titleView.setText(interest.getName());
        this.interest = interest;
    }

}

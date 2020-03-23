package com.gethighlow.highlowandroid.CustomViews.ViewHolders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.gethighlow.highlowandroid.CustomViews.ViewHolders.Delegates.InterestViewHolderDelegate;
import com.gethighlow.highlowandroid.R;
import com.gethighlow.highlowandroid.model.Resources.Interest;

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

    View.OnClickListener addInterest = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (delegate != null) {
                delegate.addInterest(interest, position);
            }
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

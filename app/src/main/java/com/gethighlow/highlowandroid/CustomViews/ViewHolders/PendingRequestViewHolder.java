package com.gethighlow.highlowandroid.CustomViews.ViewHolders;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.gethighlow.highlowandroid.CustomViews.ViewHolders.Delegates.PendingRequestViewHolderDelegate;
import com.gethighlow.highlowandroid.R;
import com.gethighlow.highlowandroid.model.util.Consumer;
import com.gethighlow.highlowandroid.model.Managers.LiveDataModels.UserLiveData;
import com.gethighlow.highlowandroid.model.Resources.User;

public class PendingRequestViewHolder extends RecyclerView.ViewHolder {
    private PendingRequestViewHolderDelegate delegate;

    private ImageView userImage;
    private TextView userName;
    private ImageView chevron;
    private ImageView unFriend;
    private Button acceptButton;
    private Button rejectButton;

    private int position = -1;

    LifecycleOwner lifecycleOwner;

    public PendingRequestViewHolder(PendingRequestViewHolderDelegate delegate, View view) {
        super(view);
        this.delegate = delegate;
        userImage = view.findViewById(R.id.userImage);
        userName = view.findViewById(R.id.userName);
        acceptButton = view.findViewById(R.id.acceptButton);
        rejectButton = view.findViewById(R.id.rejectButton);

        acceptButton.setOnClickListener(accept);
        rejectButton.setOnClickListener(reject);
    }

    View.OnClickListener accept = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            delegate.requestAccepted(position);
        }
    };

    View.OnClickListener reject = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            delegate.requestRejected(position);
        }
    };

    public void setPosition(int pos) {
        position = pos;
    }

    public void attachToLifecycleOwner(LifecycleOwner lifecycleOwner) {
        this.lifecycleOwner = lifecycleOwner;
    }

    public void setUser(UserLiveData userLiveData) {
        userLiveData.observe(lifecycleOwner, userLiveDataObserver);
    }

    private Observer<User> userLiveDataObserver = new Observer<User>() {
        @Override
        public void onChanged(User user) {
            userImage.setImageBitmap(null);
            userName.setText("");
            user.fetchProfileImage(new Consumer<Bitmap>() {
                @Override
                public void accept(Bitmap bitmap) {
                    userImage.setImageBitmap(bitmap);
                }
            }, new Consumer<String>() {
                @Override
                public void accept(String error) {

                }
            });

            userName.setText(user.name());
        }
    };

}

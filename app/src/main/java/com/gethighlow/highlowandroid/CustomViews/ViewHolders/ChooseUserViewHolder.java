package com.gethighlow.highlowandroid.CustomViews.ViewHolders;

import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.SwitchCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.gethighlow.highlowandroid.Activities.Tabs.Profile.ProfileViewActivity;
import com.gethighlow.highlowandroid.CustomViews.ViewHolders.Delegates.ChooseUserRowViewHolderDelegate;
import com.gethighlow.highlowandroid.CustomViews.ViewHolders.Delegates.UserRowViewHolderDelegate;
import com.gethighlow.highlowandroid.R;
import com.gethighlow.highlowandroid.model.util.Consumer;
import com.gethighlow.highlowandroid.model.Managers.LiveDataModels.UserLiveData;
import com.gethighlow.highlowandroid.model.Resources.User;
import com.gethighlow.highlowandroid.model.Services.AuthService;

public class ChooseUserViewHolder extends RecyclerView.ViewHolder {
    private ImageView userImage;
    private TextView userName;
    private SwitchCompat switchCompat;

    private int position;

    private ChooseUserRowViewHolderDelegate delegate;

    private CompoundButton.OnCheckedChangeListener onSwitchStateChanged = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

            //Notify the delegate
            delegate.onSwitchStateChanged(b, uid);

        }
    };

    String uid = AuthService.shared().getUid();

    LifecycleOwner lifecycleOwner;

    public ChooseUserViewHolder(LifecycleOwner lifecycleOwner, ChooseUserRowViewHolderDelegate delegate, View view) {
        super(view);
        this.delegate = delegate;
        this.lifecycleOwner = lifecycleOwner;

        userImage = view.findViewById(R.id.userImage);
        userName = view.findViewById(R.id.userName);
        switchCompat = view.findViewById(R.id.toggle);

        switchCompat.setOnCheckedChangeListener(onSwitchStateChanged);
    }

    public void setSwitch(boolean state) {

        //Set the switch state
        switchCompat.setChecked(state);

    }

    public void setPosition(int pos) {
        position = pos;
    }

    public void setUser(UserLiveData userLiveData) {
        userLiveData.observe(lifecycleOwner, userLiveDataObserver);
    }

    private Observer<User> userLiveDataObserver = new Observer<User>() {
        @Override
        public void onChanged(User user) {

            uid = user.uid();
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

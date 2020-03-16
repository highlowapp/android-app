package com.gethighlow.highlowandroid.CustomViews;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.gethighlow.highlowandroid.Activities.ProfileViewActivity;
import com.gethighlow.highlowandroid.R;
import com.gethighlow.highlowandroid.model.Managers.LiveDataModels.UserLiveData;
import com.gethighlow.highlowandroid.model.Resources.User;
import com.gethighlow.highlowandroid.model.Services.AuthService;

public class UserRowViewHolder extends RecyclerView.ViewHolder {
    private ImageView userImage;
    private TextView userName;
    private ImageView chevron;
    private ImageView unFriend;
    private ImageView addAsFriend;
    private int position;
    private UserRowViewHolderDelegate delegate;
    String uid = AuthService.shared().getUid();

    LifecycleOwner lifecycleOwner;

    public UserRowViewHolder(UserRowViewHolderDelegate delegate, View view) {
        super(view);
        this.delegate = delegate;

        userImage = view.findViewById(R.id.userImage);
        userName = view.findViewById(R.id.userName);
        chevron = view.findViewById(R.id.chevron);
        unFriend = view.findViewById(R.id.unfriend);
        addAsFriend = view.findViewById(R.id.addAsFriend);
        unFriend.setOnClickListener(delete);

        view.setOnClickListener(navigate);

        addAsFriend.setVisibility(View.GONE);
        addAsFriend.setOnClickListener(addAsFriendListener);
        setEditable(false);
    }

    public void isAddFriendCell() {
        addAsFriend.setVisibility(View.VISIBLE);
        chevron.setVisibility(View.GONE);
        unFriend.setVisibility(View.GONE);
    }

    public void setPosition(int pos) {
        position = pos;
    }

    private void setEditable(Boolean value) {
        if (value) {
            chevron.setVisibility(View.GONE);
            unFriend.setVisibility(View.VISIBLE);
        } else {
            chevron.setVisibility(View.VISIBLE);
            unFriend.setVisibility(View.GONE);
        }
    }

    public void showDelete() {
        setEditable(true);
    }

    public void hideDelete() {
        setEditable(false);
    }

    private View.OnClickListener delete = view -> delegate.unFriend(position);

    private View.OnClickListener navigate = view -> {
        Intent starter = new Intent(view.getContext(), ProfileViewActivity.class);
        starter.putExtra("uid", uid);
        view.getContext().startActivity(starter);
    };

    private View.OnClickListener addAsFriendListener = view -> delegate.addFriend(position);

    public void attachToLifecycleOwner(LifecycleOwner lifecycleOwner) {
        this.lifecycleOwner = lifecycleOwner;
    }

    public void setUser(UserLiveData userLiveData) {
        userLiveData.observe(lifecycleOwner, userLiveDataObserver);
    }

    private Observer<User> userLiveDataObserver = user -> {
        uid = user.uid();
        userImage.setImageBitmap(null);
        userName.setText("");
        user.fetchProfileImage(bitmap -> {
            userImage.setImageBitmap(bitmap);
        }, error -> {

        });

        userName.setText(user.name());
    };

}

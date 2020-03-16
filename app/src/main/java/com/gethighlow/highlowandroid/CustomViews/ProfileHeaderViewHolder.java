package com.gethighlow.highlowandroid.CustomViews;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.gethighlow.highlowandroid.Activities.EditProfileActivity;
import com.gethighlow.highlowandroid.Activities.FriendsActivity;
import com.gethighlow.highlowandroid.R;
import com.gethighlow.highlowandroid.model.Managers.LiveDataModels.UserLiveData;
import com.gethighlow.highlowandroid.model.Resources.Interest;
import com.gethighlow.highlowandroid.model.Resources.User;
import com.gethighlow.highlowandroid.model.Services.AuthService;

import java.util.ArrayList;
import java.util.List;

import co.lujun.androidtagview.TagContainerLayout;

public class ProfileHeaderViewHolder extends RecyclerView.ViewHolder {
    private ImageView profileImage;
    private TextView streak;
    public TextView name;
    private TextView bio;
    private Button manageFriends;
    private Button editProfile;
    private TagContainerLayout tagContainerLayout;

    private User currentUser;

    private LifecycleOwner lifecycleOwner;

    public ProfileHeaderViewHolder(View view) {
        super(view);

        profileImage = view.findViewById(R.id.profile_header_image);
        streak = view.findViewById(R.id.streak);
        name = view.findViewById(R.id.user_name);
        bio = view.findViewById(R.id.bio_text);
        manageFriends = view.findViewById(R.id.manageFriends);
        editProfile = view.findViewById(R.id.editProfile);
        tagContainerLayout = view.findViewById(R.id.interests);

        editProfile.setOnClickListener(editProfileClickListener);
        manageFriends.setOnClickListener(manageFriendsClickListener);
    }

    private View.OnClickListener editProfileClickListener = view -> {
        Intent starter = new Intent(view.getContext(), EditProfileActivity.class);
        starter.putExtra("firstName", currentUser.getFirstname());
        starter.putExtra("lastName", currentUser.getLastname());
        starter.putExtra("bio", currentUser.bio());
        starter.putExtra("profileImage", currentUser.getProfileImageUrl());
        starter.putStringArrayListExtra("interests", new ArrayList<>(currentUser.interests()));
        view.getContext().startActivity(starter);
    };

    private View.OnClickListener manageFriendsClickListener = view -> {
        Intent starter = new Intent(view.getContext(), FriendsActivity.class);
        starter.putExtra("uid", currentUser.uid());
        view.getContext().startActivity(starter);
    };

    void attachToLifecycleOwner(LifecycleOwner lifecycleOwner) {
        this.lifecycleOwner = lifecycleOwner;
    }

    public void setUser(UserLiveData user) {
        if (user != null) {
            user.observe(lifecycleOwner, userLiveDataObserver);
        }
    }

    private Observer<User> userLiveDataObserver = user -> {
        this.currentUser = user;
        user.fetchProfileImage(bitmap -> {
            profileImage.setImageBitmap(bitmap);
        }, error -> {

        });
        name.setText(user.name());
        bio.setText(user.bio());
        streak.setText(Integer.toString(user.streak()));

        if (!user.uid().equals(AuthService.shared().getUid())) {
            editProfile.setVisibility(View.GONE);
        }

        tagContainerLayout.setTags(user.interests());
    };
}

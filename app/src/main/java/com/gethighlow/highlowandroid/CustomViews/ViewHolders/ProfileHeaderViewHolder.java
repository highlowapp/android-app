package com.gethighlow.highlowandroid.CustomViews.ViewHolders;

import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.gethighlow.highlowandroid.Activities.Tabs.Profile.EditProfileActivity;
import com.gethighlow.highlowandroid.Activities.Tabs.Profile.FriendsActivity;
import com.gethighlow.highlowandroid.R;
import com.gethighlow.highlowandroid.model.util.Consumer;
import com.gethighlow.highlowandroid.model.Managers.LiveDataModels.UserLiveData;
import com.gethighlow.highlowandroid.model.Resources.User;
import com.gethighlow.highlowandroid.model.Services.AuthService;

import java.util.ArrayList;

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

    private View.OnClickListener editProfileClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent starter = new Intent(view.getContext(), EditProfileActivity.class);
            starter.putExtra("firstName", currentUser.getFirstname());
            starter.putExtra("lastName", currentUser.getLastname());
            starter.putExtra("bio", currentUser.bio());
            starter.putExtra("profileImage", currentUser.getProfileImageUrl());
            starter.putStringArrayListExtra("interests", new ArrayList<String>(currentUser.interests()));
            view.getContext().startActivity(starter);
        }
    };

    private View.OnClickListener manageFriendsClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent starter = new Intent(view.getContext(), FriendsActivity.class);
            starter.putExtra("uid", currentUser.uid());
            view.getContext().startActivity(starter);
        }
    };

    public void attachToLifecycleOwner(LifecycleOwner lifecycleOwner) {
        this.lifecycleOwner = lifecycleOwner;
    }

    public void setUser(UserLiveData user) {
        if (user != null) {
            user.observe(lifecycleOwner, userLiveDataObserver);
            this.currentUser = user.getUser();
        }
    }

    private Observer<User> userLiveDataObserver = new Observer<User>() {
        @Override
        public void onChanged(User user) {
            ProfileHeaderViewHolder.this.currentUser = user;
            user.fetchProfileImage(new Consumer<Bitmap>() {
                @Override
                public void accept(Bitmap bitmap) {
                    profileImage.setImageBitmap(bitmap);
                }
            }, new Consumer<String>() {
                @Override
                public void accept(String error) {

                }
            });
            name.setText(user.name());
            bio.setText(user.bio());
            streak.setText(Integer.toString(user.streak()));

            if (!user.uid().equals(AuthService.shared().getUid())) {
                editProfile.setVisibility(View.GONE);
            }

            if (user.interests() != null) {
                tagContainerLayout.setTags(user.interests());
            }
        }
    };
}

package com.gethighlow.highlowandroid.CustomViews;

import android.app.PendingIntent;
import android.content.Intent;
import android.util.Log;
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
import co.lujun.androidtagview.TagView;

public class EditInterestsHeaderViewHolder extends RecyclerView.ViewHolder {
    private TagContainerLayout tagContainerLayout;

    private LifecycleOwner lifecycleOwner;

    private List<String> interestIds = new ArrayList<>();
    public List<Interest> interests = new ArrayList<>();

    public EditInterestsHeaderViewHolder(View view) {
        super(view);

        tagContainerLayout = view.findViewById(R.id.interests);

        tagContainerLayout.setOnTagClickListener(onTagClickListener);

        getUserInterests();
    }

    public void getUserInterests() {
        User.getInterests(interestResponse -> {
            interestIds.clear();
            interests.clear();
            tagContainerLayout.removeAllTags();
            for (Interest interest: interestResponse.getInterests()) {
                interests.add(interest);
                interestIds.add(interest.getInterestid());
                tagContainerLayout.addTag(interest.getName());
            }
        }, error -> {

        });
    }

    private TagView.OnTagClickListener onTagClickListener = new TagView.OnTagClickListener() {
        @Override
        public void onTagClick(int position, String text) {

        }

        @Override
        public void onTagLongClick(int position, String text) {

        }

        @Override
        public void onSelectedTagDrag(int position, String text) {

        }

        @Override
        public void onTagCrossClick(int position) {
            String interestId = interestIds.get(position);
            Log.w("Debug", interestId);
            User.removeInterest(interestId, genericResponse -> {
                tagContainerLayout.removeTag(position);
                interestIds.remove(position);
                removeInterest(interestId);
            }, error -> {

            });
        }
    };

    private void removeInterest(String id) {
        for (Interest interest: interests) {
            if (interest.getInterestid().equals(id)) {
                interests.remove(interest);
                break;
            }
        }
    }

    void attachToLifecycleOwner(LifecycleOwner lifecycleOwner) {
        this.lifecycleOwner = lifecycleOwner;
    }
}

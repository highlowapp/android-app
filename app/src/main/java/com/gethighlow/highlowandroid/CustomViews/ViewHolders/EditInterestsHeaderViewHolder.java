package com.gethighlow.highlowandroid.CustomViews.ViewHolders;

import android.view.View;

import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.gethighlow.highlowandroid.R;
import com.gethighlow.highlowandroid.model.util.Consumer;
import com.gethighlow.highlowandroid.model.Resources.Interest;
import com.gethighlow.highlowandroid.model.Resources.User;
import com.gethighlow.highlowandroid.model.Responses.GenericResponse;
import com.gethighlow.highlowandroid.model.Responses.InterestResponse;

import java.util.ArrayList;
import java.util.List;

import co.lujun.androidtagview.TagContainerLayout;
import co.lujun.androidtagview.TagView;

public class EditInterestsHeaderViewHolder extends RecyclerView.ViewHolder {
    private TagContainerLayout tagContainerLayout;

    private LifecycleOwner lifecycleOwner;

    private List<String> interestIds = new ArrayList<String>();
    public List<Interest> interests = new ArrayList<Interest>();

    public EditInterestsHeaderViewHolder(View view) {
        super(view);

        tagContainerLayout = view.findViewById(R.id.interests);

        tagContainerLayout.setOnTagClickListener(onTagClickListener);

        getUserInterests();
    }

    public void getUserInterests() {
        User.getInterests(new Consumer<InterestResponse>() {
            @Override
            public void accept(InterestResponse interestResponse) {
                interestIds.clear();
                interests.clear();
                tagContainerLayout.removeAllTags();
                for (Interest interest : interestResponse.getInterests()) {
                    interests.add(interest);
                    interestIds.add(interest.getInterestid());
                    tagContainerLayout.addTag(interest.getName());
                }
            }
        }, new Consumer<String>() {
            @Override
            public void accept(String error) {

            }
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
        public void onTagCrossClick(final int position) {
            final String interestId = interestIds.get(position);
            User.removeInterest(interestId, new Consumer<GenericResponse>() {
                @Override
                public void accept(GenericResponse genericResponse) {
                    tagContainerLayout.removeTag(position);
                    interestIds.remove(position);
                    removeInterest(interestId);
                }
            }, new Consumer<String>() {
                @Override
                public void accept(String error) {

                }
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

    public void attachToLifecycleOwner(LifecycleOwner lifecycleOwner) {
        this.lifecycleOwner = lifecycleOwner;
    }
}

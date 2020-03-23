package com.gethighlow.highlowandroid.CustomViews.Sections;

import android.view.View;

import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.gethighlow.highlowandroid.CustomViews.Sections.Delegates.FriendsSectionDelegate;
import com.gethighlow.highlowandroid.CustomViews.ViewHolders.Delegates.UserRowViewHolderDelegate;
import com.gethighlow.highlowandroid.CustomViews.ViewHolders.UserRowViewHolder;
import com.gethighlow.highlowandroid.R;
import com.gethighlow.highlowandroid.model.Managers.LiveDataModels.UserLiveData;

import java.util.List;

import io.github.luizgrp.sectionedrecyclerviewadapter.Section;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.utils.EmptyViewHolder;

public class FriendsSection extends Section implements UserRowViewHolderDelegate {
    private List<UserLiveData> friends;
    private LifecycleOwner lifecycleOwner;

    private FriendsSectionDelegate delegate;

    private boolean editable = false;

    public FriendsSection(FriendsSectionDelegate delegate, LifecycleOwner lifecycleOwner, List<UserLiveData> requests) {
        super(SectionParameters.builder().itemResourceId(R.layout.user_row_item).headerResourceId(R.layout.your_friends_header).build());
        this.lifecycleOwner = lifecycleOwner;
        this.friends = requests;
        this.delegate = delegate;
    }

    public void toggleEditable() {
        editable = !editable;
    }

    @Override
    public int getContentItemsTotal() {
        return friends.size();
    }

    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        UserRowViewHolder userRowViewHolder = new UserRowViewHolder(this, view);
        userRowViewHolder.attachToLifecycleOwner(lifecycleOwner);
        userRowViewHolder.hideDelete();
        return userRowViewHolder;
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        UserRowViewHolder userRowViewHolder = (UserRowViewHolder) holder;
        if (editable) userRowViewHolder.showDelete();
        else userRowViewHolder.hideDelete();
        UserLiveData userLiveData = friends.get(position);
        userLiveData.removeObservers(lifecycleOwner);
        userRowViewHolder.setPosition(position);
        userRowViewHolder.setUser(userLiveData);
    }

    @Override
    public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
        return new EmptyViewHolder(view);
    }

    @Override
    public void unFriend(int position) {
        delegate.unFriend(position);
    }

    @Override
    public void addFriend(int position) {

    }
}

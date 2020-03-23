package com.gethighlow.highlowandroid.CustomViews.Sections;

import android.view.View;

import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.gethighlow.highlowandroid.CustomViews.Sections.Delegates.PendingRequestsSectionDelegate;
import com.gethighlow.highlowandroid.CustomViews.ViewHolders.Delegates.PendingRequestViewHolderDelegate;
import com.gethighlow.highlowandroid.CustomViews.ViewHolders.PendingRequestViewHolder;
import com.gethighlow.highlowandroid.R;
import com.gethighlow.highlowandroid.model.Managers.LiveDataModels.UserLiveData;

import java.util.List;

import io.github.luizgrp.sectionedrecyclerviewadapter.Section;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.utils.EmptyViewHolder;

public class PendingRequestsSection extends Section implements PendingRequestViewHolderDelegate {
    private List<UserLiveData> requests;
    private LifecycleOwner lifecycleOwner;
    private PendingRequestsSectionDelegate delegate;
    public PendingRequestsSection(PendingRequestsSectionDelegate delegate, LifecycleOwner lifecycleOwner, List<UserLiveData> requests) {
        super(
                SectionParameters.builder().itemResourceId(R.layout.pending_request).headerResourceId(R.layout.pending_requests_header).build()
        );
        this.delegate = delegate;
        this.lifecycleOwner = lifecycleOwner;
        this.requests = requests;
    }

    @Override
    public int getContentItemsTotal() {
        return requests.size();
    }

    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        PendingRequestViewHolder pendingRequestViewHolder = new PendingRequestViewHolder(this, view);
        pendingRequestViewHolder.attachToLifecycleOwner(lifecycleOwner);
        return pendingRequestViewHolder;
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        PendingRequestViewHolder pendingRequestViewHolder = (PendingRequestViewHolder) holder;
        UserLiveData userLiveData = requests.get(position);
        userLiveData.removeObservers(lifecycleOwner);
        pendingRequestViewHolder.setPosition(position);
        pendingRequestViewHolder.setUser(userLiveData);
    }

    @Override
    public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
        return new EmptyViewHolder(view);
    }

    @Override
    public void requestAccepted(int position) {
        delegate.acceptFriend(position);
    }

    @Override
    public void requestRejected(int position) {
        delegate.rejectFriend(position);
    }
}

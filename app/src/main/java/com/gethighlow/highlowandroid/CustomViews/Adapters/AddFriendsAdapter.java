package com.gethighlow.highlowandroid.CustomViews.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.gethighlow.highlowandroid.CustomViews.ViewHolders.Delegates.UserRowViewHolderDelegate;
import com.gethighlow.highlowandroid.CustomViews.ViewHolders.ProfileHeaderViewHolder;
import com.gethighlow.highlowandroid.CustomViews.ViewHolders.UserRowViewHolder;
import com.gethighlow.highlowandroid.R;
import com.gethighlow.highlowandroid.model.Managers.LiveDataModels.UserLiveData;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AddFriendsAdapter extends RecyclerView.Adapter<UserRowViewHolder> {
    private List<UserLiveData> users;

    public ProfileHeaderViewHolder profileHeaderViewHolder;

    private LifecycleOwner lifecycleOwner;

    private UserRowViewHolderDelegate delegate;

    public AddFriendsAdapter(LifecycleOwner lifecycleOwner, UserRowViewHolderDelegate delegate, List<UserLiveData> users) {
        this.lifecycleOwner = lifecycleOwner;
        this.delegate = delegate;
        this.users = users;
    }

    @NotNull
    @Override
    public UserRowViewHolder onCreateViewHolder(@NotNull ViewGroup viewGroup, int viewType) {
        View view;
        view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.user_row_item, viewGroup, false);
        UserRowViewHolder userRowViewHolder = new UserRowViewHolder(delegate, view);
        userRowViewHolder.attachToLifecycleOwner(lifecycleOwner);
        userRowViewHolder.isAddFriendCell();
        return userRowViewHolder;
    }

    @Override
    public void onBindViewHolder(@NotNull UserRowViewHolder viewHolder, int position) {
        viewHolder.setUser(users.get(position));
        viewHolder.setPosition(position);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }
}

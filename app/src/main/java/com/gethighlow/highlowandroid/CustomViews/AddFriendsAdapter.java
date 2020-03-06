package com.gethighlow.highlowandroid.CustomViews;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SortedList;

import com.gethighlow.highlowandroid.R;
import com.gethighlow.highlowandroid.model.Managers.LiveDataModels.HighLowLiveData;
import com.gethighlow.highlowandroid.model.Managers.LiveDataModels.UserLiveData;
import com.gethighlow.highlowandroid.model.Resources.SearchItem;

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

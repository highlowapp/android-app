package com.gethighlow.highlowandroid.CustomViews;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.gethighlow.highlowandroid.R;
import com.gethighlow.highlowandroid.model.Managers.LiveDataModels.HighLowLiveData;
import com.gethighlow.highlowandroid.model.Managers.LiveDataModels.UserLiveData;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ProfileAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<HighLowLiveData> highLows;
    private UserLiveData user;



    public ProfileHeaderViewHolder profileHeaderViewHolder;

    private int HEADER = 0;
    private int HIGHLOW = 1;

    private LifecycleOwner lifecycleOwner;

    public ProfileAdapter(LifecycleOwner lifecycleOwner, UserLiveData user, List<HighLowLiveData> highLows) {
        this.lifecycleOwner = lifecycleOwner;
        this.highLows = highLows;
        this.user = user;
    }

    public void setUser(UserLiveData userLiveData) {
        this.user = userLiveData;
    }

    @Override
    public int getItemViewType(int position) {
        return (position == 0) ? HEADER: HIGHLOW;
    }

    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NotNull ViewGroup viewGroup, int viewType) {
        View view;
        if (viewType == HEADER) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.profile_header, viewGroup, false);

            profileHeaderViewHolder = new ProfileHeaderViewHolder(view);
            return profileHeaderViewHolder;
        }
        else {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.highlow_view_holder, viewGroup, false);
            return new HighLowViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NotNull RecyclerView.ViewHolder viewHolder, int position) {
        if (position == 0) {
            ((ProfileHeaderViewHolder) viewHolder).attachToLifecycleOwner(lifecycleOwner);
            ((ProfileHeaderViewHolder) viewHolder).setUser(user);
            return;
        }
        ((HighLowViewHolder) viewHolder).attachToLifecycleOwner(lifecycleOwner);

        ((HighLowViewHolder) viewHolder).setHighLow(highLows.get(position - 1));
    }

    @Override
    public int getItemCount() {
        return highLows.size() + 1;
    }
}

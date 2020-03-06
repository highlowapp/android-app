package com.gethighlow.highlowandroid.CustomViews;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.gethighlow.highlowandroid.R;
import com.gethighlow.highlowandroid.model.Managers.LiveDataModels.HighLowLiveData;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FeedAdapter extends RecyclerView.Adapter<HighLowViewHolder> {
    private List<HighLowLiveData> highLows;

    private LifecycleOwner lifecycleOwner;

    public FeedAdapter(LifecycleOwner lifecycleOwner, List<HighLowLiveData> highLows) {
        this.lifecycleOwner = lifecycleOwner;
        this.highLows = highLows;
    }

    @NotNull
    @Override
    public HighLowViewHolder onCreateViewHolder(@NotNull ViewGroup viewGroup, int viewType) {
        View view;
        view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.highlow_view_holder, viewGroup, false);
        return new HighLowViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NotNull HighLowViewHolder viewHolder, int position) {
        viewHolder.attachToLifecycleOwner(lifecycleOwner);
        viewHolder.setHighLow(highLows.get(position));
    }

    @Override
    public int getItemCount() {
        return highLows.size();
    }
}

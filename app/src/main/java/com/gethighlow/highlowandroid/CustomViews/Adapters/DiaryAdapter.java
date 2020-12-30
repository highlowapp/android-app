package com.gethighlow.highlowandroid.CustomViews.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gethighlow.highlowandroid.Activities.Tabs.Diary.Diary;
import com.gethighlow.highlowandroid.CustomViews.ViewHolders.DiaryViewHolder;
import com.gethighlow.highlowandroid.CustomViews.ViewHolders.HighLowViewHolder;
import com.gethighlow.highlowandroid.CustomViews.ViewHolders.ProfileHeaderViewHolder;
import com.gethighlow.highlowandroid.R;
import com.gethighlow.highlowandroid.model.Managers.LiveDataModels.ActivityLiveData;
import com.gethighlow.highlowandroid.model.Managers.LiveDataModels.HighLowLiveData;
import com.gethighlow.highlowandroid.model.Managers.LiveDataModels.UserLiveData;
import com.gethighlow.highlowandroid.model.Services.ActivityService;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

public class DiaryAdapter extends RecyclerView.Adapter<DiaryViewHolder> {
    Context context;
    private ArrayList<ActivityLiveData> activities;
    public Diary diary;
    private UserLiveData user;



    private LifecycleOwner lifecycleOwner;

    public DiaryAdapter(LifecycleOwner lifecycleOwner, ArrayList<ActivityLiveData> activities) {
        this.context = context;
        this.lifecycleOwner = lifecycleOwner;
        this.activities = activities;
    }




    @NotNull
    @Override
    public DiaryViewHolder onCreateViewHolder(@NotNull ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.diary_card_layout, viewGroup, false);
        return new DiaryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DiaryViewHolder holder, int position) {
        holder.attachToLifecycleOwner(lifecycleOwner);
        holder.setDiaryEntry(activities.get(position));
    }

    @Override
    public int getItemCount() {
        return activities.size();
    }

}

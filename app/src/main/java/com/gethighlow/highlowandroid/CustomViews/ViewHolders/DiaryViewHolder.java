package com.gethighlow.highlowandroid.CustomViews.ViewHolders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gethighlow.highlowandroid.R;
import com.gethighlow.highlowandroid.model.Managers.LiveDataModels.ActivityLiveData;
import com.gethighlow.highlowandroid.model.Resources.Activity;

import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

public class DiaryViewHolder extends RecyclerView.ViewHolder {
    private Activity activity;
    private ImageView typeImage;
    private TextView title;
    private LifecycleOwner lifecycleOwner;

    public DiaryViewHolder(View view) {
        super(view);

        typeImage = itemView.findViewById(R.id.diary_image);
        title = itemView.findViewById(R.id.diary_title);
    }

    public void attachToLifecycleOwner(LifecycleOwner lifecycleOwner) {
        this.lifecycleOwner = lifecycleOwner;
    }

    public void setDiaryEntry(ActivityLiveData activityLiveData){
        title.setText(activityLiveData.getTitle());
        String type = activityLiveData.getType();
        if(type != null) {
            if (type.equals("highlow")) {
                typeImage.setImageResource(R.drawable.logo_light);
            } else if (type.equals("audio")) {
                typeImage.setImageResource(R.drawable.ic_diary_entry);
            } else if (type.equals("diary")) {
                typeImage.setImageResource(R.drawable.ic_diary_entry);
            }
        }
    }
}

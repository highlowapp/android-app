package com.gethighlow.highlowandroid.CustomViews.ViewHolders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gethighlow.highlowandroid.CustomViews.Adapters.DiaryEntryClickListener;
import com.gethighlow.highlowandroid.R;
import com.gethighlow.highlowandroid.model.Managers.LiveDataModels.ActivityLiveData;
import com.gethighlow.highlowandroid.model.Resources.Activity;

import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

public class DiaryViewHolder extends RecyclerView.ViewHolder {
    private ActivityLiveData activity;
    private ImageView typeImage;
    private TextView title;
    private ImageView moreButton;
    private LifecycleOwner lifecycleOwner;

    private DiaryEntryClickListener diaryEntryClickListener;

    public DiaryViewHolder(View view) {
        super(view);

        typeImage = itemView.findViewById(R.id.diary_image);
        title = itemView.findViewById(R.id.diary_title);
        moreButton = itemView.findViewById(R.id.moreButton);

    }

    public void setDiaryEntryClickListener(DiaryEntryClickListener listener) {
        diaryEntryClickListener = listener;

        if (itemView != null) {

            //Set the itemView click listener
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //Forward to the DiaryEntryClickListener
                    diaryEntryClickListener.onItemTapped(activity);

                }
            });

        }

        if (moreButton != null) {

            //Set the more button click listener
            moreButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //Forward to the DiaryEntryClickListener
                    diaryEntryClickListener.onMoreButtonTapped(activity);

                }
            });

        }
    }

    public void attachToLifecycleOwner(LifecycleOwner lifecycleOwner) {
        this.lifecycleOwner = lifecycleOwner;
    }

    public void setDiaryEntry(ActivityLiveData activityLiveData){

        //Set the title text
        title.setText(activityLiveData.getTitle());

        //Set the type
        String type = activityLiveData.getType();

        //Use the type to get the right image
        if(type != null) {
            if (type.equals("highlow")) {
                typeImage.setImageResource(R.drawable.highlow_small_icon);
            } else if (type.equals("audio")) {
                typeImage.setImageResource(R.drawable.ic_diary_entry);
            } else if (type.equals("diary")) {
                typeImage.setImageResource(R.drawable.ic_diary_entry);
            }
        }

        //Set the activity
        this.activity = activityLiveData;

    }
}

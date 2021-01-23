package com.gethighlow.highlowandroid.CustomViews.Adapters;

import com.gethighlow.highlowandroid.model.Managers.LiveDataModels.ActivityLiveData;

public interface DiaryEntryClickListener {
    public void onItemTapped(ActivityLiveData item);
    public void onMoreButtonTapped(ActivityLiveData item);
}

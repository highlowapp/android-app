package com.gethighlow.highlowandroid.Activities.Tabs.Fragments;

import androidx.lifecycle.ViewModel;

import com.gethighlow.highlowandroid.model.Managers.LiveDataModels.HighLowLiveData;

import java.util.function.Consumer;

public class HomeViewLayoutModel extends ViewModel {
    private HighLowLiveData highLow;

    public void loadHighLowForDate(String date, Consumer<HighLowLiveData> onSuccess, Consumer<String> onError) {

    }
}

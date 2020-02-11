package com.gethighlow.highlowandroid.Activities.Fragments;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.gethighlow.highlowandroid.model.Managers.LiveDataModels.HighLowLiveData;

import java.util.function.Consumer;

public class HomeViewLayoutModel extends ViewModel {
    private HighLowLiveData highLow;

    public void loadHighLowForDate(String date, Consumer<HighLowLiveData> onSuccess, Consumer<String> onError) {

    }
}

package com.gethighlow.highlowandroid.model.Managers.LiveDataModels;

import android.graphics.drawable.Drawable;

import androidx.lifecycle.MutableLiveData;

import com.gethighlow.highlowandroid.model.Resources.HighLow;

import java.util.function.Consumer;

public class HighLowLiveData extends MutableLiveData<HighLow> {
    private HighLow highLow;

    public HighLowLiveData(HighLow highLow) {
        super();
        this.highLow = highLow;
    }

    public HighLow getHighLow() {
        return highLow;
    }

    public void test() {
        this.setValue(highLow);
    }

    public void setHigh(String high, String date, Boolean isPrivate, Drawable image, Consumer<HighLow> onSuccess, Consumer<String> onError) {
        highLow.setHigh(high, date, isPrivate, image, (newHighLow) -> {
            this.setValue(highLow);
            onSuccess.accept(highLow);
        }, onError);
    }

    public void setLow(String low, String date, Boolean isPrivate, Drawable image, Consumer<HighLow> onSuccess, Consumer<String> onError) {
        highLow.setLow(low, date, isPrivate, image, (newHighLow) -> {
            this.setValue(highLow);
            onSuccess.accept(highLow);
        }, onError);
    }

    public void makePrivate(Consumer<HighLow> onSuccess, Consumer<String> onError) {
        highLow.makePrivate((newHighLow) -> {
            this.setValue(highLow);
            onSuccess.accept(highLow);
        }, onError);
    }

    public void makePublic(Consumer<HighLow> onSuccess, Consumer<String> onError) {
        highLow.makePublic((newHighLow) -> {
            this.setValue(highLow);
            onSuccess.accept(highLow);
        }, onError);
    }

    public void like(Consumer<HighLow> onSuccess, Consumer<String> onError) {
        highLow.like((newHighLow) -> {
            this.setValue(highLow);
            onSuccess.accept(highLow);
        }, onError);
    }

    public void unLike(Consumer<HighLow> onSuccess, Consumer<String> onError) {
        highLow.unLike((newHighLow) -> {
            this.setValue(highLow);
            onSuccess.accept(highLow);
        }, onError);
    }

    public void flag(Consumer<HighLow> onSuccess, Consumer<String> onError) {
        highLow.flag((newHighLow) -> {
            this.setValue(highLow);
            onSuccess.accept(highLow);
        }, onError);
    }

    public void unFlag(Consumer<HighLow> onSuccess, Consumer<String> onError) {
        highLow.unFlag((newHighLow) -> {
            this.setValue(highLow);
            onSuccess.accept(highLow);
        }, onError);
    }

    public void comment(String message, Consumer<HighLow> onSuccess, Consumer<String> onError) {
        highLow.comment(message, (newHighLow) -> {
            this.setValue(highLow);
            onSuccess.accept(highLow);
        }, onError);
    }

    public void getComments(Consumer<HighLow> onSuccess, Consumer<String> onError) {
        highLow.getComments((newHighLow) -> {
            this.setValue(highLow);
            onSuccess.accept(highLow);
        }, onError);
    }

}

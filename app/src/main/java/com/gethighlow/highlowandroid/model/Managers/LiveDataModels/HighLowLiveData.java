package com.gethighlow.highlowandroid.model.Managers.LiveDataModels;

import android.graphics.drawable.Drawable;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.gethighlow.highlowandroid.model.Resources.HighLow;

import java.util.function.Consumer;

public class HighLowLiveData extends MutableLiveData<HighLow> {
    private HighLow highLow;

    public HighLowLiveData(HighLow highLow) {
        super();
        this.highLow = highLow;
        this.setValue(highLow);
    }

    public HighLow getHighLow() {
        return this.getValue();
    }

    public void setHigh(String high, String date, Boolean isPrivate, Drawable image, Consumer<HighLow> onSuccess, Consumer<String> onError) {
        HighLow highLow = getValue();
        if (highLow == null) {
            onError.accept("does-not-exist");
        }
        highLow.setHigh(high, date, isPrivate, image, (newHighLow) -> {
            this.setValue(highLow);
            onSuccess.accept(highLow);
        }, onError);
    }

    public void setLow(String low, String date, Boolean isPrivate, Drawable image, Consumer<HighLow> onSuccess, Consumer<String> onError) {
        HighLow highLow = getValue();
        if (highLow == null) {
            onError.accept("does-not-exist");
        }
        highLow.setLow(low, date, isPrivate, image, (newHighLow) -> {
            this.setValue(highLow);
            onSuccess.accept(highLow);
        }, onError);
    }

    public void makePrivate(Consumer<HighLow> onSuccess, Consumer<String> onError) {
        HighLow highLow = getValue();
        if (highLow == null) {
            onError.accept("does-not-exist");
        }
        highLow.makePrivate((newHighLow) -> {
            this.setValue(highLow);
            onSuccess.accept(highLow);
        }, onError);
    }

    public void makePublic(Consumer<HighLow> onSuccess, Consumer<String> onError) {
        HighLow highLow = getValue();
        if (highLow == null) {
            onError.accept("does-not-exist");
        }
        highLow.makePublic((newHighLow) -> {
            this.setValue(highLow);
            onSuccess.accept(highLow);
        }, onError);
    }

    public void like(Consumer<HighLow> onSuccess, Consumer<String> onError) {
        HighLow highLow = getValue();
        if (highLow == null) {
            onError.accept("does-not-exist");
        }
        highLow.like((newHighLow) -> {
            this.setValue(highLow);
            onSuccess.accept(highLow);
        }, onError);
    }

    public void unLike(Consumer<HighLow> onSuccess, Consumer<String> onError) {
        HighLow highLow = getValue();
        if (highLow == null) {
            onError.accept("does-not-exist");
        }
        highLow.unLike((newHighLow) -> {
            this.setValue(highLow);
            onSuccess.accept(highLow);
        }, onError);
    }

    public void flag(Consumer<HighLow> onSuccess, Consumer<String> onError) {
        HighLow highLow = getValue();
        if (highLow == null) {
            onError.accept("does-not-exist");
        }
        highLow.flag((newHighLow) -> {
            Log.w("Debug", "UPDATED: " + highLow.toString());
            this.setValue(highLow);
            onSuccess.accept(highLow);
        }, onError);
    }

    public void unFlag(Consumer<HighLow> onSuccess, Consumer<String> onError) {
        HighLow highLow = getValue();
        if (highLow == null) {
            onError.accept("does-not-exist");
        }
        highLow.unFlag((newHighLow) -> {
            this.setValue(highLow);
            onSuccess.accept(highLow);
        }, onError);
    }

    public void comment(String message, Consumer<HighLow> onSuccess, Consumer<String> onError) {
        HighLow highLow = getValue();
        if (highLow == null) {
            onError.accept("does-not-exist");
        }
        highLow.comment(message, (newHighLow) -> {
            this.setValue(highLow);
            onSuccess.accept(highLow);
        }, onError);
    }

    public void getComments(Consumer<HighLow> onSuccess, Consumer<String> onError) {
        HighLow highLow = getValue();
        if (highLow == null) {
            onError.accept("does-not-exist");
        }
        highLow.getComments((newHighLow) -> {
            this.setValue(highLow);
            onSuccess.accept(highLow);
        }, onError);
    }

    public String getHighlowid() {
        HighLow highLow = getValue();
        if (highLow == null) return null;
        return highLow.getHighlowid();
    }

    public String getHigh() {
        HighLow highLow = getValue();
        if (highLow == null) return null;
        return highLow.getHigh();
    }

    public String getHighImage() {
        HighLow highLow = getValue();
        if (highLow == null) return null;
        return highLow.getHighImage();
    }

    public String getLow() {
        HighLow highLow = getValue();
        if (highLow == null) return null;
        return highLow.getLow();
    }

    public String getLowImage() {
        HighLow highLow = getValue();
        if (highLow == null) return null;
        return highLow.getLowImage();
    }

    public Boolean getLiked() {
        HighLow highLow = getValue();
        if (highLow == null) return false;
        return highLow.getLiked();
    }

    public Boolean getFlagged() {
        HighLow highLow = getValue();
        if (highLow == null) return false;
        return highLow.getFlagged();
    }

    public Integer getTotalLikes() {
        HighLow highLow = getValue();
        if (highLow == null) return 0;
        return highLow.getTotal_likes();
    }

    public Boolean getPrivate() {
        HighLow highLow = getValue();
        if (highLow == null) return false;
        return highLow.getPrivate();
    }

    public String getUid() {
        HighLow highLow = getValue();
        if (highLow == null) return null;
        return highLow.getUid();
    }

    public String getDate() {
        HighLow highLow = getValue();
        if (highLow == null) return null;
        return highLow.getDate();
    }

    public String getTimestamp() {
        HighLow highLow = getValue();
        if (highLow == null) return null;
        return highLow.getTimestamp();
    }

}

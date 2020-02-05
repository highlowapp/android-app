package com.gethighlow.highlowandroid.model.Resources;

import android.graphics.drawable.Drawable;

import com.gethighlow.highlowandroid.model.Managers.HighLowManager;
import com.gethighlow.highlowandroid.model.Services.HighLowService;

import java.util.List;
import java.util.function.Consumer;

public class HighLow {
    private String highlowid;

    private String uid;

    private String high;

    private String low;

    private String highImage;

    private String lowImage;

    private Integer total_likes;

    private Boolean flagged;

    private Boolean liked;

    private List<Comment> comments;

    private String timestamp;

    private String date;

    private Boolean isPrivate;

    private String error;

    public String getError() {
        return error;
    }

    public Boolean getPrivate() {
        return isPrivate;
    }

    public Integer getTotal_likes() {
        return total_likes;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public String getDate() {
        return date;
    }

    public String getHigh() {
        return high;
    }

    public String getHighImage() {
        return highImage;
    }

    public String getHighlowid() {
        return highlowid;
    }

    public String getLow() {
        return low;
    }

    public String getLowImage() {
        return lowImage;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getUid() {
        return uid;
    }

    public Boolean getFlagged() {
        return flagged;
    }

    public Boolean getLiked() {
        return liked;
    }

    public void update(String key, Object value) {
        Object id = this.error;
        switch(key) {
            case "highlowid":
                id = this.highlowid;
                break;
            case "uid":
                id = this.uid;
                break;
            case "high":
                id = this.high;
                break;
            case "low":
                id = this.low;
                break;
            case "highImage":
                id = this.highImage;
                break;
            case "lowImage":
                id = this.lowImage;
                break;
            case "total_likes":
                id = this.total_likes;
                break;
            case "comments":
                id = this.comments;
                break;
            case "timestamp":
                id = this.timestamp;
                break;
            case "date":
                id = this.date;
            case "isPrivate":
                id = this.isPrivate;
                break;
            case "flagged":
                id = this.flagged;
                break;
            case "liked":
                id = this.liked;
                break;
            default:
                break;
        }

        if (value != null && !id.equals(value)) {
            id = value;
        }
    }

    public void updateWithHighLow(HighLow highLow) {
        update("highlowid", highLow.getHighlowid());
        update("uid", highLow.getUid());
        update("high", highLow.getHigh());
        update("low", highLow.getLow());
        update("highImage", highLow.getHighImage());
        update("lowImage", highLow.getLowImage());
        update("total_likes", highLow.getTotal_likes());
        update("comments", highLow.getComments());
        update("timestamp", highLow.getTimestamp());
        update("date", highLow.getDate());
        update("isPrivate", highLow.getPrivate());
        update("flagged", highLow.getFlagged());
        update("liked", highLow.getLiked());
    }

    public void setHigh(String high, String date, Boolean isPrivate, Drawable image, Consumer<HighLow> onSuccess, Consumer<String> onError) {
        HighLowService.shared().setHigh(this.highlowid, high, date, isPrivate, image, (highLow) -> {
            updateWithHighLow(highLow);
            HighLowManager.shared().saveHighLow(this);
            onSuccess.accept(highLow);
        }, onError);
    }

    public void setLow(String low, String date, Boolean isPrivate, Drawable image, Consumer<HighLow> onSuccess, Consumer<String> onError) {
        HighLowService.shared().setLow(this.highlowid, low, date, isPrivate, image, (highLow) -> {
            updateWithHighLow(highLow);
            HighLowManager.shared().saveHighLow(this);
            onSuccess.accept(highLow);
        }, onError);
    }

    public void makePrivate(Consumer<HighLow> onSuccess, Consumer<String> onError) {
        HighLowService.shared().makePrivate(this.highlowid, (highLow) -> {
            updateWithHighLow(highLow);
            HighLowManager.shared().saveHighLow(this);
            onSuccess.accept(highLow);
        }, onError);
    }

    public void makePublic(Consumer<HighLow> onSuccess, Consumer<String> onError) {
        HighLowService.shared().makePublic(this.highlowid, (highLow) -> {
            updateWithHighLow(highLow);
            HighLowManager.shared().saveHighLow(this);
            onSuccess.accept(highLow);
        }, onError);
    }

    public void like(Consumer<HighLow> onSuccess, Consumer<String> onError) {
        HighLowService.shared().like(this.highlowid, (highLow) -> {
            updateWithHighLow(highLow);
            HighLowManager.shared().saveHighLow(this);
            onSuccess.accept(highLow);
        }, onError);
    }

    public void unLike(Consumer<HighLow> onSuccess, Consumer<String> onError) {
        HighLowService.shared().unLike(this.highlowid, (highLow) -> {
            updateWithHighLow(highLow);
            HighLowManager.shared().saveHighLow(this);
            onSuccess.accept(highLow);
        }, onError);
    }

    public void flag(Consumer<HighLow> onSuccess, Consumer<String> onError) {
        HighLowService.shared().flag(this.highlowid, (highLow) -> {
            updateWithHighLow(highLow);
            HighLowManager.shared().saveHighLow(this);
            onSuccess.accept(highLow);
        }, onError);
    }

    public void unFlag(Consumer<HighLow> onSuccess, Consumer<String> onError) {
        HighLowService.shared().unFlag(this.highlowid, (highLow) -> {
            updateWithHighLow(highLow);
            HighLowManager.shared().saveHighLow(this);
            onSuccess.accept(highLow);
        }, onError);
    }

    public void comment(String message, Consumer<HighLow> onSuccess, Consumer<String> onError) {
        HighLowService.shared().comment(this.highlowid, message, highLow -> {
            updateWithHighLow(highLow);
            HighLowManager.shared().saveHighLow(this);
            onSuccess.accept(highLow);
        }, onError);
    }

    public void getComments(Consumer<HighLow> onSuccess, Consumer<String> onError) {
        HighLowService.shared().getComments(this.highlowid, highLow -> {
            updateWithHighLow(highLow);
            HighLowManager.shared().saveHighLow(this);
            onSuccess.accept(highLow);
        }, onError);
    }

}

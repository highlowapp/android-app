package com.gethighlow.highlowandroid.model.Resources;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.gethighlow.highlowandroid.model.Managers.HighLowManager;
import com.gethighlow.highlowandroid.model.Responses.GenericResponse;
import com.gethighlow.highlowandroid.model.Services.HighLowService;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.function.Consumer;

public class HighLow {
    private String highlowid;

    private String uid;

    private String high;

    private String low;

    @SerializedName("high_image")
    private String highImage;

    @SerializedName("low_image")
    private String lowImage;

    private Integer total_likes;

    private Boolean flagged;

    private Boolean liked;

    private List<Comment> comments;

    @SerializedName("_timestamp")
    private String timestamp;

    @SerializedName("_date")
    private String date;

    @SerializedName("private")
    private Boolean isPrivate;

    private String error;

    public void setDate(String date) {
        this.date = date;
    }

    public String toString() {
        return "{\n\thigh: " + (high == null ? "null":high) + "\n\tlow: " + (low == null ? "null":low) + "\n\thighlowid: " + (highlowid == null ? "null": highlowid) + "\n\tuid: " + (uid == null ? "null":uid) + "\n\tdate: " + (date == null ? "null": date) + "\n\tflagged: " + (flagged == null ? "null": flagged);
    }

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
        if (flagged == null) return false;
        return flagged;
    }

    public Boolean getLiked() {
        if (liked == null) return false;
        return liked;
    }

    public void update(String key, Object value) {
        Log.w("Debug", key);
        if (value == null) return;
        switch(key) {
            case "highlowid":
                this.highlowid = value.toString();
                break;
            case "uid":
                this.uid = value.toString();
                break;
            case "high":
                this.high = value.toString();
                break;
            case "low":
                this.low = value.toString();
                break;
            case "highImage":
                this.highImage = value.toString();
                break;
            case "lowImage":
                this.lowImage = value.toString();
                break;
            case "total_likes":
                this.total_likes = (Integer) value;
                break;
            case "comments":
                this.comments = (List<Comment>) value;
                break;
            case "timestamp":
                this.timestamp = value.toString();
                break;
            case "date":
                this.date = value.toString();
                break;
            case "isPrivate":
                this.isPrivate = (Boolean) value;
                break;
            case "flagged":
                this.flagged = (Boolean) value;
                break;
            case "liked":
                this.liked = (Boolean) value;
                break;
            default:
                break;
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

    public void setHigh(String high, String date, Boolean isPrivate, Bitmap image, Consumer<HighLow> onSuccess, Consumer<String> onError) {
        HighLowService.shared().setHigh(high, date, isPrivate, image, (highLow) -> {
            updateWithHighLow(highLow);
            HighLowManager.shared().saveHighLow(this);
            onSuccess.accept(highLow);
        }, onError);
    }

    public void setLow(String low, String date, Boolean isPrivate, Bitmap image, Consumer<HighLow> onSuccess, Consumer<String> onError) {
        HighLowService.shared().setLow(low, date, isPrivate, image, (highLow) -> {
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

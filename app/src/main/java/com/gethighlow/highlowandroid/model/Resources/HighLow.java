package com.gethighlow.highlowandroid.model.Resources;

import android.graphics.Bitmap;

import com.gethighlow.highlowandroid.model.util.Consumer;
import com.gethighlow.highlowandroid.model.Managers.HighLowManager;
import com.gethighlow.highlowandroid.model.Services.HighLowService;
import com.google.gson.annotations.SerializedName;

import java.util.List;

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
        if (value == null) return;
        if ("highlowid".equals(key)) {
            this.highlowid = value.toString();
        } else if ("uid".equals(key)) {
            this.uid = value.toString();
        } else if ("high".equals(key)) {
            this.high = value.toString();
        } else if ("low".equals(key)) {
            this.low = value.toString();
        } else if ("highImage".equals(key)) {
            this.highImage = value.toString();
        } else if ("lowImage".equals(key)) {
            this.lowImage = value.toString();
        } else if ("total_likes".equals(key)) {
            this.total_likes = (Integer) value;
        } else if ("comments".equals(key)) {
            this.comments = (List<Comment>) value;
        } else if ("timestamp".equals(key)) {
            this.timestamp = value.toString();
        } else if ("date".equals(key)) {
            this.date = value.toString();
        } else if ("isPrivate".equals(key)) {
            this.isPrivate = (Boolean) value;
        } else if ("flagged".equals(key)) {
            this.flagged = (Boolean) value;
        } else if ("liked".equals(key)) {
            this.liked = (Boolean) value;
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

    public void setHigh(String high, String date, Boolean isPrivate, Bitmap image, final Consumer<HighLow> onSuccess, Consumer<String> onError) {
        HighLowService.shared().setHigh(high, date, highlowid, isPrivate, image, new Consumer<HighLow>() {
            @Override
            public void accept(HighLow highLow) {
                HighLow.this.updateWithHighLow(highLow);
                HighLowManager.shared().saveHighLow(HighLow.this);
                onSuccess.accept(highLow);
            }
        }, onError);
    }

    public void setLow(String low, String date, Boolean isPrivate, Bitmap image, final Consumer<HighLow> onSuccess, Consumer<String> onError) {
        HighLowService.shared().setLow(low, date, highlowid, isPrivate, image, new Consumer<HighLow>() {
            @Override
            public void accept(HighLow highLow) {
                HighLow.this.updateWithHighLow(highLow);
                HighLowManager.shared().saveHighLow(HighLow.this);
                onSuccess.accept(highLow);
            }
        }, onError);
    }

    public void makePrivate(final Consumer<HighLow> onSuccess, Consumer<String> onError) {
        HighLowService.shared().makePrivate(this.highlowid, new Consumer<HighLow>() {
            @Override
            public void accept(HighLow highLow) {
                HighLow.this.updateWithHighLow(highLow);
                HighLowManager.shared().saveHighLow(HighLow.this);
                onSuccess.accept(highLow);
            }
        }, onError);
    }

    public void makePublic(final Consumer<HighLow> onSuccess, Consumer<String> onError) {
        HighLowService.shared().makePublic(this.highlowid, new Consumer<HighLow>() {
            @Override
            public void accept(HighLow highLow) {
                HighLow.this.updateWithHighLow(highLow);
                HighLowManager.shared().saveHighLow(HighLow.this);
                onSuccess.accept(highLow);
            }
        }, onError);
    }

    public void like(final Consumer<HighLow> onSuccess, Consumer<String> onError) {
        HighLowService.shared().like(this.highlowid, new Consumer<HighLow>() {
            @Override
            public void accept(HighLow highLow) {
                HighLow.this.updateWithHighLow(highLow);
                HighLowManager.shared().saveHighLow(HighLow.this);
                onSuccess.accept(highLow);
            }
        }, onError);
    }

    public void unLike(final Consumer<HighLow> onSuccess, Consumer<String> onError) {
        HighLowService.shared().unLike(this.highlowid, new Consumer<HighLow>() {
            @Override
            public void accept(HighLow highLow) {
                HighLow.this.updateWithHighLow(highLow);
                HighLowManager.shared().saveHighLow(HighLow.this);
                onSuccess.accept(highLow);
            }
        }, onError);
    }

    public void flag(final Consumer<HighLow> onSuccess, Consumer<String> onError) {
        HighLowService.shared().flag(this.highlowid, new Consumer<HighLow>() {
            @Override
            public void accept(HighLow highLow) {
                HighLow.this.updateWithHighLow(highLow);
                HighLowManager.shared().saveHighLow(HighLow.this);
                onSuccess.accept(highLow);
            }
        }, onError);
    }

    public void unFlag(final Consumer<HighLow> onSuccess, Consumer<String> onError) {
        HighLowService.shared().unFlag(this.highlowid, new Consumer<HighLow>() {
            @Override
            public void accept(HighLow highLow) {
                HighLow.this.updateWithHighLow(highLow);
                HighLowManager.shared().saveHighLow(HighLow.this);
                onSuccess.accept(highLow);
            }
        }, onError);
    }

    public void comment(String message, final Consumer<HighLow> onSuccess, Consumer<String> onError) {
        HighLowService.shared().comment(this.highlowid, message, new Consumer<HighLow>() {
            @Override
            public void accept(HighLow highLow) {
                HighLow.this.updateWithHighLow(highLow);
                HighLowManager.shared().saveHighLow(HighLow.this);
                onSuccess.accept(highLow);
            }
        }, onError);
    }

    public void getComments(final Consumer<HighLow> onSuccess, Consumer<String> onError) {
        HighLowService.shared().getComments(this.highlowid, new Consumer<HighLow>() {
            @Override
            public void accept(HighLow highLow) {
                HighLow.this.updateWithHighLow(highLow);
                HighLowManager.shared().saveHighLow(HighLow.this);
                onSuccess.accept(highLow);
            }
        }, onError);
    }

}

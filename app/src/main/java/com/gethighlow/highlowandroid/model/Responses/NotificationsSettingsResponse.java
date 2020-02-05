package com.gethighlow.highlowandroid.model.Responses;

public class NotificationsSettingsResponse {
    private Boolean notify_new_friend_req;
    private Boolean notify_new_friend_acc;
    private Boolean notify_new_feed_item;
    private Boolean notify_new_like;
    private Boolean notify_new_comment;
    private String error;

    public Boolean getNotify_new_comment() {
        return notify_new_comment;
    }

    public Boolean getNotify_new_feed_item() {
        return notify_new_feed_item;
    }

    public Boolean getNotify_new_friend_acc() {
        return notify_new_friend_acc;
    }

    public Boolean getNotify_new_friend_req() {
        return notify_new_friend_req;
    }

    public Boolean getNotify_new_like() {
        return notify_new_like;
    }

    public String getError() {
        return error;
    }
}

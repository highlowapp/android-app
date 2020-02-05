package com.gethighlow.highlowandroid.model.Responses;

public class NotificationsRegisterResponse {
    private String error;
    private String platform;
    private String device_id;
    private String uid;

    public String getError() {
        return error;
    }

    public String getUid() {
        return uid;
    }

    public String getDevice_id() {
        return device_id;
    }

    public String getPlatform() {
        return platform;
    }
}

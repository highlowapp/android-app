package com.gethighlow.highlowandroid.model.Responses;

public class AuthResponse {
    private String access;
    private String refresh;
    private String uid;
    private String error;

    public String getError() {
        return error;
    }

    public String access() {
        return access;
    }

    public String refresh() {
        return refresh;
    }

    public String uid() {
        return uid;
    }
}

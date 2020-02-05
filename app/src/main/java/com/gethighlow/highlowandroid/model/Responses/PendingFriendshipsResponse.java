package com.gethighlow.highlowandroid.model.Responses;

import com.gethighlow.highlowandroid.model.Resources.User;

import java.util.List;

public class PendingFriendshipsResponse {
    private List<User> requests;
    private String error;

    public String getError() {
        return error;
    }

    public List<User> getRequests() {
        return requests;
    }
}

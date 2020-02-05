package com.gethighlow.highlowandroid.model.Responses;

import com.gethighlow.highlowandroid.model.Resources.User;

import java.util.List;

public class FriendsResponse {
    private List<User> friends;
    private String error;

    public List<User> getFriends() {
        return friends;
    }

    public String getError() {
        return error;
    }
}

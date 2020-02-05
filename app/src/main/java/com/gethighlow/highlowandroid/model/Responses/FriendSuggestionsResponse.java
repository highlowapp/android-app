package com.gethighlow.highlowandroid.model.Responses;

import com.gethighlow.highlowandroid.model.Resources.User;

import java.util.List;

public class FriendSuggestionsResponse {
    private List<User> users;
    private String error;

    public List<User> getUsers() {
        return users;
    }

    public String getError() {
        return error;
    }
}

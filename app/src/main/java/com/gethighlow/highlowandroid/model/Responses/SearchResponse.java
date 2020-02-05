package com.gethighlow.highlowandroid.model.Responses;

import com.gethighlow.highlowandroid.model.Resources.SearchItem;

import java.util.List;

public class SearchResponse {
    private String error;
    private List<SearchItem> users;

    public String getError() {
        return error;
    }

    public List<SearchItem> getUsers() {
        return users;
    }
}

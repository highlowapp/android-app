package com.gethighlow.highlowandroid.model.Responses;

import com.gethighlow.highlowandroid.model.Resources.FeedItem;

import java.util.List;

public class FeedResponse {
    private List<FeedItem> feed;
    private String error;

    public List<FeedItem> getFeed() {
        return feed;
    }

    public String getError() {
        return error;
    }
}

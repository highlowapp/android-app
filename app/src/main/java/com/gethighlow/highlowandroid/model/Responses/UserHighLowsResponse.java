package com.gethighlow.highlowandroid.model.Responses;

import com.gethighlow.highlowandroid.model.Resources.HighLow;

import java.util.List;

public class UserHighLowsResponse {
    private String error;
    private List<HighLow> highlows;

    public String getError() {
        return error;
    }

    public List<HighLow> getHighlows() {
        return highlows;
    }
}

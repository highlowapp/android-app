package com.gethighlow.highlowandroid.model.Responses;

import com.gethighlow.highlowandroid.model.Resources.Interest;

import java.util.List;

public class InterestResponse {
    private List<Interest> interests;
    private String error;

    public String getError() {
        return error;
    }

    public List<Interest> getInterests() {
        return interests;
    }
}

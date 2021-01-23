package com.gethighlow.highlowandroid.model.Responses;

import com.gethighlow.highlowandroid.model.Resources.Activity;

import java.util.List;

public class ActivitiesResponse {
    private String error;
    private List<Activity> activities;

    public String getError() {
        return error;
    }

    public List<Activity> getActivities() {
        return activities;
    }
}

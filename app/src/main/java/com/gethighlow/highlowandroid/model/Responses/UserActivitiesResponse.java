package com.gethighlow.highlowandroid.model.Responses;


import com.gethighlow.highlowandroid.model.Resources.Activity;
import com.gethighlow.highlowandroid.model.Resources.ActivityComment;

import org.json.JSONObject;

import java.util.List;

public class UserActivitiesResponse {
    private List<Activity> activities;
    private String error;
    private String activityId;
    private String uid;
    private String type;
    private String title;
    private String timestamp;
    JSONObject data = new JSONObject();
    private String date;
    private Boolean flagged = false;
    private List<ActivityComment> comments;


    public List<Activity> getActivities(){
        return activities;
    }

    public String getError(){
        return error;
    }

    public String getActivityId(){
        return activityId;
    }

    public String getUid(){
        return uid;
    }

    public String getType(){
        return type;
    }

    public String getTitle(){
        return title;
    }

    public String getTimestamp(){
        return timestamp;
    }

    public String getDate(){
        return date;
    }

    public Boolean getFlagged(){
        return flagged;
    }

    public List<ActivityComment> getComments(){
        return comments;
    }


}

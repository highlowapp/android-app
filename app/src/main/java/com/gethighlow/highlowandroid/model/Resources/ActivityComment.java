package com.gethighlow.highlowandroid.model.Resources;

import com.gethighlow.highlowandroid.model.Responses.GenericResponse;
import com.gethighlow.highlowandroid.model.Services.ActivityService;
import com.gethighlow.highlowandroid.model.util.Consumer;
import com.google.gson.annotations.SerializedName;

public class ActivityComment {

    private String commentid;

    private String activityId;

    private String uid;

    private String message;

    @SerializedName("_timestamp")
    private String timestamp;

    private String firstname;

    private String lastname;

    private String profileimage;

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getName() {
        return firstname + " " + lastname;
    }

    public String getProfileimage() {
        return profileimage;
    }

    public String getUid() {
        return uid;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getActivityId() {
        return activityId;
    }

    public String getCommentid() {
        return commentid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void update(final String message, final Consumer<GenericResponse> onSuccess, Consumer<String> onError) {
        ActivityService.shared().updateComment(this.commentid, message, new Consumer<GenericResponse>() {
            @Override
            public void accept(GenericResponse genericResponse) {
                ActivityComment.this.message = message;
                onSuccess.accept(genericResponse);
            }
        }, onError);
    }

    public void delete(Consumer<GenericResponse> onSuccess, Consumer<String> onError) {
        ActivityService.shared().deleteComment(this.commentid, onSuccess, onError);
    }
}

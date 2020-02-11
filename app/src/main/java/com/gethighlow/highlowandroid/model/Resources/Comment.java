package com.gethighlow.highlowandroid.model.Resources;

import com.gethighlow.highlowandroid.model.Responses.GenericResponse;
import com.gethighlow.highlowandroid.model.Services.HighLowService;
import com.google.gson.annotations.SerializedName;

import java.util.function.Consumer;

public class Comment {
    private String commentid;

    private String highlowid;

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

    public String getHighlowid() {
        return highlowid;
    }

    public String getCommentid() {
        return commentid;
    }

    public String getMessage() {
        return message;
    }

    public void update(String message, Consumer<GenericResponse> onSuccess, Consumer<String> onError) {
        HighLowService.shared().updateComment(this.commentid, message, genericResponse -> {
            this.message = message;
            onSuccess.accept(genericResponse);
        }, onError);
    }

    public void delete(Consumer<GenericResponse> onSuccess, Consumer<String> onError) {
        HighLowService.shared().deleteComment(this.commentid, onSuccess, onError);
    }
}

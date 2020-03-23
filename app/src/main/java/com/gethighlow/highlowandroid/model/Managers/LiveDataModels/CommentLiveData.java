package com.gethighlow.highlowandroid.model.Managers.LiveDataModels;

import androidx.lifecycle.MutableLiveData;

import com.gethighlow.highlowandroid.model.util.Consumer;
import com.gethighlow.highlowandroid.model.Resources.Comment;
import com.gethighlow.highlowandroid.model.Responses.GenericResponse;
import com.gethighlow.highlowandroid.model.Services.HighLowService;

public class CommentLiveData extends MutableLiveData<Comment> {
    public CommentLiveData(Comment comment) {
        this.setValue(comment);
    }

    public String getName() {
        Comment comment = getValue();
        if (comment != null) {
            return comment.getName();
        }
        return null;
    }

    public String getProfileimage() {
        Comment comment = getValue();
        if (comment != null) {
            return comment.getProfileimage();
        }
        return null;
    }

    public String getUid() {
        Comment comment = getValue();
        if (comment != null) {
            return comment.getUid();
        }
        return null;
    }

    public String getTimestamp() {
        Comment comment = getValue();
        if (comment != null) {
            return comment.getTimestamp();
        }
        return null;
    }

    public String getHighlowid() {
        Comment comment = getValue();
        if (comment != null) {
            return comment.getHighlowid();
        }
        return null;
    }

    public String getCommentid() {
        Comment comment = getValue();
        if (comment != null) {
            return comment.getCommentid();
        }
        return null;
    }

    public String getMessage() {
        Comment comment = getValue();
        if (comment != null) {
            return comment.getMessage();
        }
        return null;
    }

    public void update(final String message, final Consumer<GenericResponse> onSuccess, Consumer<String> onError) {
        final Comment comment = getValue();
        if (comment == null) {
            onError.accept("does-not-exist");
        } else {

            HighLowService.shared().updateComment(comment.getCommentid(), message, new Consumer<GenericResponse>() {
                @Override
                public void accept(GenericResponse genericResponse) {
                    comment.setMessage(message);
                    CommentLiveData.this.setValue(comment);
                    onSuccess.accept(genericResponse);
                }
            }, onError);

        }
    }

    public void delete(Consumer<GenericResponse> onSuccess, Consumer<String> onError) {
        Comment comment = getValue();
        if (comment == null) {
            onError.accept("does-not-exist");
        } else {
            HighLowService.shared().deleteComment(comment.getCommentid(), onSuccess, onError);
        }
    }
}

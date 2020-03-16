package com.gethighlow.highlowandroid.CustomViews;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.gethighlow.highlowandroid.R;
import com.gethighlow.highlowandroid.model.Managers.ImageManager;
import com.gethighlow.highlowandroid.model.Managers.LiveDataModels.CommentLiveData;
import com.gethighlow.highlowandroid.model.Resources.Comment;
import com.gethighlow.highlowandroid.model.Services.HighLowService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CommentView extends RelativeLayout {
    private TextView name;
    private ImageView profileImage;
    private TextView timestamp;
    private TextView message;
    private ImageView moreButton;
    private CommentLiveData comment;
    private String commentId;
    private CommentViewDelegate delegate;

    public CommentView(Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
        setup(context, attributeSet);
    }

    public void setup(Context context, @Nullable AttributeSet attributeSet) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.comment_view, this, true);
        name = findViewById(R.id.name);
        profileImage = findViewById(R.id.profile_image);
        timestamp = findViewById(R.id.timestamp);
        message = findViewById(R.id.message);
        moreButton = findViewById(R.id.moreButton);

        moreButton.setOnClickListener(moreOptions);
    }

    public void attachToLiveData(CommentLiveData commentLiveData) {
        this.comment = commentLiveData;
        loadComment(this.comment.getValue());
    }


    private void alert(String title, String message) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setCancelable(true);
        alertDialog.setPositiveButton("OK", null);
        alertDialog.show();
    }

    public View.OnClickListener moreOptions = view -> {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Choose an action to perform on this comment");

        String[] options = {"Edit", "Delete", "Cancel"};

        builder.setItems(options, (dialogInterface, i) -> {
            if (i == 0) {
                edit();
            } else if (i == 1) {
                delete();
            }

        });
        builder.setCancelable(false);
        builder.create().show();
    };

    private void edit() {
        if (comment != null) {
            Intent editComment = new Intent(getContext(), EditCommentActivity.class);
            editComment.putExtra("commentId", comment.getCommentid());
            editComment.putExtra("highlowid", comment.getHighlowid());
            editComment.putExtra("prevMessage", comment.getMessage());
            getContext().startActivity(editComment);
        } else {
            alert("An error occurred", "Please try again");
        }
    }

    private void delete() {
        if (comment == null) {
            alert("An error occurred", "Please try again");
            return;
        }

        String commentId = comment.getCommentid();

        if (commentId == null) {
            alert("An error occurred", "Please try again");
            return;
        }

        HighLowService.shared().deleteComment(commentId, genericResponse -> {
            Intent newIntent = new Intent("highlow-updated");
            newIntent.putExtra("highlowid", comment.getHighlowid());
            LocalBroadcastManager.getInstance(getContext()).sendBroadcast(newIntent);
        }, error -> {});
    }

    public void loadComment(Comment comment) {
        commentId = comment.getCommentid();
        name.setText(comment.getName());
        String url = comment.getProfileimage();
        if (!url.startsWith("http")) {
            url = "https://storage.googleapis.com/highlowfiles/" + url;
        }


        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        LocalDateTime date = LocalDateTime.parse(comment.getTimestamp(), formatter);

        DateTimeFormatter newFormatter = DateTimeFormatter.ofPattern("MMM d, uuuu 'at' h:m a");
        timestamp.setText( date.format(newFormatter) );

        message.setText(comment.getMessage());

        ImageManager.shared().getImage(url, (img) -> {
            profileImage.setImageBitmap(img);
        }, error -> {

        });
    }
}

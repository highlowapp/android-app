package com.gethighlow.highlowandroid.CustomViews.Other;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.gethighlow.highlowandroid.Activities.Tabs.Home.EditCommentActivity;
import com.gethighlow.highlowandroid.CustomViews.Other.Delegates.CommentViewDelegate;
import com.gethighlow.highlowandroid.R;
import com.gethighlow.highlowandroid.model.Services.SetActivityTheme;
import com.gethighlow.highlowandroid.model.util.Consumer;
import com.gethighlow.highlowandroid.model.Managers.ImageManager;
import com.gethighlow.highlowandroid.model.Managers.LiveDataModels.CommentLiveData;
import com.gethighlow.highlowandroid.model.Resources.Comment;
import com.gethighlow.highlowandroid.model.Responses.GenericResponse;
import com.gethighlow.highlowandroid.model.Services.HighLowService;

import org.threeten.bp.LocalDateTime;
import org.threeten.bp.format.DateTimeFormatter;

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

        String currentTheme = SetActivityTheme.getTheme(context);
        if(currentTheme.equals("light")) {
            final ContextThemeWrapper theme = new ContextThemeWrapper(context, R.style.LightTheme);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            LayoutInflater localInflater = inflater.cloneInContext(theme);
            localInflater.inflate(R.layout.comment_view, this, true);
        } else if(currentTheme.equals("dark")){
            final ContextThemeWrapper theme = new ContextThemeWrapper(context, R.style.DarkTheme);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            LayoutInflater localInflater = inflater.cloneInContext(theme);
            localInflater.inflate(R.layout.comment_view, this, true);
        }


//        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        inflater.inflate(R.layout.comment_view, this, true);
        name = findViewById(R.id.name);
        profileImage = findViewById(R.id.profile_image);
        timestamp = findViewById(R.id.timestamp);
        message = findViewById(R.id.message);
        moreButton = findViewById(R.id.moreButton);

        moreButton.setOnClickListener(moreOptions);

        LocalBroadcastManager.getInstance(getContext()).registerReceiver(themeReceiver, new IntentFilter("theme-updated"));

    }

    public void attachToLiveData(CommentLiveData commentLiveData) {
        this.comment = commentLiveData;
        loadComment(this.comment.getValue());
    }


    private void alert(String title, String message) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext(), R.style.AlertDialogCustom);
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setCancelable(true);
        alertDialog.setPositiveButton("OK", null);
        alertDialog.show();
    }

    public View.OnClickListener moreOptions = new OnClickListener() {
        @Override
        public void onClick(View view) {
            AlertDialog.Builder builder = new AlertDialog.Builder(CommentView.this.getContext(), R.style.AlertDialogCustom);
            builder.setTitle("Choose an action to perform on this comment");

            String[] options = {"Edit", "Delete", "Cancel"};

            builder.setItems(options, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (i == 0) {
                        CommentView.this.edit();
                    } else if (i == 1) {
                        CommentView.this.delete();
                    }

                }
            });
            builder.setCancelable(false);
            builder.create().show();
        }
    };

    private void edit() {
        if (comment != null) {
            Intent editComment = new Intent(getContext(), EditCommentActivity.class);
            editComment.putExtra("commentId", comment.getCommentid());
            editComment.putExtra("highlowid", comment.getHighlowid());
            editComment.putExtra("prevMessage", comment.getMessage());
            getContext().startActivity(editComment);
        } else {
            alert(getResources().getString(R.string.an_error_occurred), getResources().getString(R.string.please_try_again));
        }
    }

    private void delete() {
        if (comment == null) {
            alert(getResources().getString(R.string.an_error_occurred), getResources().getString(R.string.please_try_again));
            return;
        }

        String commentId = comment.getCommentid();

        if (commentId == null) {
            alert(getResources().getString(R.string.an_error_occurred), getResources().getString(R.string.please_try_again));
            return;
        }

        HighLowService.shared().deleteComment(commentId, new Consumer<GenericResponse>() {
            @Override
            public void accept(GenericResponse genericResponse) {
                Intent newIntent = new Intent("highlow-updated");
                newIntent.putExtra("highlowid", comment.getHighlowid());
                LocalBroadcastManager.getInstance(CommentView.this.getContext()).sendBroadcast(newIntent);
            }
        }, new Consumer<String>() {
            @Override
            public void accept(String error) {
            }
        });
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

        ImageManager.shared().getImage(url, new Consumer<Bitmap>() {
            @Override
            public void accept(Bitmap img) {
                profileImage.setImageBitmap(img);
            }
        }, new Consumer<String>() {
            @Override
            public void accept(String error) {

            }
        });
    }


    private BroadcastReceiver themeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String currentTheme = SetActivityTheme.getTheme(context);

            if(currentTheme.equals("light")) {
                String layoutTheme = "light";
                setLayout(context, layoutTheme);
            } else if(currentTheme.equals("dark")){
                String layoutTheme = "dark";
                setLayout(context, layoutTheme);
            }

        }
    };

    private void setLayout(Context context, String layoutTheme){
        if(layoutTheme.equals("light")) {
            final ContextThemeWrapper theme = new ContextThemeWrapper(context, R.style.LightTheme);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            LayoutInflater localInflater = inflater.cloneInContext(theme);
            localInflater.inflate(R.layout.comment_view, this, true);
        } else if(layoutTheme.equals("dark")){
            final ContextThemeWrapper theme = new ContextThemeWrapper(context, R.style.DarkTheme);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            LayoutInflater localInflater = inflater.cloneInContext(theme);
            localInflater.inflate(R.layout.comment_view, this, true);
        }
    }

}

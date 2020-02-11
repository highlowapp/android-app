package com.gethighlow.highlowandroid.CustomViews;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.gethighlow.highlowandroid.R;
import com.gethighlow.highlowandroid.model.Managers.ImageManager;
import com.gethighlow.highlowandroid.model.Resources.Comment;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CommentView extends RelativeLayout {
    private TextView name;
    private ImageView profileImage;
    private TextView timestamp;
    private TextView message;

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
    }

    public void loadComment(Comment comment) {
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

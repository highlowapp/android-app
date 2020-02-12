package com.gethighlow.highlowandroid.CustomViews;

import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;

import com.gethighlow.highlowandroid.R;
import com.gethighlow.highlowandroid.model.Managers.ImageManager;
import com.gethighlow.highlowandroid.model.Managers.LiveDataModels.HighLowLiveData;
import com.gethighlow.highlowandroid.model.Resources.Comment;
import com.gethighlow.highlowandroid.model.Resources.HighLow;
import com.gethighlow.highlowandroid.model.Services.AuthService;

public class HighLowView extends LinearLayout implements HLButtonDelegate {
    private Fragment fragment;
    private ImageView highImage;
    private TextView high;
    private ImageView lowImage;
    private TextView low;
    private HLButton addHigh;
    private HLButton addLow;
    private TextView highNotGiven;
    private TextView lowNotGiven;
    private boolean editable = false;
    private LinearLayout privateSwitch;
    private HighLowLiveData highLow;
    private ImageView flagButton;
    private ImageView likeButton;
    private TextView likeCount;
    private boolean showAllComments = false;
    private LifecycleOwner lifecycleOwner;
    private LinearLayout comments;

    public HighLowViewDelegate delegate;

    public HighLowView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);

        setup(context, attributeSet);
    }

    private void setup(Context context, @Nullable AttributeSet attributeSet) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.highlowview, this, true);
        this.setOrientation(LinearLayout.VERTICAL);
        this.setGravity(Gravity.CENTER);
        this.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));

        high = findViewById(R.id.high);
        low = findViewById(R.id.low);
        highImage = findViewById(R.id.highImage);
        lowImage = findViewById(R.id.lowImage);
        addHigh = findViewById(R.id.addHigh);
        addLow = findViewById(R.id.addLow);
        highNotGiven = findViewById(R.id.highNotGiven);
        lowNotGiven = findViewById(R.id.lowNotGiven);
        privateSwitch = findViewById(R.id.privateSwitch);
        flagButton = findViewById(R.id.flagButton);
        likeButton = findViewById(R.id.likeButton);
        likeCount = findViewById(R.id.likeCount);
        comments = findViewById(R.id.comments);

        addHigh.delegate = this;

        high.setVisibility(View.GONE);
        low.setVisibility(View.GONE);
        highImage.setVisibility(View.GONE);
        lowImage.setVisibility(View.GONE);
        addHigh.setVisibility(View.GONE);
        addLow.setVisibility(View.GONE);
    }

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }

    public void addHigh(View view) {
        if (delegate != null) {
            delegate.willAddHigh(highLow.getHighlowid());
        }
    }
    public void addLow(View view) {
        if (delegate != null) {
            delegate.willAddLow(highLow.getHighlowid());
        }
    }

    public View.OnClickListener like = new  View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (delegate != null) {
                delegate.willLike(highLow.getHighlowid());
            }
        }
    };

    public View.OnClickListener unLike = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (delegate != null) {
                delegate.willUnLike(highLow.getHighlowid());
            }
        }
    };

    public View.OnClickListener flag = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle(R.string.flag_title)
                    .setMessage(R.string.flag_message)
            .setPositiveButton(R.string.flag_confirmation, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    flagButton.setImageResource(R.drawable.ic_flag);
                    highLow.flag(newHighLow -> {
                        Log.w("Debug", "Success");
                    }, error -> {
                        Log.w("Debug", "Failure");
                    });
                }
            }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            }).setCancelable(true);

            AlertDialog dialog = builder.create();
            dialog.show();
        }
    };

    public View.OnClickListener unFlag = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle(R.string.unflag_title)
                    .setMessage(R.string.unflag_message)
                    .setPositiveButton(R.string.flag_confirmation, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            flagButton.setImageResource(R.drawable.ic_flag_grayed);
                            highLow.unFlag(newHighLow -> {
                            }, error -> {
                                Log.w("Debug", "Failure");
                            });
                        }
                    }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            }).setCancelable(true);

            AlertDialog dialog = builder.create();
            dialog.show();
        }
    };

    public void attachToLiveData(LifecycleOwner lifecycleOwner, HighLowLiveData liveData) {
        this.lifecycleOwner = lifecycleOwner;
        highLow = liveData;
        highLow.observe(this.lifecycleOwner, onHighLowUpdate);
        if (highLow.getValue() != null) {
            loadHighLow(highLow.getValue());
        }
    }

    public void loadHighLow(HighLow highLow) {
        Log.w("Debug", highLow.toString());
        if (highLow.getUid().equals(AuthService.shared().getUid())) {
            editable = true;
        } else {
            editable = false;
        }

        String highText = highLow.getHigh();
        String lowText = highLow.getLow();

        if (highLow.getFlagged()) {
            flagButton.setOnClickListener(unFlag);
        } else {
            flagButton.setOnClickListener(flag);
        }

        if (highLow.getLiked()) {
            likeButton.setOnClickListener(unLike);
        } else {
            likeButton.setOnClickListener(like);
        }

        likeCount.setText( highLow.getTotal_likes().toString() );

        if (highLow.getLiked()) {
            likeButton.setImageResource(R.drawable.ic_like);
        } else {
            likeButton.setImageResource(R.drawable.ic_like_outline);
        }

        if (highLow.getFlagged()) {
            flagButton.setImageResource(R.drawable.ic_flag);
        } else {
            flagButton.setImageResource(R.drawable.ic_flag_grayed);
        }

        if (highText != null && !highText.equals("")) {
            high.setText(highLow.getHigh());
            high.setVisibility(View.VISIBLE);
        }
        if (lowText != null && !lowText.equals("")) {
            low.setText(highLow.getLow());
            low.setVisibility(View.VISIBLE);
        }

        String highImageUrl = highLow.getHighImage();
        String lowImageUrl = highLow.getLowImage();

        if (highImageUrl != null && !highImageUrl.equals("")) {
            ImageManager.shared().getImage("https://storage.googleapis.com/highlowfiles/highs/" + highImageUrl, (img) -> {
                highImage.setImageBitmap(img);
                highImage.setVisibility(View.VISIBLE);
            }, (error) -> {
                Log.w("Debug", error);
            });
        }

        if (lowImageUrl != null && !lowImageUrl.equals("")) {
            ImageManager.shared().getImage("https://storage.googleapis.com/highlowfiles/lows/" + highImageUrl, (img) -> {
                lowImage.setImageBitmap(img);
                lowImage.setVisibility(View.VISIBLE);
            }, (error) -> {
                Log.w("Debug", error);
            });
        }

        addHigh.setVisibility(View.GONE);
        highNotGiven.setVisibility(View.GONE);
        addLow.setVisibility(View.GONE);
        lowNotGiven.setVisibility(View.GONE);

        //If the high doesn't exist
        if ((highText == null || highText.length() == 0) && (highImageUrl == null || highImageUrl.length() == 0)) {
            high.setVisibility(View.GONE);
            highImage.setVisibility(View.GONE);
            if (editable) {
                addHigh.setVisibility(View.VISIBLE);
                highNotGiven.setVisibility(View.GONE);
            } else {
                highNotGiven.setVisibility(View.VISIBLE);
                addHigh.setVisibility(View.GONE);
                privateSwitch.setVisibility(View.GONE);
            }
        } else {
            high.setVisibility(View.VISIBLE);
            highImage.setVisibility(View.VISIBLE);
            privateSwitch.setVisibility(View.VISIBLE);
        }


        //If the low doesn't exist
        if ((lowText == null || lowText.length() == 0) && (lowImageUrl == null || lowImageUrl.length() == 0)) {
            low.setVisibility(View.GONE);
            lowImage.setVisibility(View.GONE);
            if (editable) {
                addLow.setVisibility(View.VISIBLE);
                lowNotGiven.setVisibility(View.GONE);
            } else {
                lowNotGiven.setVisibility(View.VISIBLE);
                addLow.setVisibility(View.GONE);
                privateSwitch.setVisibility(View.GONE);
            }
        } else {
            low.setVisibility(View.VISIBLE);
            lowImage.setVisibility(View.VISIBLE);
            privateSwitch.setVisibility(View.VISIBLE);
        }

        //Now for the comments
        comments.removeAllViews();

        for (Comment comment: highLow.getComments()) {
            CommentView commentView = new CommentView(getContext(), null);
            LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0, 30, 0, 0);
            commentView.setLayoutParams(layoutParams);
            commentView.loadComment(comment);
            comments.addView(commentView);
        }
    }


    @Override
    public void onButtonClick(View view) {
        if (view == addHigh) {
            if (delegate != null) {
                delegate.willAddHigh(highLow.getHighlowid());
            }
        } else if (view == addLow) {
            if (delegate != null) {
                delegate.willAddLow(highLow.getHighlowid());
            }
        }
    }

    private final Observer<HighLow> onHighLowUpdate = new Observer<HighLow>() {
        @Override
        public void onChanged(HighLow highLow) {
            loadHighLow(highLow);
        }
    };
}

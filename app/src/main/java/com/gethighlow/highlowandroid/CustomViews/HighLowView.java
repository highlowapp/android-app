package com.gethighlow.highlowandroid.CustomViews;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.gethighlow.highlowandroid.Activities.Tabs.EditHLActivity;
import com.gethighlow.highlowandroid.CustomViews.BaseComponents.HLButton;
import com.gethighlow.highlowandroid.CustomViews.BaseComponents.HLButtonDelegate;
import com.gethighlow.highlowandroid.R;
import com.gethighlow.highlowandroid.model.Managers.ImageManager;
import com.gethighlow.highlowandroid.model.Managers.LiveDataModels.CommentLiveData;
import com.gethighlow.highlowandroid.model.Managers.LiveDataModels.HighLowLiveData;
import com.gethighlow.highlowandroid.model.Managers.UserManager;
import com.gethighlow.highlowandroid.model.Resources.HighLow;
import com.gethighlow.highlowandroid.model.Resources.User;
import com.gethighlow.highlowandroid.model.Services.AuthService;
import com.gethighlow.highlowandroid.model.Services.HighLowService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class HighLowView extends RelativeLayout implements HLButtonDelegate, CommentViewDelegate {
    private Fragment fragment;
    private ImageView highImage;
    private TextView high;
    private ImageView lowImage;
    private TextView low;
    private HLButton addHigh;
    private HLButton addLow;
    private TextView highNotGiven;
    private TextView lowNotGiven;
    private RelativeLayout editHigh;
    private RelativeLayout editLow;
    private boolean editable = false;
    private HighLowLiveData highLow;
    private ImageView flagButton;
    private ImageView likeButton;
    private TextView likeCount;
    private boolean showAllComments = false;
    private LifecycleOwner lifecycleOwner;
    private LinearLayout comments;
    private String date;
    private SwitchCompat privateSwitch;
    private TextView dateLabel;
    private ImageView profileImage;
    private EditText leaveComment;
    private TextView showAllCommentsView;
    private ImageView sendImg;
    private ProgressBar sendProgress;
    private RelativeLayout sendButton;
    private LinearLayout privateView;

    public HighLowViewDelegate delegate;

    public HighLowView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);

        setup(context, attributeSet);
    }

    private void alert(String title, String message) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setCancelable(true);
        alertDialog.setPositiveButton("OK", null);
        alertDialog.show();
    }

    private void setup(Context context, @Nullable AttributeSet attributeSet) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.highlowview, this, true);
        this.setGravity(Gravity.CENTER);
        this.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
        this.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

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
        editHigh = findViewById(R.id.editHigh);
        editLow = findViewById(R.id.editLow);
        editLow = findViewById(R.id.editLow);
        privateView = findViewById(R.id.privateView);
        privateSwitch = findViewById(R.id.privateSwitch);
        dateLabel = findViewById(R.id.dateLabel);
        profileImage = findViewById(R.id.commentProfileImage);
        leaveComment = findViewById(R.id.leaveComment);
        showAllCommentsView = findViewById(R.id.show_all_comments);
        sendImg = findViewById(R.id.sendImg);
        sendProgress = findViewById(R.id.sendProgressBar);
        sendButton = findViewById(R.id.sendButton);

        sendProgress.setVisibility(View.GONE);

        addHigh.delegate = this;
        addLow.delegate = this;

        high.setVisibility(View.GONE);
        low.setVisibility(View.GONE);
        highImage.setVisibility(View.GONE);
        lowImage.setVisibility(View.GONE);
        addHigh.setVisibility(View.GONE);
        addLow.setVisibility(View.GONE);

        editHigh.setOnClickListener(editHighListener);
        editLow.setOnClickListener(editLowListener);
        showAllCommentsView.setOnClickListener(showAllCommentsListener);
        sendButton.setOnClickListener(sendCommentListener);


        privateSwitch.setOnCheckedChangeListener(onCheckedChangeListener);

        LocalBroadcastManager.getInstance(getContext()).registerReceiver(receiver, new IntentFilter("highlow-updated"));
    }

    public View.OnClickListener sendCommentListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String message = leaveComment.getText().toString();
            startCommentLoading();
            highLow.comment(message, newHighLow -> {
                stopCommentLoading();
                leaveComment.setText("");
                loadComments(highLow.getComments());
            }, error -> {
                stopCommentLoading();
                leaveComment.setText("");
            });
        }
    };

    public View.OnClickListener showAllCommentsListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            showAllComments = !showAllComments;
            if (showAllComments) {
                showAllCommentsView.setText("Show less comments");
            } else {
                showAllCommentsView.setText("Show all comments");
            }
            loadComments(highLow.getComments());
        }
    };

    private CompoundButton.OnCheckedChangeListener onCheckedChangeListener = (buttonView, isChecked) -> setPrivate(isChecked);

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }

    public void addHigh(View view) {
        Intent presenter = new Intent(getContext(), EditHLActivity.class);
        presenter.putExtra("type", "high");
        presenter.putExtra("date", highLow.getDate());
        getContext().startActivity(presenter);
    }
    public void addLow(View view) {
        Intent presenter = new Intent(getContext(), EditHLActivity.class);
        presenter.putExtra("type", "low");
        presenter.putExtra("date", highLow.getDate());
        getContext().startActivity(presenter);
    }

    public View.OnClickListener editHighListener = this::addHigh;

    public View.OnClickListener editLowListener = this::addLow;

    public View.OnClickListener like = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            highLow.like((highLow) -> {
            }, error -> {
                alert("An error occurred", "Please try again");
            });
        }
    };

    public View.OnClickListener unLike = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            highLow.unLike((highLow) -> {
            }, error -> {
                alert("An error occurred", "Please try again");
            });
        }
    };

    public View.OnClickListener flag = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle(R.string.flag_title)
                    .setMessage(R.string.flag_message)
            .setPositiveButton(R.string.flag_confirmation, (dialogInterface, i) -> {
                flagButton.setImageResource(R.drawable.ic_flag);
                highLow.flag(newHighLow -> {
                }, error -> {
                    alert("An error occurred", "Please try again");
                });
            }).setNegativeButton(R.string.cancel, (dialogInterface, i) -> {

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
                    .setPositiveButton(R.string.flag_confirmation, (dialogInterface, i) -> {
                        flagButton.setImageResource(R.drawable.ic_flag_grayed);
                        highLow.unFlag(newHighLow -> {
                        }, error -> {
                            alert("An error occurred", "Please try again");
                        });
                    }).setNegativeButton(R.string.cancel, (dialogInterface, i) -> {

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

    public void setPrivate(Boolean isPrivate) {
        if (isPrivate) {
            HighLowService.shared().makePrivate(highLow.getHighlowid(), highLow -> {

            }, error -> {
                alert("An error occurred", "Please try again");
                privateSwitch.setChecked(false);
            });
        } else {
            HighLowService.shared().makePublic(highLow.getHighlowid(), highLow -> {

            }, error -> {
                alert("An error occurred", "Please try again");
                privateSwitch.setChecked(true);
            });
        }
    }

    public void loadHighLow(HighLow highLow) {
        editable = highLow.getUid().equals(AuthService.shared().getUid());

        String highText = highLow.getHigh();
        String lowText = highLow.getLow();
        if (highLow.getHighlowid() != null)

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

        highImage.setImageBitmap(null);
        lowImage.setImageBitmap(null);

        String highImageUrl = highLow.getHighImage();
        String lowImageUrl = highLow.getLowImage();

        if (highImageUrl != null && !highImageUrl.equals("") && !highImageUrl.equals("NULL")) {
            ImageManager.shared().getImage("https://storage.googleapis.com/highlowfiles/highs/" + highImageUrl, (img) -> {
                highImage.setImageBitmap(img);
                highImage.setVisibility(View.VISIBLE);
            }, (error) -> {
            });
        }

        if (lowImageUrl != null && !lowImageUrl.equals("") && !lowImageUrl.equals("NULL")) {
            ImageManager.shared().getImage("https://storage.googleapis.com/highlowfiles/lows/" + highImageUrl, (img) -> {
                lowImage.setImageBitmap(img);
                lowImage.setVisibility(View.VISIBLE);
            }, (error) -> {
            });
        }

        addHigh.setVisibility(View.GONE);
        highNotGiven.setVisibility(View.GONE);
        editHigh.setVisibility(View.GONE);
        addLow.setVisibility(View.GONE);
        lowNotGiven.setVisibility(View.GONE);
        editLow.setVisibility(View.GONE);
        privateView.setVisibility(View.GONE);

        if (editable) {
            privateView.setVisibility(View.VISIBLE);
        }

        if (highLow.getPrivate() != null) {
            privateSwitch.setOnCheckedChangeListener(null);
            privateSwitch.setChecked(highLow.getPrivate());
            privateSwitch.setOnCheckedChangeListener(onCheckedChangeListener);
        }

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
                editHigh.setVisibility(View.GONE);
                privateSwitch.setVisibility(View.GONE);
            }
        } else {
            high.setVisibility(View.VISIBLE);
            highImage.setVisibility(View.VISIBLE);
            if (editable) {
                editHigh.setVisibility(View.VISIBLE);
                privateSwitch.setVisibility(View.VISIBLE);
            }
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
                editLow.setVisibility(View.GONE);
                privateSwitch.setVisibility(View.GONE);
            }
        } else {
            low.setVisibility(View.VISIBLE);
            lowImage.setVisibility(View.VISIBLE);
            if (editable) {
                editLow.setVisibility(View.VISIBLE);
                privateSwitch.setVisibility(View.VISIBLE);
            }
        }

        if (highLow.getDate() != null) {
            DateTimeFormatter toDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate localDate = LocalDate.parse(highLow.getDate(), toDate);
            DateTimeFormatter newFormatter = DateTimeFormatter.ofPattern("MMM d, uuuu");
            dateLabel.setText(localDate.format(newFormatter));
        }

        loadComments(this.highLow.getComments());

        UserManager.shared().getUser(AuthService.shared().getUid(), userLiveData -> {
            userLiveData.observe(lifecycleOwner , currentUserObserver);
        }, error -> {
            alert("An error occurred", "Try refreshing the page");
        });
    }

    private Observer<User> currentUserObserver = this::setCurrentUser;

    public void setCurrentUser(User user) {
        String url = "";
        String profileImageUrl = user.profileimage();
        if (profileImageUrl.startsWith("http")) {
            url = profileImageUrl;
        } else {
            url = "https://storage.googleapis.com/highlowfiles/" + profileImageUrl;
        }
        ImageManager.shared().getImage(url, bitmap -> {
            profileImage.setImageBitmap(bitmap);
        }, error -> {
            alert("An error occurred", "Please try again");
        });
    }

    public void startCommentLoading() {
        sendImg.setVisibility(View.GONE);
        sendProgress.setVisibility(View.VISIBLE);
    }

    public void stopCommentLoading() {
        sendImg.setVisibility(View.VISIBLE);
        sendProgress.setVisibility(View.GONE);
    }

    public void loadComments(List<CommentLiveData> commentsList) {
        //Now for the comments
        comments.removeAllViews();

        for (int i = 0; i < ((showAllComments) ? commentsList.size(): 3); i++) {
            if (i > commentsList.size() - 1) { return; }
            CommentLiveData comment = commentsList.get(i);
            CommentView commentView = new CommentView(getContext(), null);
            LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0, 30, 0, 0);
            commentView.setLayoutParams(layoutParams);
            commentView.attachToLiveData(comment);
            comments.addView(commentView);
        }
    }

    @Override
    public void onButtonClick(View view) {
        if (view == addHigh) {
            addHigh(view);
        } else if (view == addLow) {
            addLow(view);
        }
    }

    private final Observer<HighLow> onHighLowUpdate = this::loadHighLow;

    public void deleteComment(String commentId) {

    }

    public void editComment(String commentId) {

    }


    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String date = intent.getStringExtra("date");
            String highLowId = intent.getStringExtra("highlowid");
            if (date != null && highLow.getDate() != null) {
                if (highLow.getDate().equals(date)) {
                    highLow.update();
                }
            }
            else if (highLowId != null && highLow.getHighlowid() != null) {
                if (highLowId.equals(highLow.getHighlowid())) {
                    highLow.update();
                }
            }
        }
    };
}

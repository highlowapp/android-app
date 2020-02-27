package com.gethighlow.highlowandroid.CustomViews;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.gethighlow.highlowandroid.CustomViews.BaseComponents.HLButton;
import com.gethighlow.highlowandroid.CustomViews.BaseComponents.HLButtonDelegate;
import com.gethighlow.highlowandroid.R;
import com.gethighlow.highlowandroid.model.Services.HighLowService;

public class EditCommentActivity extends AppCompatActivity implements HLButtonDelegate {
    private EditText message;
    private HLButton submit;
    private String commentId;
    private String highLowId;
    private String prevMessage;
    private HLButton cancel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.edit_comment_layout);

        message = findViewById(R.id.edit_comment_message);
        submit = findViewById(R.id.edit_comment_submit_button);
        cancel = findViewById(R.id.cancel_edit);
        submit.delegate = this;
        cancel.delegate = this;

        commentId = getIntent().getStringExtra("commentId");
        highLowId = getIntent().getStringExtra("highlowid");
        prevMessage = getIntent().getStringExtra("prevMessage");

        if (prevMessage != null) message.setText(prevMessage);
    }

    private void alert(String title, String message) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setCancelable(true);
        alertDialog.setPositiveButton("OK", null);
        alertDialog.show();
    }

    @Override
    public void onButtonClick(View view) {
        if (view == submit) {
            submit.startLoading();
            HighLowService.shared().updateComment(commentId, message.getText().toString(), genericResponse -> {
                Intent newIntent = new Intent("highlow-updated");
                newIntent.putExtra("highlowid", highLowId);
                LocalBroadcastManager.getInstance(this).sendBroadcast(newIntent);
                submit.stopLoading();
                finish();
            }, error -> {
                submit.stopLoading();
                alert("An error occurred", "Please try again");
            });
        } else if (view == cancel) {
            this.finish();
        }
    }
}

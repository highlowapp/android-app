package com.gethighlow.highlowandroid.Activities.Tabs.Home;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.gethighlow.highlowandroid.CustomViews.BaseComponents.HLButton;
import com.gethighlow.highlowandroid.CustomViews.BaseComponents.HLButtonDelegate;
import com.gethighlow.highlowandroid.R;
import com.gethighlow.highlowandroid.model.Services.SetActivityTheme;
import com.gethighlow.highlowandroid.model.util.Consumer;
import com.gethighlow.highlowandroid.model.Responses.GenericResponse;
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
        String theme = SetActivityTheme.getTheme(getApplicationContext());
        if(theme.equals("light")){
            setTheme(R.style.LightTheme);
        }else{
            setTheme(R.style.DarkTheme);
        }

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

        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(themeReceiver, new IntentFilter("theme-updated"));

    }

    private void alert(String title, String message) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this, R.style.AlertDialogCustom);
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
            HighLowService.shared().updateComment(commentId, message.getText().toString(), new Consumer<GenericResponse>() {
                @Override
                public void accept(GenericResponse genericResponse) {
                    Intent newIntent = new Intent("highlow-updated");
                    newIntent.putExtra("highlowid", highLowId);
                    LocalBroadcastManager.getInstance(EditCommentActivity.this).sendBroadcast(newIntent);
                    submit.stopLoading();
                    EditCommentActivity.this.finish();
                }
            }, new Consumer<String>() {
                @Override
                public void accept(String error) {
                    submit.stopLoading();
                    EditCommentActivity.this.alert(getResources().getString(R.string.an_error_occurred), getResources().getString(R.string.please_try_again));
                }
            });
        } else if (view == cancel) {
            this.finish();
        }
    }

    private BroadcastReceiver themeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String currentTheme = SetActivityTheme.getTheme(context);
            if(currentTheme.equals("light")) {
                setTheme(R.style.LightTheme);
            } else if(currentTheme.equals("dark")){
                setTheme(R.style.DarkTheme);
            }

        }
    };

}

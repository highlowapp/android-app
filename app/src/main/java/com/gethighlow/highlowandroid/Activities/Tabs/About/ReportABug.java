package com.gethighlow.highlowandroid.Activities.Tabs.About;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.gethighlow.highlowandroid.CustomViews.BaseComponents.HLButton;
import com.gethighlow.highlowandroid.CustomViews.BaseComponents.HLButtonDelegate;
import com.gethighlow.highlowandroid.CustomViews.BaseComponents.TextInput;
import com.gethighlow.highlowandroid.R;
import com.gethighlow.highlowandroid.model.Services.SetActivityTheme;
import com.gethighlow.highlowandroid.model.util.Consumer;
import com.gethighlow.highlowandroid.model.Responses.GenericResponse;
import com.gethighlow.highlowandroid.model.Services.BugReportsService;

public class ReportABug extends AppCompatActivity implements HLButtonDelegate {
    private TextInput subject;
    private EditText message;
    private HLButton submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        String theme = SetActivityTheme.getTheme(getApplicationContext());
        if(theme.equals("light")){
            setTheme(R.style.LightTheme);
        }else{
            setTheme(R.style.DarkTheme);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_a_bug_activity);

        getSupportActionBar().setTitle("Report A Bug");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        subject = findViewById(R.id.subject);
        message = findViewById(R.id.message);
        submitButton = findViewById(R.id.submitButton);

        submitButton.delegate = this;

        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(themeReceiver, new IntentFilter("theme-updated"));


    }

    @Override
    public void onButtonClick(View view) {
        String subjectStr = subject.getText();
        String messageStr = message.getText().toString();

        submitButton.startLoading();

        BugReportsService.shared().submitBugReport(subjectStr, messageStr, new Consumer<GenericResponse>() {
            @Override
            public void accept(GenericResponse genericResponse) {
                submitButton.stopLoading();
                Toast.makeText(ReportABug.this, "Bug report sent", Toast.LENGTH_SHORT).show();
            }
        }, new Consumer<String>() {
            @Override
            public void accept(String error) {
                submitButton.stopLoading();
                ReportABug.this.alert(getResources().getString(R.string.an_error_occurred), getResources().getString(R.string.please_try_again));
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void alert(String title, String message) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this, R.style.AlertDialogCustom);
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setCancelable(true);
        alertDialog.setPositiveButton("OK", null);
        alertDialog.show();
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

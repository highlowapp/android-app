package com.gethighlow.highlowandroid.Activities.Authentication;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.gethighlow.highlowandroid.CustomViews.BaseComponents.HLButton;
import com.gethighlow.highlowandroid.CustomViews.BaseComponents.HLButtonDelegate;
import com.gethighlow.highlowandroid.CustomViews.BaseComponents.TextInput;
import com.gethighlow.highlowandroid.R;
import com.gethighlow.highlowandroid.model.Services.AuthService;

public class ForgotPasswordActivity extends Activity implements HLButtonDelegate {
    private TextInput emailInput;
    private HLButton sendConfirmation;
    private TextView errors;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.forgot_password_layout);

        emailInput = findViewById(R.id.emailInput);
        sendConfirmation = findViewById(R.id.sendConfirmation);
        errors = findViewById(R.id.errors);
        errors.setText("");
        sendConfirmation.delegate = this;
    }

    public void close(View view) {
        this.finish();
    }

    public void sendConfirmation(View view) {
        String email = emailInput.getText();
        sendConfirmation.startLoading();
        AuthService.shared().forgotPassword(email, (response) -> {
            sendConfirmation.stopLoading();
            errors.setText("Ok, you should be getting an email shortly!");
        }, (error) -> {
            sendConfirmation.stopLoading();

            if (error.equals("user-no-exist")) {
                errors.setText("No user exists with that email");
            }
            else if (error.equals("")) {
                errors.setText("Ok, you should be getting an email shortly!");
            }
            else {
                errors.setText("An unkown error occurred");
            }
        });
    }

    @Override
    public void onButtonClick(View view) {
        if (view == sendConfirmation) {
            sendConfirmation(view);
        }
    }

}

package com.gethighlow.highlowandroid.Activities.Authentication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.gethighlow.highlowandroid.CustomViews.HLButton;
import com.gethighlow.highlowandroid.CustomViews.HLButtonDelegate;
import com.gethighlow.highlowandroid.CustomViews.TextInput;
import com.gethighlow.highlowandroid.CustomViews.TextInputDelegate;
import com.gethighlow.highlowandroid.R;
import com.gethighlow.highlowandroid.model.Services.AuthService;


public class SignInActivity extends Activity implements TextInputDelegate, HLButtonDelegate {
    private ScrollView scrollView;
    private TextInput emailInput;
    private TextInput passwordInput;
    private TextView errors;
    private HLButton signInButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.sign_in_layout);
        scrollView = findViewById(R.id.sign_in_scrollview);

        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        errors = findViewById(R.id.errors);
        signInButton = findViewById(R.id.sign_in_button);
        signInButton.delegate = this;

        errors.setText("");

        emailInput.delegate = this;
        passwordInput.delegate = this;
    }

    public void signIn(View view) {
        Log.w("Debug", "Hi");

        String email = emailInput.getText();
        String password = passwordInput.getText();

        signInButton.startLoading();

        AuthService.shared().signIn(email, password, authResponse -> {
            signInButton.stopLoading();
            signInButton.setEnabled(true);
            Log.w("Debug", authResponse.access());
        }, error -> {
            signInButton.stopLoading();
            signInButton.setEnabled(true);
            switch(error) {
                case "incorrect-email-or-password":
                    errors.setText("The email or password entered was incorrect");
                    break;
                case "user-no-exist":
                    errors.setText("A user with that email does not exist");
                    break;
                default:
                    errors.setText("An unkown error occurred");
                    break;
            }
        });
    }

    public void openCreateAccountActivity(View view) {
        Intent presenter = new Intent(this, CreateAccountActivity.class);
        this.startActivity(presenter);
    }

    public void openForgotPasswordActivity(View view) {
        Intent presenter = new Intent(this, ForgotPasswordActivity.class);
        this.startActivity(presenter);
    }


    public void onFocusChange(View view, boolean b) {
        if (b) {
            int[] coords = {0, 0};
            view.getLocationOnScreen(coords);
            scrollView.scrollTo(coords[0], coords[1]);
        }
    }

    public void onButtonClick(View view) {
        Log.w("Debug", "TEST");
        if (view == signInButton) {
            signIn(view);
        }
    }
}

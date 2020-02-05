package com.gethighlow.highlowandroid.Activities.Authentication;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.gethighlow.highlowandroid.CustomViews.HLButton;
import com.gethighlow.highlowandroid.CustomViews.HLButtonDelegate;
import com.gethighlow.highlowandroid.CustomViews.TextInput;
import com.gethighlow.highlowandroid.CustomViews.TextInputDelegate;
import com.gethighlow.highlowandroid.R;
import com.gethighlow.highlowandroid.model.Services.AuthService;

public class CreateAccountActivity extends Activity implements HLButtonDelegate, TextInputDelegate {
    private TextInput firstName;
    private TextInput lastName;
    private TextInput email;
    private TextInput password;
    private TextInput confirmPassword;
    private TextView errors;
    private HLButton createAccountButton;
    private ScrollView scrollView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.create_account_layout);

        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirm_password);
        errors = findViewById(R.id.errors);
        createAccountButton = findViewById(R.id.create_account_button);

        createAccountButton.delegate = this;
        firstName.delegate = this;
        lastName.delegate = this;
        email.delegate = this;
        password.delegate = this;
        confirmPassword.delegate = this;

        scrollView = findViewById(R.id.scrollView);

        errors.setText("");
    }

    public void close(View view) {
        this.finish();
    }

    @Override
    public void onButtonClick(View view) {
        if (view == createAccountButton) {
            createAccount();
        }
    }

    public void createAccount() {
        createAccountButton.startLoading();
        AuthService.shared().signUp(firstName.getText(), lastName.getText(), email.getText(), password.getText(), confirmPassword.getText(), response -> {
            createAccountButton.stopLoading();


        }, error -> {
            createAccountButton.stopLoading();

            switch(error) {
                case "empty-first-name":
                    errors.setText("Please enter a first name");
                    break;
                case "empty-last-name":
                    errors.setText("Please enter a last name");
                    break;
                case "empty-email":
                    errors.setText("Please enter an email");
                    break;
                case "email-already-taken":
                    errors.setText("A user with that email already exists");
                    break;
                case "invalid-email":
                    errors.setText("The email given was invalid");
                    break;
                case "password-too-short":
                    errors.setText("Your password must be at least 6 characters");
                    break;
                case "passwords-no-match":
                    errors.setText("Password don't match");
                    break;
                default:
                    errors.setText("An unknown error occurred");
                    break;
            }

        });
    }

    @Override
    public void onFocusChange(View view, boolean b) {
        if (b) {
            int[] coords = {0, 0};
            view.getLocationOnScreen(coords);
            scrollView.scrollTo(coords[0], coords[1]);
        }
    };
}

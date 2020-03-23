package com.gethighlow.highlowandroid.Activities.Authentication;

import android.os.Bundle;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.gethighlow.highlowandroid.CustomViews.BaseComponents.HLButton;
import com.gethighlow.highlowandroid.CustomViews.BaseComponents.HLButtonDelegate;
import com.gethighlow.highlowandroid.CustomViews.BaseComponents.TextInput;
import com.gethighlow.highlowandroid.CustomViews.BaseComponents.TextInputDelegate;
import com.gethighlow.highlowandroid.R;
import com.gethighlow.highlowandroid.model.util.Consumer;
import com.gethighlow.highlowandroid.model.Responses.AuthResponse;
import com.gethighlow.highlowandroid.model.Services.AuthService;

public class CreateAccountActivity extends AppCompatActivity implements HLButtonDelegate, TextInputDelegate {
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
        AuthService.shared().signUp(firstName.getText(), lastName.getText(), email.getText(), password.getText(), confirmPassword.getText(), new Consumer<AuthResponse>() {
            @Override
            public void accept(AuthResponse response) {
                createAccountButton.stopLoading();


            }
        }, new Consumer<String>() {
            @Override
            public void accept(String error) {
                createAccountButton.stopLoading();

                if ("empty-first-name".equals(error)) {
                    errors.setText("Please enter a first name");
                } else if ("empty-last-name".equals(error)) {
                    errors.setText("Please enter a last name");
                } else if ("empty-email".equals(error)) {
                    errors.setText("Please enter an email");
                } else if ("email-already-taken".equals(error)) {
                    errors.setText("A user with that email already exists");
                } else if ("invalid-email".equals(error)) {
                    errors.setText("The email given was invalid");
                } else if ("password-too-short".equals(error)) {
                    errors.setText("Your password must be at least 6 characters");
                } else if ("passwords-no-match".equals(error)) {
                    errors.setText("Password don't match");
                } else {
                    errors.setText("An unknown error occurred");
                }

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

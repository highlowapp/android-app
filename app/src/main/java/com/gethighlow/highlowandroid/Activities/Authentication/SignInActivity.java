package com.gethighlow.highlowandroid.Activities.Authentication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.gethighlow.highlowandroid.CustomViews.BaseComponents.HLButton;
import com.gethighlow.highlowandroid.CustomViews.BaseComponents.HLButtonDelegate;
import com.gethighlow.highlowandroid.CustomViews.BaseComponents.TextInput;
import com.gethighlow.highlowandroid.CustomViews.BaseComponents.TextInputDelegate;
import com.gethighlow.highlowandroid.R;
import com.gethighlow.highlowandroid.model.util.Consumer;
import com.gethighlow.highlowandroid.model.Responses.AuthResponse;
import com.gethighlow.highlowandroid.model.Services.AuthService;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;


public class SignInActivity extends AppCompatActivity implements TextInputDelegate, HLButtonDelegate {
    private ScrollView scrollView;
    private TextInput emailInput;
    private TextInput passwordInput;
    private TextView errors;
    private HLButton signInButton;
    private GoogleSignInClient mGoogleSignInClient;
    private SignInButton googleSignInButton;

    private int GOOGLE_SIGN_IN = 80;

    @Override
    protected void onStart() {
        super.onStart();

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {

        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.sign_in_layout);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(getString(R.string.google_client_id))
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        scrollView = findViewById(R.id.sign_in_scrollview);

        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        errors = findViewById(R.id.errors);
        signInButton = findViewById(R.id.sign_in_button);

        googleSignInButton = findViewById(R.id.googleSignIn);
        googleSignInButton.setSize(SignInButton.SIZE_WIDE);

        googleSignInButton.setOnClickListener(googleSignInListener);

        signInButton.delegate = this;

        errors.setText("");

        emailInput.delegate = this;
        passwordInput.delegate = this;
    }

    private View.OnClickListener googleSignInListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            SignInActivity.this.startActivityForResult(signInIntent, GOOGLE_SIGN_IN);
        }
    };

    public void signIn(View view) {
        String email = emailInput.getText();
        String password = passwordInput.getText();

        signInButton.startLoading();

        AuthService.shared().signIn(email, password, new Consumer<AuthResponse>() {
            @Override
            public void accept(AuthResponse authResponse) {
                signInButton.stopLoading();
                signInButton.setEnabled(true);
                finish();
            }
        }, new Consumer<String>() {
            @Override
            public void accept(String error) {
                signInButton.stopLoading();
                signInButton.setEnabled(true);
                if ("incorrect-email-or-password".equals(error)) {
                    errors.setText("The email or password entered was incorrect");
                } else if ("user-no-exist".equals(error)) {
                    errors.setText("A user with that email does not exist");
                } else {
                    errors.setText("An unkown error occurred");
                }
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
        if (view == signInButton) {
            signIn(view);
        }
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == GOOGLE_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);

                String firstName = account.getGivenName();
                String lastName = account.getFamilyName();
                String email = account.getEmail();
                String profileImage = account.getPhotoUrl().toString();

                String providerName = "google";
                String provider_key = account.getIdToken();


                AuthService.shared().oauthSignIn(firstName, lastName, email, profileImage, provider_key, providerName, new Consumer<AuthResponse>() {
                    @Override
                    public void accept(AuthResponse authResponse) {

                    }
                }, new Consumer<String>() {
                    @Override
                    public void accept(String error) {
                        SignInActivity.this.alert(getResources().getString(R.string.an_error_occurred), getResources().getString(R.string.please_try_again));
                    }
                });
            } catch (ApiException e) {
            }
        }
    }
}

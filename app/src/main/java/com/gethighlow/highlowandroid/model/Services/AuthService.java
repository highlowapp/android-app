package com.gethighlow.highlowandroid.model.Services;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.android.volley.VolleyError;
import com.gethighlow.highlowandroid.R;
import com.gethighlow.highlowandroid.model.util.Consumer;
import com.gethighlow.highlowandroid.model.Responses.AuthResponse;
import com.gethighlow.highlowandroid.model.Responses.GenericResponse;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;


public class AuthService {
    private static final AuthService ourInstance = new AuthService();
    public static AuthService shared() { return ourInstance; }

    private Gson gson = new Gson();
    private String uid;
    private Context context;

    public String getUid() {
        return uid;
    }

    public AuthService() {}

    public void attachToContext(Context context) {
        this.context = context;
        SharedPreferences sharedPreferences = context.getSharedPreferences("com.gethighlow.SharedPref", Context.MODE_PRIVATE);
        uid = sharedPreferences.getString("uid", null);
    }

    public void logOut() {
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                if (task.isSuccessful()) {
                    String id = task.getResult().getId();
                    NotificationsService.shared().deRegister(id, new Consumer<GenericResponse>() {
                        @Override
                        public void accept(GenericResponse genericResponse) {

                        }
                    }, new Consumer<String>() {
                        @Override
                        public void accept(String error) {

                        }
                    });
                }
            }
        });
        SharedPreferences sharedPreferences = context.getSharedPreferences("com.gethighlow.SharedPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("access");
        editor.remove("refresh");
        editor.apply();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(context.getString(R.string.google_client_id))
                .build();
        GoogleSignInClient client = GoogleSignIn.getClient(context, gso);
        client.signOut();
        Intent newIntent = new Intent("com.gethighlow.com_logout");
        LocalBroadcastManager.getInstance(context).sendBroadcast(newIntent);
        APIService.shared().switchToAuth();
    }

    public void signIn(final String email, final String password, final Consumer<AuthResponse> onSuccess, final Consumer<String> onError) {
        Map<String, String> params = new HashMap<String, String>() {{
            put("email", email);
            put("password", password);
        }};

        APIService.shared().makeHTTPRequest("/auth/sign_in", 1, params, new Consumer<String>() {
            @Override
            public void accept(String response) {
                AuthResponse authResponse = gson.fromJson(response, AuthResponse.class);

                String error = authResponse.getError();
                if (error != null) {
                    onError.accept(error);
                } else {

                    APIService.shared().authenticate(authResponse);
                    uid = authResponse.uid();

                    onSuccess.accept(authResponse);

                }
            }
        }, new Consumer<VolleyError>() {
            @Override
            public void accept(VolleyError error) {
                onError.accept("network-error");
            }
        });
    }

    public void signUp(final String firstname, final String lastname, final String email, final String password, final String confirmPassword, final Consumer<AuthResponse> onSuccess, final Consumer<String> onError) {
        Map<String, String> params = new HashMap<String, String>() {{
            put("firstname", firstname);
            put("lastname", lastname);
            put("email", email);
            put("password", password);
            put("confirmpassword", confirmPassword);
            put("is_android", "1");
        }};

        Log.w("Debug", params.toString());

        APIService.shared().makeHTTPRequest("/auth/sign_up", 1, params, new Consumer<String>() {
            @Override
            public void accept(String response) {
                AuthResponse authResponse = gson.fromJson(response, AuthResponse.class);

                String error = authResponse.getError();
                if (error != null) {
                    onError.accept(error);
                } else {

                    APIService.shared().authenticate(authResponse);
                    uid = authResponse.uid();
                    onSuccess.accept(authResponse);

                }
            }
        }, new Consumer<VolleyError>() {
            @Override
            public void accept(VolleyError error) {
                onError.accept("network-error");
            }
        });
    }

    public void forgotPassword(String email, final Consumer<GenericResponse> onSuccess, final Consumer<String> onError) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("email", email);
        APIService.shared().makeHTTPRequest("/auth/forgot_password", 1, params, new Consumer<String>() {
            @Override
            public void accept(String response) {
                GenericResponse genericResponse = gson.fromJson(response, GenericResponse.class);

                String error = genericResponse.getError();
                if (error != null) {
                    onError.accept(error);
                } else {
                    onSuccess.accept(genericResponse);
                }
            }
        }, new Consumer<VolleyError>() {
            @Override
            public void accept(VolleyError error) {
                onError.accept("network-error");
            }
        });

    }

    public void oauthSignIn(String firstname, String lastname, String email, String profileimage, String provider_key, String provider_name, final Consumer<AuthResponse> onSuccess, final Consumer<String> onError) {
        Map<String, String> params = new HashMap<String, String>();

        if (firstname != null) {
            params.put("firstname", firstname);
        }
        if (lastname != null) {
            params.put("lastname", lastname);
        }
        if (email != null) {
            params.put("email", email);
        }
        if (profileimage != null) {
            params.put("profileimage", profileimage);
        }

        params.put("provider_key", provider_key);
        params.put("provider_name", provider_name);
        params.put("is_android", "1");

        APIService.shared().makeHTTPRequest("/auth/oauth/sign_in", 1, params, new Consumer<String>() {
            @Override
            public void accept(String response) {
                AuthResponse authResponse = gson.fromJson(response, AuthResponse.class);

                String error = authResponse.getError();
                if (error != null) {
                    onError.accept(error);
                } else {

                    APIService.shared().authenticate(authResponse);
                    uid = authResponse.uid();
                    onSuccess.accept(authResponse);

                }
            }
        }, new Consumer<VolleyError>() {
            @Override
            public void accept(VolleyError error) {
                onError.accept("network-error");
            }
        });
    }
}

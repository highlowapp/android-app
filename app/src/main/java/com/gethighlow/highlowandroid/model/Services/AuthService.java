package com.gethighlow.highlowandroid.model.Services;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.gethighlow.highlowandroid.model.Responses.AuthResponse;
import com.gethighlow.highlowandroid.model.Responses.GenericResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

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
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String id = task.getResult().getId();
                NotificationsService.shared().deRegister(id, genericResponse -> {

                }, error -> {

                });
            }
        });
        SharedPreferences sharedPreferences = context.getSharedPreferences("com.gethighlow.SharedPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("access");
        editor.remove("refresh");
        editor.apply();
        Intent newIntent = new Intent("com.gethighlow.com_logout");
        LocalBroadcastManager.getInstance(context).sendBroadcast(newIntent);
        APIService.shared().switchToAuth();
    }

    public void signIn(String email, String password, Consumer<AuthResponse> onSuccess, Consumer<String> onError) {
        Map<String, String> params = new HashMap<String, String>() {{
            put("email", email);
            put("password", password);
        }};

        APIService.shared().makeHTTPRequest("/auth/sign_in", 1, params, (response) -> {
            AuthResponse authResponse = gson.fromJson(response, AuthResponse.class);

            String error = authResponse.getError();
            if (error != null) {
                onError.accept(error);
            }
            else {

                APIService.shared().authenticate(authResponse);
                uid = authResponse.uid();

                onSuccess.accept(authResponse);

            }
        }, (error) -> {
            onError.accept("network-error");
        });
    }

    public void signUp(String firstname, String lastname, String email, String password, String confirmPassword, Consumer<AuthResponse> onSuccess, Consumer<String> onError) {
        Map<String, String> params = new HashMap<String, String>() {{
            put("firstname", firstname);
            put("lastname", lastname);
            put("email", email);
            put("password", password);
            put("confirmpassword", confirmPassword);
        }};

        APIService.shared().makeHTTPRequest("/auth/sign_up", 1, params, (response) -> {
            AuthResponse authResponse = gson.fromJson(response, AuthResponse.class);

            String error = authResponse.getError();
            if (error != null) {
                onError.accept(error);
            }
            else {

                APIService.shared().authenticate(authResponse);
                uid = authResponse.uid();
                onSuccess.accept(authResponse);

            }
        }, (error) -> {
            onError.accept("network-error");
        });
    }

    public void forgotPassword(String email, Consumer<GenericResponse> onSuccess, Consumer<String> onError) {
        Map<String, String> params = new HashMap<>();
        params.put("email", email);
        APIService.shared().makeHTTPRequest("/auth/forgot_password", 1, params, (response) -> {
            GenericResponse genericResponse = gson.fromJson(response, GenericResponse.class);

            String error = genericResponse.getError();
            if (error != null) {
                onError.accept(error);
            } else {
                onSuccess.accept(genericResponse);
            }
        }, (error) -> {
            onError.accept("network-error");
        });

    }

    public void oauthSignIn(String firstname, String lastname, String email, String profileimage, String provider_key, String provider_name, Consumer<AuthResponse> onSuccess, Consumer<String> onError) {
        Map<String, String> params = new HashMap<>();

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

        APIService.shared().makeHTTPRequest("/auth/oauth/sign_in", 1, params, (response) -> {
            AuthResponse authResponse = gson.fromJson(response, AuthResponse.class);

            String error = authResponse.getError();
            if (error != null) {
                onError.accept(error);
            }
            else {

                APIService.shared().authenticate(authResponse);
                uid = authResponse.uid();
                onSuccess.accept(authResponse);

            }
        }, (error) -> {
            onError.accept("network-error");
        });
    }
}

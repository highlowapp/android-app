package com.gethighlow.highlowandroid.model.Services;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

import androidx.core.content.ContextCompat;
import androidx.core.util.Consumer;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.NetworkResponse;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.gethighlow.highlowandroid.Activities.MainActivity;
import com.gethighlow.highlowandroid.model.APIConfig;
import com.gethighlow.highlowandroid.model.Responses.AuthResponse;
import com.gethighlow.highlowandroid.model.Responses.GenericResponse;
import com.gethighlow.highlowandroid.model.VolleyMultipartRequest;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class APIService implements APIConfig {
    private static final APIService ourInstance = new APIService();

    public static APIService shared() {
        return ourInstance;
    }

    private String accessToken;
    private String refreshToken;

    private Context context;
    private MainActivity mainActivity;

    RequestQueue requestQueue;

    private SharedPreferences sharedPref;
    private SharedPreferences.Editor sharedPrefEditor;

    private APIService() {}

    public void authenticate(AuthResponse response) {
        accessToken = response.access();
        refreshToken = response.refresh();
        String uid = response.uid();

        sharedPrefEditor.putString("access", accessToken);
        sharedPrefEditor.putString("refresh", refreshToken);
        sharedPrefEditor.putString("uid", uid);
        sharedPrefEditor.apply();

        mainActivity.isAuthenticated();
    }

    public void setAccessToken(String token) {
        accessToken = token;
        sharedPrefEditor.putString("access", accessToken);
        sharedPrefEditor.apply();
    }

    private void initiateRequestQueue() {
        Cache cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024);
        Network network = new BasicNetwork(new HurlStack());

        requestQueue = new RequestQueue(cache, network);

        requestQueue.start();
    }

    public void attachToContext(MainActivity context) {
        this.context = context;
        this.mainActivity = context;
        sharedPref = context.getSharedPreferences("com.gethighlow.SharedPref", Context.MODE_PRIVATE);
        sharedPrefEditor = sharedPref.edit();

        accessToken = sharedPref.getString("access", null);
        refreshToken = sharedPref.getString("refresh", null);

        initiateRequestQueue();
    }

    public void switchToAuth() {
        mainActivity.switchToAuth();
    }

    private static String urlFromMap(String url, Map<String, String> params) {
        if (params == null) return base_url + url;
        StringBuilder completeUrl = new StringBuilder();
        completeUrl.append(base_url);
        completeUrl.append(url);
        int i = 0;
        for (String key: params.keySet()) {
            if (i == 0) {
                completeUrl.append('?');
                completeUrl.append(key);
                completeUrl.append('=');
                completeUrl.append(params.get(key));
            } else {
                completeUrl.append('&');
                completeUrl.append(key);
                completeUrl.append('=');
                completeUrl.append(params.get(key));
            }
        }

        return completeUrl.toString();
    }

    public void makeHTTPRequest(String url, int method, Map<String, String> params, Consumer<String> onSuccess, Consumer<VolleyError> onError) {
        if (requestQueue == null) return;

        String completeUrl;

        if (method == 0) {
            completeUrl = urlFromMap(url, params);
        } else {
            completeUrl = base_url + url;
        }


        StringRequest request = new StringRequest(method, completeUrl, onSuccess::accept, onError::accept) {
            @Override
            public Map<String, String> getParams() {
                if (method == 0) return null;
                return params;
            }
        };

        requestQueue.add(request);
    }

    private void refresh_access(Consumer<Boolean> callback) {
        if (refreshToken == null) {
            callback.accept(false);
            mainActivity.switchToAuth();
            return;
        }

        Map<String, String> params = new HashMap<>();
        params.put("refresh", refreshToken);
        makeHTTPRequest("/auth/refresh_access", 1, params, (response) -> {
            Gson gson = new Gson();
            AuthResponse authResponse = gson.fromJson(response, AuthResponse.class);

            if (authResponse.getError() != null) {
                callback.accept(false);
                mainActivity.switchToAuth();
            } else {
                accessToken = authResponse.access();
                sharedPrefEditor.putString("access", accessToken);
                sharedPrefEditor.apply();

                //Retry request
                callback.accept(true);
            }
        }, (error) -> {
            callback.accept(false);
        });
    }

    public void authenticatedRequest(String url, int method, Map<String, String> params, Consumer<String> onSuccess, Consumer<String> onError) {
        if (requestQueue == null) return;

        String completeUrl;

        if (method == 0) {
            completeUrl = urlFromMap(url, params);
        } else {
            completeUrl = base_url + url;
        }
        Log.w("Debug", completeUrl);
        StringRequest request = new StringRequest(method, completeUrl, response -> {
            Gson gson = new Gson();
            GenericResponse genericResponse = gson.fromJson(response, GenericResponse.class);

            if (genericResponse.getError() != null) {

                if (genericResponse.getError().equals("ERROR-INVALID-TOKEN")) {
                    refresh_access((shouldContinue) -> {
                        if (shouldContinue) {
                            authenticatedRequest(url, method, params, onSuccess, onError);
                        } else {
                            mainActivity.switchToAuth();
                        }
                    });
                } else {
                    onError.accept(genericResponse.getError());
                }

            }

            else {
                onSuccess.accept(response);
            }

        }, error -> onError.accept("network-error")) {
            @Override
            public Map<String, String> getParams() {
                return params;
            }
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + accessToken);
                return headers;
            }
        };

        requestQueue.add(request);
    }

    public void makeMultipartRequest(String url, int method, Map<String, String> params, Bitmap image, Consumer<String> onSuccess, Consumer<String> onError) {
        if (requestQueue == null) return;

        Log.w("Debug", base_url + url);
        VolleyMultipartRequest request = new VolleyMultipartRequest(method, base_url + url, response -> {
            String json = new String(response.data);
            Gson gson = new Gson();
            GenericResponse genericResponse = gson.fromJson(json, GenericResponse.class);

            String error = genericResponse.getError();
            if (error != null) {
                if (error.equals("ERROR-INVALID-TOKEN")) {
                    refresh_access((shouldContinue) -> {
                        if (shouldContinue) {
                            makeMultipartRequest(url, method, params, image, onSuccess, onError);
                        } else {
                            mainActivity.switchToAuth();
                        }
                    });
                } else {
                    onError.accept(error);
                }
            } else {
                onSuccess.accept(json);
            }
        }, error -> onError.accept("network-error")) {
            @Override
            protected Map<String, String> getParams() {
                return params;
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + accessToken);
                return headers;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                if (image != null) {
                    params.put("file", new DataPart("file.jpg", getFileDataFromBitmap(context, image), "image/jpeg"));
                }
                return params;
            }
        };

        requestQueue.add(request);
    }





    /**
     * Turn drawable resource into byte array.
     *
     * @param context parent context
     * @param id      drawable resource id
     * @return byte array
     */
    public static byte[] getFileDataFromDrawable(Context context, int id) {
        Drawable drawable = ContextCompat.getDrawable(context, id);
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    /**
     * Turn drawable into byte array.
     *
     * @param drawable data
     * @return byte array
     */
    public static byte[] getFileDataFromDrawable(Context context, Drawable drawable) {
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public static byte[] getFileDataFromBitmap(Context context, Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }
}

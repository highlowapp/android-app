package com.gethighlow.highlowandroid.model.Managers;

import android.app.ActivityManager;
import android.content.Context;
import android.util.LruCache;

import com.gethighlow.highlowandroid.model.Managers.Caches.UserCache;
import com.gethighlow.highlowandroid.model.Resources.User;
import com.gethighlow.highlowandroid.model.Services.UserService;

import java.util.function.Consumer;

public class UserManager {
    private static final UserManager ourInstance = new UserManager();

    public static UserManager shared() { return ourInstance; }

    private Context context;
    private UserCache cache;

    public void attachToContext(Context context) {
        this.context = context;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        int maxKb = am.getMemoryClass() * 1024;
        int limitKb = maxKb / 16;

        cache = new UserCache(limitKb);
    }

    public void getUser(String uid, Consumer<User> onSuccess, Consumer<String> onError) {
        User user = cache.get(uid);
        if (user == null) {

            UserService.shared().getUser(uid, (newUser) -> {

                cache.put(uid, newUser);
                onSuccess.accept(newUser);

            }, (error) -> {
                onError.accept(error);
            });

        } else {
            onSuccess.accept(user);
        }
    }

    public void saveUser(String uid, User user) {
        cache.put(uid, user);
    }

    public void saveUser(User user) {
        cache.put(user.uid(), user);
    }
}
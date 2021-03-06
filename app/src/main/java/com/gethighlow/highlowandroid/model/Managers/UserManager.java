package com.gethighlow.highlowandroid.model.Managers;

import android.app.ActivityManager;
import android.content.Context;

import com.gethighlow.highlowandroid.model.util.Consumer;
import com.gethighlow.highlowandroid.model.Managers.LiveDataModels.UserLiveData;
import com.gethighlow.highlowandroid.model.Resources.User;
import com.gethighlow.highlowandroid.model.Services.UserService;

import java.util.HashMap;
import java.util.Map;

public class UserManager {
    private static final UserManager ourInstance = new UserManager();

    public static UserManager shared() { return ourInstance; }

    private Context context;
    private Map<String, UserLiveData> userCache = new HashMap<String, UserLiveData>();

    public void attachToContext(Context context) {
        this.context = context;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
    }

    public void getUser(final String uid, final Consumer<UserLiveData> onSuccess, Consumer<String> onError) {
        UserLiveData user = userCache.get(uid);

        if (user == null) {
            UserService.shared().getUser(uid, new Consumer<User>() {
                @Override
                public void accept(User newUser) {
                    UserLiveData liveData = new UserLiveData(newUser);
                    userCache.put(uid, liveData);
                    onSuccess.accept(liveData);
                }
            }, onError);

        } else {
            onSuccess.accept(user);
        }
    }

    public UserLiveData saveUser(String uid, User user) {
        UserLiveData liveData = new UserLiveData(user);
        userCache.put(uid, liveData);
        return liveData;
    }

    public UserLiveData saveUser(User user) {
        UserLiveData liveData = new UserLiveData(user);
        userCache.put(user.uid(), liveData);
        return liveData;
    }
}
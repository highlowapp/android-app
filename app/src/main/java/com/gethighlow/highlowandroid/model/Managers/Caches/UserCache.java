package com.gethighlow.highlowandroid.model.Managers.Caches;

import android.util.LruCache;

import com.gethighlow.highlowandroid.model.Resources.User;

public class UserCache extends LruCache<String, User> {
    public UserCache(int maxSize) { super(maxSize); }

    @Override
    protected int sizeOf(String key, User value) {
        return 1;
    }
}

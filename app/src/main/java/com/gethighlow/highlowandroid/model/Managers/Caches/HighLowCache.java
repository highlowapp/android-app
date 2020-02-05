package com.gethighlow.highlowandroid.model.Managers.Caches;

import android.util.LruCache;

import com.gethighlow.highlowandroid.model.Resources.HighLow;

public class HighLowCache extends LruCache<String, HighLow> {
    public HighLowCache(int maxSize) { super(maxSize); }

    @Override
    protected int sizeOf(String key, HighLow value) {
        return 1;
    }
}

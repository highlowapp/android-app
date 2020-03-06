package com.gethighlow.highlowandroid.model.Managers.Caches;

import android.graphics.Bitmap;
import android.util.LruCache;

public class ImageCache extends LruCache<String, Bitmap> {
    public ImageCache(int maxSize) { super(maxSize); }

    @Override
    protected int sizeOf(String key, Bitmap value) {
        return value.getByteCount() / 1024;
    }
}

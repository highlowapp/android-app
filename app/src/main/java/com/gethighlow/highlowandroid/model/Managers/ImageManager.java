package com.gethighlow.highlowandroid.model.Managers;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.gethighlow.highlowandroid.model.util.Consumer;
import com.gethighlow.highlowandroid.model.Managers.Caches.ImageCache;

public class ImageManager {
    private static final ImageManager ourInstance = new ImageManager();
    public static ImageManager shared() { return ourInstance; }

    private Context context;
    private ImageCache cache;

    public void attachToContext(Context context) {
        this.context = context;

        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        int maxKb = am.getMemoryClass() * 1024;
        int limitKb = maxKb / 8;
        cache = new ImageCache(limitKb);
    }

    public void getImage(final String url, final Consumer<Bitmap> onSuccess, final Consumer<String> onError) {
        if (cache == null) {
            onError.accept("cache-no-exist");
            return;
        }

        Bitmap img = cache.get(url);
        if (img == null) {
            DownloadImageTask task = new DownloadImageTask(new Consumer<Bitmap>() {
                @Override
                public void accept(Bitmap image) {
                    if (image == null) {
                        onError.accept("no-image");
                    } else {
                        cache.put(url, image);
                        onSuccess.accept(image);
                    }
                }
            });
            task.execute(url);
        } else {
            onSuccess.accept(img);
        }
    }

    public void invalidateUrl(final String url) {
        cache.remove(url);
    }

    public void saveImage(String url, Bitmap image) { cache.put(url, image); }
}

package com.gethighlow.highlowandroid.model.Managers;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;

import com.gethighlow.highlowandroid.model.Managers.Caches.ImageCache;

import java.util.function.Consumer;

public class ImageManager {
    private static final ImageManager ourInstance = new ImageManager();
    public static ImageManager shared() { return ourInstance; }

    private Context context;
    private ImageCache cache;

    public void attachToContext(Context context) {
        this.context = context;

        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        int maxKb = am.getMemoryClass() * 1024;
        int limitKb = maxKb / 16;

        cache = new ImageCache(limitKb);
    }

    public void getImage(String url, Consumer<Bitmap> onSuccess, Consumer<String> onError) {
        Bitmap img = cache.get(url);

        if (img == null) {
            DownloadImageTask task = new DownloadImageTask((image) -> {
                if (image == null) {
                    onError.accept("no-image");
                } else {
                    onSuccess.accept(image);
                }
            });
            task.execute(url);
        } else {
            onSuccess.accept(img);
        }
    }

    public void saveImage(String url, Bitmap image) { cache.put(url, image); }
}

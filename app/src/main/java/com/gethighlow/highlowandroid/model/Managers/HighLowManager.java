package com.gethighlow.highlowandroid.model.Managers;

import android.app.ActivityManager;
import android.content.Context;

import com.gethighlow.highlowandroid.model.Managers.Caches.HighLowCache;
import com.gethighlow.highlowandroid.model.Resources.HighLow;
import com.gethighlow.highlowandroid.model.Services.HighLowService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class HighLowManager {
    private static final HighLowManager ourInstance = new HighLowManager();
    public static HighLowManager shared() { return ourInstance; }

    private HighLowCache cache;

    private Map<String, HighLow> cachedHighLows = new HashMap<>();

    private Context context;

    public void attachToContext(Context context) {
        this.context = context;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        int maxKb = am.getMemoryClass() * 1024;
        int limitKb = maxKb / 16;

        cache = new HighLowCache(0);
    }

    public void getHighLow(String highlowid, Consumer<HighLow> onSuccess, Consumer<String> onError) {
        HighLow highLow = cachedHighLows.getOrDefault(cachedHighLows, null);

        if (highLow == null) {
            HighLowService.shared().get(highlowid, (newHighLow) -> {
                cachedHighLows.put(highlowid, newHighLow);
                onSuccess.accept(newHighLow);
            }, onError);
        } else {
            onSuccess.accept(highLow);
        }
    }

    public void saveHighLow(String highlowid, HighLow highLow) {
        cachedHighLows.put(highlowid, highLow);
    }

    public void saveHighLow(HighLow highLow) {
        cachedHighLows.put(highLow.getHighlowid(), highLow);
    }

    public void getHighLowByDate(String date, Consumer<HighLow> onSuccess, Consumer<String> onError) {
        HighLow highLow = null;
        for (String key: cachedHighLows.keySet()) {
            HighLow curr = cachedHighLows.get(key);
            if (curr.getDate().equals(date)) {
                highLow = curr;
                break;
            }
        }
        if (highLow == null) {
            HighLowService.shared().getDate(date, (newHighLow) -> {
                cachedHighLows.put(newHighLow.getHighlowid(), newHighLow);
                onSuccess.accept(newHighLow);
            }, onError);
        } else {
            onSuccess.accept(highLow);
        }
    }

    public void getToday(Consumer<HighLow> onSuccess, Consumer<String> onError) {
        HighLow highLow = null;
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-mm-dd");
        String dateStr = formatter.format(today);

        for (String key: cachedHighLows.keySet()) {
            HighLow curr = cachedHighLows.get(key);
            if (curr.getDate().equals(dateStr)) {
                highLow = curr;
                break;
            }
        }

        if (highLow == null) {
            HighLowService.shared().getToday((newHighLow) -> {
                cachedHighLows.put(newHighLow.getHighlowid(), newHighLow);
                onSuccess.accept(newHighLow);
            }, onError);
        } else {
            onSuccess.accept(highLow);
        }
    }

    public void saveHighLowForDate(String date, HighLow highLow) {
        cachedHighLows.put(highLow.getHighlowid(), highLow);
    }

    public void saveTodayHighLow(HighLow highLow) {
        cachedHighLows.put(highLow.getHighlowid(), highLow);
    }
}

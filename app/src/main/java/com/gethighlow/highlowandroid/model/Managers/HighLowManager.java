package com.gethighlow.highlowandroid.model.Managers;

import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;

import com.gethighlow.highlowandroid.model.Managers.Caches.HighLowCache;
import com.gethighlow.highlowandroid.model.Managers.LiveDataModels.HighLowLiveData;
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

    private Map<String, HighLowLiveData> cachedHighLows = new HashMap<>();
    private Map<String, HighLowLiveData> dateHighLows = new HashMap<>();
    private HighLowLiveData today;

    private Context context;

    public void attachToContext(Context context) {
        this.context = context;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        int maxKb = am.getMemoryClass() * 1024;
        int limitKb = maxKb / 16;

    }

    public void getHighLow(String highlowid, Consumer<HighLowLiveData> onSuccess, Consumer<String> onError) {
        HighLowLiveData highLow = cachedHighLows.getOrDefault(highlowid, null);

        if (highLow == null) {
            HighLowService.shared().get(highlowid, (newHighLow) -> {
                HighLowLiveData liveDataObject = new HighLowLiveData(newHighLow);
                cachedHighLows.put(highlowid, liveDataObject);
                onSuccess.accept(liveDataObject);
            }, onError);
        } else {
            onSuccess.accept(highLow);
        }
    }

    public void saveHighLow(String highlowid, HighLow highLow) {
        cachedHighLows.put(highlowid, new HighLowLiveData(highLow));
    }

    public void saveHighLow(HighLow highLow) {
        cachedHighLows.put(highLow.getHighlowid(), new HighLowLiveData(highLow));
    }

    public void getHighLowByDate(String date, Consumer<HighLowLiveData> onSuccess, Consumer<String> onError) {
        HighLowLiveData highLow = dateHighLows.getOrDefault(date, null);
        if (highLow == null) {
            HighLowService.shared().getDate(date, (newHighLow) -> {
                HighLowLiveData liveData = new HighLowLiveData(newHighLow);
                if (newHighLow.getDate() != null) {
                    dateHighLows.put(newHighLow.getDate(), liveData);
                }
                onSuccess.accept(liveData);
            }, onError);
        } else {
            onSuccess.accept(highLow);
        }
    }

    public void getToday(Consumer<HighLowLiveData> onSuccess, Consumer<String> onError) {
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String dateStr = formatter.format(today);

        if (today == null) {
            HighLowService.shared().getToday((newHighLow) -> {
                this.today = new HighLowLiveData(newHighLow);
                onSuccess.accept(this.today);
            }, onError);
        } else {
            onSuccess.accept(this.today);
        }
    }

    public void saveHighLowForDate(String date, HighLow highLow) {
        dateHighLows.put(date, new HighLowLiveData(highLow));
    }

    public void saveTodayHighLow(HighLow highLow) {
        today = new HighLowLiveData(highLow);
    }
}

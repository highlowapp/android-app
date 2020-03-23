package com.gethighlow.highlowandroid.model.Managers;

import android.app.ActivityManager;
import android.content.Context;

import com.gethighlow.highlowandroid.model.util.Consumer;
import com.gethighlow.highlowandroid.model.Managers.LiveDataModels.HighLowLiveData;
import com.gethighlow.highlowandroid.model.Resources.HighLow;
import com.gethighlow.highlowandroid.model.Services.AuthService;
import com.gethighlow.highlowandroid.model.Services.HighLowService;

import org.threeten.bp.LocalDate;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HighLowManager {
    private static final HighLowManager ourInstance = new HighLowManager();
    public static HighLowManager shared() { return ourInstance; }

    private Map<String, HighLowLiveData> cachedHighLows = new HashMap<String, HighLowLiveData>();
    private Map<String, HighLowLiveData> dateHighLows = new HashMap<String, HighLowLiveData>();
    private HighLowLiveData today;

    private Context context;

    public void attachToContext(Context context) {
        this.context = context;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        int maxKb = am.getMemoryClass() * 1024;
        int limitKb = maxKb / 16;

    }

    public List<HighLowLiveData> restoreHighLows(List<String> highlowids) {
        List<HighLowLiveData> result = new ArrayList<HighLowLiveData>();
        for (String highlowid: highlowids) {
            HighLowLiveData highLowLiveData = null;
            if (cachedHighLows.containsKey(highlowid)) {
                highLowLiveData = cachedHighLows.get(highlowid);
            }
            if (highLowLiveData == null) return null;
            result.add(highLowLiveData);
        }
        return result;
    }

    public void getHighLow(final String highlowid, final Consumer<HighLowLiveData> onSuccess, Consumer<String> onError) {
        HighLowLiveData highLow = null;
        if (cachedHighLows.containsKey(highlowid)) {
            highLow = cachedHighLows.get(highlowid);
        }

        if (highLow == null) {
            HighLowService.shared().get(highlowid, new Consumer<HighLow>() {
                @Override
                public void accept(HighLow newHighLow) {
                    HighLowLiveData liveDataObject = new HighLowLiveData(newHighLow);
                    cachedHighLows.put(highlowid, liveDataObject);
                    onSuccess.accept(liveDataObject);
                }
            }, onError);
        } else {
            onSuccess.accept(highLow);
        }
    }



    public HighLowLiveData saveHighLow(String highlowid, HighLow highLow) {
        if (cachedHighLows.containsKey(highlowid)) {
            HighLowLiveData liveData = cachedHighLows.get(highlowid);
            liveData.setValue(highLow);
            return liveData;
        } else {
            HighLowLiveData liveData = new HighLowLiveData(highLow);
            cachedHighLows.put(highlowid, liveData);
            return liveData;
        }
    }

    public HighLowLiveData saveHighLow(HighLow highLow) {
        HighLowLiveData aLiveData = saveHighLow(highLow.getHighlowid(), highLow);

        if (highLow.getUid().equals(AuthService.shared().getUid())) {
            if (dateHighLows.containsKey(highLow.getDate())) {
                HighLowLiveData liveData = dateHighLows.get(highLow.getDate());
                liveData.setValue(highLow);
                return liveData;
            } else {
                HighLowLiveData liveData = new HighLowLiveData(highLow);
                dateHighLows.put(highLow.getDate(), liveData);
                return liveData;
            }
        }

        return aLiveData;
    }

    public void getHighLowByDate(String date, final Consumer<HighLowLiveData> onSuccess, Consumer<String> onError) {
        HighLowLiveData highLow = null;
        if (dateHighLows.containsKey(date)) {
            dateHighLows.get(date);
        }
        if (highLow == null) {
            HighLowService.shared().getDate(date, new Consumer<HighLow>() {
                @Override
                public void accept(HighLow newHighLow) {
                    HighLowLiveData liveData = new HighLowLiveData(newHighLow);
                    if (newHighLow.getDate() != null) {
                        dateHighLows.put(newHighLow.getDate(), liveData);
                    }
                    onSuccess.accept(liveData);
                }
            }, onError);
        } else {
            onSuccess.accept(highLow);
        }
    }

    public void getToday(final Consumer<HighLowLiveData> onSuccess, Consumer<String> onError) {
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String dateStr = formatter.format(today);

        if (today == null) {
            HighLowService.shared().getToday(new Consumer<HighLow>() {
                @Override
                public void accept(HighLow newHighLow) {
                    HighLowManager.this.today = new HighLowLiveData(newHighLow);
                    onSuccess.accept(HighLowManager.this.today);
                }
            }, onError);
        } else {
            onSuccess.accept(this.today);
        }
    }

    public HighLowLiveData saveHighLowForDate(String date, HighLow highLow) {
        if (cachedHighLows.containsKey(date)) {
            HighLowLiveData liveData = cachedHighLows.get(date);
            liveData.setValue(highLow);
            return liveData;
        } else {
            HighLowLiveData liveData = new HighLowLiveData(highLow);
            cachedHighLows.put(date, liveData);
            return liveData;
        }
    }

    public HighLowLiveData saveTodayHighLow(HighLow highLow) {
        if (today != null) {
            today.setValue(highLow);
            return today;
        }
        HighLowLiveData liveData = new HighLowLiveData(highLow);
        today = liveData;
        return liveData;
    }
}

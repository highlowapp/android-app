package com.gethighlow.highlowandroid.Activities.Tabs.Home;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.gethighlow.highlowandroid.CustomViews.Other.HighLowView;
import com.gethighlow.highlowandroid.R;
import com.gethighlow.highlowandroid.model.Services.SetActivityTheme;
import com.gethighlow.highlowandroid.model.util.Consumer;
import com.gethighlow.highlowandroid.model.Managers.HighLowManager;
import com.gethighlow.highlowandroid.model.Managers.LiveDataModels.HighLowLiveData;
import com.gethighlow.highlowandroid.model.util.Runnable;

import org.threeten.bp.LocalDate;
import org.threeten.bp.format.DateTimeFormatter;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeViewLayout extends LinearLayout implements SwipeRefreshLayout.OnRefreshListener {
    private HighLowLiveData highLow;
    private Fragment fragment;
    private TextView textView;
    private HighLowView highLowView;
    private SwipeRefreshLayout refreshLayout;

    public HomeViewLayout(Fragment fragment, AttributeSet attributeSet, LocalDate localDate) {
        super(fragment.getContext(), attributeSet);

        setup(fragment, attributeSet, localDate);
    }

    private void setup(Fragment fragment, @Nullable AttributeSet attributeSet, LocalDate localDate) {

/*        Context context = getContext();

        String currentTheme = SetActivityTheme.getTheme(context);
        Log.d("Current theme", currentTheme);
        if(currentTheme.equals("light")) {
            final ContextThemeWrapper theme = new ContextThemeWrapper(context, R.style.LightTheme);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            LayoutInflater localInflater = inflater.cloneInContext(theme);
            localInflater.inflate(R.layout.home_view_layout, this, true);
            this.fragment = fragment;
            this.setOrientation(LinearLayout.VERTICAL);
        } else if(currentTheme.equals("dark")){
            final ContextThemeWrapper theme = new ContextThemeWrapper(context, R.style.DarkTheme);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            LayoutInflater localInflater = inflater.cloneInContext(theme);
            localInflater.inflate(R.layout.home_view_layout, this, true);
            this.fragment = fragment;
            this.setOrientation(LinearLayout.VERTICAL);
        }*/



        LayoutInflater inflater = (LayoutInflater) fragment.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.home_view_layout, this, true);
        this.fragment = fragment;
        this.setOrientation(LinearLayout.VERTICAL);



//        setColor(context);

        //this.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));


        highLowView = findViewById(R.id.highlowview);
        refreshLayout = findViewById(R.id.homeViewRefresher);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setColorSchemeColors(Color.RED);


//        LocalBroadcastManager.getInstance(getContext()).registerReceiver(themeReceiver, new IntentFilter("theme-updated"));
    }

    private String prettyFormat(LocalDate localDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return formatter.format(localDate);
    }

    public void setHighlowid(String highlowid) {
        HighLowManager.shared().getHighLow(highlowid, new Consumer<HighLowLiveData>() {
            @Override
            public void accept(HighLowLiveData highLowLiveData) {
                highLow = highLowLiveData;
                highLowView.attachToLiveData(fragment, highLowLiveData);
            }
        }, new Consumer<String>() {
            @Override
            public void accept(String s) {

            }
        });
    }

    public void setDate(LocalDate localDate) {
        HighLowManager.shared().getHighLowByDate(prettyFormat(localDate), new Consumer<HighLowLiveData>() {
            @Override
            public void accept(HighLowLiveData highLowLiveData) {
                highLow = highLowLiveData;
                highLowView.setDate(localDate);
                highLowView.attachToLiveData(fragment, highLowLiveData);
            }
        }, new Consumer<String>() {
            @Override
            public void accept(String error) {
            }
        });
    }

    @Override
    public void onRefresh() {
        highLowView.highLow.update(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(false);
            }
        });
    }
/*

    private void setColor(Context context){

        String theme = SetActivityTheme.getTheme(context);

        if(theme.equals("light")){
            this.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
        } else if(theme.equals("dark")){
            this.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.black));
        }

    }


    private BroadcastReceiver themeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            setColor(context);
        }
    };*/
}

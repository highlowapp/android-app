package com.gethighlow.highlowandroid.Activities.Tabs.About;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.gethighlow.highlowandroid.model.Services.SetActivityTheme;
import com.gethighlow.highlowandroid.model.util.AlarmNotificationReceiver;
import com.gethighlow.highlowandroid.R;
import com.gethighlow.highlowandroid.model.util.Consumer;
import com.gethighlow.highlowandroid.model.Responses.GenericResponse;
import com.gethighlow.highlowandroid.model.Responses.NotificationsSettingsResponse;
import com.gethighlow.highlowandroid.model.Services.NotificationsService;

import org.threeten.bp.LocalTime;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class PushNotifSettings extends AppCompatActivity {

    private SwitchCompat newFriendReq, newFriendAcc, newFeedItems, newLikes, newComments;
    private Button datePickerButton, cancelReminder;
    private Map<SwitchCompat, String> values = new HashMap<SwitchCompat, String>();
    private LocalTime currTime = LocalTime.now();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String theme = SetActivityTheme.getTheme(getApplicationContext());
        if(theme.equals("light")){
            setTheme(R.style.LightTheme);
        }else{
            setTheme(R.style.DarkTheme);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.push_notif_settings_activity);

        getSupportActionBar().setTitle("Notification Settings");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        newFriendReq = findViewById(R.id.newFriendReq);
        newFriendAcc = findViewById(R.id.newFriendAcc);
        newFeedItems = findViewById(R.id.newFeedItems);
        newLikes = findViewById(R.id.newLikes);
        newComments = findViewById(R.id.newComments);
        datePickerButton = findViewById(R.id.datePickerButton);
        cancelReminder = findViewById(R.id.cancelReminder);

        datePickerButton.setOnClickListener(onDatePickerButtonClick);
        cancelReminder.setOnClickListener(onCancelReminderButtonClick);

        newFriendReq.setOnCheckedChangeListener(onCheckedChangeListener);
        newFriendAcc.setOnCheckedChangeListener(onCheckedChangeListener);
        newFeedItems.setOnCheckedChangeListener(onCheckedChangeListener);
        newLikes.setOnCheckedChangeListener(onCheckedChangeListener);
        newComments.setOnCheckedChangeListener(onCheckedChangeListener);

        values.put(newFriendReq, "notify_new_friend_req");
        values.put(newFriendAcc, "notify_new_friend_acc");
        values.put(newFeedItems, "notify_new_feed_item");
        values.put(newLikes, "notify_new_like");
        values.put(newComments, "notify_new_comment");

        if (savedInstanceState == null || !savedInstanceState.containsKey(values.get(newFriendReq))) {
            getSettings();
        } else {

            newFriendReq.setOnCheckedChangeListener(null);
            newFriendAcc.setOnCheckedChangeListener(null);
            newFeedItems.setOnCheckedChangeListener(null);
            newLikes.setOnCheckedChangeListener(null);
            newComments.setOnCheckedChangeListener(null);

            newFriendReq.setChecked(savedInstanceState.getBoolean(values.get(newFriendReq)));
            newFriendAcc.setChecked(savedInstanceState.getBoolean(values.get(newFriendAcc)));
            newFeedItems.setChecked(savedInstanceState.getBoolean(values.get(newFeedItems)));
            newLikes.setChecked(savedInstanceState.getBoolean(values.get(newLikes)));
            newComments.setChecked(savedInstanceState.getBoolean(values.get(newComments)));
            newFriendReq.jumpDrawablesToCurrentState();
            newFriendAcc.jumpDrawablesToCurrentState();
            newFeedItems.jumpDrawablesToCurrentState();
            newLikes.jumpDrawablesToCurrentState();
            newComments.jumpDrawablesToCurrentState();

            newFriendReq.setOnCheckedChangeListener(onCheckedChangeListener);
            newFriendAcc.setOnCheckedChangeListener(onCheckedChangeListener);
            newFeedItems.setOnCheckedChangeListener(onCheckedChangeListener);
            newLikes.setOnCheckedChangeListener(onCheckedChangeListener);
            newComments.setOnCheckedChangeListener(onCheckedChangeListener);

        }

        cancelReminder.setVisibility(View.GONE);

        LocalTime aTime = getTimeFromSharedPref();
        if (aTime != null) {
            setReminderTime(aTime);
        }

        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(themeReceiver, new IntentFilter("theme-updated"));

    }

    private LocalTime getTimeFromSharedPref() {
        SharedPreferences pref = getSharedPreferences("com.gethighlow.SharedPref", Context.MODE_PRIVATE);
        String time = pref.getString("reminderTime", null);
        if (time == null) return null;
        return LocalTime.parse(time, DateTimeFormatter.ofPattern("h:m a"));
    }

    private void setReminderNotif(LocalTime time) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            String channelId = "com.gethighlow.dailyReminder";
            String channelName = "High/Low Daily Reminder";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(channelId, channelName, importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(getColor(R.color.colorPrimary));
            notificationManager.createNotificationChannel(notificationChannel);

            AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent myIntent;
            PendingIntent pendingIntent;


            // SET TIME HERE
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, time.getHour());
            calendar.set(Calendar.MINUTE, time.getMinute());

            myIntent = new Intent(PushNotifSettings.this, AlarmNotificationReceiver.class);
            pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, myIntent, 0);
            manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        }
    }

    private void changeReminderTime(LocalTime localTime) {
        SharedPreferences pref = getSharedPreferences("com.gethighlow.SharedPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("reminderTime", localTime.format(DateTimeFormatter.ofPattern("h:m a")));
        editor.apply();
        setReminderNotif(localTime);
        setReminderTime(localTime);
    }

    private void setReminderTime(LocalTime time) {
        cancelReminder.setVisibility(View.VISIBLE);
        datePickerButton.setText(time.format(DateTimeFormatter.ofPattern("h:m a") ));
        this.currTime = time;
    }

    TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int i, int i1) {
            PushNotifSettings.this.changeReminderTime(LocalTime.of(i, i1));
        }
    };

    View.OnClickListener onCancelReminderButtonClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            PushNotifSettings.this.currTime = LocalTime.now();
            datePickerButton.setText("Not Scheduled");
            cancelReminder.setVisibility(View.GONE);
            SharedPreferences pref = PushNotifSettings.this.getSharedPreferences("com.gethighlow.SharedPref", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.remove("reminderTime");
            editor.apply();
            Intent myIntent = new Intent(PushNotifSettings.this, AlarmNotificationReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(PushNotifSettings.this.getApplicationContext(), 0, myIntent, 0);

            AlarmManager manager = (AlarmManager) PushNotifSettings.this.getSystemService(Context.ALARM_SERVICE);
            manager.cancel(pendingIntent);
        }
    };

    View.OnClickListener onDatePickerButtonClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(PushNotifSettings.this, onTimeSetListener, currTime.getHour(), currTime.getMinute(), false);
                timePickerDialog.show();
            } else {
                alert("Requires newer Android", "Sorry, but the daily reminder feature is only available on Android Oreo or newer");
            }
        }
    };

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean(values.get(newFriendReq), newFriendReq.isChecked());
        outState.putBoolean(values.get(newFriendAcc), newFriendAcc.isChecked());
        outState.putBoolean(values.get(newFeedItems), newFeedItems.isChecked());
        outState.putBoolean(values.get(newLikes), newLikes.isChecked());
        outState.putBoolean(values.get(newComments), newComments.isChecked());
    }

    private void alert(String title, String message) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this, R.style.AlertDialogCustom);
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setCancelable(true);
        alertDialog.setPositiveButton("OK", null);
        alertDialog.show();
    }

    private CompoundButton.OnCheckedChangeListener onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(final CompoundButton buttonView, boolean isChecked) {
            String value = values.get(buttonView);
            if (isChecked) {
                NotificationsService.shared().turnOn(value, new Consumer<GenericResponse>() {
                    @Override
                    public void accept(GenericResponse genericResponse) {
                    }
                }, new Consumer<String>() {
                    @Override
                    public void accept(String error) {
                        buttonView.setChecked(!buttonView.isChecked());
                    }
                });
            } else {
                NotificationsService.shared().turnOff(value, new Consumer<GenericResponse>() {
                    @Override
                    public void accept(GenericResponse genericResponse) {
                    }
                }, new Consumer<String>() {
                    @Override
                    public void accept(String error) {
                        PushNotifSettings.this.alert(getResources().getString(R.string.an_error_occurred), getResources().getString(R.string.please_try_again));
                        buttonView.setChecked(!buttonView.isChecked());
                    }
                });
            }
        }
    };

    private void getSettings() {
        NotificationsService.shared().getNotificationSettings(new Consumer<NotificationsSettingsResponse>() {
            @Override
            public void accept(NotificationsSettingsResponse notificationsSettingsResponse) {

                newFriendReq.setOnCheckedChangeListener(null);
                newFriendAcc.setOnCheckedChangeListener(null);
                newFeedItems.setOnCheckedChangeListener(null);
                newLikes.setOnCheckedChangeListener(null);
                newComments.setOnCheckedChangeListener(null);

                newFriendReq.setChecked(notificationsSettingsResponse.getNotify_new_friend_req());
                newFriendAcc.setChecked(notificationsSettingsResponse.getNotify_new_friend_acc());
                newFeedItems.setChecked(notificationsSettingsResponse.getNotify_new_feed_item());
                newLikes.setChecked(notificationsSettingsResponse.getNotify_new_like());
                newComments.setChecked(notificationsSettingsResponse.getNotify_new_comment());
                newFriendReq.jumpDrawablesToCurrentState();
                newFriendAcc.jumpDrawablesToCurrentState();
                newFeedItems.jumpDrawablesToCurrentState();
                newLikes.jumpDrawablesToCurrentState();
                newComments.jumpDrawablesToCurrentState();

                newFriendReq.setOnCheckedChangeListener(onCheckedChangeListener);
                newFriendAcc.setOnCheckedChangeListener(onCheckedChangeListener);
                newFeedItems.setOnCheckedChangeListener(onCheckedChangeListener);
                newLikes.setOnCheckedChangeListener(onCheckedChangeListener);
                newComments.setOnCheckedChangeListener(onCheckedChangeListener);

            }
        }, new Consumer<String>() {
            @Override
            public void accept(String error) {
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private BroadcastReceiver themeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String currentTheme = SetActivityTheme.getTheme(context);
            if(currentTheme.equals("light")) {
                setTheme(R.style.LightTheme);
            } else if(currentTheme.equals("dark")){
                setTheme(R.style.DarkTheme);
            }

        }
    };

}

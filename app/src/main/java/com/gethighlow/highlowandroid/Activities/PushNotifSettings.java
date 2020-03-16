package com.gethighlow.highlowandroid.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.gethighlow.highlowandroid.R;
import com.gethighlow.highlowandroid.model.Services.NotificationsService;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class PushNotifSettings extends AppCompatActivity {

    private SwitchCompat newFriendReq, newFriendAcc, newFeedItems, newLikes, newComments;
    private Button datePickerButton, cancelReminder;
    private Map<SwitchCompat, String> values = new HashMap<>();
    private LocalTime currTime = LocalTime.now();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
    }

    private LocalTime getTimeFromSharedPref() {
        SharedPreferences pref = getSharedPreferences("com.gethighlow.SharedPref", Context.MODE_PRIVATE);
        String time = pref.getString("reminderTime", null);
        if (time == null) return null;
        return LocalTime.parse(time, DateTimeFormatter.ofPattern("h:m a"));
    }

    private void setReminderNotif(LocalTime time) {

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
        Calendar calendar= Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,time.getHour());
        calendar.set(Calendar.MINUTE,time.getMinute());

        myIntent = new Intent(PushNotifSettings.this, AlarmNotificationReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),0,myIntent,0);
        manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY,pendingIntent);

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

    TimePickerDialog.OnTimeSetListener onTimeSetListener = (timePicker, i, i1) -> {
        changeReminderTime(LocalTime.of(i, i1));
    };

    View.OnClickListener onCancelReminderButtonClick = view -> {
        this.currTime = LocalTime.now();
        datePickerButton.setText("Not Scheduled");
        cancelReminder.setVisibility(View.GONE);
        SharedPreferences pref = getSharedPreferences("com.gethighlow.SharedPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.remove("reminderTime");
        editor.apply();
        Intent myIntent = new Intent(PushNotifSettings.this, AlarmNotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),0,myIntent,0);

        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        manager.cancel(pendingIntent);
    };

    View.OnClickListener onDatePickerButtonClick = view -> {
        Log.w("Debug", "CLICKED");
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, onTimeSetListener, currTime.getHour(), currTime.getMinute(), false);
        timePickerDialog.show();
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
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setCancelable(true);
        alertDialog.setPositiveButton("OK", null);
        alertDialog.show();
    }

    private CompoundButton.OnCheckedChangeListener onCheckedChangeListener = (buttonView, isChecked) -> {
        String value = values.get(buttonView);
        if (isChecked) {
            NotificationsService.shared().turnOn(value, genericResponse -> {
            }, error -> {
                buttonView.setChecked(!buttonView.isChecked());
            });
        } else {
            NotificationsService.shared().turnOff(value, genericResponse -> {
            }, error -> {
                alert("An error occurred", "Please try again");
                buttonView.setChecked(!buttonView.isChecked());
            });
        }
    };

    private void getSettings() {
        NotificationsService.shared().getNotificationSettings(notificationsSettingsResponse -> {

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

        }, error -> {
            Log.w("Debug", error);
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
}

package com.gethighlow.highlowandroid.Activities.Tabs.Diary;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.gethighlow.highlowandroid.R;

import java.io.IOException;
import java.util.Objects;

public class AudioEntryActivity extends AppCompatActivity {

    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private static String fileName = null;

    private MediaRecorder recorder = null;
    private MediaPlayer player = null;

    private boolean permissionToRecordAccepted = false;
    private String[] permissions = {Manifest.permission.RECORD_AUDIO};

    private boolean isRecording = false;
    private boolean isPlaying = false;

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_RECORD_AUDIO_PERMISSION:
                permissionToRecordAccepted  = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permissionToRecordAccepted ) finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_audio_entry);

        //Show the back button
        Objects.requireNonNull( getSupportActionBar() ).setDisplayHomeAsUpEnabled(true);

    }

    private View.OnClickListener onRecordButtonClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            if (!isRecording) {

                recorder = new MediaRecorder();

                //Set the file name
                fileName = getExternalCacheDir().getAbsolutePath();
                fileName += "/highlowAudioRecording.m4a";

                recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
                recorder.setOutputFile(fileName);
                recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

                try {
                    recorder.prepare();
                } catch(IOException e) {
                    Log.e("Audio", "Prepare failed");
                    isRecording = true;
                }

                recorder.start();

            } else {
                recorder.stop();
                recorder.release();
                recorder = null;
            }

            isRecording = !isRecording;

        }
    };

    private View.OnClickListener onPlayButtonClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            if (!isPlaying) {

                player = new MediaPlayer();

                try {
                    player.setDataSource(fileName);
                    player.prepare();
                    player.start();
                } catch(IOException e) {
                    Log.e("Audio", "Prepare failed");
                    isPlaying = true;
                }

            } else {
                player.stop();
                player.release();
                player = null;
            }

            isPlaying = !isPlaying;

        }
    };

    @Override
    public void onStop() {
        super.onStop();

        if (recorder != null) {
            recorder.release();
            recorder = null;
        }

        if (player != null) {
            player.release();
            player = null;
        }

    }
}
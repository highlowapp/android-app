package com.gethighlow.highlowandroid.model.Managers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.gethighlow.highlowandroid.model.util.Consumer;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

    private Consumer<Bitmap> onSuccess;

    public DownloadImageTask(Consumer<Bitmap> onSuccess) {
        this.onSuccess = onSuccess;
    }

    protected Bitmap doInBackground(String... src) {
        try {
            URL url = new URL(src[0]);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            connection.disconnect();

            return myBitmap;
        } catch (Exception e) {
            // Log exception
            return null;
        }
    }

    protected void onPostExecute(Bitmap result) {
        onSuccess.accept(result);
    }
}

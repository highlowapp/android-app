package com.gethighlow.highlowandroid.model.Managers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.function.Consumer;

public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

    private Consumer<Bitmap> onSuccess;

    public DownloadImageTask(Consumer<Bitmap> onSuccess) {
        this.onSuccess = onSuccess;
    }

    protected Bitmap doInBackground(String... src) {
        try {
            URL url = new URL(src[0]);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }

    protected void onPostExecute(Bitmap result) {
        onSuccess.accept(result);
    }
}

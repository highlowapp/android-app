package com.gethighlow.highlowandroid.CustomViews.Other;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MenuItem;
import com.gethighlow.highlowandroid.R;
import java.io.FileNotFoundException;
import androidx.appcompat.app.AppCompatActivity;

public  class ExpandLowImage extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Low Image");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        try {
            setup(getApplicationContext());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }



    private void setup(Context context) throws FileNotFoundException {



        //Retrieve the filename of the image saved in HighLowView.java
        String filename = getIntent().getStringExtra("lowImageFilename");
        //Decode the image file into a bitmap
        Bitmap img = BitmapFactory.decodeStream(context.openFileInput(filename));

        ZoomActivity image = new ZoomActivity(context);
        image.setImageBitmap(img);
        image.setMaxZoom(4f);
        image.setBackgroundColor(getResources().getColor(R.color.black));
        setContentView(image);


        //Make sure you delete the file so that the app doesn't get bogged down with useless images
        this.deleteFile(filename);


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


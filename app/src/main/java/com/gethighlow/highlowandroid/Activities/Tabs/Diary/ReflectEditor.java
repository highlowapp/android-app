package com.gethighlow.highlowandroid.Activities.Tabs.Diary;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;


import com.gethighlow.highlowandroid.R;
import com.gethighlow.highlowandroid.model.Managers.LiveDataModels.ActivityLiveData;
import com.gethighlow.highlowandroid.model.Resources.Activity;
import com.gethighlow.highlowandroid.model.Services.ActivityService;
import com.gethighlow.highlowandroid.model.util.Consumer;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.Locale;


import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.lifecycle.Observer;

public class ReflectEditor extends AppCompatActivity {

    private ActivityLiveData activityLiveData;
    private Activity activity = null;
    private WebView reflectEditorWebview;
    private Boolean hasEdited = false;
    private String filePath;
    private LocalDate localDate;

    private int GALLERY_REQUEST_CODE = 80;
    private int CAMERA_REQUEST_CODE = 82;
    private int READ_STORAGE_REQUEST = 81;
    private int WRITE_STORAGE_REQUEST = 83;

    private Observer<Activity> onActivityUpdate = new Observer<Activity>() {
        @Override
        public void onChanged(Activity activity) {

            //Update the webview with the activity

        }
    };

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);

        //Create the reflect editor webview
        this.reflectEditorWebview = new WebView(this.getApplicationContext());

        //Set javascript enabled for the webview settings
        WebSettings webSettings = reflectEditorWebview.getSettings();
        webSettings.setJavaScriptEnabled(true);

        //Load the file url
        reflectEditorWebview.loadUrl("file:///android_asset/Reflect_Editor/index.html");


        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.reflect_editor_navigation);

        /*Toolbar toolbar = new Toolbar(this);
        toolbar.setMinimumHeight(45);
        toolbar.
        setSupportActionBar(toolbar);*/
        setContentView(reflectEditorWebview);

        //Set ourselves as the webview client
        reflectEditorWebview.setWebViewClient(new WebViewClient() {


            @Override
            public void onPageFinished(WebView view, String url) {

                //Unless it was a javascript url we finished loading...
                if(!url.startsWith("javascript")){

                    //Get the type from the intent
                    Intent intent = getIntent();
                    String type = intent.getStringExtra("type");

                    //If the type is 'highlow'...
                    if(type.equals("highlow")){

                        //Set the type in the editor
                        ReflectEditor.this.setType("highlow");
                    }

                    //If the type is 'diary'...
                    else if(type.equals("diary")){

                        //Set that type in the editor
                        ReflectEditor.this.setType("diary");
                    }
                }

            }
        });

        //Create a javascript interface
        WebAppInterface webAppInterface = new WebAppInterface(this);

        //Add the javascript interface so the app can communicate
        reflectEditorWebview.addJavascriptInterface(webAppInterface, "Android");


        try {

            //Create the JSONArray with the template data
            //TODO: Change the template data based on the type of activity being created
            JSONArray data = new JSONArray("[{'type':'h1','content':'High','editable':false},{'type':'img'},{'type':'p','content':'','editable':true},{'type':'h1','content':'Low','editable':false},{'type':'img'},{'type':'p','content':'','editable':true}]");

            //Finally, run the 'save' method on the interface once to create an activity
            webAppInterface.saveActivity(data);

        } catch (JSONException e) {

            //TODO: Create an alert here
            e.printStackTrace();

        }


    }

    //The Web App Interface
    public class WebAppInterface {
        Context mContext;

        /** Instantiate the interface and set the context */
        WebAppInterface(Context c) {
            mContext = c;
        }


        @JavascriptInterface
        public void saveActivity(JSONArray blocks) {
            try {

                //Create a new JSONObject with the blocks
                JSONObject data = new JSONObject();
                data.put("blocks", blocks);

                //If we have an activityLiveData to work with...
                if (activityLiveData != null) {

                    //Update that activity with our new data
                    activityLiveData.update(data, new Consumer<Activity>() {
                        @Override
                        public void accept(Activity activity) {

                            //We don't really need to do much here, since the LiveData will automatically update everything else

                        }
                    }, new Consumer<String>() {
                        @Override
                        public void accept(String s) {

                            //If an error occurred, show an alert
                            ReflectEditor.this.alert(getResources().getString(R.string.an_error_occurred), getResources().getString(R.string.please_try_again));

                        }
                    });

                } else {

                    //Set the activity title
                    data.put("title", "Untitled Entry");

                    //Get the current date in the correct format
                    String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

                    //Create an activity using the new data and the current date
                    ActivityService.shared().createActivity(data, "highlow", currentDate, new Consumer<ActivityLiveData>() {
                        @Override
                        public void accept(ActivityLiveData activity) {

                            Log.d("Activity", activity.toString());

                            //Set the liveData
                            ReflectEditor.this.activityLiveData = activity;

                            //Observe the liveData
                            ReflectEditor.this.activityLiveData.observe(ReflectEditor.this, ReflectEditor.this.onActivityUpdate);

                            //For debugging purposes; log the activityId of the newly created activity
                            String activityId = activity.getActivityId();

                        }
                    }, new Consumer<String>() {
                        @Override
                        public void accept(String error) {

                            //If there was an error, show an alert
                            ReflectEditor.this.alert(getResources().getString(R.string.an_error_occurred), getResources().getString(R.string.please_try_again));

                        }
                    });
                }
            } catch (JSONException e){

                //If there was an error, show an alert
                ReflectEditor.this.alert(getResources().getString(R.string.an_error_occurred), getResources().getString(R.string.please_try_again));

            }

        }


        @JavascriptInterface
        public void hasEdited(){
            hasEdited = true;
            /*LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(receiver, new IntentFilter("has-edited"));*/
        }

        @JavascriptInterface
        public void showPremium(){
            //TODO create ShowPremium.class
            /*Intent intent = new Intent(getApplicationContext(), ShowPremium.class);
            getApplicationContext().startActivity(intent);*/

        }

        @JavascriptInterface
        public void chooseImage(JSONArray blockIdArray) throws JSONException {
            try {
                JSONObject blockIdObject = new JSONObject();
                blockIdObject.put("blockIdObject", blockIdArray);
                String blockId = blockIdObject.toString();
                pickImage();
                String url = activity.getActivityImage();
                JSONObject jsonObject = new JSONObject(url);
                JSONArray imgBlock = jsonObject.getJSONArray("url");
                updateBlocks(blockId, imgBlock);
            } catch (JSONException e) {}
        }

    }



    public void setBlocks(JSONArray blocks){
        /*JSONArray blocks = new JSONArray();
        blocks.put(activity);*/
        ReflectEditor.this.reflectEditorWebview.loadUrl("javascript:document.setBlocks(" + blocks + ")");
    }

    public void updateBlocks(String blockId, JSONArray blocks){
        ReflectEditor.this.reflectEditorWebview.loadUrl("javascript:document.updateBlocks(" + blockId + "," + blocks + ")");
    }

    public void createH1Block(){
        ReflectEditor.this.reflectEditorWebview.loadUrl("javascript:document.createH1Block()");
    }

    public void createH2Block(){
        ReflectEditor.this.reflectEditorWebview.loadUrl("javascript:document.createH2Block()");
    }

    public void createImageBlock(){
        ReflectEditor.this.reflectEditorWebview.loadUrl("javascript:document.createImageBlock()");
    }

    public void createQuoteBlock(){
        ReflectEditor.this.reflectEditorWebview.loadUrl("javascript:document.createQuoteBlock()");
    }

    public void enablePremiumFeatures(){

        ReflectEditor.this.reflectEditorWebview.loadUrl("javascript:document.enablePremiumFeatures()");
    }

    public void setType(String type){

        /*WebView reflectEditorWebview = new WebView(this.getApplicationContext());*/
        /*WebSettings webSettings = reflectEditorWebview.getSettings();
        webSettings.setJavaScriptEnabled(true);*/

        /*reflectEditorWebview.loadUrl("file:///android_asset/Reflect_Editor/index.html");*/

        reflectEditorWebview.loadUrl("javascript:setType('" + type + "')");
        setContentView(reflectEditorWebview);
    }

    private void getActivity(){
        if (activityLiveData != null) {

            String activityId = activityLiveData.getActivityId();


            ActivityService.shared().getActivity(activityId, new Consumer<Activity>() {
                @Override
                public void accept(Activity activity) {

                    ReflectEditor.this.activity = activity;

                }
            }, new Consumer<String>() {
                @Override
                public void accept(String error) {
                    ReflectEditor.this.alert(getResources().getString(R.string.an_error_occurred), getResources().getString(R.string.please_try_again));

                }
            });
        } else {
            return;
        }

    }



    public void pickImage(){
        AlertDialog.Builder builder = new AlertDialog.Builder(ReflectEditor.this, R.style.AlertDialogCustom);
        builder.setTitle("How would you like to upload an image?");
        final String[] items = {"Take a photo", "Use existing photo", "Cancel"};
        final Context context = ReflectEditor.this;
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (items[i].equals("Use existing photo")) {
                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        gallery();
                    } else {
                        ActivityCompat.requestPermissions(ReflectEditor.this,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                READ_STORAGE_REQUEST);

                    }
                } else if (items[i].equals("Take a photo")) {
                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        camera();
                    } else {
                        ActivityCompat.requestPermissions(ReflectEditor.this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                WRITE_STORAGE_REQUEST);
                    }
                }
            }
        });

        builder.create().show();
    }

    private void gallery() {
        Intent picker = new Intent(Intent.ACTION_PICK);
        picker.setType("image/*");
        String[] mimeTypes = {"image/jpeg", "image/png"};
        picker.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        startActivityForResult(picker, GALLERY_REQUEST_CODE);
    }

    private void camera() {
        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        File photo = null;
        try {
            photo = createImageFile();

        } catch(IOException e) {
            alert(getResources().getString(R.string.an_error_occurred), getResources().getString(R.string.please_try_again));
        }

        if (photo != null) {
            Uri photoUri = FileProvider.getUriForFile(this, "com.gethighlow.highlowandroid.fileprovider", photo);
            camera.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            startActivityForResult(camera, CAMERA_REQUEST_CODE);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == READ_STORAGE_REQUEST) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                gallery();
            } else {
                alert("Sorry", "We could not complete your request");
            }
        } else if (requestCode == WRITE_STORAGE_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                camera();
            } else {
                alert("Sorry", "We could not complete your request");
            }
        }

    }

    private File createImageFile() throws IOException {
        String name = "FROM_CAMERA";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                name,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        filePath = image.getAbsolutePath();
        return image;
    }





    private void alert(String title, String message) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getApplicationContext(), R.style.AlertDialogCustom);
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setCancelable(true);
        alertDialog.setPositiveButton("OK", null);
        alertDialog.show();
    }

}

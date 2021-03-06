package com.gethighlow.highlowandroid.Activities.Tabs.Diary;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import com.gethighlow.highlowandroid.Activities.Other.UpsellActivity;
import com.gethighlow.highlowandroid.CustomViews.Other.ProgressLoaderView;
import com.gethighlow.highlowandroid.R;
import com.gethighlow.highlowandroid.model.Managers.ActivityManager;
import com.gethighlow.highlowandroid.model.Managers.LiveDataModels.ActivityLiveData;
import com.gethighlow.highlowandroid.model.Resources.Activity;
import com.gethighlow.highlowandroid.model.Services.ActivityService;
import com.gethighlow.highlowandroid.model.util.Consumer;
import com.gethighlow.highlowandroid.model.util.PremiumStatusListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.revenuecat.purchases.EntitlementInfo;
import com.revenuecat.purchases.PurchaserInfo;
import com.revenuecat.purchases.Purchases;
import com.revenuecat.purchases.interfaces.UpdatedPurchaserInfoListener;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
    private WebAppInterface webAppInterface;
    private ProgressLoaderView progressLoaderView;

    //Toolbar
    private Toolbar formattingToolbar;

    //Toolbar items
    private ImageView bold;
    private ImageView italics;
    private ImageView underline;
    private ImageView strikethrough;
    private ImageView header1;
    private ImageView header2;
    private ImageView image;
    private ImageView quote;
    private CircularProgressIndicator savingIndicator;
    private TextView errorMessage;

    private int GALLERY_REQUEST_CODE = 80;
    private int CAMERA_REQUEST_CODE = 82;
    private int READ_STORAGE_REQUEST = 81;
    private int WRITE_STORAGE_REQUEST = 83;

    private String type = "diary";

    private String currentImageBlockId; //This is used to keep track of an image block that the user chooses to upload to

    private Observer<Activity> onActivityUpdate = new Observer<Activity>() {
        @Override
        public void onChanged(Activity activity) {

            //Update the webview with the activity

        }
    };

    private Consumer<PurchaserInfo> onPremiumStatusChanged = new Consumer<PurchaserInfo>() {
        @Override
        public void accept(PurchaserInfo purchaserInfo) {

            //Get the premium entitlement info
            EntitlementInfo premiumEntitlement = purchaserInfo.getEntitlements().get("Premium");

            Log.w("Debug", "Premium status changed");

            //If they have premium...
            if (premiumEntitlement != null && premiumEntitlement.isActive()) {

                Log.w("Debug", "Enabling Premium Features");

                //Enable the premium features
                enablePremiumFeatures();

            }

        }
    };

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.reflect_editor_webview);

        //Show the back button
        Objects.requireNonNull( getSupportActionBar() ).setDisplayHomeAsUpEnabled(true);

        //Create the reflect editor webview
        this.reflectEditorWebview = findViewById(R.id.reflect_editor_webview);

        //Get the formatting toolbar and its items
        formattingToolbar = findViewById(R.id.formattingToolbar);
        bold = findViewById(R.id.bold);
        italics = findViewById(R.id.italics);
        underline = findViewById(R.id.underline);
        strikethrough = findViewById(R.id.strikethrough);
        header1 = findViewById(R.id.header1);
        header2 = findViewById(R.id.header2);
        image = findViewById(R.id.image);
        quote = findViewById(R.id.quote);
        savingIndicator = findViewById(R.id.savingIndicator);
        errorMessage = findViewById(R.id.errorMessage);

        //Set the item onClick listener
        bold.setOnClickListener(onFormattingOptionTapped);
        italics.setOnClickListener(onFormattingOptionTapped);
        underline.setOnClickListener(onFormattingOptionTapped);
        strikethrough.setOnClickListener(onFormattingOptionTapped);
        header1.setOnClickListener(onFormattingOptionTapped);
        header2.setOnClickListener(onFormattingOptionTapped);
        image.setOnClickListener(onFormattingOptionTapped);
        quote.setOnClickListener(onFormattingOptionTapped);

        //Hide the indicator
        hideIndicator();

        //Set the progress indicator color
        savingIndicator.setIndicatorColor( getResources().getColor(R.color.colorPrimary) );

        //Set the progress indicator to indeterminate
        savingIndicator.setIndeterminate(true);

        //Set javascript enabled for the webview settings
        WebSettings webSettings = reflectEditorWebview.getSettings();
        webSettings.setJavaScriptEnabled(true);

        //Load the file url
        reflectEditorWebview.loadUrl("file:///android_asset/Reflect/index.html");

        //Create a loader view
        progressLoaderView = (ProgressLoaderView) findViewById(R.id.progressLoaderView);

        //Set the loader view container
        progressLoaderView.setView(reflectEditorWebview);

        //Set debugging enabled
        WebView.setWebContentsDebuggingEnabled(true);

        //Set ourselves as the webview client
        reflectEditorWebview.setWebViewClient(new WebViewClient() {


            @Override
            public void onPageFinished(WebView view, String url) {

                Log.w("Info", url);

                //Unless it was a javascript url we finished loading...
                if (!url.startsWith("javascript")) {

                    //Call our onLoad function
                    onLoad(url);

                } else {

                }

            }
        });

        //Create a javascript interface
        webAppInterface = new WebAppInterface(this);

        //Add the javascript interface so the app can communicate
        reflectEditorWebview.addJavascriptInterface(webAppInterface, "Android");

        //Listen for the keyboard opening
        View rootView = findViewById(R.id.rootView);
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                //Get the height difference
                int heightDiff = rootView.getRootView().getHeight() - rootView.getHeight();

                //If the difference is more than 200dp...
                if (heightDiff > dpToPx(ReflectEditor.this, 200)) {

                    //The keyboard is on, so we display the toolbar
                    formattingToolbar.setVisibility(View.VISIBLE);

                } else {

                    //Otherwise, hide the toolbar
                    formattingToolbar.setVisibility(View.GONE);

                }

            }
        });

    }



    private void showIndicator() {

        //Hide the error message
        errorMessage.setVisibility(View.GONE);

        //Show the indicator
        savingIndicator.setVisibility(View.VISIBLE);

    }

    private void hideIndicator() {

        //Hide the indicator
        savingIndicator.setVisibility(View.GONE);

        //Hide the error message
        errorMessage.setVisibility(View.GONE);

    }

    private void showErrorMessage() {

        //Show the error message
        errorMessage.setVisibility(View.VISIBLE);

        //Hide the indicator
        savingIndicator.setVisibility(View.GONE);

    }



    public static float dpToPx(Context context, float valueInDp) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInDp, metrics);
    }

    private View.OnClickListener onFormattingOptionTapped = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            Log.w("Debug", "Called");

            //Run a command depending on which item was pressed
            if (view == bold) runJSCommand("document.execCommand('bold');"); //For some reason, bold doesn't seem to be working - it seems to be a webview bug
            else if (view == italics) runJSCommand("document.execCommand('italic');");
            else if (view == underline) runJSCommand("document.execCommand('underline');");
            else if (view == strikethrough) runJSCommand("document.execCommand('strikethrough');");
            else if (view == header1) createH1Block();
            else if (view == header2) createH2Block();
            else if (view == image) createImageBlock();
            else if (view == quote) createQuoteBlock();

        }
    };

    private void runJSCommand(String command) {

        //Run the command in the webview
        reflectEditorWebview.loadUrl("javascript:" + command);

    }

    //Everything that happens to set up the webview after it has been loaded
    private void onLoad(String url) {

        //Get the type from the intent
        Intent intent = getIntent();
        String type = intent.getStringExtra("type");

        //Set the type
        this.type = type;

        //If the type is 'highlow'...
        if (type.equals("highlow")) {

            //Set the type in the editor
            ReflectEditor.this.setType("highlow");
        }

        //If the type is 'diary'...
        else if (type.equals("diary")) {

            //Set that type in the editor
            //ReflectEditor.this.setType("diary");
        }

        //Get the activityId
        String activityId = intent.getStringExtra("activityId");

        //If the activityId is null, create an activity
        if (activityId == null) {

            //If the type is null, show an error
            if (type == null) {
                alert(getString(R.string.an_error_occurred), getString(R.string.please_try_again));
                return;
            }

            //Otherwise, create the activity with the type
            createActivity(type);
        }

        //If the activityId is not null, get the corresponding activity and load it in the webview
        else {
            ActivityManager.shared().getActivity(activityId, new Consumer<ActivityLiveData>() {
                @Override
                public void accept(ActivityLiveData activityLiveData) {

                    //Set the new activityLiveData
                    ReflectEditor.this.activityLiveData = activityLiveData;

                    //Load the new data into the webView
                    ReflectEditor.this.setBlocks( activityLiveData.getData() );

                }
            }, new Consumer<String>() {
                @Override
                public void accept(String s) {

                    //Show an alert
                    ReflectEditor.this.alert(getString(R.string.an_error_occurred), getString(R.string.please_try_again));

                }
            });
        }

        //Set our premium listener
        PremiumStatusListener.shared().addSubscriber(onPremiumStatusChanged);

    }

    private void setBlocks(JsonObject data) {

        //Get the blocks as a JsonObject
        JsonArray blocks = data.getAsJsonArray("blocks");

        //Convert the JsonObject to a string
        String blocksString = blocks.toString();

        //Now, load those blocks into the webview
        reflectEditorWebview.loadUrl("javascript:setBlocks(" + blocksString + ");");

    }

    //The block templates for various entry types
    private Map<String, String> templates = new HashMap<String, String>() {{
        put("highlow", "[{'type':'h1','content':'High','editable':false},{'type':'img'},{'type':'p','content':'','editable':true},{'type':'h1','content':'Low','editable':false},{'type':'img'},{'type':'p','content':'','editable':true}]");
        put("diary", "[{'type':'h1', 'content': '', 'editable': true}]");
    }};

    //For creating activities
    public void createActivity(String type) {

            //Get the template string
            String template = templates.get(type);

            //Finally, run the 'save' method on the interface once to create an activity
            webAppInterface.saveActivity(template);

    }

    //The Web App Interface
    public class WebAppInterface {
        Context mContext;

        /** Instantiate the interface and set the context */
        WebAppInterface(Context c) {
            mContext = c;
        }


        @JavascriptInterface
        public void saveActivity(String blocks) {

            try {

                //Show the indicator
               reflectEditorWebview.post(new Runnable() {
                   @Override
                   public void run() {
                       showIndicator();
                   }
               });


                //Get the blocks as a JSONArray
                JSONArray blocksArr = new JSONArray(blocks);

                //Create a new JSONObject with the blocks
                JSONObject data = new JSONObject();
                data.put("blocks", blocksArr);

                //If we have an activityLiveData to work with...
                if (activityLiveData != null) {

                    //Get the title
                    String title = Activity.getTitleForActivity( activityLiveData.getValue() );

                    //Add the title to the data
                    data.put("title", title);

                    //Update that activity with our new data
                    activityLiveData.update(data, new Consumer<Activity>() {
                        @Override
                        public void accept(Activity activity) {

                            Log.w("Debug", "YES");

                            //Hide the saving indicator
                            hideIndicator();

                        }
                    }, new Consumer<String>() {
                        @Override
                        public void accept(String s) {

                            //If an error occurred, show the error message
                            showErrorMessage();

                        }
                    });

                } else {

                    //Set the activity title
                    data.put("title", "Untitled Entry");

                    //Get the current date in the correct format
                    String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

                    //Create an activity using the new data and the current date
                    ActivityService.shared().createActivity(data, ReflectEditor.this.type, currentDate, new Consumer<ActivityLiveData>() {
                        @Override
                        public void accept(ActivityLiveData activity) {

                            //Set the liveData
                            ReflectEditor.this.activityLiveData = activity;

                            //Set the blocks
                            ReflectEditor.this.setBlocks( activity.getData() );

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

            Log.w("Debug", "WORKING");
            Intent intent = new Intent(getApplicationContext(), UpsellActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getApplicationContext().startActivity(intent);

        }

        @JavascriptInterface
        public void chooseImage(String blockId) throws JSONException {

            //Update our currentBlockId
            currentImageBlockId = blockId;

            //Open the image picker
            pickImage();

        }

    }

    public void updateBlocks(String blockId, JSONArray blocks){
        ReflectEditor.this.reflectEditorWebview.loadUrl("javascript:document.updateBlocks(" + blockId + "," + blocks + ")");
    }

    public void createH1Block(){
        ReflectEditor.this.reflectEditorWebview.loadUrl("javascript:createH1Block()");
    }

    public void createH2Block(){
        ReflectEditor.this.reflectEditorWebview.loadUrl("javascript:createH2Block()");
    }

    public void createImageBlock(){
        ReflectEditor.this.reflectEditorWebview.loadUrl("javascript:createImageBlock()");
    }

    public void createQuoteBlock(){
        ReflectEditor.this.reflectEditorWebview.loadUrl("javascript:createQuoteBlock()");
    }

    public void enablePremiumFeatures(){

        ReflectEditor.this.reflectEditorWebview.loadUrl("javascript:enablePremiumFeatures()");

    }

    public void setType(String type){

        //Run the JS function to set the type
        reflectEditorWebview.loadUrl("javascript:setType('" + type + "')");

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

        //Create the alert dialog builder
        AlertDialog.Builder builder = new AlertDialog.Builder(ReflectEditor.this, R.style.AlertDialogCustom);

        //Set the title
        builder.setTitle("How would you like to upload an image?");

        //List the options
        final String[] items = {"Take a photo", "Use existing photo", "Enter Image URL", "Cancel"};
        final Context context = ReflectEditor.this;

        //Set the items
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                //If they chose to use an existing photo...
                if (items[i].equals("Use existing photo")) {

                    //If we have the permissions to do so...
                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                        //Show the gallery
                        gallery();

                    }

                    //Otherwise...
                    else {

                        //Request the permissions
                        ActivityCompat.requestPermissions(ReflectEditor.this,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                READ_STORAGE_REQUEST);

                    }

                }

                //If they chose to take a photo...
                else if (items[i].equals("Take a photo")) {

                    //If we have the permissions to do so...
                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                        //Open the camera
                        camera();

                    }

                    //Otherwise...
                    else {

                        //Request the permissions
                        ActivityCompat.requestPermissions(ReflectEditor.this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                WRITE_STORAGE_REQUEST);

                    }

                }

                //If they chose to use a URL
                else if (items[i].equals("Enter Image URL")) {

                    //Present a new dialog to choose a URL
                    setImageWithUrl();

                }
            }
        });

        //Show the dialog
        builder.create().show();
    }

    private void gallery() {

        //Create a picker intent
        Intent picker = new Intent(Intent.ACTION_PICK);

        //Set the type
        picker.setType("image/*");

        //Set the allowed MIME types
        String[] mimeTypes = {"image/jpeg", "image/png"};
        picker.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);

        //Start an activity for that result
        startActivityForResult(picker, GALLERY_REQUEST_CODE);

    }

    private void camera() {

        //Create a camera intent
        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        //Create a photo file
        File photo = null;
        try {
            photo = createImageFile();

        } catch(IOException e) {
            alert(getResources().getString(R.string.an_error_occurred), getResources().getString(R.string.please_try_again));
        }

        //If the file was successfully created...
        if (photo != null) {

            //Get the URI for the foto
            Uri photoUri = FileProvider.getUriForFile(this, "com.gethighlow.highlowandroid.fileprovider", photo);

            //Set that URI as the camera's output
            camera.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);

            //Start an activity for that result
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


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    private void alert(String title, String message) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getApplicationContext(), R.style.AlertDialogCustom);
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setCancelable(true);
        alertDialog.setPositiveButton("OK", null);
        alertDialog.show();
    }


    private void setImageWithUrl() {

        //Create an AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //Set the title
        builder.setTitle("Enter a URL below");

        //Create an input
        final EditText input = new EditText(this);

        //Set the input type
        input.setInputType(InputType.TYPE_TEXT_VARIATION_URI);

        //Set the dialog view
        builder.setView(input);

        //Create an OK button
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                //Get the url
                String url = input.getText().toString();

                //Send the url to the image block
                reflectEditorWebview.post(new Runnable() {
                    @Override
                    public void run() {
                        reflectEditorWebview.loadUrl("javascript:updateBlock('" + currentImageBlockId + "', { url: '" + url + "' });");
                    }
                });

            }
        });

        //Create a cancel button
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                //Cancel the dialog
                dialogInterface.cancel();

            }
        });

        //Show the alert
        builder.create().show();

    }


    private void uploadActivityImageBitmap(Bitmap bitmap) {

        //Start loading
        progressLoaderView.startLoading();

        //Set the title
        progressLoaderView.setTitle("Uploading...");

        //Make a request
        ActivityService.shared().uploadImage(bitmap, new Consumer<String>() {
            @Override
            public void accept(String url) {

                //Stop showing the loading screen
                progressLoaderView.stopLoading();

                //Now that we have the url, we can update the image block
                reflectEditorWebview.loadUrl("javascript:updateBlock('" + currentImageBlockId + "', { url: '" + url + "' });");

            }
        }, new Consumer<String>() {
            @Override
            public void accept(String s) {

                //Stop loading
                progressLoaderView.stopLoading();

                //Alert the user of the error
                alert(getResources().getString(R.string.an_error_occurred), getResources().getString(R.string.please_try_again));

            }
        });

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Check that we got an "OK" result
        if (resultCode == android.app.Activity.RESULT_OK) {

            //If we just received an image from the gallery...
            if (requestCode == GALLERY_REQUEST_CODE) {

                try {

                    //Get the URI of the image
                    Uri selectedImage = data.getData();

                    //Create a file descriptor
                    ParcelFileDescriptor parcelFileDescriptor = getContentResolver().openFileDescriptor(selectedImage, "r", null);

                    //Create an input stream for the file
                    FileInputStream inputStream = new FileInputStream(parcelFileDescriptor.getFileDescriptor());

                    //Create a File instance
                    File file = new File(getCacheDir(), "activity_img");

                    //Create an output stream to the file
                    FileOutputStream fileOutputStream = new FileOutputStream(file);

                    //Copy the input contents to the file's output stream
                    IOUtils.copy(inputStream, fileOutputStream);

                    //Get the image bitmap
                    Bitmap imgBitmap = BitmapFactory.decodeFile( getCacheDir() + "/activity_img" );

                    //Close the streams
                    fileOutputStream.close();
                    inputStream.close();

                    //Now, upload the bitmap
                    uploadActivityImageBitmap(imgBitmap);

                } catch(Exception e) {
                    alert(getString(R.string.an_error_occurred), getString(R.string.please_try_again));
                }

            } else if (requestCode == CAMERA_REQUEST_CODE) {

                //Get the image bitmap
                Bitmap bitmap = BitmapFactory.decodeFile(filePath);

                //Upload the bitmap
                uploadActivityImageBitmap(bitmap);

            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        //Create the menu inflater
        MenuInflater inflater = getMenuInflater();

        //Inflate the menu
        inflater.inflate(R.menu.reflect_menu, menu);

        //Return true
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        //If it's the share item
        if (item.getItemId() == R.id.share) {

            //Show the sharing options screen
            showSharingOptions();

        }

        //If it's the back button
        else if (item.getItemId() == android.R.id.home) {

            //Go back
            onBackPressed();

        }

        return true;
    }

    private void showSharingOptions() {

        //Only run if the activity has been loaded
        if (activityLiveData != null) {

            //Create the intent
            Intent intent = new Intent(this, SharingOptionsActivity.class);

            //Add the activity id extra
            intent.putExtra("activityId", activityLiveData.getActivityId());

            //Present the sharing options activity
            startActivity(intent);

        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //Deregister our premium status listener
        PremiumStatusListener.shared().removeSubscriber(onPremiumStatusChanged);

    }
}

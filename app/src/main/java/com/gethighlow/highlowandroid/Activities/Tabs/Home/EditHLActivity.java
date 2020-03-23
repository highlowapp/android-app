package com.gethighlow.highlowandroid.Activities.Tabs.Home;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.VectorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.lifecycle.Observer;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.gethighlow.highlowandroid.R;
import com.gethighlow.highlowandroid.model.util.Consumer;
import com.gethighlow.highlowandroid.model.Managers.HighLowManager;
import com.gethighlow.highlowandroid.model.Managers.ImageManager;
import com.gethighlow.highlowandroid.model.Managers.LiveDataModels.HighLowLiveData;
import com.gethighlow.highlowandroid.model.Resources.HighLow;

import org.threeten.bp.LocalDate;
import org.threeten.bp.format.DateTimeFormatter;

import java.io.File;
import java.io.IOException;

public class EditHLActivity extends AppCompatActivity {

    private EditText textInput;
    private ImageView imageView;
    private String filePath;
    private HighLowLiveData highLow;
    private String type;
    private Boolean isPrivate = true;

    private int GALLERY_REQUEST_CODE = 80;
    private int CAMERA_REQUEST_CODE = 82;
    private int READ_STORAGE_REQUEST = 81;
    private int WRITE_STORAGE_REQUEST = 83;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.edit_hl_layout);

        imageView = findViewById(R.id.add_image);
        textInput = findViewById(R.id.text);

        type = getIntent().getStringExtra("type");

        String date = getIntent().getStringExtra("date");

        if (date != null) {
            HighLowManager.shared().getHighLowByDate(date, new Consumer<HighLowLiveData>() {
                @Override
                public void accept(HighLowLiveData liveData) {
                    highLow = liveData;
                    highLow.observe(EditHLActivity.this, highLowObserver);
                }
            }, new Consumer<String>() {
                @Override
                public void accept(String error) {
                    EditHLActivity.this.alert(getResources().getString(R.string.an_error_occurred), getResources().getString(R.string.please_try_again));
                }
            });
        }
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

    private Observer<HighLow> highLowObserver = new Observer<HighLow>() {
        @Override
        public void onChanged(HighLow highLow) {
            loadHighLow(highLow);
        }
    };

    private void loadHighLow(HighLow highLow) {
        String url;
        String text;
        if (type.equals("high")) {
            url = highLow.getHighImage();
            text = highLow.getHigh();
        } else {
            url = highLow.getLowImage();
            text = highLow.getLow();
        }

        if (url != null) {
            ImageManager.shared().getImage(url, new Consumer<Bitmap>() {
                @Override
                public void accept(Bitmap img) {
                    imageView.setImageBitmap(img);
                }
            }, new Consumer<String>() {
                @Override
                public void accept(String error) {
                    EditHLActivity.this.alert(getResources().getString(R.string.an_error_occurred), getResources().getString(R.string.please_try_again), new Runnable() {
                        @Override
                        public void run() {
                            EditHLActivity.this.finish();
                        }
                    });
                }
            });
        }

        if (text != null) {
            textInput.setText(text);
        }

    }

    public void pickImage(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("How would you like to upload an image?");
        final String[] items = {"Take a photo", "Use existing photo", "Cancel"};
        final Activity context = this;
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (items[i].equals("Use existing photo")) {
                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        gallery();
                    } else {
                        ActivityCompat.requestPermissions(context,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                READ_STORAGE_REQUEST);

                    }
                } else if (items[i].equals("Take a photo")) {
                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        camera();
                    } else {
                        ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_STORAGE_REQUEST);
                    }
                }
            }
        });

        builder.create().show();
    }

    private void alert(String title, String message) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setCancelable(true);
        alertDialog.setPositiveButton("OK", null);
        alertDialog.show();
    }
    private void alert(String title, String message, final Runnable onComplete) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setCancelable(true);
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                onComplete.run();
            }
        });
        alertDialog.show();
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GALLERY_REQUEST_CODE) {

                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                // Get the cursor
                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();
                //Get the column index of MediaStore.Images.Media.DATA
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                //Gets the String value in the column
                String imgDecodableString = cursor.getString(columnIndex);
                cursor.close();
                // Set the Image in ImageView after decoding the String
                imageView.setImageTintList(null);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setImageBitmap(BitmapFactory.decodeFile(imgDecodableString));

            } else if (requestCode == CAMERA_REQUEST_CODE) {
                Bitmap bitmap = BitmapFactory.decodeFile(filePath);

                imageView.setImageTintList(null);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setImageBitmap(bitmap);
            }
        }
    }

    public void done(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Who do  you want to see this High/Low?");

        final String[] items = {"Public", "Private", "Who can see my High/Lows?"};

        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == 0) {
                    isPrivate = false;
                    submit();
                } else if (i == 1) {
                    isPrivate = true;
                    submit();
                } else {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://gethighlow.com/help/highlowvisibility.html"));
                    startActivity(browserIntent);
                }
            }
        });

        builder.create().show();
    }

    public void submit() {
        String text = textInput.getText().toString();
        Bitmap img = null;

        if (!(imageView.getDrawable() instanceof VectorDrawable)) {
            img = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        }

        final String date;

        if (highLow != null) {
            date = highLow.getDate();
        } else {
            LocalDate localDate = LocalDate.now();
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            date = localDate.format(dateTimeFormatter);
        }

        if (highLow == null) {
            HighLow newHighLow = new HighLow();
            newHighLow.setDate(date);
            highLow = new HighLowLiveData(
                    newHighLow
            );
        }

        if (type.equals("high")) {
            highLow.setHigh(text, date, isPrivate, img, new Consumer<HighLow>() {
                @Override
                public void accept(HighLow highLow) {
                    HighLowManager.shared().saveHighLow(highLow);
                    Intent newIntent = new Intent("highlow-updated");
                    newIntent.putExtra("date", date);
                    LocalBroadcastManager.getInstance(EditHLActivity.this).sendBroadcast(newIntent);

                    EditHLActivity.this.finish();
                }
            }, new Consumer<String>() {
                @Override
                public void accept(String error) {
                }
            });
        } else {
            highLow.setLow(text, date, isPrivate, img, new Consumer<HighLow>() {
                @Override
                public void accept(HighLow highLow) {
                    HighLowManager.shared().saveHighLow(highLow);
                    Intent newIntent = new Intent("highlow-updated");
                    newIntent.putExtra("date", date);
                    LocalBroadcastManager.getInstance(EditHLActivity.this).sendBroadcast(newIntent);

                    EditHLActivity.this.finish();
                }
            }, new Consumer<String>() {
                @Override
                public void accept(String error) {
                }
            });
        }

    }


    public void dismiss(View view) {
        this.finish();
    }
}

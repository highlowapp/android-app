package com.gethighlow.highlowandroid.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.gethighlow.highlowandroid.R;
import com.gethighlow.highlowandroid.model.Managers.ImageManager;
import com.gethighlow.highlowandroid.model.Managers.UserManager;
import com.gethighlow.highlowandroid.model.Services.AuthService;

import java.io.File;
import java.io.IOException;

public class EditProfileActivity extends AppCompatActivity {
    private EditText firstName;
    private EditText lastName;
    private ImageView profileImage;
    private EditText bio;
    private RelativeLayout loader;

    private int GALLERY_REQUEST_CODE = 80;
    private int CAMERA_REQUEST_CODE = 82;
    private int READ_STORAGE_REQUEST = 81;
    private int WRITE_STORAGE_REQUEST = 83;

    private String filePath;

    private Boolean imgHasBeenChanged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile_activity);

        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        profileImage = findViewById(R.id.profile_image);
        bio = findViewById(R.id.bio);
        loader = findViewById(R.id.loader);

        String currentFirstName = getIntent().getStringExtra("firstName");
        String currentLastName = getIntent().getStringExtra("lastName");
        String currentProfileImage = getIntent().getStringExtra("profileImage");
        String currentBio = getIntent().getStringExtra("bio");

        firstName.setText(currentFirstName);
        lastName.setText(currentLastName);
        bio.setText(currentBio);

        loader.setVisibility(View.GONE);

        ImageManager.shared().getImage(currentProfileImage, bitmap -> {
            profileImage.setImageBitmap(bitmap);
        }, error -> {

        });
    }

    private void alert(String title, String message) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setCancelable(true);
        alertDialog.setPositiveButton("OK", null);
        alertDialog.show();
    }
    private void alert(String title, String message, Runnable onComplete) {
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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
                profileImage.setImageTintList(null);
                profileImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                profileImage.setImageBitmap(BitmapFactory.decodeFile(imgDecodableString));
                imgHasBeenChanged = true;

            } else if (requestCode == CAMERA_REQUEST_CODE) {
                Bitmap bitmap = BitmapFactory.decodeFile(filePath);

                profileImage.setImageTintList(null);
                profileImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                profileImage.setImageBitmap(bitmap);
                imgHasBeenChanged = true;
            }
        }
    }

    public void changeImage(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("How would you like to upload an image?");
        final String[] items = {"Take a photo", "Use existing photo", "Cancel"};
        Activity context = this;
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
            alert("An error occurred", "Please try again");
        }

        if (photo != null) {
            Uri photoUri = FileProvider.getUriForFile(this, "com.gethighlow.highlowandroid.fileprovider", photo);
            camera.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            startActivityForResult(camera, CAMERA_REQUEST_CODE);
        }
    }

    public void dismiss(View view) {
        this.finish();
    }

    public void done(View view) {
        this.submit();
    }

    private void submit() {
        loader.setVisibility(View.VISIBLE);

        String currentUserUid = AuthService.shared().getUid();
        UserManager.shared().getUser(currentUserUid, userLiveData -> {
            Bitmap img = null;

            if (imgHasBeenChanged) {
                img = ((BitmapDrawable) profileImage.getDrawable()).getBitmap();
            }

            userLiveData.setProfile(firstName.getText().toString(), lastName.getText().toString(), null, bio.getText().toString(), img, genericResponse -> {
                loader.setVisibility(View.GONE);
                this.finish();
            }, error -> {
                loader.setVisibility(View.GONE);
                alert("An error occurred", "Please try again");
            });

        }, error -> {
            loader.setVisibility(View.GONE);
            alert("An error occurred", "Please try again");
        });
    }
}

package com.gethighlow.highlowandroid.Activities.Tabs.Profile;

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
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.lifecycle.Observer;

import com.gethighlow.highlowandroid.R;
import com.gethighlow.highlowandroid.model.util.Consumer;
import com.gethighlow.highlowandroid.model.Managers.ImageManager;
import com.gethighlow.highlowandroid.model.Managers.LiveDataModels.UserLiveData;
import com.gethighlow.highlowandroid.model.Managers.UserManager;
import com.gethighlow.highlowandroid.model.Resources.User;
import com.gethighlow.highlowandroid.model.Responses.GenericResponse;
import com.gethighlow.highlowandroid.model.Services.AuthService;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import co.lujun.androidtagview.TagContainerLayout;

public class EditProfileActivity extends AppCompatActivity {
    private EditText firstName;
    private EditText lastName;
    private ImageView profileImage;
    private EditText bio;
    private RelativeLayout loader;
    private TagContainerLayout interestsView;
    private Button editInterests;

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
        interestsView = findViewById(R.id.interests);
        editInterests = findViewById(R.id.editInterests);

        editInterests.setOnClickListener(editInterestsListener);

        String currentFirstName = getIntent().getStringExtra("firstName");
        String currentLastName = getIntent().getStringExtra("lastName");
        String currentProfileImage = getIntent().getStringExtra("profileImage");
        String currentBio = getIntent().getStringExtra("bio");
        List<String> interests = getIntent().getStringArrayListExtra("interests");

        firstName.setText(currentFirstName);
        lastName.setText(currentLastName);
        bio.setText(currentBio);

        UserManager.shared().getUser(null, new Consumer<UserLiveData>() {
            @Override
            public void accept(UserLiveData userLiveData) {
                userLiveData.observe(EditProfileActivity.this, userObserver);
            }
        }, new Consumer<String>() {
            @Override
            public void accept(String error) {
                EditProfileActivity.this.alert(getResources().getString(R.string.an_error_occurred), getResources().getString(R.string.please_try_again));
            }
        });

        loader.setVisibility(View.GONE);

        ImageManager.shared().getImage(currentProfileImage, new Consumer<Bitmap>() {
            @Override
            public void accept(Bitmap bitmap) {
                profileImage.setImageBitmap(bitmap);
            }
        }, new Consumer<String>() {
            @Override
            public void accept(String error) {

            }
        });
    }

    private Observer<User> userObserver = new Observer<User>() {
        @Override
        public void onChanged(User user) {
            interestsView.removeAllTags();
            for (String interest : user.interests()) {
                interestsView.addTag(interest);
            }
        }
    };

    View.OnClickListener editInterestsListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent starter = new Intent(EditProfileActivity.this.getApplicationContext(), EditInterestsActivity.class);
            EditProfileActivity.this.startActivity(starter);
        }
    };

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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GALLERY_REQUEST_CODE) {

                try {
                    Uri selectedImage = data.getData();

                    ParcelFileDescriptor parcelFileDescriptor = getContentResolver().openFileDescriptor(selectedImage, "r", null);

                    FileInputStream inputStream = new FileInputStream(parcelFileDescriptor.getFileDescriptor());

                    File file = new File(getCacheDir(), "profile_img");

                    FileOutputStream fileOutputStream = new FileOutputStream(file);

                    IOUtils.copy(inputStream, fileOutputStream);

                    profileImage.setImageTintList(null);
                    profileImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    profileImage.setImageBitmap(BitmapFactory.decodeFile(getCacheDir() + "/profile_img"));
                    imgHasBeenChanged = true;

                    fileOutputStream.close();
                    inputStream.close();
                } catch(Exception e) {
                    alert(getString(R.string.an_error_occurred), getString(R.string.please_try_again));
                }


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
            alert(getResources().getString(R.string.an_error_occurred), getResources().getString(R.string.please_try_again));
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
        UserManager.shared().getUser(currentUserUid, new Consumer<UserLiveData>() {
            @Override
            public void accept(UserLiveData userLiveData) {
                Bitmap img = null;

                if (imgHasBeenChanged) {
                    img = ((BitmapDrawable) profileImage.getDrawable()).getBitmap();
                }

                userLiveData.setProfile(firstName.getText().toString(), lastName.getText().toString(), null, bio.getText().toString(), img, new Consumer<GenericResponse>() {
                    @Override
                    public void accept(GenericResponse genericResponse) {
                        loader.setVisibility(View.GONE);
                        EditProfileActivity.this.finish();
                    }
                }, new Consumer<String>() {
                    @Override
                    public void accept(String error) {
                        loader.setVisibility(View.GONE);
                        EditProfileActivity.this.alert(getResources().getString(R.string.an_error_occurred), getResources().getString(R.string.please_try_again));
                    }
                });

            }
        }, new Consumer<String>() {
            @Override
            public void accept(String error) {
                loader.setVisibility(View.GONE);
                EditProfileActivity.this.alert(getResources().getString(R.string.an_error_occurred), getResources().getString(R.string.please_try_again));
            }
        });
    }
}

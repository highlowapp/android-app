package com.gethighlow.highlowandroid.CustomViews;

import android.media.Image;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.gethighlow.highlowandroid.R;
import com.gethighlow.highlowandroid.model.Managers.LiveDataModels.HighLowLiveData;
import com.gethighlow.highlowandroid.model.Managers.UserManager;
import com.gethighlow.highlowandroid.model.Resources.User;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class HighLowViewHolder extends RecyclerView.ViewHolder {
    private HighLowView highLowView;
    private ImageView userImage;
    private TextView userName;
    private TextView date;
    private LifecycleOwner lifecycleOwner;

    public HighLowViewHolder(View view) {
        super(view);

        highLowView = view.findViewById(R.id.highlowview);
        userImage = view.findViewById(R.id.userImage);
        userName = view.findViewById(R.id.userName);
        date = view.findViewById(R.id.highlowDate);

    }

    public void attachToLifecycleOwner(LifecycleOwner lifecycleOwner) {
        this.lifecycleOwner = lifecycleOwner;
    }

    public void setHighLow(HighLowLiveData highLowLiveData) {
        userImage.setImageBitmap(null);
        userName.setText("");
        date.setText("");
        UserManager.shared().getUser(highLowLiveData.getUid(), userLiveData -> {
            setUser(userLiveData.getUser());
        }, error -> {
            Log.w("Debug", error);
        });

        highLowView.attachToLiveData(lifecycleOwner, highLowLiveData);

        DateTimeFormatter toDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(highLowLiveData.getDate(), toDate);
        DateTimeFormatter newFormatter = DateTimeFormatter.ofPattern("'Posted on' MMM d, uuuu");
        date.setText(localDate.format(newFormatter));
    }

    private void setUser(User user) {
        userImage.setImageBitmap(null);

        user.fetchProfileImage(bitmap -> {
            userImage.setImageBitmap(bitmap);
        }, error -> {

        });

        userName.setText(user.name());
    }
}

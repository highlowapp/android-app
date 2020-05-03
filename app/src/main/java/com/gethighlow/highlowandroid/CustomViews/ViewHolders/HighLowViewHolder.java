package com.gethighlow.highlowandroid.CustomViews.ViewHolders;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.gethighlow.highlowandroid.CustomViews.Other.HighLowView;
import com.gethighlow.highlowandroid.R;
import com.gethighlow.highlowandroid.model.util.Consumer;
import com.gethighlow.highlowandroid.model.Managers.LiveDataModels.HighLowLiveData;
import com.gethighlow.highlowandroid.model.Managers.LiveDataModels.UserLiveData;
import com.gethighlow.highlowandroid.model.Managers.UserManager;
import com.gethighlow.highlowandroid.model.Resources.User;

import org.threeten.bp.LocalDate;
import org.threeten.bp.format.DateTimeFormatter;

public class HighLowViewHolder extends RecyclerView.ViewHolder /*implements SwipeRefreshLayout.OnRefreshListener*/ {
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
        UserManager.shared().getUser(highLowLiveData.getUid(), new Consumer<UserLiveData>() {
            @Override
            public void accept(UserLiveData userLiveData) {
                HighLowViewHolder.this.setUser(userLiveData.getUser());
            }
        }, new Consumer<String>() {
            @Override
            public void accept(String error) {
            }
        });

        highLowView.attachToLiveData(lifecycleOwner, highLowLiveData);

        DateTimeFormatter toDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(highLowLiveData.getDate(), toDate);
        DateTimeFormatter newFormatter = DateTimeFormatter.ofPattern("'Posted on' MMM d, uuuu");
        date.setText(localDate.format(newFormatter));

    }

    private void setUser(User user) {
        userImage.setImageBitmap(null);

        user.fetchProfileImage(new Consumer<Bitmap>() {
            @Override
            public void accept(Bitmap bitmap) {
                userImage.setImageBitmap(bitmap);
            }
        }, new Consumer<String>() {
            @Override
            public void accept(String error) {

            }
        });

        userName.setText(user.name());
    }
}

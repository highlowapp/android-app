package com.gethighlow.highlowandroid.CustomViews.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gethighlow.highlowandroid.Activities.Tabs.Diary.Diary;
import com.gethighlow.highlowandroid.CustomViews.ViewHolders.ChooseUserViewHolder;
import com.gethighlow.highlowandroid.CustomViews.ViewHolders.Delegates.ChooseUserRowViewHolderDelegate;
import com.gethighlow.highlowandroid.CustomViews.ViewHolders.DiaryViewHolder;
import com.gethighlow.highlowandroid.R;
import com.gethighlow.highlowandroid.model.Managers.LiveDataModels.ActivityLiveData;
import com.gethighlow.highlowandroid.model.Managers.LiveDataModels.UserLiveData;
import com.gethighlow.highlowandroid.model.Resources.User;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

public class ChooseIndividuallyAdapter extends RecyclerView.Adapter<ChooseUserViewHolder> {
    Context context;
    private List<UserLiveData> users;
    public Diary diary;
    private UserLiveData user;

    private List<String> uids = new ArrayList<String>();

    private ChooseUserRowViewHolderDelegate chooseUserRowViewHolderDelegate;


    private LifecycleOwner lifecycleOwner;

    public ChooseIndividuallyAdapter(LifecycleOwner lifecycleOwner, List<UserLiveData> users, ChooseUserRowViewHolderDelegate chooseUserRowViewHolderDelegate) {
        this.lifecycleOwner = lifecycleOwner;
        this.users = this.users;
        this.chooseUserRowViewHolderDelegate = chooseUserRowViewHolderDelegate;
    }

    public void setUsers(List<UserLiveData> users) {

        //Update the users
        this.users = users;

        //Notify the data set changed
        this.notifyDataSetChanged();

    }

    public void setUids(List<String> uids) {

        //Update the uids
        this.uids = uids;

        //Notify the data set changed
        this.notifyDataSetChanged();

    }


    @NotNull
    @Override
    public ChooseUserViewHolder onCreateViewHolder(@NotNull ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.choose_user_layout, viewGroup, false);
        return new ChooseUserViewHolder(lifecycleOwner, chooseUserRowViewHolderDelegate, itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ChooseUserViewHolder holder, int position) {

        //Stop with the users
        if (users == null) return;

        //Get the user at that position
        UserLiveData userLiveData = users.get(position);

        //Set the user
        holder.setUser( userLiveData );

        //Get the user object
        User user = userLiveData.getUser();

        //If this user's uid is in the list
        //Set the switch on
        //Otherwise, set the switch off
        holder.setSwitch( uids.contains( user.uid() ) );

    }

    @Override
    public int getItemCount() {
        if (users != null) return users.size();
        return 0;
    }

}

package com.gethighlow.highlowandroid.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.gethighlow.highlowandroid.CustomViews.FriendsSection;
import com.gethighlow.highlowandroid.CustomViews.FriendsSectionDelegate;
import com.gethighlow.highlowandroid.CustomViews.PendingRequestsSection;
import com.gethighlow.highlowandroid.CustomViews.PendingRequestsSectionDelegate;
import com.gethighlow.highlowandroid.R;
import com.gethighlow.highlowandroid.model.Managers.LiveDataModels.UserLiveData;
import com.gethighlow.highlowandroid.model.Managers.UserManager;
import com.gethighlow.highlowandroid.model.Services.AuthService;

import java.util.ArrayList;
import java.util.List;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;

public class FriendsActivity extends AppCompatActivity implements PendingRequestsSectionDelegate, FriendsSectionDelegate {
    private RecyclerView recyclerView;
    private SectionedRecyclerViewAdapter adapter;
    private List<UserLiveData> users = new ArrayList<>();
    private List<UserLiveData> pending = new ArrayList<>();
    private String uid = AuthService.shared().getUid();
    private FriendsSection friendsSection;
    private RelativeLayout addFriendsSection;
    private Button addFriends;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friends_activity);

        getSupportActionBar().setTitle("Friends");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.recyclerView);
        addFriendsSection = findViewById(R.id.addFriendsSection);
        addFriends = findViewById(R.id.addFriends);

        addFriends.setOnClickListener(addFriendsListener);

        uid = getIntent().getStringExtra("uid");

        if (uid == null) uid = AuthService.shared().getUid();
        else {
            if (!uid.equals(AuthService.shared().getUid())) addFriendsSection.setVisibility(View.GONE);
        }

        adapter = new SectionedRecyclerViewAdapter();

        if (uid.equals(AuthService.shared().getUid())) adapter.addSection(new PendingRequestsSection(this, this, pending));

        friendsSection = new FriendsSection(this, this, users);
        adapter.addSection(friendsSection);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        getFriends();
    }

    private View.OnClickListener addFriendsListener = view -> {
        Intent starter = new Intent(this, AddFriendsActivity.class);
        startActivity(starter);
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.friends_action_bar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        } else if (item.getItemId() == R.id.editFriends) {
            if (item.getTitle().equals("Edit")) {
                item.setTitle("Cancel");
            } else {
                item.setTitle("Edit");
            }
            friendsSection.toggleEditable();
            adapter.notifyDataSetChanged();
        }

        return super.onOptionsItemSelected(item);
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


    private void getFriends() {
        UserManager.shared().getUser(uid, userLiveData -> {

            userLiveData.getFriends(friends -> {
                users.clear();
                users.addAll(friends);
                adapter.notifyDataSetChanged();
            }, error -> alert("An error occurred", "Please try again"));

            if (uid.equals(AuthService.shared().getUid())) {
                userLiveData.getPendingFriendships(requests -> {
                    pending.clear();
                    pending.addAll(requests);
                    adapter.notifyDataSetChanged();
                }, error -> alert("An error occurred", "Please try again"));
            }

        }, error -> alert("An error occurred", "Please try again"));
    }

    @Override
    public void acceptFriend(int position) {
        UserLiveData user = pending.get(position);
        pending.remove(user);
        adapter.notifyItemRemoved(position);
        user.acceptFriendship(genericResponse -> {
            getFriends();
        }, error -> alert("An error occurred", "Please try again"));
    }

    @Override
    public void rejectFriend(int position) {
        UserLiveData user = pending.get(position);
        user.unFriend(genericResponse -> {
            pending.remove(user);
            adapter.notifyItemRemoved(position);
        }, error -> alert("An error occurred", "Please try again"));
    }

    @Override
    public void unFriend(int position) {
        UserLiveData user = users.get(position);
        user.unFriend(genericResponse -> {
            pending.remove(user);
            adapter.notifyItemRemoved(position + 2 + pending.size());
        }, error -> alert("An error occurred", "Please try again"));
    }
}

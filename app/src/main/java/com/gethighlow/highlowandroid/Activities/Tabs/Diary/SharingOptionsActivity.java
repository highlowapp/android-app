package com.gethighlow.highlowandroid.Activities.Tabs.Diary;

import android.content.Intent;
import android.os.Bundle;

import com.gethighlow.highlowandroid.CustomViews.Other.ProgressLoaderView;
import com.gethighlow.highlowandroid.model.Resources.SharingPolicy;
import com.gethighlow.highlowandroid.model.Responses.SharingPolicyResponse;
import com.gethighlow.highlowandroid.model.ViewModels.SharingOptionsViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentContainer;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.gethighlow.highlowandroid.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SharingOptionsActivity extends AppCompatActivity {

    private LinearLayout sharePrivate;
    private LinearLayout sharePublic;
    private LinearLayout shareFriends;
    private LinearLayout shareChooseIndividually;

    private ChooseIndividuallyFragment chooseIndividuallyFragment = new ChooseIndividuallyFragment();


    private ViewGroup rootView;

    private Button saveButton;

    private ProgressLoaderView progressLoaderView;

    private Map<String, LinearLayout> policyTypeToView = new HashMap<String, LinearLayout>();

    private View.OnClickListener onItemSelected = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            //Do what needs to be done, depending on the view
            if (view == sharePrivate) {
                onSharePrivateSelected();
            } else if (view == sharePublic) {
                onSharePublicSelected();
            } else if (view == shareFriends) {
                onShareFriendsSelected();
            } else if (view == shareChooseIndividually) {
                onShareChooseIndividuallySelected();
            }

        }
    };

    private View.OnClickListener onSaveButtonPressed = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            //Show the loading screen
            progressLoaderView.startLoading();

            //Set the sharing policy
            viewModel.setSharingPolicy(new Runnable() {
                @Override
                public void run() {

                    //Stop loading
                    progressLoaderView.stopLoading();

                }
            });

        }
    };

    private SharingOptionsViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Set the content view
        setContentView(R.layout.activity_sharing_options);

        //Add the back button
        Objects.requireNonNull( getSupportActionBar() ).setDisplayHomeAsUpEnabled(true);

        //Get the views by id
        sharePrivate = findViewById(R.id.sharingPrivate);
        sharePublic = findViewById(R.id.sharingPublic);
        shareFriends = findViewById(R.id.sharingFriends);
        shareChooseIndividually = findViewById(R.id.shareChooseIndividually);
        saveButton = findViewById(R.id.save);
        progressLoaderView = findViewById(R.id.loader);
        rootView = findViewById(R.id.rootView);

        //Set the on click listeners
        sharePrivate.setOnClickListener(onItemSelected);
        sharePublic.setOnClickListener(onItemSelected);
        shareFriends.setOnClickListener(onItemSelected);
        shareChooseIndividually.setOnClickListener(onItemSelected);
        saveButton.setOnClickListener(onSaveButtonPressed);

        //Update map with views
        policyTypeToView.put("private", sharePrivate);
        policyTypeToView.put("public", sharePublic);
        policyTypeToView.put("friends", shareFriends);
        policyTypeToView.put("uids", shareChooseIndividually);
        policyTypeToView.put("none", sharePrivate);

        //Set the progress loader view content view
        progressLoaderView.setView(rootView);

        //Get our view model
        viewModel = new ViewModelProvider(this).get(SharingOptionsViewModel.class);

        //Get the activity id
        String activityId = getActivityId();

       //Set the activity id to the view model
       viewModel.setActivityId(activityId);

       //Subscribe to the selection
        viewModel.getSelection().observe(this, onSelectionUpdate);

        //Subscribe to the sharing policy
        viewModel.getSharingPolicy().observe(this, onSharingPolicyUpdate);

        //Add the fragment to the container
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .add(R.id.chooseIndividuallyFragment, chooseIndividuallyFragment)
                .setReorderingAllowed(true)
                .hide(chooseIndividuallyFragment)
                .commit();

    }

    private Observer<SharingPolicy> onSelectionUpdate = new Observer<SharingPolicy>() {
        @Override
        public void onChanged(SharingPolicy sharingPolicyResponse) {

            //Get the relevant view
            LinearLayout view = policyTypeToView.get( sharingPolicyResponse.getSharingPolicy() );

            //Select that view
            selectView(view);

        }
    };

    private Observer<SharingPolicy> onSharingPolicyUpdate = new Observer<SharingPolicy>() {
        @Override
        public void onChanged(SharingPolicy sharingPolicyResponse) {

            Log.w("Debug", sharingPolicyResponse.getSharingPolicy());

            //Get the view corresponding with the sharing policy type
            LinearLayout view = policyTypeToView.get( sharingPolicyResponse.getSharingPolicy() );

            //Select the view
            selectView(view);

        }
    };


    private String getActivityId() {

        //Get the intent
        Intent intent = getIntent();

        //Get the activityId extra
        return intent.getStringExtra("activityId");

    }

    private void selectView(LinearLayout view) {

        //Highlight the selected view while removing highlights from other views
        view.setBackground(ContextCompat.getDrawable(this, R.drawable.selected_option));

        //Remove the background from other items...
        if (view != sharePrivate) sharePrivate.setBackground(null);
        if (view != sharePublic) sharePublic.setBackground(null);
        if (view != shareFriends) shareFriends.setBackground(null);
        if (view != shareChooseIndividually) shareChooseIndividually.setBackground(null);

    }

    private void onSharePrivateSelected() {

        //Create sharing policy
        SharingPolicy sharingPolicy = new SharingPolicy("none", null);

        //Set the selection to private
        viewModel.setSelected(sharingPolicy);

    }

    private void onSharePublicSelected() {

        //Create sharing policy
        SharingPolicy sharingPolicy = new SharingPolicy("public", null);

        //Set the selection to public
        viewModel.setSelected(sharingPolicy);

    }

    private void onShareFriendsSelected() {

        //Create sharing policy
        SharingPolicy sharingPolicy = new SharingPolicy("friends", null);

        //Set the selection to friends
        viewModel.setSelected(sharingPolicy);

    }

    private void onShareChooseIndividuallySelected() {

        //Create the sharing policy
        SharingPolicy sharingPolicy = new SharingPolicy("uids", null);

        //Set the selection to uids
        viewModel.setSelected(sharingPolicy);

        //Show the choose individually screen
        showChooseIndividuallyFragment();

    }

    private void showChooseIndividuallyFragment() {

        //Get the fragment manager
        FragmentManager fm = getSupportFragmentManager();

        //Create and run the transaction
        fm.beginTransaction()

                .show(chooseIndividuallyFragment)
                .commit();

    }

    private void hideChooseIndividuallyFragment() {

        //Get the fragment manager
        FragmentManager fm = getSupportFragmentManager();

        //Create and run the transaction
        fm.beginTransaction()
                .hide(chooseIndividuallyFragment)
                .commit();

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
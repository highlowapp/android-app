package com.gethighlow.highlowandroid.Activities.Tabs.Diary;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.gethighlow.highlowandroid.CustomViews.Adapters.ChooseIndividuallyAdapter;
import com.gethighlow.highlowandroid.CustomViews.ViewHolders.Delegates.ChooseUserRowViewHolderDelegate;
import com.gethighlow.highlowandroid.R;
import com.gethighlow.highlowandroid.model.Managers.LiveDataModels.UserLiveData;
import com.gethighlow.highlowandroid.model.Resources.SharingPolicy;
import com.gethighlow.highlowandroid.model.ViewModels.SharingOptionsViewModel;

import java.util.List;

public class    ChooseIndividuallyFragment extends Fragment implements ChooseUserRowViewHolderDelegate {

    private Button cancelButton;

    private RecyclerView recyclerView;
    private ChooseIndividuallyAdapter adapter;

    private SharingOptionsViewModel viewModel;

    private View.OnClickListener onCancelButtonClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            //Run a fragment transaction to hide the fragment
            FragmentManager fm = getParentFragmentManager();
            fm.beginTransaction()
                    .hide(ChooseIndividuallyFragment.this)
                    .commit();

        }
    };

    public ChooseIndividuallyFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_choose_individually, container, false);

        //Get the views
        cancelButton = view.findViewById(R.id.cancel_button);
        recyclerView = view.findViewById(R.id.recyclerView);

        //Set the cancel button onclicklistener
        cancelButton.setOnClickListener(onCancelButtonClicked);

        //Create the linearlayoutmanager
        LinearLayoutManager layoutManager = new LinearLayoutManager( getContext() );

        //Set the layout manager
        recyclerView.setLayoutManager(layoutManager);

        //Create the adapter
        adapter = new ChooseIndividuallyAdapter( this, null, this );

        //Set the adapter
        recyclerView.setAdapter(adapter);

        //Get the viewmodel
        viewModel = new ViewModelProvider( requireActivity() ).get(SharingOptionsViewModel.class);

        //Get the friends and observe them
        viewModel.getFriends().observe( getViewLifecycleOwner(), onFriendsUpdate );

        //We'll also observe the sharing policy
        viewModel.getSharingPolicy().observe( getViewLifecycleOwner(), onSharingPolicyUpdate );

        //Return the view
        return view;

    }

    private Observer<List<UserLiveData>> onFriendsUpdate = new Observer<List<UserLiveData>>() {
        @Override
        public void onChanged(List<UserLiveData> userLiveDataList) {

            //Set the friends
            adapter.setUsers(userLiveDataList);

        }
    };


    private Observer<SharingPolicy> onSharingPolicyUpdate = new Observer<SharingPolicy>() {
        @Override
        public void onChanged(SharingPolicy sharingPolicy) {

            //Get the uids
            List<String> uids = sharingPolicy.getUids();

            //If the uids are not null
            if (uids != null) {

                //Set the adapter uids
                adapter.setUids(uids);

            }

        }
    };


    @Override
    public void onSwitchStateChanged(boolean state, String uid) {

        //If the switch is on, add to the uids
        if (state) viewModel.addToUids(uid);

        //If the switch is off, remove from the uids
        else viewModel.removeFromUids(uid);

    }
}
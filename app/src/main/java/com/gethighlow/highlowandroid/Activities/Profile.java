package com.gethighlow.highlowandroid.Activities;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gethighlow.highlowandroid.CustomViews.ProfileAdapter;
import com.gethighlow.highlowandroid.R;
import com.gethighlow.highlowandroid.model.Managers.HighLowManager;
import com.gethighlow.highlowandroid.model.Managers.LiveDataModels.HighLowLiveData;
import com.gethighlow.highlowandroid.model.Managers.LiveDataModels.UserLiveData;
import com.gethighlow.highlowandroid.model.Managers.UserManager;
import com.gethighlow.highlowandroid.model.Services.AuthService;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Profile.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class Profile extends Fragment {

    private OnFragmentInteractionListener mListener;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<HighLowLiveData> highLows = new ArrayList<>();
    private UserLiveData user;

    public Profile() {
        // Required empty public constructor
    }

    public void getProfile() {
        String uid = AuthService.shared().getUid();
        UserManager.shared().getUser(uid, userLiveData -> {
            user = userLiveData;
            adapter.notifyDataSetChanged();

            user.getHighLows(0, highLowLiveDataList -> {
                highLows.clear();
                highLows.addAll(highLowLiveDataList);
                Log.w("Debug", highLows.toString());
                adapter.notifyDataSetChanged();
            }, error -> {
                Log.w("Debug", error);
            });
        }, error -> {
            Log.w("Debug", error);
        });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.profile_fragment, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ProfileAdapter(this, user, highLows);
        recyclerView.setAdapter(adapter);


        getProfile();

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onProfileFragmentInteraction(Uri uri);
    }
}

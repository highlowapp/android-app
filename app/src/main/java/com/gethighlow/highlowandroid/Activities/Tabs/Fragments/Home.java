package com.gethighlow.highlowandroid.Activities.Tabs.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.gethighlow.highlowandroid.R;
import com.gethighlow.highlowandroid.thehayro.view.InfiniteViewPager;

import java.time.LocalDate;

public class Home extends Fragment {

    private OnFragmentInteractionListener mListener;
    private InfiniteViewPager viewPager;
    private AppCompatActivity appCompatActivity;

    public Home() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void setAppCompatActivity(AppCompatActivity activity) {
        appCompatActivity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View result = inflater.inflate(R.layout.fragment_home, container, false);

        return result;
    }

    public void setDate(LocalDate date) {
        viewPager.setCurrentIndicator(date);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    public void onViewCreated(View v, Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);

        viewPager = getView().findViewById(R.id.viewpager);
        HomePagerAdapter homePagerAdapter = new HomePagerAdapter(this, LocalDate.now());
        viewPager.setAdapter(homePagerAdapter);

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
        void onHomeFragmentInteraction(Uri uri);
    }
}

package com.gethighlow.highlowandroid.Activities.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gethighlow.highlowandroid.R;
import com.gethighlow.highlowandroid.thehayro.view.InfinitePagerAdapter;
import com.gethighlow.highlowandroid.thehayro.view.InfiniteViewPager;

import java.time.LocalDate;
import java.util.Date;

public class Home extends Fragment {

    private OnFragmentInteractionListener mListener;
    private InfiniteViewPager viewPager;

    public Home() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View result = inflater.inflate(R.layout.fragment_home, container, false);

        return result;
    }

    public void onViewCreated(View v, Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);

        viewPager = getView().findViewById(R.id.viewpager);
        HomePagerAdapter homePagerAdapter = new HomePagerAdapter(getContext(), LocalDate.now());
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

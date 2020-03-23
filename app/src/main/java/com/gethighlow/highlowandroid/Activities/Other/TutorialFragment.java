package com.gethighlow.highlowandroid.Activities.Other;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.gethighlow.highlowandroid.CustomViews.BaseComponents.HLButton;
import com.gethighlow.highlowandroid.CustomViews.BaseComponents.HLButtonDelegate;
import com.gethighlow.highlowandroid.R;

public class TutorialFragment extends Fragment {
    private String caption = "";
    private int imageResource = 0;
    private HLButton getStartedButton;

    public HLButtonDelegate buttonDelegate;

    private TextView captionView;
    private ImageView imageView;

    private Boolean hasButton;

    public TutorialFragment(String caption, int imageResource, Boolean hasButton) {
        this.caption = caption;
        this.imageResource = imageResource;
        this.hasButton = hasButton;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tutorial_fragment, container, false);

        captionView = view.findViewById(R.id.caption);
        imageView = view.findViewById(R.id.imageView);
        getStartedButton = view.findViewById(R.id.getStartedButton);

        if (!hasButton) {
            getStartedButton.setVisibility(View.GONE);
        } else {
            getStartedButton.delegate = buttonDelegate;
        }

        captionView.setText(caption);
        imageView.setImageResource(imageResource);

        return view;
    }

    public void setButtonDelegate(HLButtonDelegate delegate) {
        buttonDelegate = delegate;
    }
}

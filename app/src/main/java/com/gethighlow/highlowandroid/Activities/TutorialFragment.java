package com.gethighlow.highlowandroid.Activities;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gethighlow.highlowandroid.R;

import org.w3c.dom.Text;

public class TutorialFragment extends Fragment {
    private String caption = "";
    private int imageResource = 0;

    private TextView captionView;
    private ImageView imageView;

    public TutorialFragment(String caption, int imageResource) {
        this.caption = caption;
        this.imageResource = imageResource;
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

        captionView.setText(caption);
        imageView.setImageResource(imageResource);

        return view;
    }
}

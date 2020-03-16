package com.gethighlow.highlowandroid.Activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.gethighlow.highlowandroid.R;
import com.gethighlow.highlowandroid.model.Services.AuthService;

public class About extends Fragment {
    private TextView logOut;
    private TextView reportBug;
    private TextView pushNotifSettings;
    private TextView ourWebsite;
    private TextView contactUs;
    private TextView privacyPolicy;
    private TextView termsOfService;

    public About() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.about_fragment, container, false);

        logOut = view.findViewById(R.id.logOut);
        reportBug = view.findViewById(R.id.reportBug);
        pushNotifSettings = view.findViewById(R.id.pushNotifSettings);
        ourWebsite = view.findViewById(R.id.ourWebsite);
        contactUs = view.findViewById(R.id.contactUs);
        privacyPolicy = view.findViewById(R.id.privacyPolicy);
        termsOfService = view.findViewById(R.id.termsOfService);

        logOut.setOnClickListener(logOutListener);
        reportBug.setOnClickListener(reportBugListener);
        pushNotifSettings.setOnClickListener(pushNotifSettingsListener);
        ourWebsite.setOnClickListener(ourWebsiteListener);
        contactUs.setOnClickListener(contactUsListener);
        privacyPolicy.setOnClickListener(privacyPolicyListener);
        termsOfService.setOnClickListener(termsOfServiceListener);

        return view;
    }



    private View.OnClickListener logOutListener = view -> {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Confirm logout");
        builder.setMessage("Are you sure you want to log out?");
        builder.setPositiveButton("Yes", (dialogInterface, i) -> AuthService.shared().logOut());
        builder.setNegativeButton("No", null);
        builder.create().show();
    };



    private View.OnClickListener reportBugListener = view -> {
        Intent starter = new Intent(getContext(), ReportABug.class);
        this.startActivity(starter);
    };
    private View.OnClickListener pushNotifSettingsListener = view -> {
        Intent starter = new Intent(getContext(), PushNotifSettings.class);
        this.startActivity(starter);
    };
    private View.OnClickListener ourWebsiteListener = view -> {
        //Present web page
        openURL("https://gethighlow.com");
    };
    private View.OnClickListener contactUsListener = view -> {
        //Present web page
        openURL("https://gethighlow.com/contact");
    };
    private View.OnClickListener privacyPolicyListener = view -> {
        //Present web page
        openURL("https://gethighlow.com/privacy");
    };
    private View.OnClickListener termsOfServiceListener = view -> {
        //Present web page
        openURL("https://gethighlow.com/eula");
    };

    private void openURL(String url) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }
}

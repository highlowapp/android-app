package com.gethighlow.highlowandroid.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.gethighlow.highlowandroid.R;
import com.gethighlow.highlowandroid.model.Services.APIService;
import com.gethighlow.highlowandroid.model.Services.AuthService;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

public class TutorialActivity extends AppCompatActivity {
    private ViewPager2 viewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tutorial_activity);

        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager.setAdapter(new TutorialAdapter(getSupportFragmentManager()));
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            viewPager.setCurrentItem(position);
        }).attach();
    }

    private class TutorialAdapter extends FragmentStateAdapter {
        private List<TutorialFragment> fragments = new ArrayList<>();


        public TutorialAdapter(FragmentManager fm) {
            super(fm, getLifecycle());

            fragments.add(new TutorialFragment("Swipe to see previous High/Lows", R.drawable.tutorial1));
            fragments.add(new TutorialFragment("Connect with Friends based on your interests", R.drawable.connect));
            fragments.add(new TutorialFragment("Get a daily reminder to reflect", R.drawable.dailyreminder));
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return fragments.get(position);
        }

        @Override
        public int getItemCount() {
            return fragments.size();
        }
    }

    private void skip() {
        SharedPreferences.Editor editor = getSharedPreferences("com.gethighlow.SharedPref", Context.MODE_PRIVATE).edit();
        editor.putBoolean("hasReceivedTutorial", true);
        editor.apply();
        this.finish();

        Intent starter = new Intent(this, TabActivity.class);
        startActivity(starter);
    }
}

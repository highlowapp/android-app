
package com.gethighlow.highlowandroid.Activities.Tabs.Diary;

import com.gethighlow.highlowandroid.model.Resources.Activity;

import android.app.Application.ActivityLifecycleCallbacks;
import android.content.Context;
import android.location.Address;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gethighlow.highlowandroid.Activities.Tabs.Profile.Profile;
import com.gethighlow.highlowandroid.CustomViews.Adapters.DiaryAdapter;
import com.gethighlow.highlowandroid.CustomViews.Sections.EndlessRecyclerViewScrollListener;
import com.gethighlow.highlowandroid.R;
import com.gethighlow.highlowandroid.model.Managers.ActivityManager;
import com.gethighlow.highlowandroid.model.Managers.HighLowManager;
import com.gethighlow.highlowandroid.model.Managers.LiveDataModels.ActivityLiveData;
import com.gethighlow.highlowandroid.model.Managers.LiveDataModels.HighLowLiveData;
import com.gethighlow.highlowandroid.model.Managers.LiveDataModels.UserLiveData;
import com.gethighlow.highlowandroid.model.Managers.UserManager;
import com.gethighlow.highlowandroid.model.Resources.HighLow;
import com.gethighlow.highlowandroid.model.Responses.UserActivitiesResponse;
import com.gethighlow.highlowandroid.model.Services.ActivityService;
import com.gethighlow.highlowandroid.model.Services.SetActivityTheme;
import com.gethighlow.highlowandroid.model.util.Consumer;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class Diary extends Fragment {
    private Profile.OnFragmentInteractionListener mListener;
    private RecyclerView recyclerView;
    private DiaryAdapter adapter;
    private ActivityLiveData activity;
    private ArrayList<ActivityLiveData> activities = new ArrayList<ActivityLiveData>();
    private EndlessRecyclerViewScrollListener scrollListener;
    private SwipeRefreshLayout refreshLayout;
    private LinearLayoutManager layoutManager;
    private TextView noDiary;
    private TextView title;
    private ImageView typeImage;



    public Diary() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

//        Context context = getApplicationContext();
//        String theme = SetActivityTheme.getTheme(context);
//
//        if(theme.equals("light")){
//            setTheme(R.style.LightTheme);
//        }else{
//            setTheme(R.style.DarkTheme);
//        }


        super.onCreate(savedInstanceState);




    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View result = inflater.inflate(R.layout.diary_layout, container, false);

        recyclerView = (RecyclerView) result.findViewById(R.id.diaryRecyclerView);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        noDiary = result.findViewById(R.id.no_diary_notifier);
        title = result.findViewById(R.id.diary_title);
        typeImage = result.findViewById(R.id.diary_image);

        if(activity != null) {
            noDiary.setVisibility(View.GONE);
            getDiaryEntries(0);
        } else{
            noDiary.setVisibility(View.VISIBLE);
        }





        adapter = new DiaryAdapter(this, activities);
        recyclerView.setAdapter(adapter);
        return result;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    private View getView(Context context, LayoutInflater inflater, ViewGroup container){
        View view;
        String currentTheme = SetActivityTheme.getTheme(context);
        if(currentTheme.equals("light")) {
            final ContextThemeWrapper theme = new ContextThemeWrapper(context, R.style.LightTheme);
            LayoutInflater localInflater = inflater.cloneInContext(theme);
            view = localInflater.inflate(R.layout.diary_fragment, container, false);
            return view;
        } else{
            final ContextThemeWrapper theme = new ContextThemeWrapper(context, R.style.DarkTheme);
            LayoutInflater localInflater = inflater.cloneInContext(theme);
            view = localInflater.inflate(R.layout.diary_fragment, container, false);
            return view;
        }

    }



    public void getDiaryEntries(int page) {

        if (activity.equals(null)) {
            return;
        } else {

            if (activities.size() == 0 || page + 1 > activities.size() / 10) {
                final int currentPos = activities.size();
                ActivityService.shared().getDiaryEntries(page, new Consumer<List<ActivityLiveData>>() {
                    @Override
                    public void accept(List<ActivityLiveData> activityLiveDataList) {
                        activities.addAll(activityLiveDataList);
                        adapter.notifyItemRangeInserted(currentPos + 1, 10);

                        //TODO: Continue working on diary display
                        if (activities.size() == 0) {

                        } else {

                        }
                        refreshLayout.setRefreshing(false);
                    }
                }, new Consumer<String>() {
                    @Override
                    public void accept(String error) {
                        refreshLayout.setRefreshing(false);
                        Diary.this.alert(getResources().getString(R.string.an_error_occurred), getResources().getString(R.string.please_try_again));
                    }
                });
            }
        }
    }

    private void setTitle(){
        title.setText(activity.getTitle());
    }

    private void setTypeImage(){
        String type = activity.getType();
        if(type.equals("highlow")){
            typeImage.setImageResource(R.drawable.logo_light);
        }else if(type.equals("audio")){
            typeImage.setImageResource(R.drawable.ic_audio_entry);
        }else if(typeImage.equals("diary")){
            typeImage.setImageResource(R.drawable.ic_diary_entry);
        }
    }


    public void getHighLow(){

    }



    private void alert(String title, String message) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext(), R.style.AlertDialogCustom);
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setCancelable(true);
        alertDialog.setPositiveButton("OK", null);
        alertDialog.show();
    }


}



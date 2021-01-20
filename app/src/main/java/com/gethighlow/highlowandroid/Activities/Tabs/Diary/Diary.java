
package com.gethighlow.highlowandroid.Activities.Tabs.Diary;

import com.gethighlow.highlowandroid.CustomViews.Adapters.DiaryEntryClickListener;
import com.gethighlow.highlowandroid.model.Resources.Activity;

import android.app.Application.ActivityLifecycleCallbacks;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.util.Log;
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
import com.gethighlow.highlowandroid.model.ViewModels.DiaryViewModel;
import com.gethighlow.highlowandroid.model.util.Consumer;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class Diary extends Fragment {
    private Profile.OnFragmentInteractionListener mListener;

    private RecyclerView recyclerView;
    private DiaryAdapter adapter;
    private EndlessRecyclerViewScrollListener scrollListener;

    private SwipeRefreshLayout refreshLayout;

    private GridLayoutManager layoutManager;

    private DiaryViewModel viewModel;

    private TextView noDiary;
    private TextView title;
    private ImageView typeImage;

    private DiaryEntryClickListener diaryEntryClickListener = new DiaryEntryClickListener() {
        @Override
        public void onItemTapped(ActivityLiveData item) {

            //When an item is pressed, we want to open a new Reflect Editor window
            Intent reflectEditorIntent = new Intent( getContext(), ReflectEditor.class);

            //Set the type
            reflectEditorIntent.putExtra("type", item.getType() );

            //Set the activityId
            reflectEditorIntent.putExtra("activityId", item.getActivityId() );

            //Present the intent
            getContext().startActivity(reflectEditorIntent);

        }

        @Override
        public void onMoreButtonTapped(ActivityLiveData item) {

            //When a more button is pressed, we want to make an alert that gives them the appropriate options
            //Create the dialog builder
            AlertDialog.Builder builder = new AlertDialog.Builder( getContext() );

            //Set the title
            builder.setTitle("Options");

            //Create a sequence of options
            CharSequence options[] = new CharSequence[] { "Edit Permissions", "Delete" };

            //Use those options in the dialog
            builder.setItems(options, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    //If they selected "Edit Permissions"...
                    if ( options[i].toString().equals("Edit Permissions") ) {

                        showSharingOptions(item);

                    }

                    //If they selected "Delete"...
                    else if ( options[i].toString().equals("Delete") ) {

                        //Delete the item
                        Diary.this.viewModel.deleteEntry(item);

                    }

                }
            });

            //Now, get the dialog
            AlertDialog dialog = builder.create();

            //And finally, show the dialog
            dialog.show();

        }
    };

    private Observer<List<ActivityLiveData>> onEntriesUpdate = new Observer<List<ActivityLiveData>>() {
        @Override
        public void onChanged(List<ActivityLiveData> activityLiveData) {

            //When the entries list changes, we want to reload the adapter with the updated data
            adapter.setActivities(activityLiveData);

            //Finish refreshing
            refreshLayout.setRefreshing(false);

            //If the number of activities is greater than 0, don't show the "No diary entries" message; otherwise, show it
            if (activityLiveData.size() > 0) noDiary.setVisibility(View.GONE);
            else noDiary.setVisibility(View.VISIBLE);

        }
    };


    public Diary() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Inflate the diary layout
        View diaryLayoutView = inflater.inflate(R.layout.diary_fragment, container, false);

        //Get the recyclerView
        recyclerView = diaryLayoutView.findViewById(R.id.diaryRecyclerView);

        //Get the refresh layout
        refreshLayout = diaryLayoutView.findViewById(R.id.diary_refresher);

        //Instantiate the new diary view model
        viewModel = new ViewModelProvider(this).get( DiaryViewModel.class );

        //Get the entries and observe them
        viewModel.getEntries().observe(getViewLifecycleOwner(), onEntriesUpdate);

        //Create our layout manager
        layoutManager = new GridLayoutManager( getContext(), 2 );

        //Set the layout manager
        recyclerView.setLayoutManager(layoutManager);

        //Create the adapter
        adapter = new DiaryAdapter(this, null, diaryEntryClickListener );

        //Set the adapter
        recyclerView.setAdapter(adapter);

        //Create an onScrollListener
        scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {

                //Load the next page
                viewModel.loadEntries(page);

            }
        };

        //Set the onScrollListener
        recyclerView.addOnScrollListener(scrollListener);

        //Set the onRefreshListener
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Diary.this.viewModel.loadEntries(0);
            }
        });

        //Get the view that tells us if we don't have any diary entries
        noDiary = diaryLayoutView.findViewById(R.id.no_diary_notifier);

        //Return the layout view
        return diaryLayoutView;
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

    private void showSharingOptions(ActivityLiveData activityLiveData) {

        //Create the intent
        Intent intent = new Intent(getContext(), SharingOptionsActivity.class);

        //Add the activity id extra
        intent.putExtra("activityId", activityLiveData.getActivityId());

        //Present the sharing options activity
        startActivity(intent);


    }

    public void getDiaryEntries(int page) {

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



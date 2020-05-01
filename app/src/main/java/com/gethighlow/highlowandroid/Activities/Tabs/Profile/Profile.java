package com.gethighlow.highlowandroid.Activities.Tabs.Profile;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.gethighlow.highlowandroid.CustomViews.Sections.EndlessRecyclerViewScrollListener;
import com.gethighlow.highlowandroid.CustomViews.Adapters.ProfileAdapter;
import com.gethighlow.highlowandroid.R;
import com.gethighlow.highlowandroid.model.Services.SetActivityTheme;
import com.gethighlow.highlowandroid.model.util.Consumer;
import com.gethighlow.highlowandroid.model.Managers.LiveDataModels.HighLowLiveData;
import com.gethighlow.highlowandroid.model.Managers.LiveDataModels.UserLiveData;
import com.gethighlow.highlowandroid.model.Managers.UserManager;
import com.gethighlow.highlowandroid.model.Resources.User;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Profile.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class Profile extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private OnFragmentInteractionListener mListener;
    private RecyclerView recyclerView;
    private ProfileAdapter adapter;
    private List<HighLowLiveData> highLows = new ArrayList<HighLowLiveData>();
    private UserLiveData user;
    private EndlessRecyclerViewScrollListener scrollListener;
    private TextView notice;
    private SwipeRefreshLayout refreshLayout;
    private LinearLayoutManager layoutManager;

    private String uid = null;

    public Profile() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private void alert(String title, String message) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext(), R.style.AlertDialogCustom);
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setCancelable(true);
        alertDialog.setPositiveButton("OK", null);
        alertDialog.show();
    }

    public void setUser(String uid) {
        this.uid = uid;
    }

    private void getProfile() {
        scrollListener.resetState();
        highLows.clear();
        UserManager.shared().getUser(uid, new Consumer<UserLiveData>() {
            @Override
            public void accept(UserLiveData userLiveData) {
                if (userLiveData == null) return;
                user = userLiveData;
                user.observe(getViewLifecycleOwner(), userObserver);
                adapter.setUser(userLiveData);
                adapter.notifyDataSetChanged();

                getHighlows(0);
            }
        }, new Consumer<String>() {
            @Override
            public void accept(String error) {
                alert(getResources().getString(R.string.an_error_occurred), getResources().getString(R.string.please_try_again));
            }
        });
    }

    public void getHighlows(int page) {
        if (highLows.size() == 0 || page + 1 > highLows.size() / 10) {
            final int currentPos = highLows.size();
            user.getHighLows(page, new Consumer<List<HighLowLiveData>>() {
                @Override
                public void accept(List<HighLowLiveData> highLowLiveDataList) {
                    highLows.addAll(highLowLiveDataList);
                    adapter.notifyItemRangeInserted(currentPos + 1, 10);

                    if (highLows.size() == 0) {
                        notice.setVisibility(View.VISIBLE);
                    } else {
                        notice.setVisibility(View.GONE);
                    }
                    refreshLayout.setRefreshing(false);
                }
            }, new Consumer<String>() {
                @Override
                public void accept(String error) {
                    refreshLayout.setRefreshing(false);
                    Profile.this.alert(getResources().getString(R.string.an_error_occurred), getResources().getString(R.string.please_try_again));
                }
            });
        }
    }

    private Observer<User> userObserver = new Observer<User>() {
        @Override
        public void onChanged(User user) {
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.profile_fragment, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        refreshLayout = view.findViewById(R.id.refresher);
        refreshLayout.setColorSchemeColors(Color.RED);
        notice = view.findViewById(R.id.notice);

        layoutManager = new LinearLayoutManager(getContext());

        scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if (user == null) {
                    getProfile();
                } else {
                    getHighlows(page);
                }
            }
        };

        recyclerView.setLayoutManager(layoutManager);
        adapter = new ProfileAdapter(this, user, highLows);
        recyclerView.setAdapter(adapter);
        Drawable divider = getResources().getDrawable(getHighLowDivider());
        DividerItemDecoration decoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        decoration.setDrawable(divider);
        recyclerView.addItemDecoration(decoration);

        recyclerView.addOnScrollListener(scrollListener);

        refreshLayout.setOnRefreshListener(this);

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

    @Override
    public void onRefresh() {
        getProfile();
    }

    public interface OnFragmentInteractionListener {
        void onProfileFragmentInteraction(Uri uri);
    }


    private int getHighLowDivider(){
        String theme = SetActivityTheme.getTheme(getContext());
        if(theme.equals("light")){
            return R.drawable.highlow_divider;
        }else{
            return R.drawable.highlow_divider_dark;
        }

    }
}

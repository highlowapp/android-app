package com.gethighlow.highlowandroid.Activities;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.gethighlow.highlowandroid.CustomViews.EndlessRecyclerViewScrollListener;
import com.gethighlow.highlowandroid.CustomViews.ProfileAdapter;
import com.gethighlow.highlowandroid.R;
import com.gethighlow.highlowandroid.model.Managers.HighLowManager;
import com.gethighlow.highlowandroid.model.Managers.ImageManager;
import com.gethighlow.highlowandroid.model.Managers.LiveDataModels.HighLowLiveData;
import com.gethighlow.highlowandroid.model.Managers.LiveDataModels.UserLiveData;
import com.gethighlow.highlowandroid.model.Managers.UserManager;
import com.gethighlow.highlowandroid.model.Resources.User;
import com.gethighlow.highlowandroid.model.Services.AuthService;

import org.w3c.dom.Text;

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
    private List<HighLowLiveData> highLows = new ArrayList<>();
    private UserLiveData user;
    private EndlessRecyclerViewScrollListener scrollListener;
    private TextView notice;
    private SwipeRefreshLayout refreshLayout;

    String uid = AuthService.shared().getUid();

    public Profile() {
        // Required empty public constructor
    }

    private void alert(String title, String message) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setCancelable(true);
        alertDialog.setPositiveButton("OK", null);
        alertDialog.show();
    }
    private void alert(String title, String message, Runnable onComplete) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setCancelable(true);
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                onComplete.run();
            }
        });
        alertDialog.show();
    }

    public void setUser(String uid) {
        this.uid = uid;
    }

    public void getProfile() {
        scrollListener.resetState();
        highLows.clear();
        UserManager.shared().getUser(uid, userLiveData -> {
            user = userLiveData;
            user.observe(this, userObserver);
            adapter.setUser(userLiveData);
            adapter.notifyDataSetChanged();

            getHighlows(0);
        }, error -> {
            alert("An error occurred", "Please try again");
        });
    }

    public void getHighlows(int page) {
        if (highLows.size() == 0 || page + 1 > highLows.size() / 10) {
            int currentPos = highLows.size();
            user.getHighLows(page, highLowLiveDataList -> {
                highLows.addAll(highLowLiveDataList);
                adapter.notifyItemRangeInserted(currentPos + 1, 10);

                if (highLows.size() == 0) {
                    notice.setVisibility(View.VISIBLE);
                } else {
                    notice.setVisibility(View.GONE);
                }
                refreshLayout.setRefreshing(false);
            }, error -> {
                refreshLayout.setRefreshing(false);
                alert("An error occurred", "Please try again");
            });
        }
    }

    private Observer<User> userObserver = user -> {
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

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());

        scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                getHighlows(page);
            }
        };

        recyclerView.setLayoutManager(layoutManager);
        adapter = new ProfileAdapter(this, user, highLows);
        recyclerView.setAdapter(adapter);
        Drawable divider = getResources().getDrawable(R.drawable.highlow_divider, null);
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
}

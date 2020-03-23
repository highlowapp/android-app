package com.gethighlow.highlowandroid.Activities.Tabs.Feed;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.gethighlow.highlowandroid.CustomViews.Sections.EndlessRecyclerViewScrollListener;
import com.gethighlow.highlowandroid.CustomViews.Adapters.FeedAdapter;
import com.gethighlow.highlowandroid.R;
import com.gethighlow.highlowandroid.model.util.Consumer;
import com.gethighlow.highlowandroid.model.Managers.LiveDataModels.HighLowLiveData;
import com.gethighlow.highlowandroid.model.Resources.User;

import java.util.ArrayList;
import java.util.List;

public class Feed extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private RecyclerView recyclerView;
    private List<HighLowLiveData> highlows = new ArrayList<HighLowLiveData>();
    private FeedAdapter adapter;
    private EndlessRecyclerViewScrollListener scrollListener;
    private TextView notice;
    private SwipeRefreshLayout refreshLayout;

    public Feed() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.feed_fragment, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        refreshLayout = view.findViewById(R.id.refresher);
        notice = view.findViewById(R.id.notice);

        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setColorSchemeColors(Color.RED);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());

        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                getFeed(page);
            }
        };

        adapter = new FeedAdapter(this, highlows);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(scrollListener);

        Drawable divider = getResources().getDrawable(R.drawable.highlow_divider, null);
        DividerItemDecoration decoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        decoration.setDrawable(divider);
        recyclerView.addItemDecoration(decoration);

        getFeed(0);

        return view;
    }


    private void getFeed(int page) {
        final int currentPos = highlows.size();
        if (highlows.size() == 0 || page + 1 > highlows.size() / 10) {
            User.getFeed(page, new Consumer<List<HighLowLiveData>>() {
                @Override
                public void accept(List<HighLowLiveData> highLowLiveData) {
                    refreshLayout.setRefreshing(false);
                    highlows.addAll(highLowLiveData);
                    adapter.notifyItemRangeInserted(currentPos + 1, 10);

                    if (highlows.size() == 0) {
                        notice.setVisibility(View.VISIBLE);
                    } else {
                        notice.setVisibility(View.GONE);
                    }
                }
            }, new Consumer<String>() {
                @Override
                public void accept(String error) {
                    refreshLayout.setRefreshing(false);
                }
            });
        } else {
            refreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onRefresh() {
        highlows.clear();
        scrollListener.resetState();
        getFeed(0);
    }
}

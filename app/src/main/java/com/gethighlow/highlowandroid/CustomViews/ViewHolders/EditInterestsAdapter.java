package com.gethighlow.highlowandroid.CustomViews.ViewHolders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.gethighlow.highlowandroid.CustomViews.ViewHolders.Delegates.InterestViewHolderDelegate;
import com.gethighlow.highlowandroid.R;
import com.gethighlow.highlowandroid.model.Resources.Interest;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class EditInterestsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Interest> interests;

    public EditInterestsHeaderViewHolder editInterestsHeaderViewHolder;

    private String search = "";

    private int HEADER = 0;
    private int INTEREST = 1;
    public InterestViewHolderDelegate delegate;

    private LifecycleOwner lifecycleOwner;

    public void udpateSearch(String newSearch) {
        search = newSearch;
        //notifyItemChanged(1);
    }

    public EditInterestsAdapter(LifecycleOwner lifecycleOwner, InterestViewHolderDelegate delegate, List<Interest> interests) {
        this.lifecycleOwner = lifecycleOwner;
        this.delegate = delegate;
        this.interests = interests;
    }

    @Override
    public int getItemViewType(int position) {
        return (position == 0) ? HEADER: INTEREST;
    }

    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NotNull ViewGroup viewGroup, int viewType) {
        View view;
        if (viewType == HEADER) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.edit_interests_header, viewGroup, false);

            editInterestsHeaderViewHolder = new EditInterestsHeaderViewHolder(view);
            return editInterestsHeaderViewHolder;
        }
        else {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.interest_row_item, viewGroup, false);
            InterestViewHolder interestViewHolder = new InterestViewHolder(view);
            interestViewHolder.delegate = delegate;
            return interestViewHolder;
        }
    }

    @Override
    public void onBindViewHolder(@NotNull RecyclerView.ViewHolder viewHolder, int position) {
        if (position == 0) {
            ((EditInterestsHeaderViewHolder) viewHolder).attachToLifecycleOwner(lifecycleOwner);
            return;
        }
        ((InterestViewHolder) viewHolder).attachToLifecycleOwner(lifecycleOwner);
        ((InterestViewHolder) viewHolder).setPosition(position);
        Interest interest;
        if (position == 1) {
            interest = new Interest(search);
        } else {
            interest = interests.get(position - 2);
        }
        ((InterestViewHolder) viewHolder).setInterest(interest);
    }

    @Override
    public int getItemCount() {
        return interests.size() + 2;
    }
}

package com.gethighlow.highlowandroid.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.gethighlow.highlowandroid.CustomViews.EditInterestsAdapter;
import com.gethighlow.highlowandroid.CustomViews.InterestViewHolderDelegate;
import com.gethighlow.highlowandroid.R;
import com.gethighlow.highlowandroid.model.Managers.UserManager;
import com.gethighlow.highlowandroid.model.Resources.Interest;
import com.gethighlow.highlowandroid.model.Resources.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EditInterestsActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, InterestViewHolderDelegate {
    private List<Interest> allInterests = new ArrayList<>();
    private List<Interest> interests = new ArrayList<>();
    private RecyclerView recyclerView;
    private EditInterestsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_interests_activity);

        getSupportActionBar().setTitle("Edit Interests");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.recyclerView);
        adapter = new EditInterestsAdapter(this, this, interests);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        getInterests();
    }

    private void getInterests() {
        User.getInterests(interestResponse -> {
            List<String> noGos = new ArrayList<>();
            for (Interest interest: interestResponse.getInterests()) {
                noGos.add(interest.getInterestid());
            }

            User.getAllInterests(interestResponse2 -> {
                interests.clear();
                allInterests.clear();
                for (Interest interest: interestResponse2.getInterests()) {
                    if (!noGos.contains(interest.getInterestid())) {
                        interests.add(interest);
                        allInterests.add(interest);
                    }
                }
                adapter.notifyDataSetChanged();
            }, error -> {
                alert("An error occurred", "Please try again");
            });
        }, error -> {
            alert("An error occurred", "Please try again");
        });
    }

    private void alert(String title, String message) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setCancelable(true);
        alertDialog.setPositiveButton("OK", null);
        alertDialog.show();
    }

    @Override
    protected void onStop() {
        super.onStop();

        UserManager.shared().getUser(null, userLiveData -> {
            userLiveData.setInterests(adapter.editInterestsHeaderViewHolder.interests);
        }, error -> {
            alert("An error occurred", "Please try again");
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_bar, menu);

        final MenuItem searchItem = menu.findItem(R.id.search_bar);
        final SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(this);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        interests.clear();
        for (Interest interest: allInterests) {
            if (interest.getName().startsWith(newText)) {
                interests.add(interest);
            }
        }
        adapter.udpateSearch(newText);
        adapter.notifyDataSetChanged();

        return true;
    }

    @Override
    public void addInterest(Interest interest, int position) {
        if (interest.getInterestid() != null) {
            User.addInterest(interest.getInterestid(), genericResponse -> {
                interests.remove(interest);
                adapter.notifyItemRemoved(position);
                adapter.notifyItemRangeChanged(position, interests.size());
                adapter.editInterestsHeaderViewHolder.getUserInterests();
            }, error -> {
                alert("An error occurred", "Please try again");
            });
        } else {
            User.createInterest(interest.getName(), genericResponse -> {
                interests.remove(interest);
                adapter.notifyItemRemoved(position);
                adapter.notifyItemRangeChanged(position, interests.size());
                adapter.editInterestsHeaderViewHolder.getUserInterests();
            }, error -> {
                alert("An error occurred", "Please try again");
            });
        }
    }
}

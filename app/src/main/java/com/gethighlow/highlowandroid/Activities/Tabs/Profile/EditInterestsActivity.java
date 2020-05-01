package com.gethighlow.highlowandroid.Activities.Tabs.Profile;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gethighlow.highlowandroid.CustomViews.ViewHolders.EditInterestsAdapter;
import com.gethighlow.highlowandroid.CustomViews.ViewHolders.Delegates.InterestViewHolderDelegate;
import com.gethighlow.highlowandroid.R;
import com.gethighlow.highlowandroid.model.Services.SetActivityTheme;
import com.gethighlow.highlowandroid.model.util.Consumer;
import com.gethighlow.highlowandroid.model.Managers.LiveDataModels.UserLiveData;
import com.gethighlow.highlowandroid.model.Managers.UserManager;
import com.gethighlow.highlowandroid.model.Resources.Interest;
import com.gethighlow.highlowandroid.model.Resources.User;
import com.gethighlow.highlowandroid.model.Responses.GenericResponse;
import com.gethighlow.highlowandroid.model.Responses.InterestResponse;

import java.util.ArrayList;
import java.util.List;

public class EditInterestsActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, InterestViewHolderDelegate {
    private List<Interest> allInterests = new ArrayList<Interest>();
    private List<Interest> interests = new ArrayList<Interest>();
    private RecyclerView recyclerView;
    private EditInterestsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String theme = SetActivityTheme.getTheme(getApplicationContext());
        if(theme.equals("light")){
            setTheme(R.style.LightTheme);
        }else{
            setTheme(R.style.DarkTheme);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_interests_activity);

        getSupportActionBar().setTitle("Edit Interests");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.recyclerView);
        adapter = new EditInterestsAdapter(this, this, interests);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(themeReceiver, new IntentFilter("theme-updated"));


        getInterests();
    }

    private void getInterests() {
        User.getInterests(new Consumer<InterestResponse>() {
            @Override
            public void accept(InterestResponse interestResponse) {
                final List<String> noGos = new ArrayList<String>();
                for (Interest interest : interestResponse.getInterests()) {
                    noGos.add(interest.getInterestid());
                }

                User.getAllInterests(new Consumer<InterestResponse>() {
                    @Override
                    public void accept(InterestResponse interestResponse2) {
                        interests.clear();
                        allInterests.clear();
                        for (Interest interest : interestResponse2.getInterests()) {
                            if (!noGos.contains(interest.getInterestid())) {
                                interests.add(interest);
                                allInterests.add(interest);
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }
                }, new Consumer<String>() {
                    @Override
                    public void accept(String error) {
                        EditInterestsActivity.this.alert(getResources().getString(R.string.an_error_occurred), getResources().getString(R.string.please_try_again));
                    }
                });
            }
        }, new Consumer<String>() {
            @Override
            public void accept(String error) {
                EditInterestsActivity.this.alert(getResources().getString(R.string.an_error_occurred), getResources().getString(R.string.please_try_again));
            }
        });
    }

    private void alert(String title, String message) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this, R.style.AlertDialogCustom);
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setCancelable(true);
        alertDialog.setPositiveButton("OK", null);
        alertDialog.show();
    }

    @Override
    protected void onStop() {
        super.onStop();

        UserManager.shared().getUser(null, new Consumer<UserLiveData>() {
            @Override
            public void accept(UserLiveData userLiveData) {
                userLiveData.setInterests(adapter.editInterestsHeaderViewHolder.interests);
            }
        }, new Consumer<String>() {
            @Override
            public void accept(String error) {
                EditInterestsActivity.this.alert(getResources().getString(R.string.an_error_occurred), getResources().getString(R.string.please_try_again));
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent result = new Intent();
            setResult(Activity.RESULT_OK, result);
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
    public void addInterest(final Interest interest, final int position) {
        if (interest.getInterestid() != null) {
            User.addInterest(interest.getInterestid(), new Consumer<GenericResponse>() {
                @Override
                public void accept(GenericResponse genericResponse) {
                    interests.remove(interest);
                    adapter.notifyItemRemoved(position);
                    adapter.notifyItemRangeChanged(position, interests.size());
                    adapter.editInterestsHeaderViewHolder.getUserInterests();
                }
            }, new Consumer<String>() {
                @Override
                public void accept(String error) {
                    EditInterestsActivity.this.alert(getResources().getString(R.string.an_error_occurred), getResources().getString(R.string.please_try_again));
                }
            });
        } else {
            User.createInterest(interest.getName(), new Consumer<GenericResponse>() {
                @Override
                public void accept(GenericResponse genericResponse) {
                    interests.remove(interest);
                    adapter.notifyItemRemoved(position);
                    adapter.notifyItemRangeChanged(position, interests.size());
                    adapter.editInterestsHeaderViewHolder.getUserInterests();
                }
            }, new Consumer<String>() {
                @Override
                public void accept(String error) {
                    EditInterestsActivity.this.alert(getResources().getString(R.string.an_error_occurred), getResources().getString(R.string.please_try_again));
                }
            });
        }
    }

    private BroadcastReceiver themeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String currentTheme = SetActivityTheme.getTheme(context);
            if(currentTheme.equals("light")) {
                setTheme(R.style.LightTheme);
            } else if(currentTheme.equals("dark")){
                setTheme(R.style.DarkTheme);
            }

        }
    };

}

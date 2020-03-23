package com.gethighlow.highlowandroid.Activities.Tabs.Profile;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gethighlow.highlowandroid.CustomViews.Adapters.AddFriendsAdapter;
import com.gethighlow.highlowandroid.CustomViews.ViewHolders.Delegates.UserRowViewHolderDelegate;
import com.gethighlow.highlowandroid.R;
import com.gethighlow.highlowandroid.model.util.Consumer;
import com.gethighlow.highlowandroid.model.Managers.LiveDataModels.UserLiveData;
import com.gethighlow.highlowandroid.model.Managers.UserManager;
import com.gethighlow.highlowandroid.model.Responses.GenericResponse;
import com.gethighlow.highlowandroid.model.Services.AuthService;

import java.util.ArrayList;
import java.util.List;

public class AddFriendsActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, UserRowViewHolderDelegate {

    private TextView header;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    private List<UserLiveData> users = new ArrayList<UserLiveData>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_friends_activity);

        getSupportActionBar().setTitle("Add Friends");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        header = findViewById(R.id.header);
        recyclerView = findViewById(R.id.recyclerView);

        adapter = new AddFriendsAdapter(this, this, users);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        getMutual();
    }

    private void alert(String title, String message) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setCancelable(true);
        alertDialog.setPositiveButton("OK", null);
        alertDialog.show();
    }

    private void getMutual() {
        header.setVisibility(View.VISIBLE);
        String uid = AuthService.shared().getUid();

        UserManager.shared().getUser(uid, new Consumer<UserLiveData>() {
            @Override
            public void accept(UserLiveData userLiveData) {

                userLiveData.getFriendSuggestions(new Consumer<List<UserLiveData>>() {
                    @Override
                    public void accept(List<UserLiveData> friends) {
                        users.clear();
                        users.addAll(friends);
                        adapter.notifyDataSetChanged();
                    }
                }, new Consumer<String>() {
                    @Override
                    public void accept(String error) {
                        AddFriendsActivity.this.alert("An error has occurred", "Please try again");
                    }
                });

            }
        }, new Consumer<String>() {
            @Override
            public void accept(String error) {
                AddFriendsActivity.this.alert("An error has occurred", "Please try again");
            }
        });
    }

    private void search(final String query) {
        header.setVisibility(View.GONE);

        String uid = AuthService.shared().getUid();

        UserManager.shared().getUser(uid, new Consumer<UserLiveData>() {
            @Override
            public void accept(UserLiveData userLiveData) {

                userLiveData.searchUsers(query, new Consumer<List<UserLiveData>>() {
                    @Override
                    public void accept(List<UserLiveData> friends) {
                        users.clear();
                        users.addAll(friends);
                        adapter.notifyDataSetChanged();
                    }
                }, new Consumer<String>() {
                    @Override
                    public void accept(String error) {
                        AddFriendsActivity.this.alert("An error has occurred", "Please try again");
                    }
                });

            }
        }, new Consumer<String>() {
            @Override
            public void accept(String error) {
                AddFriendsActivity.this.alert("An error has occurred", "Please try again");
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_bar, menu);

        final MenuItem searchItem = menu.findItem(R.id.search_bar);
        final SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(this);

        return true;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        if (query.length() == 0) {
            getMutual();
        }
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if (query.length() > 0) {
            search(query);
        }
        else {
            getMutual();
        }
        return false;
    }

    @Override
    public void unFriend(int position) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void addFriend(int position) {
        final UserLiveData user = users.get(position);
        user.requestFriendship(new Consumer<GenericResponse>() {
            @Override
            public void accept(GenericResponse genericResponse) {
                Toast.makeText(AddFriendsActivity.this, "Requested Friendship from " + user.getUser().name(), Toast.LENGTH_SHORT).show();
            }
        }, new Consumer<String>() {
            @Override
            public void accept(String error) {
                AddFriendsActivity.this.alert("An error has occurred", "Please try again");
            }
        });

        users.remove(position);
        adapter.notifyDataSetChanged();
    }
}

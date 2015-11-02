package com.codepath.instagram.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.instagram.R;
import com.codepath.instagram.activities.HomeActivity;
import com.codepath.instagram.adapters.SearchUserResultsAdapter;
import com.codepath.instagram.helpers.Utils;
import com.codepath.instagram.models.InstagramUser;
import com.codepath.instagram.networking.InstagramClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by lujiawang on 10/31/15.
 */
public class SearchUsersResultFragment extends Fragment {
    private static final String TAG = "SearchUsersResult";
    private ArrayList<InstagramUser> users;
    private SearchUserResultsAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        users = new ArrayList<InstagramUser>();
        // Create Adapter
        adapter = new SearchUserResultsAdapter(users);

        // setHasOptionsMenu(true);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_users, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        // Get RecyclerView Reference
        RecyclerView rvSearchUsers = (RecyclerView) view.findViewById(R.id.rvSearchUsers);

        // Set Adapter
        rvSearchUsers.setAdapter(adapter);

        // Set Layout
        rvSearchUsers.setLayoutManager(new LinearLayoutManager(view.getContext()));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Fetch search users result
                getSearchUsersResult(query);
                // Reset SearchView
                searchView.clearFocus();
                searchView.setQuery("", false);
                searchView.setIconified(true);
                searchItem.collapseActionView();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void getSearchUsersResult(String queryItem) {
        Log.i(TAG, "query item " + queryItem);
        // get search users results
        InstagramClient client = HomeActivity.getRestClient();
        client.getSearchUsersResult(queryItem, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.i(TAG, Integer.toString(statusCode));
                users.clear();
                users.addAll(Utils.decodeUsersFromJsonResponse(response));
                Log.i(TAG, "get result size = " + users.size());
                Log.i(TAG, "first user " + users.get(0).userName);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e(TAG, Integer.toString(statusCode));
                Log.e(TAG, responseString);
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }
}

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
import com.codepath.instagram.adapters.SearchTagResultsAdapter;
import com.codepath.instagram.helpers.Utils;
import com.codepath.instagram.models.InstagramSearchTag;
import com.codepath.instagram.networking.InstagramClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by lujiawang on 11/1/15.
 */
public class SearchTagsResultFragment extends Fragment {
    private static final String TAG = "SearchTagsResult";
    private ArrayList<InstagramSearchTag> tags;
    private SearchTagResultsAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tags = new ArrayList<InstagramSearchTag>();
        // Create Adapter
        adapter = new SearchTagResultsAdapter(tags);

        // setHasOptionsMenu(true);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_tags, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        // Get RecyclerView Reference
        RecyclerView rvSearchTags = (RecyclerView) view.findViewById(R.id.rvSearchTags);

        // Set Adapter
        rvSearchTags.setAdapter(adapter);

        // Set Layout
        rvSearchTags.setLayoutManager(new LinearLayoutManager(view.getContext()));
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
                getSearchTagssResult(query);
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

    private void getSearchTagssResult(String queryItem) {
        Log.i(TAG, "query item " + queryItem);
        // get search users results
        InstagramClient client = HomeActivity.getRestClient();
        client.getSearchTagsResult(queryItem, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.i(TAG, Integer.toString(statusCode));
                tags.clear();
                tags.addAll(Utils.decodeSearchTagsFromJsonResponse(response));
                Log.i(TAG, "get result size = " + tags.size());
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

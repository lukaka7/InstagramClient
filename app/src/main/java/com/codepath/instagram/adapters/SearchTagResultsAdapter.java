package com.codepath.instagram.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codepath.instagram.R;
import com.codepath.instagram.helpers.Utils;
import com.codepath.instagram.models.InstagramSearchTag;

import java.util.List;

/**
 * Created by lujiawang on 11/1/15.
 */
public class SearchTagResultsAdapter extends RecyclerView.Adapter<SearchTagResultsAdapter.SearchTagViewHolder> {
    private static final String TAG = "SearchTagResultsAdapter";

    private List<InstagramSearchTag> tags;
    private Context context;

    public SearchTagResultsAdapter(List<InstagramSearchTag> tags) {
        this.tags = tags;
    }

    @Override
    public SearchTagViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        this.context = context;
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View searchTagView = inflater.inflate(R.layout.layout_item_tag_search, parent, false);

        SearchTagViewHolder searchTagViewHolder = new SearchTagViewHolder(searchTagView);
        return searchTagViewHolder;
    }

    @Override
    public void onBindViewHolder(SearchTagViewHolder holder, int position) {
        InstagramSearchTag tag = tags.get(position);

        holder.tvTagName.setText("#" + tag.tag);
        holder.tvPostsNumber.setText(Utils.formatNumberForDisplay(tag.count) + " posts");
    }

    @Override
    public int getItemCount() {
        return tags.size();
    }

    public static class SearchTagViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTagName;
        public TextView tvPostsNumber;

        public SearchTagViewHolder(View layoutView) {
            super(layoutView);
            tvTagName = (TextView) layoutView.findViewById(R.id.tvTagName);
            tvPostsNumber = (TextView) layoutView.findViewById(R.id.tvPostsNumber);
        }
    }
}

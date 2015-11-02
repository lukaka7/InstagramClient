package com.codepath.instagram.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.codepath.instagram.fragments.SearchTagsResultFragment;
import com.codepath.instagram.fragments.SearchUsersResultFragment;
import com.codepath.instagram.helpers.SmartFragmentStatePagerAdapter;

/**
 * Created by lujiawang on 11/1/15.
 */
public class SearchFragmentStatePagerAdapter extends SmartFragmentStatePagerAdapter {
    private static final String TAG = "SearchPagerAdapter";
    private static int NUM_ITEMS = 2;
    private Context context;

    public SearchFragmentStatePagerAdapter(FragmentManager fragmentManager, Context context) {
        super(fragmentManager);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new SearchUsersResultFragment();
            case 1:
                return new SearchTagsResultFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "USERS";
            case 1:
                return "TAGS";
            default:
                return "USERS";
        }
    }
}

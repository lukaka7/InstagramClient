package com.codepath.instagram.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.instagram.R;
import com.codepath.instagram.adapters.SearchFragmentStatePagerAdapter;

/**
 * Created by lujiawang on 11/1/15.
 */
public class SearchFragment extends Fragment {
    private SearchFragmentStatePagerAdapter pagerAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ViewPager vpPager = (ViewPager) view.findViewById(R.id.vpSearchPager);
        pagerAdapter = new SearchFragmentStatePagerAdapter(getFragmentManager(), view.getContext());
        vpPager.setAdapter(pagerAdapter);

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tlSearchTabs);
        tabLayout.setupWithViewPager(vpPager);
    }
}

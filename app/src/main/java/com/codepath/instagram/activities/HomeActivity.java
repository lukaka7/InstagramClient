package com.codepath.instagram.activities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.codepath.instagram.R;
import com.codepath.instagram.adapters.HomeFragmentStatePagerAdapter;
import com.codepath.instagram.networking.InstagramClient;
import com.facebook.drawee.backends.pipeline.Fresco;

import static com.codepath.instagram.core.MainApplication.sharedApplication;

public class HomeActivity extends AppCompatActivity {
    private HomeFragmentStatePagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Fresco.initialize(this);
        setContentView(R.layout.activity_home);

        // Find the toolbar view inside the activity layout
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView tvToolbarTitle = (TextView) findViewById(R.id.tvToolbarTitle);

        ViewPager vpPager = (ViewPager) findViewById(R.id.vpPager);
        pagerAdapter = new HomeFragmentStatePagerAdapter(getSupportFragmentManager(), this);
        vpPager.setAdapter(pagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tlTabs);
        tabLayout.setupWithViewPager(vpPager);
    }

    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    public static InstagramClient getRestClient() {
        return (InstagramClient) InstagramClient.getInstance(InstagramClient.class, sharedApplication());
    }
}

package com.codepath.instagram.activities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

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

        if (!isNetworkAvailable()) {
            // Popup a dialog
            Toast.makeText(this, "Network is not available! Please check your network setting!", Toast.LENGTH_SHORT).show();
            return;
        }

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

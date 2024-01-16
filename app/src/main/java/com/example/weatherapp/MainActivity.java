package com.example.weatherapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.weatherapp.Fragments.PageAdapter;

public class MainActivity extends AppCompatActivity {

    private static final String CURRENT_FRAGMENT_INDEX_KEY = "current_fragment_index";

    private ViewPager2 viewPager;
    private PageAdapter pageAdapter;
    private int currentFragmentIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen_port);

        viewPager = findViewById(R.id.viewPager);

        // Check if savedInstanceState is not null, which means the activity is being recreated (e.g., orientation change)
        if (savedInstanceState != null) {
            currentFragmentIndex = savedInstanceState.getInt(CURRENT_FRAGMENT_INDEX_KEY, 0);
        }

        // Set up the fragments
        setupViewPager();

        // Set the current item to the saved index
        viewPager.setCurrentItem(currentFragmentIndex, false);
    }

    public void openSettings(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
    public void closeSettings(View view) {
        if (getResources().getConfiguration().orientation == android.content.res.Configuration.ORIENTATION_PORTRAIT) {
            setContentView(R.layout.main_screen_port);
        }
        else {
            setContentView(R.layout.main_screen_land);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(CURRENT_FRAGMENT_INDEX_KEY, viewPager.getCurrentItem());
    }

    private void setupViewPager() {
        ViewPager2 viewPager = findViewById(R.id.viewPager);
        PageAdapter pageAdapter = new PageAdapter(this);
        viewPager.setAdapter(pageAdapter);
    }
}
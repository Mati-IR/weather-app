package com.example.weatherapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

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
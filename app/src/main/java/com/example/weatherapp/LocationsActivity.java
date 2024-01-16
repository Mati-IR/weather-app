package com.example.weatherapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class LocationsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // if orientation is portrait
        if (getResources().getConfiguration().orientation == android.content.res.Configuration.ORIENTATION_PORTRAIT)
            setContentView(R.layout.locations_screen_port);
        else {
            // if orientation is landscape
            setContentView(R.layout.locations_screen_land);
        }

        // Your locations activity initialization code goes here
    }
}

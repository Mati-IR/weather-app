package com.example.weatherapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_screen_port);
    }

    // handle locations button click
    public void onLocationsButtonClick(android.view.View view) {
        Intent intent = new Intent(this, LocationsActivity.class);
        startActivity(intent);
    }
}

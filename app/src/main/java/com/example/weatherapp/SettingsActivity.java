package com.example.weatherapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import com.example.weatherapp.GlobalVariables.Units;
import com.example.weatherapp.GlobalVariables;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_screen_port);
    }

    public void onLocationsButtonClick(android.view.View view) {
        Intent intent = new Intent(this, LocationsActivity.class);
        startActivity(intent);
    }

    public void onUnitsSwitchClick(android.view.View view) {
        if (GlobalVariables.getUnits().equals(Units.METRIC)) {
            GlobalVariables.setUnits(GlobalVariables.Units.METRIC);
        } else {
            GlobalVariables.setUnits(GlobalVariables.Units.IMPERIAL);
        }
    }
}

package com.example.weatherapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.rxjava3.RxPreferenceDataStoreBuilder;
import androidx.datastore.rxjava3.RxDataStore;

import com.example.weatherapp.GlobalVariables.Units;
import com.example.weatherapp.GlobalVariables;

public class SettingsActivity extends AppCompatActivity {

    private DataStorage dataStorage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_screen_port);
        dataStorage = new DataStorage();

        // Retrieve the current setting from shared preferences
        boolean useImperialUnits = dataStorage.getUseImperialUnits(this);

        // Find the switch in the layout
        SwitchCompat switchUnits = findViewById(R.id.textUnits);

        // Set the state of the switch based on the retrieved setting
        switchUnits.setChecked(useImperialUnits);

        // Set a listener on the switch to handle changes
        switchUnits.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Save the new setting to shared preferences
                dataStorage.saveUseImperialUnits(SettingsActivity.this, isChecked);

                // Update global variables
                GlobalVariables.setUnits(isChecked ? Units.IMPERIAL : Units.METRIC);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Retrieve the current setting from shared preferences
        boolean useImperialUnits = dataStorage.getUseImperialUnits(this);

        // Set the state of the switch based on the retrieved setting
        SwitchCompat switchUnits = findViewById(R.id.textUnits);
        switchUnits.setChecked(useImperialUnits);
    }

    public void onLocationsButtonClick(android.view.View view) {
        Intent intent = new Intent(this, LocationsActivity.class);
        startActivity(intent);
    }

    public void onClickImperialUnitsSwitch(android.view.View view) {
        // save to preferences
        SwitchCompat unitsSwitch = (SwitchCompat) view;
        boolean useImperialUnits = unitsSwitch.isChecked();

        dataStorage.saveUseImperialUnits(this, useImperialUnits);

        // update global variables
        boolean useMetricUnits = !useImperialUnits;
        GlobalVariables.setUnits(useMetricUnits ? Units.METRIC : Units.IMPERIAL);
    }


}

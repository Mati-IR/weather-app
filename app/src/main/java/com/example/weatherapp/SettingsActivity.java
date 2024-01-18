package com.example.weatherapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.example.weatherapp.GlobalVariables.Units;
import com.example.weatherapp.GlobalVariables;

public class SettingsActivity extends AppCompatActivity {
    public class DataStorage {
        private static final String PREFS_NAME = "preferences";
        private static final String KEY_USE_IMPERIAL_UNITS = "use_imperial_units";
        public DataStorage() {
            //RxDataStore<Preferences> dataStore = new RxPreferenceDataStoreBuilder(context, /*name=*/ "settings").build();
        }
        public boolean addLocationToStorage(String location) {
            return true;
        }

        public boolean saveUseImperialUnits(boolean useImperialUnits) {
            SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(KEY_USE_IMPERIAL_UNITS, String.valueOf(useImperialUnits));
            editor.apply();
            return true;
        }

        public boolean getUseImperialUnits() {
            SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
            String useImperialUnits = preferences.getString(KEY_USE_IMPERIAL_UNITS, "false");
            return Boolean.parseBoolean(useImperialUnits);
        }
    }

    private DataStorage dataStorage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_screen_port);
        dataStorage = new DataStorage();

        // Retrieve the current setting from shared preferences
        boolean useImperialUnits = dataStorage.getUseImperialUnits();

        // Find the switch in the layout
        SwitchCompat switchUnits = findViewById(R.id.textUnits);

        // Set the state of the switch based on the retrieved setting
        switchUnits.setChecked(useImperialUnits);

        // Set a listener on the switch to handle changes
        switchUnits.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Save the new setting to shared preferences
                dataStorage.saveUseImperialUnits(isChecked);

                // Update global variables
                GlobalVariables.setUnits(isChecked ? Units.IMPERIAL : Units.METRIC);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Retrieve the current setting from shared preferences
        boolean useImperialUnits = dataStorage.getUseImperialUnits();

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

        dataStorage.saveUseImperialUnits(useImperialUnits);

        // update global variables
        boolean useMetricUnits = !useImperialUnits;
        GlobalVariables.setUnits(useMetricUnits ? Units.METRIC : Units.IMPERIAL);
    }


}

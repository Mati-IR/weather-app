package com.example.weatherapp;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.rxjava3.RxPreferenceDataStoreBuilder;
import androidx.datastore.rxjava3.RxDataStore;

public class DataStorage {
    private static final String PREFS_NAME = "preferences";
    private static final String KEY_USE_IMPERIAL_UNITS = "use_imperial_units";
    public DataStorage() {
        //RxDataStore<Preferences> dataStore = new RxPreferenceDataStoreBuilder(context, /*name=*/ "settings").build();
    }

    public int getMaxAmountOfLocations() {
        return 5;
    }

    private boolean saveAmountOfLocations(Context context, int amountOfLocations) {
        SharedPreferences preferences = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("amountOfLocations", String.valueOf(amountOfLocations));
        editor.apply();
        return true;
    }

    public int getAmountOfLocations(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String amountOfLocations = preferences.getString("amountOfLocations", "0");
        return Integer.parseInt(amountOfLocations);
    }

    public boolean saveUseImperialUnits(Context context, boolean useImperialUnits) {
        SharedPreferences preferences = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(KEY_USE_IMPERIAL_UNITS, String.valueOf(useImperialUnits));
        editor.apply();
        return true;
    }

    public boolean getUseImperialUnits(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String useImperialUnits = preferences.getString(KEY_USE_IMPERIAL_UNITS, "false");
        return Boolean.parseBoolean(useImperialUnits);
    }

    public boolean saveLocation(Context context, String city, String lat, String lon) {
        String location = city + ";" + lat + ";" + lon;
        int amountOfLocations = getAmountOfLocations(context);
        if (amountOfLocations < getMaxAmountOfLocations()) {
            SharedPreferences preferences = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("location" + amountOfLocations, location);
            editor.apply();
            saveAmountOfLocations(context, amountOfLocations + 1);
            return true;
        }
        return false;
    }

    public boolean removeLocation(Context context, String location) {
        int amountOfLocations = getAmountOfLocations(context);
        SharedPreferences preferences = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        for (int i = 0; i < amountOfLocations; i++) {
            String locationName = preferences.getString("location" + i, "");
            if (locationName.equals(location)) {
                editor.remove("location" + i);
                editor.apply();
                return true;
            }
        }
        return false;
    }

    public String getLocations(Context context) {

        int amountOfLocations = getAmountOfLocations(context);
        SharedPreferences preferences = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String locations = "";
        for (int i = 0; i < amountOfLocations; i++) {
            locations += preferences.getString("location" + i, "") + "\n";
        }
        return locations;
    }

    public boolean saveSelectedLocation(Context context, int location_index) {
        if (location_index < 0 || location_index >= getAmountOfLocations(context)) {
            return false;
        }
        SharedPreferences preferences = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("selected_location", String.valueOf(location_index));
        editor.apply();
        return true;
    }

    public int getSelectedLocation(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String selectedLocation = preferences.getString("selected_location", "0");
        return Integer.parseInt(selectedLocation);
    }
}
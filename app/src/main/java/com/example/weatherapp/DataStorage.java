package com.example.weatherapp;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;

import androidx.datastore.rxjava2.RxDataStore;
import androidx.datastore.preferences.rxjava3.RxPreferenceDataStoreBuilder;

import java.util.prefs.Preferences;

public class DataStorage {
    private static final String PREFS_NAME = "preferences";
    private static final String KEY_USE_IMPERIAL_UNITS = "use_imperial_units";
    public DataStorage() {
        //RxDataStore<Preferences> dataStore = new RxPreferenceDataStoreBuilder(context, /*name=*/ "settings").build();
    }
    public boolean addLocationToStorage(String location) {
        return true;
    }
}

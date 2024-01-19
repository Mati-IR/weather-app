package com.example.weatherapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;

import java.util.ArrayList;
import java.util.List;

public class LocationsActivity extends AppCompatActivity implements PlaceTestAdapter.ClickListener {
    private DataStorage dataStorage;

    EditText get_location_edit_text;
    RecyclerView locations_recycler_view;

    PlaceTestAdapter placeTestAdapter;

    private List<String> locations = new ArrayList<>(); // List to store locations

    LocationAdapter locationAdapter;

    MergedAdapter mergedAdapter;

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
        dataStorage = new DataStorage();

        locations_recycler_view = findViewById(R.id.locations_recycler_view);
        get_location_edit_text = findViewById(R.id.get_location_edit_text);


        Places.initialize(LocationsActivity.this, getResources().getString(R.string.google_maps_key));
        placeTestAdapter = new PlaceTestAdapter(this);
        get_location_edit_text.addTextChangedListener(getLocationTextWatcher());
        locations_recycler_view.setLayoutManager(new LinearLayoutManager(LocationsActivity.this));
        placeTestAdapter.setClickListener(this);

        // Add locations to the recycler view
        // get locations from DataStorage
        String locationsString[] = dataStorage.getLocations(this).split("\n");
        int amountOfLocations = dataStorage.getAmountOfLocations(this);
        for (int i = 0; i < amountOfLocations; i++) {
            locations.add(locationsString[i].split(";")[0]);
        }

        //get selected location
        int selectedLocation = dataStorage.getSelectedLocation(this);
        locationAdapter = new LocationAdapter(this, locations, selectedLocation, dataStorage, locations_recycler_view);

        this.mergedAdapter = new MergedAdapter(placeTestAdapter, locationAdapter, dataStorage);
        locations_recycler_view.setAdapter(locationAdapter);
        locationAdapter.notifyDataSetChanged();
    }

    // function triggered by get_location_edit_text, it is meant to change adapter to placeTestAdapter
    public void switchAdapter(View view) {
        locations_recycler_view.setAdapter(mergedAdapter);
        this.placeTestAdapter.notifyDataSetChanged();
        this.mergedAdapter.notifyDataSetChanged();
        this.locationAdapter.notifyDataSetChanged();
    }

    public TextWatcher getLocationTextWatcher() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Do nothing
            }

            @Override
            public void afterTextChanged(android.text.Editable s) {
                // if adapter is locationAdapter, change it to mergedAdapter
                if (locations_recycler_view.getAdapter() == locationAdapter) {
                    locations_recycler_view.setAdapter(mergedAdapter);
                }
                if (!s.toString().isEmpty()) {
                    placeTestAdapter.getFilter().filter(s.toString());
                    if (!(locations_recycler_view.getVisibility() == View.VISIBLE)) {
                        locations_recycler_view.setVisibility(android.view.View.VISIBLE);
                    }
                } else if (locations_recycler_view.getVisibility() == View.VISIBLE) {
                    locations_recycler_view.setVisibility(View.GONE);
                }

                // Check if the adapter is null before trying to update the RecyclerView
                if (locations_recycler_view.getAdapter() != null) {
                    locations_recycler_view.getAdapter().notifyDataSetChanged();
                }
            }
        };
    }

    @Override
    public void click(Place place) {
        String lat = String.valueOf(place.getLatLng().latitude);
        String log = String.valueOf(place.getLatLng().longitude);
        String name = String.valueOf(place.getName());

        // Save the new location to shared preferences
        dataStorage.saveLocation(this, name, lat, log);

        String debugGetLocations = dataStorage.getLocations(this);
        Toast.makeText(this, debugGetLocations, Toast.LENGTH_SHORT).show();
    }


}

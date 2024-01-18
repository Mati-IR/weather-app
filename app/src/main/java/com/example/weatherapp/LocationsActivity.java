package com.example.weatherapp;

import android.os.Bundle;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;

public class LocationsActivity extends AppCompatActivity implements PlaceTestAdapter.ClickListener {

    EditText get_location_edit_text;
    RecyclerView locations_recycler_view;

    PlaceTestAdapter placeTestAdapter;
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

        locations_recycler_view = findViewById(R.id.locations_recycler_view);
        get_location_edit_text = findViewById(R.id.get_location_edit_text);

        Places.initialize(LocationsActivity.this, getResources().getString(R.string.google_maps_key));
        placeTestAdapter = new PlaceTestAdapter(this);
        get_location_edit_text.addTextChangedListener(getLocationTextWatcher());
        locations_recycler_view.setLayoutManager(new LinearLayoutManager(LocationsActivity.this));
        placeTestAdapter.setClickListener(this);
        locations_recycler_view.setAdapter(placeTestAdapter);
        placeTestAdapter.notifyDataSetChanged();
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
                if (!s.toString().isEmpty()) {
                    placeTestAdapter.getFilter().filter(s.toString());
                    if ((locations_recycler_view.getVisibility() == View.GONE)) {
                        locations_recycler_view.setVisibility(android.view.View.VISIBLE);
                    }
                } else if (locations_recycler_view.getVisibility() == View.VISIBLE) {
                    locations_recycler_view.setVisibility(View.GONE);
                }
            }
        };
    }

    @Override
    public void click(Place place) {
        String lat = String.valueOf(place.getLatLng().latitude);
        String log = String.valueOf(place.getLatLng().longitude);

        Toast.makeText(this, "latitude: " + lat + "longtitude: " + log, Toast.LENGTH_SHORT).show();
    }
}

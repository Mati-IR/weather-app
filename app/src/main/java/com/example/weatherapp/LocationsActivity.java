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
        locations_recycler_view.setAdapter(placeTestAdapter);
        placeTestAdapter.notifyDataSetChanged();

        // Add locations to the recycler view
        // get locations from DataStorage
        String locationsString[] = dataStorage.getLocations(this).split("\n");
        int amountOfLocations = dataStorage.getAmountOfLocations(this);
        for (int i = 0; i < amountOfLocations; i++) {
            locations.add(locationsString[i].split(";")[0]);
        }

        //get selected location
        int selectedLocation = dataStorage.getSelectedLocation(this);
        locationAdapter = new LocationAdapter(locations, selectedLocation);

        // Set the LocationAdapter on the RecyclerView
        locations_recycler_view.setAdapter(locationAdapter);
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
        String name = String.valueOf(place.getName());

        // Save the new location to shared preferences
        dataStorage.saveLocation(this, name, lat, log);

        String debugGetLocations = dataStorage.getLocations(this);
        Toast.makeText(this, debugGetLocations, Toast.LENGTH_SHORT).show();
    }

    private class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.LocationViewHolder> {

        private List<String> locations;
        private int selectedLocation;
        LocationAdapter(List<String> locations, int selectedLocation) {
            this.locations = locations;
            this.selectedLocation = selectedLocation;
        }

        @NonNull
        @Override
        public LocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_saved_location_port, parent, false);
            return new LocationViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull LocationViewHolder holder, int position) {
            String location = locations.get(position);
            holder.bind(location);
        }

        @Override
        public int getItemCount() {
            return locations.size();
        }

        // ViewHolder class for each location item
        class LocationViewHolder extends RecyclerView.ViewHolder {

            private TextView locationTextView;
            private ImageView deleteImageView;
            private ImageView tickImageView;

            LocationViewHolder(@NonNull View itemView) {
                super(itemView);
                locationTextView = itemView.findViewById(R.id.locationTextView);
                deleteImageView = itemView.findViewById(R.id.deleteImageView);
                tickImageView = itemView.findViewById(R.id.tickImageView);
            }

            void bind(String location) {
                locationTextView.setText(location);

                // If this location is the selected one, show the tick
                if (getAdapterPosition() == selectedLocation) {
                    tickImageView.setVisibility(View.VISIBLE);
                } else {
                    tickImageView.setVisibility(View.INVISIBLE);
                }

                // Handle click on location item
                itemView.setOnClickListener(v -> {
                    // Perform your action when a location is chosen
                    // For example, show a green tick mark
                    tickImageView.setVisibility(View.VISIBLE);

                    int debug = dataStorage.getSelectedLocation(LocationsActivity.this);
                    int debugLayoutPosition = getLayoutPosition();
                    int debugAdapterPosition = getAdapterPosition();
                    dataStorage.saveSelectedLocation(LocationsActivity.this, getLayoutPosition());
                    debug = dataStorage.getSelectedLocation(LocationsActivity.this);
                    //untick other locations
                    for (int i = 0; i < locations.size(); i++) {
                        if (i != getAdapterPosition()) {
                            RecyclerView.ViewHolder viewHolder = locations_recycler_view.findViewHolderForAdapterPosition(i);
                            if (viewHolder != null) {
                                ImageView tickImageView = viewHolder.itemView.findViewById(R.id.tickImageView);
                                tickImageView.setVisibility(View.INVISIBLE);
                            }
                        }
                    }
                });

                // Handle click on delete icon
                deleteImageView.setOnClickListener(v -> {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        locations.remove(position);
                        notifyItemRemoved(position);
                    }
                });
            }
        }
    }
}

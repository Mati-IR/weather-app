package com.example.weatherapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.LocationViewHolder> {

    private List<String> locations;
    private int selectedLocation;
    private DataStorage dataStorage;
    private Context context;
    private RecyclerView locations_recycler_view;


    LocationViewHolder holder;
    LocationAdapter(Context context, List<String> locations, int selectedLocation, DataStorage dataStorage, RecyclerView locations_recycler_view) {
        this.context = context;
        this.locations = locations;
        this.selectedLocation = selectedLocation;
        this.dataStorage = dataStorage;
        this.locations_recycler_view = locations_recycler_view;
        this.holder = onCreateViewHolder(locations_recycler_view, 0);
        loadLocations();
    }

    private void loadLocations() {
        // Clear the current locations
        locations.clear();

        // Get locations from DataStorage
        String locationsString[] = dataStorage.getLocations(context).split("\n");
        int amountOfLocations = dataStorage.getAmountOfLocations(context);

        // Add locations to the list
        for (int i = 0; i < amountOfLocations; i++) {
            locations.add(locationsString[i].split(";")[0]);
        }

        // Notify the adapter that the data has changed
        notifyDataSetChanged();
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
            this.locationTextView = itemView.findViewById(R.id.locationTextView);
            this.deleteImageView = itemView.findViewById(R.id.deleteImageView);
            this.tickImageView = itemView.findViewById(R.id.tickImageView);
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

                int debug = dataStorage.getSelectedLocation(context);
                int debugLayoutPosition = getLayoutPosition();
                int debugAdapterPosition = getAdapterPosition();
                dataStorage.saveSelectedLocation(context, getLayoutPosition());
                debug = dataStorage.getSelectedLocation(context);
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
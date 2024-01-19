package com.example.weatherapp;

import android.content.Context;
import android.text.style.CharacterStyle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherapp.PlaceTestAdapter;
import com.example.weatherapp.R;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class MergedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public PlaceTestAdapter placeTestAdapter;
    public LocationAdapter locationAdapter;
    private DataStorage dataStorage;

    public MergedAdapter(PlaceTestAdapter placeTestAdapter, LocationAdapter locationAdapter, DataStorage dataStorage) {
        this.placeTestAdapter = placeTestAdapter;
        this.locationAdapter = locationAdapter;
        this.dataStorage = dataStorage;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0)
            return placeTestAdapter.onCreateViewHolder(parent, viewType);
        else
            return locationAdapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (position < placeTestAdapter.getItemCount()) {
            if (holder instanceof PlaceTestAdapter.PredictionHolder) {
                placeTestAdapter.onBindViewHolder((PlaceTestAdapter.PredictionHolder) holder, position);
            } else {
                // Handle invalid ViewHolder type
                Log.e("MergedAdapters", "Invalid ViewHolder type at position: " + position);
            }
        } else if (position - placeTestAdapter.getItemCount() < locationAdapter.getItemCount()) {
            if (holder instanceof LocationAdapter.LocationViewHolder) {
                locationAdapter.onBindViewHolder((LocationAdapter.LocationViewHolder) holder, position - placeTestAdapter.getItemCount());
            } else {
                // Handle invalid ViewHolder type
                Log.e("MergedAdapters", "Invalid ViewHolder type at position: " + position);
            }
        } else {
            // Handle invalid position
            Log.e("MergedAdapters", "Invalid position: " + position);
        }
    }


    @Override
    public int getItemCount() {
        return placeTestAdapter.getItemCount() + locationAdapter.getItemCount();
    }

}

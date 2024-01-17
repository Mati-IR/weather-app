package com.example.weatherapp;

import android.content.Context;
import android.graphics.Typeface;
import android.text.style.CharacterStyle;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class PlaceTestAdapter extends RecyclerView.Adapter<PlaceTestAdapter.PredictionHolder> implements Filterable {
        private ArrayList<PlaceAutocomplete> mResultList = new ArrayList<>();

        private Context mContext;
        private CharacterStyle STYLE_BOLD;
        private CharacterStyle STYLE_NORMAL;
        private final PlacesClient placesClient;
        private ClickListener clickListener;

        public PlaceTestAdapter(Context context) {
            mContext = context;
            STYLE_BOLD = new StyleSpan(Typeface.BOLD);
            STYLE_NORMAL = new StyleSpan(Typeface.NORMAL);
            placesClient = com.google.android.libraries.places.api.Places.createClient(context);
        }

        @NonNull
        @Override
        public PredictionHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
            LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View convertView = layoutInflater.inflate(R.layout.place_recycler_item_layout, viewGroup, false);
            return new PredictionHolder(convertView);
        }

        @Override
        public void onBindViewHolder(@NonNull PredictionHolder mPredictionHolder, final int position) {
            mPredictionHolder.address.setText(mResultList.get(position).address);
            mPredictionHolder.area.setText(mResultList.get(position).area);
        }

        @Override
        public Filter getFilter() {
            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults results = new FilterResults();
                    if (constraint != null) {
                        // Query the autocomplete API for the entered constraint
                        mResultList = myPlaceData(constraint);
                        if (mResultList != null) {
                            results.values = mResultList;
                            results.count = mResultList.size();
                        }
                    }
                    return results;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    if (results != null && results.count > 0) {
                        notifyDataSetChanged();
                    }
                }
            };
        }

        private ArrayList<PlaceAutocomplete> myPlaceData(CharSequence constraint) {
            final ArrayList<PlaceAutocomplete> resultList = new ArrayList<>();

            AutocompleteSessionToken token = AutocompleteSessionToken.newInstance();

            FindAutocompletePredictionsRequest request = FindAutocompletePredictionsRequest.builder()
                    .setSessionToken(token)
                    .setQuery(constraint.toString())
                    .build();

            Task<FindAutocompletePredictionsResponse> autocompletePredictions = placesClient.findAutocompletePredictions(request);

            try {
                Tasks.await(autocompletePredictions, 60, TimeUnit.SECONDS);
            } catch (ExecutionException | InterruptedException | TimeoutException e) {
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            // Return the size of your data set
        }

        public class PredictionHolder extends RecyclerView.ViewHolder {
            // Define your ViewHolder
        }
}

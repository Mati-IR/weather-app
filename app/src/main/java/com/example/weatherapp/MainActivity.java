package com.example.weatherapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.weatherapp.Fragments.PageAdapter;
import com.example.weatherapp.openweathermap.WeatherDataProvider;

import net.aksingh.owmjapis.api.APIException;
import net.aksingh.owmjapis.core.OWM;
import net.aksingh.owmjapis.model.CurrentWeather;
import net.aksingh.owmjapis.model.DailyWeatherForecast;
import net.aksingh.owmjapis.util.WeatherConditions;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private static final String CURRENT_FRAGMENT_INDEX_KEY = "current_fragment_index";

    private ViewPager2 viewPager;
    private PageAdapter pageAdapter;
    private int currentFragmentIndex = 0;

    private WeatherDataProvider weatherDataProvider;

    DailyWeatherForecast dailyForecast;
    CurrentWeather currentWeather = new CurrentWeather();

    DataStorage dataStorage = new DataStorage();

    public MainActivity() throws APIException, IOException {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen_port);
        weatherDataProvider = new WeatherDataProvider(this);
        //pageAdapter = new PageAdapter(this);
        // Set up the fragments
        setupViewPager();
        viewPager = findViewById(R.id.viewPager);

        // Check if savedInstanceState is not null, which means the activity is being recreated (e.g., orientation change)
        if (savedInstanceState != null) {
            currentFragmentIndex = savedInstanceState.getInt(CURRENT_FRAGMENT_INDEX_KEY, 0);
        }


        // Set the current item to the saved index
        viewPager.setCurrentItem(currentFragmentIndex, false);

        // Use ViewTreeObserver to wait until the views are fully loaded
        ViewTreeObserver viewTreeObserver = viewPager.getViewTreeObserver();
        viewPager.setCurrentItem(currentFragmentIndex, true);

        // Call your methods after views are fully loaded
        updateWeatherData();
    }

    public void updateWeatherData() {
        int selectedCityIndex = this.dataStorage.getSelectedLocation(this);
        String locations = this.dataStorage.getLocations(this);
        String[] location = locations.split("\n");
        String city = location[selectedCityIndex].split(";")[0];
        String lat  = location[selectedCityIndex].split(";")[1];
        String lon  = location[selectedCityIndex].split(";")[2];

        String unit = this.dataStorage.getUseImperialUnits(this) ? "imperial" : "metric";
        //new FetchDailyWeatherTask().execute(Double.parseDouble(lat), Double.parseDouble(lon));
        new FetchCurrentWeather().execute(city, unit);
        // wait for the data to be loaded

    }

    public void updateInterface() {
        boolean useImperialUnits = this.dataStorage.getUseImperialUnits(this);
        pageAdapter.updateUI(currentWeather, dailyForecast, useImperialUnits ? OWM.Unit.IMPERIAL : OWM.Unit.METRIC);
    }
    private class FetchDailyWeatherTask extends AsyncTask<Double, Void, DailyWeatherForecast> {
        @Override
        protected DailyWeatherForecast doInBackground(Double... params) {
            try {
                // Assuming weatherDataProvider.getDailyWeatherForecastByCoords() takes double parameters
                return weatherDataProvider.getDailyWeatherForecastByCoords(params[0], params[1]);
            } catch (APIException | IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(DailyWeatherForecast result) {
            dailyForecast = result;
            updateInterface();
        }
    }

    private class FetchCurrentWeather extends AsyncTask<String, Void, CurrentWeather> {
        @Override
        protected CurrentWeather doInBackground(String... params) {
            try {
                // Assuming weatherDataProvider.getCurrentWeatherData() takes String parameters
                return weatherDataProvider.getCurrentWeatherData(params[0], params[1]);
            } catch (APIException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(CurrentWeather result) {
            currentWeather = result;
            // update your UI here using the result
            updateInterface();
        }
    }



    public void openSettings(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
    public void closeSettings(View view) {
        if (getResources().getConfiguration().orientation == android.content.res.Configuration.ORIENTATION_PORTRAIT) {
            setContentView(R.layout.main_screen_port);
        }
        else {
            setContentView(R.layout.main_screen_land);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(CURRENT_FRAGMENT_INDEX_KEY, viewPager.getCurrentItem());
    }

    private void setupViewPager() {
        ViewPager2 viewPager = findViewById(R.id.viewPager);
        this.pageAdapter = new PageAdapter(this);
        viewPager.setAdapter(pageAdapter);
    }
}
package com.example.weatherapp.Fragments;

// RightFragment.java
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;

import com.example.weatherapp.R;

import net.aksingh.owmjapis.model.CurrentWeather;
import net.aksingh.owmjapis.model.DailyWeatherForecast;

public class RightFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_right_port, container, false);
    }

    public void updateUI(CurrentWeather currentWeather, DailyWeatherForecast dailyForecast) {
        // update your UI here using currentWeather and dailyForecast
    }
}


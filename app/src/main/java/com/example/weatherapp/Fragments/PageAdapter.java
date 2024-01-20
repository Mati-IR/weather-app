package com.example.weatherapp.Fragments;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import net.aksingh.owmjapis.core.OWM;
import net.aksingh.owmjapis.model.CurrentWeather;
import net.aksingh.owmjapis.model.DailyWeatherForecast;
import net.aksingh.owmjapis.util.WeatherConditions;


public class PageAdapter extends FragmentStateAdapter {
    LeftFragment leftFragment;
    MidFragment midFragment;
    RightFragment rightFragment;

    CurrentWeather currentWeather = new CurrentWeather();
    DailyWeatherForecast dailyForecast = new DailyWeatherForecast();

    public PageAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
        if (leftFragment == null) {
            leftFragment = new LeftFragment();
        }
        if (midFragment == null) {
            midFragment = new MidFragment();
        }
        if (rightFragment == null) {
            rightFragment = new RightFragment();
        }
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                if (leftFragment == null) {
                    leftFragment = new LeftFragment();
                }
                updateUI(currentWeather, dailyForecast, OWM.Unit.METRIC);
                return leftFragment;
            case 1:
                if (midFragment == null) {
                    midFragment = new MidFragment();
                }
                updateUI(currentWeather, dailyForecast, OWM.Unit.METRIC);
                return this.midFragment;
            case 2:
                if (rightFragment == null) {
                    rightFragment = new RightFragment();
                }
                updateUI(currentWeather, dailyForecast, OWM.Unit.METRIC);
                return this.rightFragment;
            default:
                throw new IllegalArgumentException("Invalid position: " + position);
        }
    }

    public void updateUI(CurrentWeather currentWeather, DailyWeatherForecast dailyForecast, OWM.Unit unit) {
        this.currentWeather = currentWeather;
        this.dailyForecast = dailyForecast;
        // update your UI here using currentWeather and dailyForecast
        leftFragment.updateUI(currentWeather, dailyForecast, unit);
        midFragment.updateUI(currentWeather, dailyForecast, unit);
    }

    @Override
    public int getItemCount() {
        return 3; // Number of fragments
    }
}


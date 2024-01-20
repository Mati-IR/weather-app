package com.example.weatherapp.Fragments;

// MidFragment.java
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.weatherapp.R;

import net.aksingh.owmjapis.core.OWM;
import net.aksingh.owmjapis.model.CurrentWeather;
import net.aksingh.owmjapis.model.DailyWeatherForecast;

public class MidFragment extends Fragment {
    private View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_mid_port, container, false);
        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void updateUI(CurrentWeather currentWeather, DailyWeatherForecast dailyForecast, OWM.Unit unit) {
        if (view == null) {
            return;
        }
        // update your UI here using currentWeather and dailyForecast
        if (currentWeather != null) {
            TextView humidity = view.findViewById(R.id.textViewHumidity);
            TextView wind = view.findViewById(R.id.textViewWind);
            TextView sunrise = view.findViewById(R.id.textViewSunrise);
            TextView sunset = view.findViewById(R.id.textViewSunset);
            String speedUnit = unit == OWM.Unit.IMPERIAL ? "mph" : "km/h";

            humidity.setText("Humidity: " + Double.toString(currentWeather.getMainData().getHumidity().intValue()) + "%");
            wind.setText("Wind: " + currentWeather.getWindData().getSpeed() + " " + speedUnit);
            sunrise.setText("Sunrise: "
                    + currentWeather.getSystemData().getSunriseDateTime().getHours()
                    + ":" + currentWeather.getSystemData().getSunriseDateTime().getMinutes());
            sunset.setText("Sunset: "
                    + currentWeather.getSystemData().getSunsetDateTime().getHours()
                    + ":" + currentWeather.getSystemData().getSunsetDateTime().getMinutes());
        }


    }
}

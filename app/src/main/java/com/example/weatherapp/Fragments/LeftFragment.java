package com.example.weatherapp.Fragments;

// LeftFragment.java
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.weatherapp.R;

import net.aksingh.owmjapis.core.OWM;
import net.aksingh.owmjapis.model.CurrentWeather;
import net.aksingh.owmjapis.model.DailyWeatherForecast;
import net.aksingh.owmjapis.util.WeatherConditions;

import java.util.Date;

public class LeftFragment extends Fragment {
    private enum Weekday {
        SUN(1), MON(2), TUE(3), WED(4), THU(5), FRI(6), SAT(7);

        private final int dayNumber;

        Weekday(int dayNumber) {
            this.dayNumber = dayNumber;
        }

        public int getDayNumber() {
            return dayNumber;
        }
    }
    Weekday weekday = Weekday.SUN;
    private View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_left_port, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void updateUI(CurrentWeather currentWeather, DailyWeatherForecast dailyForecast, OWM.Unit unit) {
        if (view != null && currentWeather != null) {
            weekday = Weekday.values()[currentWeather.getDateTime().getDay()];
            // update your UI here using currentWeather and dailyForecast
            TextView city = view.findViewById(R.id.City);
            TextView temp = view.findViewById(R.id.textViewTemp);
            TextView sensibleTemp = view.findViewById(R.id.textViewTempLong);
            TextView date = view.findViewById(R.id.textViewDate);
            ImageView weatherIcon = view.findViewById(R.id.weatherIcon);

            String iconCode = currentWeather.getWeatherList().get(0).getIconCode();
            // Use the conditionId to get the icon code
            String iconName = "icon_"
                    + iconCode;
            String packageName = getActivity().getPackageName();
            System.out.println("Icon name: " + iconName);
            System.out.println("Package name: " + packageName);
            System.out.println("Resource name: " + getResources().getIdentifier(iconName, "drawable", getActivity().getPackageName()));

            String minute = currentWeather.getDateTime().getMinutes() < 10 ? "0" + currentWeather.getDateTime().getMinutes() : "" + currentWeather.getDateTime().getMinutes();
            city.setText(currentWeather.getCityName());
            temp.setText((currentWeather.getMainData().getTemp().intValue()) + (unit == OWM.Unit.METRIC ? "°C" : "°F"));
            sensibleTemp.setText((currentWeather.getMainData().getTempMin().intValue())
                    + "° / "
                    + (currentWeather.getMainData().getTempMax().intValue()));
            date.setText(weekday.toString()
                    + ", "
                    + currentWeather.getDateTime().getHours()
                    + ":"
                    + minute);
            //weatherIcon.setImageResource(getResources().getIdentifier(iconName, "drawable", getActivity().getPackageName()));
            weatherIcon.setImageResource(R.drawable.icon_04n);

        }

    }
}


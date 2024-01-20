package com.example.weatherapp.openweathermap;

import android.content.Context;
import android.content.res.Resources;

import com.example.weatherapp.LocationsActivity;
import com.example.weatherapp.R;
import com.google.android.libraries.places.api.Places;

import net.aksingh.owmjapis.api.APIException;
import net.aksingh.owmjapis.api.DailyWeatherForecastAPI;
import net.aksingh.owmjapis.core.OWM;
import net.aksingh.owmjapis.model.CurrentWeather;
import net.aksingh.owmjapis.model.DailyWeatherForecast;
import net.aksingh.owmjapis.util.WeatherConditions;

import java.io.IOException;
import java.util.ResourceBundle;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class WeatherDataProvider {
    private OWM owm;

    // get key from strings.xml
    private String apiKey;
    private Context context;

    interface ForecastAPI {
        @GET("forecast")
        Call<DailyWeatherForecast> getForecastByCoords(
                @Query("lat") double lat,
                @Query("lon") double lon,
                @Query("appid") String apiKey
        );
    }
    public WeatherDataProvider(Context context) {
        this.context = context;
        apiKey = this.context.getResources().getString(R.string.openweathermap_api_key);
        owm = new OWM(apiKey);
    }

    public CurrentWeather getCurrentWeatherData(String city, String unit) throws APIException {
        owm.setUnit(unit.equals("imperial") ? OWM.Unit.IMPERIAL : OWM.Unit.METRIC);
        return owm.currentWeatherByCityName(city);
    }

    // get 5 day weather forecast for a city
    public DailyWeatherForecast getDailyWeatherForecastByCoords(double latitude, double longitude) throws APIException, IOException {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.openweathermap.org/data/2.5/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ForecastAPI api = retrofit.create(ForecastAPI.class);

        Call<DailyWeatherForecast> apiCall = api.getForecastByCoords(latitude, longitude, apiKey);
        System.out.println("Request URL: " + apiCall.request().url());

        Response<DailyWeatherForecast> apiResp = apiCall.execute();
        DailyWeatherForecast forecast = apiResp.body();

        if (forecast == null) {
            if (!apiResp.isSuccessful()) {
                throw new APIException(apiResp.code(), apiResp.message());
            }

            forecast = new DailyWeatherForecast();
        }

        return forecast;
    }
    }

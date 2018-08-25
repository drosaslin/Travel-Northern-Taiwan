package com.example.android.weatherproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;

public class HourlyWeatherData extends AppCompatActivity {
    private WeatherData weatherData;
    private TextView place;
    private TextView temp;
    private TextView precipitationProb;
    private TextView windSpeed;
    private ImageView weather;
    private RecyclerView hourlyRecycler;
    private RecyclerView dailyRecycler;
    private HourlyDataAdapter hourlyAdapter;
    private DailyDataAdapter dailyAdapter;
    private HashMap<String, Integer> weatherIcons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hourly_weather_data);

        hourlyRecycler = findViewById(R.id.hourly_recycler);
        hourlyRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayout.HORIZONTAL, false));

        dailyRecycler = findViewById(R.id.daily_recycler);
        dailyRecycler.setLayoutManager(new LinearLayoutManager(this));

        place = findViewById(R.id.current_name);
        temp = findViewById(R.id.current_temperature);
        precipitationProb = findViewById(R.id.precipitation_prob);
        windSpeed = findViewById(R.id.wind_speed);
        weather = findViewById(R.id.current_weather);
        weatherIcons = new HashMap<>();

        weatherData = null;
        getIncomingIntent();
        populateIcons();
        updateGUI();
        weatherData.showData();
    }

    private void updateGUI() {
        if(weatherData != null) {
            String temperature = Integer.toString(Math.round(weatherData.getCurrently().getTemperature()));
            String precipitation = Float.toString(weatherData.getCurrently().getPrecipProbability());
            String windSp = Double.toString(weatherData.getCurrently().getWindSpeed());
            String icon = weatherData.getCurrently().getIcon();

            place.setText(weatherData.getCity());
            temp.setText(temperature);
            precipitationProb.setText(precipitation);
            windSpeed.setText(windSp);
            weather.setImageResource(weatherIcons.get(icon));

            hourlyAdapter = new HourlyDataAdapter(weatherData.getHourly().getData());
            hourlyRecycler.setAdapter(hourlyAdapter);

            dailyAdapter = new DailyDataAdapter(weatherData.getDaily().getData());
            dailyRecycler.setAdapter(dailyAdapter);
        }
    }

    private void getIncomingIntent() {
        Bundle data = getIntent().getExtras();
        if(data != null) {
            weatherData = data.getParcelable("WeatherData");
        }
    }

    private void populateIcons() {
        weatherIcons.put("clear-day", R.drawable.ic_wi_day_sunny);
        weatherIcons.put("clear-night", R.drawable.ic_wi_night_clear);
        weatherIcons.put("partly-cloudy-day", R.drawable.ic_wi_day_cloudy);
        weatherIcons.put("partly-cloudy-night", R.drawable.ic_wi_night_partly_cloudy);
        weatherIcons.put("cloudy", R.drawable.ic_wi_cloudy);
        weatherIcons.put("rain", R.drawable.ic_wi_day_rain);
        weatherIcons.put("sleet", R.drawable.ic_wi_day_sleet);
        weatherIcons.put("snow", R.drawable.ic_wi_day_snow);
        weatherIcons.put("wind", R.drawable.ic_wi_day_windy);
        weatherIcons.put("fog", R.drawable.ic_wi_day_fog);
    }
}

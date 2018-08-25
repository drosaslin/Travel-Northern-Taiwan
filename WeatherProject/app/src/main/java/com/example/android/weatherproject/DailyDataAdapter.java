package com.example.android.weatherproject;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by David Rosas on 8/23/2018.
 */

public class DailyDataAdapter extends RecyclerView.Adapter<DailyDataAdapter.ViewHolder> {
    private ArrayList<Forecast> forecasts;
    private HashMap<String, Integer> weatherIcons;


    public DailyDataAdapter(ArrayList<Forecast> newForecast) {
        forecasts = newForecast;
        weatherIcons = new HashMap<>();
        populateIcons();
    }

    @NonNull
    @Override
    public DailyDataAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.daily_weather_template, parent, false);
        return new DailyDataAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DailyDataAdapter.ViewHolder holder, int position) {
        String time = forecasts.get(position).getFormattedTime();
        String day = time.substring(0, time.indexOf(" "));
        String tempHigh = Integer.toString(Math.round(forecasts.get(position).getTemperatureHigh())) + "°";
        String tempLow = Integer.toString(Math.round(forecasts.get(position).getTemperatureLow())) + "°";
        String icon = forecasts.get(position).getIcon();

        holder.day.setText(day);
        holder.tempHigh.setText(tempHigh);
        holder.tempLow.setText(tempLow);
        holder.weatherIcon.setImageResource(weatherIcons.get(icon));
    }

    @Override
    public int getItemCount() {
        return forecasts.size();
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

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView day;
        TextView tempHigh;
        TextView tempLow;
        ImageView weatherIcon;

        public ViewHolder(View itemView) {
            super(itemView);

            day = (itemView).findViewById(R.id.day);
            tempHigh= (itemView).findViewById(R.id.temp_high);
            tempLow = (itemView).findViewById(R.id.temp_low);
            weatherIcon = (itemView).findViewById(R.id.weather);
        }
    }
}

package com.example.android.weatherproject;

import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by David Rosas on 8/13/2018.
 */

public class SummaryWeatherAdapter extends RecyclerView.Adapter<SummaryWeatherAdapter.ViewHolderData> {
    ArrayList<WeatherData> weatherData;
    HashMap<String, Integer> weatherIcons;

    public SummaryWeatherAdapter(ArrayList<WeatherData> newData){
        weatherData = newData;
        weatherIcons = new HashMap<>();
        populateIcons();
    }

    @Override
    public ViewHolderData onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.province_list, parent, false);
        return new ViewHolderData(view);
    }

    @Override
    public void onBindViewHolder(ViewHolderData holder, int position) {
        int temperature = Math.round(weatherData.get(position).getCurrently().getTemperature());
        String icon = weatherData.get(position).getCurrently().getIcon();

        holder.province.setText(weatherData.get(position).getCity());
        holder.temp.setText(Integer.toString(temperature) + "°");
        holder.weather.setImageResource(weatherIcons.get(icon));
    }

    @Override
    public int getItemCount() {
        return weatherData.size();
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

    public class ViewHolderData extends RecyclerView.ViewHolder {
        TextView province;
        TextView temp;
        ImageView weather;

        public ViewHolderData(View itemView) {
            super(itemView);

            province = itemView.findViewById(R.id.province_name);
            temp = itemView.findViewById(R.id.temp);
            weather = itemView.findViewById(R.id.weather);
        }
    }
}

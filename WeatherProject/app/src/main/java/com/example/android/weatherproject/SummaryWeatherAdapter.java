package com.example.android.weatherproject;

import android.content.res.Resources;
import android.support.annotation.NonNull;
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

public class SummaryWeatherAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<WeatherData> weatherData;
    HashMap<String, Integer> weatherIcons;

    public SummaryWeatherAdapter(ArrayList<WeatherData> newData){
        weatherData = newData;
        weatherIcons = new HashMap<>();
        populateIcons();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == R.layout.weather_list_main_row) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.weather_list_main_row, parent, false);
            return new CurrentPositionViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.province_list, parent, false);
            return new ProvinceViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int temperature = Math.round(weatherData.get(position).getCurrently().getTemperature());
        String icon = weatherData.get(position).getCurrently().getIcon();

        if(holder instanceof ProvinceViewHolder) {
            ((ProvinceViewHolder)holder).province.setText(weatherData.get(position).getCity());
            ((ProvinceViewHolder)holder).temp.setText(Integer.toString(temperature) + "°");
            ((ProvinceViewHolder)holder).weather.setImageResource(weatherIcons.get(icon));
        }
        else if(holder instanceof CurrentPositionViewHolder) {
            String precipitation = Float.toString(weatherData.get(position).getCurrently().getPrecipProbability());
            String windSpeed = Double.toString(weatherData.get(position).getCurrently().getWindSpeed());

            ((CurrentPositionViewHolder)holder).place.setText(weatherData.get(position).getCity());
            ((CurrentPositionViewHolder)holder).temp.setText(Integer.toString(temperature));
            ((CurrentPositionViewHolder)holder).precipitationProb.setText(precipitation);
            ((CurrentPositionViewHolder)holder).windSpeed.setText(windSpeed);
            ((CurrentPositionViewHolder)holder).weather.setImageResource(weatherIcons.get(icon));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0) {
            return R.layout.weather_list_main_row;
        }

        return R.layout.province_list;
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

    public class ProvinceViewHolder extends RecyclerView.ViewHolder {
        TextView province;
        TextView temp;
        ImageView weather;

        public ProvinceViewHolder(View itemView) {
            super(itemView);

            province = itemView.findViewById(R.id.province_name);
            temp = itemView.findViewById(R.id.temp);
            weather = itemView.findViewById(R.id.weather);
        }
    }

    public class CurrentPositionViewHolder extends RecyclerView.ViewHolder {
        TextView place;
        TextView temp;
        TextView precipitationProb;
        TextView windSpeed;
        ImageView weather;

        public CurrentPositionViewHolder(View itemView) {
            super(itemView);

            place = itemView.findViewById(R.id.current_name);
            temp = itemView.findViewById(R.id.current_temperature);
            precipitationProb = itemView.findViewById(R.id.precipitation_prob);
            windSpeed = itemView.findViewById(R.id.wind_speed);
            weather = itemView.findViewById(R.id.current_weather);
        }
    }
}

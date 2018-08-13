package com.example.android.weatherproject;

import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by David Rosas on 8/13/2018.
 */

public class SummaryWeatherAdapter extends RecyclerView.Adapter<SummaryWeatherAdapter.ViewHolderData> {
    ArrayList<WeatherData> weatherData;

    public SummaryWeatherAdapter(ArrayList<WeatherData> newData){
        weatherData = newData;
    }

    @Override
    public ViewHolderData onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.province_list, parent, false);
        ViewHolderData holder = new ViewHolderData(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolderData holder, int position) {
        holder.province.setText(weatherData.get(position).getCity().getName());
        holder.tempMin.setText("30");
        holder.tempMax.setText("32");
        holder.weather.setImageResource(R.drawable.ic_wi_sunrise);
    }

    @Override
    public int getItemCount() {
        return weatherData.size();
    }

    public class ViewHolderData extends RecyclerView.ViewHolder {
        TextView province;
        TextView tempMin;
        TextView tempMax;
        ImageView weather;

        public ViewHolderData(View itemView) {
            super(itemView);

            province = itemView.findViewById(R.id.province_name);
            tempMin = itemView.findViewById(R.id.temp_min);
            tempMax = itemView.findViewById(R.id.temp_max);
            weather = itemView.findViewById(R.id.weather);
        }
    }
}

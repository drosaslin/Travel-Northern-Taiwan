package com.example.android.weather;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by David Rosas on 8/4/2018.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolderData>{
    ArrayList<Forecast> weatherData;
    String city;

    public RecyclerAdapter(ArrayList<Forecast> newData, String newCity){
        weatherData = newData;
        city = newCity;
    }

    @Override
    public ViewHolderData onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.weather_list, null, false);
        ViewHolderData holder = new ViewHolderData(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolderData holder, int position) {
        String minTemp = Double.toString(weatherData.get(position).getTemperature().getTemp_min());
        String maxTemp = Double.toString(weatherData.get(position).getTemperature().getTemp_max());

        holder.date.setText(weatherData.get(position).getDt_txt() + " " + city);
        holder.tempMin.setText(minTemp);
        holder.tempMax.setText(maxTemp);
    }

    @Override
    public int getItemCount() {
        return weatherData.size();
    }

    public class ViewHolderData extends RecyclerView.ViewHolder {
        TextView date;
        TextView tempMin;
        TextView tempMax;

        public ViewHolderData(View itemView) {
            super(itemView);

            date = itemView.findViewById(R.id.weather_time);
            tempMin = itemView.findViewById(R.id.min_temp);
            tempMax = itemView.findViewById(R.id.max_temp);
        }
    }
}

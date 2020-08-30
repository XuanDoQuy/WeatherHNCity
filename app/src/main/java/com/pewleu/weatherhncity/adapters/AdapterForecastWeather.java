package com.pewleu.weatherhncity.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.pewleu.weatherhncity.R;
import com.pewleu.weatherhncity.common.Common;
import com.pewleu.weatherhncity.model.DataHourForecast;
import com.pewleu.weatherhncity.model.ForecastResult;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AdapterForecastWeather extends RecyclerView.Adapter<AdapterForecastWeather.ViewHolder> {
    private Context context;
    private List<DataHourForecast> listResult;

    public AdapterForecastWeather(Context context, List<DataHourForecast> listResult) {
        this.context = context;
        this.listResult = listResult;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_hourly_weather, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DataHourForecast result = listResult.get(position);
        Glide.with(context).load(Common.BASE_URL_ICON + result.getWeather().get(0).getIcon() + ".png").into(holder.imgState);
        holder.txtTemp.setText(result.getMain().getTemp() + "°C");
        holder.txtState.setText(result.getWeather().get(0).getMain());
        Date date = new Date(result.getDt() * 1000L);
        Log.d(Common.TAG, "onBindViewHolder: " + result.getDt());
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH");
        String dateFormeted = sdf.format(date);
        String[] tokens = dateFormeted.split(" ");
        holder.txtHour.setText(tokens[1]+"h");
        holder.txtDate.setText(tokens[0]);
    }

    @Override
    public int getItemCount() {
        return listResult.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgState;
        TextView txtTemp, txtState, txtHour, txtDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgState = itemView.findViewById(R.id.imageViewItemState);
            txtTemp = itemView.findViewById(R.id.textViewItemTemp);
            txtState = itemView.findViewById(R.id.textViewItemState);
            txtHour = itemView.findViewById(R.id.textViewItemHour);
            txtDate = itemView.findViewById(R.id.textViewItemDate);
        }
    }
}

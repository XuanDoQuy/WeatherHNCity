package com.pewleu.weatherhncity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.pewleu.weatherhncity.adapters.AdapterForecastWeather;
import com.pewleu.weatherhncity.common.Common;
import com.pewleu.weatherhncity.model.DataHourForecast;
import com.pewleu.weatherhncity.model.ForecastResult;
import com.pewleu.weatherhncity.retrofit.RetrofitClient;
import com.pewleu.weatherhncity.retrofit.WeatherApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ForecastActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    AdapterForecastWeather adapter;
    Retrofit retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.blue));
        }

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Forecast 5 days next");

        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        retrofit = RetrofitClient.getInstance();
        WeatherApi weatherApi = retrofit.create(WeatherApi.class);
        Call<ForecastResult> resultCall = weatherApi.getForecastByName(Common.CITY_NAME, Common.API_KEY, Common.MODE_DISPLAY);
        resultCall.enqueue(new Callback<ForecastResult>() {
            @Override
            public void onResponse(Call<ForecastResult> call, Response<ForecastResult> response) {
                if (!response.isSuccessful()){
                    Log.d(Common.TAG, "onResponse: " + response.code());
                    return;
                }
                List<DataHourForecast> listData =  response.body().getList();
                adapter = new AdapterForecastWeather(getApplicationContext(),listData);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<ForecastResult> call, Throwable t) {
                Log.d(Common.TAG, "onFailure: " + t.getMessage());
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
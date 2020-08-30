package com.pewleu.weatherhncity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pewleu.weatherhncity.common.Common;
import com.pewleu.weatherhncity.model.WeatherResult;
import com.pewleu.weatherhncity.retrofit.RetrofitClient;
import com.pewleu.weatherhncity.retrofit.WeatherApi;

import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {
    ConstraintLayout container;
    TextView txtPlace, txtCountry, txtLastUpdate, txtCurTemp, txtState, txtHumidity, txtCloud, txtWind,txtFeel,txtForecast;
    ImageView imgState;
    ProgressBar prB;
    Retrofit retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.blue));
        }

        init();

        getDataAndUpdateUI();

        txtForecast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,ForecastActivity.class);
                startActivity(intent);
            }
        });

    }

    void init() {
        container = findViewById(R.id.container);
        txtPlace = findViewById(R.id.textViewPlace);
        txtCountry = findViewById(R.id.textViewCountry);
        txtLastUpdate = findViewById(R.id.textViewLastUpdate);
        txtCurTemp = findViewById(R.id.textViewCurTemp);
        txtState = findViewById(R.id.textViewState);
        txtHumidity = findViewById(R.id.textViewHumidity);
        txtCloud = findViewById(R.id.textViewCloud);
        txtWind = findViewById(R.id.textViewWind);
        txtFeel = findViewById(R.id.textViewFeels_like);
        txtForecast = findViewById(R.id.textViewForecast);
        imgState = findViewById(R.id.imageViewMainIcon);
        prB = findViewById(R.id.progressBar);
        prB.setIndeterminate(true);
        prB.setVisibility(View.GONE);
        retrofit = RetrofitClient.getInstance();
    }

    void getDataAndUpdateUI() {
        WeatherApi weatherApi = retrofit.create(WeatherApi.class);
        Call<WeatherResult> resultCall = weatherApi.getWeatherByName(Common.CITY_NAME, Common.API_KEY, Common.MODE_DISPLAY);
        prB.setVisibility(View.VISIBLE);

        container.setVisibility(View.INVISIBLE);

        resultCall.enqueue(new Callback<WeatherResult>() {
            @Override
            public void onResponse(Call<WeatherResult> call, final Response<WeatherResult> response) {
                if (!response.isSuccessful()) {
                    Log.d(Common.TAG, "onResponse: not successful " + response.code());
                    return;
                }


                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        prB.setVisibility(View.GONE);
                        WeatherResult result = response.body();
                        container.setVisibility(View.VISIBLE);
                        txtPlace.setText("The Weather in " + result.getName());
                        txtCountry.setText("Country : " + result.getSys().getCountry());
                        Glide.with(getApplicationContext()).load(Common.BASE_URL_ICON + result.getWeather().get(0).getIcon() + ".png").into(imgState);
                        txtCurTemp.setText(result.getMain().getTemp() + " °C");
                        txtFeel.setText("Feel like : "+(result.getMain().getFeels_like()-1)+" °C");
                        txtState.setText(result.getWeather().get(0).getMain());
                        txtHumidity.setText(result.getMain().getHumidity() + " %");
                        txtCloud.setText(result.getClouds().getAll() + " %");
                        txtWind.setText(result.getWind().getSpeed() + " m/s");
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                        Date date = new Date();
                        String time = sdf.format(date).toString();
                        txtLastUpdate.setText("Last Update : "+time);
                    }
                },500);
            }

            @Override
            public void onFailure(Call<WeatherResult> call, Throwable t) {
                Log.d(Common.TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.update:
                getDataAndUpdateUI();
                break;
            case R.id.about:
                Intent intent = new Intent(MainActivity.this,AboutActivity.class);
                startActivity(intent);
                break;
            case R.id.exit:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
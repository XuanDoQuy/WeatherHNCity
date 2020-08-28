package com.pewleu.weatherhncity.retrofit;

import com.pewleu.weatherhncity.model.WeatherResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherApi {
    @GET("weather")
    Call<WeatherResult> getWeatherByName(@Query("q") String nameCity,@Query("appid") String apiKey);
}

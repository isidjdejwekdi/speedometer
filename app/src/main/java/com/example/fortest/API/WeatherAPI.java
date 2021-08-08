package com.example.fortest.API;

import com.example.fortest.API.response.ResponseData;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface WeatherAPI {

    @GET("/data/2.5/weather")
    Single<ResponseData> getWeather (@Query("lat") double lat,
                                     @Query("lon") double lon,
                                     @Query("appid") String key);
}

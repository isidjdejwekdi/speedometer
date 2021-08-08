package com.example.fortest.API.response;

import com.example.fortest.API.response.jsonObj.Clouds;
import com.example.fortest.API.response.jsonObj.Coord;
import com.example.fortest.API.response.jsonObj.Main;
import com.example.fortest.API.response.jsonObj.Sys;
import com.example.fortest.API.response.jsonObj.Weather;
import com.example.fortest.API.response.jsonObj.Wind;

import java.util.List;

public class ResponseData {
    Coord coord;
    List<Weather> weather;
    String base;
    Main main;
    Wind wind;
    Clouds clouds;
    double dt;
    Sys sys;
    int timezone;
    int id;
    String name;
    int cod;


    public Main getMain() {
        return main;
    }

    public List<Weather> getWeather() {
        return weather;
    }
}

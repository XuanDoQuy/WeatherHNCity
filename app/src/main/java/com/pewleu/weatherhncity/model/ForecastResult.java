package com.pewleu.weatherhncity.model;

import java.util.List;

public class ForecastResult {
    private String cod;
    private int message;
    private int cnt;
    private List<DataHourForecast> list;

    public String getCod() {
        return cod;
    }

    public int getMessage() {
        return message;
    }

    public int getCnt() {
        return cnt;
    }

    public List<DataHourForecast> getList() {
        return list;
    }

    public City getCity() {
        return city;
    }

    private City city;

}

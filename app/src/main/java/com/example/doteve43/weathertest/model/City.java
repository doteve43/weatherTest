package com.example.doteve43.weathertest.model;

/**
 * Created by doteve43 on 2016/1/7.
 */
public class City {
    private int id;
    private String cityName;
    private String provinceName;

    public void setId(int id) {
        this.id = id;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public int getId() {
        return id;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public String getCityName() {
        return cityName;
    }

}

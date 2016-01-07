package com.example.doteve43.weathertest.model;

/**
 * Created by doteve43 on 2016/1/7.
 */
public class District {
    private int id;
    private String districtName;
    private String cityName;

    public int getId() {
        return id;
    }

    public String getCityName() {
        return cityName;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }
}

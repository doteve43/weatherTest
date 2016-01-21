package com.example.doteve43.weathertest.model;

/**
 * Created by doteve on 2016/1/20.
 */
public class SixDayWeather {
    private String date;
    private String dayTemp;
    private String nightTemp;
    private String nightWeather;
    private String dayWeather;
    private String week;

    public String getDate() {
        return date;
    }

    public String getDayTemp() {
        return dayTemp;
    }

    public String getDayWeather() {
        return dayWeather;
    }

    public String getNightTemp() {
        return nightTemp;
    }

    public String getNightWeather() {
        return nightWeather;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setDayTemp(String dayTemp) {
        this.dayTemp = dayTemp;
    }

    public void setDayWeather(String dayWeather) {
        this.dayWeather = dayWeather;
    }

    public void setNightTemp(String nightTemp) {
        this.nightTemp = nightTemp;
    }

    public void setNightWeather(String nightWeather) {
        this.nightWeather = nightWeather;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }
}

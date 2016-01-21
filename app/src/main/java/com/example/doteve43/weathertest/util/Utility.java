package com.example.doteve43.weathertest.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.doteve43.weathertest.db.WeatherDB;
import com.example.doteve43.weathertest.model.City;
import com.example.doteve43.weathertest.model.District;
import com.example.doteve43.weathertest.model.Province;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.SortedMap;

/**
 * Created by doteve43 on 2016/1/6.
 */
public class Utility {
    public static void handleProvinceWeatherResponse(String response,WeatherDB weatherDB){
        try {
            JSONObject object = new JSONObject(response);
           // JSONObject resultcode  = object.getJSONObject("resultcode");
            JSONArray array = object.getJSONArray("result");
            for (int i=0;i<array.length();i++){
                JSONObject object1 = array.getJSONObject(i);
                String name = object1.getString("province");
                //Log.d("handleWeatherResponse", name);
                //将服务器获取的数据存入数据库
                Province province = new Province();
                province.setProvinceName(name);
                weatherDB.saveProvince(province);

            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 处理服务器返回的city信息
     * @param response
     * @param weatherDB
     */
    public static void handleCityWeatherResponse(String response,WeatherDB weatherDB){
        try {
            JSONObject object = new JSONObject(response);
            JSONArray array = object.getJSONArray("result");
            for (int i=0;i<array.length();i++){
                JSONObject object1 = array.getJSONObject(i);
                String name = object1.getString("city");
                String name2 = object1.getString("province");
                //Log.d("handleWeatherResponse", name2);
                //将服务器获取的数据存入数据库
                City city = new City();
                city.setCityName(name);
                city.setProvinceName(name2);
                weatherDB.saveCities(city);

            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void handleDistrictWeatherResponse(String response,WeatherDB weatherDB){
        try {
            JSONObject object = new JSONObject(response);
            JSONArray array = object.getJSONArray("result");
            for (int i=0;i<array.length();i++){
                JSONObject object1 = array.getJSONObject(i);
                String name = object1.getString("district");
                String name2 = object1.getString("city");
                //Log.d("handleWeatherResponse", name);
                //将服务器获取的数据存入数据库
                District district = new District();
                district.setDistrictName(name);
                district.setCityName(name2);
                weatherDB.saveDistrict(district);

            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 解析服务器返回的天气数据
     * @param response
     */
    public static void handleWeatherResponse(Context context,String response){
        try {
            JSONObject object = new JSONObject(response);

            JSONObject result = object.getJSONObject("result");
            JSONObject data = result.getJSONObject("data");
            JSONObject realtime = data.getJSONObject("realtime");
            String fabuTime = realtime.getString("time");
            JSONObject weather = realtime.getJSONObject("weather");

            JSONArray weatherByTime = data.getJSONArray("weather");

            JSONObject today = weatherByTime.getJSONObject(0);

            JSONObject info = today.getJSONObject("info");

            JSONArray day = info.getJSONArray("day");
            JSONArray night = info.getJSONArray("night");

            String dayWeather = day.getString(1);//当天白天的天气情况
            String nightWeather = night.getString(1);//当天晚上的天气情况

            Log.d("TAG",dayWeather+nightWeather);

            String temp = weather.getString("temperature");
            saveWeatherInfo(context, temp,dayWeather,nightWeather,fabuTime);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 将返回的数据存入sharePreferences中
     * @param context
     * @param temp
     * @param dayWeather
     * @param nightWeather
     * @param fabuTime
     */
    public static void saveWeatherInfo(Context context,String temp,String dayWeather,String nightWeather,String fabuTime){
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString("temp",temp);
        editor.putString("dayWeather",dayWeather);
        editor.putString("nightWeather",nightWeather);
        editor.putString("fabuTime",fabuTime);
        editor.commit();
    }

    public static void handleMoreInfoRequest(Context context,String response){
        try {
            JSONObject object = new JSONObject(response);

            JSONObject result = object.getJSONObject("result");
            JSONObject data = result.getJSONObject("data");
            JSONArray weatherByTime = data.getJSONArray("weather");
            for (int i=1;i<weatherByTime.length();i++){
                JSONObject weatherByDay = weatherByTime.getJSONObject(i);
                String date = weatherByDay.getString("date");//日期
                String week = weatherByDay.getString("week");//星期
                JSONObject info = weatherByDay.getJSONObject("info");
                JSONArray day = info.getJSONArray("day");
                JSONArray night = info.getJSONArray("night");
                String dayTemp = day.getString(2);//白天的最低温度
                String nightTemp = night.getString(2);//晚上的最低气温
                String dayWeather = day.getString(1);//白天的天气情况
                String nightWeather = night.getString(1);//晚上的天气情况

                saveSixDayWeatherInfo(context,date,i,dayTemp,nightTemp,dayWeather,nightWeather,week);
            }


        }
        catch (Exception e){
            e.printStackTrace();
        }
    }



    public static void saveSixDayWeatherInfo(Context context,String date,int i,String dayTemp,String nightTemp,String dayWeather,String nightWeather,String week){
        SharedPreferences.Editor editor = context.getSharedPreferences("sixDayWeatherInfo",0).edit();
        editor.putString("date"+i,date);
        editor.putString("Temp"+i,nightTemp+"~"+dayTemp);
        editor.putString("dayWeather"+i,dayWeather);
        editor.putString("nightWeather"+i,nightWeather);
        editor.putString("week"+i,week);
        editor.commit();

    }
}

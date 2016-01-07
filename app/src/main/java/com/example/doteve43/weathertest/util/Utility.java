package com.example.doteve43.weathertest.util;

import android.util.Log;

import com.example.doteve43.weathertest.db.WeatherDB;
import com.example.doteve43.weathertest.model.City;
import com.example.doteve43.weathertest.model.District;
import com.example.doteve43.weathertest.model.Province;

import org.json.JSONArray;
import org.json.JSONObject;

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
                Log.d("handleWeatherResponse", name);
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

}

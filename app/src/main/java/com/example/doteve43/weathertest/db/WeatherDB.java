package com.example.doteve43.weathertest.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.doteve43.weathertest.model.City;
import com.example.doteve43.weathertest.model.District;
import com.example.doteve43.weathertest.model.Province;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by doteve43 on 2016/1/6.
 */
public class WeatherDB {
    public static  final String DB_NAME = "cool_weather";
    public static final int VERSION = 1;
    private SQLiteDatabase database;
    private static WeatherDB weatherDB;

    /**
     * 将构造方法私有化
     * @param context
     */
    private WeatherDB(Context context){
        CoolWeatherOpenHelper helper = new CoolWeatherOpenHelper(context,DB_NAME,null,VERSION);
        database = helper.getWritableDatabase();
    }

    /**
     * 获取WeatherDB实例
     * @param context
     * @return
     */
    public static WeatherDB getInstance(Context context){
        if (weatherDB==null){
            weatherDB = new WeatherDB(context);
        }
        return weatherDB;
    }

    /**
     * 将Province实例存入数据库
     * @param province
     */
    public void saveProvince(Province province){
        if (province!=null){
            ContentValues values = new ContentValues();
            values.put("province_name",province.getProvinceName());
            database.insert("Province", null, values);
            Log.d("handle","insert successful");
        }
    }

    /**
     * 从数据库中读取全国数据
     * @return
     */
    public List<Province> loadProvinces(){
        List<Province> list = new ArrayList<>();
        Cursor cursor = database.query("Province",null,null,null,null,null,null);
        if (cursor.moveToFirst()){
            do {
                Province province = new Province();
                province.setId(cursor.getInt(cursor.getColumnIndex("id")));
                province.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
                list.add(province);
            }while (cursor.moveToNext());
        }
        if (cursor!=null){
            cursor.close();
        }
        return list;
    }

    /**
     * 将City实例存入数据库
     */
    public void saveCities(City city){
        if (city!=null){
            ContentValues values = new ContentValues();
            values.put("city_name",city.getCityName());
            values.put("province_name",city.getProvinceName());
            database.insert("City", null, values);
        }
    }

    /**
     * 从数据库读取某province下所有的city信息
     * @param provinceName
     * @return
     */
    public List<City> loadCities(String provinceName){
        List<City> list = new ArrayList<>();
        Cursor cursor = database.query("City",null,"province_name=?",new String[]{provinceName},null,null,null);
        if (cursor.moveToFirst()){
            do {
                City city = new City();
                city.setId(cursor.getInt(cursor.getColumnIndex("id")));
                city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
                city.setProvinceName(provinceName);
                list.add(city);
            }while (cursor.moveToNext());
        }
        if (cursor!=null){
            cursor.close();
        }
        return list;
    }

    /**
     * 将District实例存入数据库
     */
    public void saveDistrict(District district){
        if (district!=null){
            ContentValues values = new ContentValues();
            values.put("district_name",district.getDistrictName());
            values.put("city_name",district.getCityName());
            database.insert("District", null, values);
        }
    }

    /**
     * 从数据库中读取某city下所有的district
     */
    public List<District> loadDistrict(String cityName){
        List<District> list = new ArrayList<>();
        Cursor cursor = database.query("District",null,"city_name=?",new String[]{cityName},null,null,null);
        if (cursor.moveToFirst()){
            do {
                District district = new District();
                district.setId(cursor.getInt(cursor.getColumnIndex("id")));
                district.setDistrictName(cursor.getString(cursor.getColumnIndex("district_name")));
                district.setCityName(cityName);
                list.add(district);

            }while (cursor.moveToNext());
        }if (cursor!=null){
            cursor.close();
        }
        return list;
    }


}

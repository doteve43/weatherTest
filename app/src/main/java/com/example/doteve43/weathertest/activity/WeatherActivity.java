package com.example.doteve43.weathertest.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.doteve43.weathertest.R;
import com.example.doteve43.weathertest.db.WeatherDB;
import com.example.doteve43.weathertest.model.City;
import com.example.doteve43.weathertest.model.District;
import com.example.doteve43.weathertest.model.Province;
import com.example.doteve43.weathertest.util.HttpCallbackListener;
import com.example.doteve43.weathertest.util.HttpUtil;
import com.example.doteve43.weathertest.util.Utility;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by doteve43 on 2016/1/6.
 */
public class WeatherActivity extends Activity {
    public static final int LEVEL_PROVINCE = 0;
    public static final int LEVEL_CITY = 1;
    public static final int LEVEL_DISTRICT =2;
    private int currentLevel;
    private ListView listView;
    private TextView titleText;
    private ArrayAdapter<String> adapter;
    private List<String> dataList = new ArrayList<>();
    private List<Province> provinceList;
    private List<City> cityList;
    private List<District> districtList;
    private WeatherDB weatherDB;
    private Province selectedProvince;
    private City selectedCity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.choose_area);
        listView = (ListView) findViewById(R.id.list_view);
        titleText = (TextView) findViewById(R.id.title_text);

        adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,dataList);
        listView.setAdapter(adapter);
        weatherDB = WeatherDB.getInstance(this);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //填写具体逻辑
                if (currentLevel==LEVEL_PROVINCE){
                    selectedProvince = provinceList.get(position);
                    queryCities();
                }else if (currentLevel==LEVEL_DISTRICT){
                    selectedCity=cityList.get(position);
                    queryDistricts();
                }

            }
        });

        queryProvince();
    }

    private void queryProvince(){
        provinceList = weatherDB.loadProvinces();
        if (provinceList.size()>0){
            dataList.clear();
            for (Province province:provinceList){
                dataList.add(province.getProvinceName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            titleText.setText("China");
            currentLevel =LEVEL_PROVINCE;
        }else {
            queryFromServer("province");
        }
    }

    private void queryCities(){
        cityList = weatherDB.loadCities(selectedProvince.getProvinceName());
        if (cityList.size()>0){
            dataList.clear();
            for (City city:cityList){
                if (city.getProvinceName().equals(selectedProvince.getProvinceName())){
                    dataList.add(city.getCityName());
                }

            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            titleText.setText("City");
            currentLevel=LEVEL_CITY;
        }else {
            queryFromServer("city");
        }

    }

    public void queryDistricts(){

    }

    private void queryFromServer(final String type){
        String address="http://v.juhe.cn/weather/citys?key=a20ca9eba433167228cb96002ab48f7a";
        HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                //在这里根据返回的内容执行具体操作

                if ("province".equals(type)){
                    Utility.handleProvinceWeatherResponse(response,weatherDB);
                }else if ("city".equals(type)){
                    Utility.handleCityWeatherResponse(response,weatherDB);
                }



                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if ("province".equals(type)){
                            queryProvince();
                        }
                        else if ("city".equals(type)){
                            queryCities();
                        }

                    }
                });
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }
}

package com.example.doteve43.weathertest.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.doteve43.weathertest.R;
import com.example.doteve43.weathertest.util.HttpCallbackListener;
import com.example.doteve43.weathertest.util.HttpUtil;
import com.example.doteve43.weathertest.util.Utility;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by doteve43 on 2016/1/7.
 */
public class WeatherActivity extends AppCompatActivity {
    private TextView title;
    private TextView weatherTempNow;
    private String cityName;
    private TextView dayWeather;
    private TextView nightWeather;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_layout);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        weatherTempNow = (TextView) findViewById(R.id.text_temp);
        dayWeather = (TextView) findViewById(R.id.day_weather);
        nightWeather = (TextView) findViewById(R.id.night_weather);
        title = (TextView) findViewById(R.id.weather_city_name);
        cityName = getIntent().getStringExtra("selectedCityName");
        title.setText(cityName);
        queryWeather(cityName);
    }

    /**
     * 将传入的city名字转码
     * @param districtName
     */
    private void queryWeather(String districtName){
        String utf = null;
        try {
            utf = URLEncoder.encode(districtName,"UTF-8");
            Log.d("handleWeatherResponse",utf);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String address = "http://op.juhe.cn/onebox/weather/query?cityname="+utf+"&dtype=&key=6405e94cb36025fe5b9c37513f0806aa";
        //填写queryFromServer
        queryFromServer(address);
    }

    private void queryFromServer(String address){
        HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                Utility.handleWeatherResponse(WeatherActivity.this,response);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showWeather();
                    }
                });
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }

    private void showWeather(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        weatherTempNow.setText(preferences.getString("temp","")+"℃");
        dayWeather.setText("白天："+preferences.getString("dayWeather",""));
        nightWeather.setText("夜晚："+preferences.getString("nightWeather",""));

    }

}

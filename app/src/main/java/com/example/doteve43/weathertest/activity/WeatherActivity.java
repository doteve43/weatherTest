package com.example.doteve43.weathertest.activity;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.doteve43.weathertest.R;
import com.example.doteve43.weathertest.db.WeatherDB;
import com.example.doteve43.weathertest.model.City;
import com.example.doteve43.weathertest.model.SixDayWeather;
import com.example.doteve43.weathertest.recyclerView.SixWeatherAdapter;
import com.example.doteve43.weathertest.util.HttpCallbackListener;
import com.example.doteve43.weathertest.util.HttpUtil;
import com.example.doteve43.weathertest.util.Utility;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by doteve43 on 2016/1/7.
 */
public class WeatherActivity extends AppCompatActivity {
    private ImageView image;
    private TextView title;
    private TextView weatherTempNow;
    private String cityName;
    private TextView dayWeather;
    private TextView nightWeather;
    private TextView fabuTime;
    private ProgressDialog progressDialog;

    private RecyclerView sixDayRecyclerView;

    private SixWeatherAdapter adapter;

    private List<SixDayWeather> dateList = new ArrayList<>();

    private WeatherDB weatherDB;

    private boolean collect;

    private List<City> cityList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);


        image = (ImageView) findViewById(R.id.image_pic);
        weatherTempNow = (TextView) findViewById(R.id.text_temp);
        dayWeather = (TextView) findViewById(R.id.day_weather);
        nightWeather = (TextView) findViewById(R.id.night_weather);
        title = (TextView) findViewById(R.id.weather_city_name);
        fabuTime = (TextView) findViewById(R.id.text_fabuTime);
        //intent传入的名字
        cityName = getIntent().getStringExtra("selectedCityName");
        title.setText(cityName);
        queryWeather(cityName);
        //设置recyclerView
        sixDayRecyclerView = (RecyclerView) findViewById(R.id.particular_weather_msg);
        adapter = new SixWeatherAdapter(dateList);
        sixDayRecyclerView.setAdapter(adapter);
        sixDayRecyclerView.setLayoutManager(new LinearLayoutManager(WeatherActivity.this, LinearLayoutManager.HORIZONTAL, false));

        weatherDB =WeatherDB.getInstance(this);

    }

    /**
     * 收藏功能实现
     * 如果可以获取City实例收藏将更加简单
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_weather, menu);
        MenuItem favoriteItem = menu.findItem(R.id.favorite);

        cityList = weatherDB.updateCollectSituation();
        favoriteItem.setIcon(R.drawable.ic_favorite_border_white_24dp);
        collect=false;

        for (City city:cityList){
            Log.d("TAG",city.getCityName());
            if (cityName.equals(city.getCityName())){
                favoriteItem.setIcon(R.drawable.ic_favorite_white_24dp);
                collect =true;
                break;
            }

        }

        favoriteItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                //判断是否收藏
                if (collect==false){

                    weatherDB.addCollect(cityName);
                    item.setIcon(R.drawable.ic_favorite_white_24dp);
                    Toast.makeText(WeatherActivity.this, "收藏成功了喵~",Toast.LENGTH_SHORT).show();
                    collect=true;
                    return collect;
                }
                else if (collect==true){
                    weatherDB.deleteCollect(cityName);
                    item.setIcon(R.drawable.ic_favorite_border_white_24dp);
                    Toast.makeText(WeatherActivity.this, "取消收藏了喵~",Toast.LENGTH_SHORT).show();
                    collect=false;
                    return collect;
                }

                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }


    /**
     * 将传入的city名字转码
     * @param districtName
     */
    private void queryWeather(String districtName){
        String utf = null;
        try {
            utf = URLEncoder.encode(districtName,"UTF-8");
            //Log.d("handleWeatherResponse",utf);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String address = "http://op.juhe.cn/onebox/weather/query?cityname="+utf+"&dtype=&key=6405e94cb36025fe5b9c37513f0806aa";
        //填写queryFromServer
        queryFromServer(address);
    }

    private void queryFromServer(String address){
        showProgressDialog();
        HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                Utility.handleWeatherResponse(WeatherActivity.this, response);
                Utility.handleMoreInfoRequest(WeatherActivity.this, response);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showWeather();
                        loadSixWeather();
                        closeProgressDialog();
                    }
                });
            }

            @Override
            public void onError(Exception e) {
                //error
            }
        });
    }

    /**
     * 显示weather信息
     */
    private void showWeather(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean dayORnight = changeBackgroundByTime();
        if (dayORnight==true){
            image.setImageResource(R.drawable.day);
        }else {
            image.setImageResource(R.drawable.night);
        }
        weatherTempNow.setText(preferences.getString("temp","")+"℃");
        dayWeather.setText("白天："+preferences.getString("dayWeather",""));
        nightWeather.setText("夜晚：" + preferences.getString("nightWeather", ""));
        fabuTime.setText("发布于今天" + preferences.getString("fabuTime", ""));

    }

    /**
     * 显示未来6天数据
     */
    private void loadSixWeather(){
        SharedPreferences preferences =getSharedPreferences("sixDayWeatherInfo", 0);
        for (int i=1;i<7;i++){
            //6天数据
            SixDayWeather sixDayWeather = new SixDayWeather();
            sixDayWeather.setDate(preferences.getString("date"+i,""));
            sixDayWeather.setDayTemp("温度："+preferences.getString("Temp"+i,"")+"℃");
            sixDayWeather.setDayWeather("白天天气："+preferences.getString("dayWeather"+i,""));
            sixDayWeather.setNightWeather("夜晚天气："+preferences.getString("nightWeather"+i,""));
            sixDayWeather.setWeek("周"+preferences.getString("week"+i,""));
            dateList.add(sixDayWeather);
        }
    }

    /**
     * 显示进度对话框
     */
    private void showProgressDialog(){
        if (progressDialog==null){
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("loading...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    /**
     * 关闭进度对话框
     */
    private void closeProgressDialog(){
        if (progressDialog!=null){
            progressDialog.dismiss();
        }
    }

    /**
     * 判断时间，根据时间来换imageView中的图片
     * @return
     */
    private boolean changeBackgroundByTime(){
        Calendar calendar = Calendar.getInstance();
        int time = calendar.get(Calendar.HOUR_OF_DAY);
        if (time>=6 && time<=18){
            return true;
        }else {
            return false;
        }
    }

}

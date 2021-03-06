package com.example.doteve43.weathertest.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.doteve43.weathertest.R;
import com.example.doteve43.weathertest.db.WeatherDB;
import com.example.doteve43.weathertest.model.City;
import com.example.doteve43.weathertest.model.District;
import com.example.doteve43.weathertest.model.Province;
import com.example.doteve43.weathertest.recyclerView.MyAdapter;
import com.example.doteve43.weathertest.util.HttpCallbackListener;
import com.example.doteve43.weathertest.util.HttpUtil;
import com.example.doteve43.weathertest.util.Utility;

import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by doteve43 on 2016/1/6.
 */
public class ChooserActivity extends AppCompatActivity {
    public static final int LEVEL_PROVINCE = 0;
    public static final int LEVEL_CITY = 1;
    public static final int LEVEL_DISTRICT =2;
    private int currentLevel;
    //private ListView listView;
    //private ArrayAdapter<String> adapter;

    private MyAdapter adapter;

    private RecyclerView recyclerView;

    private List<String> dataList = new ArrayList<>();

    private List<Province> provinceList;
    private List<City> cityList;
    //private List<District> districtList;
    private WeatherDB weatherDB;
    //private Province selectedProvince;
    private City selectedCity;
    private ProgressDialog progressDialog;
    private String selectedName;

    private SearchView searchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);

        //listView = (ListView) findViewById(R.id.list_view);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        //adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,dataList);
        adapter = new MyAdapter(dataList);

        //listView.setAdapter(adapter);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(ChooserActivity.this, LinearLayoutManager.VERTICAL, false));

        weatherDB = WeatherDB.getInstance(this);

        //点击事件
        adapter.setOnItemClickListener(new MyAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, String data) {
                selectedName = data;
                //Toast.makeText(ChooserActivity.this, data, Toast.LENGTH_SHORT).show();
                if (currentLevel == LEVEL_PROVINCE) {
                    queryCities(selectedName);
                } else if (currentLevel == LEVEL_CITY) {
                    Intent intent = new Intent(ChooserActivity.this, WeatherActivity.class);
                    intent.putExtra("selectedCityName", selectedName);

                    startActivity(intent);
                }
            }
        });
        queryProvince();

        /*listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //填写具体逻辑
                if (currentLevel==LEVEL_PROVINCE){
                    selectedProvince = provinceList.get(position);
                    queryCities();
                }else if (currentLevel==LEVEL_CITY){
                    selectedCity=cityList.get(position);
                    Intent intent = new Intent(ChooserActivity.this,WeatherActivity.class);
                    intent.putExtra("selectedCityName",selectedCity.getCityName());
                    startActivity(intent);
                }

            }
        });*/

    }

    /**
     * 创建菜单选项
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main,menu);

        MenuItem searchItem = menu.findItem(R.id.search_view);

        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //查询数据库中的城市名字
                List<City> cityList = weatherDB.querySelectedCity(query);
                dataList.clear();
                for (City city:cityList){
                    dataList.add(city.getCityName());

                }
                if (dataList.size()==0){
                    Toast.makeText(ChooserActivity.this,"输入有误，请输入需要查询的城市名",Toast.LENGTH_SHORT).show();
                }
                adapter.notifyDataSetChanged();
                currentLevel=LEVEL_CITY;
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d("TAG",newText);
                return true;
            }
        });


        return super.onCreateOptionsMenu(menu);
    }

    /**
     *从数据库中获取province信息，如果没有就去服务器查询
     */
    private void queryProvince(){
        provinceList = weatherDB.loadProvinces();
        if (provinceList.size()>0){
            dataList.clear();
            for (Province province:provinceList){
                dataList.add(province.getProvinceName());
            }
            adapter.notifyDataSetChanged();
            //listView.setSelection(0);
            currentLevel =LEVEL_PROVINCE;
        }else {
            queryFromServer("province");
        }
    }

    /**
     * 从数据库中获取city信息，如果没有就去服务器中查询
     */
    private void queryCities(String heheName){
        cityList = weatherDB.loadCities(heheName);
        if (cityList.size()>0){
            dataList.clear();
            for (City city:cityList){
                if (city.getProvinceName().equals(heheName)){
                    dataList.add(city.getCityName());
                }

            }
            adapter.notifyDataSetChanged();
            //listView.setSelection(0);
            currentLevel=LEVEL_CITY;
        }else {
            queryFromServer("city");
        }

    }


    /**
     * 从服务器中查询信息
     * @param type
     */
    private void queryFromServer(final String type){
        String address="http://v.juhe.cn/weather/citys?key=a20ca9eba433167228cb96002ab48f7a";
        showProgressDialog();
        HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                //在这里根据返回的内容执行具体操作

                if ("province".equals(type)){
                    Utility.handleProvinceWeatherResponse(response,weatherDB);
                }else if ("city".equals(type)){
                    Utility.handleCityWeatherResponse(response,weatherDB);
                }else if ("district".equals(type)){
                    Utility.handleDistrictWeatherResponse(response,weatherDB);
                }



                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        if ("province".equals(type)){
                            queryProvince();
                        }
                        else if ("city".equals(type)){
                            queryCities(selectedName);
                        }

                    }
                });
            }

            @Override
            public void onError(Exception e) {

            }
        });
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
     * Back 返回，根据当前的级别来判断
     */
    @Override
    public void onBackPressed() {
        if (currentLevel==LEVEL_DISTRICT){
            queryCities(selectedName);
        }else if (currentLevel==LEVEL_CITY){
            queryProvince();
        }else {
            finish();
        }
    }
}

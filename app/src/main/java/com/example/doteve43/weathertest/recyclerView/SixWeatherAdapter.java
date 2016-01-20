package com.example.doteve43.weathertest.recyclerView;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.doteve43.weathertest.R;
import com.example.doteve43.weathertest.model.SixDayWeather;

import java.util.List;

/**
 * Created by doteve on 2016/1/20.
 */
public class SixWeatherAdapter extends RecyclerView.Adapter {
    private List<SixDayWeather> dataList;
    private SixDayWeather sixDayWeather;

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView date;
        TextView futureDayTemp;
        TextView futureDayWeather;
        TextView futureNightTemp;
        TextView futureNightWeather;
        public ViewHolder(View itemView) {
            super(itemView);
            date = (TextView) itemView.findViewById(R.id.date_info);
            futureDayTemp = (TextView) itemView.findViewById(R.id.future_day_temp);
            futureDayWeather = (TextView) itemView.findViewById(R.id.future_day_weather);
            futureNightTemp = (TextView) itemView.findViewById(R.id.future_night_temp);
            futureNightWeather = (TextView) itemView.findViewById(R.id.future_night_weather);
        }

        public TextView getDate() {
            return date;
        }

        public TextView getFutureDayTemp() {
            return futureDayTemp;
        }

        public TextView getFutureDayWeather() {
            return futureDayWeather;
        }

        public TextView getFutureNightTemp() {
            return futureNightTemp;
        }

        public TextView getFutureNightWeather() {
            return futureNightWeather;
        }
    }

    /**
     * 自定义构造方法
     */
   public SixWeatherAdapter(List<SixDayWeather> dataList){
        this.dataList = dataList;
   }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.six_day_weather_info,null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        sixDayWeather = dataList.get(position);
        viewHolder.getDate().setText(sixDayWeather.getDate());
        viewHolder.getFutureDayTemp().setText(sixDayWeather.getDayTemp());
        viewHolder.getFutureDayWeather().setText(sixDayWeather.getDayWeather());
        viewHolder.getFutureNightTemp().setText(sixDayWeather.getNightTemp());
        viewHolder.getFutureNightWeather().setText(sixDayWeather.getNightWeather());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }


}

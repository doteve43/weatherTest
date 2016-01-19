package com.example.doteve43.weathertest.recyclerView;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.doteve43.weathertest.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by doteve43 on 2016/1/19.
 */
public class MyAdapter extends RecyclerView.Adapter{
    private List<String> dataList = new ArrayList<>();
    private String cityNameByDataList;

    private int collected=0;//记录收藏状态，0为不收藏，1为收藏

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView listCityName;
        ImageButton collectButton;

        public ViewHolder(View root) {
            super(root);
            listCityName = (TextView) root.findViewById(R.id.list_city_name);
            collectButton = (ImageButton) root.findViewById(R.id.button_collection);
        }
        public TextView getListCityName(){
            return listCityName;
        }

        public ImageButton getCollectButton(){
            return collectButton;
        }
    }

    public MyAdapter(List<String> dataList){
        this.dataList = dataList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,null);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final ViewHolder viewHolder = (ViewHolder)holder;
        cityNameByDataList = dataList.get(position);
        viewHolder.getListCityName().setText(cityNameByDataList);

        viewHolder.getCollectButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (collected==0){
                    viewHolder.getCollectButton().setImageResource(R.drawable.ic_star_black_18dp);
                    collected=1;
                    return;

                }else if (collected==1){
                    viewHolder.getCollectButton().setImageResource(R.drawable.ic_star_border_black_18dp);
                    collected=0;
                    return;
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}

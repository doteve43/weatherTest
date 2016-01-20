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
public class MyAdapter extends RecyclerView.Adapter implements View.OnClickListener {
    private List<String> dataList = new ArrayList<>();
    private String cityNameByDataList;
    private OnRecyclerViewItemClickListener itemClickListener = null;

    private int collected=0;//记录收藏状态，0为不收藏，1为收藏


    class ViewHolder extends RecyclerView.ViewHolder {

        TextView listCityName;
        //ImageButton collectButton;

        public ViewHolder(View root) {
            super(root);
            listCityName = (TextView) root.findViewById(R.id.list_city_name);
           // collectButton = (ImageButton) root.findViewById(R.id.button_collection);
        }
        public TextView getListCityName(){
            return listCityName;
        }

       /* public ImageButton getCollectButton(){
            return collectButton;
        }*/
    }

    /**
     * 自定义构造方法，将Activity中的dataList传入
     * @param dataList
     */
    public MyAdapter(List<String> dataList){
        this.dataList = dataList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,null);
        itemView.setOnClickListener(this);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder)holder;
        cityNameByDataList = dataList.get(position);
        viewHolder.getListCityName().setText(cityNameByDataList);
        viewHolder.itemView.setTag(cityNameByDataList);
    }

    /**
     * 获取数据的长度
     * @return
     */
    @Override
    public int getItemCount() {
        return dataList.size();
    }

    /**
     * 自定义点击监听器
     */
    public static interface OnRecyclerViewItemClickListener{
        void onItemClick(View view,String data);
    }

    @Override
    public void onClick(View v) {
        if (itemClickListener!=null){
            itemClickListener.onItemClick(v, (String) v.getTag());
        }
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }

}

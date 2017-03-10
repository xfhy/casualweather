package com.xfhy.casualweather.adapter;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xfhy.casualweather.R;
import com.xfhy.casualweather.fragments.ChooseAreaFragment;
import com.xfhy.casualweather.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xfhy on 2017/3/9.
 * 选择省市县数据的(RecyclerView)列表的适配器
 */

public class ProvinceCityAdapter extends
        RecyclerView.Adapter<ProvinceCityAdapter.ViewHolder> {

    private static final String TAG = "ProvinceCityAdapter";


    private List<String> mDataList = new ArrayList<>();   //数据


    /**
     * 当前选中的级别
     */
    private int currentLevel;

    private LocalBroadcastManager localBroadcastManager;

    //用于缓存数据
    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView textView;

        //构造方法
        public ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.tv_prcico);
        }
    }

    public ProvinceCityAdapter(List<String> mDataList) {
        this.mDataList = mDataList;
    }

    //创建ViewHolder实例
    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.item_prcico_layout, parent, false);
        final ViewHolder viewHolder = new ViewHolder(view);
        localBroadcastManager = LocalBroadcastManager.getInstance(parent.getContext());
        final Intent intent = new Intent(ChooseAreaFragment.LOCAL_BROADCAST);
        viewHolder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int adapterPosition = viewHolder.getAdapterPosition();
                if (currentLevel == ChooseAreaFragment.LEVEL_PROVINCE) {
                    intent.putExtra("province_position", adapterPosition);   //当前点击的省份位置
                    intent.putExtra("query_city", true);   //通知fragment,让它去调用queryCity()方法
                } else if (currentLevel == ChooseAreaFragment.LEVEL_CITY) {
                    intent.putExtra("city_position", adapterPosition);   //当前点击的城市位置
                    intent.putExtra("query_county", true);
                } else if(currentLevel == ChooseAreaFragment.LEVEL_COUNTY){
                    intent.putExtra("county_position", adapterPosition);   //当前点击的城市位置
                    Log.i(TAG, "onClick: "+adapterPosition);
                }
                localBroadcastManager.sendBroadcast(intent);   //发送本地广播   通知fragment该刷新了
            }
        });
        return viewHolder;
    }

    //子项数据进行赋值   当滚动到屏幕内时执行
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String position_choice = mDataList.get(position);
        holder.textView.setText(position_choice);
    }

    //多少个子项
    @Override
    public int getItemCount() {
        return mDataList.size();
    }


    public void setCurrentLevel(int currentLevel) {
        this.currentLevel = currentLevel;
    }

    public void setDataList(List<String> dataList) {
        this.mDataList = dataList;
    }

}

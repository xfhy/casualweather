package com.xfhy.casualweather.fragments;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.xfhy.casualweather.R;
import com.xfhy.casualweather.WeatherActivity;
import com.xfhy.casualweather.adapter.ProvinceCityAdapter;
import com.xfhy.casualweather.bean.City;
import com.xfhy.casualweather.bean.County;
import com.xfhy.casualweather.bean.Province;
import com.xfhy.casualweather.util.HttpUtil;
import com.xfhy.casualweather.util.Utility;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by xfhy on 2017/3/9.
 * 选择省市县选项
 */

public class ChooseAreaFragment extends Fragment {

    private static final String TAG = "ChooseAreaFragment";

    public static final int LEVEL_PROVINCE = 0;
    public static final int LEVEL_CITY = 1;
    public static final int LEVEL_COUNTY = 2;

    private ProgressDialog progressDialog;  //进度对话框

    private TextView titleText;

    private Button backButton;

    private RecyclerView recyclerView;  //列表
    private ProvinceCityAdapter adapter;  //适配器
    private List<String> dataList = new ArrayList<>();   //数据

    /**
     * 省列表
     */
    private List<Province> provinceList;
    /**
     * 市列表
     */
    private List<City> cityList;
    /**
     * 县列表
     */
    private List<County> countyList;

    /**
     * 选中的省份
     */
    private Province selectedProvince;
    /**
     * 选中的城市
     */
    private City selectedCity;
    /**
     * 当前选中的级别
     */
    private int currentLevel;

    private IntentFilter intentFilter;
    private LocalReceiver localReceiver;    //本地广播接收者
    private LocalBroadcastManager localBroadcastManager;   //本地广播管理者   可以用来注册广播

    /**
     * 发送本地广播的action
     */
    public static final String LOCAL_BROADCAST = "com.xfhy.casualweather.LOCAL_BROADCAST";

    //做一些初始化操作
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.choose_area, container, false);
        titleText = (TextView) view.findViewById(R.id.title_text);
        backButton = (Button)view.findViewById(R.id.back_button);
        recyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);

        //获取LocalBroadcastManager   本地广播管理者实例
        localBroadcastManager = LocalBroadcastManager.getInstance(getContext());
        localReceiver = new LocalReceiver();
        intentFilter = new IntentFilter();
        intentFilter.addAction(LOCAL_BROADCAST);   //添加action
        localBroadcastManager.registerReceiver(localReceiver,intentFilter);   //注册本地广播

        //设置LayoutManager
        LinearLayoutManager linearManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearManager);


        //设置adapter
        adapter = new ProvinceCityAdapter(dataList);
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //按返回键   监听器    显示上一级数据
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentLevel == LEVEL_COUNTY){
                    queryCities();
                } else if(currentLevel == LEVEL_CITY){
                    queryProvinces();
                }
            }
        });
        queryProvinces();
    }

    /**
     * 查询全国所有的省,优先从数据库查询,如果没有查询到再去服务器上查询
     */
    private void queryProvinces() {
        titleText.setText("中国");
        backButton.setVisibility(View.GONE);
        provinceList = DataSupport.findAll(Province.class);    //查询province表的全部内容
        if (provinceList.size() > 0) {    //将省份名称全部封装到一个集合中
            dataList.clear();
            for (Province province : provinceList) {
                dataList.add(province.getProvinceName());
            }
            adapter.setDataList(dataList);     //设置需要显示的数据
            adapter.notifyDataSetChanged();   //通知数据已更新
            recyclerView.getLayoutManager().smoothScrollToPosition(recyclerView,null,0);
            currentLevel = LEVEL_PROVINCE;
            adapter.setCurrentLevel(LEVEL_PROVINCE);   //设置当前等级是省的那个等级
        } else {
            String address = "http://guolin.tech/api/china";
            queryFromServer(address, "province");
        }
    }

    private void queryFromServer(String address, final String type) {
        showProgressDialog();    //显示对话框
        HttpUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                boolean result = false;
                if ("province".equals(type)) {
                    result = Utility.handleProvinceResponse(responseText);
                } else if ("city".equals(type)) {
                    result = Utility.handleCityResponse(responseText, selectedProvince.getId());
                } else if ("county".equals(type)) {
                    result = Utility.handleCountyResponse(responseText, selectedCity.getId());
                }
                if (result) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                            if ("province".equals(type)) {
                                queryProvinces();
                            } else if ("city".equals(type)) {
                                queryCities();
                            } else if ("county".equals(type)) {
                                queryCounties();
                            }
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                // 通过runOnUiThread()方法回到主线程处理逻辑
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(getContext(), "加载失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    /**
     * 显示进度对话框
     */
    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("正在加载...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    /**
     * 关闭进度对话框
     */
    private void closeProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    /**
     * 查询选中省内所有的市,优先从数据库查询,如果没有查询到再去服务器查询
     */
    protected void queryCities() {
        titleText.setText(selectedProvince.getProvinceName());
        backButton.setVisibility(View.VISIBLE);
        cityList = DataSupport.where("provinceid = ?", String.valueOf(selectedProvince.getId())).find(City.class);
        if (cityList.size() > 0) {
            dataList.clear();
            for (City city : cityList) {
                dataList.add(city.getCityName());
            }
            adapter.setDataList(dataList);
            adapter.notifyDataSetChanged();
            currentLevel = LEVEL_CITY;
            adapter.setCurrentLevel(LEVEL_CITY);
        } else {
            int provinceCode = selectedProvince.getProvinceCode();
            String address = "http://guolin.tech/api/china/" + provinceCode;
            queryFromServer(address, "city");
        }
    }

    /**
     * 查询选中市内所有的县,优先从数据库查询,如果没有查询到再去服务器查询
     */
    private void queryCounties() {
        titleText.setText(selectedCity.getCityName());
        backButton.setVisibility(View.VISIBLE);

        //查询数据库     查询county表
        countyList = DataSupport.where("cityid = ?", String.valueOf(selectedCity.getId())).find(County.class);
        if (countyList.size() > 0) {
            dataList.clear();
            for (County county : countyList) {
                dataList.add(county.getCountyName());
            }
            adapter.setDataList(dataList);    //设置数据
            adapter.notifyDataSetChanged();   //通知更新
            currentLevel = LEVEL_COUNTY;      //更新级别
            adapter.setCurrentLevel(LEVEL_COUNTY);
        } else {
            int provinceCode = selectedProvince.getProvinceCode();
            int cityCode = selectedCity.getCityCode();
            String address = "http://guolin.tech/api/china/" + provinceCode + "/" + cityCode;
            queryFromServer(address, "county");
        }
    }

    /**
     * 本地广播   接收发送来的广播
     */
    private class LocalReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(!action.equals(LOCAL_BROADCAST)){
                return ;
            }

            boolean queryCity = intent.getBooleanExtra("query_city",false);  //判断是否需要调用查询城市

            if(queryCity){
                //获取当前点击的省份位置
                selectedProvince = provinceList.get(intent.getIntExtra("province_position",0));
                queryCities();
            }

            boolean queryCounty = intent.getBooleanExtra("query_county",false);

            if(queryCounty){
                //获取当前点击的城市位置
                selectedCity = cityList.get(intent.getIntExtra("city_position",0));
                queryCounties();
            }

            //如果点击的是县区
            int countyPosition = intent.getIntExtra("county_position", -1);
            if(countyPosition != -1){
                //获取到点击的县区的索引   然后获取到该县区的id   打开显示天气详情的Activity
                String weatherId = countyList.get(countyPosition).getWeatherId();
                Intent intent1 = new Intent(getContext(), WeatherActivity.class);
                intent1.putExtra("weather_id",weatherId);
                startActivity(intent1);
                getActivity().finish();
            }

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        localBroadcastManager.unregisterReceiver(localReceiver);    //取消广播的注册
    }
}
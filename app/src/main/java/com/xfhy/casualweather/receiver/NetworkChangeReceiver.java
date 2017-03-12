package com.xfhy.casualweather.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.xfhy.casualweather.WeatherActivity;
import com.xfhy.casualweather.service.AutoUpdateService;
import com.xfhy.casualweather.util.LogUtil;


/**
 * Created by xfhy on 2017/3/12.
 */

public class NetworkChangeReceiver extends BroadcastReceiver {

    private static final String TAG = "NetworkChangeReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        LogUtil.i(TAG,"网络发送变化");

        //获取网络状态
        ConnectivityManager connectionManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectionManager.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isAvailable()){
            Intent intent1 = new Intent(context, AutoUpdateService.class);
            context.startService(intent1);
        }
    }
}

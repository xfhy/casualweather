package com.xfhy.casualweather.util;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by xfhy on 2017/3/9.
 * HTTP请求
 */

public class HttpUtil {

    /**
     * 和服务器交互    发送一条HTTP请求
     * @param address    地址
     * @param callback   回调接口
     */
    public static void sendOkHttpRequest(String address,okhttp3.Callback callback){
        //1. 创建一个OkHttpClient实例
        OkHttpClient client = new OkHttpClient();
        //2. 构建Request对象
        Request request = new Request.Builder().url(address).build();
        //3. 执行请求   请求结果返回到callback接口
        client.newCall(request).enqueue(callback);
    }

}

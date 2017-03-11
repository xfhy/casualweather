package com.xfhy.casualweather.bean.json;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by xfhy on 2017/3/11.
 *
 * 创建一个总的实例类来引用刚刚创建的各个实体类
 *
 */

public class Weather {

    /**
     * 返回的天气数据中还会包含一项status数据,成功返回ok
     * 失败则会返回具体的原因
     */
    public String status;

    public Basic basic;

    public AQI aqi;

    public Now now;

    public Suggestion suggestion;

    @SerializedName("daily_forecast")
    public List<Forecast> forecastList;

}

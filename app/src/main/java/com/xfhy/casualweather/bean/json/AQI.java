package com.xfhy.casualweather.bean.json;

/**
 * Created by xfhy on 2017/3/11.
 * AQI是服务器返回的数据里面的一个JSON实体类
 *
 * json数据格式为
 * "aqi":{
 *     "city":{
 *         "aqi":44,
 *         "pm25":"13"
 *     }
 * }
 *
 */

public class AQI {

    public AQICity city;


    public class AQICity {
        public String aqi;
        public String pm25;
    }
}

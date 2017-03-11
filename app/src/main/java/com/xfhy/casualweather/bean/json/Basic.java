package com.xfhy.casualweather.bean.json;

import com.google.gson.annotations.SerializedName;

/**
 * Created by xfhy on 2017/3/11.
 * basic是服务器返回的数据里面的一个JSON实体类
 *
 * 结构如下:
 * "basic" :{
 *     "city":"苏州",
 *     "id":"CN101190401",
 *     "update":{
 *         "loc":"2016-08-08 21:58"
 *     }
 * }
 *
 */

public class Basic {

    @SerializedName("city")
    public String cityName;

    @SerializedName("id")
    public String weatherId;

    public Update update;

    public class Update {
        @SerializedName("loc")
        public String updateTime;
    }

}

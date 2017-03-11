package com.xfhy.casualweather.bean.json;

import com.google.gson.annotations.SerializedName;

/**
 * Created by xfhy on 2017/3/11.
 * Now是服务器返回的数据里面的一个JSON实体类
 *
 * json数据格式为
 * "now":{
 *     "tmp":"29",
 *     "cond":{
 *         "txt":"阵雨"
 *     }
 * }
 *
 */

public class Now {

    @SerializedName("tmp")
    public String temperature;

    @SerializedName("cond")
    public More more;

    public class More{
        @SerializedName("txt")
        public String info;
    }

}

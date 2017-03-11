package com.xfhy.casualweather.bean.json;

import com.google.gson.annotations.SerializedName;

/**
 * Created by xfhy on 2017/3/11.
 *
 * Forecast是服务器返回的代表着未来一天的天气信息
 *
 * json数据如下:
 * "daily_forecast":[
 *  {
 *      "date": "2016-08-31",  //预报日期
 *      "cond": {   //天气状况
 *          "txt_d": "晴",   //白天天气状况描述
 *      },
 *      "tmp": {   //温度
 "          max": "33",   //最高温度
 "          min": "19"   //最低温度
        },
 *  },
 *  {
 *      "date": "2016-09-01",  //预报日期
 *      "cond": {   //天气状况
 *          "txt_d": "晴",   //白天天气状况描述
 *      },
 *      "tmp": {   //温度
 "          max": "35",   //最高温度
 "          min": "19"   //最低温度
        },
 *  }
 *
 * ]
 *
 * 分析:可以看到daily_forecast中包含的是一个数组,数组中的每一项都代表着未来一天的天气信息.
 * 针对这种情况,我们只需要定义出单日天气的实体类就可以了,然后在声明实体类引用的时候使用集合类型来进行声明.
 *
 */

public class Forecast {

    public String date;

    @SerializedName("tmp")
    public Temperature temperature;

    @SerializedName("cond")
    public More more;

    public class Temperature {
        public String max;
        public String min;
    }

    public class More {
        @SerializedName("txt_d")
        public String info;
    }

}

package com.xfhy.casualweather.util;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.xfhy.casualweather.bean.City;
import com.xfhy.casualweather.bean.County;
import com.xfhy.casualweather.bean.Province;
import com.xfhy.casualweather.bean.json.Weather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by xfhy on 2017/3/9.
 * 解析和处理JSON数据
 */

public class Utility {

    /**
     * 解析和处理服务器返回的省级数据
     * @param response   JSON数据
     * @return
     */
    public static boolean handleProvinceResponse(String response){

        if(TextUtils.isEmpty(response)){
            return false;
        }

        try {
            //1. 构建JSON数组    外层是数组
            JSONArray allProvinces = new JSONArray(response);
            for (int i = 0; i < allProvinces.length(); i++) {
                //2. 里面一个一个的是JSONObject
                JSONObject provinceObject = allProvinces.getJSONObject(i);
                //3. 创建实体类 Province,将数据放进去
                Province province = new Province();
                province.setProvinceName(provinceObject.getString("name"));
                province.setProvinceCode(provinceObject.getInt("id"));
                //4. 保存实体类 数据  到 数据库
                province.save();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     * 解析和处理服务器返回的市级数据
     * @param response JSON数据
     * @param provinceId 省 的  id
     * @return
     */
    public static boolean handleCityResponse(String response,int provinceId){

        if(TextUtils.isEmpty(response)){
            return false;
        }

        try {
            JSONArray allCities = new JSONArray(response);
            for (int i = 0; i < allCities.length(); i++) {
                JSONObject cityObject = allCities.getJSONObject(i);
                City city = new City();
                city.setCityName(cityObject.getString("name"));
                city.setCityCode(cityObject.getInt("id"));
                city.setProvinceId(provinceId);
                city.save();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     * 解析和处理服务器返回的县区级数据
     * @param response JSON数据
     * @param cityId 城市id
     * @return
     */
    public static boolean handleCountyResponse(String response,int cityId){

        if(TextUtils.isEmpty(response)){
            return false;
        }

        try {
            JSONArray allCounties = new JSONArray(response);
            for (int i = 0; i < allCounties.length(); i++) {
                JSONObject countyObject = allCounties.getJSONObject(i);
                County county = new County();
                county.setCountyName(countyObject.getString("name"));
                county.setWeatherId(countyObject.getString("weather_id"));
                county.setCityId(cityId);
                county.save();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     * 根据json数据解析成Weather对象
     * @param response
     * @return
     */
    public static Weather handleWeatherResponse(String response) {

        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("HeWeather");
            String weatherContent = jsonArray.getJSONObject(0).toString();
            return new Gson().fromJson(weatherContent,Weather.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

}

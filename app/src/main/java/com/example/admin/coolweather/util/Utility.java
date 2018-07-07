package com.example.admin.coolweather.util;

import android.text.TextUtils;

import com.example.admin.coolweather.db.City;
import com.example.admin.coolweather.db.County;
import com.example.admin.coolweather.db.Province;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Utility {
    /*
    * 解析和处理省级数据
    * */
    public static boolean handleProvinceResquest(String response){
        if(TextUtils.isEmpty(response)){
            try{
                JSONArray allProvince = new JSONArray(response);
                for (int i=0;i<allProvince.length();i++){
                    JSONObject provinceobj = allProvince.getJSONObject(i);
                    Province province = new Province();
                    province.setProvincename(provinceobj.getString("name"));
                    province.setcode(provinceobj.getInt("id"));
                    province.save();
                }
                return true;
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return false;
    }
    /*
    * 解析和处理市级数据
    * */
    public static boolean handleCityResquest(String response,int provinceId){
        if(TextUtils.isEmpty(response)){
            try{
                JSONArray allCity = new JSONArray(response);
                for (int i=0;i<allCity.length();i++){
                    JSONObject cityobj = allCity.getJSONObject(i);
                    City city = new City();
                    city.setCityname(cityobj.getString("name"));
                    city.setcode(cityobj.getInt("id"));
                    city.setProvinceid(provinceId);
                    city.save();
                }
                return true;
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return false;
    }
    /*
     * 解析和处理县级数据
     * */
    public static boolean handleCountyResquest(String response,int cityid){
        if(TextUtils.isEmpty(response)){
            try{
                JSONArray allCounty = new JSONArray(response);
                for (int i=0;i<allCounty.length();i++){
                    JSONObject countyobj = allCounty.getJSONObject(i);
                    County county = new County();
                    county.setCountyname(countyobj.getString("name"));
                    county.setcode(countyobj.getInt("id"));
                    county.setCityid(cityid);
                    county.save();
                }
                return true;
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return false;
    }
}

package com.example.admin.coolweather.db;

import org.litepal.crud.DataSupport;

public class City extends DataSupport {

    private int id;
    private int citycode;
    private int provinceid;
    private String cityname;

    public City(int id,String cityname,int citycode,int proviceid){
        this.id = id;
        this.cityname = cityname;
        this.provinceid = proviceid;
        this.citycode = citycode;
    }

    public int getid(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public int getcode(){
        return this.citycode;
    }

    public void setcode(int citycode){
        this.citycode = citycode;
    }

    public int getProvinceid(){
        return this.provinceid;
    }

    public void setProvinceid(int provinceid){
        this.provinceid = provinceid;
    }

    public String getCityname(){
        return this.cityname;
    }

    public void setCityname(String cityname){
        this.cityname = cityname;
    }
}

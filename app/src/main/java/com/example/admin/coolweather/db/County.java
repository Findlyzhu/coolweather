package com.example.admin.coolweather.db;

import org.litepal.crud.DataSupport;

public class County extends DataSupport {
    private int id;
    private int countycode;
    private int cityid;
    private String countyname;

    public int getid(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public int getcode(){
        return this.countycode;
    }

    public void setcode(int countycode){
        this.countycode = countycode;
    }

    public int getCityid(){
        return this.cityid;
    }

    public void setCityid(int cityid){
        this.cityid = cityid;
    }

    public String getCountyname(){
        return this.countyname;
    }

    public void setCountyname(String countyname){
        this.countyname = countyname;
    }

}

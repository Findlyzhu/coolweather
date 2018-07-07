package com.example.admin.coolweather.db;

import org.litepal.crud.DataSupport;

public class Province extends DataSupport {

    private int id = 0;
    private String provincename = "";
    private int provicecode = 0;

    public int getid(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public int getcode(){
        return provicecode;
    }

    public void setcode(int code){
        this.provicecode = code;
    }

    public String getprovincename(){
        return this.provincename;
    }

    public void setProvincename(String provincename){
        this.provincename = provincename;
    }
}

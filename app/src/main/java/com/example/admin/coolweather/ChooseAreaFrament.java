package com.example.admin.coolweather;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.coolweather.db.City;
import com.example.admin.coolweather.db.County;
import com.example.admin.coolweather.db.Province;
import com.example.admin.coolweather.util.HttpUtil;
import com.example.admin.coolweather.util.Utility;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ChooseAreaFrament extends Fragment {

    public static final int LEVEL_PROVINCE  = 0;
    public static final int LEVEL_CITY      = 1;
    public static final int LEVEL_COUNTY    = 2;
    private ProgressDialog progressDialog;
    private TextView    titletext;
    private Button      backbutton;
    private ListView    listView;
    private ArrayAdapter<String> adapter;
    private List<String> dataList = new ArrayList<>();
    private List<Province> provinceList;
    private List<City>      cityList;
    private List<County>    countyList;
    private Province        selectprovince;
    private City            selectcity;
    private County          selectcounty;
    private int             currentlevel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.choose_area,container,false);
        titletext = (TextView) view.findViewById(R.id.title_text);
        backbutton = (Button)view.findViewById(R.id.back_button);
        listView = (ListView)view.findViewById(R.id.list_view);
        adapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_list_item_1,dataList);
        listView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(currentlevel == LEVEL_PROVINCE){
                    selectprovince = provinceList.get(i);
                    queryCities();
                }
                else if(currentlevel == LEVEL_CITY){
                    selectcity = cityList.get(i);
                    queryCounties();
                }
            }
        });
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentlevel == LEVEL_COUNTY){
                    queryCities();
                }
                else if (currentlevel == LEVEL_CITY){
                    queryProvinces();
                }
            }
        });
        queryProvinces();
    }

    private void queryProvinces(){
        titletext.setText("中国");
        backbutton.setVisibility(View.GONE);
        provinceList = DataSupport.findAll(Province.class);
        if(provinceList.size()>0){
            dataList.clear();
            for(Province province:provinceList){
                dataList.add(province.getprovincename());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentlevel = LEVEL_PROVINCE;
        }
        else {
            String address = "http://guolin.tech/api/china";
            queryFromServer(address,"province");
        }
    }

    private void queryCities(){
        titletext.setText(selectprovince.getprovincename());
        backbutton.setVisibility(View.VISIBLE);
        cityList = DataSupport.where("provinceid = ?",String.valueOf(selectprovince.getid())).find(City.class);
        if(cityList.size()>0){
            dataList.clear();
            for(City city:cityList){
                dataList.add(city.getCityname());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentlevel = LEVEL_CITY;
        }
        else {
            int provincecode = selectprovince.getcode();
            String address = "http://guolin.tech/api/china/" + provincecode;
            queryFromServer(address,"city");
        }
    }

    private void queryCounties(){
        titletext.setText(selectcity.getCityname());
        backbutton.setVisibility(View.VISIBLE);
        countyList = DataSupport.where("cityid = ?",String.valueOf(selectcity.getid())).find(County.class);
        if(countyList.size()>0){
            dataList.clear();
            for(County county:countyList){
                dataList.add(county.getCountyname());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentlevel = LEVEL_COUNTY;
        }
        else{
            int provincecode = selectprovince.getcode();
            int citycode = selectcity.getcode();
            String address = "http://guolin.tech/api/china/" + provincecode + "/" + citycode;
            queryFromServer(address,"county");
        }
    }

    private void queryFromServer(String address,final String type){
        showProgressDialog();
        HttpUtil.sendOkhttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(getContext(),"加载失败",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String respnseText = response.body().string();
                boolean result = false;
                if("province".equals(type)){
                    result = Utility.handleProvinceResquest(respnseText);
                }
                else if("city".equals(type)){
                    result = Utility.handleCityResquest(respnseText,selectprovince.getid());
                }
                else if("county".equals(type)){
                    result = Utility.handleCountyResquest(respnseText,selectcity.getid());
                }
                if(result){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                            if("province".equals(type)){
                                queryProvinces();
                            }
                            else if("city".equals(type)){
                                queryCities();
                            }
                            else if("county".equals(type)){
                                queryCounties();
                            }
                        }
                    });
                }
            }
        });
    }

    private void showProgressDialog(){
        if(progressDialog == null){
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("正在加载...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    private void closeProgressDialog(){
        if(progressDialog != null){
            progressDialog.dismiss();
        }
    }

}

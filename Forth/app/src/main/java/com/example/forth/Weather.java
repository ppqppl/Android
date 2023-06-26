package com.example.forth;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.google.gson.Gson;
import com.qweather.sdk.bean.base.Code;
import com.qweather.sdk.bean.base.Lang;
import com.qweather.sdk.bean.base.Range;
import com.qweather.sdk.bean.geo.GeoBean;
import com.qweather.sdk.view.HeConfig;
import com.qweather.sdk.view.QWeather;

import java.util.List;

public class Weather {
    String temp,air;
    private String pub_id = "HE2212052347071839", pri_key = "00858fa07b664cc0abfbd9cd5862573f";

    // public key:  HE2212052347071839
    // private key: 00858fa07b664cc0abfbd9cd5862573f

    public void InitWeather(String id,String key){
        HeConfig.init(id,key);  // 初始化函数中共有两个参数，第一个参数为 public key，第二个参数为 private key
        HeConfig.switchToDevService();
    }

    public void GetCityId(Context context,String city){
        QWeather.getGeoCityLookup(context, city, Range.CN, 1, Lang.ZH_HANS,
                new QWeather.OnResultGeoListener() {
            @Override
            public void onError(Throwable throwable) {
//                showToast("获取城市Id ：失败");
                Log.w("获取城市ID ：","失败 ！");
            }

            @Override
            public void onSuccess(GeoBean geoBean) {
                Log.i("获取城市编码成功",new Gson().toJson(geoBean));
                if(Code.OK == geoBean.getCode()){
                    List<GeoBean.LocationBean> locationBeanList = geoBean.getLocationBean();
                    Log.i("城市列表",locationBeanList.toString());
                }
                else {
                    Code code = geoBean.getCode();
                    Log.w("错误代码 : ",code.toString());
                }
            }
        });
    }
}

package com.example.ppqppl1;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.ppqppl1.databinding.ActivityHomeBinding;
import com.example.ppqppl1.databinding.ActivityMytoolsBinding;
import com.google.gson.Gson;
import com.qweather.sdk.bean.Basic;
import com.qweather.sdk.bean.air.AirNowBean;
import com.qweather.sdk.bean.base.Code;
import com.qweather.sdk.bean.base.Lang;
import com.qweather.sdk.bean.base.Range;
import com.qweather.sdk.bean.base.Unit;
import com.qweather.sdk.bean.geo.GeoBean;
import com.qweather.sdk.bean.weather.WeatherNowBean;
import com.qweather.sdk.view.HeConfig;
import com.qweather.sdk.view.QWeather;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class MytoolsActivity extends AppCompatActivity {

    private ActivityMytoolsBinding binding;
    private Button btn_connact,btn_camera,btn_wechat,btn_call,btn_youku,btn_msg;
    private TextView txt_time,txt_date,txt_week,txt_temp,txt_air;
    private ImageButton weatherurl;
    private String[] pub_id = new String[2],pri_key = new String[2];

    private Location location = null;
    public double latitude,longitude;

    private String WeaIconCode, tempstr, locationstr;
    private int WeaIconResID;

    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 0 , MY_PERMISSIONS_REQUEST_SEND_SMS = 1 , MY_PERMISSIONS_REQUEST_TAKE_PHOTO = 2 , MY_PERMISSIONS_REQUEST_connacts = 3;

    private String [] temp = new String[2] , air = new String[2];

    private String iconcode,weathernet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMytoolsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        btn_connact = findViewById(R.id.connactbtn);
        btn_camera = findViewById(R.id.camerabtn);
        btn_wechat = findViewById(R.id.wechatbtn);
        btn_call = findViewById(R.id.callbtn);
        btn_youku = findViewById(R.id.youkubtn);
        btn_msg = findViewById(R.id.msgbtn);
        txt_time = findViewById(R.id.timetext);
        txt_date = findViewById(R.id.datetext);
        txt_week = findViewById(R.id.weektext);
        weatherurl = findViewById(R.id.weatext);
        txt_temp = findViewById(R.id.temptext);
        txt_air = findViewById(R.id.airtext);

        pub_id[0]= "HE2212052347071839";
        pub_id[1] = "HE2212291340261933";
        pri_key[0] = "00858fa07b664cc0abfbd9cd5862573f";
        pri_key[1] = "7ddaa16c6360478e838efaa8833978db";


//获得位置服务的名称
        String serviceName = this.LOCATION_SERVICE;
//获得位置服务的管理对象
        LocationManager locationManager = (LocationManager) getSystemService(serviceName);
// 通过GPS获取定位的位置数据
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        location = locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER);
        updateToNewLocation(location);

        showToast("addddd");
        /**服务管理对象的监听器*/
        //参数1：定位的方式   参数2：监听更新间隔时间(ms)  参数3：监听更新的距离(m) 参数4：监听的方法
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 2000, 10, new LocationListener() {

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }

            public void onLocationChanged(Location location) {
                updateToNewLocation(location);
            }
        });

        weathernet = "http://www.baidu.com";

        InitWeather(pub_id[0],pri_key[0]);

        showDateInfo();
        showWeatherInfo();

        weatherurl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                // 设置要打开的网页
                intent.setData(Uri.parse(weathernet));
                startActivity(intent);
            }
        });

        btn_connact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
//                    Intent intent = new Intent(this,ContactsActivity.class);
//      startActivityForResult(intent, 0);
                    Intent intent = new Intent();
                    intent.setAction("android.intent.action.PICK");
                    intent.addCategory("android.intent.category.DEFAULT");
                    intent.setType("vnd.android.cursor.dir/phone_v2");
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    // TODO: handle exception
                    showToast("联系人打开失败！");
                }
            }
        });

        btn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(MytoolsActivity.this,
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                    // 没有获得授权，申请授权
                    if (ActivityCompat.shouldShowRequestPermissionRationale(MytoolsActivity.this,
                            Manifest.permission.CAMERA)) {
                        // 返回值：
//                          如果app之前请求过该权限,被用户拒绝, 这个方法就会返回true.
//                          如果用户之前拒绝权限的时候勾选了对话框中”Don’t ask again”的选项,那么这个方法会返回false.
//                          如果设备策略禁止应用拥有这条权限, 这个方法也返回false.
                        // 弹窗需要解释为何需要该权限，再次请求授权
                        Toast.makeText(MytoolsActivity.this, "请授权！", Toast.LENGTH_LONG).show();

                        // 帮跳转到该应用的设置界面，让用户手动授权
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);
                    }else{
                        // 不需要解释为何需要该权限，直接请求授权
                        ActivityCompat.requestPermissions(MytoolsActivity.this,
                                new String[]{Manifest.permission.CAMERA},
                                MY_PERMISSIONS_REQUEST_TAKE_PHOTO);
                    }
                }else {
                    // 已经获得授权，可以打电话
                    camera();
                }
            }
        });

        btn_wechat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    ComponentName cmp = new ComponentName("com.tencent.mm","com.tencent.mm.ui.LauncherUI");
                    intent.addCategory(Intent.CATEGORY_LAUNCHER);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setComponent(cmp);
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    // TODO: handle exception
                    showToast("没有安装微信或打开微信失败！");
                }

            }
        });

        btn_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(intent.ACTION_DIAL);
                //设置数据 后面123456789是默认要拨打的电话
//                intent.setData(Uri.parse("tel:123456789"));
                startActivity(intent);
            }
        });

        btn_youku.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    Intent intent = new Intent();
                    intent = getPackageManager().getLaunchIntentForPackage("com.youku.phone");
                    startActivity(intent);
                } catch (Exception e) {
                    showToast("手机中没有优酷视频或打开失败");
                }
            }
        });

        btn_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(intent.ACTION_SENDTO);
                //设置发送至 10086
                intent.setData(Uri.parse("smsto:10086"));
                //设置短信的默认发送内容
                intent.putExtra("sms_body","短信测试信息");

                startActivity(intent);
            }
        });
        View v = findViewById(R.id.container);
        v.getBackground().setAlpha(175);
        // 半透明状态栏
        if (Build.VERSION.SDK_INT >= 21) {//21表示5.0
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        } else if (Build.VERSION.SDK_INT >= 19) {//19表示4.4
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //虚拟键盘也透明
            // getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }



    }

    private void updateToNewLocation(Location location) {

        if (location != null) {
            DecimalFormat df = new DecimalFormat("#.00");
            String a = df.format(location.getLatitude());
            String b = df.format(location.getLongitude());
            latitude = Double.parseDouble(a);
            longitude = Double.parseDouble(b);
//            locationstr = getCity(latitude,longitude);
//            locationstr = a+","+b;
        } else {
            locationstr = "北京";
            showToast("获取位置失败");
        }

    }

    public String getCity(){

        return null;
    }

    public void camera(){
        Intent intent = new Intent(); //调用照相机
//        intent.setAction("android.media.action.STILL_IMAGE_CAMERA");
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, MY_PERMISSIONS_REQUEST_TAKE_PHOTO);
//        startActivity(intent);
        showToast("打开相机成功！");
    }

    public void showweather(){

    }

    public void showDateInfo(){
        DateFormat time = new SimpleDateFormat("HH:mm:ss");
        String timestr = time.format(new Date());
        DateFormat date = new SimpleDateFormat("yyyy-MM-dd");
        String datestr = date.format(new Date());
        DateFormat week = new SimpleDateFormat("EEEE");
        String weekstr = week.format(new Date());
        txt_time.setText(timestr);
        txt_date.setText(datestr);
        txt_week.setText(weekstr);
        new TimeThread().start();
    }

    public class TimeThread extends Thread{
        @Override
        public void run() {
            super.run();
            do {
                try {
                    //每隔一秒 发送一次消息
                    Message msg = new Message();
                    //消息内容 为MSG_ONE
                    msg.what = 1;
                    //发送
                    mHandler.sendMessage(msg);
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (true);
        }
    }

    public void showToast(String str){
        Toast toast=Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM, 0, 100);
        toast.show();
    }

    public void InitWeather(String id,String key){
        HeConfig.init(id,key);  // 初始化函数中共有两个参数，第一个参数为 public key，第二个参数为 private key
        HeConfig.switchToDevService();
    }

    public void GetCityId(String city){
        QWeather.getGeoCityLookup(MytoolsActivity.this, city, Range.CN, 1, Lang.ZH_HANS,
                new QWeather.OnResultGeoListener() {
                    @Override
                    public void onError(Throwable throwable) {
//                showToast("获取城市Id ：失败！");
                        Log.e("获取城市ID ：","失败 ！"+city+throwable.toString());
                        InitWeather(pub_id[1],pri_key[1]);
                        GetCityId("松原");
                    }

                    @Override
                    public void onSuccess(GeoBean geoBean) {
                        Log.i("获取城市编码成功",new Gson().toJson(geoBean));
                        if(Code.OK == geoBean.getCode()){
                            List<GeoBean.LocationBean> locationBeanList = geoBean.getLocationBean();
                            Log.i("城市列表",locationBeanList.get(0).getId());
                            QueryWeather(locationBeanList.get(0).getId().toString());
                            QueryAir(locationBeanList.get(0).getId().toString());
                        }
                        else {
                            showToast("解析城市错误!");
                            Code code = geoBean.getCode();
                            Log.w("解析城市错误，·错误代码 : ",code.toString());
                        }
                    }
                });
    }

    public void QueryWeather(String CityId){
        QWeather.getWeatherNow(MytoolsActivity.this, CityId, Lang.ZH_HANS, Unit.METRIC,
                new QWeather.OnResultWeatherNowListener() {
                    @Override
                    public void onError(Throwable throwable) {
//                        showToast("获取实时天气 ：失败！");
                        Log.w("获取实时天气 ：","失败 ！"+throwable.toString());
                        InitWeather(pub_id[1],pri_key[1]);
                        GetCityId(locationstr);
                    }

                    @Override
                    public void onSuccess(WeatherNowBean weatherNowBean) {
                        Log.i(TAG, "getWeather onSuccess: " + new Gson().toJson(weatherNowBean));
                        if(Code.OK == weatherNowBean.getCode()){
                            WeatherNowBean.NowBaseBean now = weatherNowBean.getNow();
                            Basic basic = weatherNowBean.getBasic();
                            Log.i("天气",weatherNowBean.toString());
                            temp[0] = now.getTemp();
                            temp[1] = now.getFeelsLike();
                            iconcode = now.getIcon();
                            weathernet = basic.getFxLink();
                            Log.i("netnetnetnet: ",weathernet);
                            tempstr = temp[0].toString()+"℃"+"（体感温度："+temp[1].toString()+"℃）";
                            Log.i("netnetnetnet: ",tempstr);
                            String weastr = now.getIcon();
                            weastr = "wea"+weastr;
                            WeaIconResID =getResources().getIdentifier(weastr, "drawable", getPackageName());
                        }
                        else {
                            Code code = weatherNowBean.getCode();
                            Log.w("解析天气错误，错误代码 : ",code.toString());
                        }
                    }
                });
    }

    public void QueryAir(String CityId){
        QWeather.getAirNow(MytoolsActivity.this, CityId, Lang.ZH_HANS,
                new QWeather.OnResultAirNowListener() {
                    @Override
                    public void onError(Throwable throwable) {
//                        showToast("获取实时空气质量 ：失败！");
                        Log.w("获取实时空气质量 ：","失败 ！");
                    }

                    @Override
                    public void onSuccess(AirNowBean airNowBean) {
                        Log.i(TAG,"getAir onSuccess: "+ new Gson().toJson(airNowBean));
                        if(Code.OK == airNowBean.getCode()){
                            AirNowBean.NowBean now = airNowBean.getNow();
                            Log.i("空气质量",airNowBean.toString());
                            air[0] = now.getAqi();
                            air[1] = now.getCategory();
                        }
                        else {
//                            showToast("解析空气质量错误!");
                            Code code = airNowBean.getCode();
                            Log.w("解析空气质量错误，错误代码 : ",code.toString());
                        }
                    }
                });
    }

    public void showWeatherInfo(){
        air = new String[]{"-1", "-1"};
        tempstr = "正在获取温度中······";
        WeaIconResID = R.drawable.weather_default;
        GetCityId(locationstr);
//        showToast(tempstr);
        txt_temp.setText(tempstr);
        weatherurl.setImageResource(WeaIconResID);
        txt_air.setText("空气指数：" + air[0] + "，质量：" + air[1]);
        new WeatherThread().start();
    }

    public class WeatherThread extends Thread{
        @Override
        public void run() {
            super.run();
            do {
                try {
                    Looper.prepare();
                    Thread.sleep(1000);
                    Message msg = new Message();
                    msg.what = 2;
                    mHandler.sendMessage(msg);
                    Looper.loop();
                } catch (InterruptedException e){
                    e.printStackTrace();
                }
            } while (true);
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1: {
                    long time = System.currentTimeMillis();
                    SimpleDateFormat time1 = new SimpleDateFormat("HH:mm:ss");
                    String timestr1 = time1.format(new Date(time));
                    DateFormat date = new SimpleDateFormat("yyyy-MM-dd");
                    String datestr = date.format(new Date());
                    DateFormat week = new SimpleDateFormat("EEEE");
                    String weekstr = week.format(new Date());
                    txt_time.setText(timestr1); //更新时间
                    txt_date.setText(datestr);
                    txt_week.setText(weekstr);
                    break;
                }
                case 2:{
//                    GetCityId("松原");
                    txt_temp.setText(tempstr);
                    weatherurl.setImageResource(WeaIconResID);
                    txt_air.setText("空气指数：" + air[0] + "，质量：" + air[1]);
                }
                default:
                    break;

            }
        }
    };

}
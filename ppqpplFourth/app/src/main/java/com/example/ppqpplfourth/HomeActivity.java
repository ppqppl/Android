package com.example.ppqpplfourth;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import com.example.ppqpplfourth.databinding.ActivityHomeBinding;
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

import java.io.File;
import java.text.SimpleDateFormat;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private ActivityHomeBinding binding;
    private Button btn_connact,btn_camera,btn_wechat,btn_call,btn_youku,btn_msg;
    private TextView txt_time,txt_date,txt_week,txt_temp,txt_air,weatherurl;
    private String pub_id = "HE2212052347071839", pri_key = "00858fa07b664cc0abfbd9cd5862573f";

    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 0 , MY_PERMISSIONS_REQUEST_SEND_SMS = 1 , MY_PERMISSIONS_REQUEST_TAKE_PHOTO = 2 , MY_PERMISSIONS_REQUEST_connacts = 3;

    private String [] temp = new String[2], air = new String[2];

    private String iconcode,weathernet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
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

        weathernet = "http://www.baidu.com";

        showDateInfo();
        showWeatherInfo();

        weatherurl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                if (ContextCompat.checkSelfPermission(HomeActivity.this,
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                    // 没有获得授权，申请授权
                    if (ActivityCompat.shouldShowRequestPermissionRationale(HomeActivity.this,
                            Manifest.permission.CAMERA)) {
                        // 返回值：
//                          如果app之前请求过该权限,被用户拒绝, 这个方法就会返回true.
//                          如果用户之前拒绝权限的时候勾选了对话框中”Don’t ask again”的选项,那么这个方法会返回false.
//                          如果设备策略禁止应用拥有这条权限, 这个方法也返回false.
                        // 弹窗需要解释为何需要该权限，再次请求授权
                        Toast.makeText(HomeActivity.this, "请授权！", Toast.LENGTH_LONG).show();

                        // 帮跳转到该应用的设置界面，让用户手动授权
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);
                    }else{
                        // 不需要解释为何需要该权限，直接请求授权
                        ActivityCompat.requestPermissions(HomeActivity.this,
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
                intent.setData(Uri.parse("tel:123456789"));
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
//                intent.putExtra("sms_body","公众号：霸道的程序猿");
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
                    Thread.sleep(1000);
                    Message msg = new Message();
                    //消息内容 为MSG_ONE
                    msg.what = 1;
                    //发送
                    mHandler.sendMessage(msg);
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
        QWeather.getGeoCityLookup(HomeActivity.this, city, Range.CN, 1, Lang.ZH_HANS,
                new QWeather.OnResultGeoListener() {
                    @Override
                    public void onError(Throwable throwable) {
//                showToast("获取城市Id ：失败！");
                        Log.w("获取城市ID ：","失败 ！");
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
        QWeather.getWeatherNow(HomeActivity.this, CityId, Lang.ZH_HANS, Unit.METRIC,
                new QWeather.OnResultWeatherNowListener() {
                    @Override
                    public void onError(Throwable throwable) {
//                        showToast("获取实时天气 ：失败！");
                        Log.w("获取实时天气 ：","失败 ！"+throwable);
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
                            String tempstr = temp[0].toString()+"℃"+"（体感温度："+temp[1].toString()+"℃）";
                            txt_temp.setText(tempstr);
                            String weastr = now.getText();
                            weatherurl.setText(weastr);
                        }
                        else {
//                            showToast("解析天气错误!");
                            Code code = weatherNowBean.getCode();
                            Log.w("解析天气错误，错误代码 : ",code.toString());
                        }
                    }
                });
//        QWeather.getWeatherNow(HomeActivity.this, CityId, Lang.ZH_HANS, Unit.METRIC, new QWeather.OnResultWeatherNowListener() {
//            @Override
//            public void onError(Throwable e) {
//                Log.i(TAG, "getWeather onError: " + e);
//            }
//
//            @Override
//            public void onSuccess(WeatherNowBean weatherBean) {
//                Log.i(TAG, "getWeather onSuccess: " + new Gson().toJson(weatherBean));
//                //先判断返回的status是否正确，当status正确时获取数据，若status不正确，可查看status对应的Code值找到原因
//                if (Code.OK == weatherBean.getCode()) {
//                    WeatherNowBean.NowBaseBean now = weatherBean.getNow();
//                } else {
//                    //在此查看返回数据失败的原因
//                    Code code = weatherBean.getCode();
//                    Log.i(TAG, "failed code: " + code);
//                }
//            }
//        });
    }

    public void QueryAir(String CityId){
        QWeather.getAirNow(HomeActivity.this, CityId, Lang.ZH_HANS,
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
                            txt_air.setText("空气质量指数：" + air[0] + "，质量：" + air[1]);
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
        InitWeather(pub_id,pri_key);
        GetCityId("松原");
        new WeatherThread().start();
    }

    public class WeatherThread extends Thread{
        @Override
        public void run() {
            super.run();
            do {
                try {
                    Thread.sleep(1800000);
                    Looper.prepare();
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
                    GetCityId("松原");
                }
                default:
                    break;

            }
        }
    };

}
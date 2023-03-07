package com.example.ppqpplsecond;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.ppqpplsecond.databinding.ActivityMainBinding;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity{

    private ImageButton btn_call,btn_camera,btn_message,btn_clock,btn_weather,btn_gps,btn_contact;
    private Button btn_baidu,btn_wechat;
    private TextView text_number,text_message;
    private ImageView firstimg;
    private String phonenumber,msgtext;
    private Uri imageUri;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 0 , MY_PERMISSIONS_REQUEST_SEND_SMS = 1 , MY_PERMISSIONS_REQUEST_TAKE_PHOTO = 2 , MY_PERMISSIONS_REQUEST_connacts = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        text_number = findViewById(R.id.text_number);
        text_message = findViewById(R.id.text_message);
        firstimg = findViewById(R.id.albumfirstimg);
        btn_call = findViewById(R.id.imgphonecall);
        btn_message = findViewById(R.id.imgmessage);
        btn_message = findViewById(R.id.imgmessage);
        btn_clock = findViewById(R.id.timebtn);
        btn_weather = findViewById(R.id.temperaturebtn);
        btn_gps = findViewById(R.id.gpsbtn);
        btn_camera = findViewById(R.id.imgcamera);
        btn_contact = findViewById(R.id.imgcontact);
        btn_baidu = findViewById(R.id.baidubtn);
        btn_wechat = findViewById(R.id.wechatbtn);

        btn_call.setOnClickListener(new View.OnClickListener() {
        @Override
            public void onClick(View view) {
                // 检查是否获得了权限（Android6.0运行时权限）
                if (ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
                    // 没有获得授权，申请授权
                    if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                            Manifest.permission.CALL_PHONE)) {
                        // 返回值：
//                          如果app之前请求过该权限,被用户拒绝, 这个方法就会返回true.
//                          如果用户之前拒绝权限的时候勾选了对话框中”Don’t ask again”的选项,那么这个方法会返回false.
//                          如果设备策略禁止应用拥有这条权限, 这个方法也返回false.
                        // 弹窗需要解释为何需要该权限，再次请求授权
                        Toast.makeText(MainActivity.this, "请授权！", Toast.LENGTH_LONG).show();

                        // 帮跳转到该应用的设置界面，让用户手动授权
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);
                    }else{
                        // 不需要解释为何需要该权限，直接请求授权
                        ActivityCompat.requestPermissions(MainActivity.this,
                                new String[]{Manifest.permission.CALL_PHONE},
                                MY_PERMISSIONS_REQUEST_CALL_PHONE);
                    }
                }else {
                    // 已经获得授权，可以打电话
                    call();
                }
            }
        });

        btn_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 检查是否获得了权限（Android6.0运行时权限）
                if (ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED){
                    // 没有获得授权，申请授权
                    if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                            Manifest.permission.SEND_SMS)) {
                        // 返回值：
//                          如果app之前请求过该权限,被用户拒绝, 这个方法就会返回true.
//                          如果用户之前拒绝权限的时候勾选了对话框中”Don’t ask again”的选项,那么这个方法会返回false.
//                          如果设备策略禁止应用拥有这条权限, 这个方法也返回false.
                        // 弹窗需要解释为何需要该权限，再次请求授权
                        Toast.makeText(MainActivity.this, "请授权！", Toast.LENGTH_LONG).show();

                        // 帮跳转到该应用的设置界面，让用户手动授权
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);
                    }else{
                        // 不需要解释为何需要该权限，直接请求授权
                        ActivityCompat.requestPermissions(MainActivity.this,
                                new String[]{Manifest.permission.SEND_SMS},
                                MY_PERMISSIONS_REQUEST_SEND_SMS);
                    }
                }else {
                    // 已经获得授权，可以打电话
                    sendmsg();
                }
            }
        });

        btn_baidu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                // 设置要打开的网页
                intent.setData(Uri.parse("http://www.baidu.com"));
                startActivity(intent);
            }
        });

        btn_wechat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

        btn_clock.setOnClickListener(view->{
            Intent intent = new Intent();
            intent.setClass(MainActivity.this,ClockActivity.class);
            startActivity(intent);
        });

        btn_weather.setOnClickListener(view ->{
            Intent intent = new Intent();
            intent.setClass(MainActivity.this,WeatherActivity.class);
            startActivity(intent);
        });

        btn_gps.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.setClass(MainActivity.this,GpsActivity.class);
            startActivity(intent);
        });

        btn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {// 检查是否获得了权限（Android6.0运行时权限）
                if (ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                    // 没有获得授权，申请授权
                    if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                            Manifest.permission.CAMERA)) {
                        // 返回值：
//                          如果app之前请求过该权限,被用户拒绝, 这个方法就会返回true.
//                          如果用户之前拒绝权限的时候勾选了对话框中”Don’t ask again”的选项,那么这个方法会返回false.
//                          如果设备策略禁止应用拥有这条权限, 这个方法也返回false.
                        // 弹窗需要解释为何需要该权限，再次请求授权
                        Toast.makeText(MainActivity.this, "请授权！", Toast.LENGTH_LONG).show();

                        // 帮跳转到该应用的设置界面，让用户手动授权
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);
                    }else{
                        // 不需要解释为何需要该权限，直接请求授权
                        ActivityCompat.requestPermissions(MainActivity.this,
                                new String[]{Manifest.permission.CAMERA},
                                MY_PERMISSIONS_REQUEST_TAKE_PHOTO);
                    }
                }else {
                    // 已经获得授权，可以打电话
                    camera();
                }
            }
        });

        btn_contact.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.setClass(MainActivity.this,ContactActivity.class);
            startActivityForResult(intent,MY_PERMISSIONS_REQUEST_connacts);
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

    public void call(){
        Intent intent = new Intent(Intent.ACTION_CALL);
        phonenumber = text_number.getText().toString();
        intent.setData(Uri.parse("tel:"+phonenumber));
//        startActivity(intent);
        startActivityForResult(intent, MY_PERMISSIONS_REQUEST_CALL_PHONE);
        showToast("拨打电话成功！");
    }

    public void sendmsg(){
        phonenumber = text_number.getText().toString();
        msgtext = text_message.getText().toString();
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phonenumber, null, msgtext, null, null);
        showToast("发送短信成功！");
    }

    public void camera(){
        Intent intent = new Intent(); //调用照相机
//        intent.setAction("android.media.action.STILL_IMAGE_CAMERA");
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, MY_PERMISSIONS_REQUEST_TAKE_PHOTO);
//        startActivity(intent);
        showToast("打开相机成功！");
    }

    public Bitmap rotateImage(Bitmap bitmap, float degree) {
        //create new matrix
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap bmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return bmp;
    }

    public void showToast(String str){
        Toast toast=Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM, 0, 100);
        toast.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CALL_PHONE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 授权成功，继续打电话
                    showToast("拨打电话成功！");
                    call();
                } else {
                    // 授权失败！
                    showToast("授权失败！");
                }
                break;
            }
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    showToast("发送短信成功！");
                    sendmsg();
                } else {
                    // 授权失败！
                    showToast("授权失败！");
                }
                break;
            }
        }
    }
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bundle bundle = data.getExtras();
        if (data != null) {
            switch (requestCode) {
                case MY_PERMISSIONS_REQUEST_TAKE_PHOTO: {
                    Bitmap bitmap = (Bitmap) bundle.get("data");
                    //在这里对图片的方向做一个调整(顺时针调整90度)
//                    bitmap =rotateImage(bitmap, 90);
                    firstimg.setImageBitmap(bitmap);
                    showToast("图片加载成功！");
                    break;
                }
                case MY_PERMISSIONS_REQUEST_connacts: {
                    String num = data.getStringExtra("num");
                    text_number.setText(num);
                    showToast("显示电话号成功！");
                    break;
                }
            }
        }
    }
}
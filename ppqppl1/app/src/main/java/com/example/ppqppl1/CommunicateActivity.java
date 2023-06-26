package com.example.ppqppl1;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
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

import com.example.ppqppl1.databinding.ActivityMainBinding;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import utils.Permission;

public class CommunicateActivity extends AppCompatActivity{

    private ImageButton btn_call,btn_camera,btn_message,btn_clock,btn_weather,btn_gps,btn_contact;
    private Button btn_baidu,btn_wechat;
    private TextView text_number,text_message;
    private ImageView firstimg;
    private String phonenumber,msgtext;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 0 , MY_PERMISSIONS_REQUEST_SEND_SMS = 1 , MY_PERMISSIONS_REQUEST_TAKE_PHOTO = 2 , MY_PERMISSIONS_REQUEST_connacts = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_communicate);

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
                if(Permission.checknowPermission(CommunicateActivity.this, Manifest.permission.CALL_PHONE)){
                    call();
                }
                else{
                    showToast("需要手动获取相机权限");
                }
            }
        });

        btn_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 检查是否获得了权限（Android6.0运行时权限）
                if (ContextCompat.checkSelfPermission(CommunicateActivity.this,
                        Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED){
                    // 没有获得授权，申请授权
                    if (ActivityCompat.shouldShowRequestPermissionRationale(CommunicateActivity.this,
                            Manifest.permission.SEND_SMS)) {
                        // 返回值：
//                          如果app之前请求过该权限,被用户拒绝, 这个方法就会返回true.
//                          如果用户之前拒绝权限的时候勾选了对话框中”Don’t ask again”的选项,那么这个方法会返回false.
//                          如果设备策略禁止应用拥有这条权限, 这个方法也返回false.
                        // 弹窗需要解释为何需要该权限，再次请求授权
                        Toast.makeText(CommunicateActivity.this, "请授权！", Toast.LENGTH_LONG).show();

                        // 帮跳转到该应用的设置界面，让用户手动授权
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);
                    }else{
                        // 不需要解释为何需要该权限，直接请求授权
                        ActivityCompat.requestPermissions(CommunicateActivity.this,
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
            intent.setClass(CommunicateActivity.this,DateActivity.class);
            startActivity(intent);
        });

        btn_weather.setOnClickListener(view ->{
            Intent intent = new Intent();
            intent.setClass(CommunicateActivity.this,GetWeatherActivity.class);
            startActivity(intent);
        });

        btn_gps.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.setClass(CommunicateActivity.this,GetGpsActivity.class);
            startActivity(intent);
        });

        btn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {// 检查是否获得了权限（Android6.0运行时权限）
                if (ContextCompat.checkSelfPermission(CommunicateActivity.this,
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                    // 没有获得授权，申请授权
                    if (ActivityCompat.shouldShowRequestPermissionRationale(CommunicateActivity.this,
                            Manifest.permission.CAMERA)) {
                        // 返回值：
//                          如果app之前请求过该权限,被用户拒绝, 这个方法就会返回true.
//                          如果用户之前拒绝权限的时候勾选了对话框中”Don’t ask again”的选项,那么这个方法会返回false.
//                          如果设备策略禁止应用拥有这条权限, 这个方法也返回false.
                        // 弹窗需要解释为何需要该权限，再次请求授权
                        Toast.makeText(CommunicateActivity.this, "请授权！", Toast.LENGTH_LONG).show();

                        // 帮跳转到该应用的设置界面，让用户手动授权
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);
                    }else{
                        // 不需要解释为何需要该权限，直接请求授权
                        ActivityCompat.requestPermissions(CommunicateActivity.this,
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
            try {
//                    Intent intent = new Intent(this,ContactsActivity.class);
//      startActivityForResult(intent, 0);
//                Intent intent = new Intent();
//                intent.setAction("android.intent.action.PICK");
//                intent.addCategory("android.intent.category.DEFAULT");
//                intent.setType("vnd.android.cursor.dir/phone_v2");
//                startActivityForResult(intent,MY_PERMISSIONS_REQUEST_connacts);
//                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
//                startActivityForResult(intent,MY_PERMISSIONS_REQUEST_connacts);
                Intent intent = new Intent(Intent.ACTION_PICK);
                //从有电话号码的联系人中选取
                intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
                startActivityForResult(intent, MY_PERMISSIONS_REQUEST_connacts);
            } catch (ActivityNotFoundException e) {
                // TODO: handle exception
                showToast("联系人打开失败！");
            }
//            Intent intent = new Intent();
//            intent.setClass(CommunicateActivity.this,ContactActivity.class);
//            startActivityForResult(intent,MY_PERMISSIONS_REQUEST_connacts);
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
        if(msgtext != null && !msgtext.equals("")) {

            if(phonenumber != null && !phonenumber.equals("")) {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(phonenumber, null, msgtext, null, null);
                showToast("发送短信成功！");
            }
            else {
                showToast("电话号码不能为空");
            }
        }
        else {
            showToast("短信内容不能为空");
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

    private String getPhoneNumber(Intent intent){
        Cursor cursor = null;
        Cursor phone = null;
        try{
            String[] projections = {ContactsContract.Contacts._ID,ContactsContract.Contacts.HAS_PHONE_NUMBER};
            cursor = getContentResolver().query(intent.getData(),projections, null, null, null);
            if ((cursor == null) || (!cursor.moveToFirst())){
                return null;
            }
            int _id = cursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID);
            String id = cursor.getString(_id);
            int has_phone_number = cursor.getColumnIndexOrThrow(ContactsContract.Contacts.HAS_PHONE_NUMBER);
            int hasPhoneNumber = cursor.getInt(has_phone_number);
            String phoneNumber = null;
            if(hasPhoneNumber>0){
                phone = getContentResolver().query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "="
                                + id, null, null);
                while(phone.moveToNext()){
                    int index = phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                    String number = phone.getString(index);
                    phoneNumber = number;
                }
            }
            return phoneNumber;
        }catch(Exception e){

        }finally{
            if (cursor != null) cursor.close();
            if(phone != null) phone.close();
        }
        return null;
    }


    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            Bundle bundle = data.getExtras();
            switch (requestCode) {
                case MY_PERMISSIONS_REQUEST_TAKE_PHOTO: {
                    Bitmap bitmap = (Bitmap) bundle.get("data");
                    //在这里对图片的方向做一个调整(顺时针调整90度)
//                    bitmap =rotateImage(bitmap, 90);
                    if(bitmap == null){
                        showToast("asd");
                    }
                    firstimg.setImageBitmap(bitmap);
                    showToast("图片加载成功！");
                    break;
                }
                case MY_PERMISSIONS_REQUEST_connacts: {
//                    String num = data.getStringExtra("num");
//                    text_number.setText(num);
                    Uri contactUri = data.getData();
                    String[] projection = new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER};
                    Cursor cursor = getContentResolver().query(contactUri, projection,
                            null, null, null);
                    //
                    if (cursor != null && cursor.moveToFirst()) {
                        int numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                        //电话号码
                        String number = cursor.getString(numberIndex);
                        text_number.setText(number);
                    }
                    break;
                }
            }
        }
    }



}
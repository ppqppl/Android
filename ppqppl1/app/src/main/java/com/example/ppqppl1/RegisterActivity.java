package com.example.ppqppl1;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.ppqppl1.databinding.ActivityRegisterBinding;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.List;

import bean.Constant;
import bean.Student;
import bean.Teacher;
import bean.User;
import utils.SqLiteHelper;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;

    private EditText txt_account,txt_pwd,txt_name,txt_phone;
    private Button btn_register;

    private String account,pwd,name,phone,ID,authority;
    private int account_length,pwd_length;

    private String insert_tea_url = AdminActivity.url_sercver+"/addtea";
    private String find_tea_byid_url = AdminActivity.url_sercver+"/findbyteaid";
    private String url_findallstu = AdminActivity.url_sercver+"/student";
    private String url_findalltea = AdminActivity.url_sercver+"/teacher";
    private String find_url = AdminActivity.url_sercver+"/findbyteaid";

    private List<Teacher> tea_find = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        txt_account = findViewById(R.id.Accounttxt);
        txt_pwd = findViewById(R.id.Pwdtxt);
        txt_name = findViewById(R.id.Nametxt);
        txt_phone = findViewById(R.id.Phonenumtxt);
        btn_register = findViewById(R.id.Registerbtn);


        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                account = String.valueOf(txt_account.getText());
                pwd = String.valueOf(txt_pwd.getText());
                name = String.valueOf(txt_name.getText());
                phone = String.valueOf(txt_phone.getText());
                account_length = account.length();
                pwd_length = pwd.length();
                if(account.equals("")||pwd.equals("")||name.equals("")||phone.equals("")){
                    showToast("注册信息必须全部填写完整");
                }
                else {
                    if(account_length<8){
                        showToast("账号最少为8位");
                    }
                    else if(pwd_length<8){
                        showToast("密码最少为8位");
                    }
                    else if(!phone.substring(0,1).equals("1")||phone.length()<11||phone.length()>11){
                        showToast("电话号码格式错误");
                    }
                    else if(ifUserExist(account)){
                        showToast("账号已经存在");
                    }
                    else if(findById(account)){
                        showToast("ID已存在");
                    }
                    else{
                        Teacher tea = new Teacher(account,name,phone,pwd);
                        insertInto(tea);
                        getstus();
                        finish();
                    }
                }
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

    public void getstus(){
        OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder().url(url_findallstu).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.v("Fail", e.getMessage());
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String responseStr = response.body().string();
                Log.e("response",responseStr);
                MainActivity.stus = com.alibaba.fastjson.JSONArray.parseArray(responseStr,Student.class);
                getteas();
            }
        });
    }
    public void getteas(){
        OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder().url(url_findalltea).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.v("Fail", e.getMessage());
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String responseStr = response.body().string();
                Log.e("response",responseStr);
                MainActivity.teas = com.alibaba.fastjson.JSONArray.parseArray(responseStr, Teacher.class);
                Message msg= myhandler.obtainMessage();
                msg.what = 1;
                myhandler.sendMessage(msg);
            }
        });
    }

    private boolean findById(String Id) {
        final boolean judge = false;
        String idStr = JSON.toJSONString(Id);
        OkHttpClient mOkHttpClient = new OkHttpClient();
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("teaid", idStr);
        final Request request = new Request.Builder()
                .url(find_url)
                .post(builder.build())
                .build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.v("Fail", e.getMessage());
                tea_find.clear();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String responseStr = response.body().string();
                Log.e("response",responseStr);
                tea_find = com.alibaba.fastjson.JSONArray.parseArray(responseStr, Teacher.class);
            }
        });
        if(tea_find!=null && tea_find.size()!=0) {
            return true;
        }
        return false;
    }

    private void insertInto(Teacher tea) {
        String stuStr = JSON.toJSONString(tea);
        OkHttpClient mOkHttpClient = new OkHttpClient();
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("tea", stuStr);
        final Request request = new Request.Builder()
                .url(insert_tea_url)
                .post(builder.build())
                .build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.v("Fail", e.getMessage());
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String str = response.body().string();
                Log.e("response",str);
            }
        });
    }

    public void showToast(String str){
        Toast toast=Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM, 0, 100);
        toast.show();
    }

    public boolean ifUserExist(String account){
        for(int i=0;i<MainActivity.users.size();i++){
            if(MainActivity.users.get(i).getAccount().equals(account)){
                return true;
            }
        }
        return false;
    }
    public boolean ifPhoneExist(String phone){
        for(int i=0;i<MainActivity.users.size();i++){
            if(MainActivity.users.get(i).getPhonenum().equals(phone)){
                return true;
            }
        }
        return false;
    }
    private Handler myhandler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            switch (msg.what){
                case 1:{
                    showToast("教师信息添加成功");
                    break;
                }
                default:{
                    break;
                }
            }
        }
    };

}
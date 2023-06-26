package com.example.ppqppl1;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ppqppl1.databinding.ActivityMainBinding;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import bean.Student;
import bean.Teacher;
import bean.User;
import bean.Constant;
import utils.DbManger;
import utils.SqLiteHelper;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private Button login_btn,bu_btn,resetpwd_btn,register_btn;
    private EditText txt_account,txt_pwd;
    private TextView txt_iflogin;
    private CheckBox ck_password,ck_login;

    private SharedPreferences sp;

    private SQLiteDatabase db_user = null;
    private SqLiteHelper sqLiteHelper = new SqLiteHelper(MainActivity.this);

    public static List<User> users = new ArrayList<>();
    public static  Student stu_login = null;
    public static  Teacher tea_login = null;
    public static List<Student> stus = new ArrayList<>();
    public static List<Teacher> teas = new ArrayList<>();
    public static int num = -1;

    public static String Login_identity;

    private String url_findallstu = AdminActivity.url_sercver+"/student";
    private String url_findalltea = AdminActivity.url_sercver+"/teacher";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        login_btn = findViewById(R.id.Loginbtn);
        bu_btn = findViewById(R.id.buquan);
        txt_account = findViewById(R.id.Accounttxt);
        txt_pwd = findViewById(R.id.Pwdtxt);
        txt_iflogin = findViewById(R.id.Loginprotxt);
        ck_password = findViewById(R.id.remindpwdcheckbox);
        ck_login = findViewById(R.id.autologincheckbox);
        resetpwd_btn = findViewById(R.id.resetpwdbtn);
        register_btn = findViewById(R.id.registerbtn);

        CreateDB();
        get_users();
        getstus();
//        getteas();


        sp = getSharedPreferences("Personal", MODE_PRIVATE);
        //登录方法
        LoginMethod();
        //程序再次进入获取SharedPreferences中的数据
        AgainInto();

        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View dialogView = LayoutInflater.from(MainActivity.this).inflate(R.layout.register_choose_layout, null);
                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                alertDialog.setView(dialogView, 0, 0, 0, 0);
                alertDialog.show();
                Button btn_stu = dialogView.findViewById(R.id.sturegisterbtn);
                Button btn_tea = dialogView.findViewById(R.id.tearegisterbtn);
                btn_stu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setClass(MainActivity.this,AddstudentActivity.class);
                        intent.putExtra("backnum",1);
                        startActivity(intent);
                        alertDialog.dismiss();
                    }
                });
                btn_tea.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setClass(MainActivity.this,RegisterActivity.class);
                        startActivity(intent);
                        alertDialog.dismiss();
                    }
                });
            }
        });

        bu_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txt_account.setText("ppqppl");
                txt_pwd.setText("1911817187");
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

    private void AgainInto() {
        //如果获取为空就返回默认值
        boolean ck1 = sp.getBoolean("ck_password", false);
        boolean ck2 = sp.getBoolean("ck_login", false);

        //如果是记住密码
        if (ck1){
            String name=sp.getString("name","");
            String password=sp.getString("password","");
            txt_account.setText(name);
            txt_pwd.setText(password);
            //记住密码打上√
            ck_password.setChecked(true);
        }
        //如果是自动登录
        if (ck2){
            ck_login.setChecked(true);
            String name=txt_account.getText().toString().trim();
            String password=txt_pwd.getText().toString().trim();
            if(FindUserByAccount(name,password)){
                Intent intent = new Intent();
                intent.setClass(MainActivity.this,HomeActivity.class);
                GetFilePermission();
                startActivity(intent);
                txt_iflogin.setText("欢迎使用本系统，请登录");
            }
            else {
                txt_iflogin.setText("账号或密码错误！");
            }
        }
    }

    private void LoginMethod() {
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=txt_account.getText().toString().trim();
                String password=txt_pwd.getText().toString().trim();
                Login_identity = "";
                //判断用户名和密码是否为空
                if (TextUtils.isEmpty(name)|| TextUtils.isEmpty(password)){
                    showToast("账号密码不能为空");
                }else {
                    //如果记录密码是勾选的
                    if (ck_password.isChecked()){
                        //把用户名和密码保存在SharedPreferences中
                        sp.edit().putString("name",name).apply();
                        sp.edit().putString("password",password).apply();
                        sp.edit().putBoolean("ck_password",true).apply();
                    }else {//没有勾选,保存空值
                        sp.edit().putString("name","").apply();
                        sp.edit().putString("password","").apply();
                        sp.edit().putBoolean("ck_password",false).apply();
                    }
                    //如果自动登录是勾选的
                    if (ck_login.isChecked()){
                        sp.edit().putBoolean("ck_login",true).apply();
                    }else {
                        sp.edit().putBoolean("ck_login",false).apply();
                    }
                    Log.e("账号密码1111","   "+name+"   "+password);
                    if(FindUserByAccount(name,password)){
                        Intent intent = new Intent();
                        intent.setClass(MainActivity.this,HomeActivity.class);
                        GetFilePermission();
                        startActivity(intent);
                        txt_iflogin.setText("欢迎使用本系统，请登录");
                    }
                    else {
                        txt_iflogin.setText("账号或密码错误！");
                    }
                }
            }
        });

    }

    private void GetFilePermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.MANAGE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {// android 11  且 不是已经被拒绝
            // 先判断有没有权限
            if (!Environment.isExternalStorageManager()) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.setData(Uri.parse("package:" + getPackageName()));
                startActivity(intent);
            }
        }
    }

    public void showToast(String str){
        Toast toast=Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM, 0, 100);
        toast.show();
    }

    @Override
    protected void onStart() {
        super.onStart();
//        Permission.checkPermission(this);
    }



    public void CreateDB(){
        db_user = sqLiteHelper.getReadableDatabase();
    }

    public void get_users(){
        db_user = sqLiteHelper.getWritableDatabase();
        String selectSql="select * from "+ Constant.TABLE_NAME_ACC;
        Cursor cursor= DbManger.UserselectDataBySql(db_user,selectSql,null);
        users = DbManger.UsercursorToList(cursor);
        Log.e("users", String.valueOf(users));
    }

    public boolean FindUserByAccount(String account, String pwd){
        num = -1;
        if(account.equals("ppqppl")&&pwd.equals("1911817187")){
            Login_identity = "admin";
            return true;
        }
        if((stus==null&&teas==null)||(stus.size()==0&&teas.size()==0)){
            return false;
        }
        for(int i=0;i<stus.size();i++){
            if(stus.get(i).getStu_id().equals(account)){
                num = i;
                Log.e("账号密码","   "+account+"   "+pwd);
                break;
            }
        }
        if(num != -1){
            if(stus.get(num).getPwd().equals(pwd)){
                showToast("学生登录成功！");
                Login_identity = "stu";
                stu_login = stus.get(num);
                return true;
            }
            showToast("密码错误！");
            return false;
        }
        for(int i=0;i<teas.size();i++){
            if(teas.get(i).getId().equals(account)){
                num = i;
                break;
            }
        }
        if(num != -1){
            if(teas.get(num).getPwd().equals(pwd)){
                showToast("教师登录成功！");
                Login_identity = "tea";
                tea_login = teas.get(num);
                return true;
            }
            showToast("密码错误！");
            return false;
        }
        showToast("账号不存在！");
        return false;
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
                stus = com.alibaba.fastjson.JSONArray.parseArray(responseStr,Student.class);
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
                teas = com.alibaba.fastjson.JSONArray.parseArray(responseStr, Teacher.class);
                Message msg= myhandler.obtainMessage();
                msg.what = 1;
                myhandler.sendMessage(msg);
            }
        });
    }

    private Handler myhandler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            switch (msg.what){
                case 1:{
                    AgainInto();
                    break;
                }
                case 2:{
                    showToast("信息更新成功");
                    break;
                }
                default:{
                    break;
                }
            }
        }
    };

}
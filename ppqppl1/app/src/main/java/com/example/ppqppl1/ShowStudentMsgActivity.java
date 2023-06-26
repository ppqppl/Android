package com.example.ppqppl1;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.ppqppl1.databinding.ActivityShowstudentmsgBinding;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

import bean.Student;
import bean.Teacher;

public class ShowStudentMsgActivity extends AppCompatActivity {

    private ActivityShowstudentmsgBinding binding;

    private TextView txt_id,txt_name,txt_age,txt_phone,txt_data,txt_major,txt_class,txt_sex,txt_pwd;
    private Button btn_back,btn_modify,btn_save,btn_cancle;
    private LinearLayout btn_layout,pwd_layout;

    private int num;
    private String insertstu_url = AdminActivity.url_sercver+"/addstu";
    private String url_findallstu = AdminActivity.url_sercver+"/student";
    private String url_findalltea = AdminActivity.url_sercver+"/teacher";

    private boolean if_modify = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityShowstudentmsgBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        txt_id = findViewById(R.id.stu_idtxt);
        txt_name = findViewById(R.id.nametxt);
        txt_age = findViewById(R.id.agetxt);
        txt_sex = findViewById(R.id.sextxt);
        txt_phone = findViewById(R.id.phonetxt);
        txt_data = findViewById(R.id.traimDatetxt);
        txt_major = findViewById(R.id.majortxt);
        txt_class = findViewById(R.id.classtxt);
        txt_pwd = findViewById(R.id.pwdtxt);
        btn_back = findViewById(R.id.backbtn);
        btn_modify = findViewById(R.id.modifymsgbtn);
        btn_save = findViewById(R.id.savebtn);
        btn_cancle = findViewById(R.id.cancelbtn);
        btn_layout = findViewById(R.id.btnlayout);
        pwd_layout = findViewById(R.id.pwdlayout);

        Intent getnum = this.getIntent();
        num = getnum.getIntExtra("stu_num",-1);

        init_editview();
        initByIdentify();

        txt_data.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN)
                {
                    showDatePickDlg();
                    return true;
                }
                return false;
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if_modify = true;
                init_editview();
                btn_layout.setVisibility(View.VISIBLE);
                btn_modify.setVisibility(View.GONE);
            }
        });

        btn_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if_modify = false;
                initByIdentify();
                init_editview();
                btn_layout.setVisibility(View.GONE);
                btn_modify.setVisibility(View.VISIBLE);
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if_modify = false;
                init_editview();
                Student stu = new Student(txt_id.getText().toString(),txt_name.getText().toString(),Integer.parseInt(txt_age.getText().toString()),
                        txt_sex.getText().toString(),txt_phone.getText().toString(),txt_data.getText().toString(),MainActivity.stu_login.getModifyDateTime(),
                        txt_major.getText().toString(),txt_class.getText().toString(),txt_pwd.getText().toString(),MainActivity.stu_login.getIfchosecourse());
                insertInto(stu);
                btn_layout.setVisibility(View.GONE);
                btn_modify.setVisibility(View.VISIBLE);
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

    public void initByIdentify(){
        if(num == -1){
            btn_back.setVisibility(View.GONE);
            btn_layout.setVisibility(View.GONE);
            if(MainActivity.Login_identity.equals("stu")){
                Student data = MainActivity.stu_login;
                txt_id.setText(data.getStu_id());
                txt_name.setText(data.getName());
                txt_age.setText(String.valueOf(data.getAge()));
                txt_sex.setText(data.getSex());
                txt_phone.setText(data.getPhoneNumber());
                txt_data.setText(data.getTrainDate());
                txt_major.setText(data.getMajor());
                txt_class.setText(data.getClass_num());
                txt_pwd.setText(data.getPwd());
            }
            else {
                txt_id.setText("学生不存在");
                showToast("显示错误");
            }
        }
        else {
            Student data = AdminActivity.students.get(num);
            btn_modify.setVisibility(View.GONE);
            btn_layout.setVisibility(View.GONE);
            txt_id.setText(data.getStu_id());
            txt_name.setText(data.getName());
            txt_age.setText(String.valueOf(data.getAge()));
            txt_sex.setText(data.getSex());
            txt_phone.setText(data.getPhoneNumber());
            txt_data.setText(data.getTrainDate());
            txt_major.setText(data.getMajor());
            txt_class.setText(data.getClass_num());
            txt_pwd.setText(data.getPwd());
        }
    }

    public void init_editview(){
            txt_id.setEnabled(false);
            txt_name.setEnabled(if_modify);
            txt_age.setEnabled(if_modify);
            txt_sex.setEnabled(if_modify);
            txt_phone.setEnabled(if_modify);
            txt_data.setEnabled(if_modify);
            txt_major.setEnabled(if_modify);
            txt_class.setEnabled(if_modify);
            txt_pwd.setEnabled(if_modify);
            if(if_modify){
                pwd_layout.setVisibility(View.VISIBLE);
            }
            else{
                pwd_layout.setVisibility(View.GONE);
            }
    }

    private void showDatePickDlg() {
        Calendar calendar = Calendar.getInstance();
//        获取当前日期
        final int mYear = calendar.get(Calendar.YEAR);
        final int mMonth = calendar.get(Calendar.MONTH);
        final int mDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(ShowStudentMsgActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//                        判断选择年是否小于当前年
                        if (year > mYear) {
                            view.updateDate(mYear, mMonth, mDayOfMonth);
                            showToast("入学日期不能晚于今天");
                            return;
                        }
//                        判断选择月是否小于当前月
                        if (year == mYear && month > mMonth) {
                            view.updateDate(mYear, mMonth, mDayOfMonth);
                            showToast("入学日期不能晚于今天");
                            return;
                        }
//                        判断选择日是否小于当前日
                        if (year == mYear && month == mMonth && dayOfMonth > mDayOfMonth) {
                            view.updateDate(mYear, mMonth, mDayOfMonth);
                            showToast("入学日期不能晚于今天");
                            return;
                        }

                        ShowStudentMsgActivity.this.txt_data.setText(year+"-"+(month+1)+"-"+dayOfMonth);
                    }
                },
                mYear,
                mMonth,
                mDayOfMonth);
        datePickerDialog.show();
    }

    private void insertInto(Student stu) {
        String stuStr = JSON.toJSONString(stu);
        OkHttpClient mOkHttpClient = new OkHttpClient();
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("stu", stuStr);
        final Request request = new Request.Builder()
                .url(insertstu_url)
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
                getstus();
            }
        });
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

    public void showToast(String str){
        Toast toast=Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM, 0, 100);
        toast.show();
    }

    private Handler myhandler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            switch (msg.what){
                case 1:{
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
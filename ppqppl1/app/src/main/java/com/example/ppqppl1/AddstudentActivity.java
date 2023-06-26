package com.example.ppqppl1;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.ppqppl1.databinding.ActivityAddstudentBinding;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import bean.Photo;
import bean.Student;
import bean.Teacher;

public class AddstudentActivity extends AppCompatActivity {

    private ActivityAddstudentBinding binding;

    private EditText txt_id,txt_name,txt_age,txt_phone,txt_data,txt_major,txt_class,txt_pwd;
    private Button btn_save,btn_renew;
    private RadioButton rbtn_man,rbtn_woman;
    private RadioGroup rg_sex;

    private String modifydate,sex;
    private String insert_url = AdminActivity.url_sercver+"/addstu";
    private String find_url = AdminActivity.url_sercver+"/findbyid";
    private String url_findallstu = AdminActivity.url_sercver+"/student";
    private String url_findalltea = AdminActivity.url_sercver+"/teacher";

    private Student data = null;
    private List<Student> stu_find = new ArrayList<>();

    private int num,backnum;

    private static final int DATE_PICKER_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAddstudentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        DateFormat filedate = new java.text.SimpleDateFormat("yy-MM-dd");
        modifydate = filedate.format(new Date());

        txt_id = findViewById(R.id.stu_idtxt);
        txt_name = findViewById(R.id.nametxt);
        txt_age = findViewById(R.id.agetxt);
        txt_phone = findViewById(R.id.phonetxt);
        txt_data = findViewById(R.id.traimDatetxt);
        txt_major = findViewById(R.id.majortxt);
        txt_class = findViewById(R.id.classtxt);
        txt_pwd = findViewById(R.id.pwdtxt);

        btn_renew = findViewById(R.id.renewbtn);
        btn_save = findViewById(R.id.savebtn);

        rbtn_man = findViewById(R.id.manradiobox);
        rbtn_woman = findViewById(R.id.womanradiobox);
        rg_sex = findViewById(R.id.sexradiogroup);

        rg_sex.check(rbtn_man.getId());
        txt_data.setText(getCurrentDate());
        txt_age.setText("0");

        Intent getnum = this.getIntent();
        num = getnum.getIntExtra("stu_num",-1);
        backnum = getnum.getIntExtra("backnum",-1);

        if(num != -1){
            data = AdminActivity.students.get(num);
            txt_id.setText(data.getStu_id());
            txt_name.setText(data.getName());
            txt_phone.setText(data.getPhoneNumber());
            txt_major.setText(data.getMajor());
            txt_class.setText(data.getClass_num());
            txt_data.setText(data.getTrainDate());
            txt_age.setText(String.valueOf(data.getAge()));
            txt_pwd.setText(data.getPwd());
            if(data.getSex().equals("男")){
                rg_sex.check(rbtn_man.getId());
            }
            else{
                rg_sex.check(rbtn_woman.getId());
            }
            txt_id.setEnabled(false);
//            android:focusable="false"
//            android:focusableInTouchMode="false"
        }

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


        btn_renew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(num == -1) {
                    txt_id.setText("");
                    txt_name.setText("");
                    txt_phone.setText("");
                    txt_major.setText("");
                    txt_class.setText("");
                    rg_sex.check(rbtn_man.getId());
                    txt_data.setText(getCurrentDate());
                    txt_age.setText("0");
                    txt_pwd.setText("");
                }
                else{
                    txt_id.setText(data.getStu_id());
                    txt_name.setText(data.getName());
                    txt_phone.setText(data.getPhoneNumber());
                    txt_major.setText(data.getMajor());
                    txt_class.setText(data.getClass_num());
//                    rg_sex.check(rbtn_man.getId());
                    txt_data.setText(data.getTrainDate());
                    txt_age.setText(String.valueOf(data.getAge()));
                    txt_pwd.setText(data.getPwd());
                    if(data.getSex().equals("男")){
                        rg_sex.check(rbtn_man.getId());
                    }
                    else{
                        rg_sex.check(rbtn_woman.getId());
                    }
                }
            }
        });
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateFormat filedate = new java.text.SimpleDateFormat("yy-MM-dd");
                String modifydate = filedate.format(new Date());
                findById(txt_id.getText().toString());

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

    public void addstu(){
        if(stu_find==null||stu_find.size()==0||num!=-1) {
            if (rbtn_man.isChecked()) {
                sex = "男";
            } else if (rbtn_woman.isChecked()) {
                sex = "女";
            }
            Student data = new Student(txt_id.getText().toString(), txt_name.getText().toString(),
                    Integer.parseInt(txt_age.getText().toString()), sex, txt_phone.getText().toString(),
                    txt_data.getText().toString(), modifydate, txt_major.getText().toString(), txt_class.getText().toString(), txt_pwd.getText().toString(),0);
            String stuid = txt_id.getText().toString();
            String stupwd = txt_pwd.getText().toString();
            String stuname = txt_name.getText().toString();
            if(!stuid.equals("")&&!stupwd.equals("")&&!stuname.equals("")) {
                if(stuid.length()<8){
                    showToast("学号最少8位");
                }
                else if(stupwd.length()<8){
                    showToast("密码最少8位");
                }
                else if(stuname.length()<2){
                    showToast("姓名格式错误");
                }
                else {
                    showToast("添加成功，需要后续补全信息");
                    insertInto(data);
                    getstus();
                    finish();
                }
            }
            else{
                showToast("至少要填写学号、密码和姓名");
            }
        }
        else{
            showToast("学号已存在，请重新输入");
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

    /**
     * 插入(更新)单个student到mysql
     * **/
    private void insertInto(Student stu) {
        String stuStr = JSON.toJSONString(stu);
        OkHttpClient mOkHttpClient = new OkHttpClient();
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("stu", stuStr);
        final Request request = new Request.Builder()
                .url(insert_url)
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
                if(backnum == -1) {
                    Intent intent = new Intent();
                    intent.setClass(AddstudentActivity.this, AdminActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
    private void findById(String Id) {
        final boolean judge = false;
        String idStr = JSON.toJSONString(Id);
        OkHttpClient mOkHttpClient = new OkHttpClient();
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("id", idStr);
        final Request request = new Request.Builder()
                .url(find_url)
                .post(builder.build())
                .build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.v("Fail", e.getMessage());
                stu_find.clear();
                Message msg= myhandler.obtainMessage();
                msg.what = 2;
                myhandler.sendMessage(msg);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String responseStr = response.body().string();
//                stu_find.clear();
                stu_find = com.alibaba.fastjson.JSONArray.parseArray(responseStr, Student.class);
                Message msg= myhandler.obtainMessage();
                msg.what = 2;
                myhandler.sendMessage(msg);
            }
        });
    }


    private void showDatePickDlg() {
        Calendar calendar = Calendar.getInstance();
//        获取当前日期
        final int mYear = calendar.get(Calendar.YEAR);
        final int mMonth = calendar.get(Calendar.MONTH);
        final int mDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(AddstudentActivity.this,
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

                        AddstudentActivity.this.txt_data.setText(year+"-"+(month+1)+"-"+dayOfMonth);
                    }
                },
                mYear,
                mMonth,
                mDayOfMonth);
        datePickerDialog.show();
    }

    private String getCurrentDate() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(new Date());
    }

    //时间的监听与事件
    private DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener()
    {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            txt_data.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
        }
    };

    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_PICKER_ID:
                return new DatePickerDialog(this, onDateSetListener, 2011, 8, 14);
        }
        return null;
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
                    showToast("学生信息添加成功");
                    break;
                }
                case 2:{
                    addstu();
                    break;
                }
                default:{
                    break;
                }
            }
        }
    };

}
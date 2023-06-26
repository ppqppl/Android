package com.example.ppqppl1;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.collection.ArraySet;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.ppqppl1.databinding.ActivityShowgradelistBinding;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import bean.Course;
import bean.CourseChoose;
import bean.Student;

public class ShowgradelistActivity extends AppCompatActivity {

    private ActivityShowgradelistBinding binding;

    private ListView list_grade;

    private String getcourseschosebytea_url = AdminActivity.url_sercver+"/find_CourseChoose_ByCourse_id";
    private String getStuByid_url = AdminActivity.url_sercver+"/findbyid";
    private String insert_url = AdminActivity.url_sercver+"/addCoursechose";

    private String mycid,myname;

    private int posi;

    public static List<CourseChoose> mycoursechoses = new ArrayList<>();
    public static  List<String> mynames = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityShowgradelistBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        list_grade = findViewById(R.id.gradelist);


        Intent getnum = this.getIntent();
        mycid = getnum.getStringExtra("courseid");

        showgradelist();
//        findcoursechoselist(mycid);
        list_grade.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CourseChoose cc = mycoursechoses.get(position);
                posi = position;
                findstubyid(cc.getStuid());
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

    public void showgradelist(){
        list_grade.setAdapter(new MygradeAdapter(ShowgradelistActivity.this,R.layout.grade_list_layout,mycoursechoses));
//        Log.e("mynamesssssssssize", mynames.get(0).toString());
    }

    public void findnnames(){
        for(int i=0;i<mycoursechoses.size();i++){
            String mid = mycoursechoses.get(i).getStuid();
            findstubyid(mid);
        }
        showgradelist();
    }

    public void findcoursechoselist(String myid){
        String stuStr = JSON.toJSONString(myid);
        OkHttpClient mOkHttpClient = new OkHttpClient();
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("courseid", stuStr);
        final Request request = new Request.Builder()
                .url(getcourseschosebytea_url)
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
                String responseStr = response.body().string();
                Log.e("response",responseStr);
                mycoursechoses = com.alibaba.fastjson.JSONArray.parseArray(responseStr, CourseChoose.class);
                Message msg= myhandler.obtainMessage();
                msg.what = 1;
                myhandler.sendMessage(msg);
            }
        });
    }

    private void insertgrade(CourseChoose cc) {
        String stuStr = JSON.toJSONString(cc);
        OkHttpClient mOkHttpClient = new OkHttpClient();
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("courseChose", stuStr);
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
                Message msg= myhandler.obtainMessage();
                msg.what = 3;
                myhandler.sendMessage(msg);
            }
        });
    }

    public void findstubyid(String stuid){
        String stuStr = JSON.toJSONString(stuid);
        OkHttpClient mOkHttpClient = new OkHttpClient();
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("id", stuStr);
        final Request request = new Request.Builder()
                .url(getStuByid_url)
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
                String responseStr = response.body().string();
                Log.e("response",responseStr);
                List<Student> ss = com.alibaba.fastjson.JSONArray.parseArray(responseStr, Student.class);
                Message msg= myhandler.obtainMessage();
                msg.what = 2;
                msg.obj = ss;
                myhandler.sendMessage(msg);
            }
        });
    }

    private class MygradeAdapter extends ArrayAdapter<CourseChoose> {

        public MygradeAdapter(@NonNull Context context, int resource, @NonNull List<CourseChoose> objects) {
            super(context, resource, objects);
        }

        @Override
        public CourseChoose getItem(int position) {
            return mycoursechoses.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View itemView;
            ViewHolder holder = null;
            if (convertView == null) {
                itemView = LayoutInflater.from(ShowgradelistActivity.this).inflate(R.layout.grade_list_layout, parent, false);
                holder = new ViewHolder(itemView);
                itemView.setTag(holder);
            } else {
                itemView = convertView;
                holder = (ViewHolder) itemView.getTag();
            }

            CourseChoose data = getItem(position);
            holder.txt_id.setText(data.getStuid());
            String name = mynames.get(position);
            holder.txt_name.setText(name);
            double g = data.getGrade();
            if(g == -1) {
                holder.txt_grade.setText("未评分");
            }
            else {
                holder.txt_grade.setText(String.valueOf(g));
            }
            return itemView;
        }

        public class ViewHolder{
            TextView txt_id;
            TextView txt_name;
            TextView txt_grade;

            public ViewHolder(View itemView) {
                txt_id = itemView.findViewById(R.id.coursenametxt);
                txt_name = itemView.findViewById(R.id.nametxt);
                txt_grade = itemView.findViewById(R.id.gradetxt);
                Resources resources = ShowgradelistActivity.this.getResources();
                DisplayMetrics dm = resources.getDisplayMetrics();
                float density = dm.density;
                int width = dm.widthPixels;
                int height = dm.heightPixels;
                txt_id.setWidth(width);
                txt_name.setWidth(width);
                txt_grade.setWidth(width);
            }
        }
    }

    private Handler myhandler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            switch (msg.what){
                case 1:{
                    findnnames();
                    break;
                }
                case 2:{
                    List<Student> myss = (List<Student>) msg.obj;
                    if(myss.get(0).getIfchosecourse() == 0){
                        showToast("学生尚未确定选课信息");
                    }
                    else{
                        Log.e("开始编辑成绩信息","开始编辑成绩信息");
                        CourseChoose cc = mycoursechoses.get(posi);
                        View dialogView = LayoutInflater.from(ShowgradelistActivity.this).inflate(R.layout.add_grade_list_layout, null);
                        AlertDialog alertDialog = new AlertDialog.Builder(ShowgradelistActivity.this).create();
                        alertDialog.setView(dialogView, 0, 0, 0, 0);
                        alertDialog.show();
                        EditText txt_grade = dialogView.findViewById(R.id.gradetxt);
                        Button btn_ok = dialogView.findViewById(R.id.savebtn);
                        Button btn_cancel = dialogView.findViewById(R.id.cancelbtn);
                        btn_ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String gstr = String.valueOf(txt_grade.getText());
                                if(gstr == null||gstr.equals("")) {
                                    showToast("请输入成绩信息");
                                }
                                else {
                                    double grade = Double.parseDouble(gstr);
                                    cc.setGrade(grade);
                                    insertgrade(cc);
                                    alertDialog.dismiss();
                                }
                            }
                        });
                        btn_cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                alertDialog.dismiss();
                            }
                        });

                    }
                    break;
                }
                case 3:{
                    showgradelist();
                    break;
                }
                default:{
                    break;
                }
            }
        }
    };


    public void showToast(String str){
        Toast toast=Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM, 0, 100);
        toast.show();
    }

}
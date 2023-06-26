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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.ppqppl1.databinding.ActivityShowcourselistBinding;
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
import bean.Teacher;

public class ShowcourselistActivity extends AppCompatActivity {

    private ActivityShowcourselistBinding binding;

    private ImageButton btn_addcourse;
    private ListView list_course;

    public static List<Course> mycourses = new ArrayList<>();

    private String getcoursesbytea_url = AdminActivity.url_sercver+"/find_course_by_teaid";
    private String getcourseschosebytea_url = AdminActivity.url_sercver+"/find_CourseChoose_ByCourse_id";
    private String delcourse_url = AdminActivity.url_sercver+"/delcourse";
    private String myteaid = MainActivity.tea_login.getId();
    private Course mycc = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityShowcourselistBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        btn_addcourse = findViewById(R.id.addcoursebtn);
        list_course = findViewById(R.id.courselist);

        getcourselist(myteaid);


        btn_addcourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(ShowcourselistActivity.this,ShowclassmsgActivity.class);
                startActivity(intent);
                finish();
            }
        });

        list_course.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mycc = mycourses.get(position);
                ShowgradelistActivity.mynames.clear();
                findcoursechoselist(mycc.getId());
//                Intent intent = new Intent();
//                intent.setClass(ShowcourselistActivity.this,ShowgradelistActivity.class);
//                intent.putExtra("courseid",cc.getId());
//                startActivity(intent);
            }
        });

        list_course.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                View dialogView = LayoutInflater.from(ShowcourselistActivity.this).inflate(R.layout.course_del_layout, null);
                AlertDialog alertDialog = new AlertDialog.Builder(ShowcourselistActivity.this).create();
                alertDialog.setView(dialogView, 0, 0, 0, 0);
                alertDialog.show();
                Button btn_ok = dialogView.findViewById(R.id.savebtn);
                Button btn_cancel = dialogView.findViewById(R.id.cancelbtn);
                btn_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Course cccc = mycourses.get(position);
                        delcourse(cccc);
                        delcoursechose(cccc);
                        alertDialog.dismiss();
                    }
                });
                btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                return true;
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

    private void delcourse(Course course) {
        String idStr = JSON.toJSONString(course);
        OkHttpClient mOkHttpClient = new OkHttpClient();
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("course", idStr);
        final Request request = new Request.Builder()
                .url(delcourse_url)
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
                getcourselist(MainActivity.tea_login.getId());
            }
        });
    }


    private String delcoursechose_bycid_url = AdminActivity.url_sercver+"/deletecc_by_cid";
    private void delcoursechose(Course course) {
        String idStr = JSON.toJSONString(course.getId());
        OkHttpClient mOkHttpClient = new OkHttpClient();
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("courseid", idStr);
        final Request request = new Request.Builder()
                .url(delcoursechose_bycid_url)
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
            }
        });
    }

    public void getcourselist(String myid){
        String stuStr = JSON.toJSONString(myid);
        OkHttpClient mOkHttpClient = new OkHttpClient();
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("courseid", stuStr);
        final Request request = new Request.Builder()
                .url(getcoursesbytea_url)
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
                mycourses = com.alibaba.fastjson.JSONArray.parseArray(responseStr, Course.class);
                Message msg= myhandler.obtainMessage();
                msg.what = 1;
                myhandler.sendMessage(msg);
            }
        });
    }

    public void showcourselist(){
        list_course.setAdapter(new MycourseAdapter(ShowcourselistActivity.this,R.layout.course_list_layout,mycourses));
    }

    private class MycourseAdapter extends ArrayAdapter<Course> {

        public MycourseAdapter(@NonNull Context context, int resource, @NonNull List<Course> objects) {
            super(context, resource, objects);
        }

        @Override
        public Course getItem(int position) {
            return mycourses.get(position);
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
                itemView = LayoutInflater.from(ShowcourselistActivity.this).inflate(R.layout.course_list_layout, parent, false);
                holder = new ViewHolder(itemView);
                itemView.setTag(holder);
            } else {
                itemView = convertView;
                holder = (ViewHolder) itemView.getTag();
            }

            Course data = getItem(position);
            holder.txt_id.setText(data.getId());
            holder.txt_name.setText(data.getName());
            holder.txt_tea.setText(MainActivity.tea_login.getName());
            return itemView;
        }

        public class ViewHolder{
            TextView txt_id;
            TextView txt_name;
            TextView txt_tea;

            public ViewHolder(View itemView) {
                txt_id = itemView.findViewById(R.id.courseidtxt);
                txt_tea = itemView.findViewById(R.id.teanametxt);
                txt_name = itemView.findViewById(R.id.coursenametxt);
                Resources resources = ShowcourselistActivity.this.getResources();
                DisplayMetrics dm = resources.getDisplayMetrics();
                float density = dm.density;
                int width = dm.widthPixels;
                int height = dm.heightPixels;
                txt_id.setWidth(width);
                txt_name.setWidth(width);
                txt_tea.setWidth(width);
            }
        }
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
                ShowgradelistActivity.mycoursechoses = com.alibaba.fastjson.JSONArray.parseArray(responseStr, CourseChoose.class);
                findnnames();
            }
        });
    }

    public void findnnames(){
        for(int i=0;i<ShowgradelistActivity.mycoursechoses.size();i++){
            String mid = ShowgradelistActivity.mycoursechoses.get(i).getStuid();
            findstubyid(mid);
        }
    }

    private String getStuByid_url = AdminActivity.url_sercver+"/findbyid";
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
                if(ss!=null) {
                    ShowgradelistActivity.mynames.add(ss.get(0).getName());
                }
                int num = ShowgradelistActivity.mynames.size();
                Message msg= myhandler.obtainMessage();
                msg.what = 2;
                msg.obj = num;
                myhandler.sendMessage(msg);
            }
        });
    }

    private Handler myhandler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            switch (msg.what){
                case 1:{
                    showcourselist();
                    Log.e("cacccccccccccccccccc", String.valueOf(mycourses.size()));
                    break;
                }
                case 2:{
                    int num = (int) msg.obj;
                    Log.e("aaaaaaaaaaaaaaaaaaaaaaafdfffffffffffffffffffff", String.valueOf(ShowgradelistActivity.mynames.size()));
                    if(num == ShowgradelistActivity.mycoursechoses.size()){
                        Log.e("跳转页面次数","?1111111111");
                        Intent intent = new Intent();
                        intent.setClass(ShowcourselistActivity.this,ShowgradelistActivity.class);
                        intent.putExtra("courseid",mycc.getId());
                        startActivity(intent);
                    }
                    break;
                }
                default:{
                    break;
                }
            }
        }
    };

}
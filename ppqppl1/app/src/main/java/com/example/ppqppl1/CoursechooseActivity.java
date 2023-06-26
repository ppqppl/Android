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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.ppqppl1.databinding.ActivityCoursechooseBinding;
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

public class CoursechooseActivity extends AppCompatActivity {

    private ActivityCoursechooseBinding binding;
    private Student data = MainActivity.stu_login;
    private List<Teacher> teas = MainActivity.teas;

    private ListView list_ischose,list_notchose;
    private Button btn_save,btn_cancel;
    private TextView txt_prompt,txt_coursechose;
    private LinearLayout layout_btn,layout_notchose;

    private String findallcourse_url = AdminActivity.url_sercver + "/subject";
    private String findallmycoursechose_url = AdminActivity.url_sercver + "/find_CourseChoose_ByStu_id";
    private String findallcoursechose_url = AdminActivity.url_sercver + "/courseChoose";
    private String addcoursechoose_url = AdminActivity.url_sercver + "/addCoursechose";
    private String delbysidandcid_url = AdminActivity.url_sercver + "/delete_by_Stuid_and_Courseid";
    private String insertStu_url = AdminActivity.url_sercver+"/addstu";

    public static List<Course> courses_notchose = new ArrayList<>();
    public static List<Course> courses_chose = new ArrayList<>();
    public static List<CourseChoose> mycourseChooses = new ArrayList<>();
    public static List<CourseChoose> courseChooses = new ArrayList<>();
    public static List<Course> courses = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCoursechooseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        list_ischose = findViewById(R.id.choselist);
        list_notchose = findViewById(R.id.notchoselist);
        btn_save = findViewById(R.id.savebtn);
        btn_cancel = findViewById(R.id.cancelbtn);
        layout_btn = findViewById(R.id.btnlayout);
        txt_prompt = findViewById(R.id.prompttext);
        layout_notchose = findViewById(R.id.notchoselayout);
        txt_coursechose = findViewById(R.id.coursechosetxt);

//        findallmycoursechose(data.getStu_id());
//        findallcoursenorchose();
//        findallcourseChose();
//
//        showchoselist();
//        shownotchoselist();

        init();

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mycourseChooses.size() == 0||mycourseChooses == null){
                    showToast("还未进行选课！");
                }
                else{
                    layout_btn.setVisibility(View.GONE);
                    MainActivity.stu_login.setIfchosecourse(1);
                    insertstu(data);
                    list_notchose.setFocusable(false);
                    list_notchose.setFocusableInTouchMode(false);

                }
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        list_notchose.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int id_cc1 = 1;
                int ju = data.getIfchosecourse();
                if (ju != 1) {
                    if (courseChooses.size() == 0 || courseChooses == null) {
                        id_cc1 = 1;
                    } else {
                        id_cc1 = courseChooses.get(courseChooses.size() - 1).getId() + 1;
                    }
                    String stu_id = data.getStu_id();
                    Course c1 = courses_notchose.get(position);
                    String course_id = c1.getId();
                    CourseChoose cc1 = new CourseChoose(id_cc1, stu_id, course_id, -1);
                    addcoursechoose(cc1);
                }
            }
        });

        list_ischose.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int ju = data.getIfchosecourse();
                String stu_id = data.getStu_id();
                Course c1 = courses_chose.get(position);
                String course_id = c1.getId();
                CourseChoose cc1 = new CourseChoose(0, stu_id, course_id, -1);
                int cc1id = getcoursechoseid(cc1);
                if (ju != 1) {
                    cc1.setId(cc1id);
                    delcoursechose(cc1);
                }
                else{
                    Intent intent = new Intent();
                    intent.setClass(CoursechooseActivity.this,ShowclassmsgActivity.class);
                    intent.putExtra("coursechoseid",cc1id);
                    startActivity(intent);

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

    public void init(){
        findallcourse();
        if(data.getIfchosecourse() == 1){
            layout_btn.setVisibility(View.GONE);
            layout_notchose.setVisibility(View.GONE);
            txt_prompt.setText("已经完成选课");
            txt_coursechose.setText("已选课程(点击查看详细信息)");
        }
        else{
            txt_prompt.setVisibility(View.GONE);
        }
    }

    public void showToast(String str){
        Toast toast=Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM, 0, 100);
        toast.show();
    }

    private void insertstu(Student stu) {
        String stuStr = JSON.toJSONString(stu);
        OkHttpClient mOkHttpClient = new OkHttpClient();
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("stu", stuStr);
        final Request request = new Request.Builder()
                .url(insertStu_url)
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

    public int getcoursechoseid(CourseChoose data){
        String d_stuid = data.getStuid();
        String d_courseid = data.getCourseid();
        for(int i=0;i<courseChooses.size();i++){
            String c_stuid = courseChooses.get(i).getStuid();
            String c_courseid = courseChooses.get(i).getCourseid();
            if(d_stuid.equals(c_stuid)&&d_courseid.equals(c_courseid)){
                return courseChooses.get(i).getId();
            }
        }
        return -1;
    }

    public void findallcourse(){
        OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder().url(findallcourse_url).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.v("Fail", e.getMessage());
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String responseStr = response.body().string();
                courses = com.alibaba.fastjson.JSONArray.parseArray(responseStr,Course.class);
                findallcourseChose();
//                List<Course> cc = com.alibaba.fastjson.JSONArray.parseArray(responseStr,Course.class);
//                Message msg= myhandler.obtainMessage();
//                msg.obj = cc;
//                msg.what = 1;
//                myhandler.sendMessage(msg);
            }
        });
    }

    public void findallcourseChose(){
        OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder().url(findallcoursechose_url).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.v("Fail", e.getMessage());
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String responseStr = response.body().string();
                courseChooses = com.alibaba.fastjson.JSONArray.parseArray(responseStr,CourseChoose.class);
                findallmycoursechose(data.getStu_id());
            }
        });
    }

    private void delcoursechose(CourseChoose data) {
        String ccStr = JSON.toJSONString(data);
        OkHttpClient mOkHttpClient = new OkHttpClient();
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("courseChose", ccStr);
        final Request request = new Request.Builder()
                .url(delbysidandcid_url)
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
                Log.e("删除选课信息:  ",responseStr);
                findallcourse();
            }
        });
    }

    private void addcoursechoose(CourseChoose data) {
        String ccStr = JSON.toJSONString(data);
        OkHttpClient mOkHttpClient = new OkHttpClient();
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("courseChose", ccStr);
        final Request request = new Request.Builder()
                .url(addcoursechoose_url)
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
                Log.e("删除课程信息:  ",responseStr);
                findallcourse();
            }
        });
    }

    private void findallmycoursechose(String str) {
        String stuStr = JSON.toJSONString(str);
        OkHttpClient mOkHttpClient = new OkHttpClient();
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("str", stuStr);
        final Request request = new Request.Builder()
                .url(findallmycoursechose_url)
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
                mycourseChooses = com.alibaba.fastjson.JSONArray.parseArray(responseStr,CourseChoose.class);
                findallcoursenorchose();
            }
        });
    }

    private void findallcoursenorchose(){
        Log.e("开始开始","刷新选课课表"+courses.size()+"    "+mycourseChooses.size());
//        courses_chose = new ArrayList<>();
//        courses_notchose = new ArrayList<>();
        courses_chose.clear();
        courses_notchose.clear();
        boolean if_chose = false;
        for(int i=0;i<courses.size();i++){
            if_chose = false;
            for(int j=0;j<mycourseChooses.size();j++){
                Log.e("课程id",courses.get(i).getId()+"   "+mycourseChooses.get(j).getCourseid());
                String cid = courses.get(i).getId();
                String ccid = mycourseChooses.get(j).getCourseid();
                if(cid.equals(ccid)){
                    if_chose = true;
                    break;
                }
            }
            if(if_chose){
                Log.e("aaaaaaaaaaaaaaaaaaaaa","asd"+"       "+i);
                courses_chose.add(courses.get(i));
            }
            else {
                Log.e("asd","aaaaaaaaaaaaaaaaaaaaa"+"       "+i);
                courses_notchose.add(courses.get(i));
            }
        }
        Log.e("全部课程", String.valueOf(courses.size()));
        Log.e("未选课程", String.valueOf(courses_notchose.size()));
        Log.e("已选课程", String.valueOf(courses_chose.size()));
        Message msg= myhandler.obtainMessage();
        msg.what = 1;
        myhandler.sendMessage(msg);
    }

    public String getteaname(String tea_id){
        for(int i=0;i<teas.size();i++){
            if(teas.get(i).getId().equals(tea_id)){
                return teas.get(i).getName();
            }
        }
        return null;
    }

    private void deletestu(Student stu) {
        String stuStr = JSON.toJSONString(stu);
        OkHttpClient mOkHttpClient = new OkHttpClient();
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("stu", stuStr);
        final Request request = new Request.Builder()
                .url(findallmycoursechose_url)
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
                Log.e("delResponse",str);
            }
        });
    }

    public void showchoselist(){
        list_ischose.setAdapter(new CoursechoseAdapter(CoursechooseActivity.this,R.layout.course_list_layout,courses_chose));
    }

    public void shownotchoselist(){
        list_notchose.setAdapter(new CoursenotchoseAdapter(CoursechooseActivity.this,R.layout.course_list_layout,courses_notchose));
    }

    private class CoursechoseAdapter extends ArrayAdapter<Course> {

        public CoursechoseAdapter(@NonNull Context context, int resource, @NonNull List<Course> objects) {
            super(context, resource, objects);
        }

        @Override
        public Course getItem(int position) {
            return courses_chose.get(position);
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
                itemView = LayoutInflater.from(CoursechooseActivity.this).inflate(R.layout.course_list_layout, parent, false);
                holder = new ViewHolder(itemView);
                itemView.setTag(holder);
            } else {
                itemView = convertView;
                holder = (ViewHolder) itemView.getTag();
            }

            Course data = getItem(position);
            holder.txt_name.setText(data.getName());
            holder.txt_tea.setText(getteaname(data.getTeaid()));
            holder.txt_id.setText(data.getId());
            return itemView;
        }

        public class ViewHolder{
            TextView txt_name;
            TextView txt_tea;
            TextView txt_id;

            public ViewHolder(View itemView) {
                txt_id = itemView.findViewById(R.id.courseidtxt);
                txt_tea = itemView.findViewById(R.id.teanametxt);
                txt_name = itemView.findViewById(R.id.coursenametxt);
                Resources resources = CoursechooseActivity.this.getResources();
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

    private class CoursenotchoseAdapter extends ArrayAdapter<Course> {

        public CoursenotchoseAdapter(@NonNull Context context, int resource, @NonNull List<Course> objects) {
            super(context, resource, objects);
        }

        @Override
        public Course getItem(int position) {
            return courses_notchose.get(position);
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
                itemView = LayoutInflater.from(CoursechooseActivity.this).inflate(R.layout.course_list_layout, parent, false);
                holder = new ViewHolder(itemView);
                itemView.setTag(holder);
            } else {
                itemView = convertView;
                holder = (ViewHolder) itemView.getTag();
            }

            Course data = getItem(position);
            holder.txt_name.setText(data.getName());
            holder.txt_tea.setText(getteaname(data.getTeaid()));
            holder.txt_id.setText(data.getId());
            return itemView;
        }

        public class ViewHolder{
            TextView txt_name;
            TextView txt_tea;
            TextView txt_id;

            public ViewHolder(View itemView) {
                txt_id = itemView.findViewById(R.id.courseidtxt);
                txt_tea = itemView.findViewById(R.id.teanametxt);
                txt_name = itemView.findViewById(R.id.coursenametxt);
                Resources resources = CoursechooseActivity.this.getResources();
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

    private Handler myhandler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            switch (msg.what){
                case 1:{
                    showchoselist();
                    shownotchoselist();
                    Log.e("cacccccccccccccccccc", String.valueOf(courses.size()));
                    break;
                }
                case 2:{

                    break;
                }
                default:{
                    break;
                }
            }
        }
    };

}
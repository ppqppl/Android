package com.example.ppqppl1;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
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

import com.example.ppqppl1.databinding.ActivityShowclassmsgBinding;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.List;

import bean.Course;
import bean.CourseChoose;
import bean.Student;
import bean.Teacher;

public class ShowclassmsgActivity extends AppCompatActivity {

    private ActivityShowclassmsgBinding binding;

    private TextView txt_name,txt_tea,txt_grade,txt_id;
    private Button btn_back,btn_save,btn_cancel;
    private LinearLayout layout_id,layout_grade,layout_btn,layout_tea;

    private int coursechoseid;
    private boolean if_modify = false;

    private List<CourseChoose> mycourseChooses = CoursechooseActivity.mycourseChooses;
    private List<Course> courses = CoursechooseActivity.courses;
    private List<Teacher> teas = MainActivity.teas;
    private Course course = null;

    private String insertcourse_url = AdminActivity.url_sercver+"/addcourse";
    private String getcoursesbytea_url = AdminActivity.url_sercver+"/find_course_by_teaid";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityShowclassmsgBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        txt_name = findViewById(R.id.nametxt);
        txt_grade = findViewById(R.id.gradetxt);
        txt_tea = findViewById(R.id.teatxt);
        txt_id = findViewById(R.id.courseidtxt);
        btn_back = findViewById(R.id.backbtn);
        btn_save = findViewById(R.id.savebtn);
        btn_cancel = findViewById(R.id.cancelbtn);
        layout_id = findViewById(R.id.idlayout);
        layout_grade = findViewById(R.id.gradelayout);
        layout_btn = findViewById(R.id.btnlayout);
        layout_tea = findViewById(R.id.tealayout);

        Intent getnum = this.getIntent();
        coursechoseid = getnum.getIntExtra("coursechoseid",-1);

        if(coursechoseid == -1){
            btn_back.setVisibility(View.GONE);
            layout_grade.setVisibility(View.GONE);
            txt_tea.setText(MainActivity.tea_login.getName());
            if_modify = true;
            init_textview();
        }
        else{
            layout_btn.setVisibility(View.GONE);
            if_modify = false;
            init_textview();
            CourseChoose courseChoose = findbyid(coursechoseid);
            String cid = courseChoose.getCourseid();
            Course course = findcourse(cid);
            String tid = course.getTeaid();
            Teacher tea = findtea(tid);
            double grade = courseChoose.getGrade();
            txt_id.setText(cid);
            txt_name.setText(course.getName());
            txt_tea.setText(tea.getName());
            if(grade<0){
                txt_grade.setText("尚未结课，成绩未公布");
            }
            else{
                txt_grade.setText(String.valueOf(grade));
            }
        }

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cid = String.valueOf(txt_id.getText());
                String cname = String.valueOf(txt_name.getText());
                String cteaid = MainActivity.tea_login.getId();
                Course course = new Course(cid,cname,cteaid);
                insertInto(course);
                txt_id.setText("");
                txt_name.setText("");
                txt_tea.setText("");
                showToast("添加成功");
                getcourselist(MainActivity.tea_login.getId());
                Intent intent = new Intent();
                intent.setClass(ShowclassmsgActivity.this,ShowcourselistActivity.class);
                startActivity(intent);
                finish();
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

    public void init_textview(){
        txt_id.setEnabled(if_modify);
        txt_name.setEnabled(if_modify);
        txt_tea.setEnabled(false);
        txt_grade.setEnabled(false);
    }

    private void insertInto(Course course) {
        String stuStr = JSON.toJSONString(course);
        OkHttpClient mOkHttpClient = new OkHttpClient();
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("course", stuStr);
        final Request request = new Request.Builder()
                .url(insertcourse_url)
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
                ShowcourselistActivity.mycourses = com.alibaba.fastjson.JSONArray.parseArray(responseStr, Course.class);
            }
        });
    }

    public CourseChoose findbyid(int id){
        for(int i=0;i<mycourseChooses.size();i++){
            int myid = mycourseChooses.get(i).getId();
            if(myid == id){
                return mycourseChooses.get(i);
            }
        }
        return null;
    }

    public Course findcourse(String id){
        for(int i=0;i<courses.size();i++){
            String myid = courses.get(i).getId();
            if(id.equals(myid)){
                return courses.get(i);
            }
        }
        return null;
    }

    public Teacher findtea(String id){
        for(int i=0;i<teas.size();i++){
            String myid = teas.get(i).getId();
            if(myid.equals(id)){
                return teas.get(i);
            }
        }
        return null;
    }

    public void showToast(String str){
        Toast toast=Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM, 0, 100);
        toast.show();
    }
}
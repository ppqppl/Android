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
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSON;
import com.example.ppqppl1.databinding.ActivityAdminBinding;
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
import bean.Photo;
import bean.Student;

public class AdminActivity extends AppCompatActivity {

    private ActivityAdminBinding binding;

    private ListView list_stu;
    private Context context;
    private Button btn_add,btn_search,btn_back,btn_searchstu;
    private LinearLayout layout_search,layout_back;
    private RadioGroup rg_search;
    private RadioButton r_byid,r_byname;
    private EditText txt_search;


    public static String url_sercver = "http://192.168.3.7";
    private String find_url = AdminActivity.url_sercver+"/findbyid";
    private String find_by_name_url = AdminActivity.url_sercver+"/find_stu_by_name_like";
    private String url = url_sercver+"/student";
    private String delstu_url = url_sercver+"/delstu";
    public static List<Student> students = new ArrayList<>();

    private boolean if_select_all = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        list_stu = findViewById(R.id.stulist);
        btn_add = findViewById(R.id.addbtn);
        btn_search = findViewById(R.id.searchbtn);
        btn_back = findViewById(R.id.backbtn);
        btn_searchstu = findViewById(R.id.searchstubtn);
        layout_search = findViewById(R.id.searchlayout);
        layout_back = findViewById(R.id.backbtnlayout);
        rg_search = findViewById(R.id.radiogroup);
        txt_search = findViewById(R.id.searchtxt);
        r_byid = findViewById(R.id.byidradio);
        r_byname = findViewById(R.id.bynameradio);

        context = this;

        getJsonData();

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(AdminActivity.this,AddstudentActivity.class);
                intent.putExtra("stu_num",-1);
                startActivity(intent);
                finish();
            }
        });

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                students = new ArrayList<>();
                showlist();
                layout_search.setVisibility(View.VISIBLE);
                layout_back.setVisibility(View.VISIBLE);
                rg_search.setVisibility(View.VISIBLE);
                rg_search.check(r_byid.getId());
            }
        });

        btn_searchstu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = String.valueOf(txt_search.getText());
                if(r_byname.isChecked()){
                    findById(str);
                }
                else{
                    findByName(str);
                }

            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getJsonData();
                layout_search.setVisibility(View.GONE);
                layout_back.setVisibility(View.GONE);
                rg_search.setVisibility(View.GONE);
                getJsonData();
            }
        });


        list_stu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Photo data = photos.get(position);
                Student data = students.get(position);
                Intent intent = new Intent();
                intent.setClass(AdminActivity.this,ShowStudentMsgActivity.class);
                intent.putExtra("stu_num",position);
                startActivity(intent);
            }
        });

        list_stu.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                View dialogView = LayoutInflater.from(AdminActivity.this).inflate(R.layout.student_edit_layout, null);
                AlertDialog alertDialog = new AlertDialog.Builder(AdminActivity.this).create();
                alertDialog.setView(dialogView, 0, 0, 0, 0);
                alertDialog.show();
                Button btn_modify = dialogView.findViewById(R.id.modifybtn);
                Button btn_del = dialogView.findViewById(R.id.deletebtn);
                btn_del.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Student data = students.get(position);
                        students.remove(position);
                        deletestu(data);
                        delcoursechose(data);
                        showlist();
                        alertDialog.dismiss();
                    }
                });
                btn_modify.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Student data = students.get(position);
                        Intent intent = new Intent();
                        intent.setClass(AdminActivity.this,AddstudentActivity.class);
                        intent.putExtra("stu_num",position);
                        startActivity(intent);
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

    private void findByName(String name) {
        String idStr = JSON.toJSONString(name);
        OkHttpClient mOkHttpClient = new OkHttpClient();
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("name", idStr);
        final Request request = new Request.Builder()
                .url(find_by_name_url)
                .post(builder.build())
                .build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.v("Fail", e.getMessage());
                students = new ArrayList<>();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String responseStr = response.body().string();
//                stu_find.clear();
                students = com.alibaba.fastjson.JSONArray.parseArray(responseStr, Student.class);
                Message msg = myhandler.obtainMessage();
                msg.what = 3;
                myhandler.sendMessage(msg);
            }
        });
    }

    private void findById(String Id) {
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
                students = new ArrayList<>();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String responseStr = response.body().string();
//                stu_find.clear();
                students = com.alibaba.fastjson.JSONArray.parseArray(responseStr, Student.class);
                Message msg = myhandler.obtainMessage();
                msg.what = 2;
                myhandler.sendMessage(msg);
            }
        });
    }

    public void getJsonData(){
        OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder().url(url).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.v("Fail", e.getMessage());
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String responseStr = response.body().string();
                List<Student> studentEntities = null;
                Log.e("response",responseStr);
                studentEntities = com.alibaba.fastjson.JSONArray.parseArray(responseStr,Student.class);
                Message msg = myhandler.obtainMessage();
                msg.obj = studentEntities;
                msg.what = 1;
                myhandler.sendMessage(msg);
            }
        });
    }
    /**
     * 插入(更新)单个student到mysql
     * **/
    private void deletestu(Student stu) {
        String stuStr = JSON.toJSONString(stu);
        OkHttpClient mOkHttpClient = new OkHttpClient();
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("stu", stuStr);
        final Request request = new Request.Builder()
                .url(delstu_url)
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

    private String delcoursechose_bysid_url = AdminActivity.url_sercver+"/deletecc_by_Stuid";
    private void delcoursechose(Student stu) {
        String idStr = JSON.toJSONString(stu.getStu_id());
        OkHttpClient mOkHttpClient = new OkHttpClient();
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("stuid", idStr);
        final Request request = new Request.Builder()
                .url(delcoursechose_bysid_url)
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
                Log.e("responsedelllllllllllllllllllllllllllll",responseStr);
            }
        });
    }

//    private Handler myhandler = new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//            students = (List<Student>)msg.obj;
//            if(null != students){
//                showlist();
//            }
//        }
//    };

    private Handler myhandler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            switch (msg.what){
                case 1:{
                    students = (List<Student>)msg.obj;
                    if(null != students){
                        showlist();
                    }
                    break;
                }
                case 2:{
                    if(students == null){
                        students = new ArrayList<>();
                        showToast("未找到相关同学信息");
                    }
                    showlist();
                    break;
                }
                case 3:{
                    if(students == null){
                        students = new ArrayList<>();
                        showToast("未找到相关同学信息");
                    }
                    showlist();
                }
                default:{
                    break;
                }
            }
        }
    };

    public void showlist(){
        list_stu.setVisibility(View.VISIBLE);
        list_stu.setAdapter(new StuAdapter(AdminActivity.this,R.layout.student_list_layout,students));
    }

    public void showToast(String str){
        Toast toast=Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM, 0, 100);
        toast.show();
    }

    private class StuAdapter extends ArrayAdapter<Student>{
        public StuAdapter(@NonNull Context context, int resource, @NonNull List<Student> objects) {
            super(context, resource, objects);
        }
        @Override
        public Student getItem(int position) {
            return students.get(position);
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
                itemView = LayoutInflater.from(AdminActivity.this).inflate(R.layout.student_list_layout, parent, false);
                holder = new ViewHolder(itemView);
                itemView.setTag(holder);
            } else {
                itemView = convertView;
                holder = (ViewHolder) itemView.getTag();
            }
            Student data = getItem(position);
            holder.txt_id.setText(data.getStu_id());
            holder.txt_name.setText(data.getName());
            holder.txt_major.setText(data.getMajor());
            holder.txt_class.setText(data.getClass_num());
            return itemView;
        }
        public class ViewHolder{
            TextView txt_id;
            TextView txt_name;
            TextView txt_major;
            TextView txt_class;
            public ViewHolder(View itemView) {
                txt_id = itemView.findViewById(R.id.idtxt);
                txt_name = itemView.findViewById(R.id.nametxt);
                txt_major = itemView.findViewById(R.id.majortxt);
                txt_class = itemView.findViewById(R.id.classtxt);
                Resources resources = AdminActivity.this.getResources();
                DisplayMetrics dm = resources.getDisplayMetrics();
                float density = dm.density;
                int width = dm.widthPixels;
                int height = dm.heightPixels;
                txt_id.setWidth(width);
                txt_name.setWidth(width);
                txt_major.setWidth(width);
                txt_class.setWidth(width);
            }
        }
    }
}
package com.example.ppqpplthird;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button btn_jump,btn_add,btn_send;
    private TextView txt_name,txt_pro,txt_title;
    private ListView stulv;
    private int user_num = 0;
    static List<Stu> data = new ArrayList<>();
    static List<Stu> stu = new ArrayList<>();
    String name,pro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_jump = findViewById(R.id.jumpbtn);
        btn_add = findViewById(R.id.addbtn);
        btn_send = findViewById(R.id.sendbtn);

        txt_name = findViewById(R.id.nametext);
        txt_pro = findViewById(R.id.protext);
        txt_title = findViewById(R.id.titletext);

        txt_title.setText(String.valueOf(stu.size()));

        stulv = findViewById(R.id.stulist);

        btn_jump.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.setClass(MainActivity.this,OtherActivity.class);
            startActivity(intent);
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = txt_name.getText().toString();
                pro = txt_pro.getText().toString();
                Stu user = new Stu();
                user.setName(name);
                user.setPro(pro);
                stu.add(user);
                showToast("添加信息成功");
                showlist();
                txt_title.setText(String.valueOf(stu.size()));
            }
        });

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int i=0;i<stu.size();i++){
                    Stu user = stu.get(i);
                    data.add(user);
                }
                stu.clear();
                showToast("存储信息成功");
                txt_title.setText(String.valueOf(stu.size()));
                showlist();
                Intent intent = new Intent();
                intent.setClass(MainActivity.this,OtherActivity.class);
                startActivity(intent);
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

    public void showlist(){
        stulv.setAdapter(new UserAdapter(stu,this));
        stulv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.e("测试","item"+i+"点击成功");
            }
        });
    }

    public void showToast(String str){
        Toast toast=Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM, 0, 100);
        toast.show();
    }
}
package com.example.ppqppl1;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.ppqppl1.databinding.ActivityWork8Binding;

public class WorkActivity8 extends AppCompatActivity {

    private ActivityWork8Binding binding;
    private TextView txt;

    private boolean ifclick = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityWork8Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        txt = findViewById(R.id.maintxt);
        txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ifclick){
                    txt.setText("Hello，重庆交通大学");
                    ifclick = false;
                }
                else {
                    ifclick = true;
                    txt.setText("hello，潘星宇，欢迎参加Android开发学习");
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

}
package com.example.ppqpplfourth;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ppqpplfourth.HomeActivity;
import com.example.ppqpplfourth.R;
import com.example.ppqpplfourth.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private Button login_btn,bu_btn;
    private EditText txt_account,txt_pwd;
    private TextView txt_iflogin;

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

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String account = txt_account.getText().toString();
                String pwd = txt_pwd.getText().toString();
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, HomeActivity.class);
                txt_iflogin.setText("登陆中，请稍后……");
                Log.d("账号",account+"   "+account.length());
                Log.d("密码",pwd+"   "+pwd.length());
                if(account.equals("ppqppl") && pwd.equals("1911817187")) {
                    txt_iflogin.setText(">_< 登录成功！");
                    showToast(">_< 登录成功");
                    startActivity(intent);
                    txt_iflogin.setText("欢迎使用本系统，请登录");
                }
                else {
                    showToast("账号或密码错误，请重试");
                    txt_iflogin.setText("账号或密码错误！");
                }
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
    public void showToast(String str){
        Toast toast=Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM, 0, 100);
        toast.show();
    }

}
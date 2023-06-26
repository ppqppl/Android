package com.example.ppqppl1;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.ppqppl1.databinding.ActivityHomeBinding;

import java.util.List;

import bean.Constant;
import bean.Photo;
import utils.DbManger;
import utils.ImgUtils;
import utils.SqLiteHelper;

public class HomeActivity extends AppCompatActivity {

    private ActivityHomeBinding binding;

    private Button btn_takeaphoto,callmsg_btn,btn_toolbox,btn_ui,btn_admin,btn_stu;

    private SQLiteDatabase db_img = null;
    private SqLiteHelper sqLiteHelper = new SqLiteHelper(HomeActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        CreateDB();
        new UpdatePhotolist().start();

        btn_takeaphoto = findViewById(R.id.takeaphotobtn);
        callmsg_btn = findViewById(R.id.callmsgbtn);
        btn_toolbox = findViewById(R.id.toolboxbtn);
        btn_ui = findViewById(R.id.UIbtn);
        btn_admin = findViewById(R.id.userbtn);
        btn_stu = findViewById(R.id.stubtn);

        if(MainActivity.Login_identity.equals("stu")){
            btn_admin.setText("个人信息");
            if(MainActivity.stu_login.getIfchosecourse() == 1){
                btn_stu.setText("课程信息");
            }
        }
        else if(MainActivity.Login_identity.equals("tea")){
            btn_stu.setText("课程系统");
        }

        btn_stu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainActivity.Login_identity.equals("stu")) {
                    Intent intent = new Intent();
                    intent.setClass(HomeActivity.this, CoursechooseActivity.class);
                    startActivity(intent);
                }
                else {
                    Intent intent = new Intent();
                    intent.setClass(HomeActivity.this, ShowcourselistActivity.class);
                    startActivity(intent);
                }
            }
        });

        btn_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainActivity.Login_identity.equals("stu")) {
                    Intent intent = new Intent();
                    intent.setClass(HomeActivity.this,ShowStudentMsgActivity.class);
                    startActivity(intent);
                }
                else{
                    Intent intent = new Intent();
                    intent.setClass(HomeActivity.this, AdminActivity.class);
                    startActivity(intent);
                }
            }
        });

        btn_ui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(HomeActivity.this,PageDesignActivity.class);
                startActivity(intent);
            }
        });

        btn_takeaphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(HomeActivity.this,TakeAPhotoActivity.class);
                startActivity(intent);
            }
        });

        callmsg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(HomeActivity.this,CommunicateActivity.class);
                startActivity(intent);
            }
        });

        btn_toolbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(HomeActivity.this,MytoolsActivity.class);
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

    public void CreateDB(){
//        photodb = sqLiteHelper.getWritableDatabase(); // 内存已满会抛出异常
        db_img = sqLiteHelper.getReadableDatabase();
    }

    public class UpdatePhotolist extends Thread {
        @Override
        public void run() {
            super.run();
            db_img = sqLiteHelper.getWritableDatabase();
            String selectSql="select * from "+ Constant.TABLE_NAME;
            Cursor cursor= DbManger.ImgselectDataBySql(db_img,selectSql,null);
            TakeAPhotoActivity.photos = DbManger.ImgcursorToList(cursor);
            for (int i = 0; i < TakeAPhotoActivity.photos.size(); i++) {
                Bitmap bitmap = null;
                bitmap = ImgUtils.GetBitmap(TakeAPhotoActivity.photos.get(i),HomeActivity.this);
                int bitwidth = bitmap.getWidth();
                int bitheight = bitmap.getHeight();
                if (bitwidth > bitheight) {
                    double com = ((double) bitwidth * 1.0) / ((double) bitheight * 1.0);
                    bitheight = 512;
                    bitwidth = (int) (com * bitheight);
                } else {
                    double com = ((double) bitheight * 1.0) / ((double) bitwidth * 1.0);
                    bitwidth = 512;
                    bitheight = (int) (com * bitwidth);
                }

                bitmap = ImgUtils.zoomBysize(bitmap, bitwidth, bitheight);

                TakeAPhotoActivity.photos.get(i).setBitmap(bitmap);
            }
        }
    }

}
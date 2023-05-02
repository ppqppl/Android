package com.example.hp.game_not_step_white;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.WindowManager;
public class MainActivity extends AppCompatActivity {

    private View_Game mView_game;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getSupportActionBar()!=null)getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        WindowManager manager = getWindowManager();
        DisplayMetrics metrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(metrics);//获取屏幕高宽
        int width = metrics.widthPixels;  //以像素为单位
        int height = metrics.heightPixels;

        mView_game=findViewById(R.id.mView_game);
        mView_game.setScreenNum(height,width);
        mView_game.init();
        mView_game.setOnClickListener(mView_game);
    }
}
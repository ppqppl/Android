package com.example.ppqppl1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ViewSwitcher.ViewFactory;
public class Activity_work7 extends AppCompatActivity implements View.OnClickListener,ViewFactory {
    private ImageSwitcher imageSwitcher;
    private Button imageButton;
    private Button iamgeButton1;
    private int image[]={R.drawable.s1,R.drawable.s2,R.drawable.s3,R.drawable.s4,R.drawable.s5};
    private String image1[]={"https://c-ssl.duitang.com/uploads/item/201712/03/20171203153850_NxjMi.jpeg","https://c-ssl.duitang.com/uploads/item/201805/25/20180525210744_8FByW.jpeg"};
    private int imageIndex=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work7);
        imageButton=(Button)findViewById(R.id.imageButton);
        iamgeButton1=(Button)findViewById(R.id.imageButton1);
        imageSwitcher=(ImageSwitcher)findViewById(R.id.imageSwitcher);
        imageButton.setOnClickListener(this);
        iamgeButton1.setOnClickListener(this);
        init();
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
    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.imageButton){
            imageIndex--;
            if(imageIndex<0){
                imageIndex=image.length-1;
            }
            imageSwitcher.setInAnimation(this,R.anim.left_in);
            imageSwitcher.setOutAnimation(this,R.anim. right_out);
        }else if(view.getId()==R.id.imageButton1){
            imageIndex++;
            if(imageIndex>4){
                imageIndex=0;
            }
            imageSwitcher.setInAnimation(this,R.anim.right_in);
            imageSwitcher.setOutAnimation(this,R.anim.left_out);
        }
        imageSwitcher.setImageResource(image[imageIndex]);
    }
    @Override
    public View makeView() {

        ImageView imageView=new ImageView(this);
        return imageView;
    }
    private void init(){
        imageSwitcher.setFactory(Activity_work7.this);
        imageSwitcher.setImageResource(image[imageIndex]);
    }
}

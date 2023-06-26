package com.example.ppqppl1;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ppqppl1.databinding.ActivityGetPhotoBinding;

import java.io.FileNotFoundException;

import bean.Photo;
import utils.ImgUtils;

public class GetPhotoActivity extends AppCompatActivity implements View.OnTouchListener, ViewSwitcher.ViewFactory {

    private ActivityGetPhotoBinding binding;

    private ImageSwitcher imageSwitcher;
    private TextView txt_name,txt_info;
    private Photo data;

    private int num_img,num,height,width;
    private float downX = 0, lastX = 0;

    private LayoutInflater.Factory mFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityGetPhotoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        imageSwitcher = findViewById(R.id.imageShow);
        txt_info = findViewById(R.id.pic_info);
        txt_name = findViewById(R.id.pic_name);

        imageSwitcher.setOnTouchListener(this);
        imageSwitcher.setFactory(this);

        Intent getnum = this.getIntent();
        num = getnum.getIntExtra("Photo_num",-1);
        num_img = num;
        if(num_img == -1){
            showToast("图片获取失败");
        } else{
            data = TakeAPhotoActivity.photos.get(num_img);

            Bitmap bitmap = null;
            bitmap = ImgUtils.GetBitmap(data,this);
            //                ContentResolver cr = getContentResolver();
//                bitmap = BitmapFactory.decodeStream(cr.openInputStream(Uri.parse(data.getFilepath())));
            if (bitmap == null) {
                showToast("null");
            } else {
                int bitheight = bitmap.getHeight();
                int bitwidth = bitmap.getWidth();
                height = 1536*3;
                width = (height * bitwidth) / bitheight;
                if(width>height){
                    width = 1024*3;
                    height = (width * bitheight) / bitwidth;
                }
                bitmap = ImgUtils.zoomBysize(bitmap,width,height);
                Bitmap finalBitmap = bitmap;
                txt_name.setText(data.getFileName());
                txt_info.setText(data.getInfo());
                Drawable drawable =new BitmapDrawable(finalBitmap);
                imageSwitcher.setImageDrawable(drawable);
            }

//            set_img();

//            imageSwitcher.setImageURI(Uri.parse(uuu));

        }

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

    public Bitmap GetPic(Uri uri){
        Bitmap bitmap = null;

        return bitmap;
    }
    public static String getRealPathFromUri(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            Log.i("i", "getRealPathFromUri: " + contentUri);
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            if (cursor != null && cursor.getColumnCount() > 0) {
                cursor.moveToFirst();
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                String path = cursor.getString(column_index);
                Log.i("i", "getRealPathFromUri: column_index=" + column_index + ", path=" + path);
                return path;
            } else {
                Log.w("i", "getRealPathFromUri: invalid cursor=" + cursor + ", contentUri=" + contentUri);
            }
        } catch (Exception e) {
            Log.e("i", "getRealPathFromUri failed: " + e  + ", contentUri=" + contentUri, e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return "";
    }

    public View makeView(){

        ImageView imageView=new ImageView(GetPhotoActivity.this);
        imageView.setBackgroundColor(0x00000000);
//        imageView.setImageBitmap(bitmap);
        imageView.setScaleType(ImageView.ScaleType.CENTER);//居中显示
//        imageView.setLayoutParams(new ImageSwitcher.LayoutParams(WindowManager.LayoutParams.FILL_PARENT, WindowManager.LayoutParams.FILL_PARENT));//定义组件
        imageView.setLayoutParams(new ImageSwitcher.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT));//定义组件
        return imageView;
    }

   public void set_img() {
       if (num_img == -1) {
       } else {
           data = TakeAPhotoActivity.photos.get(num_img);

           Bitmap bitmap = null;
           bitmap = ImgUtils.GetBitmap(data,this);
           //               ContentResolver cr = getContentResolver();
//               bitmap = BitmapFactory.decodeStream(cr.openInputStream(Uri.parse(data.getFilepath())));
           if (bitmap == null) {
               showToast("null");
           } else {
               int bitheight = bitmap.getHeight();
               int bitwidth = bitmap.getWidth();
               height = 1536 * 3;
               width = (height * bitwidth) / bitheight;
               if (width > height) {
                   width = 1024 * 3;
                   height = (width * bitheight) / bitwidth;
               }
               bitmap = ImgUtils.zoomBysize(bitmap, width, height);
               Bitmap finalBitmap = bitmap;
               txt_name.setText(data.getFileName());
               txt_info.setText(data.getInfo());
               Drawable drawable = new BitmapDrawable(finalBitmap);
               imageSwitcher.setImageDrawable(drawable);
           }
       }
   }

    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:{
                downX = event.getX();
                break;
            }
            case MotionEvent.ACTION_UP:{
                lastX = event.getX();
//                showToast(String.valueOf(lastX)+"   "+String.valueOf(downX));
                //抬起的时候的X坐标大于按下的时候就显示上一张图片
                if(lastX>downX){
                    if(num_img >= 0){
                        //设置动画，这里的动画比较简单，不明白的去网上看看相关内容
                        imageSwitcher.setInAnimation(AnimationUtils.loadAnimation(getApplication(), R.anim.left_in));
                        imageSwitcher.setOutAnimation(this,R.anim.right_out);
                        num_img --;
                        set_img();
                    }else{
                        num_img = 0;
                        showToast("已经是第一张");
                    }
                }
                else if(downX>lastX){
                    if(num_img < TakeAPhotoActivity.photos.size() - 1){
                        imageSwitcher.setInAnimation(AnimationUtils.loadAnimation(getApplication(), R.anim.right_in));
                        imageSwitcher.setOutAnimation(AnimationUtils.loadAnimation(getApplication(), R.anim.left_out));
                        num_img ++ ;
                        set_img();
                    }else{
                        showToast("已经是最后一张");
                        num_img = TakeAPhotoActivity.photos.size()-1;
                    }
                }
                break;
            }
        }

        return true;
    }

    public void showToast(String str){
        Toast toast=Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM, 0, 100);
        toast.show();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}
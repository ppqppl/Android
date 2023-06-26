package com.example.fifth;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
public class Activity_work6 extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener{
    private ImageView picture;
    private SeekBar scale1;
    private SeekBar scale2;
    private RadioGroup imageSelect;
    private Matrix matrix;
    private TextView suo;
    private TextView xuan;
    private int minWidth = 400;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work6);
        picture=(ImageView)findViewById(R.id.p);
        suo=(TextView)findViewById(R.id.suo);
        xuan=(TextView)findViewById(R.id.xuan);
        scale1=(SeekBar)findViewById(R.id.power1);
        scale2=(SeekBar)findViewById(R.id.power2);
        imageSelect=(RadioGroup)findViewById(R.id.RadioGroup);
        scale1.setOnSeekBarChangeListener(this);
        scale2.setOnSeekBarChangeListener(this);
        matrix = new Matrix();
        init();
        //屏幕宽度
        DisplayMetrics dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screen=dm.widthPixels;
        //设置硕放滑动最大值
        scale1.setMax(screen);
        scale2.setMax(360);
        imageSelect.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton radioButton=(RadioButton)findViewById(i);
                String radioName=radioButton.getText().toString();
                String imageLink="";
                if (radioName.equals("图片一")){
                    imageLink="https://images.cnblogs.com/cnblogs_com/blogs/746187/galleries/2160861/o_220514152834_04.webp";
                    setDownBitmap(imageLink,Activity_work6.this);
                }else if (radioName.equals("图片二")){
                    imageLink="https://images.cnblogs.com/cnblogs_com/blogs/746187/galleries/2160861/o_220515060900_06.webp";
                    setDownBitmap(imageLink,Activity_work6.this);
                }
                else if (radioName.equals("图片三")){
                    imageLink="https://c-ssl.duitang.com/uploads/blog/201506/06/20150606181240_s2rMJ.jpeg";
                    setDownBitmap(imageLink,Activity_work6.this);
                }
                else if (radioName.equals("图片四")){
                    imageLink="https://c-ssl.duitang.com/uploads/item/201712/03/20171203153850_NxjMi.jpeg";
                    setDownBitmap(imageLink,Activity_work6.this);
                }
            }
        });
    }
    private void setDownBitmap(final String link, final Activity activity){
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("OK");
                try {
                    URL url = new URL(link);
                    try {
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.setConnectTimeout(5000);
                        connection.setDoInput(true);
                        connection.connect();
                        int code = connection.getResponseCode();
                        if (code == 200) {
                            InputStream inputStream = connection.getInputStream();
                            final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    picture.setImageBitmap(bitmap);
                                }
                            });
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    public void init(){
        picture.setImageResource(R.drawable.pto2);
    }
    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b){
        Matrix matrix = new Matrix();
        //Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.pto2);
        if (seekBar.getId() == R.id.power1) {
            int newWidth = i + minWidth;
            int newHeight = (int) (newWidth * 3 / 4);//按照原图片的比例缩放
            picture.setLayoutParams(new LinearLayout.LayoutParams(newWidth, newHeight));
            suo.setText("缩放倍数为:" + i + "%");
        } else if (seekBar.getId() == R.id.power2) {
            float d=i;
            picture.setPivotX(picture.getWidth()/2);
            picture.setPivotY(picture.getHeight()/2);//支点在图片中心
            picture.setRotation(d);
            xuan.setText("旋转角度为:" + i + "度");
        }
    }
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }
}

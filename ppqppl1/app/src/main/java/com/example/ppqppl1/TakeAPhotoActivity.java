package com.example.ppqppl1;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.ppqppl1.databinding.ActivityTakeAPhotoBinding;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import bean.Constant;
import utils.DbManger;
import bean.Photo;
import utils.Permission;
import utils.ImgUtils;
import utils.SqLiteHelper;

public class TakeAPhotoActivity extends AppCompatActivity {

    private ActivityTakeAPhotoBinding binding;

    private ListView photolist;
    private ImageButton btn_addphoto;

    private SqLiteHelper sqLiteHelper = new SqLiteHelper(TakeAPhotoActivity.this);
    private SQLiteDatabase db = null;

    public static List<Photo> photos = new ArrayList<>();

    private Uri photouri;

    private int start,end,if_roll = 0;

    private static final int MY_PERMISSIONS_REQUEST_TAKE_PHOTO = 0,MY_PERMISSIONS_REQUEST_GET_PHOTO_PERMISSION = 1, MY_PERMISSIONS_REQUEST_GET_PHOTO = 2, MY_PERMISSIONS_REQUEST_GET_FILEPERMISSION = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityTakeAPhotoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // 打开界面获取权限
//        initPhotoError();   // 解决相机使用 putExtra 函数报错

        sqLiteHelper = DbManger.getIntance(this);

        CreateDB();

        photolist = findViewById(R.id.photoList);
        btn_addphoto = findViewById(R.id.addphotobtn);

        start = photolist.getFirstVisiblePosition();
        end = photolist.getLastVisiblePosition();
        showphotolist();

        photolist.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE://停止  0
                        if_roll = 0;
                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL://触摸滑动
                        if_roll = 1;
                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_FLING://快速滑动    2
                        if_roll = 1;
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                start = firstVisibleItem;
                end = visibleItemCount + firstVisibleItem - 1;
//                new UpdatePhoto().start();
            }
        });

        btn_addphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View dialogView = LayoutInflater.from(TakeAPhotoActivity.this).inflate(R.layout.pic_add_chose_layout, null);
                AlertDialog alertDialog = new AlertDialog.Builder(TakeAPhotoActivity.this).create();
                alertDialog.setView(dialogView, 0, 0, 0, 0);
                alertDialog.show();
                Button btn_from_camera = dialogView.findViewById(R.id.addfromcamerabtn);
                Button btn_from_gallery = dialogView.findViewById(R.id.addfrompicturebtn);
                btn_from_camera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(Permission.checknowPermission(TakeAPhotoActivity.this,Manifest.permission.CAMERA)){
                            camera();
                        }
                        else{
                            showToast("需要获取相机权限");
                        }
                        alertDialog.dismiss();
                    }
                });
                btn_from_gallery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                            // 检查是否有权限
                            if (Environment.isExternalStorageManager()) {
                                PicFromFile();;
                                // 授权成功
                            } else {
                                GetFilePermission();
                                // 授权失败
                            }
                        }
                        alertDialog.dismiss();
                    }
                });
            }
        });

        photolist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Photo data = photos.get(position);
                Intent intent = new Intent();
                intent.setClass(TakeAPhotoActivity.this,GetPhotoActivity.class);
                intent.putExtra("Photo_num",position);
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

    public boolean showphotolist(){
        if(photos.size() == 0 || photos == null){
            return false;
        }
        photolist.setAdapter(new PhotoAdapter(TakeAPhotoActivity.this,R.layout.pic_list_layout,photos));
        return true;
    }

    public void camera(){
        photouri = null;
        Intent intent = new Intent(); //调用照相机
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
//        photouri = SaveImg.SaveImgBeforeResult(TakeAPhotoActivity.this);
        photouri = ImgUtils.SaveToConstPathBeforeResult(TakeAPhotoActivity.this);
        // 指定存储路径，这样就可以保存原图了
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photouri);
        startActivityForResult(intent, MY_PERMISSIONS_REQUEST_TAKE_PHOTO);  // 设置函数回调码

//        startActivity(intent);
    }

    public void PicFromFile(){
//        Intent intent = new Intent();
//        intent.setAction(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        startActivityForResult(intent, MY_PERMISSIONS_REQUEST_GET_PHOTO);  // 设置函数回调码
        // 使用意图直接调用手机相册
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // 打开手机相册,设置请求码
        startActivityForResult(intent, MY_PERMISSIONS_REQUEST_GET_PHOTO);
        //在这里跳转到手机系统相册里面
    }

    public Uri getRealPathFromURI(Uri contentUri) { // 通过返回的 data 获取图片真实路径
        Uri res = null;
        String[] proj = {MediaStore.Images.Media.DATA};

        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = Uri.parse(cursor.getString(column_index));
        }
        cursor.close();
        return res;
    }

    private Boolean JudgeCamPermission(){  // 判断并获取权限
        if (ContextCompat.checkSelfPermission(TakeAPhotoActivity.this,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            // 没有获得授权，申请授权
            if (ActivityCompat.shouldShowRequestPermissionRationale(TakeAPhotoActivity.this,
                    Manifest.permission.CAMERA)) {
                // 返回值：
//                          如果app之前请求过该权限,被用户拒绝, 这个方法就会返回true.
//                          如果用户之前拒绝权限的时候勾选了对话框中”Don’t ask again”的选项,那么这个方法会返回false.
//                          如果设备策略禁止应用拥有这条权限, 这个方法也返回false.
                // 弹窗需要解释为何需要该权限，再次请求授权
                // 帮跳转到该应用的设置界面，让用户手动授权
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            }else{
                // 不需要解释为何需要该权限，直接请求授权
                ActivityCompat.requestPermissions(TakeAPhotoActivity.this,
                        new String[]{Manifest.permission.CAMERA},
                        MY_PERMISSIONS_REQUEST_TAKE_PHOTO);
            }
        }else {
            return true;
        }
        return false;
    }

    private void GetFilePermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && ContextCompat.checkSelfPermission(TakeAPhotoActivity.this,
                Manifest.permission.MANAGE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {// android 11  且 不是已经被拒绝
            // 先判断有没有权限
            if (!Environment.isExternalStorageManager()) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.setData(Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, MY_PERMISSIONS_REQUEST_GET_FILEPERMISSION);
            }
        }
    }

    public void CreateDB(){
//        photodb = sqLiteHelper.getWritableDatabase(); // 内存已满会抛出异常
        db = sqLiteHelper.getReadableDatabase();
    }

    // 保存图片在系统文件夹下


    public void showToast(String str){
        Toast toast=Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM, 0, 100);
        toast.show();
    }


    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {   // 回调函数
        super.onActivityResult(requestCode, resultCode, data);
        if(data!=null) {
            Bundle bundle = data.getExtras();
                switch (requestCode) {
                    case MY_PERMISSIONS_REQUEST_TAKE_PHOTO: {
                        Photo photo = new Photo();
                        Bitmap bitmap = (Bitmap) bundle.get("data");
//                        Log.i("aaaaaa",data.getData());
                        photo.setBitmap(bitmap);
                        View dialogView = LayoutInflater.from(TakeAPhotoActivity.this).inflate(R.layout.pic_add_title_layout, null);
                        AlertDialog alertDialog = new AlertDialog.Builder(TakeAPhotoActivity.this).create();
                        alertDialog.setView(dialogView, 0, 0, 0, 0);
                        alertDialog.show();
                        Button btn_savepic,btn_cancelpic;
                        EditText txt_filename,txt_info;
                        btn_savepic = alertDialog.findViewById(R.id.saveimgbtn);
                        btn_cancelpic = alertDialog.findViewById(R.id.cancelimgbtn);
                        txt_filename = alertDialog.findViewById(R.id.picnametxt);
                        txt_info = alertDialog.findViewById(R.id.picmsgtxt);
                        btn_savepic.setOnClickListener(new View.OnClickListener() { //确认保存，保存在系统图库种
                            @Override
                            public void onClick(View v) {
                                String filename = txt_filename.getText().toString();
                                String info = txt_info.getText().toString();
                                photo.setFileName(filename);
                                photo.setInfo(info);
                                alertDialog.dismiss();
                                btn_addphoto.setImageBitmap(bitmap);
//                                SaveImg.SaveImgToGallery(TakeAPhotoActivity.this,bitmap);
                                ImgUtils.saveBitmap(TakeAPhotoActivity.this,bitmap);
                            }
                        });
                        btn_cancelpic.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                alertDialog.dismiss();
                            }
                        });
                        break;
                    }
                    case MY_PERMISSIONS_REQUEST_GET_PHOTO: {//这里的requestCode是我自己设置的，就是确定返回到那个Activity的标志
                        if (resultCode == RESULT_OK) {//resultcode是setResult里面设置的code值
                            Uri selectedImage = data.getData(); //获取系统返回的照片的Uri
                            photouri = getRealPathFromURI(selectedImage);//获取uri
                            Photo photo = new Photo();
                            photo.setFilepath(getRealPathFromURI(selectedImage).toString());
                            Bitmap bitmap = null;
//                            BitmapFactory.Options options = new BitmapFactory.Options();
//                            bitmap = BitmapFactory.decodeFile(photouri.toString(), options);
                            bitmap = ImgUtils.GetBitmap(photo,this);
                            //                          Bitmap bitmap = ImgUtils.LoadImgFromLocal(TakeAPhotoActivity.this,selectedImage);
//                          bitmap = ImgUtils.zoomImg(bitmap);
//                            BitmapFactory.Options options = new BitmapFactory.Options();
//                            bitmap = BitmapFactory.decodeFile(photouri.toString(), options);
                            if(bitmap == null){
                                showToast("null");
                            } else{
                                View dialogView = LayoutInflater.from(TakeAPhotoActivity.this).inflate(R.layout.pic_add_title_layout, null);
                                AlertDialog alertDialog = new AlertDialog.Builder(TakeAPhotoActivity.this).create();
                                alertDialog.setView(dialogView, 0, 0, 0, 0);
                                alertDialog.show();
                                Button btn_savepic, btn_cancelpic;
                                EditText txt_filename, txt_info;
                                btn_savepic = alertDialog.findViewById(R.id.saveimgbtn);
                                btn_cancelpic = alertDialog.findViewById(R.id.cancelimgbtn);
                                txt_filename = alertDialog.findViewById(R.id.picnametxt);
                                txt_info = alertDialog.findViewById(R.id.picmsgtxt);
                                Bitmap finalBitmap = bitmap;
                                btn_savepic.setOnClickListener(new View.OnClickListener() { //确认保存，保存在系统图库种
                                    @Override
                                    public void onClick(View v) {

                                        photo.setBitmap(finalBitmap);
                                        String filename = txt_filename.getText().toString();
                                        String info = txt_info.getText().toString();
                                        photo.setFileName(filename);
                                        photo.setInfo(info);
                                        if(!filename.equals("")) {
                                            photo.setFilepath(photouri.toString());
//                                btn_addphoto.setImageBitmap(bitmap[0]);   // 用于测试
                                            db = sqLiteHelper.getReadableDatabase();
                                            ContentValues valuesI = new ContentValues();
                                            valuesI.put(Constant.INFO, photo.getInfo());
                                            valuesI.put(Constant.FILEPATH, photo.getFilepath());
                                            valuesI.put(Constant.FILENAME, photo.getFileName());
                                            long result = db.insert(Constant.TABLE_NAME, null, valuesI);
                                            if (result > 0) {
                                                photos.add(photo);
                                                showphotolist();
                                            } else {
                                                showToast("新增图像失败");
                                            }
                                            alertDialog.dismiss();
//                                SaveImg.SaveImgToGallery(TakeAPhotoActivity.this,bitmap);
                                        }
                                        else{
                                            showToast("文件名称不能为空");
                                        }
                                        db.close();
                                    }
                                });
                                btn_cancelpic.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        alertDialog.dismiss();
                                    }
                                });
                            }
                        }
                        break;
                    }
                    case MY_PERMISSIONS_REQUEST_GET_FILEPERMISSION:{
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                            // 检查是否有权限
                            if (Environment.isExternalStorageManager()) {
                                PicFromFile();
                                // 授权成功
                            } else {
                                showToast("获取全部文件管理权限失败");
                                // 授权失败
                            }
                        }
                        break;
                    }
                }
        }
        else{
            switch (requestCode) {
                case MY_PERMISSIONS_REQUEST_TAKE_PHOTO: {
                    Photo photo = new Photo();
                    final Bitmap[] bitmap = {null};
                    try {
                        ContentResolver cr = getContentResolver();
                        bitmap[0] = BitmapFactory.decodeStream(cr.openInputStream(photouri));
                        bitmap[0] = ImgUtils.zoomImg(bitmap[0]);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        Log.e("加载图片", "失败");
                    }
                    if(bitmap[0] != null) {
                        View dialogView = LayoutInflater.from(TakeAPhotoActivity.this).inflate(R.layout.pic_add_title_layout, null);
                        AlertDialog alertDialog = new AlertDialog.Builder(TakeAPhotoActivity.this).create();
                        alertDialog.setView(dialogView, 0, 0, 0, 0);
                        alertDialog.show();
                        Button btn_savepic, btn_cancelpic;
                        EditText txt_filename, txt_info;
                        btn_savepic = alertDialog.findViewById(R.id.saveimgbtn);
                        btn_cancelpic = alertDialog.findViewById(R.id.cancelimgbtn);
                        txt_filename = alertDialog.findViewById(R.id.picnametxt);
                        txt_info = alertDialog.findViewById(R.id.picmsgtxt);
                        btn_savepic.setOnClickListener(new View.OnClickListener() { //确认保存，保存在系统图库种
                            @Override
                            public void onClick(View v) {

                                photo.setBitmap(bitmap[0]);
                                String filename = txt_filename.getText().toString();
                                String info = txt_info.getText().toString();
                                photo.setFileName(filename);
                                photo.setInfo(info);
                                if(!filename.equals("")) {
                                    photo.setFilepath(photouri.toString());
//                                btn_addphoto.setImageBitmap(bitmap[0]);   // 用于测试
                                    db = sqLiteHelper.getReadableDatabase();
                                    ContentValues valuesI = new ContentValues();
                                    valuesI.put(Constant.INFO, photo.getInfo());
                                    valuesI.put(Constant.FILEPATH, photo.getFilepath());
                                    valuesI.put(Constant.FILENAME, photo.getFileName());
                                    long result = db.insert(Constant.TABLE_NAME, null, valuesI);
                                    if (result > 0) {
                                        photos.add(photo);
                                        showphotolist();
                                    } else {
                                        showToast("新增图像失败");
                                    }
                                    alertDialog.dismiss();
//                                SaveImg.SaveImgToGallery(TakeAPhotoActivity.this,bitmap);
                                }
                                else{
                                    showToast("文件名称不能为空");
                                }
                                db.close();
                            }
                        });
                        btn_cancelpic.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                alertDialog.dismiss();
                            }
                        });
                    }
                    break;
                }
            }
        }
    }

    public class PhotoAdapter extends ArrayAdapter<Photo> {

        public PhotoAdapter(@NonNull Context context, int resource, @NonNull List<Photo> objects) {
            super(context, resource, objects);
        }

        private void updateItem(int position) {
            View view = photolist.getChildAt(position);
            PhotoAdapter.ViewHolder holder = (PhotoAdapter.ViewHolder)view.getTag();
            showToast((String) holder.txt_title.getText());
            //记得更新list数据源中position位置的数据，避免滑动后局部刷新失效
        }

        @Override
        public Photo getItem(int position) {
            return photos.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public void updateView(View view,int position){

        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View itemView;
            ViewHolder holder = null;
            if (convertView == null) {
                itemView = LayoutInflater.from(TakeAPhotoActivity.this).inflate(R.layout.pic_list_layout, parent, false);
                holder = new ViewHolder(itemView);
                itemView.setTag(holder);
            } else {
                itemView = convertView;
                holder = (ViewHolder) itemView.getTag();
            }

            Photo data = getItem(position);
            holder.txt_title.setText(data.getFileName());
            holder.txt_info.setText(data.getInfo());
            if(data.getBitmap() != null){
                holder.view_img.setImageBitmap(data.getBitmap());
            } else{

                holder.view_img.setImageResource(R.drawable.default_image);
            }
            return itemView;
        }



        public class ViewHolder{
            TextView txt_title;
            TextView txt_info;
            ImageView view_img;

            public ViewHolder(View itemView) {
                txt_title = itemView.findViewById(R.id.nametxt);
                txt_info = itemView.findViewById(R.id.infotxt);
                view_img = itemView.findViewById(R.id.mainpic);
            }
        }
    }

    Runnable ListRoll = new Runnable() {
        @Override
        public void run() {
            int last = photolist.getLastVisiblePosition();
            if(last == photolist.getCount() - 1 && photolist.getChildAt(last).getBottom() <= photolist.getHeight()) {
                // It fits!
            }
            else {
                // It doesn't fit...
            }
        }
    };

}

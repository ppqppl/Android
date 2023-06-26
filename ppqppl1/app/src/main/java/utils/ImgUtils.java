package utils;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import bean.Photo;

public class ImgUtils {
    public static void SaveImgToGallery(Context context, Bitmap bitmap){
        if(bitmap == null){
            Log.w("存储图片","图片为空");
            return;
        }
        String fileName = GetImgFilename();
        // 保存图片
        File newimg = new File(Environment.getExternalStorageDirectory(),"Pictures/ppqppl");
        if(!newimg.exists()){
            newimg.mkdir();
        }
        File imgfile = new File(newimg,fileName);
        try{
            FileOutputStream fileOutputStream = new FileOutputStream(imgfile);
//            bitmap.compress(Bitmap.CompressFormat.PNG,100,fileOutputStream);
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,fileOutputStream);
        }catch (FileNotFoundException e){
            e.printStackTrace();
            Log.e("i/o流文件","拒绝访问");
        }
        // 把文件插入到系统图库
        try{
            if(Build.VERSION.SDK_INT<=29) {
                MediaStore.Images.Media.insertImage(context.getContentResolver(), imgfile.getPath(), fileName, null);
                Log.i("安卓版本","小于29");
            }
            else {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, imgfile.getAbsolutePath());
                values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
                Uri uri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                Log.i("安卓版本","大于29");
            }
            Log.i("图片插入到图库","成功");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.i("图片插入到图库","失败");
        }
    }

    public static Uri SaveImgBeforeResult(Context context){ // 直接保存到应用名称对应路径下
        Uri photouri = null;
        String fileName = GetImgFilename();
        File newimg = new File(Environment.getExternalStorageDirectory(),"ppqppl1");
        if(!newimg.exists()){
            newimg.mkdir();
        }
        File imgfile = new File(newimg,fileName);
        photouri = Uri.fromFile(imgfile);
        return photouri;
    }

    public static Uri SaveToConstPathBeforeResult(Context context){ // 保存图片到指定路径
        String fileName = GetImgFilename();
        ContentValues values = new ContentValues(); // 设置路径规则
        values.put(MediaStore.Images.Media.DESCRIPTION, "This is an image");
        values.put(MediaStore.Images.Media.DISPLAY_NAME, fileName);
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
        values.put(MediaStore.Images.Media.TITLE, System.currentTimeMillis()+".png");
//        values.put(MediaStore.Images.Media.RELATIVE_PATH, "DCIM/Camera");
        values.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/ppqppl");

        Uri external = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        ContentResolver resolver = context.getContentResolver();

        Uri insertUri = resolver.insert(external, values);
        File newimg = new File(insertUri.toString());
        if(!newimg.exists()){
            newimg.mkdir();
        }
        return insertUri;
    }

    public static void saveBitmap(Context context, Bitmap bitmap) {

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DESCRIPTION, "This is an image");
        values.put(MediaStore.Images.Media.DISPLAY_NAME, "Image.png");
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
        values.put(MediaStore.Images.Media.TITLE, System.currentTimeMillis()+".png");
//        values.put(MediaStore.Images.Media.RELATIVE_PATH, "DCIM/Camera");
        values.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/ppqppl");

        Uri external = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        ContentResolver resolver = context.getContentResolver();

        Uri insertUri = resolver.insert(external, values);
        OutputStream os = null;
        if (insertUri != null) {
            try {
                os = resolver.openOutputStream(insertUri);
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, os);
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                try {
                    if (os != null) {
                        os.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static String GetImgFilename(){
        DateFormat filedate = new SimpleDateFormat("yyMMdd_HHmmss");
        String fileName = "ppqppl_"+filedate.format(new Date())+"_.jpg";
        return fileName;
    }

    public static Bitmap LoadImgFromLocal(Context context,Uri uri){
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri,
                filePathColumn, null, null, null);//从系统表中查询指定Uri对应的照片
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String path = cursor.getString(columnIndex);  //获取照片路径
        cursor.close();
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        return bitmap;
    }

    //192*256

    public static Bitmap ScaleImg(Bitmap bitmap){
        /*能兼容特殊机型，如一加手机，三星手机等
        这个中方案不管是在小米、华为、oppo、vivo上都没有多大的毛病
        但是如上的两种特殊的机型不行（或许还有其他的不过没那么多测试机)*/
        Bitmap scalebit = null;
        // 获得图片的宽高
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        // 设置想要的大小
        int newWidth = 192;
        int newHeight = 256;
        // 计算缩放比例
        float scaleWidth = 1.0f*newWidth / width;
        float scaleHeight = 1.0f*newHeight / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
        scalebit = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        return scalebit;
    }

    public static Bitmap zoomImg(Bitmap bitmap) {  //可兼容绝大多数的机型，包括一些特殊的机型，如一加手机、三星手机
        int srcWidth = bitmap.getWidth();
        int srcHeight = bitmap.getHeight();
        // 设置想要的大小
        int targetWidth = 192;
        int targetHeight = 256;
        float widthScale = targetWidth * 1.0f / srcWidth;
        float heightScale = targetHeight * 1.0f / srcHeight;
        Matrix matrix = new Matrix();
        matrix.postScale(widthScale, heightScale, 0, 0);
        // 如需要可自行设置 Bitmap.Config.RGB_8888 等等
        Bitmap bmpRet = Bitmap.createBitmap(targetWidth, targetHeight, Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bmpRet);
        Paint paint = new Paint();
        canvas.drawBitmap(bitmap, matrix, paint);
        return bmpRet;
    }



    public static Bitmap Compress(Bitmap bitmap) {
        //图片允许最大空间 单位：KB
        double maxSize =400.00;
        //将bitmap放至数组中，意在bitmap的大小（与实际读取的原文件要大）
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        //将字节换成KB
        double mid = b.length/1024;
        //判断bitmap占用空间是否大于允许最大空间 如果大于则压缩 小于则不压缩
        if (mid > maxSize) {
        //获取bitmap大小 是允许最大大小的多少倍
            double i = mid / maxSize;
//开始压缩 此处用到平方根 将宽带和高度压缩掉对应的平方根倍 （1.保持刻度和高度和原bitmap比率一致，压缩后也达到了最大大小占用空间的大小）
            bitmap = zoomBysize(bitmap, bitmap.getWidth() / Math.sqrt(i),
                    bitmap.getHeight() / Math.sqrt(i));
        }
        return bitmap;
    }
    public static Bitmap zoomBysize(Bitmap bgimage, double newWidth, double newHeight) {
    // 获取这个图片的宽和高
        float width = bgimage.getWidth();
        float height = bgimage.getHeight();
    // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();
    // 计算宽高缩放率
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
    // 缩放图片动作
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width,
                (int) height, matrix, true);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        canvas.drawBitmap(bgimage, matrix, paint);
        return bitmap;
    }
    public static Canvas ZoomCanvas(Bitmap bgimage, double newWidth, double newHeight) {
        // 获取这个图片的宽和高
        float width = bgimage.getWidth();
        float height = bgimage.getHeight();
        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();
        // 计算宽高缩放率
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 缩放图片动作
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap bitmap = Bitmap.createBitmap((int)width,(int)height,Bitmap.Config.ARGB_8888);
//        Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width, (int) height, matrix, true);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        canvas.drawBitmap(bgimage, matrix, paint);
        return canvas;
    }

    public static Bitmap GetBitmap(Photo data, Context context){
        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        bitmap = BitmapFactory.decodeFile(data.getFilepath(), options);
        if(bitmap == null) {
            try {
                ContentResolver cr = context.getContentResolver();
                bitmap = BitmapFactory.decodeStream(cr.openInputStream(Uri.parse(data.getFilepath())));

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Log.e("加载图片", "失败");
            }
        }
        return bitmap;
    }

}

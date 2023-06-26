package utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.ppqppl1.TakeAPhotoActivity;

public class Permission {
    public static final int REQUEST_CODE = 5;
    //定义三个权限
    private static final String[] permission = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_FINE_LOCATION,
    };
    //每个权限是否已授
    public static boolean isPermissionGranted(Activity activity){
        if(Build.VERSION.SDK_INT >= 23){
            for(int i = 0; i < permission.length;i++) {
                int checkPermission = ContextCompat.checkSelfPermission(activity,permission[i]);
                /***
                 * checkPermission返回两个值
                 * 有权限: PackageManager.PERMISSION_GRANTED
                 * 无权限: PackageManager.PERMISSION_DENIED
                 */
                if(checkPermission != PackageManager.PERMISSION_GRANTED){
                    return false;
                }
            }
            return true;
        }else{
            return true;
        }
    }

    public static boolean checkPermission(Activity activity){
        if(isPermissionGranted(activity)) {
            return true;
        } else {
            //如果没有设置过权限许可，则弹出系统的授权窗口
            ActivityCompat.requestPermissions(activity,permission,REQUEST_CODE);
            return false;
        }
    }

    public static Boolean JudgePermission(Activity activity, String permission){
        if(Build.VERSION.SDK_INT >= 23){
            int checkPermission = ContextCompat.checkSelfPermission(activity,permission);
            /***
             * checkPermission返回两个值
             * 有权限: PackageManager.PERMISSION_GRANTED
             * 无权限: PackageManager.PERMISSION_DENIED
             */
            if(checkPermission != PackageManager.PERMISSION_GRANTED){
                if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                        Manifest.permission.CAMERA)) {
                    // 返回值：
//                          如果app之前请求过该权限,被用户拒绝, 这个方法就会返回true.
//                          如果用户之前拒绝权限的时候勾选了对话框中”Don’t ask again”的选项,那么这个方法会返回false.
//                          如果设备策略禁止应用拥有这条权限, 这个方法也返回false.
                    // 弹窗需要解释为何需要该权限，再次请求授权
                    // 帮跳转到该应用的设置界面，让用户手动授权
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
                    intent.setData(uri);
                    activity.startActivity(intent);
                    return false;
                }
                else {
                    return false;
                }
            }
            return true;
        }else{
            return true;
        }
    }
    public static boolean checknowPermission(Activity activity, String single_permission){
        if(JudgePermission(activity,single_permission)) {
            return true;
        } else {
            String [] permissions = new String[]{
                    single_permission,
            };
            //如果没有设置过权限许可，则弹出系统的授权窗口
            ActivityCompat.requestPermissions(activity,permissions,REQUEST_CODE);
            return false;
        }
    }
}

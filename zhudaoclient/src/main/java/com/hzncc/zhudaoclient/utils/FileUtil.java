package com.hzncc.zhudaoclient.utils;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.hzncc.zhudaoclient.R;

import java.io.File;

/**
 * ZhuDao
 * Created by 蔡雨峰 on 2017/9/27.
 */

public class FileUtil {
    public static String OLD = "/mnt/sdcard";
    public static String ROOT = Environment.getExternalStorageDirectory().toString();
    public static final int REQUEST_WRITE = 1;//申请权限的请求码

    public static boolean initDirectory(File file) {
        if (!file.exists()) {
            if (!file.getParentFile().exists()) {
                return file.getParentFile().mkdirs();
            }
        }
        return true;
    }

    public static boolean checkPermission(Context context) {
        //判断是否6.0以上的手机   不是就不用
        if (Build.VERSION.SDK_INT >= 23) {
            //判断是否有这个权限
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                //第一请求权限被取消显示的判断，一般可以不写
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    Log.i("readTosdCard", "我们需要这个权限给你提供存储服务");
                    showAlert(context);
                } else {
                    //2、申请权限: 参数二：权限的数组；参数三：请求码
                    ActivityCompat.requestPermissions(((Activity) context), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE);
                }
                return false;
            }
        }
        return true;
    }

    private static void showAlert(final Context context) {
        Dialog alertDialog = new AlertDialog.Builder(context).
                setTitle("权限说明").
                setMessage("我们需要这个权限给你提供存储服务").
                setIcon(R.mipmap.ic_launcher).
                setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //2、申请权限: 参数二：权限的数组；参数三：请求码
                        ActivityCompat.requestPermissions(((Activity) context),
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE);
                    }
                }).
                setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).
                create();
        alertDialog.show();
    }
}

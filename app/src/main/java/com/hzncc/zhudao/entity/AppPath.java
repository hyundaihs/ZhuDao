package com.hzncc.zhudao.entity;

import android.os.Environment;
import android.util.Log;

import com.hzncc.zhudao.utils.FileUtil;

/**
 * ZhuDao
 * Created by 蔡雨峰 on 2017/3/22.
 */
public class AppPath {
    public static final String SDCARD = Environment.getExternalStorageDirectory().getPath();
    public static String ROOT = SDCARD + "/com.hzncc.zhudao/";
    public static final String IMAGE = ROOT + "image/";
    public static final String VIDEO = ROOT + "video/";
    public static final String LOG = ROOT + "log/";
    public static final String EXCEL = ROOT + "excel/";


    public static boolean initAll() {
        if (!isExistSDCard()) {
            Log.d("AppPath", "sdcard is not exist");
//            ROOT = Environment.getRootDirectory()+ "/com.hzncc.zhudao/";
            return false;
        }
        if (FileUtil.initPath(AppPath.ROOT)) {
            Log.d("AppPath", "ROOT init success");
        } else {
            Log.d("AppPath", "ROOT init failed");
        }
        FileUtil.initPath(AppPath.IMAGE);
        FileUtil.initPath(AppPath.VIDEO);
        FileUtil.initPath(AppPath.LOG);
        FileUtil.initPath(AppPath.EXCEL);
        return true;
    }

    public static boolean isExistSDCard() {
        if (android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED)) {
            return true;
        } else
            return false;
    }
}

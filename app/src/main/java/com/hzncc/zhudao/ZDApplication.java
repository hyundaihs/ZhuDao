package com.hzncc.zhudao;

import android.app.Application;
import android.graphics.Canvas;
import android.graphics.Picture;

import com.hzncc.zhudao.db.DBManager;
import com.hzncc.zhudao.entity.AppPath;
import com.hzncc.zhudao.utils.DialogUtil;
import com.hzncc.zhudao.utils.UsbUtil;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Calendar;


/**
 * ZhuDao
 * Created by 蔡雨峰 on 2017/3/22.
 */
public class ZDApplication extends Application {
    public static DBManager dbManager;
    public static boolean isSdcardExist;

    public static void getRoot() {
        Process process;
        try {
            process = Runtime.getRuntime().exec("su");
            DataOutputStream os = new DataOutputStream(
                    process.getOutputStream());
            os.writeBytes("ifconfig eth0 192.168.1.125 netmask 255.255.255.0 up\n");
//            os.writeBytes("chmod 777 /dev/ttySAC1\n");
            os.writeBytes("exit\n");
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void initTime() {
        Calendar calendar;
        if (DialogUtil.startDate == 0 || DialogUtil.endDate == 0) {
            calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            calendar = Calendar.getInstance();
            calendar.set(year, month, day, 0, 0, 0);
            DialogUtil.startDate = calendar.getTimeInMillis();
            calendar = Calendar.getInstance();
            calendar.set(year, month, day, 24, 0, 0);
            DialogUtil.endDate = calendar.getTimeInMillis();
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        UsbUtil.changeUsbEnable(this, false);
        isSdcardExist = AppPath.initAll();
        dbManager = DBManager.getInstance(this);
        dbManager.openDatabase();
        initTime();
//        setIpWithTfiStaticIp();
    }

    //在系统内存不足，所有后台程序（优先级为background的进程，不是指后台运行的进程）都被杀死时，系统会调用OnLowMemory
//    @Override
//    public void onLowMemory() {
//        Toast.makeText(this, "onLowMemory hahaha!", Toast.LENGTH_LONG).show();
//        super.onLowMemory();
//    }
//
//    @Override
//    public void onTrimMemory(int level) {
//        switch (level) {
//            case TRIM_MEMORY_COMPLETE://内存不足，并且该进程在后台进程列表最后一个，马上就要被清理
//                Toast.makeText(this, "onTrimMemory hahaha COMPLETE!", Toast.LENGTH_LONG).show();
//                break;
//            case TRIM_MEMORY_MODERATE://内存不足，并且该进程在后台进程列表的中部。
//                Toast.makeText(this, "onTrimMemory hahaha MODERATE!", Toast.LENGTH_LONG).show();
//                break;
//            case TRIM_MEMORY_BACKGROUND://内存不足，并且该进程是后台进程。
//                Toast.makeText(this, "onTrimMemory hahaha BACKGROUND!", Toast.LENGTH_LONG).show();
//                break;
//            case TRIM_MEMORY_UI_HIDDEN://内存不足，并且该进程的UI已经不可见了。
//                Toast.makeText(this, "onTrimMemory hahaha UI_HIDDEN!", Toast.LENGTH_LONG).show();
//                break;
//            case TRIM_MEMORY_RUNNING_CRITICAL://内存不足(后台进程不足3个)，并且该进程优先级比较高，需要清理内存
//                Toast.makeText(this, "onTrimMemory hahaha RUNNING_CRITICAL!", Toast.LENGTH_LONG).show();
//                break;
//            case TRIM_MEMORY_RUNNING_LOW://内存不足(后台进程不足5个)，并且该进程优先级比较高，需要清理内存
//                Toast.makeText(this, "onTrimMemory hahaha RUNNING_LOW!", Toast.LENGTH_LONG).show();
//                break;
//            case TRIM_MEMORY_RUNNING_MODERATE://内存不足(后台进程超过5个)，并且该进程优先级比较高，需要清理内存
//                Toast.makeText(this, "onTrimMemory hahaha RUNNING_MODERATE!", Toast.LENGTH_LONG).show();
//                break;
//        }
//        super.onTrimMemory(level);
//    }
}

package com.hzncc.zhudao.utils;

import android.os.Environment;
import android.os.StatFs;

import java.io.File;

/**
 * Created by Administrator on 2017/3/31.
 */
public class RAMUtil {


    //得到外部储存sdcard的状态
    private static String sdcard = Environment.getExternalStorageState();
    //外部储存sdcard存在的情况
    private static String state = Environment.MEDIA_MOUNTED;

    /**
     * SDCard 总容量大小
     *
     * @return MB
     */
    public static long getTotalSize() {
        //获得路径
        File file = Environment.getExternalStorageDirectory();
        StatFs statFs = new StatFs(file.getPath());
        if (sdcard.equals(state)) {
            //获得sdcard上 block的总数
            long blockCount = statFs.getBlockCount();
            //获得sdcard上每个block 的大小
            long blockSize = statFs.getBlockSize();
            //计算标准大小使用：1024，当然使用1000也可以
            long bookTotalSize = blockCount * blockSize / 1000 / 1000;
            return bookTotalSize;

        } else {
            return -1;
        }

    }

    /**
     * 计算Sdcard的剩余大小
     *
     * @return MB
     */
    public static long getAvailableSize() {
        //获得路径
        File file = Environment.getExternalStorageDirectory();
        StatFs statFs = new StatFs(file.getPath());
        if (sdcard.equals(state)) {
            //获得Sdcard上每个block的size
            long blockSize = statFs.getBlockSize();
            //获取可供程序使用的Block数量
            long blockavailable = statFs.getAvailableBlocks();
            //计算标准大小使用：1024，当然使用1000也可以
            long blockavailableTotal = blockSize * blockavailable / 1000 / 1000;
            return blockavailableTotal;
        } else {
            return -1;
        }
    }
}

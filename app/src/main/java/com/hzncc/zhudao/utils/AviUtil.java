package com.hzncc.zhudao.utils;


import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * ZhuDao
 * Created by 蔡雨峰 on 2017/4/19.
 */

public class AviUtil {
    //javah  -encoding utf-8 -d ../jni com.hzncc.zhudao.utils.AviUtil
    static {
        System.loadLibrary("avilib");
    }

    private int handle;
    private boolean recording = false;
//    private List<InsertRunnable> runnables = new ArrayList<InsertRunnable>();

    public AviUtil() {
        init();
    }

    /**
     * @param filename
     * @return
     */
    public int avi_start(String filename) {
        return avi_start(filename, 320, 240, 4);
    }

    public int avi_start(String filename, int width, int height, double fps) {
        handle = start(filename, width, height, fps);
        if (handle >= 0) {
            recording = true;
        }
        return handle;
    }

    public int avi_insert(Bitmap bitmap) {
//        if(recording) {
//            InsertRunnable runnable = new InsertRunnable(bitmap);
//            new Thread(runnable).start();
//            return 0;
//        }
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return insert(handle, bitmap, bitmap.getByteCount());
    }

    public int avi_stop() {
        int result = stop(handle);
        if (result >= 0) {
            recording = false;
        }
        return result;
    }

    private native int init();

    private native int start(String filename, int width, int height, double fps);

    private native int insert(int avi_t, Object bmpObj, long length);

    private native int stop(int avi_t);

    public boolean isRecording() {
        return recording;
    }

    public void setRecording(boolean recording) {
        this.recording = recording;
    }

//    private class InsertRunnable implements Runnable {
//        private Bitmap bitmap;
//
//        public InsertRunnable(Bitmap bitmap) {
//            this.bitmap = bitmap;
//        }
//
//        @Override
//        public void run() {
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
//            insert(handle, baos.toByteArray(), bitmap.getByteCount());
//        }
//    }
}

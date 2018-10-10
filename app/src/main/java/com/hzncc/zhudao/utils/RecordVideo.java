package com.hzncc.zhudao.utils;

import android.graphics.Bitmap;
import android.util.Log;
import android.util.SparseArray;

//import org.bytedeco.javacpp.opencv_core;
//import org.bytedeco.javacv.FFmpegFrameRecorder;
//import org.bytedeco.javacv.Frame;
//import org.bytedeco.javacv.FrameRecorder;

import java.io.File;

public class RecordVideo {
    private static int FPS = 5;//帧频
    /**
     * 录像
     */
//    private opencv_core.IplImage newin;
//    private FFmpegFrameRecorder recorder;
    private int SPACE_TIME = 200;//录制间隔时间
    private boolean isRecording; // 是否正在录制
    private SparseArray<Bitmap> bitmaps;
    private int current, count;
    private Thread thread;
    private long start;

    public RecordVideo() {
        bitmaps = new SparseArray<Bitmap>();
        current = 0;
        count = 0;
        isRecording = false;
    }

    /**
     * 录制开始
     */
    public void recordingStart(int width, int height, File file) {
        start = System.currentTimeMillis();
//        String fileName = FileUtil.getDataToFileName(".avi");
//        File file = new File(path, fileName);
//        recorder = new FFmpegFrameRecorder(file, width, height);
//        long temp = System.currentTimeMillis();
//        Log.d("start_new", (temp - start) + "ms");
//        start = temp;
//        recorder.setVideoCodec(13);
//        recorder.setFormat("mp4");
//        recorder.setFrameRate(FPS);// 录像帧率
//        if (null != recorder) {
//            try {
//                recorder.start();
//            } catch (FrameRecorder.Exception e) {
//                e.printStackTrace();
//            }
//        }
//        temp = System.currentTimeMillis();
//        Log.d("start_start", (temp - start) + "ms");
//        start = temp;
//        isRecording = true;
//        thread = new Thread(new RecordingThread());
//        thread.start();
    }

    /**
     * 录像结束
     */
    public void recordingStop() {
//        isRecording = false;
//        try {
//            thread.join();
//            recorder.stop();
//            recorder.release();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (FrameRecorder.Exception e) {
//            e.printStackTrace();
//        }
    }

    public void insertData(Bitmap bitmap) {
        if (isRecording())
            bitmaps.append(count++, bitmap);
    }

    public boolean isRecording() {
        return isRecording;
    }

    public void setRecording(boolean isRecording) {
        this.isRecording = isRecording;
    }

    class RecordingThread implements Runnable {

        @Override
        public void run() {
            while (isRecording || current < count) {
//                if (current >= count) {
//                    continue;
//                }
//                Bitmap bitmap = bitmaps.get(current);
//                Frame frame = new Frame(bitmap.getWidth(), bitmap.getHeight(),
//                        opencv_core.IPL_DEPTH_8U, 4);
//                bitmap.copyPixelsToBuffer(frame.image[0]);
//                bitmaps.remove(current++);
//                bitmap.recycle();
//                bitmap = null;
////                newin = opencv_core.IplImage.create(bitmap.getWidth(), bitmap.getHeight(),
////                        opencv_core.IPL_DEPTH_8U, 4);
////                bitmap.copyPixelsToBuffer(newin.getByteBuffer());
//                try {
//                    recorder.record(frame);
////                    newin.release();
////                    newin = null;
//                } catch (FrameRecorder.Exception e) {
//                    e.printStackTrace();
//                }
            }
        }

    }

}

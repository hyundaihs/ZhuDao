package com.hzncc.zhudao.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;

import java.io.IOException;
import java.util.List;

/**
 * ZhuDao
 * Created by 蔡雨峰 on 2017/4/14.
 */

public class CameraUtil {

    private static Camera camera;
    private static boolean isPreview = false;
    private static Bitmap bitmap;

    public static Camera getCamera() {
        return camera;
    }

    public static boolean isPreview() {
        return isPreview;
    }

    public static Camera getInstance() {
        if (null == camera) {
            try {
                camera = android.hardware.Camera.open();
            } catch (Exception e) {
                Log.e("camera open failed", "camera can not open");
                e.printStackTrace();
            } finally {
                return null;
            }
        }
        return camera;
    }

    public static void startPreview(SurfaceHolder holder) {
        if (null == holder) {
            Log.e("CameraUtil", "surface is not available");
            return;
        }
        if (!isPreview) {
            try {
                camera.setPreviewDisplay(holder);
                camera.setDisplayOrientation(90);
                camera.startPreview();
                isPreview = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void setPreviewSize(int width, int height) {
        Log.d("setPreviewSize", "w = " + width + " h = " + height);
        if (null == camera || !isPreview) {
            Log.e("CameraUtil", "camera is not opened or previewed");
            return;
        }
        printSupportPreviewSize(camera.getParameters());
        printSupportPictureSize(camera.getParameters());
        printSupportFocusMode(camera.getParameters());
        Camera.Parameters parameter = camera.getParameters();
        parameter.setPreviewSize(width, height);
        parameter.setPreviewFrameRate(9);
        camera.setParameters(parameter);
    }

    public static void stopPreview() {
        //关闭、释放Camera资源
        if (null != camera) {
            if (isPreview) {
                camera.stopPreview();
                isPreview = false;
            }
        }
    }

    public static void flashOn() {
        if (null == camera || !isPreview) {
            Log.e("CameraUtil", "camera is not opened or previewed");
            return;
        }
        Camera.Parameters parameter = camera.getParameters();
        parameter.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
        camera.setParameters(parameter);
    }

    public static void flashOff() {
        if (null == camera || !isPreview) {
            Log.e("CameraUtil", "camera is not opened or previewed");
            return;
        }
        Camera.Parameters parameter = camera.getParameters();
        parameter.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
        camera.setParameters(parameter);
    }

    public static void closeCamera() {
        if (null != camera) {
            camera.release();
            camera = null;
        }
    }

    /**
     * 打印支持的previewSizes
     *
     * @param params
     */
    public static void printSupportPreviewSize(Camera.Parameters params) {
        List<Camera.Size> previewSizes = params.getSupportedPreviewSizes();
        for (int i = 0; i < previewSizes.size(); i++) {
            Camera.Size size = previewSizes.get(i);
            Log.i("CameraUtil", "previewSizes:width = " + size.width + " height = " + size.height);
        }

    }

    /**
     * 打印支持的pictureSizes
     *
     * @param params
     */
    public static void printSupportPictureSize(Camera.Parameters params) {
        List<Camera.Size> pictureSizes = params.getSupportedPictureSizes();
        for (int i = 0; i < pictureSizes.size(); i++) {
            Camera.Size size = pictureSizes.get(i);
            Log.i("CameraUtil", "pictureSizes:width = " + size.width
                    + " height = " + size.height);
        }
    }

    /**
     * 打印支持的聚焦模式
     *
     * @param params
     */
    public static void printSupportFocusMode(Camera.Parameters params) {
        List<String> focusModes = params.getSupportedFocusModes();
        for (String mode : focusModes) {
            Log.i("CameraUtil", "focusModes--" + mode);
        }
    }

    public static Bitmap takePicture() {
        bitmap = null;
        if (null == camera) {
            Log.e("CameraUtil_takePicture", "camera is null");
        }
        camera.takePicture(null, null, new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                bitmap = BitmapFactory.decodeByteArray(data, 0,
                        data.length);
            }
        });
        return bitmap;
    }

}

package com.hzncc.zhudao.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

/**
 * ZhuDao
 * Created by 蔡雨峰 on 2017/4/12.
 */

public class VlSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    private int viewWidth, viewHeight;
    private Camera camera;
    private boolean isPreview;
    private Camera.PreviewCallback callback;


    public VlSurfaceView(Context context) {
        super(context);
        init();
    }

    public VlSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Camera.PreviewCallback getCallback() {
        return callback;
    }

    public void setCallback(Camera.PreviewCallback callback) {
        this.callback = callback;
        if (null != camera) {
            camera.setPreviewCallback(callback);
        }
    }

    private void init() {
        SurfaceHolder mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        viewWidth = measureWidth(widthMeasureSpec);
        viewHeight = viewWidth * 3 / 4;
        setMeasuredDimension(viewWidth, viewHeight);
    }

    private int measureWidth(int measureSpec) {
        int preferred = 0;
        return getMeasurement(measureSpec, preferred);
    }

    private int getMeasurement(int measureSpec, int preferred) {
        int specSize = MeasureSpec.getSize(measureSpec);
        int measurement;
        switch (MeasureSpec.getMode(measureSpec)) {
            case MeasureSpec.EXACTLY:
                measurement = specSize;
                break;
            case MeasureSpec.AT_MOST:
                measurement = Math.min(preferred, specSize);
                break;
            default:
                measurement = preferred;
                break;
        }
        return measurement;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            camera = android.hardware.Camera.open();
            camera.setPreviewDisplay(holder);
            camera.setDisplayOrientation(90);
            if (null != callback) {
                camera.setPreviewCallback(callback);
            }
            camera.startPreview();
            isPreview = true;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            Log.e("camera open failed", "camera can not open");
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        //关闭、释放Camera资源
        if (null != camera) {
            if (isPreview) {
                camera.stopPreview();
            }
            camera.release();
            camera = null;
        }
    }
}

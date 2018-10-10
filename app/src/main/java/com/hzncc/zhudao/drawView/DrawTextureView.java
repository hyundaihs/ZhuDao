package com.hzncc.zhudao.drawView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.SurfaceTexture;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Surface;
import android.view.TextureView;

import com.hzncc.zhudao.R;
import com.hzncc.zhudao.utils.IRCamera;
import com.hzncc.zhudao.utils.SPUtils;
import com.hzncc.zhudao.utils.ToastUtil;

/**
 * ZhuDao
 * Created by 蔡雨峰 on 2017/3/13.
 */
public class DrawTextureView extends TextureView implements TextureView.SurfaceTextureListener {

    private ViewModel viewModel = ViewModel.IR;
    private int viewWidth, viewHeight;
    private Context context;

    public DrawTextureView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        setScaleX(1.00001f);
        setSurfaceTextureListener(this);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.DrawTextureView);
        int resourceId = typedArray.getInt(R.styleable.DrawTextureView_viewModel, 1);
        viewModel = resourceId == 0 ? ViewModel.VL : ViewModel.IR;
        typedArray.recycle();
        setSurfaceTextureListener(this);
    }

    public ViewModel getViewModel() {
        return viewModel;
    }

    public void setViewModel(ViewModel viewModel) {
        this.viewModel = viewModel;
    }

    public int getViewWidth() {
        return viewWidth;
    }

    public void setViewWidth(int viewWidth) {
        this.viewWidth = viewWidth;
    }

    public int getViewHeight() {
        return viewHeight;
    }

    public void setViewHeight(int viewHeight) {
        this.viewHeight = viewHeight;
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
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        if (viewModel == ViewModel.IR) {
            Log.d("Surface", "IR");
            IRCamera.init(null, new Surface(surface));
        } else {
            Log.d("Surface", "VL");
            IRCamera.init(new Surface(surface), null);
        }
        IRCamera.open();
        IRCamera.tempCorrent(new SPUtils(context).getTempcorrect());
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        IRCamera.close();
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }
}

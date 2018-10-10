package com.hzncc.zhudao.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;

import com.hzncc.zhudao.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * ZhuDao
 * Created by 蔡雨峰 on 2017/4/27.
 */

public class MyProgressBar extends View {

    private int viewWidth, viewHeight;
    private int index = 0;
    private int count = 3;
    private Bitmap light, dark;
    private Paint paint;
    private boolean flag = false;

    public MyProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        light = BitmapFactory.decodeResource(getResources(), R.mipmap.arrow_light);
        dark = BitmapFactory.decodeResource(getResources(), R.mipmap.arrow_dark);
        viewWidth = light.getWidth() * 4 + getPaddingLeft() + getPaddingRight();
        viewHeight = light.getHeight() + getPaddingTop() + getPaddingBottom();
        paint = new Paint();
        paint.setAntiAlias(true);
        flag = true;
        new Thread(new MyRunnable()).start();
    }

    private void next() {
        index++;
        if (index >= count) {
            index = 0;
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        flag = false;
        super.onDetachedFromWindow();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int bitmapW = light.getWidth();
        for (int i = 0; i < count; i++) {
            Bitmap bitmap = index == i ? dark : light;
            canvas.drawBitmap(bitmap, getPaddingLeft() + (bitmapW + bitmapW / 2) * i, getPaddingTop(), paint);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(viewWidth, viewHeight);
    }

    private class MyRunnable implements Runnable {

        @Override
        public void run() {
            while (flag) {
                next();
                postInvalidate();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

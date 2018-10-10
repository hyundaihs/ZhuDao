package com.hzncc.zhudao.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.hzncc.zhudao.utils.SizeUtil;

/**
 * ZhuDao
 * Created by 蔡雨峰 on 2017/3/14.
 */
public class BatteryView extends View {

    private int mPower = 0;

    private int battery_left = 0;
    private int battery_top = 0;
    private int battery_width = 25;
    private int battery_height = 15;

    private int battery_head_width = 3;
    private int battery_head_height = 3;

    private int battery_inside_margin = 3;

    private int textSize = 12;

    private float text_x = 0;
    private float text_y = 0;
    private Context context;

    public BatteryView(Context context) {
        super(context);
        this.context = context;
//        initSize(context);
    }

    public BatteryView(Context context, AttributeSet attrs) {
        super(context, attrs);
//        initSize(context);
        this.context = context;
    }

    private void initSize(Context context) {
        battery_width = SizeUtil.dp2px(context, 25);
        battery_height = SizeUtil.dp2px(context, 15);
        battery_head_width = SizeUtil.dp2px(context, 3);
        battery_head_height = SizeUtil.dp2px(context, 3);
        battery_inside_margin = SizeUtil.dp2px(context, 3);
        textSize = SizeUtil.sp2px(context, 12);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //先画外框
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);

        Rect rect = new Rect(battery_left, battery_top,
                battery_left + battery_width, battery_top + battery_height);
        canvas.drawRect(rect, paint);

        float power_percent = mPower / 100.0f;

        Paint paint2 = new Paint(paint);
        paint2.setStyle(Paint.Style.FILL);
        if (mPower <= 25) {
            paint2.setColor(Color.RED);
        } else if (mPower <= 75) {
            paint2.setColor(Color.GREEN);
        } else {
            paint2.setColor(Color.WHITE);
        }
        if (power_percent <= 0.01f) {
            power_percent = 0.01f;
        }
        //画电量
        if (power_percent != 0) {
            int p_left = battery_left + battery_inside_margin;
            int p_top = battery_top + battery_inside_margin;
            int p_right = p_left - battery_inside_margin + (int) ((battery_width - battery_inside_margin) * power_percent);
            int p_bottom = p_top + battery_height - battery_inside_margin * 2;
            Rect rect2 = new Rect(p_left, p_top, p_right, p_bottom);
            canvas.drawRect(rect2, paint2);
        }
        paint2.setColor(Color.WHITE);
        //画电池头
        int h_left = battery_left + battery_width;
        int h_top = battery_top + battery_height / 2 - battery_head_height / 2;
        int h_right = h_left + battery_head_width;
        int h_bottom = h_top + battery_head_height;
        Rect rect3 = new Rect(h_left, h_top, h_right, h_bottom);
        canvas.drawRect(rect3, paint2);

        paint2.setTextSize(textSize);
        String str = mPower + "%";
        canvas.drawText(str, text_x, text_y, paint2);
    }

    public void setPower(int power) {
        mPower = power;
        if (mPower < 0) {
            mPower = 0;
        }
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int viewWidth = measureWidth(widthMeasureSpec);
        int viewHeight = measureHeight(heightMeasureSpec);
        if (viewWidth == 0 || viewHeight == 0) {
            viewWidth = SizeUtil.dp2px(context, 80);
            viewHeight = SizeUtil.dp2px(context, 20);
            initData(viewWidth, viewHeight);
        }
        setMeasuredDimension(viewWidth, viewHeight);

    }

    private void initData(int viewWidth, int viewHeight) {
        Paint textPaint = new Paint();
        textPaint.setTextSize(textSize);

        battery_width = (viewWidth / 2 - battery_head_width) * 2 / 3;
        battery_height = (viewHeight) * 2 / 3;

        battery_left = (viewWidth / 2 - battery_width) / 2;
        battery_top = (viewHeight - battery_height) / 2;
        Rect textRect = new Rect();
        String str = "100%";
        textPaint.getTextBounds(str, 0, str.length(), textRect);

        Rect rect = new Rect(viewWidth / 2, (viewHeight - textRect.height()) / 2,
                viewWidth, viewHeight - (viewHeight - textRect.height()) / 2);

        Paint.FontMetricsInt fontMetrics = textPaint.getFontMetricsInt();
        int baseline = (rect.bottom + rect.top - fontMetrics.bottom - fontMetrics.top) / 2;
        //text_x = viewWidth / 2 + (viewWidth / 2 - textRect.width()) / 2;
        text_x = viewWidth / 2;
        text_y = baseline;

        textSize = SizeUtil.sp2px(context, 12);
    }

    private int measureWidth(int measureSpec) {
        int preferred = 0;
        return getMeasurement(measureSpec, preferred);
    }

    private int measureHeight(int measureSpec) {
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
}

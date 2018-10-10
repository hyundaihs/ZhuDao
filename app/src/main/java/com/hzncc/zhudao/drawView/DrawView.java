package com.hzncc.zhudao.drawView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.hzncc.zhudao.R;
import com.hzncc.zhudao.utils.IRCamera;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * ZhuDao
 * Created by 蔡雨峰 on 2017/4/13.
 */

public class DrawView extends View {
    private static final int checkColor = Color.YELLOW;
    private static final int cleanCheckColor = Color.GRAY;
    private static final int[] colors = {R.color.circle_temp_red_bg, R.color.circle_temp_green_bg,
            R.color.circle_temp_blue_bg};
    public static double rate;
    /**
     * 存放绘制矩形的集合
     */
    public static ArrayList<DrawRect> rects = new ArrayList<DrawRect>();
    /**
     * 存放绘制点的集合
     */
    public static ArrayList<Point> points = new ArrayList<Point>();
    /**
     * 存放取消的集合
     */
    public static ArrayList<Object> cleans = new ArrayList<Object>();
    public static float cenTemp = 0f;
    private static int normalColor = Color.WHITE;
    public boolean isDrawCenterTemp = false;
    private ViewModel viewModel = ViewModel.IR;
    private int viewWidth, viewHeight;
    private DrawType drawType = DrawType.RECTANGLE;
    private int shapeNum = 3;
    private int nearSize = 20;
    private Object handObject;
    private OnDrawChangeListener onDrawChangeListener;
    private float down_x, down_y;
    private int index = 0;//当前获取的矩形下标
    private boolean flag = false;
    private Timer timer;
    private RefreshTask refreshTask;
    private View vlView;
    private boolean isDraging = false;
    private Handler handle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            refresh();
        }
    };

    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.DrawView);
        int resourceId = typedArray.getInt(R.styleable.DrawView_viewType, 1);
        viewModel = resourceId == 0 ? ViewModel.VL : ViewModel.IR;
        resourceId = typedArray.getInt(R.styleable.DrawView_drawType, 2);
        drawType = resourceId == 0 ? DrawType.CLEAN : (resourceId == 1 ? DrawType.POINT : DrawType.RECTANGLE);
        shapeNum = typedArray.getInt(R.styleable.DrawView_shapeNum, 3);
        nearSize = typedArray.getInt(R.styleable.DrawView_nearSize, 20);
        typedArray.recycle();
        if (viewModel == ViewModel.IR) {
            startTimer();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        Log.e("DrawView", "onDetachedFromWindow");
        stopTimer();
        super.onDetachedFromWindow();
    }

    private void startTimer() {
        if (null == timer) {
            timer = new Timer();
        }
        if (null == refreshTask) {
            refreshTask = new RefreshTask();
        }
        timer.schedule(refreshTask, 1000, 1000);
    }

    private void stopTimer() {
        if (null != timer) {
            timer.cancel();
            timer = null;
        }
        if (null != refreshTask) {
            refreshTask = null;
        }
    }

    public ViewModel getViewModel() {
        return viewModel;
    }

    public void setViewModel(ViewModel viewModel) {
        this.viewModel = viewModel;
    }

    public View getVlView() {
        return vlView;
    }

    public void setVlView(View vlView) {
        this.vlView = vlView;
    }

    private void refresh() {
        cenTemp = IRCamera.getCentemp();
        for (int i = 0; i < rects.size(); i++) {
            IRCamera.getObjectTemp(rects.get(i));
//            invalidateByArea(i);
        }
        if (!isDraging) {
            invalidate();
            if (null != vlView) {
                vlView.invalidate();
            }
        }
        if (null != onDrawChangeListener) {
            onDrawChangeListener.onDataChange();
        }
    }

    public DrawType getDrawType() {
        return drawType;
    }

    public void setDrawType(DrawType drawType) {
        this.drawType = drawType;
    }

    public int getShapeNum() {
        return shapeNum;
    }

    public void setShapeNum(int shapeNum) {
        this.shapeNum = shapeNum;
    }

    public int getNearSize() {
        return nearSize;
    }

    public void setNearSize(int nearSize) {
        this.nearSize = nearSize;
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

    public OnDrawChangeListener getOnDrawChangeListener() {
        return onDrawChangeListener;
    }

    public void setOnDrawChangeListener(OnDrawChangeListener onDrawChangeListener) {
        this.onDrawChangeListener = onDrawChangeListener;
    }

    public boolean isDrawCenterTemp() {
        return isDrawCenterTemp;
    }

    public void setDrawCenterTemp(boolean drawCenterTemp) {
        isDrawCenterTemp = drawCenterTemp;
    }

    public void clean() {
        points.clear();
        rects.clear();
        cleans.clear();
    }

    public void clean(int index) {
        if (index < rects.size()) {
            IRCamera.deleteObject(rects.get(index).handle);
            rects.remove(index);
        }
//        invalidate();
//        if (null != onDrawChangeListener) {
//            onDrawChangeListener.onDrawChange(this);
//        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawShapeBold(canvas);
        drawTemp(canvas);
        if (isDrawCenterTemp) {
            drawCentemp(canvas);
        }
        drawMaxTemp(canvas);
    }

    public void invalidateByArea(DrawRect drawRect) {
        isDraging = true;
        invalidate(drawRect.left, drawRect.top, drawRect.right, drawRect.bottom);
        isDraging = false;
    }

    public void invalidateByArea(int index) {
        invalidateByArea(rects.get(index));
    }

    public void drawShape(Canvas canvas) {
        if (points.size() <= 0 && rects.size() <= 0) {
            return;
        }
        Paint paint = new Paint();
        canvas.saveLayer(0f, 0f, (float) viewWidth, (float) viewHeight, paint, Canvas.ALL_SAVE_FLAG);

        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.argb(125, 0, 0, 0));
        canvas.drawRect(0, 0, viewWidth, viewHeight, paint);

        PorterDuffXfermode avoid = new PorterDuffXfermode(PorterDuff.Mode.DST_OUT);
        paint.setXfermode(avoid);

        normalColor = Color.WHITE;
        if (drawType == DrawType.POINT) {
            for (int i = 0; i < points.size(); i++) {
                Point point = points.get(i);
                canvas.drawCircle(point.x, point.y, 20f, paint);
            }
        } else if (drawType == DrawType.RECTANGLE) {
            for (int i = 0; i < rects.size(); i++) {
                DrawRect drawRect = rects.get(i);
                drawRect.drawRect(canvas, normalColor);
            }
        } else {
            for (int i = 0; i < points.size(); i++) {
                Point point = points.get(i);
                if (cleans.contains(point)) {
                    paint.setColor(cleanCheckColor);
                    canvas.drawCircle(point.x, point.y, nearSize, paint);
                } else {
                    paint.setColor(normalColor);
                    canvas.drawCircle(point.x, point.y, nearSize, paint);
                }
            }
            for (int i = 0; i < rects.size(); i++) {
                DrawRect drawRect = rects.get(i);
                if (cleans.contains(drawRect)) {
                    drawRect.drawRect(canvas, cleanCheckColor);
                } else {
                    drawRect.drawRect(canvas, normalColor);
                }
            }
        }
        canvas.restore();
    }

    public void drawShapeBold(Canvas canvas) {
        if (points.size() <= 0 && rects.size() <= 0) {
            return;
        }
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        if (drawType == DrawType.POINT) {
            for (int i = 0; i < points.size(); i++) {
                normalColor = getResources().getColor(colors[i]);
                paint.setColor(normalColor);
                Point point = points.get(i);
                canvas.drawCircle(point.x, point.y, 20f, paint);
            }
        } else if (drawType == DrawType.RECTANGLE) {
            for (int i = 0; i < rects.size(); i++) {
                normalColor = getResources().getColor(colors[i]);
                DrawRect drawRect = rects.get(i);
                drawRect.drawRect(canvas, drawRect.moveL ? checkColor : normalColor,
                        drawRect.moveT ? checkColor : normalColor,
                        drawRect.moveR ? checkColor : normalColor,
                        drawRect.moveB ? checkColor : normalColor);
            }
        } else {
            for (int i = 0; i < points.size(); i++) {
                normalColor = getResources().getColor(colors[i]);
                Point point = points.get(i);
                if (cleans.contains(point)) {
                    paint.setColor(cleanCheckColor);
                    canvas.drawCircle(point.x, point.y, nearSize, paint);
                } else {
                    paint.setColor(normalColor);
                    canvas.drawCircle(point.x, point.y, nearSize, paint);
                }
            }
            for (int i = 0; i < rects.size(); i++) {
                normalColor = getResources().getColor(colors[i]);
                DrawRect drawRect = rects.get(i);
                if (cleans.contains(drawRect)) {
                    drawRect.drawRect(canvas, cleanCheckColor);
                } else {
                    drawRect.drawRect(canvas, normalColor);
                }
            }
        }
    }

    public void drawMaxTemp(Canvas canvas) {
        DrawRect drawRect = new DrawRect(new Point(0, 0), new Point(viewWidth, viewHeight));
        if (drawRect.handle == -1) {
            drawRect.handle = IRCamera.addObject(drawRect, rate);
        } else {
            IRCamera.updateObject(drawRect, rate);
        }
        drawRect.drawMax(canvas, normalColor);
    }

    public void drawTemp(Canvas canvas) {
        if (points.size() <= 0 && rects.size() <= 0) {
            return;
        }
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        if (drawType == DrawType.POINT) {
            for (int i = 0; i < points.size(); i++) {
                normalColor = getResources().getColor(colors[i]);
                paint.setColor(normalColor);
                Point point = points.get(i);
                canvas.drawCircle(point.x, point.y, 20f, paint);
            }
        } else if (drawType == DrawType.RECTANGLE) {
            for (int i = 0; i < rects.size(); i++) {
                normalColor = getResources().getColor(colors[i]);
                DrawRect drawRect = rects.get(i);
                if (drawRect.handle == -1) {
                    return;
                }
                drawRect.drawMax(canvas, normalColor);
            }
        } else {
            for (int i = 0; i < points.size(); i++) {
                normalColor = getResources().getColor(colors[i]);
                Point point = points.get(i);
                if (cleans.contains(point)) {
                    paint.setColor(cleanCheckColor);
                    canvas.drawCircle(point.x, point.y, nearSize, paint);
                } else {
                    paint.setColor(normalColor);
                    canvas.drawCircle(point.x, point.y, nearSize, paint);
                }
            }
            for (int i = 0; i < rects.size(); i++) {
                normalColor = getResources().getColor(colors[i]);
                DrawRect drawRect = rects.get(i);
                if (cleans.contains(drawRect)) {
                    drawRect.drawRect(canvas, cleanCheckColor);
                } else {
                    drawRect.drawRect(canvas, normalColor);
                }
            }
        }
    }

    public void drawCentemp(Canvas canvas) {
        Paint blackPaint = new Paint();
        Paint whitePaint = new Paint();
        blackPaint.setTextAlign(Paint.Align.CENTER);
        whitePaint.setTextAlign(Paint.Align.CENTER);

        blackPaint.setStrokeWidth(2);
        blackPaint.setStyle(Paint.Style.STROKE);
        blackPaint.setFakeBoldText(true);
        blackPaint.setColor(Color.BLACK);

        whitePaint.setStyle(Paint.Style.FILL);
        whitePaint.setStrokeWidth(1);
        whitePaint.setColor(Color.WHITE);
        blackPaint.setTextSize(14);
        whitePaint.setTextSize(14);
        String text = String.valueOf(cenTemp);
        int x = viewWidth / 2;
        int y = viewHeight / 2;
        canvas.drawLine(x - 10, y, x + 10, y, blackPaint);
        canvas.drawLine(x, y - 10, x, y + 10, blackPaint);
        canvas.drawLine(x - 10, y, x + 10, y, whitePaint);
        canvas.drawLine(x, y - 10, x, y + 10, whitePaint);
        Rect rect = new Rect();
        blackPaint.getTextBounds(text, 0, text.length(), rect);
        float text_x, text_y;
        text_x = x + 5;
        text_y = y - 5 - rect.height();
        canvas.drawText(text, text_x, text_y, blackPaint);
        canvas.drawText(text, text_x, text_y, whitePaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                return actionDown(event);
            case MotionEvent.ACTION_MOVE:
                return actionMove(event);
            case MotionEvent.ACTION_UP:
                return actionUp(event);
        }
        return super.onTouchEvent(event);
    }

    private boolean actionDown(MotionEvent event) {
        down_x = event.getX();
        down_y = event.getY();
        handObject = getHandObject(event);
        if (null == handObject && drawType != DrawType.CLEAN) {
            return super.onTouchEvent(event);
        }
        if (handObject instanceof DrawRect) {
            invalidateByArea((DrawRect) handObject);
        }
//        if (null != onDrawChangeListener) {
//            onDrawChangeListener.onDrawChange(this);
//        }
        return true;
    }

    private Object getHandObject(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        if (drawType == DrawType.POINT) {
            for (int i = 0; i < points.size(); i++) {
                Point point = points.get(i);
                if (point.isNearPoint(x, y)) {
                    return point;
                }
            }
            if (points.size() >= shapeNum) {
                return null;
            }
            Point point = new Point();
            point.x = (short) x;
            point.y = (short) y;
            points.add(point);
            return points.get(points.size() - 1);
        } else if (drawType == DrawType.RECTANGLE) {
            for (int i = 0; i < rects.size(); i++) {
                DrawRect drawRect = rects.get(i);
                Point handPoint = new Point((int) x, (int) y);
                drawRect.moveL = false;
                drawRect.moveT = false;
                drawRect.moveR = false;
                drawRect.moveB = false;
                if (handPoint.isNearPoint(drawRect.left, drawRect.top)) {
                    drawRect.moveL = true;
                    drawRect.moveT = true;
                } else if (handPoint.isNearPoint(drawRect.right, drawRect.top)) {
                    drawRect.moveT = true;
                    drawRect.moveR = true;
                } else if (handPoint.isNearPoint(drawRect.right, drawRect.bottom)) {
                    drawRect.moveR = true;
                    drawRect.moveB = true;
                } else if (handPoint.isNearPoint(drawRect.left, drawRect.bottom)) {
                    drawRect.moveL = true;
                    drawRect.moveB = true;
                } else if (Math.abs(x - drawRect.left) <= nearSize
                        && Math.abs(y - drawRect.top) + Math.abs(y - drawRect.bottom)
                        <= Math.abs(drawRect.top - drawRect.bottom)) {
                    drawRect.moveL = true;
                } else if (Math.abs(y - drawRect.top) <= nearSize
                        && Math.abs(x - drawRect.left) + Math.abs(x - drawRect.right)
                        <= Math.abs(drawRect.left - drawRect.right)) {
                    drawRect.moveT = true;
                } else if (Math.abs(x - drawRect.right) <= nearSize
                        && Math.abs(y - drawRect.top) + Math.abs(y - drawRect.bottom)
                        <= Math.abs(drawRect.top - drawRect.bottom)) {
                    drawRect.moveR = true;
                } else if (Math.abs(y - drawRect.bottom) <= nearSize
                        && Math.abs(x - drawRect.left) + Math.abs(x - drawRect.right)
                        <= Math.abs(drawRect.left - drawRect.right)) {
                    drawRect.moveB = true;
                } else {
                    continue;
                }
                return drawRect;
            }
            if (rects.size() >= shapeNum) {
                return null;
            }
            DrawRect drawRect = new DrawRect();
            drawRect.create((int) x, (int) y);
            drawRect.moveL = false;
            drawRect.moveT = false;
            drawRect.moveR = true;
            drawRect.moveB = true;
//            drawRect.rightBorder = viewWidth;
//            drawRect.bottomBorder = viewHeight;
            rects.add(drawRect);
            return rects.get(rects.size() - 1);
        } else {
            return null;
        }
    }

    private boolean actionMove(MotionEvent event) {
        if (drawType == DrawType.POINT) {
            Point point = (Point) handObject;
            point.x = (short) event.getX();
            point.y = (short) event.getY();
        } else if (drawType == DrawType.RECTANGLE) {
            DrawRect drawRect = (DrawRect) handObject;
            float off_x = (event.getX() - down_x);
            float off_y = (event.getY() - down_y);
            drawRect.offSet(off_x, off_y);
            down_x = (int) event.getX();
            down_y = (int) event.getY();
            invalidateByArea(drawRect);
        } else {
            for (int i = 0; i < points.size(); i++) {
                Point point = points.get(i);
                if (cleans.contains(point)) {
                    continue;
                }
                if (point.isNearPoint(event.getX(), event.getY())) {
                    cleans.add(point);
                }
            }
            for (int i = 0; i < rects.size(); i++) {
                DrawRect drawRect = rects.get(i);
                if (cleans.contains(drawRect)) {
                    continue;
                }
                if (drawRect.contains((int) event.getX(), (int) event.getY())) {
                    cleans.add(drawRect);
                    invalidateByArea(drawRect);
                }
            }
        }
//        if (null != onDrawChangeListener) {
//            onDrawChangeListener.onDrawChange(this);
//        }

        return true;
    }

    private boolean actionUp(MotionEvent event) {
        if (drawType == DrawType.POINT) {
            Point point = (Point) handObject;
            point.x = (short) event.getX();
            point.y = (short) event.getY();
        } else if (drawType == DrawType.RECTANGLE) {
            DrawRect drawRect = (DrawRect) handObject;
            if (Math.abs(drawRect.right - drawRect.left) <= nearSize
                    && Math.abs(drawRect.bottom - drawRect.top) <= nearSize) {
                rects.remove(handObject);
            } else {
                drawRect.moveL = false;
                drawRect.moveT = false;
                drawRect.moveR = false;
                drawRect.moveB = false;
                drawRect.name = "rect" + rects.size();
                if (drawRect.handle == -1) {
                    drawRect.handle = IRCamera.addObject(drawRect, rate);
                } else {
                    IRCamera.updateObject(drawRect, rate);
                }
//                IRCamera.getObjectTemp(drawRect);
//                if (null != onDrawChangeListener) {
//                    onDrawChangeListener.onDataChange();
//                }
            }
            invalidateByArea(drawRect);
        } else {
            for (int i = 0; i < cleans.size(); i++) {
                Object object = cleans.get(i);
                if (object.getClass() == Point.class) {
                    points.remove(object);
                } else {
                    DrawRect drawRect = (DrawRect) object;
                    IRCamera.deleteObject(drawRect.handle);
                    rects.remove(drawRect);
                    invalidateByArea(drawRect);
                }
            }
            cleans.clear();
        }
//        if (null != onDrawChangeListener) {
//            onDrawChangeListener.onDrawChange(this);
//        }
        return true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        viewWidth = measureWidth(widthMeasureSpec);
        viewHeight = viewWidth * 3 / 4;
        rate = viewWidth / 80;
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

    public interface OnDrawChangeListener {
        void onDrawChange(View view);

        void onDataChange();
    }

//    @Override
//    protected void onFinishInflate() {
//        timer.cancel();
//        super.onFinishInflate();
//    }

    private class RefreshTask extends TimerTask {
        @Override
        public void run() {
            handle.sendEmptyMessage(0);
        }
    }

}
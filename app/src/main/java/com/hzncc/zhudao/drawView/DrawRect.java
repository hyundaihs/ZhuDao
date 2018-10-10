package com.hzncc.zhudao.drawView;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.hzncc.zhudao.utils.IRCamera;

/**
 * ZhuDao
 * Created by 蔡雨峰 on 2017/4/13.
 */

public class DrawRect {
    //    public int rightBorder, bottomBorder;
    public boolean moveL, moveT, moveR, moveB;
    public int left, top, right, bottom;
    public int handle = -1;
    public String name = "";
    private Point RectS, RectE, mPtCer;
    private short mMaxAD;
    private short mMinAD;
    private short mCerAD;
    private short mAvgAD;
    private Point mPtMax;
    private Point mPtMin;
    private float mAvgTemp;
    private float mMaxTemp;
    private float mMinTemp;
    private float mCerTemp;

    private int mEmiss;
    private String mDesp = "";
    private Paint blackPaint, whitePaint;

    public DrawRect() {
        init();
    }

    public DrawRect(Point s, Point e) {
        init();
        set(s, e);
    }

    private void init() {
        RectS = new Point(0, 0);
        RectE = new Point(0, 0);
        mPtCer = new Point(0, 0);
        mPtMax = new Point(0, 0);
        mPtMin = new Point(0, 0);
    }

    public void create(int x, int y) {
        left = x;
        top = y;
        right = x;
        bottom = y;
        measureDrawPoints();
    }

    public void set(Point s, Point e) {
        left = s.x > e.x ? e.x : s.x;
        top = s.y > e.y ? e.y : s.y;
        right = s.x > e.x ? s.x : e.x;
        bottom = s.y > e.y ? s.y : e.y;
        measureDrawPoints();
    }

    public void offSet(float off_x, float off_y) {
        if (moveL) {
            left += off_x;
        }
        if (moveT) {
            top += off_y;
        }
        if (moveR) {
            right += off_x;
        }
        if (moveB) {
            bottom += off_y;
        }
        measureDrawPoints();
    }

    public void measureDrawPoints() {
        if (left > right) {
            int temp = left;
            left = right;
            right = temp;
        }
        if (top > bottom) {
            int temp = top;
            top = bottom;
            bottom = temp;
        }
        RectS.set(left, top);
        RectE.set(right, bottom);
    }

    public void drawRect(Canvas canvas, int color) {
        drawRect(canvas, color, color, color, color);
    }

    public void drawRect(Canvas canvas, int leftColor, int topColor, int rightColor, int bottomColor) {
        Paint paint = new Paint();
        paint.setColor(leftColor);
        canvas.drawLine(left, top, left, bottom, paint);
        paint.setColor(topColor);
        canvas.drawLine(left, top, right, top, paint);
        paint.setColor(rightColor);
        canvas.drawLine(right, top, right, bottom, paint);
        paint.setColor(bottomColor);
        canvas.drawLine(left, bottom, right, bottom, paint);
    }

    public void drawMax(Canvas canvas, int color) {
        drawPoints(canvas, color, mPtMax, mMaxTemp);
    }

    private void drawPoints(Canvas canvas, int color, Point point, float temp) {
        blackPaint = new Paint();
        whitePaint = new Paint();
        blackPaint.setTextAlign(Paint.Align.CENTER);
        whitePaint.setTextAlign(Paint.Align.CENTER);

        blackPaint.setStrokeWidth(2);
        blackPaint.setStyle(Paint.Style.STROKE);
        blackPaint.setFakeBoldText(true);
        blackPaint.setColor(color);

        whitePaint.setStyle(Paint.Style.FILL);
        whitePaint.setStrokeWidth(1);
        whitePaint.setColor(Color.WHITE);
        blackPaint.setTextSize(14);
        whitePaint.setTextSize(14);
        String text = String.valueOf(temp);
        int x = (int) (mPtMax.x * DrawView.rate);
        int y = (int) (mPtMax.y * DrawView.rate);
        if (x < left) {
            x = left;
        }
        if (x > right) {
            x = right;
        }
        if (y < top) {
            y = top;
        }
        if (y > bottom) {
            y = bottom;
        }
        canvas.drawLine(x - 10, y, x + 10, y, blackPaint);
        canvas.drawLine(x, y - 10, x, y + 10, blackPaint);
        canvas.drawLine(x - 10, y, x + 10, y, whitePaint);
        canvas.drawLine(x, y - 10, x, y + 10, whitePaint);
        Rect rect = new Rect();
        blackPaint.getTextBounds(text, 0, text.length(), rect);
        float text_x, text_y;
        text_x = left + 5 + rect.height();
        text_y = top + 5 + rect.height();
        canvas.drawText(text, text_x, text_y, blackPaint);
        canvas.drawText(text, text_x, text_y, whitePaint);

    }

    public boolean contains(int x, int y) {
        return x >= (left - Point.nearSize) && y >= (top - Point.nearSize)
                && x <= (right + Point.nearSize) && y <= (bottom + Point.nearSize);
    }

    public boolean isMoveL() {
        return moveL;
    }

    public void setMoveL(boolean moveL) {
        this.moveL = moveL;
    }

    public boolean isMoveT() {
        return moveT;
    }

    public void setMoveT(boolean moveT) {
        this.moveT = moveT;
    }

    public boolean isMoveR() {
        return moveR;
    }

    public void setMoveR(boolean moveR) {
        this.moveR = moveR;
    }

    public boolean isMoveB() {
        return moveB;
    }

    public void setMoveB(boolean moveB) {
        this.moveB = moveB;
    }

    public int getLeft() {
        return left;
    }

    public void setLeft(int left) {
        this.left = left;
    }

    public int getTop() {
        return top;
    }

    public void setTop(int top) {
        this.top = top;
    }

    public int getRight() {
        return right;
    }

    public void setRight(int right) {
        this.right = right;
    }

    public int getBottom() {
        return bottom;
    }

    public void setBottom(int bottom) {
        this.bottom = bottom;
    }

    public int getHandle() {
        return handle;
    }

    public void setHandle(int handle) {
        this.handle = handle;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Point getRectS() {
        return RectS;
    }

    public void setRectS(Point rectS) {
        RectS = rectS;
    }

    public Point getRectE() {
        return RectE;
    }

    public void setRectE(Point rectE) {
        RectE = rectE;
    }

    public Point getmPtCer() {
        return mPtCer;
    }

    public void setmPtCer(Point mPtCer) {
        this.mPtCer = mPtCer;
    }

    public short getmMaxAD() {
        return mMaxAD;
    }

    public void setmMaxAD(short mMaxAD) {
        this.mMaxAD = mMaxAD;
    }

    public short getmMinAD() {
        return mMinAD;
    }

    public void setmMinAD(short mMinAD) {
        this.mMinAD = mMinAD;
    }

    public short getmCerAD() {
        return mCerAD;
    }

    public void setmCerAD(short mCerAD) {
        this.mCerAD = mCerAD;
    }

    public short getmAvgAD() {
        return mAvgAD;
    }

    public void setmAvgAD(short mAvgAD) {
        this.mAvgAD = mAvgAD;
    }

    public Point getmPtMax() {
        return mPtMax;
    }

    public void setmPtMax(Point mPtMax) {
        this.mPtMax = mPtMax;
    }

    public Point getmPtMin() {
        return mPtMin;
    }

    public void setmPtMin(Point mPtMin) {
        this.mPtMin = mPtMin;
    }

    public float getmAvgTemp() {
        return mAvgTemp;
    }

    public void setmAvgTemp(float mAvgTemp) {
        this.mAvgTemp = mAvgTemp;
    }

    public float getmMaxTemp() {
        return mMaxTemp;
    }

    public void setmMaxTemp(float mMaxTemp) {
        this.mMaxTemp = mMaxTemp;
    }

    public float getmMinTemp() {
        return mMinTemp;
    }

    public void setmMinTemp(float mMinTemp) {
        this.mMinTemp = mMinTemp;
    }

    public float getmCerTemp() {
        return mCerTemp;
    }

    public void setmCerTemp(float mCerTemp) {
        this.mCerTemp = mCerTemp;
    }

    public int getmEmiss() {
        return mEmiss;
    }

    public void setmEmiss(int mEmiss) {
        this.mEmiss = mEmiss;
    }

    public String getmDesp() {
        return mDesp;
    }

    public void setmDesp(String mDesp) {
        this.mDesp = mDesp;
    }

    public Paint getBlackPaint() {
        return blackPaint;
    }

    public void setBlackPaint(Paint blackPaint) {
        this.blackPaint = blackPaint;
    }

    public Paint getWhitePaint() {
        return whitePaint;
    }

    public void setWhitePaint(Paint whitePaint) {
        this.whitePaint = whitePaint;
    }

    @Override
    public String toString() {
        return "DrawRect{" +
                "moveL=" + moveL +
                ", moveT=" + moveT +
                ", moveR=" + moveR +
                ", moveB=" + moveB +
                ", left=" + left +
                ", top=" + top +
                ", right=" + right +
                ", bottom=" + bottom +
                ", handle=" + handle +
                ", name='" + name + '\'' +
                ", RectS=" + RectS +
                ", RectE=" + RectE +
                ", mPtCer=" + mPtCer +
                ", mMaxAD=" + mMaxAD +
                ", mMinAD=" + mMinAD +
                ", mCerAD=" + mCerAD +
                ", mAvgAD=" + mAvgAD +
                ", mPtMax=" + mPtMax +
                ", mPtMin=" + mPtMin +
                ", mAvgTemp=" + mAvgTemp +
                ", mMaxTemp=" + mMaxTemp +
                ", mMinTemp=" + mMinTemp +
                ", mCerTemp=" + mCerTemp +
                ", mEmiss=" + mEmiss +
                ", mDesp='" + mDesp + '\'' +
                ", blackPaint=" + blackPaint +
                ", whitePaint=" + whitePaint +
                '}';
    }
}

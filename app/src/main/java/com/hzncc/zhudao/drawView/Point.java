package com.hzncc.zhudao.drawView;

/**
 * ZhuDao
 * Created by 蔡雨峰 on 2017/4/13.
 */

public class Point {
    public short x, y;
    public static final int nearSize = 20;

    public Point() {
        super();
    }

    public Point(int x, int y) {
        set(x,y);
    }

    public boolean isNearPoint(float x, float y) {
        return Math.abs(this.x - x) <= nearSize && Math.abs(this.y - y) <= nearSize;
    }

    public boolean isNearPoint(Point point) {
        return isNearPoint(this.x, this.y);
    }

    public void set(int x,int y){
        this.x = (short) x;
        this.y = (short) y;
    }
}

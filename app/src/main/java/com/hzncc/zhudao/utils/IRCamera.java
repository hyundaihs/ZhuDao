package com.hzncc.zhudao.utils;

import android.util.Log;
import android.view.Surface;

import com.hzncc.zhudao.drawView.DrawRect;
import com.hzncc.zhudao.drawView.Point;
import com.hzsk.andriod.ir.sdk.api;

/**
 * ZhuDao
 * Created by 蔡雨峰 on 2017/4/14.
 */

public class IRCamera {

    private static Surface vlSur, irSur;

    public static void init(Surface vl, Surface ir) {
        if (null != vl) {
            api.setSurface(vl);
            vlSur = vl;
        }
        if (null != ir) {
            api.setSurfaceWin(ir);
            irSur = ir;
        }
    }

    public static void open() {
        if (vlSur == null || irSur == null) {
            Log.e("IrCamera", "surface is not all ready");
            return;
        }
        api.LEPInit();
        int ret = api.LEPOpenDev("192.168.1.100", 2);
        if (ret == 0) {
            Log.d("IRCamera", "LEPDevStart");
            ret = api.LEPDevStart(2);
        } else {
            Log.e("Demo", "open error");
        }
    }

    public static void close() {
        vlSur = null;
        irSur = null;
        Log.d("IRCamera", "close");
        api.LEPCloseDev(2);
    }

    public static void reopen() {
        api.LEPReConnect(2);
    }

    public static void setColorsName(int colorName) {
        api.LEPSetPalette(2, colorName);
    }

    public static int getObjectTemp(DrawRect rect) {
        if (rect.handle == -1)
            return -1;
        return api.LEPGetObjectTemp(rect.handle, rect, Point.class.getName().replace(".", "/"));
    }

    public static int addObject(DrawRect drawRect, double rate) {
        double xs = drawRect.left / rate;
        double yx = drawRect.top / rate;
        double xe = drawRect.right / rate;
        double ye = drawRect.bottom / rate;
        return api.LEPAddObject(1, (int) xs, (int) yx, (int) xe, (int) ye, drawRect.name, 98);
    }

    public static int deleteObject(int handle) {
        return api.LEPDeleteObject(handle);
    }

    public static int deleteAllObject() {
        return api.LEPDeleteAllObject();
    }

    public static int[] getObjectHandle(int count) {
        int[] handles = new int[count];
        api.LEPGetObjectHandle(handles);
        return handles;
    }

    public static int updateObject(DrawRect drawRect, double rate) {
        double xs = drawRect.left / rate;
        double yx = drawRect.top / rate;
        double xe = drawRect.right / rate;
        double ye = drawRect.bottom / rate;
        return api.LEPUpdateObject(drawRect.handle, (int) xs, (int) yx, (int) xe,
                (int) ye, drawRect.name, 98);
    }

    public static void flashOn() {
        api.LEPLed(2, 1);
    }

    public static void flashOff() {
        api.LEPLed(2, 0);
    }

    public static float getCentemp() {
        return api.LEPGetCerTemp();
    }

    public static void tempCorrent(int in) {
        api.LEPTempCrt(in);
    }

}

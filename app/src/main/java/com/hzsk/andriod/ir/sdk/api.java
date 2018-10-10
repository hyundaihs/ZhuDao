package com.hzsk.andriod.ir.sdk;

import android.view.Surface;

import com.hzncc.zhudao.drawView.DrawRect;

public class api {
	static {

		System.loadLibrary("Andriod_IR_SDK");

	}

	static public native int LEPInit();

	static public native int LEPOpenDev(String name, int type);

	static public native int LEPReConnect(int type);

	static public native int LEPCloseDev(int type);

	static public native int setSurface(Surface surface);

	static public native int setSurfaceWin(Surface surface);

	static public native int LEPDevStart(int type);

	static public native int LEPRunCommand(byte[] cmd);

	static public native int LEPSetPalette(int type, int index);

	static public native int LEPGetSDKVersion(byte[] ver);

	static public native float LEPGetSysFpaTemperatureCelcius();

	static public native float LEPGetFpaFpaTemperature(int type);

	static public native float LEPGetSysAuxTemperatureCelcius();

	static public native int LEPSetSysTelemetryEnableState(int state);

	static public native int LEPSetOemVideoOutputEnable(int state);

	static public native int LEPRunOemReboot();

	static public native int LEPSetSysTelemetryLocation(int location);

	static public native int LEPRunFFC();

	static public native int LEPSetVidFreezeEnableState(int state);

	// /////IIC//////////////////////////
	static public native int LEPOpenPort(int port);

	static public native int LEPClosePort();

	public static native int LEPGetObjectTemp(int handle, DrawRect rect,
			String className);

	public static native int LEPAddObject(int type, int x0, int y0,
			int x1, int y1, String name, int n);
	
	public static native int LEPUpdateObject(int handle, int x0, int y0,
			int x1, int y1, String name, int n);
	
	public static native int LEPDeleteObject(int handle);
	
	public static native int LEPDeleteAllObject();
	
	public static native int LEPGetObjectHandle(int obj[]);
	
	public static native int LEPGetCurrPower();
	public static native int LEPLed(int type , int flag);
	public static native int LEPShutter(int type);
	
	public static native int LEPTempCrt(int value);
	public static native float LEPGetCerTemp();
}

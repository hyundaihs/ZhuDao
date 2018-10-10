package com.hzncc.zhudao.utils;

import android.content.Context;
import android.os.storage.StorageManager;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * ZhuDao
 * Created by 蔡雨峰 on 2017/6/24.
 */

public class UsbUtil {

    private static final String IS_ENABLED = "isUsbMassStorageEnabled";
    private static final String ENABLE_USB = "enableUsbMassStorage";
    private static final String DISABLE_USB = "disableUsbMassStorage";
    private static StorageManager mStorageManager;

    private static Method getMethod(String me) {
        Method method = null;
        try {
            method = mStorageManager.getClass().getDeclaredMethod(me);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return method;
    }

    public static boolean isEnabledUsb(Context context) {
        boolean flag = false;
        try {
            isEnabled(context);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return flag;
    }

    private static boolean isEnabled(Context context) throws InvocationTargetException, IllegalAccessException {
        if (mStorageManager == null) {
            mStorageManager = (StorageManager) context.getSystemService(Context.STORAGE_SERVICE);
            if (mStorageManager == null) {
                Log.w("Memory", "Failed to get StorageManager");
            }
        }
        Method method = getMethod(IS_ENABLED);
        if (null == method) {
            Log.w("Memory", "Failed to get method");
            return false;
        } else {
            method.setAccessible(true);
            return (boolean) method.invoke(mStorageManager);
        }
    }

    public static void changeUsbEnable(Context context, boolean enable) {
        if (mStorageManager == null) {
            mStorageManager = (StorageManager) context.getSystemService(Context.STORAGE_SERVICE);
            if (mStorageManager == null) {
                Log.w("Memory", "Failed to get StorageManager");
            }
        }
        try {
            enableUsb(enable);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private static void enableUsb(boolean enable) throws InvocationTargetException, IllegalAccessException {
        Method method = getMethod(enable ? ENABLE_USB : DISABLE_USB);
        if (null == method) {
            Log.w("Memory", "Failed to get method");
        } else {
            method.setAccessible(true);
            method.invoke(mStorageManager);
        }
    }
}

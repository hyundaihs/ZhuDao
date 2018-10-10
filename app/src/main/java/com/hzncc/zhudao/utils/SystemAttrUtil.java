package com.hzncc.zhudao.utils;

import android.content.Context;
import android.provider.Settings;
import android.util.Log;

import java.util.Set;

/**
 * ZhuDao
 * Created by 蔡雨峰 on 2017/6/16.
 */

public class SystemAttrUtil {

    private static final String IP = "ethernet_ip";
    private static final String MODE = "ethernet_mode";
    private static final String MASK = "ethernet_netmask";

    public static boolean setEthernetStaticIp(Context context, String ip, String mask) {
        String re1 = Settings.System.getString(context.getContentResolver(), IP);
        String re2 = Settings.System.getString(context.getContentResolver(), MODE);
        Log.d("setIp", "ip = " + re1 + " mode = " + re2);
        boolean isIp = Settings.System.putString(context.getContentResolver(), IP, ip);
        boolean isMode = Settings.System.putString(context.getContentResolver(), MODE, "manual");
        boolean isMask = Settings.System.putString(context.getContentResolver(), MASK, mask);
        re1 = Settings.System.getString(context.getContentResolver(), IP);
        re2 = Settings.System.getString(context.getContentResolver(), MODE);
        Log.d("setIp", "ip = " + re1 + " mode = " + re2);
        return isIp && isMode && isMask;
    }
}

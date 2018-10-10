package com.hzncc.zhudao.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * ZhuDao
 * Created by 蔡雨峰 on 2017/8/21.
 */

public class ToastUtil {
    public static void showShort(Context context, String string) {
        Toast.makeText(context, string, Toast.LENGTH_SHORT).show();
    }

    public static void showLong(Context context, String string) {
        Toast.makeText(context, string, Toast.LENGTH_LONG).show();
    }
}

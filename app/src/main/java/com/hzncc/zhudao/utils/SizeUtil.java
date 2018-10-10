package com.hzncc.zhudao.utils;

import android.content.Context;
import android.util.TypedValue;

/**
 * ZhuDao
 * Created by 蔡雨峰 on 2017/4/5.
 */

public class SizeUtil {
    public static int dp2px(Context context, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                context.getResources().getDisplayMetrics());
    }

    public static int sp2px(Context context, int sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp,
                context.getResources().getDisplayMetrics());
    }
}

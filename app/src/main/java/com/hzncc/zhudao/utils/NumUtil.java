package com.hzncc.zhudao.utils;

import java.math.BigDecimal;

/**
 * ZhuDao
 * Created by 蔡雨峰 on 2017/6/16.
 */

public class NumUtil {

    /**
     *
     * @param f 原数据
     * @param count 保留位数
     * @return 返回值
     */
    public static double formatDouble(double f,int count) {
        BigDecimal b = new BigDecimal(f);
        double f1 = b.setScale(count, BigDecimal.ROUND_HALF_UP).doubleValue();
        return f1;
    }

    /**
     *
     * @param f 原数据
     * @param count 保留位数
     * @return 返回值
     */
    public static String formatDoubleToString(double f,int count) {
        BigDecimal b = new BigDecimal(f);
        double f1 = b.setScale(count, BigDecimal.ROUND_HALF_UP).doubleValue();
        return String.valueOf(f1);
    }

    /**
     *
     * @param f 原数据
     * @param count 保留位数
     * @return 返回值
     */
    public static float formatFloat(float f,int count) {
        BigDecimal b = new BigDecimal(f);
        float f1 = b.setScale(count, BigDecimal.ROUND_HALF_UP).floatValue();
        return f1;
    }

    /**
     *
     * @param f 原数据
     * @param count 保留位数
     * @return 返回值
     */
    public static String formatFloatToString(float f,int count) {
        BigDecimal b = new BigDecimal(f);
        float f1 = b.setScale(count, BigDecimal.ROUND_HALF_UP).floatValue();
        return String.valueOf(f1);
    }
}

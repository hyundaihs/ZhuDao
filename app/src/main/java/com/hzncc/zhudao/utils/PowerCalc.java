package com.hzncc.zhudao.utils;

/**
 * ZhuDao
 * Created by 蔡雨峰 on 2017/6/21.
 */

public class PowerCalc {
    private static final int[] level = {8500, 7700, 7500, 7200, 6960};
    private static final int[] battery = {100, 75, 50, 25, 5};
    public static int power;
    public static int MAX_POWER = 8500;// 4.25V * 1000 电池充满电压
    public static int MIN_POWER = 6960;// 3.6V * 1000 接近关机电压
    public static int pcer = 0;

    public static int calcBattery(byte[] buffer, int size) {
        if (size == 9) {
            String result = bytesToHexString(buffer);
            short[] s = new short[result.length() / 2];
            for (int i = 0; i < result.length(); i += 2) {
                s[i / 2] = Short.parseShort(result.substring(i, i + 2), 16);
            }
            // 计算校验和
            int crc = s[2] + s[3] + s[4] + s[5] + s[6];
            if (s[0] == 0xF0 && s[1] == 0x05 && s[8] == 0xFF && s[7] == crc) {
                String str = result.substring(6, 10);
                power = Integer.valueOf(str);
            }
            for (int i = 0; i < level.length; i++) {
                if (power >= level[i]) {
                    if (i == 0) {
                        pcer = battery[0];
                    } else if ((level[i - 1] - power) < (power - level[i])) {
                        pcer = battery[i - 1];
                    } else {
                        pcer = battery[i];
                    }
                    break;
                }
            }
        }
//        if (power >= level[0]) {
//            pcer = 100;
//        } else if (power >= level[1]) {
//            if ((level[0] - power) < (power - level[1])) {
//                pcer = 100;
//            } else {
//                pcer = 75;
//            }
//        } else if (power >= level[2]) {
//            if ((level[1] - power) < (power - level[2])) {
//                pcer = 100;
//            } else {
//                pcer = 75;
//            }
//        } else if (power >= level[3]) {
//            if ((level[2] - power) < (power - level[3])) {
//                pcer = 100;
//            } else {
//                pcer = 75;
//            }
//        } else if (power >= level[4]) {
//            if ((level[3] - power) < (power - level[4])) {
//                pcer = 100;
//            } else {
//                pcer = 75;
//            }
//        } else {
//            pcer = 5;
//        }

//            if (power >= MAX_POWER) {
//                pcer = 100;
//            } else if (power <= MIN_POWER) {
//                pcer = 0;
//            } else {
//                double sub = MAX_POWER - MIN_POWER;
//                pcer = (int) ((power - MIN_POWER) / sub * 100);
//            }
//    }
        return pcer;
    }

    /*
    * Convert byte[] to hex
    * string.这里我们可以将byte转换成int，然后利用Integer.toHexString(int)来转换成16进制字符串。
    *
    * @param src byte[] data
    *
    * @return hex string
    */
    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }
}

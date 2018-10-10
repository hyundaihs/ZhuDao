package com.hzncc.zhudao.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.hzsk.andriod.ir.sdk.api;

/**
 * ZhuDao
 * Created by 蔡雨峰 on 2017/4/13.
 */

public class BitmapBuild {
    private static final int WIDTH = 80;
    private static final int HEIGHT = 60;
    private static byte[] colors = new byte[256 * 4];
    private static byte[] hearderByte = new byte[14];
    private static byte[] infohearder = new byte[40];

    public static void init() {
        addFileHeader(54 + WIDTH * HEIGHT);
        addBMPImageInfosHeader(WIDTH, HEIGHT);
    }

    public static Bitmap createBitmap(byte m_p8BitBmp[]) {
        int hl = hearderByte.length;
        int fl = infohearder.length;
        int cl = colors.length;
        int tl = m_p8BitBmp.length;

        byte[] bmpByte = new byte[hl + fl + cl + tl];
        System.arraycopy(hearderByte, 0, bmpByte, 0,
                hearderByte.length);
        System.arraycopy(infohearder, 0, bmpByte,
                hearderByte.length, infohearder.length);
        System.arraycopy(colors, 0, bmpByte, hearderByte.length
                + infohearder.length, colors.length);

        for (int i = 0; i < m_p8BitBmp.length / WIDTH; i++) {
            System.arraycopy(m_p8BitBmp, i * WIDTH, bmpByte, i
                    * WIDTH + hearderByte.length
                    + infohearder.length + colors.length, WIDTH);
        }

        Bitmap bitmap = BitmapFactory.decodeByteArray(bmpByte, 0,
                bmpByte.length);
        return bitmap;
    }

    private static void addBMPImageInfosHeader(int w, int h) {

        infohearder[0] = 0x28;
        infohearder[1] = 0x00;
        infohearder[2] = 0x00;
        infohearder[3] = 0x00;

        infohearder[4] = (byte) (w >> 0);
        infohearder[5] = (byte) (w >> 8);
        infohearder[6] = (byte) (w >> 16);
        infohearder[7] = (byte) (w >> 24);
        h = -h;

        infohearder[8] = (byte) (h >> 0);
        infohearder[9] = (byte) (h >> 8);
        infohearder[10] = (byte) (h >> 16);
        infohearder[11] = (byte) (h >> 24);

        infohearder[12] = 0x01;
        infohearder[13] = 0x00;

        infohearder[14] = 0x08;
        infohearder[15] = 0x00;

        infohearder[16] = 0x00;
        infohearder[17] = 0x00;
        infohearder[18] = 0x00;
        infohearder[19] = 0x00;

        infohearder[20] = 0x00;
        infohearder[21] = 0x00;
        infohearder[22] = 0x00;
        infohearder[23] = 0x00;
        infohearder[24] = 0x00;

        infohearder[25] = 0x00;
        infohearder[26] = 0x00;
        infohearder[27] = 0x00;

        infohearder[28] = 0x00;
        infohearder[29] = 0x00;
        infohearder[30] = 0x00;
        infohearder[31] = 0x00;

        infohearder[32] = 0x00;
        infohearder[33] = 0x00;
        infohearder[34] = 0x00;
        infohearder[35] = 0x00;

        infohearder[36] = 0x00;
        infohearder[37] = 0x00;
        infohearder[38] = 0x00;
        infohearder[39] = 0x00;
    }

    private static void addFileHeader(int size) {
        // byte[] buffer = new byte[14];

        hearderByte[0] = 0x42;
        hearderByte[1] = 0x4D;

        hearderByte[2] = (byte) (size >> 0);
        hearderByte[3] = (byte) (size >> 8);
        hearderByte[4] = (byte) (size >> 16);
        hearderByte[5] = (byte) (size >> 24);
        hearderByte[6] = 0x00;
        hearderByte[7] = 0x00;
        hearderByte[8] = 0x00;
        hearderByte[9] = 0x00;
        hearderByte[10] = 0x36;
        hearderByte[11] = 0x00;
        hearderByte[12] = 0x00;
        hearderByte[13] = 0x00;

    }

}

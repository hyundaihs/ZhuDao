package com.hzncc.zhudao.utils;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;

/**
 * ZhuDao
 * Created by 蔡雨峰 on 2017/6/20.
 */

public class SuProcessUtil {
    public static Process createSuProcess() throws IOException {
        File rootUser = new File("/system/xbin/ru");
        if (rootUser.exists()) {
            return Runtime.getRuntime().exec(rootUser.getAbsolutePath());
        } else {
            return Runtime.getRuntime().exec("su");
        }
    }

    public static Process createSuProcess(String cmd) throws IOException {
        DataOutputStream os = null;
        Process process = createSuProcess();

        try {
            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes(cmd + "\n");
            os.writeBytes("exit $?\n");
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException ignored) {
                }
            }
        }
        return process;
    }
}

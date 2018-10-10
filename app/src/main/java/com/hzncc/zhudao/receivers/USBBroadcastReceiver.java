package com.hzncc.zhudao.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.hzncc.zhudao.Constants;
import com.hzncc.zhudao.utils.UsbUtil;

/**
 * ZhuDao
 * Created by 蔡雨峰 on 2017/3/31.
 */
public class USBBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        switch (intent.getAction()) {
            case Intent.ACTION_DEVICE_STORAGE_LOW:
//                show(context, "STORAGE_LOW");
                Log.d("USBBroad", "STORAGE_LOW");
                break;
            case Intent.ACTION_MEDIA_MOUNTED:
                show(context, "ACTION_MEDIA_MOUNTED");
                break;
            case Intent.ACTION_MEDIA_UNMOUNTED:
                Log.d("USBBroad", "ACTION_MEDIA_UNMOUNTED");
                break;
            case Intent.ACTION_MEDIA_SHARED:
                Log.d("USBBroad", "SHARED " + UsbUtil.isEnabledUsb(context));
                Intent in = new Intent();
                in.setAction(Constants.ACTION_SD_UNEXIST);
                context.sendBroadcast(in);
                break;
            case Intent.ACTION_MEDIA_SCANNER_FINISHED:
                Log.d("USBBroad", "FINISHED " + UsbUtil.isEnabledUsb(context));
                in = new Intent();
                in.setAction(Constants.ACTION_SD_EXIST);
                context.sendBroadcast(in);
                break;
            default:
                Log.d("USBBroad", intent.getAction());
                break;

        }
    }

    /**
     * Intent.ACTION_MEDIA_BAD_REMOVAL
     * Intent.ACTION_MEDIA_BUTTON
     * Intent.ACTION_MEDIA_CHECKING
     * Intent.ACTION_MEDIA_EJECT
     * Intent.ACTION_MEDIA_MOUNTED
     * Intent.ACTION_MEDIA_NOFS
     * Intent.ACTION_MEDIA_REMOVED
     * Intent.ACTION_MEDIA_SCANNER_FINISHED
     * Intent.ACTION_MEDIA_SCANNER_SCAN_FILE
     * Intent.ACTION_MEDIA_SCANNER_STARTED
     * Intent.ACTION_MEDIA_SHARED
     * Intent.ACTION_MEDIA_UNMOUNTABLE
     * Intent.ACTION_MEDIA_UNMOUNTED
     *
     * @param context
     * @param text
     */

    private void show(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }
}

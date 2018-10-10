package com.hzncc.zhudao.receivers;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.widget.TextView;

import com.hzncc.zhudao.Constants;
import com.hzncc.zhudao.ui.MemoryActivity;
import com.hzncc.zhudao.utils.DateUtil;
import com.hzncc.zhudao.utils.DialogUtil;
import com.hzncc.zhudao.widget.BatteryView;

/**
 * ZhuDao
 * Created by 蔡雨峰 on 2017/6/22.
 */

public class BroadCastManager extends BroadcastReceiver {
    public static boolean isShow = false;
    private BatteryView batteryView;
    private TextView date;

    public BroadCastManager() {
        this(null, null);
    }


    public BroadCastManager(BatteryView batteryView) {
        this(batteryView, null);
    }


    public BroadCastManager(BatteryView batteryView, TextView date) {
        this.batteryView = batteryView;
        this.date = date;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        switch (intent.getAction()) {
            case "android.hardware.usb.action.USB_STATE":
                Log.d("Onreceiver", "USB_STATE");
                if (intent.getExtras().getBoolean("connected")) {
                    // usb 插入
                    if (!isShow) {
                        Activity activity = (Activity) context;
                        if (!(activity instanceof MemoryActivity)) {
                            Intent in = new Intent(context, MemoryActivity.class);
                            context.startActivity(in);
                            isShow = true;
                        }
                    }
                } else {
                    isShow = false;
                }
                break;
            case Constants.ACTION_BATTERY_UPDATE:
                if (null == batteryView) {
                    Log.d("Broad", "batteryView is null");
                    break;
                }
                int power = intent.getIntExtra("battery", 0);
                batteryView.setPower(power);
                break;
            case Constants.ACTION_BATTERY_FULL:
                Log.d("Broad", "_FULL");
//                DialogUtil.showErrorDialog((Activity) context, "电量已满！");
                break;
            case Constants.ACTION_BATTERY_LOW:
                Log.d("Broad", "LOW");
//                power = intent.getIntExtra("battery", 0);
//                if (power <= 5) {
//                    DialogUtil.showErrorDialog((Activity) context, "电量低，请尽快充电，以免影响正常使用！");
//                } else {
//                    DialogUtil.showErrorDialog((Activity) context, "电量不足" + power + "%！");
//                }
                break;
            case Intent.ACTION_DATE_CHANGED:
                Log.d("Broad", "CHANGED");
                if (null == date) {
                    break;
                }
                String dateTime = DateUtil.formatUnixTime(DateUtil.getUnixTimeByCalendar());
                date.setText("日期：" + dateTime);
                break;
            case Constants.ACTION_OS_SHUTDOWN:
                Log.d("Broad", "SHUTDOWN");
//                power = intent.getIntExtra("battery", 0);
//                DialogUtil.showErrorDialog((Activity) context, "电量不足" + power + "%，即将关机！");
                break;
            default:
                Log.d("Broad", intent.getAction());
                break;
        }
    }

//    Intent.ACTION_MEDIA_BAD_REMOVAL
//     * Intent.ACTION_MEDIA_BUTTON
//     * Intent.ACTION_MEDIA_CHECKING
//     * Intent.ACTION_MEDIA_EJECT
//     * Intent.ACTION_MEDIA_MOUNTED
//     * Intent.ACTION_MEDIA_NOFS
//     * Intent.ACTION_MEDIA_REMOVED
//     * Intent.ACTION_MEDIA_SCANNER_FINISHED
//     * Intent.ACTION_MEDIA_SCANNER_SCAN_FILE
//     * Intent.ACTION_MEDIA_SCANNER_STARTED
//     * Intent.ACTION_MEDIA_SHARED
//     * Intent.ACTION_MEDIA_UNMOUNTABLE
//     * Intent.ACTION_MEDIA_UNMOUNTED

    public void registerBattery(Context context) {
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.hardware.usb.action.USB_STATE");
        if (null != batteryView) {
            filter.addAction(Constants.ACTION_BATTERY_UPDATE);
            filter.addAction(Constants.ACTION_BATTERY_LOW);
//            filter.addAction(Constants.ACTION_BATTERY_FULL);
//            filter.addAction(Constants.ACTION_OS_SHUTDOWN);
        }
        if (null != date) {
            filter.addAction(Intent.ACTION_DATE_CHANGED);
        }
        context.registerReceiver(this, filter);
    }

    public void unregisterBattery(Context context) {
        context.unregisterReceiver(this);
    }
}

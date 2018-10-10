package com.hzncc.zhudao.ui;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hzncc.zhudao.Constants;
import com.hzncc.zhudao.R;
import com.hzncc.zhudao.entity.AppPath;
import com.hzncc.zhudao.utils.DialogUtil;
import com.hzncc.zhudao.utils.UsbUtil;
import com.hzncc.zhudao.widget.BatteryView;

/**
 * ZhuDao
 * Created by 蔡雨峰 on 2017/4/27.
 */

public class MemoryActivity extends Activity implements View.OnClickListener {
    private TextView hint;
    private Button connect;
    private BatteryView batteryView;
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case "android.hardware.usb.action.USB_STATE":
                    Log.d("Onreceiver", "USB_STATE");
                    if (!intent.getExtras().getBoolean("connected")) {
                        //   usb 拔出
                        Activity activity = (Activity) context;
                        if (activity instanceof MemoryActivity) {
                            activity.finish();
                        }
                    }
                    break;
                case Constants.ACTION_SD_EXIST:
                    Log.d("Broad", "ACTION_SD_EXIST");
                    connect.setText(AppPath.isExistSDCard() ? "连接电脑" : "断开连接");
//                    hint.setText("点击按钮连接电脑！");
                    break;
                case Constants.ACTION_SD_UNEXIST:
                    Log.d("Broad", "ACTION_SD_UNEXIST");
                    connect.setText(AppPath.isExistSDCard() ? "连接电脑" : "断开连接");
//                    hint.setText(getString(R.string.connect_pc));
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
                    DialogUtil.showErrorDialog((Activity) context, "电量已满！");
                    break;
                case Constants.ACTION_BATTERY_LOW:
                    Log.d("Broad", "LOW");
                    power = intent.getIntExtra("battery", 0);
                    if (power <= 5) {
                        DialogUtil.showErrorDialog((Activity) context, "电量低，请尽快充电，以免影响正常使用！");
                    } else {
                        DialogUtil.showErrorDialog((Activity) context, "电量不足" + power + "%！");
                    }
                    break;
                case Constants.ACTION_OS_SHUTDOWN:
                    Log.d("Broad", "SHUTDOWN");
                    power = intent.getIntExtra("battery", 0);
                    DialogUtil.showErrorDialog((Activity) context, "电量不足" + power + "%，即将关机！");
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory);
        init();
        registerBattery(this);

//        mStorageManager.registerListener(mStorageListener);
//        if (mStorageManager == null && mStorageListener != null) {
//            mStorageManager.unregisterListener(mStorageListener);
//        }
//        if (on) {
//            mStorageManager.enableUsbMassStorage();
//        } else {
//            mStorageManager.disableUsbMassStorage();
//        }

    }

    public void registerBattery(Context context) {
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.hardware.usb.action.USB_STATE");
        filter.addAction(Constants.ACTION_SD_EXIST);
        filter.addAction(Constants.ACTION_SD_UNEXIST);
        filter.addAction(Constants.ACTION_BATTERY_UPDATE);
        filter.addAction(Constants.ACTION_BATTERY_LOW);
//            filter.addAction(Constants.ACTION_BATTERY_FULL);
//            filter.addAction(Constants.ACTION_OS_SHUTDOWN);
        registerReceiver(broadcastReceiver, filter);
    }

    public void unregisterBattery(Context context) {
        context.unregisterReceiver(broadcastReceiver);
    }

    @Override
    protected void onStop() {
        UsbUtil.changeUsbEnable(this, false);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        unregisterBattery(this);
        super.onDestroy();
    }

    private void init() {
        findViewById(R.id.activity_memory_back).setOnClickListener(this);
        connect = (Button) findViewById(R.id.activity_memory_connect);
        hint = (TextView) findViewById(R.id.activity_memory_hint);
        connect.setOnClickListener(this);
        batteryView = (BatteryView) findViewById(R.id.activity_memory_battery);
        connect.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (AppPath.isExistSDCard()) {
                    hint.setText("点击按钮连接电脑！");
                } else {
                    hint.setText(getString(R.string.connect_pc));
                }
            }
        });
        connect.setText(AppPath.isExistSDCard() ? "连接电脑" : "断开连接");
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_memory_back:
                UsbUtil.changeUsbEnable(this, false);
                finish();
                break;
            case R.id.activity_memory_connect:
                if (connect.getText().toString().equals("...")) {
                    break;
                }
                connect.setText("...");
                if (AppPath.isExistSDCard()) {
                    UsbUtil.changeUsbEnable(this, true);
                } else {
                    UsbUtil.changeUsbEnable(this, false);
                }
                break;
        }
    }
}

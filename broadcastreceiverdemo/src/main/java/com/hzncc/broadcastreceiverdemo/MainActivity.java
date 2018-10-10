package com.hzncc.broadcastreceiverdemo;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

public class MainActivity extends Activity implements View.OnClickListener {

    private TextView textView;
    private ScrollView scrollView;
    private StringBuffer buffer;
    private UsbManager usbManager;
    private BatteryManager batteryManager;

    // 接受广播
    private BroadcastReceiver batteryChangedReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case "android.hardware.usb.action.USB_STATE":
                    append("Home->" + " android.hardware.usb.action.USB_STATE");
                    append("Home->" + " connected" + intent.getExtras().getBoolean("connected"));
                    break;
                case Intent.ACTION_BATTERY_CHANGED:
                    int level = intent.getIntExtra("level", 0);
                    int scale = intent.getIntExtra("scale", 100);
                    int power = level * 100 / scale;
                    append("Home->" + "ACTION_BATTERY_CHANGED " + power + "/%");
                    break;
                case Intent.ACTION_DATE_CHANGED:
                    append("Home->" + "ACTION_DATE_CHANGED");
                    break;
                case Intent.ACTION_BATTERY_LOW:
                    append("Home->" + "ACTION_BATTERY_LOW");
                    break;
                case Intent.ACTION_DEVICE_STORAGE_LOW:
                    append("Home->" + "ACTION_DEVICE_STORAGE_LOW");
                    break;
                case Intent.ACTION_MEDIA_SHARED:
                    append("Home->" + "ACTION_MEDIA_SHARED");
                    break;
                case Intent.ACTION_MEDIA_MOUNTED:
                    append("Home->" + "ACTION_MEDIA_MOUNTED");
                    break;
                case Intent.ACTION_MEDIA_UNMOUNTED:
                    append("Home->" + "ACTION_MEDIA_UNMOUNTED");
                    break;
                case Intent.ACTION_BATTERY_OKAY:
                    append("Home->" + "ACTION_BATTERY_OKAY");
                    break;
                default:
                    append("Home->" + "default");
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buffer = new StringBuffer();
        textView = (TextView) findViewById(R.id.textView);
        scrollView = (ScrollView) findViewById(R.id.scroll);
        findViewById(R.id.button).setOnClickListener(this);
        findViewById(R.id.clean).setOnClickListener(this);
    }

    public void append(String text) {
        buffer.append(text + "\n");
        textView.setText(buffer.toString());
        scrollView.fullScroll(ScrollView.FOCUS_DOWN);
    }

    @Override
    protected void onResume() {
        registerBattery();
        super.onResume();
    }

    @Override
    protected void onPause() {
        unregisterBattery();
        super.onPause();
    }

    private void registerBattery() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.hardware.usb.action.USB_STATE");
        filter.addAction(Intent.ACTION_BATTERY_CHANGED);
        filter.addAction(Intent.ACTION_DATE_CHANGED);
        filter.addAction(Intent.ACTION_BATTERY_LOW);
        filter.addAction(Intent.ACTION_DEVICE_STORAGE_LOW);
        filter.addAction(Intent.ACTION_MEDIA_SHARED);
        filter.addAction(Intent.ACTION_MEDIA_MOUNTED);
        filter.addAction(Intent.ACTION_BATTERY_OKAY);
        filter.addAction("else");
        registerReceiver(batteryChangedReceiver, filter);
    }

    private void unregisterBattery() {
        unregisterReceiver(batteryChangedReceiver);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.clean:
                buffer.delete(0, buffer.length());
                append("");
                break;
            default:
                append("send braodcastReceiver->" + "else");
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_BATTERY_LOW);
                sendBroadcast(intent);
                break;
        }
    }

    public class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case "android.hardware.usb.action.USB_STATE":
                    append("Broad->" + " connected" + intent.getExtras().getBoolean("connected"));
                    append("Broad->" + " host_connected:" + intent.getExtras().getBoolean("host_connected"));
                    break;
                case Intent.ACTION_BATTERY_CHANGED:
                    int level = intent.getIntExtra("level", 0);
                    int scale = intent.getIntExtra("scale", 100);
                    int power = level * 100 / scale;
                    append("Broad->" + "ACTION_BATTERY_CHANGED " + power + "/%");
                    break;
                case Intent.ACTION_DATE_CHANGED:
                    append("Broad->" + "ACTION_DATE_CHANGED");
                    break;
                case Intent.ACTION_BATTERY_LOW:
                    append("Broad->" + "ACTION_BATTERY_LOW");
                    break;
                case Intent.ACTION_DEVICE_STORAGE_LOW:
                    append("Broad->" + "ACTION_DEVICE_STORAGE_LOW");
                    break;
                case Intent.ACTION_MEDIA_SHARED:
                    append("Broad->" + "ACTION_MEDIA_SHARED");
                    break;
                case Intent.ACTION_MEDIA_MOUNTED:
                    append("Broad->" + "ACTION_MEDIA_MOUNTED");
                    break;
                case Intent.ACTION_MEDIA_UNMOUNTED:
                    append("Broad->" + "ACTION_MEDIA_UNMOUNTED");
                    break;
                case Intent.ACTION_BATTERY_OKAY:
                    append("Broad->" + "ACTION_BATTERY_OKAY");
                    break;
                default:
                    append("Broad->" + "default");
                    break;
            }
        }
    }
}

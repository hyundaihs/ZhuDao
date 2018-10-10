package com.hzncc.zhudao.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.example.serialport.SerialPortUtil;
import com.hzncc.zhudao.Constants;
import com.hzncc.zhudao.entity.AppPath;
import com.hzncc.zhudao.serverutil.JsonUtil;
import com.hzncc.zhudao.serverutil.ServerInterface;
import com.hzncc.zhudao.serverutil.SocketManager;
import com.hzncc.zhudao.utils.PowerCalc;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * ZhuDao
 * Created by 蔡雨峰 on 2017/6/19.
 */

public class MyService extends Service implements SerialPortUtil.OnDataReceiveListener {
    private final String TAG = "Service--->";

    private final IBinder binder = new MyBinder();
    private boolean flag = false;
    private int battery = -1;
    private byte[] mBuffer;
    private SerialPortUtil serialPortUtil;
    private boolean isLow = false;

    private SocketManager socketManager;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SocketManager.RESULT_MSG_SUCCESS:
                    reply(msg.obj.toString());
                    break;
                case SocketManager.RESULT_FILE_SUCCESS:
                    break;
                case SocketManager.RESULT_ALL_FILE_SUCCESS:
                    break;
                case SocketManager.RESULT_FAILED:
                    break;
            }
        }
    };

    private void reply(String result) {
        switch (JsonUtil.getAction(result)) {
            case ServerInterface.CONNECT:
                socketManager.otherAddr = JsonUtil.getOtherIp(result);
                File[] files = new File(AppPath.IMAGE).listFiles();
                socketManager.replyMessage(JsonUtil.getReplyString(SocketManager.REPLY_SUCCESS,
                        SocketManager.REPLY_NORMAL_MSG, ServerInterface.CONNECT, JsonUtil.getObjectByFiles(files)));
                break;
            case ServerInterface.GET_IMAGE:
                String fileName = JsonUtil.getRequestFilename(result);
                List<String> fileList = new ArrayList<>();
                fileList.add(fileName);
                List<String> message = new ArrayList<>();
                message.add(JsonUtil.getReplyString(SocketManager.REPLY_SUCCESS,
                        SocketManager.REPLY_FILE_MSG, ServerInterface.GET_IMAGE, JsonUtil.getRepylFileMessage(fileName)));
                socketManager.replyFile(message, fileList);
                break;
            case ServerInterface.GET_IMAGES:
                List<String> filenames = JsonUtil.getRequestFilenames(result);
                Log.d("getImages", filenames.toString());
                List<String> messages = new ArrayList<>();
                for (int i = 0; i < filenames.size(); i++) {
                    messages.add(JsonUtil.getReplyString(SocketManager.REPLY_SUCCESS,
                            SocketManager.REPLY_FILE_MSG, ServerInterface.GET_IMAGES, JsonUtil.getRepylFileMessage(filenames.get(i))));
                }
                socketManager.replyFile(messages, filenames);
                break;
        }
    }

    /*  绑定时执行*/
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind");
        return binder;
    }

    /*只在创建时执行一次*/
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
        initBattery();
    }

    private void initBattery() {
        mBuffer = new byte[5];
        mBuffer[0] = (byte) 0xF0;
        mBuffer[1] = (byte) 0x01;
        mBuffer[2] = (byte) 0x0B;
        mBuffer[3] = (byte) 0x0B;
        mBuffer[4] = (byte) 0xFF;
        serialPortUtil = SerialPortUtil.getInstance();
        serialPortUtil.setOnDataReceiveListener(this);
    }

    /*断开绑定或者stopService时执行*/
    @Override
    public void onDestroy() {
        flag = false;
        socketManager.close();
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    /* 当内存不够时执行改方法 */
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        onDestroy();// 注销该service
    }

    /* 当从新尝试绑定时执行 */
    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
        Log.d(TAG, "onRebind");
    }

    /* ，每次startService都会执行该方法，而改方法执行后会自动执行onStart()方法 */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand-->flags=" + flags + "  startId=" + startId);
        flag = true;
        new Thread(new MyRunnable()).start();
        socketManager = new SocketManager(handler);
        return super.onStartCommand(intent, flags, startId);
    }

    /*断开绑定时执行*/
    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "onUnbind");
        return super.onUnbind(intent);
    }

    private void sendAction(String action) {
        Intent intent = new Intent();
        intent.setAction(action);
        intent.putExtra("battery", battery);
        sendBroadcast(intent);
    }

    @Override
    public void onDataReceive(byte[] buffer, int size) {
        battery = PowerCalc.calcBattery(buffer, size);
        if (battery <= 25) {
            if (!isLow) {
                sendAction(Constants.ACTION_BATTERY_LOW);
                isLow = true;
            }
        } else {
            isLow = false;
        }
        sendAction(Constants.ACTION_BATTERY_UPDATE);
    }

    private void getBatteryPower() {
        serialPortUtil.sendBuffer(mBuffer);
    }

    public class MyBinder extends Binder {
        public MyService getService() {
            return MyService.this;
        }
    }

    private class MyRunnable implements Runnable {

        @Override
        public void run() {
            while (flag) {
                getBatteryPower();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}

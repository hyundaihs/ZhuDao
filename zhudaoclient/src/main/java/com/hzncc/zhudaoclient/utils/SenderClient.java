package com.hzncc.zhudaoclient.utils;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * ZhuDao
 * Created by 蔡雨峰 on 2017/7/21.
 */

public class SenderClient {
    private Socket socket;
    private PrintStream output;
    private boolean flag;
    private int port = 5002;
    private String ip = "";

    public SenderClient(String ip) {
        flag = true;
        this.ip = ip;
        initClientSocket();
    }

    private void initClientSocket() {
//        new Thread(new ReceiverRunnable()).start();
    }

    public void close() {
        flag = false;
        closeSocket();
    }

    public void sendMessage(String str) {
        new Thread(new SendRunnable(str)).start();
    }

    private void closeSocket() {
        try {
            if (null != output) {
                output.close();
            }
            if (null != socket) {
                socket.close();
            }
        } catch (IOException e) {
            Log.e("sender", "error" + e);
        }
    }

    private byte[] receiveData() {
        if (socket == null || socket.isClosed()) {
            try {
                socket = new Socket(ip, port);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        byte[] data = null;
        if (socket.isConnected()) {
            try {
                BufferedInputStream bufferedInputStream = new BufferedInputStream(socket.getInputStream());
                data = new byte[bufferedInputStream.available()];
                int i = bufferedInputStream.read(data);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            data = new byte[1];
        }
        return data;
    }

    private class SendRunnable implements Runnable {

        private String message;

        SendRunnable(String message) {
            this.message = message;
        }

        @Override
        public void run() {
            try {
                if (socket == null || socket.isClosed()) {
                    socket = new Socket(ip, port);
                }
                output = new PrintStream(socket.getOutputStream(), true, "gbk");
            } catch (UnknownHostException e) {
                Log.e("sender", "请检查端口号是否为服务器IP");
                return;
            } catch (IOException e) {
                Log.e("sender", "服务器未开启");
                return;
            }
            if (null == message) {
                Log.e("send", "message is null");
                return;
            }
            output.println(message);
            byte[] data = receiveData();
            if (data.length > 1) {
                Log.e("sender", new String(data));
            }
        }
    }

    private class ReceiverRunnable implements Runnable {

        @Override
        public void run() {
            while (flag) {
                byte[] data = receiveData();
                if (data.length > 1) {
                    Log.e("sender", new String(data));
                }
            }
        }
    }
}

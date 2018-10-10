package com.hzncc.zhudaoclient.serverutil;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.hzncc.zhudaoclient.utils.FileUtil;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class SocketManager {
    public static final int RESULT_MSG_SUCCESS = 0;
    public static final int RESULT_FILE_SUCCESS = 1;
    public static final int RESULT_ALL_FILE_SUCCESS = 2;
    public static final int RESULT_FAILED = -1;
    public static final int REPLY_SUCCESS = 0;
    public static final int REPLY_FAILED = 1;
    public static final int REPLY_NORMAL_MSG = 0;
    public static final int REPLY_FILE_MSG = 1;
    private static final int PORT = 9999;
    public String otherAddr = "";
    private ServerSocket server;
    private boolean flag;
    private Handler handler = null;

    public SocketManager(Handler handler) {
        this.handler = handler;
        flag = true;
        int port = 9999;
        while (port > 9000) {
            try {
                server = new ServerSocket(PORT);
                break;
            } catch (Exception e) {
                port--;
            }
        }
        Thread receiveFileThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (flag) {
                    receiveFile();
                }
            }
        });
        receiveFileThread.start();
    }

    private void sendHandleMsg(int what, Object obj) {
        if (handler != null) {
            Message.obtain(handler, what, obj).sendToTarget();
        }
    }

    public void close() {
        flag = false;
        try {
            if (null != server)
                server.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void receiveFile() {
        try {

            Socket name = server.accept();
            InputStream nameStream = name.getInputStream();
            InputStreamReader streamReader = new InputStreamReader(nameStream);
            BufferedReader br = new BufferedReader(streamReader);
            String message = br.readLine();
            br.close();
            streamReader.close();
            nameStream.close();
            name.close();
            Log.d("receiver", message);
            sendHandleMsg(RESULT_MSG_SUCCESS, message);
            if (JsonUtil.getType(message) == REPLY_NORMAL_MSG) {
                return;
            }
            Socket data = server.accept();
            InputStream dataStream = data.getInputStream();
            String fileName = JsonUtil.getFileName(message);
            File tmp = new File(fileName.replace(FileUtil.OLD, FileUtil.ROOT));
            if (FileUtil.initDirectory(tmp)) {
                Log.d("socketManager", "mkdir " + tmp.toString() + "成功");
            } else {
                Log.d("socketManager", "mkdir " + tmp.toString() + "失败");
            }
            FileOutputStream file = new FileOutputStream(fileName, false);
            byte[] buffer = new byte[1024];
            int size = -1;
            while ((size = dataStream.read(buffer)) != -1) {
                file.write(buffer, 0, size);
            }
            file.close();
            dataStream.close();
            data.close();
            sendHandleMsg(RESULT_FILE_SUCCESS, fileName);
        } catch (Exception e) {
            sendHandleMsg(RESULT_FAILED, e.getMessage());
        }
    }

    public void replyFile(List<String> message, List<String> files) {
        if ("".equals(otherAddr)) {
            Log.e("replyFile", "无效的IP地址 " + otherAddr);
            return;
        }
        sendFile(message, files, otherAddr);
    }

    public void sendFile(List<String> message, List<String> files, String ipAddress) {
        new Thread(new SendFileRunnable(message, files, ipAddress, PORT)).start();
    }

    public void sendFile(List<String> message, List<String> files, String ipAddress, int port) {
        try {
            for (int i = 0; i < files.size(); i++) {
                Log.d("sendFile", message.get(0));
                Log.d("sendFile", files.get(0));
                Socket name = new Socket(ipAddress, port);
                OutputStream outputName = name.getOutputStream();
                OutputStreamWriter outputWriter = new OutputStreamWriter(outputName);
                BufferedWriter bwName = new BufferedWriter(outputWriter);
                bwName.write(message.get(i));
                bwName.close();
                outputWriter.close();
                outputName.close();
                name.close();
//                sendHandleMsg(RESULT_MSG_SUCCESS, fileName.get(i));

                Socket data = new Socket(ipAddress, port);
                OutputStream outputData = data.getOutputStream();
                FileInputStream fileInput = new FileInputStream(files.get(i));
                int size = -1;
                byte[] buffer = new byte[1024];
                while ((size = fileInput.read(buffer, 0, 1024)) != -1) {
                    outputData.write(buffer, 0, size);
                }
                outputData.close();
                fileInput.close();
                data.close();
//                sendHandleMsg(RESULT_FILE_SUCCESS, fileName.get(i));
            }
            sendHandleMsg(RESULT_ALL_FILE_SUCCESS, "所有文件发送完成");
        } catch (Exception e) {
            sendHandleMsg(RESULT_FAILED, e.getMessage());
        }
    }

    public void replyMessage(String message) {
        if ("".equals(otherAddr)) {
            Log.e("replyFile", "无效的IP地址 " + otherAddr);
            return;
        }
        sendMessage(message, otherAddr, null);
    }

    public void sendMessage(String message, String ipAddress, Handler handler) {
        if (null != handler) {
            this.handler = handler;
        }
        new Thread(new SendMessageRunnable(message, ipAddress, PORT)).start();
    }

    public void sendMessage(String message, String ipAddress, int port) {
        try {
            Socket name = new Socket(ipAddress, port);
            OutputStream outputName = name.getOutputStream();
            OutputStreamWriter outputWriter = new OutputStreamWriter(outputName);
            BufferedWriter bwName = new BufferedWriter(outputWriter);
            bwName.write(message);
            bwName.close();
            outputWriter.close();
            outputName.close();
            name.close();
//            sendHandleMsg(RESULT_MSG_SUCCESS, message);
        } catch (Exception e) {
            sendHandleMsg(RESULT_FAILED, e.getMessage());
        }
    }

    private class SendFileRunnable implements Runnable {

        int port;
        private List<String> message;
        private List<String> files;
        private String ipAddr;

        public SendFileRunnable(List<String> message, List<String> files, String ipAddr, int port) {
            this.message = message;
            this.files = files;
            this.ipAddr = ipAddr;
            this.port = port;
        }

        @Override
        public void run() {
            sendFile(message, files, ipAddr, port);
        }
    }

    private class SendMessageRunnable implements Runnable {

        int port;
        private String ipAddr, message;

        public SendMessageRunnable(String message, String ipAddr, int port) {
            this.ipAddr = ipAddr;
            this.message = message;
            this.port = port;

        }

        @Override
        public void run() {
            sendMessage(message, ipAddr, port);
        }
    }
}

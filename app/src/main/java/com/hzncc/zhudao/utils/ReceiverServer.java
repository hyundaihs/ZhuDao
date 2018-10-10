package com.hzncc.zhudao.utils;

import android.util.Log;

import com.hzncc.zhudao.Constants;
import com.hzncc.zhudao.entity.AppPath;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * ZhuDao
 * Created by 蔡雨峰 on 2017/7/21.
 */

public class ReceiverServer {
    private ServerSocket serverSocket;
    private int port = 5002;
    private boolean flag;

    public ReceiverServer() {
        flag = true;
        initServerSocket();
    }

    private void initServerSocket() {
        new Thread(new ServerRunnable()).start();
    }

    public void close() {
        flag = false;
        if (null != serverSocket) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void replyMessage(Socket clientSocket, String string) throws IOException {
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), false);
        // 向 client 端发送响应数据
        out.println(string);
        // 关闭各个流
        out.close();
    }

    private class ServerRunnable implements Runnable {

        @Override
        public void run() {
            try {
                serverSocket = new ServerSocket(port);
                while (flag) {
                    Socket clientSocket = serverSocket.accept();//阻塞等待处理...
                    String remoteIP = clientSocket.getInetAddress().getHostAddress();
                    int remotePort = clientSocket.getLocalPort();
                    Log.d("service", "A client connected. IP:" + remoteIP + ", Port: " + remotePort);
                    Log.d("service", "server: receiving.............");
                    // 获得 client 端的输入输出流，为进行交互做准备
                    BufferedReader in = new BufferedReader(new InputStreamReader(
                            clientSocket.getInputStream()));
                    // 获得 client 端发送的数据
                    String tmp = in.readLine();
                    // String content = new String(tmp.getBytes("utf-8"));
                    Log.d("service", "Client message is: " + tmp);
                    if(null==tmp){
                        Log.e("service","tmp is null");
                        continue;
                    }
                    switch (tmp) {
                        case Constants.REQUEST_GET_LIST:
                            File file = new File(AppPath.IMAGE);
                            if(!file.exists()){
                                Log.e("service","sdcard is not found");
                                break;
                            }
                            File[] files = file.listFiles();
                            StringBuilder stringBuffer = new StringBuilder();
                            for (File f : files) {
                                stringBuffer.append("/").append(f.getName());
                            }
                            Log.d("service",stringBuffer.toString());
                            replyMessage(clientSocket, stringBuffer.toString());
                            break;
                        case Constants.REQUEST_GET_CACHE_IMAGES:
                            break;
                        case Constants.REQUEST_DOWNLOAD_IMAGES:
                            break;
                    }
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}

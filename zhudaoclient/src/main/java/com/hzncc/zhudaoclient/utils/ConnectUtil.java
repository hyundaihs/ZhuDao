package com.hzncc.zhudaoclient.utils;

import android.util.Log;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * ZhuDao
 * Created by 蔡雨峰 on 2017/7/24.
 */

public class ConnectUtil {
    public boolean isConnected;
    private SenderClient senderClient;

    public List<String> getConnectedIP() {
        List<String> connectedIP = new ArrayList<String>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(
                    "/proc/net/arp"));
            String line;
            while ((line = br.readLine()) != null) {
                String[] splitted = line.split(" +");
                if (splitted != null && splitted.length >= 4) {
                    String ip = splitted[0];
                    connectedIP.add(ip);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        connectedIP.remove(0);
        return connectedIP;
    }

    public boolean connectServer() {
        return false;
    }
}

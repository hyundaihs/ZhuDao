package com.hzncc.zhudao.utils;

import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.hzncc.zhudao.db.GroupDao;
import com.hzncc.zhudao.entity.Group;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

/**
 * ZhuDao
 * Created by 蔡雨峰 on 2017/12/14.
 */

public class ExcelUtil {

    public static final int START = 0;
    public static final int CONVERTING = 1;
    public static final int COMPLETED = 2;
    public static final int ERROR = 3;

    private Workbook book;
    private OnDataConvertListener onDataConvertListener;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (null == onDataConvertListener) {
                return;
            }
            switch (msg.what) {
                case START:
                    onDataConvertListener.onStart(msg.arg1);
                    break;
                case CONVERTING:
                    onDataConvertListener.onConverting(msg.arg1, msg.arg2);
                    break;
                case COMPLETED:
                    onDataConvertListener.onCompleted(msg.arg1);
                    break;
                case ERROR:
                    onDataConvertListener.onError(msg.arg1, msg.arg2, msg.obj.toString());
                    break;
            }
        }
    };

    public void openFile(String path) {
        File file = new File(path);
        if (!file.exists()) {
            Log.e("openExcelFile", "is invalid file path");
            return;
        }
        try {
            InputStream is = new FileInputStream(file);
            book = Workbook.getWorkbook(is);

        } catch (IOException | BiffException e) {
            e.printStackTrace();
        }

    }

    public void save(SQLiteDatabase db, OnDataConvertListener onDataConvertListener) {
        this.onDataConvertListener = onDataConvertListener;
        new Thread(new SaveRunnable(db)).start();
    }

    public interface OnDataConvertListener {
        void onStart(int count);

        void onConverting(int count, int position);

        void onCompleted(int count);

        void onError(int count, int position, String error);
    }

    private class SaveRunnable implements Runnable {
        SQLiteDatabase db;

        SaveRunnable(SQLiteDatabase db) {
            this.db = db;
        }

        @Override
        public void run() {
            handler.sendEmptyMessage(START);
            if (db == null) {
                Message message = handler.obtainMessage(ERROR, 0, 0, "加载数据库失败");
                handler.sendMessage(message);
                return;
            }
            if (null == book) {
                Message message = handler.obtainMessage(ERROR, 0, 0, "Excel文件获取失败");
                handler.sendMessage(message);
                return;
            }
            Sheet sheet = book.getSheet(0);
            GroupDao groupDao = new GroupDao(db);
            int rows = sheet.getRows();
            for (int j = 0; j < rows; ++j) {
                Group group = new Group();
                group.setGroup_name(sheet.getCell(0, j).getContents());
                if (group.getGroup_name().trim().length() <= 0) {
                    continue;
                }
                if (groupDao.isCollByName(group.getGroup_name())) {
                    continue;
                }
                if (groupDao.add(group) > 0) {
                    Message message = handler.obtainMessage(CONVERTING, rows, j);
                    handler.sendMessage(message);
                } else {
                    Message message = handler.obtainMessage(ERROR, rows, j, "数据加载失败");
                    handler.sendMessage(message);
                }
            }
            book.close();
            Message message = handler.obtainMessage(COMPLETED, rows);
            handler.sendMessage(message);
        }
    }

}

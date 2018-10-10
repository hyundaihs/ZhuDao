package com.hzncc.kevin.excel2sqlite;

import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

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
                    onDataConvertListener.onError(msg.arg1, msg.arg2);
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
            // 1
            InputStream is = new FileInputStream(file);
            // 2
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

        void onError(int count, int position);
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
                Message message = handler.obtainMessage(ERROR, "invalid SQLiteDatabase db");
                handler.sendMessage(message);
                return;
            }
            GroupDao groupDao = new GroupDao(db);
            Sheet sheet = book.getSheet(0);
            int rows = sheet.getRows();
            for (int j = 0; j < rows; ++j) {
                Group group = new Group();
                group.setGroup_name(sheet.getCell(0, j).getContents());
                if (groupDao.add(group) == 1) {
                    Message message = handler.obtainMessage(CONVERTING, rows, j);
                    handler.sendMessage(message);
                } else {
                    Message message = handler.obtainMessage(ERROR, rows, j);
                    handler.sendMessage(message);
                }
            }
            book.close();
            Message message = handler.obtainMessage(COMPLETED, rows);
            handler.sendMessage(message);
        }
    }

}

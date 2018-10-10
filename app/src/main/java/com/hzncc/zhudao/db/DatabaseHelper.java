package com.hzncc.zhudao.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.hzncc.zhudao.entity.AlarmLog;
import com.hzncc.zhudao.entity.Group;
import com.hzncc.zhudao.entity.User;
import com.hzncc.zhudao.entity.WorkLog;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String name = "zhudao.db";// 数据库名称
    private static final int version = 2;// 数据库版本

    public DatabaseHelper(Context context) {
        super(context, name, null, version);
    }

    public DatabaseHelper(Context context, int version) {
        super(context, name, null, version);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        new UserDao(db).createTable(new User());
        new AlarmLogDao(db).createTable(new AlarmLog());
        new WorkLogDao(db).createTable(new WorkLog());
        new GroupDao(db).createTable(new Group());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        new UserDao(db).updateTable(new User());
        new AlarmLogDao(db).updateTable(new AlarmLog());
        new WorkLogDao(db).updateTable(new WorkLog());
        new GroupDao(db).createTable(new Group());
    }


}

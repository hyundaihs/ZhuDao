package com.hzncc.observerdemo.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.hzncc.observerdemo.entity.Student;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String name = "observer_demo.db";// 数据库名称
    private static final int version = 1;// 数据库版本

    public DatabaseHelper(Context context) {
        super(context, name, null, version);
    }

    public DatabaseHelper(Context context, int version) {
        super(context, name, null, version);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        new StudentDao(db).createTable(new Student());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}

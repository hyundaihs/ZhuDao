package com.hzncc.kevin.excel2sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
        new GroupDao(db).createTable(new Group());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        new GroupDao(db).createTable(new Group());
    }


}

package com.hzncc.observerdemo.db;

import android.database.sqlite.SQLiteDatabase;

import com.hzncc.observerdemo.entity.Student;

/**
 * ZhuDao
 * Created by 蔡雨峰 on 2017/5/3.
 */

public class StudentDao extends SQliteDao<Student> {
    /**
     * @param db
     */
    public StudentDao(SQLiteDatabase db) {
        super(db, Student.TAB_NAME);
    }
}

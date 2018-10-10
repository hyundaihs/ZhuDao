package com.hzncc.zhudao.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.hzncc.zhudao.entity.Group;

import java.util.ArrayList;

/**
 * ZhuDao
 * Created by 蔡雨峰 on 2017/12/14.
 */

public class GroupDao extends SQliteDao<Group> {
    public GroupDao(SQLiteDatabase db) {
        super(db, Group.TAB_NAME);
    }

    public boolean isCollByName(String name) {
        Cursor cursor = db.query(TAB_NAME, null, "group_name = ?", new String[]{name}, null, null, null);
        boolean rel = cursor.moveToFirst();
        cursor.close();
        return rel;
    }
}

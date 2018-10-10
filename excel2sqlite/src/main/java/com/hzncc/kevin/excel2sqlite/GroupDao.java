package com.hzncc.kevin.excel2sqlite;

import android.database.sqlite.SQLiteDatabase;


/**
 * ZhuDao
 * Created by 蔡雨峰 on 2017/12/14.
 */

public class GroupDao extends SQliteDao<Group> {
    public GroupDao(SQLiteDatabase db) {
        super(db, Group.TAB_NAME);
    }
}

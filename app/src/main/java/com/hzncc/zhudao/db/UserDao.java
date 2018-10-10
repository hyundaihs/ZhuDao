package com.hzncc.zhudao.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.hzncc.zhudao.entity.User;

/**
 * ZhuDao
 * Created by 蔡雨峰 on 2017/4/6.
 */

public class UserDao extends SQliteDao<User> {

    public UserDao(SQLiteDatabase db) {
        super(db, User.TAB_NAME);
    }

    public boolean login(User user) {
        Cursor cursor = db.query(TAB_NAME, null,
                " ( number = ? or name = ? ) and password = ?",
                new String[]{String.valueOf(user.getNumber()), user.getName(), user.getPassword()},
                null, null, null);
        boolean result = cursor.moveToFirst();
        if (result) {
            user.setId(cursor.getInt(cursor.getColumnIndex("id")));
            user.setName(cursor.getString(cursor.getColumnIndex("name")));
            user.setNumber(cursor.getInt(cursor.getColumnIndex("number")));
        }
        cursor.close();
        return result;
    }

}

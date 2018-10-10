package com.hzncc.zhudao.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.hzncc.zhudao.entity.WorkLog;
import com.hzncc.zhudao.utils.DateUtil;

import java.util.ArrayList;

/**
 * ZhuDao
 * Created by 蔡雨峰 on 2017/3/29.
 */
public class WorkLogDao extends SQliteDao<WorkLog> {

    public WorkLogDao(SQLiteDatabase db) {
        super(db, WorkLog.TAB_NAME);
    }

    public ArrayList<WorkLog> querysByDate(long start, long end) {
        Cursor cursor = db.query(TAB_NAME, null, " dateTime >= ? and dateTime <= ?",
                new String[]{String.valueOf(start), String.valueOf(end)}, null, null, null);
        ArrayList<WorkLog> list = new ArrayList<WorkLog>();
        while (cursor.moveToNext()) {
            WorkLog workLog = query(cursor, new WorkLog());
            list.add(workLog);
        }
        cursor.close();
        return list;
    }

    public long createWorkLog(String event) {
        WorkLog workLog = new WorkLog();
        workLog.setDateTime(System.currentTimeMillis());
        workLog.setDateTag(DateUtil.formatUnixTime(workLog.getDateTime(), DateUtil.FORMAT_YMD));
        workLog.setLoginfo(event);
        return add(workLog);
    }

}

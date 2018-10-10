package com.hzncc.zhudao.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.hzncc.zhudao.entity.AlarmLog;

import java.util.ArrayList;
import java.util.List;

/**
 * ZhuDao
 * Created by 蔡雨峰 on 2017/3/22.
 */
public class AlarmLogDao extends SQliteDao<AlarmLog> {

    public AlarmLogDao(SQLiteDatabase db) {
        super(db, AlarmLog.TAB_NAME);
    }

    public List<AlarmLog> querysByDate(long start, long end) {
        Cursor cursor = db.query(TAB_NAME, null, " dateTime >= ? and dateTime <= ?",
                new String[]{String.valueOf(start), String.valueOf(end)}, null, null, null);
        List<AlarmLog> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            AlarmLog alarmLog = query(cursor, new AlarmLog());
            list.add(alarmLog);
        }
        cursor.close();
        return list;
    }

    public List<AlarmLog> querysAllYear() {
        Cursor cursor = db.query(TAB_NAME, null, null, null, null, null, null);
        List<AlarmLog> list = new ArrayList<>();
        List<Integer> years = new ArrayList<>();
        while (cursor.moveToNext()) {
            AlarmLog alarmLog = query(cursor, new AlarmLog());
            if (list.size() <= 0) {
                list.add(alarmLog);
                years.add(alarmLog.getYear());
            } else {
                if (!years.contains(alarmLog.getYear())) {
                    list.add(alarmLog);
                    years.add(alarmLog.getYear());
                }
            }
        }
        cursor.close();
        return list;
    }

    public List<AlarmLog> querysMonthByYear(int year) {
        Cursor cursor = db.query(TAB_NAME, null, " year = ? ",
                new String[]{String.valueOf(year)}, null, null, null);
        List<AlarmLog> list = new ArrayList<>();
        List<Integer> months = new ArrayList<>();
        while (cursor.moveToNext()) {
            AlarmLog alarmLog = query(cursor, new AlarmLog());
            if (list.size() <= 0) {
                list.add(alarmLog);
                months.add(alarmLog.getMonth());
            } else {
                if (!months.contains(alarmLog.getMonth())) {
                    list.add(alarmLog);
                    months.add(alarmLog.getMonth());
                }
            }
        }
        cursor.close();
        return list;
    }

    public List<AlarmLog> querysDayByMonth(int year, int month) {
        Cursor cursor = db.query(TAB_NAME, null, " year = ? and month = ? ",
                new String[]{String.valueOf(year), String.valueOf(month)}, null, null, null);
        List<AlarmLog> list = new ArrayList<>();
        List<Integer> days = new ArrayList<>();
        while (cursor.moveToNext()) {
            AlarmLog alarmLog = query(cursor, new AlarmLog());
            if (list.size() <= 0) {
                list.add(alarmLog);
                days.add(alarmLog.getDay());
            } else {
                if (!days.contains(alarmLog.getDay())) {
                    list.add(alarmLog);
                    days.add(alarmLog.getDay());
                }
            }
        }
        cursor.close();
        return list;
    }

    public List<AlarmLog> querysAlarmByDay(int year, int month, int day) {
        String where;
        String[] args;
        if (month <= 0) {
            where = " year = ? ";
            args = new String[]{String.valueOf(year)};
        } else if (day <= 0) {
            where = " year = ? and month = ? ";
            args = new String[]{String.valueOf(year), String.valueOf(month)};
        } else {
            where = " year = ? and month = ? and day = ? ";
            args = new String[]{String.valueOf(year), String.valueOf(month), String.valueOf(day)};
        }
        Cursor cursor = db.query(TAB_NAME, null, where, args, null, null, null);
        List<AlarmLog> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            AlarmLog alarmLog = query(cursor, new AlarmLog());
            list.add(alarmLog);
        }
        cursor.close();
        return list;
    }

}

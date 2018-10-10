package com.hzncc.observerdemo.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * ZhuDao
 * Created by 蔡雨峰 on 2017/4/6.
 */

public class TableDao<T> {

    protected SQLiteDatabase db;
    protected Context context;
    protected String TAB_NAME = "";
    protected Uri uri;

    protected TableDao(SQLiteDatabase db, String tabName) {
        this.db = db;
        this.TAB_NAME = tabName;
    }

    public static List<HashMap<String, String>> getObjectValue(Object object) {
        List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        Class<?> clz = object.getClass();
        Field[] fields = clz.getDeclaredFields();
        for (Field field : fields) {// --for() begin
            int mod = field.getModifiers();
            if (Modifier.isFinal(mod) && Modifier.isStatic(mod)) {
                continue;
            }
            HashMap<String, String> hashMap = new HashMap<String, String>();
            String type = field.getGenericType().toString();
            if (type.equals("class java.lang.String")) {
                hashMap.put("name", field.getName());
                hashMap.put("type", "TEXT");
            } else if (type.equals("class java.lang.Integer")
                    || type.equals("int")) {
                hashMap.put("name", field.getName());
                hashMap.put("type", "INTEGER");
            } else if (type.equals("class java.lang.Double")
                    || type.equals("double")) {
                hashMap.put("name", field.getName());
                hashMap.put("type", "DOUBLE");
            } else if (type.equals("class java.lang.Boolean")
                    || type.equals("boolean")) {
                hashMap.put("name", field.getName());
                hashMap.put("type", "INTEGER");
            } else if (type.equals("class java.util.Date")) {
                hashMap.put("name", field.getName());
                hashMap.put("type", "LONG");
            } else if (type.equals("class java.lang.Short")
                    || type.equals("short")) {
                hashMap.put("name", field.getName());
                hashMap.put("type", "INTEGER");
            } else if (type.equals("class java.lang.Long")
                    || type.equals("long")) {
                hashMap.put("name", field.getName());
                hashMap.put("type", "LONG");
            }
            list.add(hashMap);
        }
        return list;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
        if (null != context) {
            this.uri = Uri.parse("content://" + context.getPackageName() + "/" + TAB_NAME);
        }
    }

    protected List<HashMap<String, String>> getExistsObjectValue(Object object) {
        Cursor cursor = db.query(TAB_NAME, null, null, null, null, null, null);
        // 我们项目的所有实体类都继承BaseDomain （所有实体基类：该类只是串行化一下）
        // 不需要的自己去掉即可
        // if (object != null && object instanceof BaseDomain) {// if
        // (object!=null
        // ) ----begin
        // 拿到该类
        List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        Class<?> clz = object.getClass();
        // 获取实体类的所有属性，返回Field数组
        Field[] fields = clz.getDeclaredFields();
        for (Field field : fields) {// --for() begin
            int index = cursor.getColumnIndex(field.getName());
            if (index >= 0) {
                continue;
            }
            HashMap<String, String> hashMap = new HashMap<String, String>();
            System.out.println();// 打印该类的所有属性类型
            String type = field.getGenericType().toString();
            if (type.equals("class java.lang.String")
                    || type.equals("String")) {
                hashMap.put("name", field.getName());
                hashMap.put("type", "TEXT");
            } else if (type.equals("class java.lang.Integer")
                    || type.equals("int")) {
                hashMap.put("name", field.getName());
                hashMap.put("type", "INTEGER");
            } else if (type.equals("class java.lang.Double")
                    || type.equals("double")) {
                hashMap.put("name", field.getName());
                hashMap.put("type", "DOUBLE");
            } else if (type.equals("class java.lang.Boolean")
                    || type.equals("boolean")) {
                hashMap.put("name", field.getName());
                hashMap.put("type", "INTEGER");
            } else if (type.equals("class java.util.Date")) {
                hashMap.put("name", field.getName());
                hashMap.put("type", "LONG");
            } else if (type.equals("class java.lang.Short")
                    || type.equals("short")) {
                hashMap.put("name", field.getName());
                hashMap.put("type", "INTEGER");
            } else if (type.equals("class java.lang.Long")
                    || type.equals("long")) {
                hashMap.put("name", field.getName());
                hashMap.put("type", "LONG");
            }
            list.add(hashMap);
        }
        return list;
    }

    protected void createTable(Object object) {
        String perfix = "CREATE TABLE IF NOT EXISTS " + TAB_NAME
                + "( id INTEGER PRIMARY KEY AUTOINCREMENT ";
        String suffix = " )";
        try {
            List<HashMap<String, String>> list = getObjectValue(object);
            StringBuffer sb = new StringBuffer();
            sb.append(perfix);
            for (int i = 0; i < list.size(); i++) {
                HashMap<String, String> hashMap = list.get(i);
                if (null == hashMap || null == hashMap.get("name")) {
                    continue;
                }
                if (hashMap.get("name").equals("id")
                        || hashMap.get("name").equals("serialVersionUID")) {
                    continue;
                } else {
                    sb.append("," + hashMap.get("name"));
                    sb.append(" " + hashMap.get("type") + " ");
                }
            }

            sb.append(suffix);
            db.execSQL(sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void cleanTable() {
        db.delete(TAB_NAME, null, null);
        ContentValues cv = new ContentValues();
        cv.put("seq", 0);
        db.update("sqlite_sequence", cv, "name = ?", new String[]{TAB_NAME});
    }

    protected void deleteTable() {
        db.execSQL("DROP TABLE " + TAB_NAME + ";");
    }

    protected boolean isBaseType(String type) {
        boolean result = type.equals("class java.lang.Integer")
                || type.equals("int")
                || type.equals("class java.lang.Short")
                || type.equals("short")
                || type.equals("class java.lang.Boolean")
                || type.equals("boolean")
                || type.equals("class java.lang.Long")
                || type.equals("long")
                || type.equals("class java.lang.Double")
                || type.equals("double")
                || type.equals("class java.lang.String")
                || type.equals("String")
                || type.equals("class java.util.Date");
        if (!result) {
            Log.e("isBaseType", type);
        }
        return result;
    }

    protected void updateTable(Object object) {
        String perfix = "ALTER TABLE " + TAB_NAME + " ADD COLUMN ";
        // String suffix = " )";
        try {
            List<HashMap<String, String>> list = getExistsObjectValue(object);
            for (int i = 0; i < list.size(); i++) {
                StringBuffer sb = new StringBuffer();
                sb.append(perfix);
                HashMap<String, String> hashMap = list.get(i);
                if (null == hashMap || null == hashMap.get("name")) {
                    continue;
                }
                if (hashMap.get("name").equals("id")
                        || hashMap.get("name").equals("serialVersionUID")) {
                    continue;
                } else {
                    sb.append(hashMap.get("name"));
                    sb.append(" " + hashMap.get("type"));
                }
                // sb.append(suffix);
                db.execSQL(sb.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // // 把一个字符串的第一个字母大写、效率是最高的、
    // private static String getMethodName(String fildeName) throws Exception {
    // byte[] items = fildeName.getBytes();
    // items[0] = (byte) ((char) items[0] - 'a' + 'A');
    // return new String(items);
    // }
}

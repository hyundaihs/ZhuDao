package com.hzncc.observerdemo.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class SQliteDao<T> extends TableDao {

    public SQliteDao(SQLiteDatabase db, String tabName) {
        super(db, tabName);
    }

    public void adds(List<T> objs) {
        for (int i = 0; i < objs.size(); i++) {
            add(objs.get(i));
        }
        notifyChange();
    }

    public void updates(List<T> objs) {
        for (int i = 0; i < objs.size(); i++) {
            update(objs.get(i));
        }
        notifyChange();
    }

    public void deletes(List<T> objs) {
        for (int i = 0; i < objs.size(); i++) {
            delete(objs.get(i));
        }
        notifyChange();
    }

    public List<T> querys(T object) {
        Cursor cursor = db.query(TAB_NAME, null, null, null, null, null, null);
        ArrayList<T> list = new ArrayList<T>();
        while (cursor.moveToNext()) {
            T t = (T) query(cursor, object);
            list.add(t);
        }
        cursor.close();
        return list;
    }

    public long add(T object) {
        ContentValues cv = new ContentValues();
        // 得到类对象
        Class userCla = (Class) object.getClass();
        /*
         * 得到类中的所有属性集合
		 */
        Field[] fs = userCla.getDeclaredFields();
        for (Field f : fs) {
            f.setAccessible(true); // 设置些属性是可以访问的
            Object val;
            try {
                val = f.get(object);// 得到此属性的值
                String type = f.getType().toString();// 得到此属性的类型
                String name = f.getName();
                int mod = f.getModifiers();
                if (Modifier.isFinal(mod) && Modifier.isStatic(mod)) {
                    continue;
                }
                if (name.equals("id") || name.equals("serialVersionUID") || name.equals("CREATOR")) {
                    continue;
                }
                if (!isBaseType(type)) {
                    continue;
                }
                cv.put(name, String.valueOf(val));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
        long result = db.insert(TAB_NAME, null, cv);
        notifyChange();
        return result;
    }

    public int delete(T obj) {
        // 得到类对象
        Class userCla = (Class) obj.getClass();
        /*
         * 得到类中的所有属性集合
		 */
        Field[] fs = userCla.getDeclaredFields();
        String value = null;
        for (Field f : fs) {
            f.setAccessible(true); // 设置些属性是可以访问的
            Object val;
            try {
                val = f.get(obj);// 得到此属性的值
                String type = f.getType().toString();// 得到此属性的类型
                String name = f.getName();
                if (name.equals("id")) {
                    value = String.valueOf(val);
                    break;
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
        if (null == value) {
            return -1;
        }
        int result = db.delete(TAB_NAME, " id = ?", new String[]{value});
        notifyChange();
        return result;
    }

    public int update(T object) {
        ContentValues cv = new ContentValues();
        // 得到类对象
        Class userCla = (Class) object.getClass();
        /*
         * 得到类中的所有属性集合
		 */
        Field[] fs = userCla.getDeclaredFields();
        String value = null;
        for (Field f : fs) {
            f.setAccessible(true); // 设置些属性是可以访问的
            Object val;
            try {
                val = f.get(object);// 得到此属性的值
                String type = f.getType().toString();// 得到此属性的类型
                String name = f.getName();
                if (name.equals("id")) {
                    value = String.valueOf(val);
                }
                int mod = f.getModifiers();
                if (Modifier.isFinal(mod) && Modifier.isStatic(mod)) {
                    continue;
                }
                if (name.equals("serialVersionUID") || name.equals("CREATOR")) {
                    continue;
                }
                if (!isBaseType(type)) {
                    continue;
                }
                cv.put(name, String.valueOf(val));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
        if (null == value) {
            return -1;
        }
        int result = db.update(TAB_NAME, cv, " id = ?", new String[]{value});
        notifyChange();
        return result;
    }

    public T query(Cursor cursor, T object) {
        // 得到类对象
        Class userCla = (Class) object.getClass();
        /*
         * 得到类中的所有属性集合
		 */
        Field[] fs = userCla.getDeclaredFields();
        for (Field f : fs) {
            f.setAccessible(true); // 设置些属性是可以访问的
            try {
                // Object val = f.get(object);// 得到此属性的值
                String name = f.getName();
                int mod = f.getModifiers();
                if (Modifier.isFinal(mod) && Modifier.isStatic(mod)) {
                    continue;
                }
                if (name.equals("serialVersionUID") || name.equals("CREATOR")) {
                    continue;
                }
                int index = cursor.getColumnIndex(name);
                if (index < 0) {
                    continue;
                }
                String type = f.getType().toString();// 得到此属性的类型
                Object value = null;
                if (type.equals("class java.lang.String")) {
                    value = cursor.getString(index);
                } else if (type.equals("class java.lang.Integer")
                        || type.equals("int")) {
                    value = cursor.getInt(index);
                } else if (type.equals("class java.lang.Double")
                        || type.equals("double")) {
                    value = cursor.getDouble(index);
                } else if (type.equals("class java.lang.Boolean")
                        || type.equals("boolean")) {
                    value = cursor.getInt(index) == 1;
                } else if (type.equals("class java.util.Date")) {
                    value = cursor.getLong(index);
                } else if (type.equals("class java.lang.Short")
                        || type.equals("short")) {
                    value = cursor.getShort(index);
                } else if (type.equals("class java.lang.Long")
                        || type.equals("long")) {
                    value = cursor.getLong(index);
                }
                f.set(object, value);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
        return object;
    }

    protected void notifyChange() {
        if (null != context) {
            context.getContentResolver().notifyChange(uri, null);
        }
    }

    public boolean registerObserver(Context context, boolean notifyForDescendants,
                                       ContentObserver observer) {
        setContext(context);
        if (null == context || null == uri) {
            return false;
        }
        context.getContentResolver().registerContentObserver(uri, false, observer);
        return true;
    }

    public boolean unregisterObserver(ContentObserver observer) {
        if (null == context || null == observer) {
            return false;
        }
        context.getContentResolver().unregisterContentObserver(observer);
        return true;
    }


}

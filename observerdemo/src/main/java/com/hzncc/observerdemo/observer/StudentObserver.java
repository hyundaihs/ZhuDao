package com.hzncc.observerdemo.observer;

import android.content.Context;
import android.database.ContentObserver;
import android.os.Handler;
import android.util.Log;

/**
 * ZhuDao
 * Created by 蔡雨峰 on 2017/5/3.
 */

public class StudentObserver extends ContentObserver {

    private Context mContext;
    private Handler mHandler;   //更新UI线程
    private int i = 0;

    /**
     * Creates a content observer.
     *
     * @param handler The handler to run {@link #onChange} on, or null if none.
     */
    public StudentObserver(Context context, Handler handler) {
        super(handler);
        mContext = context;
        mHandler = handler;
    }

    @Override
    public void onChange(boolean selfChange) {
        Log.d("StudentObserver", "onChange = " + selfChange);
        mHandler.obtainMessage(i++).sendToTarget();
    }
}

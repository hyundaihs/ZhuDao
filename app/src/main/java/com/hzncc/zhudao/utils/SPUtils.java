package com.hzncc.zhudao.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SPUtils {
    private SharedPreferences sp;
    private Editor editor;

    public SPUtils(Context context) {
        if (null == sp) {
            sp = context.getSharedPreferences("hyl026", Context.MODE_PRIVATE);
        }
        editor = sp.edit();
        editor.commit();
    }

    public int getTempcorrect() {
        return sp.getInt("tempcorrect", -180);
    }

    public void setTempcorrect(int tempcorrect) {
        editor.putInt("tempcorrect", tempcorrect);
        editor.commit();
    }

    public int getOption(int index) {
        return sp.getInt("option" + index, 1);
    }

    public void setOption(int index, int select) {
        editor.putInt("option" + index, select);
        editor.commit();
    }

    public String getCheckName() {
        return sp.getString("check_name", "联轴器");
    }

    public void setCheckName(String checkName) {
        editor.putString("check_name", checkName);
        editor.commit();
    }


}

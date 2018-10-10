package com.hzncc.kevin.excel2sqlite;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.support.v4.media.RatingCompat;
import android.view.ViewGroup;
import android.widget.ProgressBar;

/**
 * ZhuDao
 * Created by 蔡雨峰 on 2017/3/20.
 */
public class DialogUtil {

    public static Dialog showSeriMessageDialog(Activity context) {
        final Dialog dialog = new Dialog(context);
        ProgressBar progressBar = new ProgressBar(context);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(300, 50);
        progressBar.setLayoutParams(params);
        dialog.show();
        dialog.setContentView(progressBar);
        return dialog;
    }

}

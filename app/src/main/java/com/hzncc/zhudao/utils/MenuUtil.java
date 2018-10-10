package com.hzncc.zhudao.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.hzncc.zhudao.ZDApplication;
import com.hzncc.zhudao.entity.AppPath;
import com.hzncc.zhudao.ui.CleanActivity;
import com.hzncc.zhudao.ui.GroupEditActivity;
import com.hzncc.zhudao.ui.ShareActivity;
import com.hzncc.zhudao.ui.WorkLogActivity;

import static com.hzncc.zhudao.Constants.PALETTES;

/**
 * ZhuDao
 * Created by 蔡雨峰 on 2017/6/21.
 */

public class MenuUtil {
    public static void actionLeft(Context context, int position) {
        switch (position) {
            case 0://软件介绍
                Log.d("OnItem", "软件介绍");
                break;
            case 1://信息分享
//                DialogUtil.showWifiDialog((Activity) context);
                context.startActivity(new Intent(context, ShareActivity.class));
                break;
            case 2://设置
                DialogUtil.showSettingDialog((Activity) context, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                break;
            case 3://工作日志
                context.startActivity(new Intent(context, WorkLogActivity.class));
                break;
            case 4://文件管理
                context.startActivity(new Intent(context, CleanActivity.class));
                break;
            case 5://校正
                DialogUtil.showCorrentDialog((Activity) context);
                break;
            case 6://重连
                IRCamera.reopen();
                IRCamera.tempCorrent(new SPUtils(context).getTempcorrect());
                break;
            case 7:
                context.startActivity(new Intent(context, GroupEditActivity.class));
                break;
//            case 8://中心温度
//                DialogUtil.showExitDialog((Activity) context, new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                    }
//                });
//                break;
        }
    }

    public static void actionRight(Context context, ImageView colorLine, int position) {
        IRCamera.setColorsName(position);
        colorLine.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), PALETTES[position]));
    }
}

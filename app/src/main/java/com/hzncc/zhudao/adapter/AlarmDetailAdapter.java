package com.hzncc.zhudao.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hzncc.zhudao.R;
import com.hzncc.zhudao.entity.AlarmLog;
import com.hzncc.zhudao.utils.NumUtil;
import com.hzncc.zhudao.utils.RequestUtil;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/3/27.
 */
public class AlarmDetailAdapter extends PagerAdapter {
    private Context context;
    private ArrayList<AlarmLog> data;
    private View[] views;
    private RequestUtil requestUtil;
    private boolean isChanged = false;

    public AlarmDetailAdapter(Context context, ArrayList<AlarmLog> data) {
        this.context = context;
        this.data = data;
        views = new View[data.size()];
        requestUtil = new RequestUtil(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(views[position]);//删除页卡
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {  //这个方法用来实例化页卡
        ViewHolder viewHolder;
        if (null == views[position]) {
            views[position] = LayoutInflater.from(context).inflate(R.layout.layout_alarm_detail_item, null, false);
            viewHolder = new ViewHolder();
            viewHolder.vl = (ImageView) views[position].findViewById(R.id.alarm_detail_item_vl);
            viewHolder.ir = (ImageView) views[position].findViewById(R.id.alarm_detail_item_ir);
            viewHolder.time = (TextView) views[position].findViewById(R.id.alarm_detail_item_time);
            viewHolder.wheel = (TextView) views[position].findViewById(R.id.alarm_detail_item_wheel);
            viewHolder.cenTemp = (TextView) views[position].findViewById(R.id.alarm_detail_item_centemp);
            viewHolder.temps[0] = (TextView) views[position].findViewById(R.id.alarm_detail_item_temp1);
            viewHolder.temps[1] = (TextView) views[position].findViewById(R.id.alarm_detail_item_temp2);
            viewHolder.temps[2] = (TextView) views[position].findViewById(R.id.alarm_detail_item_temp3);
            views[position].setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) views[position].getTag();
        }
        AlarmLog alarmLog = data.get(position);
        viewHolder.vl.setImageBitmap(BitmapFactory.decodeFile(alarmLog.getVlPath()));
        viewHolder.ir.setImageBitmap(BitmapFactory.decodeFile(alarmLog.getIrPath()));
        viewHolder.time.setText("检测时间：" + alarmLog.getDateTag());
        viewHolder.wheel.setText("检测轮对编组：" + alarmLog.getWheelNum() + "#");
        viewHolder.cenTemp.setText(NumUtil.formatDouble(alarmLog.getCenTemp(), 2) + "℃");
        viewHolder.temps[0].setVisibility(alarmLog.getAlarmTemp1() <= 0 ? View.GONE : View.VISIBLE);
        viewHolder.temps[1].setVisibility(alarmLog.getAlarmTemp2() <= 0 ? View.GONE : View.VISIBLE);
        viewHolder.temps[2].setVisibility(alarmLog.getAlarmTemp3() <= 0 ? View.GONE : View.VISIBLE);
        viewHolder.temps[0].setText(NumUtil.formatDouble(alarmLog.getAlarmTemp1(), 2) + "℃");
        viewHolder.temps[1].setText(NumUtil.formatDouble(alarmLog.getAlarmTemp2(), 2) + "℃");
        viewHolder.temps[2].setText(NumUtil.formatDouble(alarmLog.getAlarmTemp3(), 2) + "℃");
//        requestUtil.getLocalImage(data.get(position).getVlPath(), new MyImageCallback(viewHolder.vl));
//        requestUtil.getLocalImage(data.get(position).getIrPath(), new MyImageCallback(viewHolder.ir));
        container.addView(views[position], 0);//添加页卡
        return views[position];
    }

    @Override
    public int getCount() {
        return data.size();//返回页卡的数量
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;//官方提示这样写
    }

    @Override
    public void notifyDataSetChanged() {
        isChanged = true;
        super.notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(Object object) {
        return isChanged ? POSITION_NONE : super.getItemPosition(object);
    }

    private class ViewHolder {
        ImageView vl, ir;
        TextView time, wheel;
        TextView cenTemp;
        TextView[] temps = new TextView[3];
    }
}

package com.hzncc.zhudao.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.hzncc.zhudao.R;
import com.hzncc.zhudao.ZDApplication;
import com.hzncc.zhudao.adapter.AlarmDetailAdapter;
import com.hzncc.zhudao.db.AlarmLogDao;
import com.hzncc.zhudao.entity.AlarmLog;
import com.hzncc.zhudao.receivers.BroadCastManager;

import java.util.ArrayList;

/**
 * ZhuDao
 * Created by 蔡雨峰 on 2017/3/27.
 */
public class AlarmDetailActivity extends Activity implements View.OnClickListener {

    private ViewPager viewPager;
    private ArrayList<AlarmLog> data;
    private AlarmDetailAdapter adapter;
    private TextView current;
    private int select;
    private AlarmLogDao alarmLogDao;
    private BroadCastManager broadCastManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_detail);
        data = getIntent().getParcelableArrayListExtra("data");
        select = getIntent().getIntExtra("index", 0);
        initViews();
        initData();
        setResult(1);
        broadCastManager.registerBattery(this);
    }

    @Override
    protected void onDestroy() {
        broadCastManager.unregisterBattery(this);
        super.onDestroy();
    }

    private void initViews() {
        viewPager = (ViewPager) findViewById(R.id.activity_alarm_detail_viewPager);
        current = (TextView) findViewById(R.id.activity_alarm_detail_current);
        ImageButton delete = (ImageButton) findViewById(R.id.activity_alarm_detail_delete);
        ImageButton close = (ImageButton) findViewById(R.id.activity_alarm_detail_close);
        delete.setOnClickListener(this);
        close.setOnClickListener(this);
        broadCastManager = new BroadCastManager();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_alarm_detail_delete:
                if (alarmLogDao.delete(data.get(select)) == 1) {
                    data.remove(select);
                    adapter.notifyDataSetChanged();
                    if (select >= data.size()) {
                        select = data.size() - 1;
                    }
                    current.setText((select + 1) + "/" + data.size());
                }
                break;
            case R.id.activity_alarm_detail_close:
                finish();
                break;
        }
    }

    private void initData() {
        alarmLogDao = new AlarmLogDao(ZDApplication.dbManager.getDb());
        adapter = new AlarmDetailAdapter(this, data);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(select);
        current.setText((select + 1) + "/" + data.size());
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                select = position;
                current.setText((position + 1) + "/" + data.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}

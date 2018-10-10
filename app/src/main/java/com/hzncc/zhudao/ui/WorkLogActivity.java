package com.hzncc.zhudao.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.hzncc.zhudao.R;
import com.hzncc.zhudao.ZDApplication;
import com.hzncc.zhudao.adapter.DividerItemDecoration;
import com.hzncc.zhudao.adapter.TitleItemDecoration;
import com.hzncc.zhudao.adapter.WorkLogAdapter;
import com.hzncc.zhudao.db.WorkLogDao;
import com.hzncc.zhudao.entity.WorkLog;
import com.hzncc.zhudao.receivers.BroadCastManager;
import com.hzncc.zhudao.utils.DialogUtil;
import com.hzncc.zhudao.widget.BatteryView;

import java.util.ArrayList;

/**
 * ZhuDao
 * Created by 蔡雨峰 on 2017/3/29.
 */
public class WorkLogActivity extends Activity implements View.OnClickListener {

    private ImageButton delete;
    private TextView done;
    private BatteryView batteryView;
    private RecyclerView recyclerView;
    private ArrayList<WorkLog> data;
    private WorkLogAdapter myAdapter;
    private WorkLogDao workLogDao;
    private BroadCastManager broadCastManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worklog);
        workLogDao = new WorkLogDao(ZDApplication.dbManager.getDb());
        initViews();
        initData();
        broadCastManager.registerBattery(this);
    }


    @Override
    protected void onDestroy() {
        broadCastManager.unregisterBattery(this);
        super.onDestroy();
    }

    private void initViews() {
        recyclerView = (RecyclerView) findViewById(R.id.activity_worklog_recycler);
        batteryView = (BatteryView) findViewById(R.id.activity_worklog_battery);
        ImageButton dateChooser = (ImageButton) findViewById(R.id.activity_worklog_date_chooser);
        done = (TextView) findViewById(R.id.activity_worklog_done);
        delete = (ImageButton) findViewById(R.id.activity_worklog_delete);
        ImageButton close = (ImageButton) findViewById(R.id.activity_worklog_exit);
        dateChooser.setOnClickListener(this);
        done.setOnClickListener(this);
        delete.setOnClickListener(this);
        close.setOnClickListener(this);
        broadCastManager = new BroadCastManager(batteryView);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_worklog_date_chooser:
                DialogUtil.showDateChooser(this, new DialogUtil.OnDateChoosedListener() {
                    @Override
                    public void onDateChoosed(long start, long end) {
                        data.clear();
                        data.addAll(workLogDao.querysByDate(start, end));
                        myAdapter.notifyDataSetChanged();
                    }
                });
                break;
            case R.id.activity_worklog_done:
                myAdapter.cleanSelected();
                done.setVisibility(View.GONE);
                delete.setVisibility(View.GONE);
                break;
            case R.id.activity_worklog_delete:
                DialogUtil.showAskDialog(this, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ArrayList<WorkLog> list = myAdapter.getSelected();
                        for (int i = 0; i < list.size(); i++) {
                            if (workLogDao.delete(list.get(i)) == 1) {
                                delete(list.get(i));
                            }
                        }
                        myAdapter.cleanSelected();
                        done.setVisibility(View.GONE);
                        delete.setVisibility(View.GONE);
                    }
                }, "确定删除选中日志吗？");
                break;
            case R.id.activity_worklog_exit:
                finish();
                break;
        }
    }

    private void initData() {
        data = workLogDao.querysByDate(DialogUtil.startDate, DialogUtil.endDate);
        myAdapter = new WorkLogAdapter(this, data);
        final LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.addItemDecoration(new TitleItemDecoration(this, data));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        recyclerView.setAdapter(myAdapter);
        myAdapter.setOnItemClickListener(new WorkLogAdapter.OnRecycleItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
//                Intent intent = new Intent();
//                intent.putParcelableArrayListExtra("data", data);
//                intent.putExtra("index", position);
//                intent.setClass(WorkLogActivity.this, AlarmDetailActivity.class);
//                startActivityForResult(intent, 0);
            }

            @Override
            public void onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                done.setVisibility(View.VISIBLE);
                delete.setVisibility(View.VISIBLE);
            }
        });
    }

    private void delete(WorkLog workLog) {
        this.data.remove(workLog);
        myAdapter.notifyDataSetChanged();
    }
}

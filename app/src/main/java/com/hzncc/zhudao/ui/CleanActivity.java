package com.hzncc.zhudao.ui;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.hzncc.zhudao.R;
import com.hzncc.zhudao.ZDApplication;
import com.hzncc.zhudao.adapter.RecyclerGridCleanAdapter;
import com.hzncc.zhudao.db.AlarmLogDao;
import com.hzncc.zhudao.entity.AlarmLog;
import com.hzncc.zhudao.receivers.BroadCastManager;
import com.hzncc.zhudao.utils.DialogUtil;
import com.hzncc.zhudao.widget.BatteryView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.hzncc.zhudao.ui.CleanActivity.Level.DAY;
import static com.hzncc.zhudao.ui.CleanActivity.Level.MONTH;
import static com.hzncc.zhudao.ui.CleanActivity.Level.ONCE;
import static com.hzncc.zhudao.ui.CleanActivity.Level.YEAR;

/**
 * ZhuDao
 * Created by 蔡雨峰 on 2017/5/9.
 */

public class CleanActivity extends Activity implements View.OnClickListener {
    private ImageButton upstep, back, delete;
    private TextView navigate;
    private BatteryView batteryView;
    private Button done;
    private RecyclerView recyclerView;
    private int year = -1, month = -1, day = -1;
    private RecyclerGridCleanAdapter myAdapter;
    private List<AlarmLog> data;
    private AlarmLogDao alarmLogDao;
    private Level level = YEAR;
    private DialogUtil.ForcedWait forcedWait;
    private BroadCastManager broadCastManager;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            forcedWait.setProgress(msg.what);
            if (forcedWait.isMax(msg.what)) {
                myAdapter.notifyDataSetChanged();
                exitSelectable();
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clean);
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
        back = (ImageButton) findViewById(R.id.activity_clean_back);
        upstep = (ImageButton) findViewById(R.id.activity_clean_upstep);
        delete = (ImageButton) findViewById(R.id.activity_clean_delete);
        batteryView = (BatteryView) findViewById(R.id.activity_clean_battery);
        done = (Button) findViewById(R.id.activity_clean_done);
        recyclerView = (RecyclerView) findViewById(R.id.activity_clean_list);
        navigate = (TextView) findViewById(R.id.activity_clean_navigate);
        back.setOnClickListener(this);
        upstep.setOnClickListener(this);
        delete.setOnClickListener(this);
        done.setOnClickListener(this);
        broadCastManager = new BroadCastManager(batteryView);
    }

    private void getData() {
        if (year < 0) {
            level = YEAR;
        } else if (month < 0) {
            level = MONTH;
        } else if (day < 0) {
            level = DAY;
        } else {
            level = ONCE;
        }
        myAdapter.setLevel(level);
        data.clear();
        switch (level) {
            case YEAR:
                data.addAll(alarmLogDao.querysAllYear());
                navigate.setText("所有日志");
                break;
            case MONTH:
                data.addAll(alarmLogDao.querysMonthByYear(year));
                navigate.setText(year + " 日志");
                break;
            case DAY:
                data.addAll(alarmLogDao.querysDayByMonth(year, month));
                navigate.setText(year + "-" + month + " 日志");
                break;
            case ONCE:
                data.addAll(alarmLogDao.querysAlarmByDay(year, month, day));
                navigate.setText(year + "-" + month + "-" + day + " 日志");
                break;
        }
        myAdapter.notifyDataSetChanged();
    }

    private void initData() {
        alarmLogDao = new AlarmLogDao(ZDApplication.dbManager.getDb());
        data = new ArrayList<>();
        myAdapter = new RecyclerGridCleanAdapter(this, data);
        final GridLayoutManager manager = new GridLayoutManager(this, 5);
        recyclerView.setLayoutManager(manager);
//        recyclerView.addItemDecoration(new CleanItemDecoration(this));
        recyclerView.setAdapter(myAdapter);
        myAdapter.setOnItemClickListener(new RecyclerGridCleanAdapter.OnRecycleItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                AlarmLog alarmLog = myAdapter.getItem(position);
                switch (level) {
                    case YEAR:
                        year = alarmLog.getYear();
                        break;
                    case MONTH:
                        month = alarmLog.getMonth();
                        break;
                    case DAY:
                        day = alarmLog.getDay();
                        break;
                    case ONCE:
                        break;
                }
                getData();
            }

            @Override
            public void onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                done.setVisibility(View.VISIBLE);
                delete.setVisibility(View.VISIBLE);
            }
        });
        getData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_clean_upstep:
                switch (level) {
                    case YEAR:
                        break;
                    case MONTH:
                        year = -1;
                        break;
                    case DAY:
                        month = -1;
                        break;
                    case ONCE:
                        day = -1;
                        break;
                }
                getData();
                break;
            case R.id.activity_clean_back:
                finish();
                break;
            case R.id.activity_clean_delete:
                DialogUtil.showAskDialog(this, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        List<AlarmLog> list = myAdapter.getSelected();
                        if (list.size() <= 0) {
                            return;
                        }
                        List<AlarmLog> deletes = new ArrayList<>();
                        for (AlarmLog alarmLog : list) {
                            switch (level) {
                                case YEAR:
                                    deletes.addAll(alarmLogDao.querysAlarmByDay(alarmLog.getYear(), 0, 0));
                                    break;
                                case MONTH:
                                    deletes.addAll(alarmLogDao.querysAlarmByDay(alarmLog.getYear(),
                                            alarmLog.getMonth(), 0));
                                    break;
                                case DAY:
                                    deletes.addAll(alarmLogDao.querysAlarmByDay(alarmLog.getYear(),
                                            alarmLog.getMonth(), alarmLog.getDay()));
                                    break;
                                case ONCE:
                                    deletes.add(alarmLog);
                                    break;
                            }
                        }
                        forcedWait = DialogUtil.showForcedWaitDialog(
                                CleanActivity.this, deletes.size());
                        new Thread(new DeleteRunnable(deletes)).start();

                    }
                }, "确定删除选中日志吗？");
                break;
            case R.id.activity_clean_done:
                exitSelectable();
                break;
        }
    }

    private void exitSelectable() {
        myAdapter.cleanSelected();
        done.setVisibility(View.GONE);
        delete.setVisibility(View.GONE);
    }

    private void delete(AlarmLog alarmLog) {
        this.data.remove(alarmLog);
    }

    private void delete(String path) {
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
    }

    public enum Level {
        YEAR, MONTH, DAY, ONCE
    }

    private class DeleteRunnable implements Runnable {

        List<AlarmLog> list;

        DeleteRunnable(List<AlarmLog> list) {
            this.list = list;
        }

        @Override
        public void run() {
            for (int i = 0; i < list.size(); i++) {
                AlarmLog alarmLog = list.get(i);
                if (alarmLogDao.delete(alarmLog) == 1) {
                    delete(alarmLog.getVlPath());
                    delete(alarmLog.getIrPath());
                    delete(alarmLog.getVlVideoPath());
                    delete(alarmLog.getIrVideoPath());
                    delete(alarmLog);
                    Message msg = handler.obtainMessage(i + 1);
                    msg.sendToTarget();
                }
            }
        }
    }
}

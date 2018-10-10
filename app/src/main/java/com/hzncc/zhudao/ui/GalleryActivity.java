package com.hzncc.zhudao.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseIntArray;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.hzncc.zhudao.R;
import com.hzncc.zhudao.ZDApplication;
import com.hzncc.zhudao.adapter.GalleryItemDecoration;
import com.hzncc.zhudao.adapter.RecyclerGridAdapter;
import com.hzncc.zhudao.db.AlarmLogDao;
import com.hzncc.zhudao.entity.AlarmLog;
import com.hzncc.zhudao.receivers.BroadCastManager;
import com.hzncc.zhudao.utils.DialogUtil;
import com.hzncc.zhudao.utils.DialogUtil.OnDateChoosedListener;
import com.hzncc.zhudao.widget.BatteryView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * ZhuDao
 * Created by 蔡雨峰 on 2017/3/20.
 */
public class GalleryActivity extends Activity implements View.OnClickListener {

    private ImageButton delete;
    private TextView done;
    private RecyclerView recyclerView;
    private List<AlarmLog> data;
    private RecyclerGridAdapter myAdapter;
    private AlarmLogDao alarmLogDao;
    private BroadCastManager broadCastManager;

    private DialogUtil.ForcedWait forcedWait;
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        alarmLogDao = new AlarmLogDao(ZDApplication.dbManager.getDb());
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
        recyclerView = (RecyclerView) findViewById(R.id.activity_gallery_recycler);
        BatteryView batteryView = (BatteryView) findViewById(R.id.activity_gallery_battery);
        ImageButton dateChooser = (ImageButton) findViewById(R.id.activity_gallery_date_chooser);
        done = (TextView) findViewById(R.id.activity_gallery_done);
        delete = (ImageButton) findViewById(R.id.activity_gallery_delete);
        ImageButton close = (ImageButton) findViewById(R.id.activity_gallery_exit);
        dateChooser.setOnClickListener(this);
        done.setOnClickListener(this);
        delete.setOnClickListener(this);
        close.setOnClickListener(this);
        broadCastManager = new BroadCastManager(batteryView);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_gallery_date_chooser:
                DialogUtil.showDateChooser(this, new OnDateChoosedListener() {
                    @Override
                    public void onDateChoosed(long start, long end) {
                        data.clear();
                        data.addAll(alarmLogDao.querysByDate(start, end));
                        myAdapter.notifyDataSetChanged();
                    }
                });
                break;
            case R.id.activity_gallery_done:
                exitSelectable();
                break;
            case R.id.activity_gallery_delete:
                DialogUtil.showAskDialog(this, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ArrayList<AlarmLog> list = myAdapter.getSelected();
                        forcedWait = DialogUtil.showForcedWaitDialog(
                                GalleryActivity.this, list.size());
                        new Thread(new DeleteRunnable(list)).start();
                    }
                }, "确定删除选中日志吗？");
                break;
            case R.id.activity_gallery_exit:
                finish();
                break;
        }
    }

    private void exitSelectable() {
        myAdapter.cleanSelected();
        done.setVisibility(View.GONE);
        delete.setVisibility(View.GONE);
    }

    private void delete(String path) {
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
    }

    private void initData() {
        data = alarmLogDao.querysByDate(DialogUtil.startDate, DialogUtil.endDate);
        myAdapter = new RecyclerGridAdapter(this, data);
        final GridLayoutManager manager = new GridLayoutManager(this, 2);
        manager.setSpanSizeLookup(new MySpanSizeLookup(2));
        recyclerView.setLayoutManager(manager);
        recyclerView.addItemDecoration(new GalleryItemDecoration(this, data));
        recyclerView.setAdapter(myAdapter);
        myAdapter.setOnItemClickListener(new RecyclerGridAdapter.OnRecycleItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent = new Intent();
                intent.putParcelableArrayListExtra("data", (ArrayList<? extends Parcelable>) data);
                intent.putExtra("index", position);
                intent.setClass(GalleryActivity.this, AlarmDetailActivity.class);
                startActivityForResult(intent, 0);
            }

            @Override
            public void onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                done.setVisibility(View.VISIBLE);
                delete.setVisibility(View.VISIBLE);
            }
        });
    }

    private void delete(AlarmLog alarmLog) {
        this.data.remove(alarmLog);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.data.clear();
        this.data.addAll(alarmLogDao.querysByDate(DialogUtil.startDate, DialogUtil.endDate));
        myAdapter.notifyDataSetChanged();
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

    private class MySpanSizeLookup extends GridLayoutManager.SpanSizeLookup {

        private SparseIntArray array = new SparseIntArray();
        private int spanCount;

        MySpanSizeLookup(int spanCount) {
            this.spanCount = spanCount;
        }

        private int getAll(int position) {
            int count = 0;
            for (int i = 0; i < array.size(); i++) {
                if (i >= position) {
                    continue;
                }
                count += array.get(i);
            }
            return count;
        }

        @Override
        public int getSpanSize(int position) {
            if (position < 0 || position >= myAdapter.getItemCount()) {
                return 1;
            }
            if (position == myAdapter.getItemCount() - 1) {
                array.put(position, 1);
                return 1;
            }
            if (null != data.get(position).getDateTag() &&
                    !(data.get(position).getDateTag().equals(data.get(position + 1).getDateTag()))) {
                int size = (getAll(position) + 1) % spanCount;
                if (size == 0) {
                    array.put(position, 1);
                    return 1;
                }
                size = spanCount - size + 1;
                array.put(position, size);
                return size;
            } else {
                array.put(position, 1);
                return 1;
            }
        }
    }
}

package com.hzncc.zhudao.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.hzncc.zhudao.R;
import com.hzncc.zhudao.ZDApplication;
import com.hzncc.zhudao.adapter.DividerItemDecoration;
import com.hzncc.zhudao.adapter.GroupAdapter;
import com.hzncc.zhudao.db.GroupDao;
import com.hzncc.zhudao.entity.AppPath;
import com.hzncc.zhudao.entity.Group;
import com.hzncc.zhudao.receivers.BroadCastManager;
import com.hzncc.zhudao.utils.DialogUtil;
import com.hzncc.zhudao.utils.ExcelUtil;
import com.hzncc.zhudao.utils.ToastUtil;
import com.hzncc.zhudao.widget.BatteryView;

import java.io.File;
import java.util.List;

/**
 * ZhuDao
 * Created by 蔡雨峰 on 2017/12/15.
 */

public class GroupEditActivity extends Activity implements View.OnClickListener {

    private TextView importExcel;
    private BatteryView batteryView;
    private RecyclerView recyclerView;
    private List<Group> data;
    private GroupAdapter myAdapter;
    private GroupDao groupDao;
    private BroadCastManager broadCastManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_edit);
        groupDao = new GroupDao(ZDApplication.dbManager.getDb());
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
        recyclerView = findViewById(R.id.activity_group_edit_recycler);
        batteryView = findViewById(R.id.activity_group_edit_battery);
        importExcel = findViewById(R.id.activity_group_import_excel);
        importExcel.setOnClickListener(this);
        findViewById(R.id.activity_group_exit).setOnClickListener(this);
        findViewById(R.id.activity_group_add).setOnClickListener(this);
        broadCastManager = new BroadCastManager(batteryView);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_group_exit:
                finish();
                break;
            case R.id.activity_group_add:
                DialogUtil.showAddGroupDialog(GroupEditActivity.this, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Group group = new Group();
                        group.setGroup_name((String) v.getTag());
                        long id = groupDao.add(group);
                        if (id > 0) {
                            group.setId(id);
                            data.add(group);
                            myAdapter.notifyDataSetChanged();
                        }
                    }
                });
                break;
            case R.id.activity_group_import_excel:
                ExcelUtil excelUtil = new ExcelUtil();
                excelUtil.openFile(AppPath.EXCEL + "1.xls");
                excelUtil.save(ZDApplication.dbManager.getDb(), new ExcelUtil.OnDataConvertListener() {
                    @Override
                    public void onStart(int count) {
                        ToastUtil.showShort(GroupEditActivity.this, "后台加载Excel文件");
                    }

                    @Override
                    public void onConverting(int count, int position) {
                    }

                    @Override
                    public void onCompleted(int count) {
                        ToastUtil.showShort(GroupEditActivity.this, "Excel文件加载完成");
                        List<Group> list = groupDao.querys(Group.class);
                        data.clear();
                        data.addAll(list);
                        myAdapter.notifyDataSetChanged();
                        DialogUtil.showAskDialog(GroupEditActivity.this, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                File file = new File(AppPath.EXCEL + "1.xls");
                                if (file.exists()) {
                                    if (!file.delete()) {
                                        ToastUtil.showShort(GroupEditActivity.this, "Excel文件删除失败,请手动删除");
                                    }
                                }
                            }
                        }, "是否删除本地Excel文件");
                    }

                    @Override
                    public void onError(int count, int position, String error) {
                        ToastUtil.showShort(GroupEditActivity.this, error);
                    }
                });
                break;
        }
    }

    private void initData() {
        data = groupDao.querys(Group.class);
        myAdapter = new GroupAdapter(this, data);
        final LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        recyclerView.setAdapter(myAdapter);
        myAdapter.setOnRecycleItemDeleteListener(new GroupAdapter.OnRecycleItemDeleteListener() {
            @Override
            public void onItemDelete(View view, final int position) {
                DialogUtil.showAskDialog(GroupEditActivity.this, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (groupDao.delete(data.get(position)) == 1) {
                            data.remove(position);
                            myAdapter.notifyDataSetChanged();
                        }
                    }
                }, "确定要删除当前项吗?");
            }
        });
    }


}

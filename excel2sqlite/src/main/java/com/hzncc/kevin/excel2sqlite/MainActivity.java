package com.hzncc.kevin.excel2sqlite;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yanzhenjie.permission.PermissionListener;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String SDCARD = Environment.getExternalStorageDirectory().getPath();
    public static String ROOT = SDCARD + "/zzexcel";
    TextView textView;
    Dialog dialog;
    private DBManager dbManager;
    private PermissionListener listener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode, List<String> grantedPermissions) {
            // 权限申请成功回调。

            // 这里的requestCode就是申请时设置的requestCode。
            // 和onActivityResult()的requestCode一样，用来区分多个不同的请求。
            if (requestCode == 200) {

            }
        }

        @Override
        public void onFailed(int requestCode, List<String> deniedPermissions) {
            // 权限申请失败回调。
            if (requestCode == 200) {
                // TODO ...
            }
        }
    };
    private SwipeRefreshLayout swipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.text);
        swipe = (SwipeRefreshLayout) findViewById(R.id.swipe);
        textView.setOnClickListener(this);
        AndPermission.with(this)
                .requestCode(100)
                .permission(Permission.STORAGE)
                .callback(listener)
                .start();
        if (FileUtil.initPath(ROOT)) {
            Log.d("Main", "Excel init succ");
        } else {
            Log.d("Main", "Excel init fail");
        }
        dbManager = DBManager.getInstance(MainActivity.this);
        dbManager.openDatabase();
    }

    @Override
    public void onClick(View view) {
        dialog = DialogUtil.showSeriMessageDialog(MainActivity.this);
        ExcelUtil excelUtil = new ExcelUtil();
        excelUtil.openFile(ROOT + "/1.xls");
        excelUtil.save(dbManager.getDb(), new ExcelUtil.OnDataConvertListener() {
            @Override
            public void onStart(int count) {
            }

            @Override
            public void onConverting(int count, int position) {
            }

            @Override
            public void onCompleted(int count) {
                GroupDao groupDao = new GroupDao(dbManager.getDb());
                List<Group> list = groupDao.querys(new Group());
                for (int i = 0; i < list.size(); i++) {
                    textView.append("\n" + list.get(0).getGroup_name());
                }
                dialog.dismiss();
            }

            @Override
            public void onError(int count, int position) {
                dialog.dismiss();
            }
        });
    }
}

package com.hzncc.zhudao.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.hzncc.zhudao.R;
import com.hzncc.zhudao.ZDApplication;
import com.hzncc.zhudao.adapter.HomeListMenuAdapter;
import com.hzncc.zhudao.db.AlarmLogDao;
import com.hzncc.zhudao.db.GroupDao;
import com.hzncc.zhudao.db.WorkLogDao;
import com.hzncc.zhudao.drawView.DrawTextureView;
import com.hzncc.zhudao.drawView.DrawType;
import com.hzncc.zhudao.drawView.DrawView;
import com.hzncc.zhudao.entity.AlarmLog;
import com.hzncc.zhudao.entity.AppPath;
import com.hzncc.zhudao.entity.Group;
import com.hzncc.zhudao.entity.WorkLog;
import com.hzncc.zhudao.receivers.BroadCastManager;
import com.hzncc.zhudao.service.MyService;
import com.hzncc.zhudao.utils.DateUtil;
import com.hzncc.zhudao.utils.DialogUtil;
import com.hzncc.zhudao.utils.IRCamera;
import com.hzncc.zhudao.utils.MenuUtil;
import com.hzncc.zhudao.utils.NumUtil;
import com.hzncc.zhudao.utils.SPUtils;
import com.hzncc.zhudao.utils.SizeUtil;
import com.hzncc.zhudao.widget.BatteryView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import static com.hzncc.zhudao.Constants.IMAGE_IDS;
import static com.hzncc.zhudao.Constants.LEFT_MENUS;
import static com.hzncc.zhudao.Constants.RIGHT_MENUS;


/**
 * ZhuDao
 * Created by 蔡雨峰 on 2017/3/14.
 */
public class HomeActivity extends Activity implements CompoundButton.OnCheckedChangeListener,
        View.OnClickListener, DrawView.OnDrawChangeListener {


    /* 绑定service监听*/
    ServiceConnection sconnection = new ServiceConnection() {
        /*当绑定时执行*/
        public void onServiceConnected(ComponentName name, IBinder service) {
            Intent intent = new Intent();//这里只是为了下面传intent对象而构建的，没有实际意义
            /*绑定后就可以使用Service的相关方法和属性来开始你对Service的操作*/
            MyService serviceBinder = ((MyService.MyBinder) service).getService();
            /*比如：你可以掉用Service的onStartCommand()方法*/
            serviceBinder.onStartCommand(intent, 0, 0);//0,0是我随意的参数

        }

        /*当断开绑定时执行，但调用unbindService()时不会触发改方法*/
        public void onServiceDisconnected(ComponentName name) {
            Log.d("activity--->", "已断开绑定service");
        }
    };
    SPUtils spUtils;
    private DrawTextureView vlView, irView;
    private DrawView vlDrawView, irDrawView;
    private BatteryView batteryView;
    private TextView date;
    private TextView lowTemp, highTemp;
    private ImageView colorLine;
    private ImageButton leftMenu, rightMenu;
    private CheckBox switchDraw, switchPic, videoBtn, switchFlash;
    private TextView circleRed, circleGreen, circleBlue;
    private TextView circleWhite;
    private ImageButton takePicBtn, gallery;
    private TextView[] circleTemps = new TextView[3];
    private BroadCastManager broadCastManager;
    private List<String> groups = new ArrayList<>();
    private List<String> rooms = new ArrayList<>();
    private List<String> wheels = new ArrayList<>();
    private String[] room = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "00"};
    private String[] wheel = {"1", "2", "3", "4"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ZDApplication.getRoot();
        initData();
        bindService();
        initViews();
        initState();
        broadCastManager.registerBattery(this);
    }

    private void initData() {
        Collections.addAll(rooms, room);
        Collections.addAll(wheels, wheel);
    }

    private void bindService() {
        /*绑定service*/
        Intent bindIntent = new Intent(this, MyService.class);
        bindService(bindIntent, sconnection, Context.BIND_AUTO_CREATE);
    }

    private void unBindService() {
        /*断开绑定service*/
        unbindService(sconnection);
    }

    @Override
    protected void onDestroy() {
        broadCastManager.unregisterBattery(this);
        WorkLogDao workLogDao = new WorkLogDao(ZDApplication.dbManager.getDb());
        workLogDao.createWorkLog(WorkLog.EVENTS[1]);
        unBindService();
        super.onDestroy();
    }

    private void initViews() {
        vlView = findViewById(R.id.activity_home_vl_view);
        irView = findViewById(R.id.activity_home_ir_view);
        vlDrawView = findViewById(R.id.activity_home_vl_Drawview);
        irDrawView = findViewById(R.id.activity_home_ir_Drawview);
        irDrawView.setVlView(vlDrawView);
        batteryView = findViewById(R.id.activity_home_battery);
        date = findViewById(R.id.activity_home_date);
        lowTemp = findViewById(R.id.activity_home_low_temp);
        highTemp = findViewById(R.id.activity_home_high_temp);
        colorLine = findViewById(R.id.activity_home_color_line);
        leftMenu = findViewById(R.id.activity_home_left_menu);
        rightMenu = findViewById(R.id.activity_home_right_menu);
        switchDraw = findViewById(R.id.activity_home_switch_draw_erase);
        switchPic = findViewById(R.id.activity_home_switch_pic_video);
        switchFlash = findViewById(R.id.activity_home_switch_flash);
        videoBtn = findViewById(R.id.activity_home_video_btn);
        circleWhite = findViewById(R.id.activity_home_cen_temp);
        circleRed = findViewById(R.id.activity_home_red_temp);
        circleGreen = findViewById(R.id.activity_home_green_temp);
        circleBlue = findViewById(R.id.activity_home_blue_temp);
        takePicBtn = findViewById(R.id.activity_home_takepic_btn);
        gallery = findViewById(R.id.activity_home_gallery);
        circleTemps[0] = circleRed;
        circleTemps[1] = circleGreen;
        circleTemps[2] = circleBlue;
        broadCastManager = new BroadCastManager(batteryView, date);
    }

    private void initState() {
        vlDrawView.setOnDrawChangeListener(this);
        irDrawView.setOnDrawChangeListener(this);
        lowTemp.setText("0");
        highTemp.setText("0");
        //init time
        String dateTime = DateUtil.formatUnixTime(DateUtil.getUnixTimeByCalendar());
        date.setText("日期：" + dateTime);

        leftMenu.setOnClickListener(this);
        rightMenu.setOnClickListener(this);

        switchDraw.setOnCheckedChangeListener(this);
        switchPic.setOnCheckedChangeListener(this);
        switchFlash.setOnCheckedChangeListener(this);
        videoBtn.setOnCheckedChangeListener(this);

        takePicBtn.setOnClickListener(this);
        gallery.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_home_left_menu:
                showMenu(true);
                break;
            case R.id.activity_home_right_menu:
                showMenu(false);
                break;
            case R.id.activity_home_red_temp:
                irDrawView.clean(0);
                break;
            case R.id.activity_home_green_temp:
                irDrawView.clean(1);
                break;
            case R.id.activity_home_blue_temp:
                irDrawView.clean(2);
                break;
            case R.id.activity_home_takepic_btn:
                showRecordDialog();
                break;
            case R.id.activity_home_gallery:
                startActivity(new Intent(this, GalleryActivity.class));
                break;
        }
    }

    private void showRecordDialog() {
        final Bitmap irBitmap = getBitmap(irView, irDrawView);
        final Bitmap vlBitmap = getBitmap(vlView, vlDrawView);
        final AlarmLog alarmLog = new AlarmLog();
        alarmLog.setDateTime(System.currentTimeMillis());
        alarmLog.setDateTag(DateUtil.formatUnixTime(alarmLog.getDateTime(), DateUtil.FORMAT_YMD));
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(alarmLog.getDateTime());
        alarmLog.setYear(calendar.get(Calendar.YEAR));
        alarmLog.setMonth(calendar.get(Calendar.MONTH) + 1);
        alarmLog.setDay(calendar.get(Calendar.DAY_OF_MONTH));
        alarmLog.setCenTemp(circleWhite.getTag() == null ?
                0 : (float) circleWhite.getTag());
        alarmLog.setAlarmTemp1(circleTemps[0].getTag() == null ?
                0 : (float) circleTemps[0].getTag());
        alarmLog.setAlarmTemp2(circleTemps[1].getTag() == null ?
                0 : (float) circleTemps[1].getTag());
        alarmLog.setAlarmTemp3(circleTemps[2].getTag() == null ?
                0 : (float) circleTemps[2].getTag());
        showGroupPick(irBitmap, vlBitmap, alarmLog);
    }

    private void showGroupPick(final Bitmap irBitmap, final Bitmap vlBitmap, final AlarmLog alarmLog) {
        spUtils = new SPUtils(this);
        OptionsPickerView pvOptions = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                spUtils.setOption(1, options1);
                spUtils.setOption(2, options2);
                spUtils.setOption(3, options3);
                //返回的分别是三个级别的选中位置
                final String groupText = "_" + groups.get(options1) + "-" + rooms.get(options2) + "-" + wheels.get(options3) + "-";
                DialogUtil.showRecordDialog(HomeActivity.this, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alarmLog.setWheelNum(groupText + v.getTag());
                        takePicture(alarmLog, irBitmap, vlBitmap);
                    }
                });
            }
        })
                .setSubmitText("确定")//确定按钮文字
                .setCancelText("取消")//取消按钮文字
                .setTitleText("检测轮对编组")
                .setSubCalSize(18)//确定和取消文字大小
                .setTitleSize(20)//标题文字大小
                .setTitleColor(Color.BLACK)//标题文字颜色
                .setSubmitColor(Color.BLUE)//确定按钮文字颜色
                .setCancelColor(Color.BLUE)//取消按钮文字颜色
                .setTitleBgColor(0xFFFFFFFF)//标题背景颜色 Night mode
                .setBgColor(0xFFFFFFFF)//滚轮背景颜色 Night mode
                .setContentTextSize(16)//滚轮文字大小
                .setLabels("编组", "车厢", "车轴")//设置选择的三级单位
                .isCenterLabel(true) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setCyclic(false, false, false)//循环与否
                .setSelectOptions(spUtils.getOption(1), spUtils.getOption(2), spUtils.getOption(3))  //设置默认选中项
                .setOutSideCancelable(true)//点击外部dismiss default true
                .isDialog(false)//是否显示为对话框样式
                .build();

        GroupDao groupDao = new GroupDao(ZDApplication.dbManager.getDb());
        List<Group> list = groupDao.querys(Group.class);
        groups.clear();
        for (int i = 0; i < list.size(); i++) {
            groups.add(list.get(i).getGroup_name());
        }
        pvOptions.setNPicker(groups, rooms, wheels);//添加数据源
        pvOptions.show();
    }

    private void takePicture(AlarmLog alarmLog, Bitmap irBitmap, Bitmap vlBitmap) {
        new Thread(new MyRunnable(irBitmap, vlBitmap, alarmLog)).start();
    }

    public File saveBitmap(Bitmap bitmap, String name, boolean isIR) {
        if (null == bitmap) {
            return null;
        }
        File file = new File(AppPath.IMAGE, name);
        try {
            if (file.exists()) {
                if (!file.delete()) {
                    Log.e("saveBitmap", "文件已存在，删除失败");
                }
            } else {
                if (file.createNewFile()) {
                    Log.e("saveBitmap", "文件创建失败");
                }
            }
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        bitmap.recycle();
        return file;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.activity_home_switch_draw_erase:
                irDrawView.setDrawType(isChecked ? DrawType.CLEAN : DrawType.RECTANGLE);
                vlDrawView.setDrawType(isChecked ? DrawType.CLEAN : DrawType.RECTANGLE);
                break;
            case R.id.activity_home_switch_pic_video:
                videoBtn.setVisibility(isChecked ? View.VISIBLE : View.GONE);
                takePicBtn.setVisibility(!isChecked ? View.VISIBLE : View.GONE);
                break;
            case R.id.activity_home_switch_flash:
                if (isChecked) {
                    IRCamera.flashOn();
                } else {
                    IRCamera.flashOff();
                }
                break;
            case R.id.activity_home_video_btn:
                if (isChecked) {
                } else {
                }
                break;
        }
    }

    private void showMenu(final boolean isLeft) {
        final Dialog dialog = new Dialog(this, R.style.no_bold_dialog);
        ListView listView = new ListView(this);
        listView.setLayoutParams(new AbsListView.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        dialog.setContentView(listView);
        HomeListMenuAdapter adapter;
        Window window = dialog.getWindow();
        WindowManager.LayoutParams attr = null;
        if (window != null) {
            attr = window.getAttributes();
            attr.width = SizeUtil.dp2px(this, 120);
            attr.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        }
        if (isLeft) {
            adapter = new HomeListMenuAdapter(this, R.layout.layout_home_list_menu_item, LEFT_MENUS, IMAGE_IDS);
            if (attr != null) {
                attr.gravity = Gravity.START | Gravity.TOP;
            }
        } else {
            adapter = new HomeListMenuAdapter(this, R.layout.layout_home_list_menu_item, RIGHT_MENUS, null);
            if (attr != null) {
                attr.gravity = Gravity.END | Gravity.TOP;
            }
        }
        listView.setAdapter(adapter);
        dialog.show();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (isLeft) {
                    if (position == 8) {
                        irDrawView.setDrawCenterTemp(!irDrawView.isDrawCenterTemp());
                        vlDrawView.setDrawCenterTemp(!irDrawView.isDrawCenterTemp());
                    } else {
                        MenuUtil.actionLeft(HomeActivity.this, position);
                    }
                } else {
                    MenuUtil.actionRight(HomeActivity.this, colorLine, position);
                }
                dialog.dismiss();
            }
        });
    }

    @Override
    public void onDrawChange(View view) {
        showCircles();
    }

    private void showCircles() {
        for (int i = 0; i < 3; i++) {
            if (i < DrawView.rects.size()) {
                circleTemps[i].setVisibility(View.VISIBLE);
                circleTemps[i].setOnClickListener(this);
            } else {
                circleTemps[i].setVisibility(View.INVISIBLE);
                circleTemps[i].setOnClickListener(null);
            }
        }
    }

    @Override
    public void onDataChange() {
        showCircles();
        circleWhite.setText(DrawView.cenTemp + "");
        circleWhite.setTag(DrawView.cenTemp);
        float max = 0, min = 9999;
        for (int i = 0; i < circleTemps.length; i++) {
            if (i < DrawView.rects.size()) {
                float maxTemp = NumUtil.formatFloat(DrawView.rects.get(i).getmMaxTemp(), 2);
                float minTemp = NumUtil.formatFloat(DrawView.rects.get(i).getmMinTemp(), 2);
                if (max < maxTemp) {
                    max = maxTemp;
                }
                if (min > minTemp) {
                    min = minTemp;
                }
                String maxStr = String.valueOf(maxTemp);
                circleTemps[i].setText(maxStr);
                circleTemps[i].setTag(DrawView.rects.get(i).getmMaxTemp());
            } else {
                circleTemps[i].setText("0");
                circleTemps[i].setTag(null);
            }
        }
        highTemp.setText(NumUtil.formatFloatToString(max, 2));
        lowTemp.setText(NumUtil.formatFloatToString(min == 9999 ? 0 : min, 2));
    }

    public Bitmap getBitmap(DrawTextureView textureView, DrawView drawView) {
        Bitmap bitmap = textureView.getBitmap();
        if (null == bitmap) {
            return null;
        }
        Bitmap canBit = Bitmap.createBitmap(textureView.getViewWidth(), textureView.getViewHeight(),
                bitmap.getConfig());
        Canvas canvas = new Canvas(canBit);
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        canvas.drawBitmap(bitmap, null, new Rect(0, 0, textureView.getViewWidth(),
                textureView.getViewHeight()), paint);
        drawView.drawShapeBold(canvas);
        drawView.drawTemp(canvas);
        if (drawView.isDrawCenterTemp) {
            drawView.drawCentemp(canvas);
        }
        drawView.drawMaxTemp(canvas);
        return canBit;
    }

    private class MyRunnable implements Runnable {
        Bitmap ir;
        Bitmap vl;
        AlarmLog alarmLog;

        MyRunnable(Bitmap ir, Bitmap vl, AlarmLog alarmLog) {
            this.ir = ir;
            this.vl = vl;
            this.alarmLog = alarmLog;
        }

        @Override
        public void run() {
            File file = null;
            String text = DateUtil.formatUnixTime(alarmLog.getDateTime(), "yyyy_MM_dd_hh_mm_ss");
//            String name = alarmLog.getYear() + "_" + alarmLog.getMonth() + "_" +
//                    alarmLog.getDay() + "_" + alarmLog.getWheelNum() + "_IR.jpg";
            String name = text + "_" + alarmLog.getWheelNum() + "_IR.jpg";
            if (null != ir) {
                file = saveBitmap(ir, name, true);
            }
            if (null == file) {
                return;
            }
            alarmLog.setIrPath(file.getPath());
            if (null != vl) {
                file = saveBitmap(vl, name.replace("IR", "VL"), false);
            }
            alarmLog.setVlPath(file.getPath());
            alarmLog.setType(AlarmLog.PICTURE);
            AlarmLogDao alarmLogDao = new AlarmLogDao(ZDApplication.dbManager.getDb());
            alarmLogDao.add(alarmLog);
        }
    }

}

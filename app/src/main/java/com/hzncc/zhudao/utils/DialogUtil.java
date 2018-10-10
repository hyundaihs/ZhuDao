package com.hzncc.zhudao.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.media.AudioManager;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.hzncc.zhudao.R;

import java.util.Calendar;

/**
 * ZhuDao
 * Created by 蔡雨峰 on 2017/3/20.
 */
public class DialogUtil {
    public static long startDate;
    public static long endDate;

    /**
     * 设置校正参数
     *
     * @param context 上下文
     */
    public static void showAddGroupDialog(final Activity context, final View.OnClickListener onClickListener) {
        if (context.isFinishing()) {
            return;
        }
        final Dialog dialog = new Dialog(context, R.style.no_bold_dialog);
        View view = View.inflate(context, R.layout.layout_edit_corrent, null);
        TextView title = view.findViewById(R.id.corr_dialog_message);
        title.setText("添加新编组");
        final EditText editText = (EditText) view.findViewById(R.id.corr_dialog_para);
        editText.setInputType(InputType.TYPE_CLASS_TEXT);
        Button back = (Button) view.findViewById(R.id.corr_dialog_back);
        Button ok = (Button) view.findViewById(R.id.corr_dialog_ok);
        ok.setText("添加");
        dialog.setContentView(view);
        dialog.show();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText.getText().toString().trim().length() <= 0) {
                    dialog.dismiss();
                    return;
                }
                v.setTag(editText.getText().toString());
                onClickListener.onClick(v);
                dialog.dismiss();
            }
        });
    }

    public static Dialog showSeriMessageDialog(Activity context) {
        final Dialog dialog = new Dialog(context);
        ProgressBar progressBar = new ProgressBar(context);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(300, 50);
        progressBar.setLayoutParams(params);
        dialog.show();
        dialog.setContentView(progressBar);
        return dialog;
    }

    /**
     * after take picture
     *
     * @param context         上下文
     * @param onClickListener ok按钮点击事件
     */
    public static void showRecordDialog(Activity context, final View.OnClickListener onClickListener) {
        if (context.isFinishing()) {
            return;
        }
        final SPUtils spUtils = new SPUtils(context);
        final Dialog dialog = new Dialog(context, R.style.no_bold_dialog);
        View view = View.inflate(context, R.layout.layout_record_dialog, null);
//        EditText user = (EditText) view.findViewById(R.id.record_dialog_recorder);
//        user.setText(ZDApplication.getLoger().getName());
        final EditText wheelNum = (EditText) view.findViewById(R.id.record_dialog_record_num);
        wheelNum.setText(spUtils.getCheckName());
        Button back = (Button) view.findViewById(R.id.record_dialog_back);
        Button ok = (Button) view.findViewById(R.id.record_dialog_ok);
        dialog.setContentView(view);
        Window window = dialog.getWindow();
        if (window != null) {
            WindowManager.LayoutParams attr = window.getAttributes();
            attr.width = SizeUtil.dp2px(context, 400);
        }
        dialog.show();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                String num = wheelNum.getText().length() <= 0 ? "" : wheelNum.getText().toString();
                spUtils.setCheckName(num);
                v.setTag(num);
                onClickListener.onClick(v);
            }
        });
    }

    /**
     * Connect WiFi
     *
     * @param context 上下文
     */
    public static void connectWifiDialog(final Activity context, final WifiConnect wifiConnect,
                                         final String ssid, final View.OnClickListener onClickListener, final boolean hasPass) {
        if (context.isFinishing()) {
            return;
        }
        final Dialog dialog = new Dialog(context, R.style.no_bold_dialog);
        View view = View.inflate(context, R.layout.layout_wifi_dialog, null);
        final TextView wifiName = (TextView) view.findViewById(R.id.wifi_dialog_wifi_name);
        wifiName.setText(ssid);
        final TextView titlePsd = (TextView) view.findViewById(R.id.wifi_dialog_wifi_password_title);
        final EditText wifiPsd = (EditText) view.findViewById(R.id.wifi_dialog_wifi_password);
        titlePsd.setVisibility(hasPass ? View.VISIBLE : View.GONE);
        wifiPsd.setVisibility(hasPass ? View.VISIBLE : View.GONE);
        Button back = (Button) view.findViewById(R.id.wifi_dialog_back);
        back.setVisibility(View.GONE);
        Button ok = (Button) view.findViewById(R.id.wifi_dialog_ok);
        dialog.setContentView(view);
        Window window = dialog.getWindow();
        if (window != null) {
            WindowManager.LayoutParams attr = window.getAttributes();
            attr.width = SizeUtil.dp2px(context, 400);
        }
        dialog.show();
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (hasPass) {
                    if (wifiPsd.getText().toString().trim().length() > 0) {
                        v.setTag(wifiPsd.getText().toString().trim());
                        onClickListener.onClick(v);
                    }
                } else {
                    onClickListener.onClick(v);
                }

            }
        });
    }

    /**
     * setting
     *
     * @param context 上下文
     */
    public static void showSettingDialog(Activity context, final View.OnClickListener onClickListener) {
        if (context.isFinishing()) {
            return;
        }
        final Dialog dialog = new Dialog(context, R.style.no_bold_dialog);
        View view = View.inflate(context, R.layout.layout_setting_dialog, null);
        EditText warnValue = (EditText) view.findViewById(R.id.setting_dialog_warn_value);
        CheckBox warnVibration = (CheckBox) view.findViewById(R.id.setting_dialog_vibration);
        SeekBar seekBar = (SeekBar) view.findViewById(R.id.setting_dialog_seekbar);
        Button back = (Button) view.findViewById(R.id.setting_dialog_back);
        Button ok = (Button) view.findViewById(R.id.setting_dialog_ok);
        dialog.setContentView(view);
        Window window = dialog.getWindow();
        if (window != null) {
            WindowManager.LayoutParams attr = window.getAttributes();
            attr.width = SizeUtil.dp2px(context, 400);
        }
        dialog.show();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                onClickListener.onClick(v);
            }
        });
        final AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        seekBar.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_RING));
        seekBar.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_RING));
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                audioManager.setStreamVolume(AudioManager.STREAM_RING, progress, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    /**
     * ask for sure exit system
     *
     * @param context 上下文
     */
    public static void showExitDialog(Activity context, final View.OnClickListener onClickListener) {
        if (context.isFinishing()) {
            return;
        }
        final Dialog dialog = new Dialog(context, R.style.no_bold_dialog);
        View view = View.inflate(context, R.layout.layout_exit_dialog, null);
        Button back = (Button) view.findViewById(R.id.exit_dialog_back);
        Button ok = (Button) view.findViewById(R.id.exit_dialog_ok);
        dialog.setContentView(view);
        dialog.show();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                onClickListener.onClick(v);
            }
        });
    }

    /**
     * ask for message dialog
     *
     * @param context         上下文
     * @param onClickListener 点击事件
     * @param message         信息
     */
    public static void showAskDialog(Activity context, final View.OnClickListener onClickListener, String message) {
        if (context.isFinishing()) {
            return;
        }
        final Dialog dialog = new Dialog(context, R.style.no_bold_dialog);
        View view = View.inflate(context, R.layout.layout_exit_dialog, null);
        TextView textView = (TextView) view.findViewById(R.id.exit_dialog_message);
        Button back = (Button) view.findViewById(R.id.exit_dialog_back);
        Button ok = (Button) view.findViewById(R.id.exit_dialog_ok);
        dialog.setContentView(view);
        back.setText("取消");
        textView.setText(message);
        dialog.show();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                onClickListener.onClick(v);
            }
        });
    }

    /**
     * ask for message dialog
     *
     * @param context 上下文
     * @param message 信息
     */
    public static void showErrorDialog(Activity context, String message) {
        showErrorDialog(context, message, null);
    }

    /**
     * ask for message dialog
     *
     * @param context 上下文
     * @param message 信息
     */
    public static void showErrorDialog(Activity context, String message, final View.OnClickListener onClickListener) {
        if (context.isFinishing()) {
            return;
        }
        final Dialog dialog = new Dialog(context, R.style.no_bold_dialog);
        View view = View.inflate(context, R.layout.layout_exit_dialog, null);
        TextView textView = (TextView) view.findViewById(R.id.exit_dialog_message);
        Button back = (Button) view.findViewById(R.id.exit_dialog_back);
        back.setVisibility(View.GONE);
        Button ok = (Button) view.findViewById(R.id.exit_dialog_ok);
        dialog.setContentView(view);
        textView.setText(message);
        dialog.show();
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (null != onClickListener) {
                    onClickListener.onClick(v);
                }
            }
        });
    }

    /**
     * 设置校正参数
     *
     * @param context 上下文
     */
    public static void showCorrentDialog(final Activity context) {
        if (context.isFinishing()) {
            return;
        }
        final Dialog dialog = new Dialog(context, R.style.no_bold_dialog);
        View view = View.inflate(context, R.layout.layout_edit_corrent, null);
        final EditText editText = (EditText) view.findViewById(R.id.corr_dialog_para);
        Button back = (Button) view.findViewById(R.id.corr_dialog_back);
        Button ok = (Button) view.findViewById(R.id.corr_dialog_ok);
        dialog.setContentView(view);
        dialog.show();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                int rel = 0;
                try {
                    if (editText.getText().length() <= 0) {
                        rel = 0;
                    } else {
                        rel = Integer.parseInt(editText.getText().toString());
                    }
                } catch (NumberFormatException e) {
                    rel = 0;
                } finally {
                    new SPUtils(context).setTempcorrect(rel);
                    IRCamera.tempCorrent(new SPUtils(context).getTempcorrect());
                }
            }
        });
    }

    /**
     * choose the date space
     *
     * @param context               上下文
     * @param onDateChoosedListener 选中事件
     */
    public static void showDateChooser(Activity context, final OnDateChoosedListener onDateChoosedListener) {
        if (context.isFinishing()) {
            return;
        }
        final Dialog dialog = new Dialog(context, R.style.no_bold_dialog);
        View view = View.inflate(context, R.layout.layout_date_chooser, null);
        DatePicker start = (DatePicker) view.findViewById(R.id.date_chooser_start);
        DatePicker end = (DatePicker) view.findViewById(R.id.date_chooser_end);
        Button back = (Button) view.findViewById(R.id.date_chooser_back);
        Button ok = (Button) view.findViewById(R.id.date_chooser_ok);
        dialog.setContentView(view);
        back.setText("取消");
        dialog.show();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (null != onDateChoosedListener) {
                    onDateChoosedListener.onDateChoosed(startDate, endDate);
                }
            }
        });
        Calendar calendar;
        if (startDate == 0 || endDate == 0) {
            calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            calendar = Calendar.getInstance();
            calendar.set(year, month, day, 0, 0, 0);
            startDate = calendar.getTimeInMillis();
            calendar = Calendar.getInstance();
            calendar.set(year, month, day, 24, 0, 0);
            endDate = calendar.getTimeInMillis();
        }
        calendar = Calendar.getInstance();
        calendar.setTimeInMillis(startDate);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        start.init(year, month, day, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, monthOfYear, dayOfMonth, 0, 0, 0);
                startDate = calendar.getTimeInMillis();
            }
        });
        calendar.setTimeInMillis(endDate);
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        end.init(year, month, day, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, monthOfYear, dayOfMonth, 24, 0, 0);
                endDate = calendar.getTimeInMillis();
            }
        });
    }

    /**
     * forced waiting dialog
     *
     * @param context 上下文
     */
    public static ForcedWait showForcedWaitDialog(Activity context, int num) {
        if (context.isFinishing()) {
            return null;
        }
        final Dialog dialog = new Dialog(context, R.style.no_bold_dialog);
        View view = View.inflate(context, R.layout.layout_forced_wait, null);
        TextView current = (TextView) view.findViewById(R.id.current);
        TextView count = (TextView) view.findViewById(R.id.count);
        current.setText(0 + "");
        count.setText(num + "");
        ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        progressBar.setMax(num);
        dialog.setContentView(view);
        dialog.setCancelable(false);
        dialog.show();
        ForcedWait forcedWait = new ForcedWait(progressBar, current, dialog, num);
        return forcedWait;
    }

    public interface OnDateChoosedListener {
        void onDateChoosed(long start, long end);
    }

    public static class ForcedWait {
        ProgressBar progressBar;
        TextView current;
        Dialog dialog;
        int count;

        public ForcedWait(ProgressBar progressBar, TextView current, Dialog dialog, int count) {
            this.progressBar = progressBar;
            this.current = current;
            this.dialog = dialog;
            this.count = count;
        }

        public void setProgress(int num) {
            current.setText(num + "");
            progressBar.setProgress(num);
            if (isMax(num)) {
                dialog.dismiss();
            }
        }

        public boolean isMax(int num) {
            return num >= count;
        }
    }


}

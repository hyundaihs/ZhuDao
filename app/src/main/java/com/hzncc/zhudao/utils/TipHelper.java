package com.hzncc.zhudao.utils;

import android.app.Service;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Vibrator;

import com.hzncc.zhudao.R;

/**
 * Created by Administrator on 2017/3/29.
 */
public class TipHelper {
    static Context myContext;
    static Vibrator vibrator;
    static MediaPlayer mediaPlayer;

    public TipHelper (Context context){
        myContext=context;
        vibrator=(Vibrator)myContext.getSystemService(Service.VIBRATOR_SERVICE);
        mediaPlayer=new MediaPlayer();
    }
    /**
     * 震动功能
     * @param milliseconds 震动时长 单位为毫秒
     */
    public static void Vibrate(long milliseconds){

//      long [] pattern={100,400,100,400};//停止 开启  停止 开启
//      vibrator.vibrate(pattern, 2);//重复两次上面的panttern，如果只是震动一次，index的值设定为-1
        vibrator.vibrate(milliseconds);
    }
    /**
     * 震动功能
     * @param pattern 自定义震动（模式）周期 long[] pattern={100,400,100,400}//停止 开启 停止 开启
     * @param isRepeat
     */
    public static void Vibrate(long[] pattern,boolean isRepeat){
        vibrator.vibrate(pattern, isRepeat?1:-1);
    }
    /**
     * 停止震动与响铃
     */
    public static void destroy(){
        vibrator.cancel();
        mediaPlayer.stop();//停止
        mediaPlayer.release();//释放资源
    }

    /**
     * 提示音
     * @param FLAG 标识符为1时提示超速警告   标识符为2时提示越界警告
     */
    public static void playvoid(int FLAG){
        try{
            mediaPlayer.reset();
            mediaPlayer.setLooping(true);//重复播放
            Uri uri = null;
            if(FLAG==1){
                uri=Uri.parse("android.resource://" + myContext.getPackageName() + "/"+ R.raw.overzone);//raw文件夹下的yes音频文件，使用的时候要注意修改
            }
            else if(FLAG==2){
                uri=Uri.parse("android.resource://" + myContext.getPackageName() + "/"+R.raw.overzone);//raw文件夹下的yes音频文件，使用的时候要注意修改

            }
            //mediaPlayer.create(MainActivity.this, R.raw.yes);
            mediaPlayer.setDataSource(myContext, uri);//设置文件员
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_RING);
            mediaPlayer.prepare();//添加缓存
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

                @Override
                public void onPrepared(MediaPlayer mp) {
                    mediaPlayer.start();//开始播放
                }
            });
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            destroy();
        }
    }
}

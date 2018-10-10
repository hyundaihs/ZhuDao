package com.hzncc.zhudaoclient.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hzncc.zhudaoclient.MainActivity;
import com.hzncc.zhudaoclient.R;
import com.hzncc.zhudaoclient.serverutil.JsonUtil;
import com.hzncc.zhudaoclient.serverutil.ServerInterface;
import com.hzncc.zhudaoclient.serverutil.SocketManager;
import com.hzncc.zhudaoclient.utils.FileUtil;

import java.io.File;
import java.util.HashMap;
import java.util.List;

/**
 * ZhuDao
 * Created by 蔡雨峰 on 2017/7/28.
 */

public class GalleryActivity extends Activity {

    private ViewPager viewPager;
    private ImageView[] views;
    private List<String> files;
    private MyAdapter adapter;
    private TextView topText, bottomText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        int curr = getIntent().getIntExtra("curr", 0);
        files = getIntent().getStringArrayListExtra("list");
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        topText = (TextView) findViewById(R.id.top_text);
        bottomText = (TextView) findViewById(R.id.bottom_text);
        adapter = new MyAdapter(this, files);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(curr);
        topText.setText((curr + 1) + "/" + files.size());
        String filename = files.get(curr);
        bottomText.setText(filename.substring(filename.lastIndexOf("/") + 1));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                topText.setText((position + 1) + "/" + files.size());
                String filename = files.get(position);
                bottomText.setText(filename.substring(filename.lastIndexOf("/") + 1));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    class MyHandler extends Handler {
        private ImageView imageView;

        MyHandler(ImageView imageView) {
            this.imageView = imageView;
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SocketManager.RESULT_MSG_SUCCESS:
                    break;
                case SocketManager.RESULT_FILE_SUCCESS:
                    imageView.setImageBitmap(BitmapFactory.decodeFile((String) imageView.getTag()));
                    break;
                case SocketManager.RESULT_ALL_FILE_SUCCESS:
                    adapter.notifyDataSetChanged();
                    break;
                case SocketManager.RESULT_FAILED:
                    break;
            }
        }
    }

    private class MyAdapter extends PagerAdapter {
        private List<String> data;
        private Context context;

        public MyAdapter(Context context, List<String> data) {
            this.context = context;
            this.data = data;
            views = new ImageView[data.size()];
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(views[position]);//删除页卡
        }


        @Override
        public Object instantiateItem(ViewGroup container, int position) {  //这个方法用来实例化页卡
            if (null == views[position]) {
                views[position] = new ImageView(context);
            }
            String string = data.get(position);
            if (new File(string).exists()) {
                views[position].setImageBitmap(BitmapFactory.decodeFile(string));
            } else {
                views[position].setTag(string);
                getImage(views[position], string.replace(FileUtil.ROOT, FileUtil.OLD));
            }
            container.addView(views[position], 0);//添加页卡
            return views[position];
        }

        private void getImage(ImageView imageView, String fliename) {
            if (null == MainActivity.socketManager) {
                return;
            }
            HashMap<String, String> hashMap = new HashMap<String, String>();
            hashMap.put("file_name", fliename);
            hashMap.put("type", String.valueOf(SocketManager.REPLY_NORMAL_MSG));
            MainActivity.socketManager.sendMessage(JsonUtil.getRequestString(ServerInterface.GET_IMAGE, hashMap),
                    MainActivity.device, new MyHandler(imageView));
        }

        @Override
        public int getCount() {
            return data.size();//返回页卡的数量
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;//官方提示这样写
        }
    }
}

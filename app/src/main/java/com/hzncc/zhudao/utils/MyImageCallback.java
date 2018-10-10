package com.hzncc.zhudao.utils;

import android.graphics.Bitmap;
import android.widget.ImageView;

/**
 * Created by Administrator on 2017/3/29.
 */
public class MyImageCallback implements RequestUtil.ImageCallback {
    private ImageView imageView;

    public MyImageCallback(ImageView imageView) {
        this.imageView = imageView;
        // imageView.setImageResource(R.drawable.loading);
    }

    @Override
    public void imageLoaded(Bitmap bitmap) {
        imageView.setImageBitmap(bitmap);
    }

}

package com.hzncc.zhudao.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hzncc.zhudao.R;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by Administrator on 2017/3/20.
 */
public class HomeListMenuAdapter extends BaseAdapter {
    private String[] data;
    private int[] imageIds;
    private Context context;
    private int layoutId;

    public HomeListMenuAdapter(Context context, int layoutId, String[] data, int[] imageIds) {
        this.context = context;
        this.layoutId = layoutId;
        this.data = data;
        this.imageIds = imageIds;
    }

    @Override
    public int getCount() {
        return data.length;
    }

    @Override
    public String getItem(int position) {
        return data[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        ImageView icon;
        TextView textView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (null == convertView) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(layoutId, null, false);
            viewHolder.icon = (ImageView) convertView.findViewById(R.id.activity_home_list_menu_item_icon);
            viewHolder.textView = (TextView) convertView.findViewById(R.id.activity_home_list_menu_item_text);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (null != imageIds) {
            viewHolder.icon.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), imageIds[position]));
        }
        viewHolder.textView.setText(getItem(position));
        return convertView;
    }
}

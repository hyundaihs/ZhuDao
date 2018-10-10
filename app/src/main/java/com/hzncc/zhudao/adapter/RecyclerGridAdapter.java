package com.hzncc.zhudao.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.hzncc.zhudao.R;
import com.hzncc.zhudao.entity.AlarmLog;
import com.hzncc.zhudao.utils.MyImageCallback;
import com.hzncc.zhudao.utils.RequestUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/24.
 */
public class RecyclerGridAdapter extends RecyclerView.Adapter<RecyclerGridAdapter.MyViewHolder> {
    private List<AlarmLog> data;
    private Context context;
    private OnRecycleItemClickListener onItemClickListener;
    private RequestUtil requestUtil;
    private boolean isSelectable = false;
    private ArrayList<AlarmLog> selected;

    public RecyclerGridAdapter(Context context, List<AlarmLog> data) {
        this.context = context;
        this.data = data;
        requestUtil = new RequestUtil(context);
        selected = new ArrayList<AlarmLog>();
    }

    public ArrayList<AlarmLog> getSelected() {
        return selected;
    }

    public void setSelected(ArrayList<AlarmLog> selected) {
        this.selected = selected;
    }

    public void cleanSelected() {
        isSelectable = false;
        selected.clear();
        notifyItemRangeChanged(0, getItemCount());
    }

    public OnRecycleItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnRecycleItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public AlarmLog getItem(int position) {
        return data.get(position);
    }

    @Override
    public RecyclerGridAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(
                LayoutInflater.from(context).inflate(R.layout.layout_gallery_list_item_view, null, false));
    }

    @Override
    public void onBindViewHolder(final RecyclerGridAdapter.MyViewHolder holder, final int position) {
        final AlarmLog alarmLog = getItem(position);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(alarmLog.getVlPath(), options);
        ViewGroup.LayoutParams params = holder.vl.getLayoutParams();
        params.width = options.outWidth;
        params.height = options.outHeight;
        holder.vl.setLayoutParams(params);
        holder.ir.setLayoutParams(params);
        options.inJustDecodeBounds = false;
        requestUtil.getLocalImage(alarmLog.getVlPath(), options, new MyImageCallback(holder.vl));
        requestUtil.getLocalImage(alarmLog.getIrPath(), options, new MyImageCallback(holder.ir));
        holder.focusground.setForeground((isSelectable && selected.indexOf(alarmLog) >= 0) ?
                context.getResources().getDrawable(R.color.seleted) :
                context.getResources().getDrawable(android.R.color.transparent));
        holder.select.setVisibility((isSelectable && selected.indexOf(alarmLog) >= 0) ? View.VISIBLE : View.GONE);
        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSelectable) {
                    if (selected.indexOf(alarmLog) >= 0) {
                        selected.remove(alarmLog);
                    } else {
                        selected.add(alarmLog);
                    }
                    notifyItemChanged(position);
                    return;
                }
                if (null != onItemClickListener) {
                    onItemClickListener.onItemClick(v, holder, position);
                }
            }
        });
        holder.rootView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (!isSelectable) {
                    isSelectable = true;
                    selected.add(alarmLog);
                } else {
                    if (selected.indexOf(alarmLog) >= 0) {
                        selected.remove(alarmLog);
                    } else {
                        selected.add(alarmLog);
                    }
                }
                notifyItemChanged(position);
                if (isSelectable && null != onItemClickListener) {
                    onItemClickListener.onItemLongClick(v, holder, position);
                }
                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public interface OnRecycleItemClickListener {

        void onItemClick(View view, RecyclerView.ViewHolder holder, int position);

        void onItemLongClick(View view, RecyclerView.ViewHolder holder, int position);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView vl, ir;
        View rootView;
        FrameLayout focusground;
        ImageView select;

        MyViewHolder(View itemView) {
            super(itemView);
            rootView = itemView;
            vl = (ImageView) itemView.findViewById(R.id.gallery_list_item_vl_view);
            ir = (ImageView) itemView.findViewById(R.id.gallery_list_item_ir_view);
            focusground = (FrameLayout) itemView.findViewById(R.id.frameLayout);
            select = (ImageView) itemView.findViewById(R.id.gallery_list_item_selected_icon);
        }
    }
}


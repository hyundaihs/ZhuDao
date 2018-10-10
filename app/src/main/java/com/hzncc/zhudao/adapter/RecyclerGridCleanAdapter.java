package com.hzncc.zhudao.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.hzncc.zhudao.R;
import com.hzncc.zhudao.entity.AlarmLog;
import com.hzncc.zhudao.ui.CleanActivity;
import com.hzncc.zhudao.utils.DateUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * ZhuDao
 * Created by 蔡雨峰 on 2017/5/11.
 */

public class RecyclerGridCleanAdapter extends RecyclerView.Adapter<RecyclerGridCleanAdapter.MyViewHolder> {
    private List<AlarmLog> data;
    private Context context;
    private RecyclerGridCleanAdapter.OnRecycleItemClickListener onItemClickListener;
    private boolean isSelectable = false;
    private List<AlarmLog> selected;
    private CleanActivity.Level level = CleanActivity.Level.YEAR;

    public RecyclerGridCleanAdapter(Context context, List<AlarmLog> data) {
        this.context = context;
        this.data = data;
        selected = new ArrayList<>();
    }

    public CleanActivity.Level getLevel() {
        return level;
    }

    public void setLevel(CleanActivity.Level level) {
        this.level = level;
    }

    public List<AlarmLog> getSelected() {
        return selected;
    }

    public void setSelected(List<AlarmLog> selected) {
        this.selected = selected;
    }

    public void cleanSelected() {
        isSelectable = false;
        selected.clear();
        notifyItemRangeChanged(0, getItemCount());
    }

    public RecyclerGridCleanAdapter.OnRecycleItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(RecyclerGridCleanAdapter.OnRecycleItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public AlarmLog getItem(int position) {
        return data.get(position);
    }

    @Override
    public RecyclerGridCleanAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(
                View.inflate(context, R.layout.layout_clean_list_item_view, null));
    }

    @Override
    public void onBindViewHolder(final RecyclerGridCleanAdapter.MyViewHolder holder, final int position) {
        final AlarmLog alarmLog = getItem(position);
        switch (level) {
            case YEAR:
                holder.info.setText(alarmLog.getYear() + "年");
                holder.bg.setImageResource(R.mipmap.directory);
                break;
            case MONTH:
                holder.info.setText(alarmLog.getMonth() + "月");
                holder.bg.setImageResource(R.mipmap.directory);
                break;
            case DAY:
                holder.info.setText(alarmLog.getDay() + "日");
                holder.bg.setImageResource(R.mipmap.directory);
                break;
            case ONCE:
                String text = DateUtil.formatUnixTime(alarmLog.getDateTime(), DateUtil.FORMAT_HMS);
                holder.info.setText(alarmLog.getDateTag() + "\n" + text);
                holder.bg.setImageResource(R.mipmap.file);
                break;
        }
        holder.rootView.setForeground((isSelectable && selected.indexOf(alarmLog) >= 0) ?
                context.getResources().getDrawable(R.color.seleted2) :
                context.getResources().getDrawable(android.R.color.transparent));
        holder.select.setVisibility((isSelectable && selected.indexOf(alarmLog) >= 0)
                ? View.VISIBLE : View.GONE);
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
        ImageView bg;
        TextView info;
        FrameLayout rootView;
        ImageView select;

        MyViewHolder(View itemView) {
            super(itemView);
            rootView = (FrameLayout) itemView;
            bg = (ImageView) itemView.findViewById(R.id.clean_list_item_bg);
            info = (TextView) itemView.findViewById(R.id.clean_list_item_info);
            select = (ImageView) itemView.findViewById(R.id.gallery_list_item_selected_icon);
        }
    }
}

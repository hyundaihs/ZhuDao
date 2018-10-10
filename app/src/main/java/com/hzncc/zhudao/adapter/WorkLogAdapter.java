package com.hzncc.zhudao.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hzncc.zhudao.R;
import com.hzncc.zhudao.entity.WorkLog;
import com.hzncc.zhudao.utils.DateUtil;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/3/29.
 */
public class WorkLogAdapter extends RecyclerView.Adapter<WorkLogAdapter.MyViewHolder> {
    private ArrayList<WorkLog> data;
    private Context context;
    private OnRecycleItemClickListener onItemClickListener;
    private boolean isSelectable = false;
    private ArrayList<WorkLog> selected;

    public WorkLogAdapter(Context context, ArrayList<WorkLog> data) {
        this.context = context;
        this.data = data;
        selected = new ArrayList<WorkLog>();
    }

    public ArrayList<WorkLog> getSelected() {
        return selected;
    }

    public void setSelected(ArrayList<WorkLog> selected) {
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

    public WorkLog getItem(int position) {
        return data.get(position);
    }

    @Override
    public WorkLogAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder viewHolder = new MyViewHolder(
                LayoutInflater.from(context).inflate(R.layout.layout_worklog_list_item_view, parent, false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final WorkLogAdapter.MyViewHolder holder, final int position) {
        final WorkLog workLog = getItem(position);
        holder.time.setText(DateUtil.formatUnixTime(workLog.getDateTime(), DateUtil.FORMAT_HMS));
        holder.loginfo.setText(workLog.getLoginfo());
        holder.select.setVisibility((isSelectable && selected.indexOf(workLog) >= 0) ? View.VISIBLE : View.GONE);
        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSelectable) {
                    if (selected.indexOf(workLog) >= 0) {
                        selected.remove(workLog);
                    } else {
                        selected.add(workLog);
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
                    selected.add(workLog);
                } else {
                    if (selected.indexOf(workLog) >= 0) {
                        selected.remove(workLog);
                    } else {
                        selected.add(workLog);
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

    protected class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView time, loginfo;
        public View rootView;
        public ImageView select;

        public MyViewHolder(View itemView) {
            super(itemView);
            rootView = itemView;
            time = (TextView) itemView.findViewById(R.id.worklog_item_time);
            loginfo = (TextView) itemView.findViewById(R.id.worklog_item_loginfo);
            select = (ImageView) itemView.findViewById(R.id.worklog_item_selected_icon);
        }
    }
}
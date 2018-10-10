package com.hzncc.zhudao.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hzncc.zhudao.R;
import com.hzncc.zhudao.entity.Group;

import java.util.List;

/**
 * ZhuDao
 * Created by 蔡雨峰 on 2017/12/15.
 */

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.MyViewHolder> {
    private List<Group> data;
    private Context context;
    private GroupAdapter.OnRecycleItemClickListener onItemClickListener;
    private OnRecycleItemDeleteListener onRecycleItemDeleteListener;

    public GroupAdapter(Context context, List<Group> data) {
        this.context = context;
        this.data = data;
    }

    public OnRecycleItemDeleteListener getOnRecycleItemDeleteListener() {
        return onRecycleItemDeleteListener;
    }

    public void setOnRecycleItemDeleteListener(OnRecycleItemDeleteListener onRecycleItemDeleteListener) {
        this.onRecycleItemDeleteListener = onRecycleItemDeleteListener;
    }

    public GroupAdapter.OnRecycleItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(GroupAdapter.OnRecycleItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public Group getItem(int position) {
        return data.get(position);
    }

    @Override
    public GroupAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        GroupAdapter.MyViewHolder viewHolder = new GroupAdapter.MyViewHolder(
                LayoutInflater.from(context).inflate(R.layout.layout_text_view, parent, false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final GroupAdapter.MyViewHolder holder, final int position) {
        final Group group = getItem(position);
        holder.name.setText(group.getGroup_name());
        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != onItemClickListener) {
                    onItemClickListener.onItemClick(v, holder, position);
                }
            }
        });
        holder.name.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (null != onItemClickListener) {
                    onItemClickListener.onItemLongClick(v, holder, position);
                }
                return true;
            }
        });
        holder.del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRecycleItemDeleteListener.onItemDelete(v, position);
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

    public interface OnRecycleItemDeleteListener {
        void onItemDelete(View view, int position);
    }

    protected class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView del;

        public MyViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.group_list_item_text);
            del = itemView.findViewById(R.id.group_list_item_delete);
        }
    }
}

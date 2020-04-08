package com.rocedar.deviceplatform.app.highbloodpressure.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rocedar.deviceplatform.R;

import java.util.List;


/**
 * @author liuyi
 * @date 2017/11/24
 * @desc
 * @veison
 */

public class HBPChooseUserGridAdapter extends RecyclerView.Adapter<HBPChooseUserGridAdapter.ViewHolder> {
    private List<String> mDatas;
    private Context mContext;

    public interface OnItemClickListener {
        void onItemClick(View view);
    }

    private OnItemClickListener mOnItemClickListener = null;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public HBPChooseUserGridAdapter(List<String> mDatas, Context mContext) {
        this.mDatas = mDatas;
        this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_hbp_consult_choose_user, parent, false);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null)
                    mOnItemClickListener.onItemClick(v);
            }
        });

        return new ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.view.setTag(position);
        holder.tvItemBhpConsultUserName.setText(mDatas.get(position));
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvItemBhpConsultUserName;
        private View view;

        ViewHolder(View view) {
            super(view);
            this.view = view;
            tvItemBhpConsultUserName = (TextView) view.findViewById(R.id.tv_item_bhp_consult_user_name);
        }
    }
}

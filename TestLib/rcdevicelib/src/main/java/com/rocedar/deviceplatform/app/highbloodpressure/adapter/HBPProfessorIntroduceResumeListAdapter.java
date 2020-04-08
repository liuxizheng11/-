package com.rocedar.deviceplatform.app.highbloodpressure.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rocedar.deviceplatform.R;

import java.util.List;

/**
 * @author liuyi
 * @date 2017/11/27
 * @desc
 * @veison
 */

public class HBPProfessorIntroduceResumeListAdapter extends RecyclerView.Adapter<HBPProfessorIntroduceResumeListAdapter.ViewHolder> {
    private List<String> list;

    public HBPProfessorIntroduceResumeListAdapter(List<String> list) {
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_hbp_doctor_details_bottom, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tv_item_hbp_doctor_details_content.setText(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_item_hbp_doctor_details_content;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_item_hbp_doctor_details_content = (TextView) itemView.findViewById(R.id.tv_item_hbp_doctor_details_content);
        }
    }
}

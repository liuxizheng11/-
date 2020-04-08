package com.rocedar.deviceplatform.app.highbloodpressure.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rocedar.base.RCImageShow;
import com.rocedar.deviceplatform.R;
import com.rocedar.deviceplatform.app.view.CircleImageView;
import com.rocedar.deviceplatform.dto.highbloodpressure.RCHBPDoctorDTO;

import java.util.List;




/**
 * @author liuyi
 * @date 2017/11/21
 * @desc 实验室专家
 * @veison
 */

public class HBPProfessorListadapter extends RecyclerView.Adapter<HBPProfessorListadapter.ViewHolder> {


    private List<RCHBPDoctorDTO> mDatas;
    private Context mContext;

    public interface OnItemClickListener {
        void onItemClick(View view);
    }

    private OnItemClickListener mOnItemClickListener = null;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public HBPProfessorListadapter(Context mContext, List<RCHBPDoctorDTO> mDatas) {
        this.mDatas = mDatas;
        this.mContext = mContext;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_hbp_seminar, parent, false);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClick(v);
            }
        });

        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.item.setTag(position);
        RCHBPDoctorDTO dto = mDatas.get(position);
        RCImageShow.loadUrl(dto.getDoctor_icon(), holder.ivItemHbpSeminarHead, RCImageShow.IMAGE_TYPE_HEAD);
        holder.tvItemHbpSeminarName.setText(dto.getDoctor_name());
        holder.tvItemHbpSeminarHospital.setText(dto.getHospital_name());
        holder.tvItemHbpSeminarOffice.setText(dto.getProfession_name());
        holder.tvItemHbpSeminarJob.setText(dto.getTitle_name());
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private View item;
        private CircleImageView ivItemHbpSeminarHead;
        private TextView tvItemHbpSeminarName;
        private TextView tvItemHbpSeminarJob;
        private TextView tvItemHbpSeminarHospital;
        private TextView tvItemHbpSeminarOffice;

        ViewHolder(View view) {
            super(view);
            this.item = view;
            ivItemHbpSeminarHead = (CircleImageView) view.findViewById(R.id.iv_item_hbp_seminar_head);
            tvItemHbpSeminarName = (TextView) view.findViewById(R.id.tv_item_hbp_seminar_name);
            tvItemHbpSeminarJob = (TextView) view.findViewById(R.id.tv_item_hbp_seminar_job);
            tvItemHbpSeminarHospital = (TextView) view.findViewById(R.id.tv_item_hbp_seminar_hospital);
            tvItemHbpSeminarOffice = (TextView) view.findViewById(R.id.tv_item_hbp_seminar_office);

        }
    }
}

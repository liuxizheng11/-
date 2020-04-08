package com.rocedar.deviceplatform.app.highbloodpressure.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rocedar.base.RCImageShow;
import com.rocedar.deviceplatform.R;
import com.rocedar.deviceplatform.app.view.CircleImageView;
import com.rocedar.deviceplatform.dto.highbloodpressure.RCHBPDoctorDTO;


/**
 * @author liuyi
 * @date 2017/11/27
 * @desc
 * @veison
 */

public class HBPProfessorIntroduceListAdapter extends RecyclerView.Adapter {
    private RCHBPDoctorDTO dto;
    private Context mContext;

    public HBPProfessorIntroduceListAdapter(Context mContext, RCHBPDoctorDTO dto) {
        this.dto = dto;
        this.mContext = mContext;
    }

    public interface OnItemClickListener {
        void onItemClick(View view);
    }

    private OnItemClickListener mOnItemClickListener = null;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_USER_INFO:
                new UserInfoViewHolder(getView(parent, R.layout.item_list_hbp_doctor_details_top));
            case TYPE_RESUME_LIST:
                new ResumeViewHolder(getView(parent, R.layout.fragment_high_blood_pressure));
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof UserInfoViewHolder) {//顶部个人信息
            RCImageShow.loadUrl(dto.getDoctor_icon(), ((UserInfoViewHolder) holder).ivHbpProfessorHead, RCImageShow.IMAGE_TYPE_HEAD);
            ((UserInfoViewHolder) holder).tvHbpProfessorName.setText(dto.getDoctor_name());
            ((UserInfoViewHolder) holder).tvHbpProfessorJob.setText(dto.getTitle_name());
            ((UserInfoViewHolder) holder).tvHbpProfessorHospital.setText(dto.getHospital_name());
            ((UserInfoViewHolder) holder).tvHbpProfessorOffice.setText(dto.getProfession_name());
            ((UserInfoViewHolder) holder).tv_hbp_professor_skilled.setText(dto.getSkilled());

        } else if (holder instanceof ResumeViewHolder) {//底部成就列表
            ((ResumeViewHolder) holder).adapter.notifyDataSetChanged();
        }
    }

    /**
     * 顶部个人信息
     */
    public static final int TYPE_USER_INFO = 0xff1111;
    /**
     * 底部成就列表
     */
    public static final int TYPE_RESUME_LIST = 0xff2222;

    @Override
    public int getItemViewType(int position) {

        if (position == 0)
            return TYPE_USER_INFO;

        return TYPE_RESUME_LIST;
    }

    @Override
    public int getItemCount() {
        return dto.getResume().size() + 1;
    }

    private View getView(ViewGroup parent, int layout) {
        return LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
    }

    public class UserInfoViewHolder extends RecyclerView.ViewHolder {
        CircleImageView ivHbpProfessorHead;
        TextView tvHbpProfessorName;
        TextView tvHbpProfessorJob;
        TextView tvHbpProfessorHospital;
        TextView tvHbpProfessorOffice;
        TextView tv_hbp_professor_skilled;

        UserInfoViewHolder(View view) {
            super(view);
            ivHbpProfessorHead = (CircleImageView) view.findViewById(R.id.iv_hbp_professor_head);
            tvHbpProfessorName = (TextView) view.findViewById(R.id.tv_hbp_professor_name);
            tvHbpProfessorJob = (TextView) view.findViewById(R.id.tv_hbp_professor_job);
            tvHbpProfessorHospital = (TextView) view.findViewById(R.id.tv_hbp_professor_hospital);
            tvHbpProfessorOffice = (TextView) view.findViewById(R.id.tv_hbp_professor_office);
            tv_hbp_professor_skilled = (TextView) view.findViewById(R.id.tv_hbp_professor_skilled);
        }
    }

    public class ResumeViewHolder extends RecyclerView.ViewHolder {
        private RecyclerView rv_high_blood_pressure;
        private HBPProfessorIntroduceResumeListAdapter adapter;

        ResumeViewHolder(View view) {
            super(view);
            rv_high_blood_pressure = (RecyclerView) view.findViewById(R.id.rv_high_blood_pressure);
            rv_high_blood_pressure.setLayoutManager(new LinearLayoutManager(mContext));
            rv_high_blood_pressure.setAdapter(adapter = new HBPProfessorIntroduceResumeListAdapter(dto.getResume()));
        }
    }
}

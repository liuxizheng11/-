package com.rocedar.deviceplatform.app.familydoctor.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rocedar.base.RCDateUtil;
import com.rocedar.base.RCImageShow;
import com.rocedar.deviceplatform.R;
import com.rocedar.deviceplatform.app.view.CircleImageView;
import com.rocedar.deviceplatform.dto.familydoctor.RCFDRecordListDTO;

import java.util.ArrayList;


/**
 * @author liuyi
 * @date 2017/4/20
 * @desc
 * @veison
 */

public class FamilyDoctorRecordListAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private ArrayList<RCFDRecordListDTO> mDatas;

    public FamilyDoctorRecordListAdapter(ArrayList<RCFDRecordListDTO> mDatas, Context mContext) {
        this.mDatas = mDatas;
        this.inflater = inflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_list_family_doctor_record, parent, false);
            holder = new ViewHolder();
            holder.ivItemDoctorRecordHead = (CircleImageView) convertView.findViewById(R.id.iv_item_doctor_record_head);
            holder.tvItemDoctorRecordName = (TextView) convertView.findViewById(R.id.tv_item_doctor_record_name);
            holder.tvItemDoctorRecordJob = (TextView) convertView.findViewById(R.id.tv_item_doctor_record_job);
            holder.tvItemDoctorRecordDesc = (TextView) convertView.findViewById(R.id.tv_item_doctor_record_desc);
            holder.tvItemDoctorRecordTime = (TextView) convertView.findViewById(R.id.tv_item_doctor_record_time);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        RCFDRecordListDTO dto = mDatas.get(position);
        RCImageShow.loadUrl(dto.getPortrait(), holder.ivItemDoctorRecordHead, RCImageShow.IMAGE_TYPE_HEAD);
        holder.tvItemDoctorRecordName.setText(dto.getDoctor_name());
        holder.tvItemDoctorRecordJob.setText(dto.getTitle_name());
        holder.tvItemDoctorRecordDesc.setText(dto.getSymptom());
        //时间周期
        String time = RCDateUtil.formatServiceTime(dto.getStart_time() + "", "yyyy-MM-dd  HH:mm ");
        holder.tvItemDoctorRecordTime.setText(time + " ( " + dto.getTotal_time() + " )");
        return convertView;
    }

    static class ViewHolder {
        CircleImageView ivItemDoctorRecordHead;
        TextView tvItemDoctorRecordName;
        TextView tvItemDoctorRecordJob;
        TextView tvItemDoctorRecordDesc;
        TextView tvItemDoctorRecordTime;
    }
}

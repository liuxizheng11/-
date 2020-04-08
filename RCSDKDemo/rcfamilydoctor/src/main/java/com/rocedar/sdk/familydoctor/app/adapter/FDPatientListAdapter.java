package com.rocedar.sdk.familydoctor.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rocedar.lib.base.unit.RCJavaUtil;
import com.rocedar.sdk.familydoctor.R;
import com.rocedar.sdk.familydoctor.dto.RCFDPatientListDTO;

import java.util.List;

/**
 * 项目名称：瑰柏SDK-健康服务（家庭医生）
 * <p>
 * 作者：phj
 * 日期：2018/7/24 下午6:01
 * 版本：V1.1.00
 * 描述：瑰柏SDK-
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class FDPatientListAdapter extends BaseAdapter {

    private Context context;

    private List<RCFDPatientListDTO> patientListDTOS;


    public FDPatientListAdapter(Context context, List<RCFDPatientListDTO> patientListDTOS) {
        this.context = context;
        this.patientListDTOS = patientListDTOS;
    }

    @Override
    public int getCount() {
        return patientListDTOS.size();
    }

    @Override
    public Object getItem(int position) {
        return patientListDTOS.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.rc_fd_adapter_patient, null);
            holder = new ViewHolder();
            holder.patientName = (TextView) convertView.findViewById(R.id.rc_fd_patient_adapter_name);
            holder.patientAge = (TextView) convertView.findViewById(R.id.rc_fd_patient_adapter_age);
            holder.patientSex = (TextView) convertView.findViewById(R.id.rc_fd_patient_adapter_sex);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (patientListDTOS.get(position).getSick_name().length() > 10) {
            holder.patientName.setText(patientListDTOS.get(position).getSick_name().substring(0, 9) + "…");
        } else
            holder.patientName.setText(patientListDTOS.get(position).getSick_name());
        holder.patientAge.setText(RCJavaUtil.getAgeByBirthday(patientListDTOS.get(position).getBirthday() + "")
                + context.getString(R.string.rc_years_of_age));
        holder.patientSex.setText(context.getString(patientListDTOS.get(position).getSex() == 1 ? R.string.rc_man : R.string.rc_woman));
        return convertView;
    }

    static class ViewHolder {
        TextView patientName;
        TextView patientAge;
        TextView patientSex;
    }


}



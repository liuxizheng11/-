package com.rocedar.sdk.familydoctor.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rocedar.sdk.familydoctor.R;
import com.rocedar.sdk.familydoctor.dto.mingyi.RCMingYiDoctorListSelectlDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：lxz
 * 日期：2018/7/19 下午6:01
 * 版本：V1.0
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class MingYiDoctorSubAdapter extends BaseAdapter {
    private Context context;
    private int selected_id;
    private LayoutInflater inflater;
    private List<RCMingYiDoctorListSelectlDTO.childsDTO> childsDTOS = new ArrayList<>();

    public void setSelectedPosition(int id) {
        this.selected_id = id;
    }

    public MingYiDoctorSubAdapter(Context context, List<RCMingYiDoctorListSelectlDTO.childsDTO> list
    ) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.childsDTOS = list;
    }

    @Override
    public int getCount() {
        return childsDTOS.size();
    }

    @Override
    public Object getItem(int position) {
        return childsDTOS.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.rc_mingyi_adapter_doctor_root,
                    null);
            holder.item_text = convertView
                    .findViewById(R.id.rc_mingyi_adapter_text);
            holder.rc_mingyi_adapter_view = convertView
                    .findViewById(R.id.rc_mingyi_adapter_view);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        /**
         * 该项被选中时改变背景色
         */
        if (selected_id == childsDTOS.get(position).getId()) {
            holder.rc_mingyi_adapter_view.setVisibility(View.VISIBLE);
        } else {
            holder.rc_mingyi_adapter_view.setVisibility(View.GONE);
        }
        holder.item_text.setText(childsDTOS.get(position).getName());
        return convertView;
    }

    class ViewHolder {
        TextView item_text;
        View rc_mingyi_adapter_view;
    }

}

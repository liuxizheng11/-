package com.rocedar.sdk.familydoctor.app.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rocedar.sdk.familydoctor.R;
import com.rocedar.sdk.familydoctor.dto.mingyi.RCMingYiDoctorListSelectlDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：lxz
 * 日期：2018/7/19 下午5:50
 * 版本：V1.0
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class MingYiDoctorRootAdapter extends BaseAdapter {
    private Context context;

    private LayoutInflater inflater;

    private List<RCMingYiDoctorListSelectlDTO> mList = new ArrayList<>();

    private int selectedPosition = -1;

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
    }


    public MingYiDoctorRootAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }


    public void setItems(List<RCMingYiDoctorListSelectlDTO> list) {
        this.mList = list;
    }


    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
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
            convertView = inflater.inflate(R.layout.rc_mingyi_adapter_doctor_root, null);
            holder.rc_mingyi_adapter_text = convertView.findViewById(R.id.rc_mingyi_adapter_text);
            holder.rc_mingyi_adapter_view = convertView.findViewById(R.id.rc_mingyi_adapter_view);
            holder.rc_mingyi_adapter_ll = convertView.findViewById(R.id.rc_mingyi_adapter_ll);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        /**
         * 该项被选中时改变背景色
         */
        if (selectedPosition == position) {
            holder.rc_mingyi_adapter_view.setVisibility(View.VISIBLE);
        } else {
            holder.rc_mingyi_adapter_view.setVisibility(View.GONE);
        }
        holder.rc_mingyi_adapter_text.setText(mList.get(position).getName());
        return convertView;
    }

    class ViewHolder {
        TextView rc_mingyi_adapter_text;
        View rc_mingyi_adapter_view;
        LinearLayout rc_mingyi_adapter_ll;
    }

}

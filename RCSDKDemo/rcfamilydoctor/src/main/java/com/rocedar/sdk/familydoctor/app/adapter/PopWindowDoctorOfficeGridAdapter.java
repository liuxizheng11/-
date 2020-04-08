package com.rocedar.sdk.familydoctor.app.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rocedar.lib.base.unit.RCDrawableUtil;
import com.rocedar.sdk.familydoctor.R;
import com.rocedar.sdk.familydoctor.dto.RCFDDepartmentDTO;

import java.util.ArrayList;


/**
 * @author liuyi
 * @date 2017/4/19
 * @desc 科室筛选（家庭医生）
 * @veison V3.4.00新增
 */

public class PopWindowDoctorOfficeGridAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater inflater;
    private ArrayList<RCFDDepartmentDTO> mDatas;
    public int mSelectPos = 0;


    public PopWindowDoctorOfficeGridAdapter(ArrayList<RCFDDepartmentDTO> mDatas, Context mContext) {
        this.mDatas = mDatas;
        this.mContext = mContext;
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
            convertView = inflater.inflate(R.layout.rc_fd_activity_specialist_shared_name_griditem, parent, false);
            holder = new ViewHolder();
            holder.tv_item_family_shared_name = (TextView) convertView.findViewById(R.id.tv_item_family_shared_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tv_item_family_shared_name.setText(mDatas.get(position).getDepartment_name());
        if (position == mSelectPos) {
            holder.tv_item_family_shared_name.setBackground(
                    RCDrawableUtil.getDarkMainColorDrawable(mContext,
                            RCDrawableUtil.getThemeAttrDimension(mContext, R.attr.RCButtonRadius))
            );
            holder.tv_item_family_shared_name.setTextColor(
                    RCDrawableUtil.getThemeAttrColor(mContext, R.attr.RCLightColor)
            );
        } else {
            holder.tv_item_family_shared_name.setBackground(
                    RCDrawableUtil.getDrawableStroke(mContext,
                            Color.TRANSPARENT, 0.34f, Color.parseColor("#cccccc"),
                            RCDrawableUtil.getThemeAttrDimension(mContext, R.attr.RCButtonRadius)));
            holder.tv_item_family_shared_name.setTextColor(Color.parseColor("#666666")
            );
        }
        return convertView;
    }

    static class ViewHolder {
        TextView tv_item_family_shared_name;
    }
}


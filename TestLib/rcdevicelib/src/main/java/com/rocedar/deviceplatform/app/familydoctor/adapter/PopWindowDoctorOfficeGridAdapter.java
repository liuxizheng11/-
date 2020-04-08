package com.rocedar.deviceplatform.app.familydoctor.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rocedar.deviceplatform.R;
import com.rocedar.deviceplatform.app.familydoctor.IFamilyDoctorPlatformUtil;
import com.rocedar.deviceplatform.dto.familydoctor.RCFDDepartmentDTO;

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
    private int btnBackgroundSelected;
    private int btnBackgroundNone;
    private int btnTextSelected;
    private int btnTextNone;

    private IFamilyDoctorPlatformUtil platformUtil;

    public PopWindowDoctorOfficeGridAdapter(ArrayList<RCFDDepartmentDTO> mDatas, Context mContext
            , IFamilyDoctorPlatformUtil platformUtil) {
        this.mDatas = mDatas;
        this.mContext = mContext;
        this.platformUtil = platformUtil;
        this.inflater = inflater.from(mContext);
        btnBackgroundSelected = platformUtil.getChooseOfficeItemSelected();
        btnBackgroundNone = platformUtil.getChooseOfficeItemRetract();
        btnTextSelected = platformUtil.getChooseOfficeItemTextSelected(mContext);
        btnTextNone = platformUtil.getChooseOfficeItemTextRetract(mContext);
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
            convertView = inflater.inflate(R.layout.activity_family_shared_name_griditem, parent, false);
            holder = new ViewHolder();
            holder.tv_item_family_shared_name = (TextView) convertView.findViewById(R.id.tv_item_family_shared_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tv_item_family_shared_name.setText(mDatas.get(position).getDepartment_name());
        if (position == mSelectPos) {
            holder.tv_item_family_shared_name.setBackgroundResource(btnBackgroundSelected);
            holder.tv_item_family_shared_name.setTextColor(btnTextSelected);
        } else {
            holder.tv_item_family_shared_name.setBackgroundResource(btnBackgroundNone);
            holder.tv_item_family_shared_name.setTextColor(btnTextNone);
        }
        return convertView;
    }

    static class ViewHolder {
        TextView tv_item_family_shared_name;
    }
}


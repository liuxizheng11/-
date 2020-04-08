package com.rocedar.deviceplatform.app.behavior.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rocedar.base.RCDateUtil;
import com.rocedar.deviceplatform.R;
import com.rocedar.deviceplatform.dto.behaviorlibrary.RCBehaviorLibraryDietDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：lxz
 * 日期：17/7/27 下午9:45
 * 版本：V1.0
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class DietRecordAdapter extends BaseAdapter {
    private Context mContext;
    private List<RCBehaviorLibraryDietDTO> mList = new ArrayList<>();

    public DietRecordAdapter(Context mContext, List<RCBehaviorLibraryDietDTO> mList) {
        this.mContext = mContext;
        this.mList = mList;
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
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_diet_record, null);
            viewHolder = new ViewHolder();
            viewHolder.adapterDietStartTime = (TextView) convertView.findViewById(R.id.adapter_diet_start_time);
            viewHolder.adapterDietNumber = (TextView) convertView.findViewById(R.id.adapter_diet_number);
            viewHolder.adapterDietFrom = (TextView) convertView.findViewById(R.id.adapter_diet_from);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        initView(position, viewHolder);
        return convertView;
    }

    private void initView(int position, ViewHolder viewHolder) {
        viewHolder.adapterDietStartTime.setText(RCDateUtil.formatServiceTime(
                mList.get(position).getData_time() + "", "MM-dd  HH:mm:ss"));
        viewHolder.adapterDietFrom.setText("来自" + mList.get(position).getDevice_name());
        viewHolder.adapterDietNumber.setText(mList.get(position).getCounts() + "条记录");
    }

    static class ViewHolder {
        TextView adapterDietStartTime;
        TextView adapterDietNumber;
        TextView adapterDietFrom;
    }
}

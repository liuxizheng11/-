package com.rocedar.deviceplatform.app.devicelist.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.rocedar.base.RCImageShow;
import com.rocedar.deviceplatform.R;
import com.rocedar.deviceplatform.dto.data.RCDeviceDataListDTO;

import java.util.ArrayList;

/**
 * @author liuyi
 * @date 2017/2/13
 * @desc 设备类型列表adapter
 * @veison V1.0
 */

public class DeviceChooseListGridAdapter extends BaseAdapter {
    private ArrayList<RCDeviceDataListDTO> mDatas;
    private Context context;
    private LayoutInflater inflater;

    public DeviceChooseListGridAdapter(ArrayList<RCDeviceDataListDTO> mDatas, Context context) {
        this.mDatas = mDatas;
        this.context = context;
        this.inflater = inflater.from(context);
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
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_grid_device_list_new, parent, false);
            holder = new ViewHolder();
            holder.iv_item_choice_device_icon = (ImageView) convertView.findViewById(R.id.iv_item_choice_device_icon);
            holder.tv_item_choice_device_name = (TextView) convertView.findViewById(R.id.tv_item_choice_device_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        RCDeviceDataListDTO subListDTO = mDatas.get(position);
        holder.tv_item_choice_device_name.setText(subListDTO.getDevice_name());
        RCImageShow.loadUrl(subListDTO.getDevice_logo(), holder.iv_item_choice_device_icon, RCImageShow.IMAGE_TYPE_NINE);
        return convertView;
    }

    static class ViewHolder {
        ImageView iv_item_choice_device_icon;
        TextView tv_item_choice_device_name;
    }
}


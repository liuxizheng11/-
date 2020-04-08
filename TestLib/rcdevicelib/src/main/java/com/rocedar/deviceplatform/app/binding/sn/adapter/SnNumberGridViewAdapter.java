package com.rocedar.deviceplatform.app.binding.sn.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.rocedar.base.RCImageShow;
import com.rocedar.deviceplatform.R;
import com.rocedar.deviceplatform.dto.data.RCDeviceSnDetailsDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lxz on 17/2/11.
 */

public class SnNumberGridViewAdapter extends BaseAdapter {
    private Context mContext;
    private List<RCDeviceSnDetailsDTO.RolesBean> mList = new ArrayList<>();

    /**
     * 选中状态
     */
    private List<Integer> select_list;

    public SnNumberGridViewAdapter(Context mContext, List<RCDeviceSnDetailsDTO.RolesBean> mList, List<Integer> list) {
        this.mContext = mContext;
        this.mList = mList;
        this.select_list = list;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_grid_include_sn, null);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.sn_grld_iv);
            viewHolder.select = (ImageView) convertView.findViewById(R.id.sn_grld_select_iv);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        RCImageShow.loadUrl(mList.get(position).getRole_img(), viewHolder.imageView,
                RCImageShow.IMAGE_TYPE_NINE);
        viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select_list.add(position);
                notifyDataSetChanged();
            }
        });
        if (select_list.size() > 0
                && select_list.get(select_list.size() - 1) == (position)) {
            viewHolder.select.setVisibility(View.VISIBLE);
        } else {
            viewHolder.select.setVisibility(View.GONE);
        }
        return convertView;
    }

    class ViewHolder {
        ImageView imageView;
        ImageView select;
    }
}

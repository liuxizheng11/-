package com.rocedar.deviceplatform.app.binding.sn.adapter;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rocedar.deviceplatform.R;
import com.rocedar.deviceplatform.app.binding.sn.RCSnNumberActivity;
import com.rocedar.deviceplatform.dto.data.RCDeviceSnDetailsDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lxz on 17/1/13.
 */

public class EchoDataAdapter extends BaseAdapter {
    private RCSnNumberActivity mContext;
    private List<RCDeviceSnDetailsDTO.RelationsBean> mList = new ArrayList<>();

    public EchoDataAdapter(RCSnNumberActivity mContext, List<RCDeviceSnDetailsDTO.RelationsBean> mList) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_list_sn_echo_data, null);
            viewHolder.echo_user_name = (TextView) convertView.findViewById(R.id.echo_user_name);
            viewHolder.echo_user_sn = (TextView) convertView.findViewById(R.id.echo_user_sn);
            viewHolder.echo_click = (TextView) convertView.findViewById(R.id.echo_click);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.echo_user_sn.setText("SN码 :" + mList.get(position).getDevice_no());

        /**键位 展示*/
        /**家人名字为空  键位名为空*/
        if (mList.get(position).getRelation_name().equals("") && mList.get(position).getDevice_role_name().equals("")) {
            viewHolder.echo_user_name.setVisibility(View.GONE);
        }
        /**家人名字为空  键位名不为空*/
        else if (mList.get(position).getRelation_name().equals("") && !mList.get(position).getDevice_role_name().equals("")) {
            viewHolder.echo_user_name.setText(mList.get(position).getDevice_role_name());
        }
        /**家人名字不为空  键位名为空*/
        else if (!mList.get(position).getRelation_name().equals("") && mList.get(position).getDevice_role_name().equals("")){
            viewHolder.echo_user_name.setText(mList.get(position).getRelation_name());
        }
        /**家人名字不为空  键位名不为空*/
        else {
            viewHolder.echo_user_name.setText(mList.get(position).getDevice_role_name() + " :"
                    + mList.get(position).getRelation_name());
        }

        viewHolder.echo_click.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        viewHolder.echo_click.getPaint().setAntiAlias(true);//抗锯齿
        viewHolder.echo_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.echoData(position);
            }
        });
        return convertView;
    }

    class ViewHolder {
        TextView echo_user_name;
        TextView echo_user_sn;
        TextView echo_click;
    }
}

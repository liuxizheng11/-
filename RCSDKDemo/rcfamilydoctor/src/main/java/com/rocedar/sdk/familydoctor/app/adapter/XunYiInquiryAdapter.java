package com.rocedar.sdk.familydoctor.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rocedar.lib.base.unit.RCDateUtil;
import com.rocedar.lib.base.unit.RCDrawableUtil;
import com.rocedar.sdk.familydoctor.R;
import com.rocedar.sdk.familydoctor.dto.xunyi.RCXunYiInquiryDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：lxz
 * 日期：2018/11/6 4:31 PM
 * 版本：V1.0
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class XunYiInquiryAdapter extends BaseAdapter {
    private Context mContext;
    private List<RCXunYiInquiryDTO> mList = new ArrayList<>();

    public XunYiInquiryAdapter(Context mContext, List<RCXunYiInquiryDTO> mList) {
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
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.rc_adapter_xun_yi_inquiry, null);
            viewHolder = new ViewHolder();
            viewHolder.rc_adapter_xun_yi_inquiry_time_tv = convertView.findViewById(R.id.rc_adapter_xun_yi_inquiry_time_tv);
            viewHolder.rc_adapter_xun_yi_inquiry_server = convertView.findViewById(R.id.rc_adapter_xun_yi_inquiry_server);
            viewHolder.rc_adapter_xun_yi_inquiry_server_money = convertView.findViewById(R.id.rc_adapter_xun_yi_inquiry_server_money);
            viewHolder.rc_adapter_xun_yi_inquiry_server_content = convertView.findViewById(R.id.rc_adapter_xun_yi_inquiry_server_content);
            convertView.setTag(viewHolder);
        } else {
            viewHolder= (ViewHolder) convertView.getTag();
        }

        initData(viewHolder, position);
        return convertView;
    }

    private void initData(ViewHolder viewHolder, int position) {
        RCXunYiInquiryDTO mDTO = mList.get(position);
        //日期格式
        viewHolder.rc_adapter_xun_yi_inquiry_time_tv.setText(RCDateUtil.formatTime(mDTO.getDate_str()+"", "yyyy-MM-dd"));
        //收费金额
        viewHolder.rc_adapter_xun_yi_inquiry_server_money.setText("收费问诊 ¥" + mDTO.getFee());
        //问题描述
        viewHolder.rc_adapter_xun_yi_inquiry_server_content.setText(mDTO.getQuestion());
        //服务状态 0，已结束；1，正在咨询
        if (mDTO.getStatus() == 1) {
            viewHolder.rc_adapter_xun_yi_inquiry_server.setText("服务中");
            viewHolder.rc_adapter_xun_yi_inquiry_server.setTextColor(RCDrawableUtil.getThemeAttrColor(mContext, R.attr.RCDarkColor));
        } else {
            viewHolder.rc_adapter_xun_yi_inquiry_server.setText("已结束");
            viewHolder.rc_adapter_xun_yi_inquiry_server.setTextColor(mContext.getResources().getColor(R.color.rc_text_color_light));
        }
    }

    class ViewHolder {
        TextView rc_adapter_xun_yi_inquiry_server;
        TextView rc_adapter_xun_yi_inquiry_server_money;
        TextView rc_adapter_xun_yi_inquiry_server_content;
        TextView rc_adapter_xun_yi_inquiry_time_tv;
    }
}

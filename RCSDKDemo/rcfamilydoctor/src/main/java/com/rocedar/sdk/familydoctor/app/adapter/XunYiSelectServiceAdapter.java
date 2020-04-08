package com.rocedar.sdk.familydoctor.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rocedar.lib.base.unit.RCImageShow;
import com.rocedar.sdk.familydoctor.R;
import com.rocedar.sdk.shop.dto.RCServerGoodsInfoDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：lxz
 * 日期：2018/11/7 3:41 PM
 * 版本：V1.0
 * 描述：寻医问诊
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class XunYiSelectServiceAdapter extends BaseAdapter {
    private Context mContext;
    private List<RCServerGoodsInfoDTO> mList;

    public XunYiSelectServiceAdapter(Context mContext, List<RCServerGoodsInfoDTO> mList) {
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
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.rc_adapter_xun_yi_service, null);
            viewHolder.rc_adapter_xun_yi_service_head = convertView.findViewById(R.id.rc_adapter_xun_yi_service_head);
            viewHolder.rc_adapter_xun_yi_service_content = convertView.findViewById(R.id.rc_adapter_xun_yi_service_content);
            viewHolder.rc_adapter_xun_yi_service_money = convertView.findViewById(R.id.rc_adapter_xun_yi_service_money);
            viewHolder.rc_adapter_xun_yi_service_select = convertView.findViewById(R.id.rc_adapter_xun_yi_service_select);
            viewHolder.rc_adapter_xun_yi_service_content_rl = convertView.findViewById(R.id.rc_adapter_xun_yi_service_content_rl);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        initView(viewHolder, position);
        return convertView;
    }

    private void initView(ViewHolder viewHolder, final int position) {
        final RCServerGoodsInfoDTO mDTO = mList.get(position);
        RCImageShow.loadUrl(mDTO.getSku_icon(), viewHolder.rc_adapter_xun_yi_service_head, RCImageShow.IMAGE_TYPE_HEAD);
        viewHolder.rc_adapter_xun_yi_service_content.setText(mDTO.getSkuName());
        viewHolder.rc_adapter_xun_yi_service_money.setText("¥" + mDTO.getFee());


        viewHolder.rc_adapter_xun_yi_service_content_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click(position);
            }
        });
        viewHolder.rc_adapter_xun_yi_service_select.setChecked(mDTO.isSelect());
    }

    class ViewHolder {
        RelativeLayout rc_adapter_xun_yi_service_content_rl;
        ImageView rc_adapter_xun_yi_service_head;
        TextView rc_adapter_xun_yi_service_content;
        TextView rc_adapter_xun_yi_service_money;
        RadioButton rc_adapter_xun_yi_service_select;
    }

    public void click(int position) {
        RCServerGoodsInfoDTO mDTO = mList.get(position);
        if (!mDTO.isSelect()) {
            mDTO.setSelect(true);
            for (int i = 0; i < mList.size(); i++) {
                if (i != position) {
                    mList.get(i).setSelect(false);
                }
            }
            XunYiSelectServiceAdapter.this.notifyDataSetChanged();
        }
    }
}

package com.rocedar.sdk.shop.app.goods.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.rocedar.lib.base.unit.RCDateUtil;
import com.rocedar.lib.base.unit.RCDrawableUtil;
import com.rocedar.sdk.shop.R;
import com.rocedar.sdk.shop.dto.RCOrderFromParticularsProgressDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：lxz
 * 日期：2018/7/12 下午6:33
 * 版本：V1.0
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class ServerGoodsOrderParticularsAdapter extends BaseAdapter {
    private Context mContext;
    private List<RCOrderFromParticularsProgressDTO> mList = new ArrayList<>();

    public ServerGoodsOrderParticularsAdapter(Context mContext, List<RCOrderFromParticularsProgressDTO> mList) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.rc_server_goods_adapter_particulars, null);
            viewHolder.rc_adapter_order_particulars_topview = convertView.findViewById(R.id.rc_server_adapter_order_particulars_topview);
            viewHolder.rc_adapter_order_particulars_message_iv = convertView.findViewById(R.id.rc_server_adapter_order_particulars_message_iv);
            viewHolder.rc_adapter_order_particulars_message = convertView.findViewById(R.id.rc_server_adapter_order_particulars_message);
            viewHolder.rc_adapter_order_particulars_message_time = convertView.findViewById(R.id.rc_server_adapter_order_particulars_message_time);
            viewHolder.rc_adapter_order_particulars_message_gray_iv = convertView.findViewById(R.id.rc_server_adapter_order_particulars_message_gray_iv);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        initView(viewHolder, position);


        return convertView;
    }

    private void initView(ViewHolder viewHolder, int position) {
        if (position == 0) {
            viewHolder.rc_adapter_order_particulars_message_iv.setVisibility(View.VISIBLE);
            viewHolder.rc_adapter_order_particulars_message_gray_iv.setVisibility(View.GONE);

            viewHolder.rc_adapter_order_particulars_message_iv.setBackground(RCDrawableUtil.getDoubleCircle(mContext));
            viewHolder.rc_adapter_order_particulars_topview.setVisibility(View.GONE);


        } else {
            viewHolder.rc_adapter_order_particulars_message_gray_iv.setVisibility(View.VISIBLE);
            viewHolder.rc_adapter_order_particulars_message_iv.setVisibility(View.GONE);

            viewHolder.rc_adapter_order_particulars_topview.setVisibility(View.VISIBLE);
        }
        RCOrderFromParticularsProgressDTO mDTO = mList.get(position);
        viewHolder.rc_adapter_order_particulars_message.setText(mDTO.getOrder_desc());
        viewHolder.rc_adapter_order_particulars_message_time.setText(RCDateUtil.formatTime(mDTO.getOrder_time() + "",
                "yyyy年M月d日 HH:mm"));

    }

    class ViewHolder {
        View rc_adapter_order_particulars_topview;
        View rc_adapter_order_particulars_message_iv;
        ImageView rc_adapter_order_particulars_message_gray_iv;
        TextView rc_adapter_order_particulars_message;
        TextView rc_adapter_order_particulars_message_time;


    }
}

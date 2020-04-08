package com.rocedar.sdk.shop.app.goods.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rocedar.lib.base.unit.RCDrawableUtil;
import com.rocedar.lib.base.unit.RCTPJump;
import com.rocedar.sdk.shop.R;
import com.rocedar.sdk.shop.app.goods.ServerGoodsCardActivity;
import com.rocedar.sdk.shop.dto.RCServerGoodsListDTO;

import java.util.List;

/**
 * 项目名称：瑰柏SDK-商城
 * <p>
 * 作者：phj
 * 日期：2018/9/25 下午4:36
 * 版本：V1.1.00
 * 描述：瑰柏SDK-服务商品
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class ServerGoodsListAdapter extends BaseAdapter {


    private Context context;
    private List<RCServerGoodsListDTO> dtoList;

    public ServerGoodsListAdapter(Context context, List<RCServerGoodsListDTO> dtoList) {
        this.context = context;
        this.dtoList = dtoList;
    }

    @Override
    public int getCount() {
        return dtoList.size();
    }

    @Override
    public Object getItem(int position) {
        return dtoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.rc_fragment_goods_list_item, null);
            holder = new ViewHolder();
            holder.buyNow = convertView.findViewById(R.id.rc_f_goods_list_item_buy);
            holder.goodsInfo = convertView.findViewById(R.id.rc_f_goods_list_item_info);
            holder.goodsName = convertView.findViewById(R.id.rc_f_goods_list_item_name);
            holder.buyNow.setBackground(RCDrawableUtil.getMainColorDrawableBaseRadius(context));
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.goodsName.setText(dtoList.get(position).getGoods_Name());
        holder.goodsInfo.setText(dtoList.get(position).getGoods_desc());
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (dtoList.get(position).getGoods_id()) {

                    //家庭医生、协议无忧
                    case 100001:
                    case 100002:
                        ServerGoodsCardActivity.goActivity(context,
                                dtoList.get(position).getGoods_id());
                        break;
                    case  100003:
                        RCTPJump.ActivityJump(context,"rctp://android##com.rocedar.sdk.familydoctor.app.RCXunYiUserPerfectActivity##{}");
                        break;
                }

            }
        });
        return convertView;
    }

    static class ViewHolder {
        TextView goodsName;
        TextView goodsInfo;
        TextView buyNow;
    }


}

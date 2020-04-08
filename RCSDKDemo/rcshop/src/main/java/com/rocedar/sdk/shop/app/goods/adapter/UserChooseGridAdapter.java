package com.rocedar.sdk.shop.app.goods.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rocedar.lib.base.unit.RCDrawableUtil;
import com.rocedar.sdk.shop.R;
import com.rocedar.sdk.shop.dto.RCShopChooseUserDTO;

import java.util.List;


/**
 * @author liuyi
 * @date 2017/4/19
 * @desc 科室筛选（家庭医生）
 * @veison V3.4.00新增
 */

public class UserChooseGridAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater inflater;
    private List<RCShopChooseUserDTO> mDatas;
    public int mSelectPos = 0;


    public UserChooseGridAdapter(List<RCShopChooseUserDTO> mDatas, Context mContext) {
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
            convertView = inflater.inflate(R.layout.rc_fragment_goods_choose_user_dadpter, parent, false);
            holder = new ViewHolder();
            holder.userNameTextView = convertView.findViewById(R.id.rc_f_goods_choose_user_adapter_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.userNameTextView.setText(mDatas.get(position).getUserNickName());
        if (position == mSelectPos) {
            holder.userNameTextView.setBackground(RCDrawableUtil.getMainColorDrawableBaseRadius(mContext));
            holder.userNameTextView.setTextColor(Color.WHITE);
        } else {
            holder.userNameTextView.setBackground(RCDrawableUtil.getColorDrawableBaseRadius(mContext,
                    Color.parseColor("#ececec")));
            holder.userNameTextView.setTextColor(Color.parseColor("#666666"));
        }
        return convertView;
    }

    static class ViewHolder {
        TextView userNameTextView;
    }
}


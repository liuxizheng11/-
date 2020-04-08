package com.rcoedar.sdk.healthclass.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rcoedar.sdk.healthclass.R;
import com.rcoedar.sdk.healthclass.dto.RCHealthClassSearchDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：lxz
 * 日期：2018/9/13 上午8:47
 * 版本：V1.0
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCHealthClassSearchAdapter extends BaseAdapter {
    private Context mContext;
    private List<RCHealthClassSearchDTO> mList = new ArrayList<>();

    public RCHealthClassSearchAdapter(Context mContext, List<RCHealthClassSearchDTO> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.rc_adapter_health_class_search, null);

            viewHolder.rc_adapter_health_class_search_name = view.findViewById(R.id.rc_adapter_health_class_search_name);
            viewHolder.rc_adapter_health_class_search_tag = view.findViewById(R.id.rc_adapter_health_class_search_tag);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        RCHealthClassSearchDTO mDTO = mList.get(i);

        viewHolder.rc_adapter_health_class_search_name.setText(mDTO.getTitle());
        viewHolder.rc_adapter_health_class_search_tag.setText(mDTO.getType_name());

        return view;
    }

    class ViewHolder {
        TextView rc_adapter_health_class_search_name;
        TextView rc_adapter_health_class_search_tag;
    }
}

package com.rocedar.sdk.assessment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.rocedar.lib.base.unit.RCDrawableUtil;
import com.rocedar.lib.base.unit.RCImageShow;
import com.rocedar.sdk.assessment.R;
import com.rocedar.sdk.assessment.dto.RCAssessmentListDTO;

import java.util.List;

/**
 * 项目名称：瑰柏SDK-基础库
 * <p>
 * 作者：lxz
 * 日期：2018/6/26 上午11:59
 * 版本：V1.0.00
 * 描述：瑰柏SDK-
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class AssessmentListAdapter extends BaseAdapter {

    private Context mContext;
    private List<RCAssessmentListDTO> mDatas;
    private LayoutInflater inflater;

    public AssessmentListAdapter(Context mContext, List<RCAssessmentListDTO> mDatas) {
        this.mContext = mContext;
        this.mDatas = mDatas;
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
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.rc_fragment_assessment_listitem, parent, false);
            holder = new ViewHolder();
            holder.iv_item_health_evaluation_icon = (ImageView) convertView.findViewById(R.id.iv_item_health_evaluation_icon);
            holder.tv_item_health_evaluation_title = (TextView) convertView.findViewById(R.id.tv_item_health_evaluation_title);
            holder.tv_item_health_evaluation_content = (TextView) convertView.findViewById(R.id.tv_item_health_evaluation_content);
            holder.tv_item_health_evaluation_fillout = (TextView) convertView.findViewById(R.id.tv_item_health_evaluation_fillout);
            holder.tv_item_health_evaluation_fillout.setBackground(RCDrawableUtil.getMainColorDrawableBaseRadius(mContext));
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_item_health_evaluation_content.setText(mDatas.get(position).getQuestionnaire_desc());
        holder.tv_item_health_evaluation_title.setText(mDatas.get(position).getQuestionnaire_name());
        RCImageShow.loadUrl(mDatas.get(position).getThumbnail(), holder.iv_item_health_evaluation_icon,
                RCImageShow.IMAGE_TYPE_HEAD);
        return convertView;
    }

    static class ViewHolder {
        ImageView iv_item_health_evaluation_icon;
        TextView tv_item_health_evaluation_title;
        TextView tv_item_health_evaluation_content;
        TextView tv_item_health_evaluation_fillout;
    }

}

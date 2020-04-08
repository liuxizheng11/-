package com.rocedar.deviceplatform.app.behavior.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rocedar.base.RCDateUtil;
import com.rocedar.deviceplatform.R;
import com.rocedar.deviceplatform.dto.behaviorlibrary.RCBehaviorRecordDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：lxz
 * 日期：17/7/17 下午6:02
 * 版本：V1.0
 * 描述：睡眠记录 adapter
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class SleepRecordAdapter extends BaseAdapter {

    private Context mContext;
    private List<RCBehaviorRecordDTO.RCSleepDayDTO> mList = new ArrayList<>();

    public SleepRecordAdapter(Context mContext, List<RCBehaviorRecordDTO.RCSleepDayDTO> mList) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_sleep_record, null);
            viewHolder = new ViewHolder();
            viewHolder.adapterSleepInvalid = (TextView) convertView.findViewById(R.id.adapter_sleep_invalid);
            viewHolder.adapterSleepStartTime = (TextView) convertView.findViewById(R.id.adapter_sleep_start_time);
            viewHolder.adapterSleepTime = (TextView) convertView.findViewById(R.id.adapter_sleep_time);
            viewHolder.adapterSleepDurationTime = (TextView) convertView.findViewById(R.id.adapter_sleep_duration_time);
            viewHolder.adapterSleepFrom = (TextView) convertView.findViewById(R.id.adapter_sleep_from);
            viewHolder.adapterSleepRl = (RelativeLayout) convertView.findViewById(R.id.adapter_sleep_rl);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        initView(viewHolder, position);
        return convertView;
    }

    private void initView(ViewHolder viewHolder, int position) {
        /**
         *  时长
         */
        String time = RCDateUtil.getWeekOfDateNew(RCDateUtil.formatServiceTime(
                mList.get(position).getFall_time() + "", "yyyyMMdd"));
        viewHolder.adapterSleepStartTime.setText(RCDateUtil.formatServiceTime(mList.get(position).
                getFall_time() + "", "MM-dd") + time);
        /**
         *  开始时间--结束时间
         */
        viewHolder.adapterSleepTime.setText(RCDateUtil.formatServiceTime(mList.get(position).getFall_time() + "",
                "HH:mm") + "--" + RCDateUtil.formatServiceTime(mList.get(position).getWake_time() + "", "HH:mm"));
        /**
         *  睡眠时长
         */
        viewHolder.adapterSleepDurationTime.setText(minConvertHourMinString(mList.get(position).getSleep_time()));
        /**
         *  设备来源
         */
        viewHolder.adapterSleepFrom.setText("来自" + mList.get(position).getDevice_name());
        /**
         * int	0：无效  1有效
         */
        if (mList.get(position).getIs_active() > 0) {
            //背景色
            viewHolder.adapterSleepRl.setBackgroundResource(R.color.white);
            //无效
            viewHolder.adapterSleepInvalid.setVisibility(View.GONE);
            //时长
            viewHolder.adapterSleepStartTime.setTextColor(mContext.getResources().getColor(R.color.fragment_record_app_text));
            // 开始时间--结束时间
            viewHolder.adapterSleepTime.setTextColor(mContext.getResources().getColor(R.color.fragment_record_app_text));
            //睡眠时长
            viewHolder.adapterSleepDurationTime.setTextColor(mContext.getResources().getColor(R.color.fragment_record_app_text));
            //设备来源
            viewHolder.adapterSleepFrom.setTextColor(mContext.getResources().getColor(R.color.fragment_record_app_text));
        } else {
            //背景色
            viewHolder.adapterSleepRl.setBackgroundResource(R.color.fragment_record_bg);
            //无效
            viewHolder.adapterSleepInvalid.setVisibility(View.VISIBLE);
            //时长
            viewHolder.adapterSleepStartTime.setTextColor(mContext.getResources().getColor(R.color.circle_number_text));
            // 开始时间--结束时间
            viewHolder.adapterSleepTime.setTextColor(mContext.getResources().getColor(R.color.circle_number_text));
            //睡眠时长
            viewHolder.adapterSleepDurationTime.setTextColor(mContext.getResources().getColor(R.color.circle_number_text));
            //设备来源
            viewHolder.adapterSleepFrom.setTextColor(mContext.getResources().getColor(R.color.circle_number_text));

        }
    }


    private String minConvertHourMinString(int min) {
        String time = "";
        if (min > 0) {
            int hour = min / 60;
            int minute = min - hour * 60;
            time = String.format("%01d", hour) + "h" + String.format("%02d", minute) + "min";
        } else {
            time = "0min";
        }
        return time;
    }

    class ViewHolder {
        TextView adapterSleepInvalid;
        TextView adapterSleepStartTime;
        TextView adapterSleepTime;
        TextView adapterSleepDurationTime;
        TextView adapterSleepFrom;
        RelativeLayout adapterSleepRl;
    }
}

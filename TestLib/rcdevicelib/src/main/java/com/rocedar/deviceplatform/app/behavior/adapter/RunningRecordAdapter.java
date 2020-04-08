package com.rocedar.deviceplatform.app.behavior.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rocedar.base.RCDateUtil;
import com.rocedar.base.RCJavaUtil;
import com.rocedar.deviceplatform.R;
import com.rocedar.deviceplatform.config.RCDeviceConductID;
import com.rocedar.deviceplatform.dto.behaviorlibrary.RCBehaviorRecordDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：lxz
 * 日期：17/7/17 下午6:02
 * 版本：V1.0
 * 描述：跑步记录 adapter
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RunningRecordAdapter extends BaseAdapter {
    private Context mContext;
    private List<RCBehaviorRecordDTO.RCRunDTO> mList = new ArrayList<>();

    private int conduct_id;

    public RunningRecordAdapter(Context mContext, int conductId, List<RCBehaviorRecordDTO.RCRunDTO> mList) {
        this.mContext = mContext;
        this.mList = mList;
        this.conduct_id = conductId;
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_running_record_item, null);
            viewHolder.adapterRuningInvalid = (TextView) convertView.findViewById(R.id.adapter_runing_invalid);
            viewHolder.adapterRuningStartTime = (TextView) convertView.findViewById(R.id.adapter_runing_start_time);
            viewHolder.adapterRuningType = (TextView) convertView.findViewById(R.id.adapter_runing_type);
            viewHolder.adapterRuningKmNumber = (TextView) convertView.findViewById(R.id.adapter_runing_km_number);
            viewHolder.adapterRuningDurationTime = (TextView) convertView.findViewById(R.id.adapter_runing_duration_time);
            viewHolder.adapterRuningSpeed = (TextView) convertView.findViewById(R.id.adapter_runing_speed);
            viewHolder.adapterRuningFrom = (TextView) convertView.findViewById(R.id.adapter_runing_from);
            viewHolder.adapterRuningRl = (RelativeLayout) convertView.findViewById(R.id.adapter_runing_rl);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        initData(viewHolder, position);
        return convertView;
    }


    private void initData(ViewHolder viewHolder, int position) {

//        开始时间
        if (conduct_id == RCDeviceConductID.EXERCISE_CALCULATE) {
            viewHolder.adapterRuningStartTime.setText(RCDateUtil.formatServiceTime(mList.get(position).getData_time() + "", "yyyy-MM-dd"));
        } else {
            viewHolder.adapterRuningStartTime.setText(RCDateUtil.formatServiceTime(mList.get(position).getData_time() + "", "MM-dd  HH:mm:ss"));
        }
//        //类型  1室内，2室外
//        if (mList.get(position).getEnvironment() == 1) {
//            if (mList.get(position).getConduct_id() == RCDeviceConductID.RUN) {
//                viewHolder.adapterRuningType.setText("室内跑");
//            } else {
//                viewHolder.adapterRuningType.setText("室内骑行");
//            }
//
//        } else if (mList.get(position).getEnvironment() == 2) {
//            if (mList.get(position).getConduct_id() == RCDeviceConductID.RUN) {
//                viewHolder.adapterRuningType.setText("户外跑");
//            } else {
//                viewHolder.adapterRuningType.setText("户外骑行");
//            }
//
//        } else if (mList.get(position).getEnvironment() == -1) {
//            if (mList.get(position).getConduct_id() == RCDeviceConductID.RUN) {
//                viewHolder.adapterRuningType.setText("跑步");
//            } else {
//                viewHolder.adapterRuningType.setText("骑行");
//            }
//        }

        //时长
        viewHolder.adapterRuningDurationTime.setText(RCJavaUtil.minConvertHourString(
                mList.get(position).getTime()));
        //距离
        if (mList.get(position).getDistance() < 0) {
            viewHolder.adapterRuningKmNumber.setText("-- km");
        } else {
            viewHolder.adapterRuningKmNumber.setText(mList.get(position).getDistance() + "km");
        }
        //来源
        if (!mList.get(position).getDevice_name().equals("")) {
            viewHolder.adapterRuningFrom.setText("来自" + mList.get(position).getDevice_name());
        } else {
            viewHolder.adapterRuningFrom.setText("");
        }

        //配速
        if (mList.get(position).getPace() > 0) {
            int m = mList.get(position).getPace();
            viewHolder.adapterRuningSpeed.setText(m / 60 + "'" + m % 60 + "\"");
        } else {
            viewHolder.adapterRuningSpeed.setText("--");
        }
        /**
         * 是否有效 int	0：无效  1有效
         */
        if (mList.get(position).getIs_active() > 0) {
            //背景色
            viewHolder.adapterRuningRl.setBackgroundResource(R.color.white);
            viewHolder.adapterRuningInvalid.setVisibility(View.GONE);

            viewHolder.adapterRuningStartTime.setTextColor(mContext.getResources().getColor(R.color.fragment_record_app_text));
            viewHolder.adapterRuningType.setTextColor(mContext.getResources().getColor(R.color.fragment_record_app_text));
            viewHolder.adapterRuningDurationTime.setTextColor(mContext.getResources().getColor(R.color.fragment_record_app_text));
            viewHolder.adapterRuningKmNumber.setTextColor(mContext.getResources().getColor(R.color.fragment_record_app_text));
            viewHolder.adapterRuningSpeed.setTextColor(mContext.getResources().getColor(R.color.fragment_record_app_text));
            viewHolder.adapterRuningFrom.setTextColor(mContext.getResources().getColor(R.color.fragment_record_app_text));

            Drawable drawableLeft = mContext.getResources().getDrawable(
                    R.mipmap.ic_runing_record_from);

            viewHolder.adapterRuningFrom.setCompoundDrawablesWithIntrinsicBounds(drawableLeft,
                    null, null, null);
        } else {
            //背景色
            viewHolder.adapterRuningRl.setBackgroundResource(R.color.fragment_record_bg);
            viewHolder.adapterRuningInvalid.setVisibility(View.VISIBLE);

            viewHolder.adapterRuningStartTime.setTextColor(mContext.getResources().getColor(R.color.circle_number_text));
            viewHolder.adapterRuningType.setTextColor(mContext.getResources().getColor(R.color.circle_number_text));
            viewHolder.adapterRuningDurationTime.setTextColor(mContext.getResources().getColor(R.color.circle_number_text));
            viewHolder.adapterRuningKmNumber.setTextColor(mContext.getResources().getColor(R.color.circle_number_text));
            viewHolder.adapterRuningSpeed.setTextColor(mContext.getResources().getColor(R.color.circle_number_text));
            viewHolder.adapterRuningFrom.setTextColor(mContext.getResources().getColor(R.color.circle_number_text));

            Drawable drawableLeft = mContext.getResources().getDrawable(
                    R.mipmap.ic_runing_record_from_no);

            viewHolder.adapterRuningFrom.setCompoundDrawablesWithIntrinsicBounds(drawableLeft,
                    null, null, null);
        }
    }


    class ViewHolder {
        TextView adapterRuningInvalid;
        TextView adapterRuningStartTime;
        TextView adapterRuningType;
        TextView adapterRuningKmNumber;
        TextView adapterRuningDurationTime;
        TextView adapterRuningSpeed;
        TextView adapterRuningFrom;
        RelativeLayout adapterRuningRl;
    }
}

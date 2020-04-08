package com.rocedar.deviceplatform.app.behavior.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.rocedar.base.RCDateUtil;
import com.rocedar.base.RCImageShow;
import com.rocedar.deviceplatform.R;
import com.rocedar.deviceplatform.app.behavior.dto.BehaviorLibraryDietDTO;
import com.rocedar.deviceplatform.config.RCDeviceIndicatorID;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者：lxz
 * 日期：17/7/28 下午6:42
 * 版本：V1.0
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class DietRecordParticularsAdapter extends BaseAdapter {
    private Context mContext;
    private List<BehaviorLibraryDietDTO> mList = new ArrayList<>();

    public DietRecordParticularsAdapter(Context mContext, List<BehaviorLibraryDietDTO> mList) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_diet_record_particulars, null);
            viewHolder = new ViewHolder();
            viewHolder.adapterDietRecordParticularsTime= (TextView) convertView.findViewById(R.id.adapter_diet_record_particulars_time);
            viewHolder.adapterDietRecordParticularsName= (TextView) convertView.findViewById(R.id.adapter_diet_record_particulars_name);
            viewHolder.adapterDietRecordParticularsImage= (ImageView) convertView.findViewById(R.id.adapter_diet_record_particulars_image);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        initView(position, viewHolder);
        return convertView;
    }

    private void initView(int position, ViewHolder viewHolder) {
        viewHolder.adapterDietRecordParticularsTime.setText(RCDateUtil.formatServiceTime(mList.get(position).getUpdate_time() + "", "HH:mm"));

        switch (mList.get(position).getIndicator_id()) {
            //早餐
            case RCDeviceIndicatorID.BreakFast:
                viewHolder.adapterDietRecordParticularsName.setText(mContext.getString(R.string.rcdevice_record_breakFast));
                break;
            //午餐
            case RCDeviceIndicatorID.Lunch:
                viewHolder.adapterDietRecordParticularsName.setText(mContext.getString(R.string.rcdevice_record_lunch));
                break;
            //晚餐
            case RCDeviceIndicatorID.Dinner:
                viewHolder.adapterDietRecordParticularsName.setText(mContext.getString(R.string.rcdevice_record_dinner));
                break;
            //加餐
            case RCDeviceIndicatorID.Snacks:
                viewHolder.adapterDietRecordParticularsName.setText(mContext.getString(R.string.rcdevice_record_snacks));
                break;
        }
        RCImageShow.loadUrl(mList.get(position).getDiet_images().split(",")[0], viewHolder.adapterDietRecordParticularsImage, RCImageShow.IMAGE_TYPE_ALBUM);
    }

    static class ViewHolder {
        TextView adapterDietRecordParticularsTime;
        TextView adapterDietRecordParticularsName;
        ImageView adapterDietRecordParticularsImage;
    }
}

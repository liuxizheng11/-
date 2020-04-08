package com.rocedar.sdk.familydoctor.app.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rocedar.lib.base.unit.RCDrawableUtil;
import com.rocedar.lib.base.unit.RCImageShow;
import com.rocedar.lib.base.view.CircleImageView;
import com.rocedar.sdk.familydoctor.R;
import com.rocedar.sdk.familydoctor.app.RCMingYiDoctorDetailActivity;
import com.rocedar.sdk.familydoctor.dto.mingyi.RCMIngYiDoctorListDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：lxz
 * 日期：2018/7/18 下午3:37
 * 版本：V1.0
 * 描述： 名医列表 adapter
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCMingYiDoctorListAdapter extends BaseAdapter {
    private Context mContext;
    private List<RCMIngYiDoctorListDTO> mList = new ArrayList<>();

    public RCMingYiDoctorListAdapter(Context mContext, List<RCMIngYiDoctorListDTO> mList) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.rc_adapter_mingyi_list, null);
            viewHolder = new ViewHolder();
            viewHolder.rc_iv_item_doctor_consult_head = convertView.findViewById(R.id.rc_iv_item_doctor_consult_head);
            viewHolder.rc_tv_item_doctor_consult_name = convertView.findViewById(R.id.rc_tv_item_doctor_consult_name);
            viewHolder.rc_tv_item_doctor_consult_job = convertView.findViewById(R.id.rc_tv_item_doctor_consult_job);
            viewHolder.rc_tv_item_doctor_hospital_grade = convertView.findViewById(R.id.rc_tv_item_doctor_hospital_grade);
            viewHolder.rc_tv_item_doctor_consult_office = convertView.findViewById(R.id.rc_tv_item_doctor_consult_office);
            viewHolder.rc_tv_item_doctor_consult_desc = convertView.findViewById(R.id.rc_tv_item_doctor_consult_desc);
            viewHolder.rc_tv_item_doctor_consult_arrow = convertView.findViewById(R.id.rc_tv_item_doctor_consult_arrow);
            viewHolder.rc_tv_item_doctor_help = convertView.findViewById(R.id.rc_tv_item_doctor_help);
            viewHolder.rc_tv_item_doctor_money = convertView.findViewById(R.id.rc_tv_item_doctor_money);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        initView(viewHolder, position);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RCMingYiDoctorDetailActivity.goActivity(mContext, mList.get(position).getDoctor_id());
            }
        });
        return convertView;
    }

    private void initView(final ViewHolder viewHolder, final int position) {
        final RCMIngYiDoctorListDTO mDTO = mList.get(position);
        //医生头像
        RCImageShow.loadUrl(mDTO.getPortrait(), viewHolder.rc_iv_item_doctor_consult_head, RCImageShow.IMAGE_TYPE_HEAD);
        //医生名字
        viewHolder.rc_tv_item_doctor_consult_name.setText(mDTO.getDoctor_name());
        //医生科室
        viewHolder.rc_tv_item_doctor_consult_job.setText(mDTO.getTitle_name() + "  " + mDTO.getDepartment_name());
        //医院级别
        viewHolder.rc_tv_item_doctor_hospital_grade.setBackground(RCDrawableUtil.getDarkMainColorDrawable(mContext, 3));
        viewHolder.rc_tv_item_doctor_hospital_grade.setText(mDTO.getHospital_level());
        //医院名称
        viewHolder.rc_tv_item_doctor_consult_office.setText(mDTO.getHospital_name());
        //介绍
        viewHolder.rc_tv_item_doctor_consult_desc.setText("擅长 : " + mDTO.getSkilled());

        //根据文字的行数显示和隐藏展开按钮
        if (mList.get(position).isHasOpen()) {
            viewHolder.rc_tv_item_doctor_consult_arrow.setVisibility(View.VISIBLE);
        } else {
            viewHolder.rc_tv_item_doctor_consult_arrow.setVisibility(View.INVISIBLE);
        }
        if (!mList.get(position).isOpen()) {//收起
            viewHolder.rc_tv_item_doctor_consult_desc.setSingleLine(false);
            viewHolder.rc_tv_item_doctor_consult_desc.setEllipsize(null);
            viewHolder.rc_tv_item_doctor_consult_arrow.setText(mContext.getString(R.string.rc_packup));
            Drawable drawable = mContext.getResources().getDrawable(R.mipmap.rc_fd_ic_packup);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            viewHolder.rc_tv_item_doctor_consult_arrow.setCompoundDrawables(drawable, null, null, null);

        } else {//展开
            viewHolder.rc_tv_item_doctor_consult_desc.setLines(1);
            viewHolder.rc_tv_item_doctor_consult_desc.setEllipsize(TextUtils.TruncateAt.END);
            viewHolder.rc_tv_item_doctor_consult_arrow.setText(mContext.getString(R.string.rc_unfold));
            Drawable drawable = mContext.getResources().getDrawable(R.mipmap.rc_fd_ic_unfold);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            viewHolder.rc_tv_item_doctor_consult_arrow.setCompoundDrawables(drawable, null, null, null);
        }
        //已帮助人数
        viewHolder.rc_tv_item_doctor_help.setText(mDTO.getServer_time() + "");
        //多少元、分钟
        String temp;
        if ((int) mDTO.getFee() == mDTO.getFee()) {
            temp = (int) mDTO.getFee() + "";
        } else {
            temp = mDTO.getFee() + "";
        }
        viewHolder.rc_tv_item_doctor_money.setText("¥" + temp + "元/" + mDTO.getFee_time() + "分钟");

        //展开合上
        viewHolder.rc_tv_item_doctor_consult_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mList.get(position).isOpen()) {//展开
                    mList.get(position).setOpen(false);
                    viewHolder.rc_tv_item_doctor_consult_desc.setSingleLine(false);
                    viewHolder.rc_tv_item_doctor_consult_desc.setEllipsize(null);
                    viewHolder.rc_tv_item_doctor_consult_arrow.setText(mContext.getString(R.string.rc_packup));
                    Drawable drawable = mContext.getResources().getDrawable(R.mipmap.rc_fd_ic_packup);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    viewHolder.rc_tv_item_doctor_consult_arrow.setCompoundDrawables(drawable, null, null, null);
                } else {//收起
                    mList.get(position).setOpen(true);
                    viewHolder.rc_tv_item_doctor_consult_desc.setLines(1);
                    viewHolder.rc_tv_item_doctor_consult_desc.setEllipsize(TextUtils.TruncateAt.END);
                    viewHolder.rc_tv_item_doctor_consult_arrow.setText(mContext.getString(R.string.rc_unfold));
                    Drawable drawable = mContext.getResources().getDrawable(R.mipmap.rc_fd_ic_unfold);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    viewHolder.rc_tv_item_doctor_consult_arrow.setCompoundDrawables(drawable, null, null, null);

                }

            }
        });
    }

    class ViewHolder {
        CircleImageView rc_iv_item_doctor_consult_head;
        TextView rc_tv_item_doctor_consult_name;
        TextView rc_tv_item_doctor_consult_job;
        TextView rc_tv_item_doctor_hospital_grade;
        TextView rc_tv_item_doctor_consult_office;
        TextView rc_tv_item_doctor_consult_desc;
        TextView rc_tv_item_doctor_consult_arrow;
        TextView rc_tv_item_doctor_help;
        TextView rc_tv_item_doctor_money;
    }


}

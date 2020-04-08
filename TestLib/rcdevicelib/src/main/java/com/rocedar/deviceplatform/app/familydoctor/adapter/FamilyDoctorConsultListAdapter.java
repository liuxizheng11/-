package com.rocedar.deviceplatform.app.familydoctor.adapter;

import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.rocedar.base.RCImageShow;
import com.rocedar.deviceplatform.R;
import com.rocedar.deviceplatform.app.familydoctor.FamilyDoctorActivity;
import com.rocedar.deviceplatform.app.view.MyRatingBar;
import com.rocedar.deviceplatform.dto.familydoctor.FamilyDoctorListDTO;
import com.rocedar.deviceplatform.dto.familydoctor.RCFDDoctorListDTO;

import java.util.ArrayList;


/**
 * @author liuyi
 * @date 2017/4/18
 * @desc 医生咨询列表（家庭医生）
 * @veison V3.4.00新增
 */

public class FamilyDoctorConsultListAdapter extends BaseAdapter {
    private FamilyDoctorActivity activity;
    private ArrayList<FamilyDoctorListDTO> mDatas;
    private LayoutInflater inflater;
    private int focus;

    /**
     * 入口是医生咨询列表
     */
    public static final int CONSULT_DOCTOR_LIST = 1;
    /**
     * 入口是我的医生列表
     */
    public static final int MY_DOCTOR_LIST = 2;


    private int unfold;
    private int packup;

    public FamilyDoctorConsultListAdapter(FamilyDoctorActivity activity, ArrayList<FamilyDoctorListDTO> mDatas,
                                          int focus) {
        this.activity = activity;
        this.mDatas = mDatas;
        this.inflater = inflater.from(activity);
        this.focus = focus;
        unfold = activity.getiPlatformUtil().getChooseOfficeSelected();
        packup = activity.getiPlatformUtil().getChooseOfficeRetract();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_list_family_doctor_consult, parent, false);
            holder = new ViewHolder();
            holder.tv_item_doctor_consult_add = (TextView) convertView.findViewById(R.id.tv_item_doctor_consult_add);
            holder.tv_item_doctor_consult_arrow = (TextView) convertView.findViewById(R.id.tv_item_doctor_consult_arrow);
            holder.tv_item_doctor_consult_desc = (TextView) convertView.findViewById(R.id.tv_item_doctor_consult_desc);
            holder.tv_item_doctor_consult_name = (TextView) convertView.findViewById(R.id.tv_item_doctor_consult_name);
            holder.tv_item_doctor_consult_count = (TextView) convertView.findViewById(R.id.tv_item_doctor_consult_count);
            holder.tv_item_doctor_consult_office = (TextView) convertView.findViewById(R.id.tv_item_doctor_consult_office);
            holder.tv_item_doctor_consult_job = (TextView) convertView.findViewById(R.id.tv_item_doctor_consult_job);
            holder.tv_item_doctor_consult_video = (TextView) convertView.findViewById(R.id.tv_item_doctor_consult_video);
            holder.iv_item_doctor_consult_status = (ImageView) convertView.findViewById(R.id.iv_item_doctor_consult_status);
            holder.iv_item_doctor_consult_head = (ImageView) convertView.findViewById(R.id.iv_item_doctor_consult_head);
            holder.ratingbar_item_doctor_consult = (MyRatingBar) convertView.findViewById(R.id.ratingbar_item_doctor_consult);
            holder.tv_item_doctor_consult_star_number = (TextView) convertView.findViewById(R.id.tv_item_doctor_consult_star_number);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final FamilyDoctorListDTO dto = mDatas.get(position);
        final RCFDDoctorListDTO doctorListDTO = dto.getRcfdDoctorListDTO();

        holder.tv_item_doctor_consult_name.setText(doctorListDTO.getDoctor_name());
//        holder.tv_item_doctor_consult_job.setText(doctorListDTO.getTitle_name());
        holder.tv_item_doctor_consult_office.setText(doctorListDTO.getDepartment_name() + "  " + doctorListDTO.getTitle_name());
        holder.tv_item_doctor_consult_desc.setText(doctorListDTO.getSkilled());
        holder.tv_item_doctor_consult_count.setText(doctorListDTO.getServer_time());
        // TODO: 2017/11/7  评分
        holder.tv_item_doctor_consult_star_number.setText(doctorListDTO.getGrade());
        if (!TextUtils.isEmpty(doctorListDTO.getGrade())) {
            holder.ratingbar_item_doctor_consult.setStar(Float.parseFloat(doctorListDTO.getGrade()));
        }

        //根据蚊子的行数显示和隐藏展开按钮
        if (dto.isHasOpen()) {
            holder.tv_item_doctor_consult_arrow.setVisibility(View.VISIBLE);
        } else {
            holder.tv_item_doctor_consult_arrow.setVisibility(View.INVISIBLE);
        }
        if (!dto.isOpen()) {//收起
            holder.tv_item_doctor_consult_desc.setSingleLine(false);
            holder.tv_item_doctor_consult_desc.setEllipsize(null);
            holder.tv_item_doctor_consult_arrow.setText(activity.getString(R.string.rcdevice_packup));
            Drawable drawable = activity.getResources().getDrawable(packup);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            holder.tv_item_doctor_consult_arrow.setCompoundDrawables(drawable, null, null, null);

        } else {//展开
            holder.tv_item_doctor_consult_desc.setLines(1);
            holder.tv_item_doctor_consult_desc.setEllipsize(TextUtils.TruncateAt.END);
            holder.tv_item_doctor_consult_arrow.setText(activity.getString(R.string.rcdevice_unfold));
            Drawable drawable = activity.getResources().getDrawable(unfold);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            holder.tv_item_doctor_consult_arrow.setCompoundDrawables(drawable, null, null, null);
        }
        RCImageShow.loadUrl(doctorListDTO.getPortrait(), holder.iv_item_doctor_consult_head, RCImageShow.IMAGE_TYPE_HEAD);

        if (doctorListDTO.getStatus() == RCFDDoctorListDTO.DOCTOR_STATUS_BUSY || doctorListDTO.getStatus() == RCFDDoctorListDTO.DOCTOR_STATUS_ONLINE) {//在线
            //在线
            holder.iv_item_doctor_consult_status.setBackgroundResource(R.mipmap.ic_family_doctor_idle);

        } else if (doctorListDTO.getStatus() == RCFDDoctorListDTO.DOCTOR_STATUS_OFFLINE) {//离线
            holder.iv_item_doctor_consult_status.setBackgroundResource(R.mipmap.ic_family_doctor_line);
        }

        if (focus == CONSULT_DOCTOR_LIST) {//如果入口是我的医生，则不显示以下文本
            if (doctorListDTO.isFocus()) {
                holder.tv_item_doctor_consult_add.setText(activity.getString(R.string.rcdevice_family_doctor_my_doctor));
                holder.tv_item_doctor_consult_add.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 10);
                holder.tv_item_doctor_consult_add.setTextColor(activity.getResources().getColor(R.color.rcbase_app_main));
                holder.tv_item_doctor_consult_add.setBackgroundColor(activity.getResources().getColor(R.color.white));
                holder.tv_item_doctor_consult_add.setEnabled(false);
            } else {
                holder.tv_item_doctor_consult_add.setBackgroundResource(R.drawable.btn_doctor_stroke_999);
                holder.tv_item_doctor_consult_add.setText("+ " + activity.getString(R.string.rcdevice_family_doctor_my_doctor));
                holder.tv_item_doctor_consult_add.setTextColor(activity.getResources().getColor(R.color.main_home_menu_normal));
                holder.tv_item_doctor_consult_add.setEnabled(true);

            }
        }

        //添加为我的医生
        holder.tv_item_doctor_consult_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.familyDoctorOfficeScreenFragment.addMyDoctor(doctorListDTO.getDoctor_id(), position);
            }
        });

        //展开合上
        holder.tv_item_doctor_consult_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (dto.isOpen()) {//展开
                    dto.setOpen(false);
                    holder.tv_item_doctor_consult_desc.setSingleLine(false);
                    holder.tv_item_doctor_consult_desc.setEllipsize(null);
                    holder.tv_item_doctor_consult_arrow.setText(activity.getString(R.string.rcdevice_packup));
                    Drawable drawable = activity.getResources().getDrawable(packup);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    holder.tv_item_doctor_consult_arrow.setCompoundDrawables(drawable, null, null, null);
                } else {//收起
                    dto.setOpen(true);
                    holder.tv_item_doctor_consult_desc.setLines(1);
                    holder.tv_item_doctor_consult_desc.setEllipsize(TextUtils.TruncateAt.END);
                    holder.tv_item_doctor_consult_arrow.setText(activity.getString(R.string.rcdevice_unfold));
                    Drawable drawable = activity.getResources().getDrawable(unfold);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    holder.tv_item_doctor_consult_arrow.setCompoundDrawables(drawable, null, null, null);

                }

            }
        });

        //视频咨询
        holder.tv_item_doctor_consult_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.doStartAdvisory(doctorListDTO.getDoctor_id(),doctorListDTO.getPortrait(),
                doctorListDTO.getDoctor_name(),doctorListDTO.getTitle_name(),doctorListDTO.getDepartment_name());
            }
        });

        holder.tv_item_doctor_consult_video.setBackgroundResource(activity.getiPlatformUtil().getDoctorListBtnBackground());

        return convertView;
    }

    static class ViewHolder {
        TextView tv_item_doctor_consult_add;
        TextView tv_item_doctor_consult_arrow;
        TextView tv_item_doctor_consult_desc;
        TextView tv_item_doctor_consult_video;
        TextView tv_item_doctor_consult_job;
        TextView tv_item_doctor_consult_count;
        TextView tv_item_doctor_consult_name;
        TextView tv_item_doctor_consult_office;
        ImageView iv_item_doctor_consult_status;
        ImageView iv_item_doctor_consult_head;
        MyRatingBar ratingbar_item_doctor_consult;
        TextView tv_item_doctor_consult_star_number;
    }
}

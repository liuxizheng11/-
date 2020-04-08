package com.rocedar.sdk.familydoctor.app.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.rocedar.lib.base.network.IRCPostListener;
import com.rocedar.lib.base.unit.RCAndroid;
import com.rocedar.lib.base.unit.RCDrawableUtil;
import com.rocedar.lib.base.unit.RCHandler;
import com.rocedar.lib.base.unit.RCImageShow;
import com.rocedar.lib.base.unit.RCToast;
import com.rocedar.sdk.familydoctor.R;
import com.rocedar.sdk.familydoctor.app.eventbean.EventFDAdvisoryBean;
import com.rocedar.sdk.familydoctor.app.eventbean.EventFDBaseBean;
import com.rocedar.sdk.familydoctor.app.fragment.RCFDSpecialistBaseFragment;
import com.rocedar.sdk.familydoctor.dto.RCFDDoctorListDTO;
import com.rocedar.sdk.familydoctor.dto.RCFDListDTO;
import com.rocedar.sdk.familydoctor.view.RCMyRatingBar;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;


/**
 * @author liuyi
 * @date 2017/4/18
 * @desc 医生咨询列表（家庭医生）
 * @veison V3.4.00新增
 */

public class FDConsultListAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater inflater;
    private RCHandler rcHandler;

    private RCFDSpecialistBaseFragment mFragment;

    private ArrayList<RCFDListDTO> mDatas;
    private int focus;

    /**
     * 入口是医生咨询列表
     */
    public static final int CONSULT_DOCTOR_LIST = 1;
    /**
     * 入口是我的医生列表
     */
    public static final int MY_DOCTOR_LIST = 2;


    public FDConsultListAdapter(RCFDSpecialistBaseFragment fragment,
                                ArrayList<RCFDListDTO> mDatas, int focus) {
        this.mDatas = mDatas;
        this.mFragment = fragment;
        this.mContext = fragment.getContext();
        this.inflater = inflater.from(mContext);
        this.focus = focus;
        rcHandler = new RCHandler(mContext);
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
            convertView = inflater.inflate(R.layout.rc_fd_item_specialist_consult_list, parent, false);
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
            holder.ratingbar_item_doctor_consult = (RCMyRatingBar) convertView.findViewById(R.id.ratingbar_item_doctor_consult);
            holder.tv_item_doctor_consult_star_number = (TextView) convertView.findViewById(R.id.tv_item_doctor_consult_star_number);
            holder.tv_item_doctor_consult_video.setBackground(
                    RCDrawableUtil.getMainColorDrawableBaseRadius(mContext)
            );
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final RCFDListDTO dto = mDatas.get(position);
        final RCFDDoctorListDTO doctorListDTO = dto.getRcfdDoctorListDTO();

        holder.tv_item_doctor_consult_name.setText(doctorListDTO.getDoctor_name());
//        holder.tv_item_doctor_consult_job.setText(doctorListDTO.getTitle_name());
        holder.tv_item_doctor_consult_office.setText(doctorListDTO.getDepartment_name() + "  " + doctorListDTO.getTitle_name());
        holder.tv_item_doctor_consult_desc.setText(doctorListDTO.getSkilled());
        holder.tv_item_doctor_consult_count.setText(doctorListDTO.getServer_time());
        holder.tv_item_doctor_consult_star_number.setText(doctorListDTO.getGrade());
        if (!TextUtils.isEmpty(doctorListDTO.getGrade())) {
            holder.ratingbar_item_doctor_consult.setStar(Float.parseFloat(doctorListDTO.getGrade()));
        }

        //根据文字的行数显示和隐藏展开按钮
        if (dto.isHasOpen()) {
            holder.tv_item_doctor_consult_arrow.setVisibility(View.VISIBLE);
        } else {
            holder.tv_item_doctor_consult_arrow.setVisibility(View.INVISIBLE);
        }
        if (!dto.isOpen()) {//收起
            holder.tv_item_doctor_consult_desc.setSingleLine(false);
            holder.tv_item_doctor_consult_desc.setEllipsize(null);
            holder.tv_item_doctor_consult_arrow.setText(mContext.getString(R.string.rc_packup));
            Drawable drawable = mContext.getResources().getDrawable(R.mipmap.rc_fd_ic_packup);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            holder.tv_item_doctor_consult_arrow.setCompoundDrawables(drawable, null, null, null);

        } else {//展开
            holder.tv_item_doctor_consult_desc.setLines(1);
            holder.tv_item_doctor_consult_desc.setEllipsize(TextUtils.TruncateAt.END);
            holder.tv_item_doctor_consult_arrow.setText(mContext.getString(R.string.rc_unfold));
            Drawable drawable = mContext.getResources().getDrawable(R.mipmap.rc_fd_ic_unfold);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            holder.tv_item_doctor_consult_arrow.setCompoundDrawables(drawable, null, null, null);
        }
        RCImageShow.loadUrl(doctorListDTO.getPortrait(), holder.iv_item_doctor_consult_head, RCImageShow.IMAGE_TYPE_HEAD);

        if (doctorListDTO.getStatus() == RCFDDoctorListDTO.DOCTOR_STATUS_BUSY || doctorListDTO.getStatus() == RCFDDoctorListDTO.DOCTOR_STATUS_ONLINE) {//在线
            //在线
            holder.iv_item_doctor_consult_status.setBackgroundResource(R.mipmap.rc_fd_ic_family_doctor_idle);
        } else if (doctorListDTO.getStatus() == RCFDDoctorListDTO.DOCTOR_STATUS_OFFLINE) {//离线
            holder.iv_item_doctor_consult_status.setBackgroundResource(R.mipmap.rc_fd_ic_family_doctor_line);
        }

        if (focus == CONSULT_DOCTOR_LIST) {//如果入口是我的医生，则不显示以下文本
            if (doctorListDTO.isFocus()) {
                holder.tv_item_doctor_consult_add.setText(mContext.getString(R.string.rc_fd_my_doctor));
                holder.tv_item_doctor_consult_add.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 10);
                holder.tv_item_doctor_consult_add.setTextColor(
                        RCAndroid.getAttColor(mContext, R.attr.RCDarkColor));
                holder.tv_item_doctor_consult_add.setBackgroundColor(mContext.getResources().getColor(R.color.rc_white));
                holder.tv_item_doctor_consult_add.setEnabled(false);
            } else {
                holder.tv_item_doctor_consult_add.setBackgroundResource(R.drawable.rc_fd_btn_doctor_stroke_999);
                holder.tv_item_doctor_consult_add.setText("+ " + mContext.getString(R.string.rc_fd_my_doctor));
                holder.tv_item_doctor_consult_add.setTextColor(Color.parseColor("#999999"));
                holder.tv_item_doctor_consult_add.setEnabled(true);

            }
        }

        //添加为我的医生
        holder.tv_item_doctor_consult_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rcHandler.sendMessage(RCHandler.START);
                mFragment.doctorRequest.addMyDoctor(doctorListDTO.getDoctor_id(), new IRCPostListener() {

                    @Override
                    public void getDataSuccess() {
                        mDatas.get(position).getRcfdDoctorListDTO().setFocus(true);
                        notifyDataSetChanged();
                        RCToast.Center(mContext, "添加成功，可以去我的医生中查看哦～");
                        EventBus.getDefault().post(new EventFDBaseBean(EventFDBaseBean.FD_EVENT_TYPE_DO_REFRESH_MY_DOCTOR));
                        rcHandler.sendMessage(RCHandler.GETDATA_OK);
                    }

                    @Override
                    public void getDataError(int status, String msg) {
                        rcHandler.sendMessage(RCHandler.GETDATA_OK);
                    }
                });
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
                    holder.tv_item_doctor_consult_arrow.setText(mContext.getString(R.string.rc_packup));
                    Drawable drawable = mContext.getResources().getDrawable(R.mipmap.rc_fd_ic_packup);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    holder.tv_item_doctor_consult_arrow.setCompoundDrawables(drawable, null, null, null);
                } else {//收起
                    dto.setOpen(true);
                    holder.tv_item_doctor_consult_desc.setLines(1);
                    holder.tv_item_doctor_consult_desc.setEllipsize(TextUtils.TruncateAt.END);
                    holder.tv_item_doctor_consult_arrow.setText(mContext.getString(R.string.rc_unfold));
                    Drawable drawable = mContext.getResources().getDrawable(R.mipmap.rc_fd_ic_unfold);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    holder.tv_item_doctor_consult_arrow.setCompoundDrawables(drawable, null, null, null);

                }

            }
        });

        //视频咨询
        holder.tv_item_doctor_consult_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventFDAdvisoryBean advisoryBean = new EventFDAdvisoryBean(EventFDBaseBean.FD_EVENT_TYPE_DO_START_ADVISORY);
                advisoryBean.setDepartment(doctorListDTO.getDepartment_name());
                advisoryBean.setDoctor_id(doctorListDTO.getDoctor_id());
                advisoryBean.setDoctor_name(doctorListDTO.getDoctor_name());
                advisoryBean.setPortrait(doctorListDTO.getPortrait());
                advisoryBean.setTitle_name(doctorListDTO.getTitle_name());
                EventBus.getDefault().post(advisoryBean);
            }
        });
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
        RCMyRatingBar ratingbar_item_doctor_consult;
        TextView tv_item_doctor_consult_star_number;
    }

}

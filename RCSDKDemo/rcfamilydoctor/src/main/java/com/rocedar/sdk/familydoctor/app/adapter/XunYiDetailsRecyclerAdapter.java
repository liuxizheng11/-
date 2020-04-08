package com.rocedar.sdk.familydoctor.app.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rocedar.lib.base.RCPhotoPreviewActivity;
import com.rocedar.lib.base.unit.RCAndroid;
import com.rocedar.lib.base.unit.RCDateUtil;
import com.rocedar.lib.base.unit.RCImageShow;
import com.rocedar.lib.base.view.CircleImageView;
import com.rocedar.sdk.familydoctor.R;
import com.rocedar.sdk.familydoctor.app.util.SpaceItemDecoration;
import com.rocedar.sdk.familydoctor.dto.xunyi.RCXunYiConsultDetailsDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：lxz
 * 日期：2018/11/13 10:36 AM
 * 版本：V1.0
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class XunYiDetailsRecyclerAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private List<RCXunYiConsultDetailsDTO.questionsDTO> mList = new ArrayList<>();

    public XunYiDetailsRecyclerAdapter(Context mContext, List<RCXunYiConsultDetailsDTO.questionsDTO> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_DOCTOR:
                return new XunYiDetailsRecyclerAdapter.SystemHolder(getView(parent, R.layout.rc_xun_yi_item_consult_left));
            case TYPE_USER:
                return new XunYiDetailsRecyclerAdapter.UserHolder(getView(parent, R.layout.rc_xun_yi_item_consult_right));
            case TYPE_PHOTO:
                return new XunYiDetailsRecyclerAdapter.PhotoHolder(getView(parent, R.layout.rc_xun_yi_item_consult_photo));
            case TYPE_FINISH:
                return new XunYiDetailsRecyclerAdapter.FinishHolder(getView(parent, R.layout.rc_xun_yi_item_consult_final));

        }
        return null;
    }

    /**
     * 医生
     */
    public static final int TYPE_DOCTOR = 0xff21;
    /**
     * 用户消息
     */
    public static final int TYPE_USER = 0xff22;
    /**
     * 用户消息 照片
     */
    public static final int TYPE_PHOTO = 0xff23;
    /**
     * 用户已结束
     */
    public static final int TYPE_FINISH = 0xff24;

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof XunYiDetailsRecyclerAdapter.SystemHolder) {
            doTypeDoctor((XunYiDetailsRecyclerAdapter.SystemHolder) holder, mList.get(position), position);
        } else if (holder instanceof XunYiDetailsRecyclerAdapter.UserHolder) {
            doTypeUser((XunYiDetailsRecyclerAdapter.UserHolder) holder, mList.get(position), position);
        } else if (holder instanceof XunYiDetailsRecyclerAdapter.PhotoHolder) {
            doTypePhoto((XunYiDetailsRecyclerAdapter.PhotoHolder) holder, mList.get(position), position);
        }
    }

    @Override
    public int getItemViewType(int position) {

        int type = mList.get(position).getWho();
        String message = mList.get(position).getQuestion();
        String img = mList.get(position).getImg();

//        if (mList.get(position).getAdvice_status() != 0) {
        //        who	Int	0，医生 ；1，自己。
        if (type == 0) {
            return TYPE_DOCTOR;
        } else if (type == 1 && !message.equals("") && (img.equals("") || img.equals("null"))) {
            return TYPE_USER;
        } else if (type == 1 && (message.equals("") || message.equals("null")) && !img.equals("")) {
            return TYPE_PHOTO;
        }
//        } else {
//            return TYPE_FINISH;
//        }
        return 0;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    private View getView(ViewGroup parent, int layout) {
        return LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
    }

    /**
     * 医生消息 UI
     *
     * @param holder
     * @param mDTO
     */
    private void doTypeDoctor(XunYiDetailsRecyclerAdapter.SystemHolder holder, RCXunYiConsultDetailsDTO.questionsDTO mDTO, int position) {
        if (showTime(mDTO.getCreate_time())) {
            holder.ivItemConsultTimeLeft.setVisibility(View.VISIBLE);
            holder.ivItemConsultTimeLeft.setText(RCDateUtil.formatTime(mDTO.getCreate_time() + "", "HH:mm"));
        } else {
            holder.ivItemConsultTimeLeft.setVisibility(View.GONE);
        }
        RCImageShow.loadUrl(mDTO.getIcon(), holder.ivItemConsultHeadLeft, RCImageShow.IMAGE_TYPE_HEAD);
        holder.tvItemConsultContentLeft.setText(mDTO.getQuestion());
        if (mDTO.getAdvice_status() == 0 && position == mList.size() - 1) {
            holder.tvItemConsultContentFinal.setVisibility(View.VISIBLE);
        } else {
            holder.tvItemConsultContentFinal.setVisibility(View.GONE);
        }
    }

    /**
     * 用消息 UI
     *
     * @param holder
     * @param mDTO
     */
    private void doTypeUser(XunYiDetailsRecyclerAdapter.UserHolder holder, RCXunYiConsultDetailsDTO.questionsDTO mDTO, int position) {
        if (showTime(mDTO.getCreate_time())) {
            holder.tvItemConsultTimeRight.setVisibility(View.VISIBLE);
            holder.tvItemConsultTimeRight.setText(RCDateUtil.formatTime(mDTO.getCreate_time() + "", "HH:mm"));
        } else {
            holder.tvItemConsultTimeRight.setVisibility(View.GONE);
        }
        holder.tvItemConsultContentRight.setText(mDTO.getQuestion());
        if (mDTO.getAdvice_status() == 0 && position == mList.size() - 1) {
            holder.tvItemConsultContentRightFinal.setVisibility(View.VISIBLE);
        } else {
            holder.tvItemConsultContentRightFinal.setVisibility(View.GONE);
        }
    }

    /**
     * 用户 图片 UI
     *
     * @param holder
     * @param mDTO
     */
    private void doTypePhoto(XunYiDetailsRecyclerAdapter.PhotoHolder holder, final RCXunYiConsultDetailsDTO.questionsDTO mDTO, int position) {
        if (showTime(mDTO.getCreate_time())) {
            holder.tvItemConsultTimeRight.setVisibility(View.VISIBLE);
            holder.tvItemConsultTimeRight.setText(RCDateUtil.formatTime(mDTO.getCreate_time() + "", "HH:mm"));
        } else {
            holder.tvItemConsultTimeRight.setVisibility(View.GONE);
        }
        RCImageShow.loadUrl(mDTO.getImg(), holder.tvItemConsultPhoto, RCImageShow.IMAGE_TYPE_ALBUM);

        //查看大图
        holder.tvItemConsultPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RCPhotoPreviewActivity.goActivity(mContext, mDTO.getImg(), true, false);
            }
        });
        if (mDTO.getAdvice_status() == 0 && position == mList.size() - 1) {
            holder.tvItemConsultTimePhotoFinal.setVisibility(View.VISIBLE);
        } else {
            holder.tvItemConsultTimePhotoFinal.setVisibility(View.GONE);
        }
    }


    /**
     * 系统（医生）回复的消息的样式
     */
    public class SystemHolder extends RecyclerView.ViewHolder {
        TextView ivItemConsultTimeLeft;
        CircleImageView ivItemConsultHeadLeft;
        TextView tvItemConsultContentLeft;
        TextView tvItemConsultContentFinal;

        public SystemHolder(View itemView) {
            super(itemView);
            ivItemConsultTimeLeft = itemView.findViewById(R.id.iv_item_xun_yi_consult_time_left);
            ivItemConsultHeadLeft = itemView.findViewById(R.id.iv_item_xun_yi_consult_head_left);
            tvItemConsultContentLeft = itemView.findViewById(R.id.tv_item_xun_yi_consult_content_left);
            tvItemConsultContentFinal = itemView.findViewById(R.id.tv_item_xun_yi_consult_left_finish);
            tvItemConsultContentLeft.setBackgroundResource(R.drawable.img_xun_yi_dialog_lef);
        }
    }

    /**
     * 用户发送的消息的样式
     */
    public class UserHolder extends RecyclerView.ViewHolder {
        TextView tvItemConsultTimeRight;
        CircleImageView civItemConsultHeadRight;
        TextView tvItemConsultContentRight;
        TextView tvItemConsultContentRightFinal;

        public UserHolder(View itemView) {
            super(itemView);
            tvItemConsultTimeRight = itemView.findViewById(R.id.tv_item_xun_yi_consult_time_right);
            civItemConsultHeadRight = itemView.findViewById(R.id.civ_item_xun_yi_consult_head_right);
            tvItemConsultContentRight = itemView.findViewById(R.id.tv_item_hbp_consult_content_right);
            tvItemConsultContentRightFinal = itemView.findViewById(R.id.tv_item_xun_yi_consult_right_finish);
            tvItemConsultContentRight.setBackgroundResource(R.drawable.img_xun_yi_dialog_right);
        }
    }

    /**
     * 用户发送的图片的样式
     */
    public class PhotoHolder extends RecyclerView.ViewHolder {
        TextView tvItemConsultTimeRight;
        TextView tvItemConsultTimePhotoFinal;
        ImageView tvItemConsultPhoto;

        PhotoHolder(View view) {
            super(view);
            tvItemConsultTimeRight = view.findViewById(R.id.tv_item_xun_yi_consult_time_right_photo);
            tvItemConsultTimePhotoFinal = view.findViewById(R.id.tv_item_xun_yi_consult_photo_finish);
            tvItemConsultPhoto = view.findViewById(R.id.tv_item_xun_yi_consult_photo);
        }
    }

    /**
     * 用户 聊天结束的样式
     */
    public class FinishHolder extends RecyclerView.ViewHolder {
        TextView tv_item_xun_yi_consult_finish;

        FinishHolder(View view) {
            super(view);
            tv_item_xun_yi_consult_finish = view.findViewById(R.id.tv_item_xun_yi_consult_finish);
        }
    }

    /**
     * 是否显示时间
     *
     * @param time
     * @return
     */
    private boolean showTime(long time) {
        String now_time = RCDateUtil.getFormatNow("yyyyMMddHHmmss");
        if (Long.parseLong(RCDateUtil.formatTime(time + "", "yyyyMMddHHmm")) >=
                Long.parseLong(RCDateUtil.formatTime(now_time + "", "yyyyMMddHHmm"))) {
            return false;
        }

        return true;
    }
}

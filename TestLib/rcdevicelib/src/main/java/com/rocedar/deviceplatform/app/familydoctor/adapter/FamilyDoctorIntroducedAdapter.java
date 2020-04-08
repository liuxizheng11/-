package com.rocedar.deviceplatform.app.familydoctor.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rocedar.base.RCHandler;
import com.rocedar.base.RCImageShow;
import com.rocedar.deviceplatform.R;
import com.rocedar.deviceplatform.app.familydoctor.IFDIntroducedPlatformUtil;
import com.rocedar.deviceplatform.app.view.MyRatingBar;
import com.rocedar.deviceplatform.dto.familydoctor.RCFDDoctorCommentsDTO;
import com.rocedar.deviceplatform.dto.familydoctor.RCFDDoctorIntroduceDTO;
import com.rocedar.deviceplatform.dto.familydoctor.RCFDDoctorListDTO;
import com.rocedar.deviceplatform.request.impl.RCFamilyDoctorWWZImpl;
import com.rocedar.deviceplatform.request.listener.familydoctor.RCFDPostListener;

import java.text.DecimalFormat;
import java.util.List;

/**
 * 项目名称：DongYa3.0
 * <p>
 * 作者：phj
 * 日期：2017/11/7 下午6:10
 * 版本：V2.2.00
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public class FamilyDoctorIntroducedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private IFDIntroducedPlatformUtil iPlatformUtil;
    private List<RCFDDoctorCommentsDTO> familyDoctorCommentDTOs;
    private RCFDDoctorIntroduceDTO introduceDTO;
    private RCHandler mRcHandler;


    public FamilyDoctorIntroducedAdapter(
            Context context, IFDIntroducedPlatformUtil iPlatformUtil,
            List<RCFDDoctorCommentsDTO> familyDoctorCommentDTOs, RCFDDoctorIntroduceDTO introduceDTO) {
        this.context = context;
        this.iPlatformUtil = iPlatformUtil;
        this.familyDoctorCommentDTOs = familyDoctorCommentDTOs;
        this.introduceDTO = introduceDTO;
        mRcHandler = new RCHandler(context);
    }

    public void addCommectListData(List<RCFDDoctorCommentsDTO> commentsDTOs) {
        int tempLength = familyDoctorCommentDTOs.size();
        this.familyDoctorCommentDTOs.addAll(commentsDTOs);
        notifyItemRangeInserted(getItemCount() - (mHasMore ? 1 : 0), commentsDTOs.size());
        if (tempLength == 0) {
            notifyItemChanged(0);
        }
    }

    //type
    public static final int TYPE_DOCTOR_INFO = 0xff01;
    public static final int TYPE_COMMENT_LIST = 0xff11;
    public static final int TYPE_FOOTER = 0xff12;

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_DOCTOR_INFO;
        }
        if (mHasMore && position == getItemCount() - 1) {
            return TYPE_FOOTER;
        }
        return TYPE_COMMENT_LIST;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_DOCTOR_INFO:
                return new DoctorInfoHolder(getView(parent, R.layout.activity_family_doctor_introduced_doctor_info));
            case TYPE_COMMENT_LIST:
                return new CommentHolder(getView(parent, R.layout.activity_family_doctor_introduced_comment_adapter));
            case TYPE_FOOTER:
                return new HolderFooter(LayoutInflater.from(parent.getContext()).inflate(com.rocedar.base.R.layout.view_loadmore, parent, false));
        }
        return null;
    }

    private View getView(ViewGroup parent, int layout) {
        return LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof DoctorInfoHolder) {
            doTypeDoctorInfoHolder((DoctorInfoHolder) holder);
        } else if (holder instanceof CommentHolder) {
            doTypeCommentHolder((CommentHolder) holder, position - 1);
        }
    }

    @Override
    public int getItemCount() {
        return familyDoctorCommentDTOs.size() + 1 + (mHasMore ? 1 : 0);
    }

    private void doTypeDoctorInfoHolder(DoctorInfoHolder holder) {
        //加载医生头像（圆形）
        RCImageShow.loadUrlToCircle(introduceDTO.getPortrait(), holder.headImage, RCImageShow.IMAGE_TYPE_HEAD);
        //医生在线状态

        //医生名称
        holder.doctorName.setText(introduceDTO.getDoctor_name());
        //医生科室
        holder.doctorOffice.setText(introduceDTO.getHospital_name() + "\t" + introduceDTO.getDepartment_name());
        //简介
        holder.abstranctInfo.setText(introduceDTO.getProfile());
        //擅长
        holder.beGoodAtInfo.setText(introduceDTO.getSkilled());
        //咨询次数
        holder.headNumber.setText(context.getString(R.string.rcdevice_family_doctor_consult_count_number,
                introduceDTO.getServer_time()));
        //评价星级数量
        holder.startNumber.setText(introduceDTO.getGrade() + "");
        holder.startView.setStar(introduceDTO.getGrade());
        holder.myDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (introduceDTO.getFocus() != 1) {
                    addMyDoctor();
                }
            }
        });
        if (introduceDTO.getFocus() == 1) {
            holder.myDoctorIsAdd.setVisibility(View.VISIBLE);
            holder.myDoctor.setVisibility(View.GONE);
        } else {
            holder.myDoctor.setVisibility(View.VISIBLE);
            holder.myDoctorIsAdd.setVisibility(View.GONE);
        }
        if (introduceDTO.getStatus() == RCFDDoctorListDTO.DOCTOR_STATUS_BUSY ||
                introduceDTO.getStatus() == RCFDDoctorListDTO.DOCTOR_STATUS_ONLINE) {//在线
            //在线
            holder.doctorStatus.setBackgroundResource(R.mipmap.ic_family_doctor_idle);
        } else if (introduceDTO.getStatus() == RCFDDoctorListDTO.DOCTOR_STATUS_OFFLINE) {//离线
            holder.doctorStatus.setBackgroundResource(R.mipmap.ic_family_doctor_line);
        }
        if (familyDoctorCommentDTOs != null && familyDoctorCommentDTOs.size() > 0) {
            holder.noEvaluateLayout.setVisibility(View.GONE);
        } else {
            holder.noEvaluateLayout.setVisibility(View.VISIBLE);
        }
        holder.doctorLicense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (iPlatformUtil != null)
                    iPlatformUtil.openImage(context, introduceDTO.getCertification());
            }
        });
    }

    public class DoctorInfoHolder extends RecyclerView.ViewHolder {
        //医生头像
        private ImageView headImage;
        //医生在线状态
        private ImageView doctorStatus;
        //我的医生-未关注
        private LinearLayout myDoctor;
        //我的医生-已经关注
        private TextView myDoctorIsAdd;
        //医生名称
        private TextView doctorName;
        //医生科室
        private TextView doctorOffice;
        //简介
        private TextView abstranctInfo;
        //擅长
        private TextView beGoodAtInfo;
        //资质证书
        private TextView doctorLicense;
        //咨询次数
        private TextView headNumber;
        //评价星级数量
        private TextView startNumber;
        //评价星级
        private MyRatingBar startView;
        //无评价布局
        private LinearLayout noEvaluateLayout;
        //
        private ImageView noEvaluateImage;
        //顶部背景
        private RelativeLayout evaluateBgImg;
        public DoctorInfoHolder(View itemView) {
            super(itemView);
            doctorStatus = (ImageView) itemView.findViewById(R.id.activity_f_d_i_doctor_info_head_status);
            headImage = (ImageView) itemView.findViewById(R.id.activity_f_d_i_doctor_info_head_image);
            doctorName = (TextView) itemView.findViewById(R.id.activity_f_d_i_doctor_info_head_name);
            myDoctor = (LinearLayout) itemView.findViewById(R.id.activity_f_d_i_doctor_info_head_my_doctor_layout);
            myDoctorIsAdd = (TextView) itemView.findViewById(R.id.activity_f_d_i_doctor_info_head_my_doctor_add);
            doctorOffice = (TextView) itemView.findViewById(R.id.activity_f_d_i_doctor_info_head_office);
            doctorLicense = (TextView) itemView.findViewById(R.id.activity_f_d_i_doctor_info_license);
            headNumber = (TextView) itemView.findViewById(R.id.activity_f_d_i_doctor_info_head_number);
            startNumber = (TextView) itemView.findViewById(R.id.activity_f_d_i_doctor_info_evaluate_start_number);
            startView = (MyRatingBar) itemView.findViewById(R.id.activity_f_d_i_doctor_info_evaluate_start);
            abstranctInfo = (TextView) itemView.findViewById(R.id.activity_f_d_i_doctor_info_abstranct_info);
            beGoodAtInfo = (TextView) itemView.findViewById(R.id.activity_f_d_i_doctor_info_be_good_at_info);
            noEvaluateLayout = (LinearLayout) itemView.findViewById(R.id.activity_f_d_i_doctor_info_evaluate_none);
            noEvaluateImage = (ImageView) itemView.findViewById(R.id.activity_f_d_i_doctor_info_evaluate_none_image);
            evaluateBgImg = (RelativeLayout) itemView.findViewById(R.id.activity_f_d_i_doctor_info_head_img);
            if (iPlatformUtil != null)
                noEvaluateImage.setImageResource(iPlatformUtil.noneCommentShow());
                evaluateBgImg.setBackgroundResource(iPlatformUtil.evaluateImg());
        }
    }


    private void doTypeCommentHolder(CommentHolder holder, int position) {
        //用户名
        holder.userName.setText(familyDoctorCommentDTOs.get(position).getUser_name());
        holder.commentInfo.setText(familyDoctorCommentDTOs.get(position).getComment());
//        holder.commentTime.setText(RCDateUtil.formatServiceTime(
//                familyDoctorCommentDTOs.get(position).getComment_time(),
//                "yyyy-MM-dd  HH:mm"));
        holder.commentTime.setText(familyDoctorCommentDTOs.get(position).getComment_time());
        holder.startNumber.setText(new DecimalFormat("#.0").format(familyDoctorCommentDTOs.get(position).getGrade()) + "");
        holder.startView.setStar(familyDoctorCommentDTOs.get(position).getGrade());
    }


    public class CommentHolder extends RecyclerView.ViewHolder {

        //星的数量
        private TextView startNumber;
        private TextView commentInfo;
        private TextView userName;
        private TextView commentTime;
        private MyRatingBar startView;

        public CommentHolder(View itemView) {
            super(itemView);
            userName = (TextView) itemView.findViewById(R.id.activity_f_d_i_comment_username);
            startNumber = (TextView) itemView.findViewById(R.id.activity_f_d_i_comment_start_number);
            commentInfo = (TextView) itemView.findViewById(R.id.activity_f_d_i_comment_info);
            commentTime = (TextView) itemView.findViewById(R.id.activity_f_d_i_comment_time);
            startView = (MyRatingBar) itemView.findViewById(R.id.activity_f_d_i_comment_start);
        }
    }


    /**
     * 添加为我的医生
     */
    public void addMyDoctor() {
        if (introduceDTO == null) return;
        mRcHandler.sendMessage(RCHandler.START);
        new RCFamilyDoctorWWZImpl(context).addMyDoctor(introduceDTO.getDoctor_id(), new RCFDPostListener() {
            @Override
            public void getDataSuccess() {
                mRcHandler.sendMessage(RCHandler.GETDATA_OK);
                introduceDTO.setFocus(1);
                notifyItemChanged(0);
            }

            @Override
            public void getDataError(int status, String msg) {
                mRcHandler.sendMessage(RCHandler.GETDATA_OK);
            }
        });
    }


    public class HolderFooter extends RecyclerView.ViewHolder {

        public LinearLayout loadmore;

        public HolderFooter(View itemView) {
            super(itemView);
            loadmore = (LinearLayout) itemView.findViewById(R.id.view_load_more_main);
            loadmore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (loadmoreLintener != null)
                        loadmoreLintener.load();
                }
            });
        }

    }

    private boolean mHasMore = true;

    public void setMoLoadMore(boolean hasMore) {
        if (mHasMore) {
            mHasMore = hasMore;
            if (!hasMore) {
                notifyItemRemoved(getItemCount());
            }
        } else {
            mHasMore = hasMore;
            if (hasMore) {
                notifyItemInserted(getItemCount());
            }
        }
    }

    public void setLoadmoreLintener(LoadmoreLintener loadmoreLintener) {
        this.loadmoreLintener = loadmoreLintener;
    }

    private LoadmoreLintener loadmoreLintener;


    public interface LoadmoreLintener {

        void load();

    }
}

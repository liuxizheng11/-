package com.rocedar.sdk.familydoctor.app.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rocedar.lib.base.unit.RCHandler;
import com.rocedar.sdk.familydoctor.R;
import com.rocedar.sdk.familydoctor.app.RCFDSpecialistActivity;
import com.rocedar.sdk.familydoctor.app.RCMingYiDoctorListActivity;
import com.rocedar.sdk.familydoctor.app.RCXunYiUserPerfectActivity;
import com.rocedar.sdk.familydoctor.app.fragment.RCFDSpecialistBaseFragment;
import com.rocedar.sdk.familydoctor.app.util.RCFamilyDoctorWwzUtil;
import com.rocedar.sdk.familydoctor.dto.RCFDRecordDetailDTO;
import com.rocedar.sdk.familydoctor.dto.RCFDServiceStatusInfoDTO;
import com.rocedar.sdk.familydoctor.dto.RCFDSpecificCommentsDTO;
import com.rocedar.sdk.familydoctor.request.IRCFDRecordRequest;
import com.rocedar.sdk.familydoctor.request.listener.fd.RCFDGetRecordDetailListener;
import com.rocedar.sdk.familydoctor.request.listener.fd.RCFDGetServerStatusListener;
import com.rocedar.sdk.familydoctor.view.RCMyRatingBar;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称：瑰柏SDK-商城
 * <p>
 * 作者：phj
 * 日期：2018/11/1 4:58 PM
 * 版本：V1.1.00
 * 描述：瑰柏SDK-服务商品
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class FDMainV3Adapter extends RecyclerView.Adapter {


    private RCFDSpecialistBaseFragment context;
    private RCFamilyDoctorWwzUtil doctorWwzUtil;
    private IRCFDRecordRequest recordRequest;
    private RCHandler mRcHandler;

    public FDMainV3Adapter(RCFDSpecialistBaseFragment context, List<RCFDSpecificCommentsDTO> mData) {
        this.context = context;
        this.doctorWwzUtil = context.doctorWwzUtil;
        this.recordRequest = context.recordRequest;
        this.mRcHandler = context.mRcHandler;
        this.mData = mData;
        getServerPermission();
    }

    /**
     * 头
     */
    public static final int TYPE_HEAD = 0xff11;
    /**
     * 评论
     */
    public static final int TYPE_COMMENT = 0xff12;


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_HEAD:
                return new HeadInfoHolder(getView(parent, R.layout.rc_fd_fragment_main_v3_top));
            case TYPE_COMMENT:
                return new CommentListHolder(getView(parent, R.layout.rc_fd_item_list_evluate));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CommentListHolder) {
            doTypeCommentList((CommentListHolder) holder, position - 1);
        } else if (holder instanceof HeadInfoHolder) {
            doTypeHead((HeadInfoHolder) holder);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEAD;
        }
        return TYPE_COMMENT;
    }


    @Override
    public int getItemCount() {
        return mData.size() + 1;
    }

    private void doTypeHead(HeadInfoHolder holder) {
        if (serviceValid) {
//            holder.serviceStatus.setVisibility(View.VISIBLE);
            holder.serviceStatus.setImageResource(R.mipmap.rc_fd_ic_kaitong);
        } else {
            holder.serviceStatus.setImageResource(R.mipmap.rc_fd_ic_hujiao);
//            holder.serviceStatus.setVisibility(View.INVISIBLE);
        }
    }


    private List<RCFDSpecificCommentsDTO> mData = new ArrayList<>();

    public void doTypeCommentList(CommentListHolder holder, final int position) {
        if (position < 0 || position > mData.size() - 1) return;
        final RCFDSpecificCommentsDTO dto = mData.get(position);
        holder.tvItemFdEvaluateName.setText(dto.getUser_phone());
        holder.tvItemDoctorEvaluateStarNumber.setText(dto.getGrade());
        if (!TextUtils.isEmpty(dto.getGrade()))
            holder.ratingbarItemDoctorEvaluate.setStar(Float.parseFloat(dto.getGrade()));

        holder.tvItemFdEvaluateDesc.setText(dto.getComment());
        holder.tvItemFdEvaluateDoctorName.setText(dto.getHospital_name() + "\t\t" + dto.getDoctor_name());
    }


    public class HeadInfoHolder extends RecyclerView.ViewHolder {

        RelativeLayout buttonOneKeyCall;
        RelativeLayout buttonOnline;
        RelativeLayout buttonMingyi;
        RelativeLayout buttonSpecialist;
        ImageView serviceStatus;


        public HeadInfoHolder(View itemView) {
            super(itemView);
            buttonOneKeyCall = itemView.findViewById(R.id.rc_fd_fragment_main_v3_top_call);
            buttonOnline = itemView.findViewById(R.id.rc_fd_fragment_main_v3_top_online);
            buttonMingyi = itemView.findViewById(R.id.rc_fd_fragment_main_v3_top_mingyi);
            buttonSpecialist = itemView.findViewById(R.id.rc_fd_fragment_main_v3_top_specialist);
            serviceStatus = itemView.findViewById(R.id.rc_fd_fragment_main_v3_top_service);
            View.OnClickListener headClick = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (v.getId() == R.id.rc_fd_fragment_main_v3_top_call) {
                        doCall();
                    } else if (v.getId() == R.id.rc_fd_fragment_main_v3_top_mingyi) {
                        RCMingYiDoctorListActivity.goActivity(context.getActivity());
                    } else if (v.getId() == R.id.rc_fd_fragment_main_v3_top_specialist) {
                        RCFDSpecialistActivity.goActivity(context.getActivity(),
                                context.mPhoneNumber, context.mDeviceNumber);
                    } else if (v.getId() == R.id.rc_fd_fragment_main_v3_top_online) {
                        RCXunYiUserPerfectActivity.goActivity(context.getActivity());
                    }
                }
            };
            buttonOneKeyCall.setOnClickListener(headClick);
            buttonMingyi.setOnClickListener(headClick);
            buttonSpecialist.setOnClickListener(headClick);
            buttonOnline.setOnClickListener(headClick);
        }
    }


    public class CommentListHolder extends RecyclerView.ViewHolder {

        TextView tvItemFdEvaluateName;
        RCMyRatingBar ratingbarItemDoctorEvaluate;
        TextView tvItemDoctorEvaluateStarNumber;
        TextView tvItemFdEvaluateDesc;
        TextView tvItemFdEvaluateDoctorName;

        public CommentListHolder(View itemView) {
            super(itemView);
            tvItemFdEvaluateName = itemView.findViewById(R.id.tv_item_fd_evaluate_name);
            ratingbarItemDoctorEvaluate = itemView.findViewById(R.id.ratingbar_item_doctor_evaluate);
            tvItemDoctorEvaluateStarNumber = itemView.findViewById(R.id.tv_item_doctor_evaluate_star_number);
            tvItemFdEvaluateDesc = itemView.findViewById(R.id.tv_item_fd_evaluate_desc);
            tvItemFdEvaluateDoctorName = itemView.findViewById(R.id.tv_item_fd_evaluate_doctor_name);
        }
    }


    private View getView(ViewGroup parent, int layout) {
        return LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
    }

    public void doCall() {
        if (!serviceValid) {
            doctorWwzUtil.showBuyDialog();
            return;
        }
        getVideoPermission();
    }

    private boolean serviceValid = false;

    /**
     * 获取服务权限
     */
    private void getServerPermission() {
        doctorWwzUtil.checkStatus(new RCFDGetServerStatusListener() {
            @Override
            public void getDataSuccess(RCFDServiceStatusInfoDTO dto) {
                serviceValid = dto.isValid();
                notifyItemChanged(0);
            }

            @Override
            public void getDataError(int status, String msg) {

            }
        });
    }

    /**
     * 获取视频通话参数
     */
    private void getVideoPermission() {
        mRcHandler.sendMessage(RCHandler.START);
        recordRequest.getFDSpecificDoctor(new RCFDGetRecordDetailListener() {
            @Override
            public void getDataSuccess(RCFDRecordDetailDTO dto) {
                mRcHandler.sendMessage(RCHandler.GETDATA_OK);
                doctorWwzUtil.startAdvisory(dto.getDoctor_id(), dto.getPortrait(),
                        dto.getDoctor_name(), dto.getTitle_name(), dto.getDepartment_name());
            }

            @Override
            public void getDataError(int status, String msg) {
                mRcHandler.sendMessage(RCHandler.GETDATA_OK);
            }
        });
    }

}

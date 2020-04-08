package com.rocedar.sdk.familydoctor.app.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rocedar.lib.base.unit.RCDrawableUtil;
import com.rocedar.lib.base.unit.RCHandler;
import com.rocedar.lib.base.view.loading.EndLessOnScrollListener;
import com.rocedar.sdk.familydoctor.R;
import com.rocedar.sdk.familydoctor.app.adapter.FDIntroducedAdapter;
import com.rocedar.sdk.familydoctor.dto.RCFDDoctorCommentsDTO;
import com.rocedar.sdk.familydoctor.dto.RCFDDoctorIntroduceDTO;
import com.rocedar.sdk.familydoctor.request.impl.RCFDDoctorRequestImpl;
import com.rocedar.sdk.familydoctor.request.listener.fd.RCFDGetDoctorCommentsListener;
import com.rocedar.sdk.familydoctor.request.listener.fd.RCFDGetDoctorIntroduceListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称：瑰柏SDK-基础库
 * <p>
 * 作者：phj
 * 日期：2018/5/30 下午3:43
 * 版本：V1.0.00
 * 描述：瑰柏SDK-
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCFDSpecialistIntroducedFragment extends RCFDSpecialistBaseFragment {


    public static RCFDSpecialistIntroducedFragment newInstance(String doctorId, String phone, String deviceNumbers) {
        Bundle args = new Bundle();
        args.putString("doctor_id", doctorId);
        args.putString(KEY_PHONENO, phone);
        args.putString(KEY_DEVICENO, deviceNumbers);
        RCFDSpecialistIntroducedFragment fragment = new RCFDSpecialistIntroducedFragment();
        fragment.setArguments(args);
        return fragment;
    }


    //视频咨询按钮
    private TextView button;
    private RecyclerView recyclerView;

    private FDIntroducedAdapter fdIntroducedAdapter;

    private List<RCFDDoctorCommentsDTO> familyDoctorCommentDTOs = new ArrayList<>();

    //医生ID
    private String doctorId = null;
    //接口获取的用户信息
    private RCFDDoctorIntroduceDTO introduceDTO = null;

    boolean isOnPullUp = false;//上拉加载更多


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.rc_fd_activity_specialist_introduced, null);
        doctorId = getArguments().getString("doctor_id", null);
        if (doctorId == null || doctorId.equals("")) {
            mActivity.finish();
        }

        doctorRequest = new RCFDDoctorRequestImpl(mActivity);
        button = (TextView) view.findViewById(R.id.activity_f_d_i_button);
        recyclerView = (RecyclerView) view.findViewById(R.id.activity_f_d_i_recyclerview);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //视频咨询
                doctorWwzUtil.startAdvisory(doctorId, introduceDTO.getPortrait(),
                        introduceDTO.getDoctor_name(), introduceDTO.getTitle_name(),
                        introduceDTO.getDepartment_name());
            }
        });
        button.setBackground(RCDrawableUtil.getMainColorDrawableBaseRadius(mActivity));


        //请求医生信息
        mRcHandler.sendMessage(RCHandler.START);
        doctorRequest.getDoctorIntroduce(doctorId, new RCFDGetDoctorIntroduceListener() {
            @Override
            public void getDataSuccess(RCFDDoctorIntroduceDTO dto) {
                introduceDTO = dto;
                recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(
                        fdIntroducedAdapter = new FDIntroducedAdapter(
                                mActivity, familyDoctorCommentDTOs, dto
                        ));
                fdIntroducedAdapter.setLoadmoreLintener(new FDIntroducedAdapter.LoadmoreLintener() {
                    @Override
                    public void load() {
                        if (isOnPullUp) {
                            return;
                        }
                        getCommentData();
                    }
                });
                recyclerView.addOnScrollListener(new EndLessOnScrollListener() {
                    @Override
                    public void onLoadMore(int currentPage) {
                        if (isOnPullUp) {
                            return;
                        }
                        getCommentData();
                    }
                });
                mRcHandler.sendMessage(RCHandler.GETDATA_OK);
            }

            @Override
            public void getDataError(int status, String msg) {
                mRcHandler.sendMessage(RCHandler.GETDATA_OK);
            }
        });

        getCommentData();

        return view;

    }


    private int page = 0;

    /**
     * 获取评论信息
     */
    private void getCommentData() {
        if (isOnPullUp) return;
        if (page > 0 && familyDoctorCommentDTOs.size() == 0) {
            if (fdIntroducedAdapter != null) {
                fdIntroducedAdapter.setMoLoadMore(false);
            }
            return;
        }
        isOnPullUp = true;
        doctorRequest.getDoctorCommentList(doctorId, page, new RCFDGetDoctorCommentsListener() {
            @Override
            public void getDataSuccess(List<RCFDDoctorCommentsDTO> dto) {
                if (fdIntroducedAdapter != null) {
                    fdIntroducedAdapter.addCommectListData(dto);
                    if (dto.size() != 20)
                        fdIntroducedAdapter.setMoLoadMore(false);
                } else {
                    familyDoctorCommentDTOs.addAll(dto);
                }
                isOnPullUp = false;
                page++;
            }

            @Override
            public void getDataError(int status, String msg) {

            }
        });
    }


}

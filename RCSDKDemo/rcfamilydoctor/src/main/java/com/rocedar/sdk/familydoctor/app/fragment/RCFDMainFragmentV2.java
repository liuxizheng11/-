package com.rocedar.sdk.familydoctor.app.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.rocedar.lib.base.unit.RCDateUtil;
import com.rocedar.lib.base.unit.RCDrawableUtil;
import com.rocedar.lib.base.unit.RCHandler;
import com.rocedar.lib.base.userinfo.RCSPUserInfo;
import com.rocedar.sdk.familydoctor.R;
import com.rocedar.sdk.familydoctor.app.RCFDSpecialistActivity;
import com.rocedar.sdk.familydoctor.app.RCMingYiDoctorListActivity;
import com.rocedar.sdk.familydoctor.app.adapter.RCMingYiDoctorListAdapter;
import com.rocedar.sdk.familydoctor.dto.RCFDRecordDetailDTO;
import com.rocedar.sdk.familydoctor.dto.RCFDServiceStatusInfoDTO;
import com.rocedar.sdk.familydoctor.dto.mingyi.RCMIngYiDoctorListDTO;
import com.rocedar.sdk.familydoctor.request.IRCMingYiRequest;
import com.rocedar.sdk.familydoctor.request.impl.RCMingYiRequestImpl;
import com.rocedar.sdk.familydoctor.request.listener.fd.RCFDGetRecordDetailListener;
import com.rocedar.sdk.familydoctor.request.listener.fd.RCFDGetServerStatusListener;
import com.rocedar.sdk.familydoctor.request.listener.mingyi.RCMingYiDoctorListListener;
import com.rocedar.sdk.familydoctor.util.RCFDDrawableUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称：瑰柏SDK-家庭医生
 * <p>
 * 作者：phj
 * 日期：2018/7/18 上午9:51
 * 版本：V1.1.00
 * 描述：瑰柏SDK-家庭医生主页V2.0
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCFDMainFragmentV2 extends RCFDSpecialistBaseFragment {


    public static RCFDMainFragmentV2 newInstance(String phoneNumber, String userPortrait, String deviceNumbers) {
        Bundle args = new Bundle();
        args.putString(KEY_PHONENO, phoneNumber);
        args.putString(KEY_DEVICENO, deviceNumbers);
        args.putString("user_portrait", userPortrait);
        RCFDMainFragmentV2 fragment = new RCFDMainFragmentV2();
        fragment.setArguments(args);
        return fragment;
    }

    private void insetData() {
        if (getArguments().getString("user_portrait") != null
                &&!getArguments().getString("user_portrait").equals(""))
            RCSPUserInfo.savePortrait(getArguments().getString("user_portrait"));
    }


    private TextView openService;
    private ImageView vipLog;
    private TextView serviceTime;
    private TextView consult;
    private TextView specialist;
    private TextView more;
    private ListView mingYiList;

    private List<RCMIngYiDoctorListDTO> mingYiDoctorList = new ArrayList<>();
    private RCMingYiDoctorListAdapter doctorListAdapter;


    private void initView(View view) {
        openService = view.findViewById(R.id.rc_fd_fragment_main_top_open);
        vipLog = view.findViewById(R.id.rc_fd_fragment_main_top_vip_icon);
        serviceTime = view.findViewById(R.id.rc_fd_fragment_main_top_service_time);
        consult = view.findViewById(R.id.rc_fd_fragment_main_consult);
        specialist = view.findViewById(R.id.rc_fd_fragment_main_specialist);
        more = view.findViewById(R.id.rc_fd_fragment_main_more);
        mingYiList = view.findViewById(R.id.rc_fd_fragment_main_list_view);
        doctorListAdapter = new RCMingYiDoctorListAdapter(mActivity, mingYiDoctorList);
        mingYiList.setAdapter(doctorListAdapter);

        consult.setBackground(RCDrawableUtil.getMainColorDrawableBaseRadius(mActivity));
        specialist.setBackground(RCFDDrawableUtil.btn_stroke_main(mActivity));
        consult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!serviceValid) {
                    doctorWwzUtil.showBuyDialog();
                    return;
                }
                getVideoPermission();
            }
        });
        specialist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RCFDSpecialistActivity.goActivity(mActivity, mPhoneNumber,
                        mDeviceNumber);
            }
        });
        openService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doctorWwzUtil.goShop();
            }
        });
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RCMingYiDoctorListActivity.goActivity(mActivity);
            }
        });
    }

    private IRCMingYiRequest rcMingYiRequest;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        rcMingYiRequest = new RCMingYiRequestImpl(mActivity);
        View view = inflater.inflate(R.layout.rc_fd_fragment_main_v2, null);
        initView(view);
        insetData();
        getServerPermission();
        getMingYiDoctorList();
        return view;
    }


    private boolean serviceValid = false;

    /**
     * 获取服务权限
     */
    private void getServerPermission() {
        mRcHandler.sendMessage(RCHandler.START);
        doctorWwzUtil.checkStatus(new RCFDGetServerStatusListener() {
            @Override
            public void getDataSuccess(RCFDServiceStatusInfoDTO dto) {
                serviceValid = dto.isValid();
                if (serviceValid) {
                    vipLog.setVisibility(View.VISIBLE);
                    openService.setVisibility(View.GONE);
                    String startTime = RCDateUtil.formatTime(dto.getStartTime() + "", "yyyy年MM月dd日");
                    String endTime = RCDateUtil.formatTime(dto.getEndTime() + "", "yyyy年MM月dd日");
                    if (isAdded()) {
                        String format = getResources().getString(R.string.rc_fd_useful_life);
                        String content = String.format(format, startTime, endTime);
                        serviceTime.setText(content);
                    }
                } else {
                    //buyServerUrl = result.optString("url");
                    openService.setVisibility(View.VISIBLE);
                    vipLog.setVisibility(View.GONE);
                }
                mRcHandler.sendMessage(RCHandler.GETDATA_OK);
            }

            @Override
            public void getDataError(int status, String msg) {
                mRcHandler.sendMessage(RCHandler.GETDATA_OK);
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
                doctorWwzUtil.startAdvisory(dto.getDoctor_id(), dto.getPortrait(),
                        dto.getDoctor_name(), dto.getTitle_name(), dto.getDepartment_name());
                mRcHandler.sendMessage(RCHandler.GETDATA_OK);

            }

            @Override
            public void getDataError(int status, String msg) {
                mRcHandler.sendMessage(RCHandler.GETDATA_OK);

            }
        });
    }


    private void getMingYiDoctorList() {
        rcMingYiRequest.getRecommendDoctor(new RCMingYiDoctorListListener() {
            @Override
            public void getDataSuccess(List<RCMIngYiDoctorListDTO> doctorListDTOS) {
                mingYiDoctorList.clear();
                mingYiDoctorList.addAll(doctorListDTOS);
                doctorListAdapter.notifyDataSetChanged();
            }

            @Override
            public void getDataError(int status, String msg) {

            }
        });

    }

}

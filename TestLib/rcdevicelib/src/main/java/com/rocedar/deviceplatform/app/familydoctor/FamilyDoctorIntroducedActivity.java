package com.rocedar.deviceplatform.app.familydoctor;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.rocedar.base.RCHandler;
import com.rocedar.base.manger.RCBaseActivity;
import com.rocedar.base.unit.EndLessOnScrollListener;
import com.rocedar.deviceplatform.R;
import com.rocedar.deviceplatform.app.familydoctor.adapter.FamilyDoctorIntroducedAdapter;
import com.rocedar.deviceplatform.device.other.RCFamilyDoctorWwzUtil;
import com.rocedar.deviceplatform.dto.familydoctor.RCFDDoctorCommentsDTO;
import com.rocedar.deviceplatform.dto.familydoctor.RCFDDoctorIntroduceDTO;
import com.rocedar.deviceplatform.request.RCFamilyDoctorEvaulateRequest;
import com.rocedar.deviceplatform.request.impl.RCFamilyDoctorEvaulateImpl;
import com.rocedar.deviceplatform.request.listener.familydoctor.RCFDGetDoctorCommnetsListener;
import com.rocedar.deviceplatform.request.listener.familydoctor.RCFDGetDoctorIntroduceListener;
import com.rocedar.deviceplatform.unit.ReadPlatformConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称：
 * <p>
 * 作者：phj
 * 日期：2017/11/7 下午3:29
 * 版本：
 * 描述：医生介绍页面
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public class FamilyDoctorIntroducedActivity extends RCBaseActivity {


    /**
     * @param context
     * @param doctorId 医生ID
     */
    public static void goActivity(Context context, String doctorId, String phone) {
        Intent intent = new Intent(context, FamilyDoctorIntroducedActivity.class);
        intent.putExtra("doctor_id", doctorId);
        intent.putExtra("phone", phone);
        context.startActivity(intent);
    }

    //视频咨询按钮
    private TextView button;
    private RecyclerView recyclerView;

    private RCFamilyDoctorEvaulateRequest request;

    private FamilyDoctorIntroducedAdapter familyDoctorIntroducedAdapter;

    private IFDIntroducedPlatformUtil iPlatformUtil;

    private List<RCFDDoctorCommentsDTO> familyDoctorCommentDTOs = new ArrayList<>();

    private RCFamilyDoctorWwzUtil wwzUtil;


    //医生ID
    private String doctorId = null;
    //用户手机号，视频咨询时有用
    private String phone = null;
    //接口获取的用户信息
    private RCFDDoctorIntroduceDTO introduceDTO = null;

    boolean isOnPullUp = false;//上拉加载更多

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent().hasExtra("doctor_id")) {
            doctorId = getIntent().getStringExtra("doctor_id");
        }
        if (doctorId == null) {
            finishActivity();
        }
        phone = getIntent().getStringExtra("phone");
        //获取适配不同应用的接口实现类对象
        try {
            iPlatformUtil = (IFDIntroducedPlatformUtil) ReadPlatformConfig.getFamilyDoctorIntroducedClass().newInstance();
        } catch (Exception e) {
            //没有实现类时，实现默认实现类
            iPlatformUtil = new FDIntroducedPlatformUtilBaseImpl();
        }

        wwzUtil = new RCFamilyDoctorWwzUtil(mContext);

        setContentView(R.layout.activity_family_doctor_introduced);
        mRcHeadUtil.setTitle(getString(R.string.rcdevice_family_doctor_introduced));

        request = new RCFamilyDoctorEvaulateImpl(mContext);

        button = (TextView) findViewById(R.id.activity_f_d_i_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //视频咨询
                iPlatformUtil.checkAccredit(mContext, new IFamilyDoctorPlatformUtil.CheckAccreditListener() {
                    @Override
                    public void error() {

                    }

                    @Override
                    public void succeed() {
                        if (introduceDTO == null) return;
                        wwzUtil.startAdvisory(doctorId, RCFamilyDoctorWwzUtil.WWZ_SERVICE_ID,
                                phone, introduceDTO.getPortrait(), introduceDTO.getDoctor_name(),
                                introduceDTO.getTitle_name(), introduceDTO.getDepartment_name());
                    }
                });
            }
        });
        recyclerView = (RecyclerView) findViewById(R.id.activity_f_d_i_recyclerview);


        //请求医生信息
        mRcHandler.sendMessage(RCHandler.START);
        request.getDoctorIntroduced(doctorId, new RCFDGetDoctorIntroduceListener() {
            @Override
            public void getDataSuccess(RCFDDoctorIntroduceDTO dto) {
                introduceDTO = dto;
                recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(
                        familyDoctorIntroducedAdapter = new FamilyDoctorIntroducedAdapter(
                                mContext, iPlatformUtil, familyDoctorCommentDTOs, dto
                        ));
                familyDoctorIntroducedAdapter.setLoadmoreLintener(new FamilyDoctorIntroducedAdapter.LoadmoreLintener() {
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

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        wwzUtil.onDestory();
    }


    private int page = 0;

    /**
     * 获取评论信息
     */
    private void getCommentData() {
        if (isOnPullUp) return;
        isOnPullUp = true;
        request.getDoctorCommentList(doctorId, page, new RCFDGetDoctorCommnetsListener() {
            @Override
            public void getDataSuccess(List<RCFDDoctorCommentsDTO> dto) {
                if (familyDoctorIntroducedAdapter != null) {
                    familyDoctorIntroducedAdapter.addCommectListData(dto);
                    if (dto.size() != 20)
                        familyDoctorIntroducedAdapter.setMoLoadMore(false);
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

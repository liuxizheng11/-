package com.rocedar.sdk.assessment.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rocedar.lib.base.manage.RCBaseFragment;
import com.rocedar.lib.base.unit.RCDrawableUtil;
import com.rocedar.lib.base.unit.RCHandler;
import com.rocedar.sdk.assessment.R;
import com.rocedar.sdk.assessment.RCAssessmentActivity;
import com.rocedar.sdk.assessment.dto.RCAssessmentResultDTO;
import com.rocedar.sdk.assessment.request.IRCAssessmentRequest;
import com.rocedar.sdk.assessment.request.impl.RCAssessmentRequestImpl;
import com.rocedar.sdk.assessment.request.listener.RCAssessmentResultListener;

import java.lang.reflect.Method;

/**
 * 项目名称：瑰柏SDK-基础库
 * <p>
 * 作者：phj
 * 日期：2018/6/28 下午3:06
 * 版本：V1.0.00
 * 描述：瑰柏SDK-
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCAssessmentResultFragment extends RCBaseFragment {


    public static RCAssessmentResultFragment newInstance(int questionnaireID, String phoneNumber,
                                                         String deviceNo) {
        Bundle args = new Bundle();
        args.putInt("questionnaire_id", questionnaireID);
        args.putString("phone_number", phoneNumber);
        args.putString("device_no", deviceNo);
        RCAssessmentResultFragment fragment = new RCAssessmentResultFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private TextView activityEvaluationResultTitle;
    private TextView activityEvaluationResultContent;
    private TextView activityWeightEnteringCommit;
    private RelativeLayout activityEvaluationResultGetDoctor;

    private void initView(View view) {
        activityEvaluationResultTitle = view.findViewById(R.id.activity_evaluation_result_title);
        activityEvaluationResultContent = view.findViewById(R.id.activity_evaluation_result_content);
        activityWeightEnteringCommit = view.findViewById(R.id.activity_weight_entering_commit);
        activityWeightEnteringCommit.setBackground(RCDrawableUtil.getMainColorDrawableBaseRadius(mActivity));
        activityEvaluationResultGetDoctor = view.findViewById(R.id.activity_evaluation_result_get_doctor);
    }


    private IRCAssessmentRequest rcEvaluation;


    /**
     * 题目 ID
     */
    private int questionnaireID = -1;
    private String questionnaireName = "";

    private String phoneNumber = null;
    private String deviceNo;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.rc_fragment_assessment_result, null);
        questionnaireID = getArguments().getInt("questionnaire_id");
        phoneNumber = getArguments().getString("phone_number");
        deviceNo = getArguments().getString("device_no");
        initView(view);
        getClassInfo();
        rcEvaluation = new RCAssessmentRequestImpl(mActivity);
        initData();
        return view;
    }

    private void initData() {
        mRcHandler.sendMessage(RCHandler.START);
        rcEvaluation.getEvaluationResultData(questionnaireID, new RCAssessmentResultListener() {
            @Override
            public void getDataSuccess(RCAssessmentResultDTO dto) {
                questionnaireName = dto.getQuestionnaireName();
                mRcHandler.sendMessage(RCHandler.GETDATA_OK);
                //Title
//                mRcHeadUtil.setTitle(mjo.optString("questionnaire_name"));
                //标题
                if (dto.getTitle().equals("")) {
                    activityEvaluationResultTitle.setVisibility(View.GONE);
                } else {
                    activityEvaluationResultTitle.setVisibility(View.VISIBLE);
                    activityEvaluationResultTitle.setText(dto.getTitle());
                }
                //内容
                activityEvaluationResultContent.setText(dto.getBulletin());
            }

            @Override
            public void getDataError(int status, String msg) {
                mRcHandler.sendMessage(RCHandler.GETDATA_OK);
            }
        });

        //重新测量
        activityWeightEnteringCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RCAssessmentActivity.goActivity(mActivity, questionnaireID,
                        questionnaireName, phoneNumber, deviceNo);
                mActivity.finish();
            }
        });
        //家庭医生服务
        activityEvaluationResultGetDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAdvisory();
            }
        });
    }


    private Class<?> fdUtilClass = null;

    private void getClassInfo() {
        try {
            fdUtilClass = Class.forName("com.rocedar.sdk.familydoctor.RCFDUtil");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (fdUtilClass != null) {
            activityEvaluationResultGetDoctor.setVisibility(View.VISIBLE);
        }


    }


    private void startAdvisory() {
        if (fdUtilClass == null) return;
        try {
            Method m1 = fdUtilClass.getMethod("startAdvisory", Context.class, String.class, String.class);
            Object userInfo = fdUtilClass.newInstance();
            m1.invoke(userInfo, mActivity, phoneNumber, deviceNo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

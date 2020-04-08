package com.rocedar.sdk.assessment.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.rocedar.lib.base.manage.RCBaseFragment;
import com.rocedar.lib.base.network.IRCPostListener;
import com.rocedar.lib.base.unit.RCHandler;
import com.rocedar.sdk.assessment.R;
import com.rocedar.sdk.assessment.RCAssessmentResultActivity;
import com.rocedar.sdk.assessment.dialog.AssessmentDialog;
import com.rocedar.sdk.assessment.dto.RCAssessmentInfoDTO;
import com.rocedar.sdk.assessment.dto.RCAssessmentTopicsDTO;
import com.rocedar.sdk.assessment.request.IRCAssessmentRequest;
import com.rocedar.sdk.assessment.request.impl.RCAssessmentRequestImpl;
import com.rocedar.sdk.assessment.request.listener.RCAssessmentInfoListener;
import com.rocedar.sdk.assessment.util.SPInfo;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 项目名称：瑰柏SDK-基础库
 * <p>
 * 作者：phj
 * 日期：2018/6/26 上午11:54
 * 版本：V1.0.00
 * 描述：瑰柏SDK-
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCAssessmentMainFragment extends RCBaseFragment implements AssessmentQuestionMainFragment.OnEvaluationMainFragmentListener {


    public static RCAssessmentMainFragment newInstance(int questionnaireId) {
        Bundle args = new Bundle();
        args.putInt("questionnaire_id", questionnaireId);
        RCAssessmentMainFragment fragment = new RCAssessmentMainFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static RCAssessmentMainFragment newInstance(int questionnaireID, String phoneNumber,
                                                       String deviceNo) {
        Bundle args = new Bundle();
        args.putInt("questionnaire_id", questionnaireID);
        args.putString("phone_number", phoneNumber);
        args.putString("device_no", deviceNo);
        RCAssessmentMainFragment fragment = new RCAssessmentMainFragment();
        fragment.setArguments(args);
        return fragment;
    }


    FrameLayout activityHealthEvaluationFl;
    ImageView activityHealthEvaluationNext;

    private RCAssessmentInfoDTO mAllDTO;
    private String nextQuestionID = "";
    /**
     * 题目 ID
     */
    private int questionnaireID = -1;
    IRCAssessmentRequest rcEvaluation;
    /**
     * 答题号  初始值为 0
     */
    private int number = 0;
    private FragmentManager mFragmentManager = null;


    private void initView(View view) {
        activityHealthEvaluationFl = view.findViewById(R.id.activity_health_evaluation_fl);
        activityHealthEvaluationNext = view.findViewById(R.id.activity_health_evaluation_next);
        setNextBg(false);
        activityHealthEvaluationNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setNextBg(false);
                if (!nextQuestionID.equals("")) {
                    initFragment(nextQuestionID);
                } else {
                    //跳转 结果页面并提交
                    saveAnswerData();
                }
            }
        });
    }

    private void setNextBg(boolean click) {
        if (click) {
            activityHealthEvaluationNext.setEnabled(true);
            activityHealthEvaluationNext.setImageResource(R.mipmap.btn_health_evaluation_next);
        } else {
            activityHealthEvaluationNext.setEnabled(false);
            activityHealthEvaluationNext.setImageResource(R.mipmap.btn_health_unselected_next);
        }
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.rc_fragment_assessment_main, null);
        questionnaireID = getArguments().getInt("questionnaire_id");
        initView(view);
        rcEvaluation = new RCAssessmentRequestImpl(mActivity);
        return view;

    }

    private AssessmentDialog dialog;

    /**
     * 请求数据
     */
    private void initData() {
        mRcHandler.sendMessage(RCHandler.START);
        rcEvaluation.getEvaluationParticulars(questionnaireID, new RCAssessmentInfoListener() {
            @Override
            public void getDataSuccess(RCAssessmentInfoDTO dto) {
                mRcHandler.sendMessage(RCHandler.GETDATA_OK);
                mAllDTO = dto;
                initFragment(mAllDTO.getStart_topic_id() + "");
                if (!mAllDTO.getQuestionnaire_desc().equals("")
                        && !SPInfo.getHealthEvaluationWrite(questionnaireID + "")) {
                    if (dialog == null)
                        dialog = new AssessmentDialog(mActivity, questionnaireID + "",
                                mAllDTO.getQuestionnaire_name(), mAllDTO.getQuestionnaire_desc());
                    dialog.setCancelable(false);
                    if (!dialog.isShowing())
                        dialog.show();
                }
            }

            @Override
            public void getDataError(int status, String msg) {
                mRcHandler.sendMessage(RCHandler.GETDATA_OK);
            }
        });
    }


    /**
     * 初始化 Fragment
     *
     * @param topic_id 传入对应答题ID
     */
    private void initFragment(String topic_id) {
        //题目数字
        number++;
        RCAssessmentTopicsDTO mDTO = new RCAssessmentTopicsDTO();
        for (int i = 0; i < mAllDTO.getTopicsDTOList().size(); i++) {
            if (mAllDTO.getTopicsDTOList().get(i).getTopics_id().equals(topic_id)) {
                mDTO = mAllDTO.getTopicsDTOList().get(i);
            }
        }
        AssessmentQuestionMainFragment healthEvaluationMainFragment = AssessmentQuestionMainFragment.newInstance(number + "", mDTO);
        healthEvaluationMainFragment.setOnEvaluationMainFragmentListener(this);
        if (mAllDTO.getStart_topic_id() == Integer.parseInt(topic_id)) {
            showContent(R.id.activity_health_evaluation_fl, healthEvaluationMainFragment);
        } else {
            if (null == mFragmentManager) {
                mFragmentManager = mActivity.getSupportFragmentManager();
            }
            FragmentTransaction fragmentTransaction = mFragmentManager
                    .beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.rc_push_left_in,
                    R.anim.rc_push_left_out, R.anim.rc_push_left_in, R.anim.rc_push_left_out);
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
            fragmentTransaction.replace(R.id.activity_health_evaluation_fl, healthEvaluationMainFragment);
            fragmentTransaction.commit();
        }
    }


    /**
     * 答题答案 JSON
     */
    private JSONObject answerJson = new JSONObject();

    @Override
    public void onQuestionSelect(String questionID, String answerID, String answer) {
        nextQuestionID = questionID;
        try {
            if (!answer.equals("")) {
                setNextBg(true);
                answerJson.put(answerID, answer);
            } else {
                setNextBg(false);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 提交 答案
     */
    private void saveAnswerData() {
        mRcHandler.sendMessage(RCHandler.START);
        rcEvaluation.saveEvaluationQuestionSubmit(questionnaireID, answerJson.toString(), new IRCPostListener() {
            @Override
            public void getDataSuccess() {
                mRcHandler.sendMessage(RCHandler.GETDATA_OK);
                RCAssessmentResultActivity.goActivity(mActivity,
                        questionnaireID, mAllDTO.getQuestionnaire_name(),
                        getArguments().getString("phone_number"),
                        getArguments().getString("device_no"));
                mActivity.finish();
            }

            @Override
            public void getDataError(int status, String msg) {
                mRcHandler.sendMessage(RCHandler.GETDATA_OK);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

}

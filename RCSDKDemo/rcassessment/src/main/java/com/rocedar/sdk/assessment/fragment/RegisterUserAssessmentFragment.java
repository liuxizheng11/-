package com.rocedar.sdk.assessment.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.rocedar.lib.base.manage.RCBaseFragment;
import com.rocedar.lib.base.network.IRCPostListener;
import com.rocedar.lib.base.unit.RCHandler;
import com.rocedar.sdk.assessment.R;
import com.rocedar.sdk.assessment.request.IRCAssessmentRequest;
import com.rocedar.sdk.assessment.request.impl.RCAssessmentRequestImpl;
import com.rocedar.sdk.assessment.util.DrawableUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 项目名称：瑰柏SDK-基础库
 * <p>
 * 作者：phj
 * 日期：2018/7/12 下午4:24
 * 版本：V1.0.00
 * 描述：瑰柏SDK-
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RegisterUserAssessmentFragment extends RCBaseFragment implements View.OnClickListener {


    public static RegisterUserAssessmentFragment newInstance() {

        Bundle args = new Bundle();

        RegisterUserAssessmentFragment fragment = new RegisterUserAssessmentFragment();
        fragment.setArguments(args);
        return fragment;
    }


    RadioButton activityUserWorkFreelance;
    RadioButton activityUserWorkNineToFive;
    RadioButton activityUserHighBloodPressureDonthave;
    RadioButton activityUserHighBloodPressureHave;
    RadioButton activityUserDiabetesMellitusDonthave;
    RadioButton activityUserDiabetesMellitusHave;
    TextView activity_user_work_next;


    private void initView(View view) {
        activityUserWorkFreelance = view.findViewById(R.id.activity_user_work_freelance);
        activityUserWorkNineToFive = view.findViewById(R.id.activity_user_work_nine_to_five);
        activityUserHighBloodPressureDonthave = view.findViewById(R.id.activity_user_high_blood_pressure_donthave);
        activityUserHighBloodPressureHave = view.findViewById(R.id.activity_user_high_blood_pressure_have);
        activityUserDiabetesMellitusDonthave = view.findViewById(R.id.activity_user_diabetes_mellitus_donthave);
        activityUserDiabetesMellitusHave = view.findViewById(R.id.activity_user_diabetes_mellitus_have);
        activity_user_work_next = view.findViewById(R.id.activity_user_work_next);

        activityUserWorkFreelance.setOnClickListener(this);
        activityUserWorkNineToFive.setOnClickListener(this);
        activityUserHighBloodPressureDonthave.setOnClickListener(this);
        activityUserHighBloodPressureHave.setOnClickListener(this);
        activityUserDiabetesMellitusDonthave.setOnClickListener(this);
        activityUserDiabetesMellitusHave.setOnClickListener(this);
        activity_user_work_next.setOnClickListener(this);

        activityUserWorkFreelance.setBackground(DrawableUtil.activity_select(mActivity));
        activityUserWorkNineToFive.setBackground(DrawableUtil.activity_select(mActivity));
        activityUserHighBloodPressureDonthave.setBackground(DrawableUtil.activity_select(mActivity));
        activityUserHighBloodPressureHave.setBackground(DrawableUtil.activity_select(mActivity));
        activityUserDiabetesMellitusDonthave.setBackground(DrawableUtil.activity_select(mActivity));
        activityUserDiabetesMellitusHave.setBackground(DrawableUtil.activity_select(mActivity));
    }

    /**
     * 高血压ID
     */
    private String HighBloodPressure_ID = "10118";
    private int HighBloodPressure_Select = 1;
    /**
     * 工作ID
     */
    private String Working_ID = "10117";
    private int Working_Select = 1;
    /**
     * 糖尿病ID
     */
    private String Diabetes_ID = "10119";
    private int Diabetes_Select = 1;


    private IRCAssessmentRequest rcEvaluation;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.rc_fragment_assessment_register, null);
        initView(view);
        rcEvaluation = new RCAssessmentRequestImpl(mActivity);
        return view;
    }


    private JSONObject answerJson = new JSONObject();


    /**
     * 提交 答案
     */
    private void saveAnswerData() {
        mRcHandler.sendMessage(RCHandler.START);
        rcEvaluation.saveEvaluationQuestionSubmit(1015, answerJson.toString(), new IRCPostListener() {
            @Override
            public void getDataSuccess() {
                mRcHandler.sendMessage(RCHandler.GETDATA_OK);
                if (listener != null) {
                    listener.getDataSuccess();
                }
            }

            @Override
            public void getDataError(int status, String msg) {
                mRcHandler.sendMessage(RCHandler.GETDATA_OK);
                if (listener != null) {
                    listener.getDataError(status, msg);
                }
            }
        });
    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.activity_user_work_freelance) {
            //                "option_id": 1,
            Working_Select = 1;
        } else if (i == R.id.activity_user_work_nine_to_five) {
            // "option_id": 2,"option_name": "早九晚五"
            Working_Select = 2;

        } else if (i == R.id.activity_user_high_blood_pressure_donthave) {
            //                "option_id": 1,   "option_name": "没有"
            HighBloodPressure_Select = 1;

        } else if (i == R.id.activity_user_high_blood_pressure_have) {
            //                "option_id": 2, "option_name": "有"
            HighBloodPressure_Select = 2;

        } else if (i == R.id.activity_user_diabetes_mellitus_donthave) {
            //                "option_id": 1,"option_name": "没有"
            Diabetes_Select = 1;

        } else if (i == R.id.activity_user_diabetes_mellitus_have) {
            //                "option_id": 2, "option_name": "有"
            Diabetes_Select = 2;

        } else if (i == R.id.activity_user_work_next) {
            try {
                answerJson.put(HighBloodPressure_ID, HighBloodPressure_Select);
                answerJson.put(Working_ID, Working_Select);
                answerJson.put(Diabetes_ID, Diabetes_Select);
                saveAnswerData();

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }

    private IRCPostListener listener;

    public void setListener(IRCPostListener listener) {
        this.listener = listener;
    }
}

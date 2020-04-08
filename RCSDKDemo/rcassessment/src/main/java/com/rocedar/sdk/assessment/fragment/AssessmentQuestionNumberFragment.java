package com.rocedar.sdk.assessment.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rocedar.lib.base.manage.RCBaseFragment;
import com.rocedar.lib.base.unit.RCFontCustom;
import com.rocedar.lib.base.view.RCScaleChat;
import com.rocedar.sdk.assessment.R;
import com.rocedar.sdk.assessment.dto.RCAssessmentOptionsDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：lxz
 * 日期：2018/5/24 下午6:06
 * 版本：V1.0
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class AssessmentQuestionNumberFragment extends RCBaseFragment {

    private static final String ANSWER = "ANSWER";
    private static final String DEFAULT = "DEFAULT";

    private TextView fragmentHealthEvaluationNumberTv;
    private TextView fragmentHealthEvaluationNumberUnit;
    private RCScaleChat fragmentHealthEvaluationNumberChat;

    private void initView(View view) {
        fragmentHealthEvaluationNumberTv = view.findViewById(R.id.fragment_health_evaluation_number_tv);
        fragmentHealthEvaluationNumberUnit = view.findViewById(R.id.fragment_health_evaluation_number_unit);
        fragmentHealthEvaluationNumberChat = view.findViewById(R.id.fragment_health_evaluation_number_chat);
    }

    private OnEvaluationSetNumber onEvaluationSetNumber;
    private List<RCAssessmentOptionsDTO> mList = new ArrayList<>();
    //范围值
    private String range = "";

    public static AssessmentQuestionNumberFragment newInstance(String range, ArrayList<RCAssessmentOptionsDTO> optionsDTOList) {
        Bundle args = new Bundle();
        args.putSerializable(ANSWER, optionsDTOList);
        args.putString(DEFAULT, range);
        AssessmentQuestionNumberFragment fragment = new AssessmentQuestionNumberFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.rc_fragment_aq_number, container, false);
        initView(view);
        if (getArguments().containsKey(ANSWER)) {
            mList = (ArrayList<RCAssessmentOptionsDTO>) getArguments().getSerializable(ANSWER);
        }
        range = getArguments().getString(DEFAULT);
        initView();
        return view;
    }

    private void initView() {
        //      "default": "170,120,260,1,CM",
        String[] strs = range.split(",");
        // 默认值 0
        fragmentHealthEvaluationNumberTv.setText(strs[0]);
        fragmentHealthEvaluationNumberChat.setData(Integer.parseInt(strs[2]), Integer.parseInt(strs[1]),
                Integer.parseInt(strs[0]), 1);
        //设置单位 4
        fragmentHealthEvaluationNumberUnit.setText(strs[4]);
        fragmentHealthEvaluationNumberTv.setTypeface(RCFontCustom.getTf(mActivity));
        fragmentHealthEvaluationNumberChat.setScaleChatChooseListener(new RCScaleChat.ScaleChatChooseListener() {
            @Override
            public void chooseData(String data, float number) {
                fragmentHealthEvaluationNumberTv.setText((int) number + "");
                if (onEvaluationSetNumber != null) {
                    onEvaluationSetNumber.onNumber((int) number);
                }
            }
        });
    }


    //回调
    public interface OnEvaluationSetNumber {
        //回调多选答案
        void onNumber(int n);
    }

    public void setOnEvaluationSetNumber(OnEvaluationSetNumber onEvaluationSetNumber) {
        this.onEvaluationSetNumber = onEvaluationSetNumber;
    }

}

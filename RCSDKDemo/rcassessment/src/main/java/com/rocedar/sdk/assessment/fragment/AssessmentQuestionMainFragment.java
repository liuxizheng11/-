package com.rocedar.sdk.assessment.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.rocedar.lib.base.manage.RCBaseFragment;
import com.rocedar.lib.base.unit.RCDrawableUtil;
import com.rocedar.sdk.assessment.R;
import com.rocedar.sdk.assessment.dto.RCAssessmentOptionsDTO;
import com.rocedar.sdk.assessment.dto.RCAssessmentTopicsDTO;
import com.rocedar.sdk.assessment.util.DrawableUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：lxz
 * 日期：2018/5/28 下午5:03
 * 版本：V1.0
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class AssessmentQuestionMainFragment extends RCBaseFragment {

    TextView fragmentHealthEvaluationMainNu;
    TextView fragmentHealthEvaluationMainQuestion;
    FrameLayout fragmentHealthEvaluationMainContent;

    private void initView(View view) {
        fragmentHealthEvaluationMainNu = view.findViewById(R.id.fragment_health_evaluation_main_number);
        fragmentHealthEvaluationMainNu.setBackground(DrawableUtil.bg_assessment_plan(mActivity));
        fragmentHealthEvaluationMainQuestion = view.findViewById(R.id.fragment_health_evaluation_main_question);
        fragmentHealthEvaluationMainContent = view.findViewById(R.id.fragment_health_evaluation_main_content);
    }

    private OnEvaluationMainFragmentListener evaluationMainFragmentListener;
    //第几题
    private static final String QUESTION = "QUESTION";
    //答案
    private static final String TOPICSDTO = "TOPICSDTO";

    /**
     * 题目类型
     */
    //单选
    private static final int SingleSelection = 1;
    //多选
    private static final int MultiSelect = 2;
    //数字
    private static final int Number = 3;
    //文字
    private static final int Text = 4;


    //问题数据
    private RCAssessmentTopicsDTO topicsDTO;

    /**
     * @param num       题号（序号）
     * @param topicsDTO 问题数据
     * @return
     */
    public static AssessmentQuestionMainFragment newInstance(String num, RCAssessmentTopicsDTO topicsDTO) {
        Bundle args = new Bundle();
        args.putString(QUESTION, num);
        args.putSerializable(TOPICSDTO, topicsDTO);
        AssessmentQuestionMainFragment fragment = new AssessmentQuestionMainFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.rc_fragment_aq_main, container, false);
        initView(view);
        fragmentHealthEvaluationMainNu.setText("当前进度第 " + getArguments().getString(QUESTION) + " 题");
        if (getArguments().containsKey(TOPICSDTO)) {
            topicsDTO = (RCAssessmentTopicsDTO) getArguments().getSerializable(TOPICSDTO);
            initQuestion();
        }
        return view;
    }

    /**
     * 初始化 Fragment 根据对应Type 加载布局
     */
    private void initQuestion() {
        //问题
        fragmentHealthEvaluationMainQuestion.setText(topicsDTO.getTopic_name());
        ArrayList<RCAssessmentOptionsDTO> optionsDTOList = topicsDTO.getOptionsDTOList();
        switch (topicsDTO.getType_id()) {
            //单选
            case SingleSelection:
                AssessmentQuestionSingleFragment singleFragment =
                        AssessmentQuestionSingleFragment.newInstance(optionsDTOList);
                singleFragment.setOnEvaluationSSetSingle(new AssessmentQuestionSingleFragment.OnEvaluationSetSingle() {
                    @Override
                    public void onNumber(RCAssessmentOptionsDTO dto) {
                        String next_topic = dto.getNext_topic();
                        evaluationMainFragmentListener.onQuestionSelect(
                                next_topic, topicsDTO.getTopics_id(), dto.getOption_id() + "");
                    }
                });
                getChildFragmentManager().beginTransaction().replace(R.id.fragment_health_evaluation_main_content,
                        singleFragment).commitAllowingStateLoss();
                break;
            //多选
            case MultiSelect:
                AssessmentQuestionMultiFragment multiSelectFragment = AssessmentQuestionMultiFragment.newInstance(optionsDTOList);
                SpannableString spannableString = new SpannableString(topicsDTO.getTopic_name() + " (可多选)");
                spannableString.setSpan(new ForegroundColorSpan(RCDrawableUtil.getThemeAttrColor(mActivity, R.attr.RCDarkColor)),
                        spannableString.length() - 5, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                fragmentHealthEvaluationMainQuestion.setText(spannableString);
                multiSelectFragment.setOnEvaluationSetMulti(new AssessmentQuestionMultiFragment.OnEvaluationSetMulti() {
                    @Override
                    public void onNumber(List<RCAssessmentOptionsDTO> list) {
                        if (list.size() > 0) {
                            String next_topic = list.get(0).getNext_topic();
                            String answer = "";
                            for (int i = 0; i < list.size(); i++) {
                                answer += list.get(i).getOption_id() + ",";
                            }
                            //去除 ，
                            evaluationMainFragmentListener.onQuestionSelect(next_topic, topicsDTO.getTopics_id(),
                                    answer.substring(0, answer.length() - 1));
                        } else {
                            evaluationMainFragmentListener.onQuestionSelect("", "",
                                    "");
                        }

                    }
                });
                getChildFragmentManager().beginTransaction().replace(R.id.fragment_health_evaluation_main_content,
                        multiSelectFragment).commitAllowingStateLoss();
                break;
            //数字
            case Number:
                //如果是注册是完善资料，保存用户信息到sp
                AssessmentQuestionNumberFragment numberFragment =
                        AssessmentQuestionNumberFragment.newInstance(topicsDTO.getDefault_text(), optionsDTOList);
                String[] strs = topicsDTO.getDefault_text().split(",");
                evaluationMainFragmentListener.onQuestionSelect(optionsDTOList.get(0).getNext_topic(),
                        topicsDTO.getTopics_id(), strs[0]);
                numberFragment.setOnEvaluationSetNumber(new AssessmentQuestionNumberFragment.OnEvaluationSetNumber() {
                    @Override
                    public void onNumber(int n) {
                        evaluationMainFragmentListener.onQuestionSelect(
                                topicsDTO.getOptionsDTOList().get(0).getNext_topic(),
                                topicsDTO.getTopics_id(), n + "");
                    }
                });

                getChildFragmentManager().beginTransaction().replace(R.id.fragment_health_evaluation_main_content,
                        numberFragment).commitAllowingStateLoss();
                break;
            //文字
            case Text:
                AssessmentQuestionTextFragment textFragment = AssessmentQuestionTextFragment.newInstance();
                textFragment.setOnEvaluationSetText(new AssessmentQuestionTextFragment.OnEvaluationSetText() {
                    @Override
                    public void onText(String s) {
                        String next_topic = topicsDTO.getOptionsDTOList().get(0).getNext_topic();
                        evaluationMainFragmentListener.onQuestionSelect(next_topic,
                                topicsDTO.getTopics_id(), s);
                    }
                });
                getChildFragmentManager().beginTransaction().replace(R.id.fragment_health_evaluation_main_content,
                        textFragment).commitAllowingStateLoss();
                break;

        }
    }

    public void setOnEvaluationMainFragmentListener(OnEvaluationMainFragmentListener onQuestionSelectListener) {
        this.evaluationMainFragmentListener = onQuestionSelectListener;
    }

    /**
     * 接口回调
     * nextQuestionID  下题ID
     * answerID    当前题ID
     * answer   选择答案
     */
    public interface OnEvaluationMainFragmentListener {
        void onQuestionSelect(String nextQuestionID, String answerID, String answer);
    }

}

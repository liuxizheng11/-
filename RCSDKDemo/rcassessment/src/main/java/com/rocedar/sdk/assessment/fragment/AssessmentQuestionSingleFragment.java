package com.rocedar.sdk.assessment.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.rocedar.lib.base.manage.RCBaseFragment;
import com.rocedar.sdk.assessment.R;
import com.rocedar.sdk.assessment.adapter.AssessmentQuestionSingleAdapter;
import com.rocedar.sdk.assessment.dto.RCAssessmentOptionsDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：lxz
 * 日期：2018/5/23 下午5:20
 * 版本：V1.0
 * 描述：问卷--单选Fragment
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class AssessmentQuestionSingleFragment extends RCBaseFragment {

    private ListView mListView;

    private static final String ANSWER = "ANSWER";


    private AssessmentQuestionSingleAdapter singleAdapter;
    private List<RCAssessmentOptionsDTO> mList = new ArrayList<>();

    private AssessmentQuestionSingleFragment.OnEvaluationSetSingle onEvaluationSetSingle;

    public static AssessmentQuestionSingleFragment newInstance(ArrayList<RCAssessmentOptionsDTO> optionsDTOList) {
        Bundle args = new Bundle();
        args.putSerializable(ANSWER, optionsDTOList);
        AssessmentQuestionSingleFragment fragment = new AssessmentQuestionSingleFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.rc_fragment_aq_choose_listview, container, false);
        if (getArguments().containsKey(ANSWER)) {
            mList = (ArrayList<RCAssessmentOptionsDTO>) getArguments().getSerializable(ANSWER);
        }
        mListView = view.findViewById(R.id.fragment_aq_chooseList_list);
        initView();
        return view;
    }

    private void initView() {
        mListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        singleAdapter = new AssessmentQuestionSingleAdapter(mActivity, mList);
        mListView.setAdapter(singleAdapter);
        singleAdapter.setOnHealthEvaluationSingleosition(new AssessmentQuestionSingleAdapter.OnHealthEvaluationSingleosition() {
            @Override
            public void onPosition(int p) {
                if (onEvaluationSetSingle != null)
                    onEvaluationSetSingle.onNumber(mList.get(p));
            }
        });
        singleAdapter.notifyDataSetChanged();

    }


    //回调
    public interface OnEvaluationSetSingle {
        //回调多选答案
        void onNumber(RCAssessmentOptionsDTO dto);
    }

    public void setOnEvaluationSSetSingle(AssessmentQuestionSingleFragment.OnEvaluationSetSingle onEvaluationSSetSingle) {
        this.onEvaluationSetSingle = onEvaluationSSetSingle;
    }
}

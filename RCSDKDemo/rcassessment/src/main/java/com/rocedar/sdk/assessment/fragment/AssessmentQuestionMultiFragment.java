package com.rocedar.sdk.assessment.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.rocedar.lib.base.manage.RCBaseFragment;
import com.rocedar.sdk.assessment.R;
import com.rocedar.sdk.assessment.adapter.AssessmentQuestionMultiAdapter;
import com.rocedar.sdk.assessment.dto.RCAssessmentOptionsDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：lxz
 * 日期：2018/5/24 下午3:27
 * 版本：V1.0
 * 描述：健康测评--多选fragment
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class AssessmentQuestionMultiFragment extends RCBaseFragment
        implements AssessmentQuestionMultiAdapter.OnHealthEvaluationMultiposition {

    private static final String ANSWER = "ANSWER";

    private ListView listViewForScrollView;
    private AssessmentQuestionMultiAdapter multiAdapter;
    private List<RCAssessmentOptionsDTO> mList = new ArrayList<>();
    private AssessmentQuestionMultiFragment.OnEvaluationSetMulti onEvaluationSetMulti;

    public static AssessmentQuestionMultiFragment newInstance(ArrayList<RCAssessmentOptionsDTO> optionsDTOList) {
        Bundle args = new Bundle();
        args.putSerializable(ANSWER, optionsDTOList);
        AssessmentQuestionMultiFragment fragment = new AssessmentQuestionMultiFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.rc_fragment_aq_choose_listview, container, false);
        if (getArguments().containsKey(ANSWER)) {
            mList = (ArrayList<RCAssessmentOptionsDTO>) getArguments().getSerializable(ANSWER);
            initView(view);
        }
        return view;

    }

    private void initView(View view) {
        listViewForScrollView = view.findViewById(R.id.fragment_aq_chooseList_list);
        multiAdapter = new AssessmentQuestionMultiAdapter(mList, mActivity);
        listViewForScrollView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        multiAdapter.setOnHealthEvaluationMultiposition(this);
        listViewForScrollView.setAdapter(multiAdapter);
        multiAdapter.notifyDataSetChanged();

    }

    private List<RCAssessmentOptionsDTO> chooseList = new ArrayList<>();

    @Override
    public void onPosition(List<Integer> list) {
        chooseList.clear();
        for (int i = 0; i < list.size(); i++) {
            chooseList.add(mList.get(list.get(i)));
        }
        if (onEvaluationSetMulti != null)
            onEvaluationSetMulti.onNumber(chooseList);
    }

    //回调
    public interface OnEvaluationSetMulti {
        //回调多选答案
        void onNumber(List<RCAssessmentOptionsDTO> list);
    }

    public void setOnEvaluationSetMulti(OnEvaluationSetMulti onEvaluationSetMulti) {
        this.onEvaluationSetMulti = onEvaluationSetMulti;
    }
}

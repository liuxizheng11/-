package com.rocedar.sdk.assessment.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.rocedar.lib.base.manage.RCBaseFragment;
import com.rocedar.sdk.assessment.R;

/**
 * 作者：lxz
 * 日期：2018/5/25 下午3:33
 * 版本：V1.0
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class AssessmentQuestionTextFragment extends RCBaseFragment {
    private EditText text;
    private OnEvaluationSetText onEvaluationSetText;

    public static AssessmentQuestionTextFragment newInstance() {
        Bundle args = new Bundle();
        AssessmentQuestionTextFragment fragment = new AssessmentQuestionTextFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.rc_fragment_aq_text, container, false);
        initView(view);
        return view;

    }

    private void initView(View view) {
        text = (EditText) view.findViewById(R.id.health_evaluation_text_et);

        text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (onEvaluationSetText != null) {
                    onEvaluationSetText.onText(s.toString());
                }
            }
        });
    }

    //回调
    public interface OnEvaluationSetText {
        //回调答案
        void onText(String s);
    }

    public void setOnEvaluationSetText(OnEvaluationSetText onEvaluationSetText) {
        this.onEvaluationSetText = onEvaluationSetText;
    }
}

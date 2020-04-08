package com.rocedar.sdk.assessment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;

import com.rocedar.sdk.assessment.R;
import com.rocedar.sdk.assessment.dto.RCAssessmentOptionsDTO;
import com.rocedar.sdk.assessment.util.DrawableUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：lxz
 * 日期：2018/6/5 下午6:46
 * 版本：V1.0
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class AssessmentQuestionMultiAdapter extends BaseAdapter {
    private List<RCAssessmentOptionsDTO> mList = new ArrayList<>();
    private Context context;
    private List<Integer> mSelectList = new ArrayList<>();

    private OnHealthEvaluationMultiposition onHealthEvaluationMultiposition;
    /**
     * 互斥
     */
    private static final int Type_Mutex = 1;
    /**
     * 不互斥
     */
    private static final int Type_No_Mutex = 0;

    public AssessmentQuestionMultiAdapter(List<RCAssessmentOptionsDTO> mList, Context context) {
        this.mList = mList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.rc_adapter_aq_multi, null);
            viewHolder = new ViewHolder(context,convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        initView(viewHolder, position);
        return convertView;
    }

    private void initView(final ViewHolder viewHolder, final int position) {
        final RCAssessmentOptionsDTO mDTO = mList.get(position);
        viewHolder.radioButton.setText(mDTO.getOption_name());
        viewHolder.radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDTO.isSelect()) {
                    mDTO.setSelect(false);
                    for (int i = 0; i < mSelectList.size(); i++) {
                        if (mSelectList.get(i).equals(position)) {
                            mSelectList.remove(i);
                        }
                    }
                } else {
                    mSelectList.add(position);
                    //互斥
                    if (mDTO.getExclusive() == Type_Mutex) {
                        isMutex(Type_No_Mutex);
                    } else {
                        isMutex(Type_Mutex);
                    }
                    mDTO.setSelect(true);
                }
                AssessmentQuestionMultiAdapter.this.notifyDataSetChanged();
                if (onHealthEvaluationMultiposition != null) {
                    onHealthEvaluationMultiposition.onPosition(mSelectList);
                }
            }
        });
        viewHolder.radioButton.setChecked(mDTO.isSelect());

    }

    private void isMutex(int type) {
        for (int i = 0; i < mList.size(); i++) {
            if (mList.get(i).getExclusive() == type) {
                mList.get(i).setSelect(false);
                for (int j = 0; j < mSelectList.size(); j++) {
                    if (mSelectList.get(j).equals(i)) {
                        mSelectList.remove(j);
                    }
                }
            }
        }
    }

    //回调
    public interface OnHealthEvaluationMultiposition {
        //回调多选答案
        void onPosition(List<Integer> list);
    }

    public void setOnHealthEvaluationMultiposition(OnHealthEvaluationMultiposition onHealthEvaluationMultiposition) {
        this.onHealthEvaluationMultiposition = onHealthEvaluationMultiposition;
    }

    static class ViewHolder {
        Context context;

        CheckBox radioButton;

        ViewHolder(Context context, View view) {
            this.context = context;
            radioButton = view.findViewById(R.id.health_evaluation_multi_context);
            radioButton.setBackground(DrawableUtil.activity_select(context));
        }
    }
}

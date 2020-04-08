package com.rocedar.sdk.assessment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;

import com.rocedar.sdk.assessment.R;
import com.rocedar.sdk.assessment.dto.RCAssessmentOptionsDTO;
import com.rocedar.sdk.assessment.util.DrawableUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 问卷单选列表适配器
 */
public class AssessmentQuestionSingleAdapter extends BaseAdapter {
    private List<RCAssessmentOptionsDTO> mList = new ArrayList<>();
    private Context context;
    private OnHealthEvaluationSingleosition onHealthEvaluationSingleosition;
    // 用于记录每个RadioButton的状态，并保证只可选一个
    private boolean isOne = true;

    class ViewHolder {
        RadioButton mRb;
    }

    public AssessmentQuestionSingleAdapter(Context context, List<RCAssessmentOptionsDTO> optionsDTOList) {
        this.context = context;
        this.mList = optionsDTOList;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        // 页面
        final ViewHolder holder;
        LayoutInflater inflater = LayoutInflater.from(context);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.rc_adapter_aq_single, null);
            holder = new ViewHolder();
            holder.mRb = (RadioButton) convertView.findViewById(R.id.health_evaluation_single_context);
            holder.mRb.setBackground(DrawableUtil.activity_select(context));
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        initView(position, holder);

        return convertView;
    }

    private void initView(final int position, final ViewHolder holder) {
        final RCAssessmentOptionsDTO mDTO = mList.get(position);
        holder.mRb.setText(mDTO.getOption_name());
        holder.mRb.setChecked(mDTO.isSelect());
        holder.mRb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mDTO.isSelect()) {
                    mDTO.setSelect(true);
                    for (int i = 0; i < mList.size(); i++) {
                        if (i != position) {
                            mList.get(i).setSelect(false);
                        }
                    }
                }
                AssessmentQuestionSingleAdapter.this.notifyDataSetChanged();
                if (onHealthEvaluationSingleosition != null) {
                    onHealthEvaluationSingleosition.onPosition(position);
                }
            }
        });


    }

    //回调
    public interface OnHealthEvaluationSingleosition {
        //回调多选答案
        void onPosition(int p);
    }

    public void setOnHealthEvaluationSingleosition(OnHealthEvaluationSingleosition onHealthEvaluationSingleosition) {
        this.onHealthEvaluationSingleosition = onHealthEvaluationSingleosition;
    }
}

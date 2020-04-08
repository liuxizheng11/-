package com.rocedar.sdk.familydoctor.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rocedar.lib.base.unit.RCDrawableUtil;
import com.rocedar.sdk.familydoctor.dto.questionnaire.RCFDQuestionOptionsDTO;
import com.rocedar.sdk.familydoctor.util.HealthHistoryData;
import com.rocedar.sdk.familydoctor.util.RCFDDrawableUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;


/**
 * Created by phj on 2016/10/27.
 * <p>
 * 问卷答案选择VIEW
 * 1.单选
 * 2.多选
 */

public class QusetionItemView extends ViewGroup {
    public QusetionItemView(Context context) {
        super(context);
    }

    public QusetionItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private int ItemHightBase = 32;
    private int ItemHightTextLine = 14;

    private float density = 3.0f;

    private int mWidth = 0;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mOptionsDTOs != null && mOptionsDTOs.size() > 0) {
            density = getResources().getDisplayMetrics().density;
            mWidth = MeasureSpec.getSize(widthMeasureSpec);
            int height = 0;
//            viewTextLine = new ArrayList<>();
            for (int i = 0; i < mOptionsDTOs.size(); i += 2) {
                int tempLine1 = getTextLines(mOptionsDTOs.get(i).getOption_name());
                int tempLine2 = 1;
                if (i + 1 < mOptionsDTOs.size()) {
                    tempLine2 = getTextLines(mOptionsDTOs.get(i + 1).getOption_name());
                }
                if (tempLine1 >= tempLine2) {
//                    viewTextLine.add(tempLine1);
//                    viewTextLine.add(tempLine1);
                    height += (int) ((ItemHightBase + (tempLine1 * ItemHightTextLine)) * density);
                } else {
//                    viewTextLine.add(tempLine2);
//                    viewTextLine.add(tempLine2);
                    height += (int) ((ItemHightBase + (tempLine2 * ItemHightTextLine)) * density);
                }
            }
            setMeasuredDimension(mWidth, height);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    /**
     * 计算文字有几行
     *
     * @return
     */
    private int getTextLines(String text) {
        int allLines = 0;
        int maxTextNumber = (int) ((mWidth / 2 - density * 17 - density * 8) / (14 * density));
        String[] temp = text.split("\n");
        for (int i = 0; i < temp.length; i++) {
            if (getTextLength(temp[i]) > 0) {
                allLines += getTextLength(temp[i]) / maxTextNumber;
                if (getTextLength(temp[i]) % maxTextNumber > 0) {
                    allLines++;
                }
            } else {
                allLines++;
            }
        }
        return allLines > 0 ? allLines : 1;
    }

    /**
     * 计算文字个数（汉字为1、字母数据0.564(估量)）
     *
     * @param text
     * @return
     */
    private static float getTextLength(String text) {
        float count = 0.0f;
        String regEx = "[\u4e00-\u9fa5]";
        for (int i = 0; i < text.length(); i++) {
            if (Pattern.matches(regEx, text.substring(i, i + 1))) {
                count++;
            } else {
                count += 0.564;
            }
        }
        return count;
    }

    private List<RCFDQuestionOptionsDTO> mOptionsDTOs;

    private int chooseType;


    private List<TextView> mTextViewList;
//    private List<Integer> viewTextLine;


    /**
     * 设置数据源：数据源的字符串集合
     */
    public void setItemList(List<RCFDQuestionOptionsDTO> optionsDTOs, int chooseType
            , String chooseD) {
        this.chooseType = chooseType;
        this.removeAllViews();
        if (chooseType == HealthHistoryData.Type.Radio) {
            try {
                int number = Integer.parseInt(chooseD);
                chooseNumber.add(number);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        } else {
            String[] temp = chooseD.split(",");
            for (int i = 0; i < temp.length; i++) {
                try {
                    int number = Integer.parseInt(temp[i]);
                    chooseNumber.add(number);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        }
        density = getResources().getDisplayMetrics().density;
        // 获取数组
        if (mOptionsDTOs == null) {
            mOptionsDTOs = new ArrayList<>();
        } else {
            mOptionsDTOs.clear();
        }
        mOptionsDTOs.addAll(optionsDTOs);
        if (mTextViewList == null) {
//            viewTextLine = new ArrayList<>();
            mTextViewList = new ArrayList<>();
        } else {
//            viewTextLine.clear();
            mTextViewList.clear();
        }
        for (int i = 0; i < mOptionsDTOs.size(); i++) {
            final int index = i;
            TextView textView = new TextView(getContext());
            textView.setGravity(Gravity.CENTER);
//            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
//                    LayoutParams.MATCH_PARENT);
//            lp.gravity = Gravity.CENTER;
//            textView.setLayoutParams(lp);
            textView.setPadding(0, (int) (density * 9), 0, 0);
            textView.setText(mOptionsDTOs.get(i).getOption_name());
            textView.setBackground(RCFDDrawableUtil.rectangle_cccccc_5px(getContext()));
            textView.setTextColor(Color.parseColor("#333333"));
            for (int optid : chooseNumber) {
                if (optid == mOptionsDTOs.get(i).getOption_id()) {
                    textView.setBackground(RCDrawableUtil.getMainColorDrawableBaseRadius(getContext()));
                    textView.setTextColor(getResources().getColor(android.R.color.white));
                }
            }
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //
                    if (onClickListener != null) {
                        reLoadTextViewBg(index);
                        onClickListener.onClick(v, index, chooseNumber);
                    }
                }
            });
            textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14f);
            mTextViewList.add(textView);
            this.addView(textView);
            //如果时多选，加载选中的选项类型（多选互斥）
            if (chooseType == HealthHistoryData.Type.MultiSelect && lastChooseType == -1) {
                for (int j = 0; j < chooseNumber.size(); j++) {
                    if (mOptionsDTOs.get(i).getOption_id() == chooseNumber.get(j)) {
                        lastChooseType = mOptionsDTOs.get(i).getOption_type();
                    }
                }
            }
        }
        invalidate();// requestLayout();


    }

    private List<Integer> chooseNumber = new ArrayList<>();


    /**
     * 多选时，最后选中的答案的类型（多选有互斥的情况）
     */
    private int lastChooseType = -1;

    /**
     * 点击了选项，如果选择则取消选中，没有选中加上选中
     *
     * @param chooseIndex
     */
    private void reLoadTextViewBg(int chooseIndex) {
        if (chooseType == HealthHistoryData.Type.Radio) {
            chooseNumber.clear();
            chooseNumber.add(mOptionsDTOs.get(chooseIndex).getOption_id());
            for (int i = 0; i < mTextViewList.size(); i++) {
                if (i == chooseIndex) {
                    mTextViewList.get(i).setBackground(RCDrawableUtil.getMainColorDrawableBaseRadius(getContext()));
                    mTextViewList.get(i).setTextColor(Color.WHITE);
                } else {
                    mTextViewList.get(i).setBackground(RCFDDrawableUtil.rectangle_cccccc_5px(getContext()));
                    mTextViewList.get(i).setTextColor(Color.parseColor("#333333"));
                }
            }
        } else if (chooseType == HealthHistoryData.Type.MultiSelect) {
            if (removeChoose(mOptionsDTOs.get(chooseIndex).getOption_id())) {
                //取消了选中
                mTextViewList.get(chooseIndex).setBackground(RCFDDrawableUtil.rectangle_cccccc_5px(getContext()));
                mTextViewList.get(chooseIndex).setTextColor(Color.parseColor("#333333"));
            } else {
                //如果选中的和之前选中的类型不一样，为互斥，清除选中
                if (lastChooseType != -1 && lastChooseType != mOptionsDTOs.get(chooseIndex).getOption_type()) {
                    chooseNumber.clear();
                    for (int i = 0; i < mTextViewList.size(); i++) {
                        mTextViewList.get(i).setBackground(RCFDDrawableUtil.rectangle_cccccc_5px(getContext()));
                        mTextViewList.get(i).setTextColor(Color.parseColor("#333333"));
                    }
                }
                lastChooseType = mOptionsDTOs.get(chooseIndex).getOption_type();
                chooseNumber.add(mOptionsDTOs.get(chooseIndex).getOption_id());
                mTextViewList.get(chooseIndex).setBackground(RCDrawableUtil.getMainColorDrawableBaseRadius(getContext()));
                mTextViewList.get(chooseIndex).setTextColor(Color.WHITE);
            }
        }
    }


    private boolean removeChoose(int qid) {
        for (int i = 0; i < chooseNumber.size(); i++) {
            if (chooseNumber.get(i) == qid) {
                if (chooseNumber.size() == 1) {
                    chooseNumber.clear();
                } else
                    chooseNumber.remove(i);
                return true;
            }
        }
        return false;
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right,
                            int bottom) {
        if (mTextViewList != null && mTextViewList.size() > 0) {
            int sLeft, sTop, sRight, sBottom;
            int temph = (int) (density * 5);
            for (int i = 0; i < mTextViewList.size(); i += 2) {
                int line = getTextLines(mTextViewList.get(i).getText().toString());
                if (i + 1 < mTextViewList.size()) {
                    int line2 = getTextLines(mTextViewList.get(i + 1).getText().toString());
                    if (line2 > line) {
                        line = line2;
                    }
                }
                int width = (right - left) / 2;
                sTop = temph;
                int height = (int) ((ItemHightBase + line * ItemHightTextLine) * density);
                sBottom = (int) (sTop + height - 10 * density);
                if (line > 1) {
                    mTextViewList.get(i).setGravity(Gravity.LEFT | Gravity.CENTER_HORIZONTAL);
                    int padding = (int) (4 * density);
                    mTextViewList.get(i).setPadding(padding, padding, padding, padding);
                }
                sLeft = (int) (0 + density * 12);
                sRight = (int) (width - density * 5);
                mTextViewList.get(i).layout(sLeft, sTop, sRight, sBottom);
                if (i + 1 < mTextViewList.size()) {
                    sLeft = (int) (width + density * 5);
                    sRight = (int) (2 * width - density * 12);
                    mTextViewList.get(i + 1).layout(sLeft, sTop, sRight, sBottom);
                    if (line > 1) {
                        mTextViewList.get(i + 1).setGravity(Gravity.LEFT | Gravity.CENTER_HORIZONTAL);
                        int padding = (int) (4 * density);
                        mTextViewList.get(i + 1).setPadding(padding, padding, padding, padding);
                    }
                }
                temph += height;
            }
        }
    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        invalidate();
    }

    private OnClickListener onClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }


    public interface OnClickListener {
        void onClick(View v, int index, List<Integer> chooseQidList);
    }
}

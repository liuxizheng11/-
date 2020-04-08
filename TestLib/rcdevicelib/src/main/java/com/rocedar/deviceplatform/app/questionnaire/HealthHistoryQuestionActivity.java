package com.rocedar.deviceplatform.app.questionnaire;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rocedar.base.RCHandler;
import com.rocedar.base.RCJavaUtil;
import com.rocedar.base.manger.RCBaseActivity;
import com.rocedar.deviceplatform.R;
import com.rocedar.deviceplatform.dto.questionnaire.HealthHistoryQuestionDTO;
import com.rocedar.deviceplatform.dto.questionnaire.HealthHistoryQuestionFirstDTO;
import com.rocedar.deviceplatform.questionnaire.HealthHistoryData;
import com.rocedar.deviceplatform.questionnaire.view.QusetionItemView;
import com.rocedar.deviceplatform.request.impl.RCQuestionnaireRequestImpl;
import com.rocedar.deviceplatform.request.listener.RCQuestionnaireDetailsListener;
import com.rocedar.deviceplatform.request.listener.RCRequestSuccessListener;
import com.rocedar.deviceplatform.sharedpreferences.RCQuestionnaireInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.List;

/**
 * Created by phj on 2016/10/27.
 */

public class HealthHistoryQuestionActivity extends RCBaseActivity {


    private LayoutInflater layoutInflater;

    private LinearLayout linearLayout;

    private List<HealthHistoryQuestionFirstDTO> dtoList;

    private TextView save;


    private boolean isEdit = false;

    public static void goActivity(Context context, int questionnaireId) {
        Intent intent = new Intent(context, HealthHistoryQuestionActivity.class);
        intent.putExtra("questionnaire_id", questionnaireId);
        context.startActivity(intent);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_history_question);
        layoutInflater = LayoutInflater.from(mContext);
        final int questionnaire_id = getIntent().getIntExtra("questionnaire_id", -1);
        if (questionnaire_id == -1)
            finishActivity();
        linearLayout = (LinearLayout) findViewById(R.id.activity_health_history_question_layout);
        save = (TextView) findViewById(R.id.activity_health_history_question_save);
        getHistoryInfo(questionnaire_id);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEdit) {
                    saveHistoryInfo(questionnaire_id);
                } else {
                    isEdit = true;
                    changeBottomBut();
                    loadFirstView();
                }
            }
        });

    }

    private void changeBottomBut() {
        if (isEdit) {
            save.setText(getString(R.string.rcdevice_save));
        } else {
            save.setText(getString(R.string.rcdevice_edit));
            save.setEnabled(true);
        }
    }

    /**
     *
     */
    private void showAnswerInfo() {
        linearLayout.removeAllViews();
        dtoList = HealthHistoryData.getQuestionnaireFisrt(resultObject);
        for (int i = 0; i < dtoList.size(); i++) {
            linearLayout.addView(getItemse(dtoList.get(i), true));
        }

    }

    /**
     * 问卷内容详情
     */
    private JSONObject resultObject = new JSONObject();
    /* 问题选择的回答存储*/
    private JSONObject saveObject = new JSONObject();

    /**
     * 获取问卷详情
     *
     * @param questionnaire_id
     */
    private void getHistoryInfo(final int questionnaire_id) {
        mRcHandler.sendMessage(RCHandler.START);
        final String[] info = RCQuestionnaireInfo.getQuestionnaireInfo(questionnaire_id);

        RCQuestionnaireRequestImpl.getInstance(mContext).loadQuestionnaireDetails(new RCQuestionnaireDetailsListener() {
            @Override
            public void getDataSuccess(JSONObject result) {
                mRcHeadUtil.setLeftBack().setTitle(getString(R.string.rcdevice_test_blood_pressure));
                //显示问卷答案
                if (!"".equals(result.optJSONObject("answer")) || result.optJSONObject("answer") != null) {
                    saveObject = result.optJSONObject("answer");
                }
                if (result.has("questionnaire") && result.optJSONArray("questionnaire").length() > 0) {
                    resultObject = result;
                    //缓存
                    RCQuestionnaireInfo.saveQuestionnaireInfo(questionnaire_id, result.toString(), result.optLong("publish_time"));
                } else {
                    //从缓存中取
                    try {
                        if (!"".equals(info[0]))
                            resultObject = new JSONObject(info[0]);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                //设置title
                mRcHeadUtil.setLeftBack().setTitle(resultObject.optString("title"));

//                else {
                isEdit = true;
                changeBottomBut();
                loadFirstView();
//                }
                mRcHandler.sendMessage(RCHandler.GETDATA_OK);
            }

            @Override
            public void getDataError(int status, String msg) {
                mRcHandler.sendMessage(RCHandler.GETDATA_OK);
            }
        }, questionnaire_id, info[1] == "-1" ? -1 : Long.parseLong(info[1]));

    }

    /**
     * 保存回答接口请求
     *
     * @param questionnaire_id
     */
    private void saveHistoryInfo(int questionnaire_id) {
        mRcHandler.sendMessage(RCHandler.START);
        RCQuestionnaireRequestImpl.getInstance(mContext).saveQuestionnaireResult(new RCRequestSuccessListener() {
            @Override
            public void requestSuccess() {
//                isEdit = false;
//                changeBottomBut();
//                showAnswerInfo();
                finishActivity();
                mRcHandler.sendMessage(RCHandler.GETDATA_OK);
            }

            @Override
            public void requestError(int status, String msg) {
                mRcHandler.sendMessage(RCHandler.GETDATA_OK);
            }
        }, questionnaire_id, saveObject.toString());

    }

    /**
     * 为填写问卷时，重新加载返回按钮状态
     */
    private void reLoadSaveBut() {
        if (saveObject == null || saveObject.length() == 0) {
            save.setEnabled(false);
            save.setBackgroundColor(getResources().getColor(R.color.rcbase_app_button_no_focus));
            return;
        }
        boolean temp = true;
        try {
            Iterator it = saveObject.keys();
            while (it.hasNext()) {
                String key = (String) it.next();
                String value = saveObject.getString(key);
                if (value != null && value.equals("")) {
                    temp = false;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            temp = false;
        }
        if (temp) {
            save.setBackgroundColor(getResources().getColor(R.color.rcbase_app_main));
            save.setEnabled(true);
        } else {
            save.setEnabled(false);
            save.setBackgroundColor(getResources().getColor(R.color.rcbase_app_button_no_focus));
        }
    }

    private void loadFirstView() {
        linearLayout.removeAllViews();
        dtoList = HealthHistoryData.getQuestionnaireFisrt(resultObject);
        for (int i = 0; i < dtoList.size(); i++) {
            linearLayout.addView(getItemse(dtoList.get(i), false));
        }
        reLoadSaveBut();
    }


    /**
     * 加载第一级数据
     *
     * @param dto
     * @param isOver
     * @return
     */
    private View getItemse(HealthHistoryQuestionFirstDTO dto, boolean isOver) {
        View view = layoutInflater.inflate(R.layout.view_question_itemize, null);
        if (dto.getGroup_name().equals("")) {
            view.findViewById(R.id.view_question_itemize_title_layout).setVisibility(View.GONE);
        } else {
            ((TextView) view.findViewById(R.id.view_question_itemize_title)).setText(
                    dto.getGroup_name()
            );
        }
        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.view_question_itemize_layout);
        for (int i = 0; dto.getTopics() != null && i < dto.getTopics().length; i++) {
            //是否是加载完成问卷后的数据显示
            if (isOver) {
                //显示完成问卷后的UI
                View tempView = getOverView(dto.getTopics()[i] + "");
                if (tempView != null) {
                    linearLayout.addView(tempView);
                }
            } else {
                //显示一级问卷的UI
                View tempView = getQusetionView(dto.getTopics()[i] + "");
                if (tempView != null) {
                    //初始化一级选项选择的数据
                    putDataJson(dto.getTopics()[i] + "", "");
                    linearLayout.addView(tempView);
                }
            }
        }
        return view;
    }

    /**
     * 更改回答选择
     *
     * @param key   问题的ID
     * @param value 问题的回答，
     *              1.传null时直接将回答置为""
     *              2.传""时会判断之前是否有值，有值不处理
     *              3.不为null且不为""，直接覆盖
     */
    private void putDataJson(String key, String value) {
        try {
            if (value == null)
                saveObject.put(key, "");
            else if (value.equals("") && saveObject.has(key) && !saveObject.optString(key).equals(""))
                return;
            else
                saveObject.put(key, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 展示回答的UI
     *
     * @param qid 问题ID
     * @return
     */
    @Nullable
    private View getOverView(String qid) {
        HealthHistoryQuestionDTO dto = HealthHistoryData.getAllQuestionnaire(resultObject, qid);
        if (dto == null) return null;
        View view = layoutInflater.inflate(R.layout.view_question_over, null);
        ((TextView) view.findViewById(R.id.view_question_over_title)).setText(
                dto.getTopic_name()
        );
        String tempOver = "";
        if (saveObject.has(qid) && !saveObject.optString(qid).equals("")) {
            String tempChoose[] = saveObject.optString(qid).split(",");
            String tempQ1 = "";
            String tempQ2 = "";
            for (int j = 0; dto.getOptionsList() != null && j < dto.getOptionsList().size(); j++) {
                for (int k = 0; k < tempChoose.length; k++) {
                    if ((dto.getOptionsList().get(j).getOption_id() + "").equals(tempChoose[k])) {
                        tempQ1 = tempQ1 + dto.getOptionsList().get(j).getOption_name() + "、";
                        tempQ2 = getOverText(dto.getOptionsList().get(j).getChild_topic());
                    }
                }
            }
            if (dto.getAnswer_template() != null && dto.getAnswer_template().contains("<value>")) {
                tempQ1 = dto.getAnswer_template().replace("<value>", "@DyS@" + RCJavaUtil.subLastDunhao(tempQ1) + "@DyE@");
            } else {
                tempQ1 = RCJavaUtil.subLastDunhao(tempQ1);
            }
            tempOver = tempQ1 + tempQ2;
        }

        ((TextView) view.findViewById(R.id.view_question_over_info)).setText(
                setShowTextStyle(tempOver)
        );

        return view;
    }


    private SpannableStringBuilder setShowTextStyle(String str) {
        SpannableStringBuilder spannable = new SpannableStringBuilder("");
        if (str.length() > 0) {
            String[] tempS = str.split("@DyS@");
            if (tempS != null) {
                for (int i = 0; i < tempS.length; i++) {
                    if (i != 0) {
                        String tempE = tempS[i];
                        int tempEindex = tempE.indexOf("@DyE@");
                        int tempSindex = spannable.length();
                        tempE = tempE.replace("@DyE@", "");
                        spannable.append(tempE);
                        spannable.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.rcbase_app_main)), tempSindex,
                                tempSindex + tempEindex
                                , Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    } else {
                        spannable.append(tempS[i]);
                    }
                }
            }

        }
        return spannable;
    }

    private String getOverText(String childIds[]) {
        String temp = "";
        for (int i = 0; childIds != null && i < childIds.length; i++) {
            if (saveObject.has(childIds[i]) && !saveObject.optString(childIds[i]).equals("")) {
                HealthHistoryQuestionDTO dto = HealthHistoryData.getAllQuestionnaire(resultObject, childIds[i]);
                String tempQ1 = "";
                String tempQ2 = "";
                if (dto.getType_id() == HealthHistoryData.Type.GapFilling) {
                    tempQ1 = dto.getAnswer_template().replace("<value>", "@DyS@" + saveObject.optString(childIds[i]) + "@DyE@");
                } else {
                    String tempChoose[] = saveObject.optString(childIds[i]).split(",");
                    for (int j = 0; dto.getOptionsList() != null && j < dto.getOptionsList().size(); j++) {
                        for (int k = 0; k < tempChoose.length; k++) {
                            if ((dto.getOptionsList().get(j).getOption_id() + "").equals(tempChoose[k])) {
                                tempQ1 = tempQ1 + dto.getOptionsList().get(j).getOption_name() + "、";
                                tempQ2 = getOverText(dto.getOptionsList().get(j).getChild_topic());
                            }
                        }
                    }
                    tempQ1 = dto.getAnswer_template().replace("<value>", "@DyS@" + RCJavaUtil.subLastDunhao(tempQ1) + "@DyE@");
                }
                temp = temp + tempQ1 + tempQ2;
            }
        }
        return temp;
    }


    /**
     * 加载问题UI方法，根据问题类型判断加载不同的UI
     *
     * @param qid
     * @return 根据不填的问题类型返回不同的VIEW
     */
    private View getQusetionView(String qid) {
        HealthHistoryQuestionDTO dto = HealthHistoryData.getAllQuestionnaire(resultObject, qid);
        if (dto.getType_id() == HealthHistoryData.Type.Radio) {
            return getRadioView(dto);
        } else if (dto.getType_id() == HealthHistoryData.Type.MultiSelect) {
            return getMultiSelectView(dto);
        } else if (dto.getType_id() == HealthHistoryData.Type.GapFilling) {
            return getGapFillingView(dto);
        }
        return null;
    }


    /**
     * 单选问题UI加载
     *
     * @param dto 问题数据
     * @return VIEW对象
     */
    private View getRadioView(final HealthHistoryQuestionDTO dto) {
        if (dto == null) return null;
        View view = layoutInflater.inflate(R.layout.view_question_radio, null);
        ((TextView) view.findViewById(R.id.view_question_radio_title)).setText(
                dto.getTopic_name()
        );
        final LinearLayout radioLinearLayout = (LinearLayout) view.findViewById(R.id.view_question_radio_layout);
        QusetionItemView qusetionItemView = (QusetionItemView) view.findViewById(R.id.view_question_radio_item);
        qusetionItemView.setItemList(dto.getOptionsList(), HealthHistoryData.Type.Radio, saveObject.optString(dto.getqId()));
        qusetionItemView.setOnClickListener(new QusetionItemView.OnClickListener() {
            @Override
            public void onClick(View v, int index, List<Integer> chooseQidList) {
                /*存入数据，遍历选择的项*/
                String temp = "";
                for (int i : chooseQidList) {
                    temp += i + ",";
                }
                if (temp.equals("")) {
                    temp = null;
                }
                temp = RCJavaUtil.subLastComma(temp);
                /* 结果存入到临时存储*/
                putDataJson(dto.getqId(), temp);
                /* 加载子UI*/
                String childTopic[] = dto.getOptionsList().get(index).getChild_topic();
                loadChildView(radioLinearLayout, childTopic);
                /* 保存按钮刷新*/
                reLoadSaveBut();
            }
        });
        if (saveObject.has(dto.getqId()) && !saveObject.optString(dto.getqId()).equals("")) {
            String temp[] = saveObject.optString(dto.getqId()).split(",");
            for (int i = 0; i < temp.length; i++) {
                for (int j = 0; j < dto.getOptionsList().size(); j++) {
                    if ((dto.getOptionsList().get(j).getOption_id() + "").equals(temp[i])) {
                         /* 加载子UI*/
                        String childTopic[] = dto.getOptionsList().get(j).getChild_topic();
                        loadChildView(radioLinearLayout, childTopic);
                    }
                }
            }
            /* 保存按钮刷新*/
            reLoadSaveBut();
        }
        return view;
    }

    /**
     * 加载子问题UI
     *
     * @param radioLinearLayout 承载UI的Layout
     * @param childTopic        子问题的ID数组
     */
    private void loadChildView(LinearLayout radioLinearLayout, String[] childTopic) {
        /*子问题不为空*/
        if (childTopic != null) {
            /*取Tag里面的子问题ID*/
            String childTopicTemp[] = ((String[]) radioLinearLayout.getTag());
            /*如果选项的子问题ID和保存的一致，不用加载*/
            if (childTopicTemp != null && equalsArray(childTopicTemp, childTopic)) {
                return;
            }
            /* 重新加载数据*/
            radioLinearLayout.removeAllViews();
            /* 将子问题ID加入到Tag*/
            radioLinearLayout.setTag(childTopic);
            /* 遍历加载子问题*/
            for (int i = 0; i < childTopic.length; i++) {
                View tempView = getQusetionView(childTopic[i]);
                if (tempView != null) {
                    /*初始化回答*/
                    putDataJson(childTopic[i], "");
                    radioLinearLayout.addView(tempView);
                }
            }
        } else {
            /*子问题为空，清空UI和子问题的回答*/
            String childTopicTemp[] = ((String[]) radioLinearLayout.getTag());
            radioLinearLayout.removeAllViews();
            radioLinearLayout.setTag(null);
            for (int j = 0; childTopicTemp != null && j < childTopicTemp.length; j++) {
                if (saveObject.has(childTopicTemp[j])) {
                     /*删除回答*/
                    saveObject.remove(childTopicTemp[j]);
                }
            }
        }
    }

    /**
     * 多选问题UI加载
     *
     * @param dto 问题数据
     * @return VIEW对象
     */
    private View getMultiSelectView(final HealthHistoryQuestionDTO dto) {
        if (dto == null) return null;
        View view = layoutInflater.inflate(R.layout.view_question_radio, null);
        ((TextView) view.findViewById(R.id.view_question_radio_title)).setText(
                dto.getTopic_name()
        );
        final LinearLayout multiSelectLinearLayout = (LinearLayout) view.findViewById(R.id.view_question_radio_layout);
        QusetionItemView qusetionItemView = (QusetionItemView) view.findViewById(R.id.view_question_radio_item);
        qusetionItemView.setItemList(dto.getOptionsList(), HealthHistoryData.Type.MultiSelect, saveObject.optString(dto.getqId()));
        qusetionItemView.setOnClickListener(new QusetionItemView.OnClickListener() {
            @Override
            public void onClick(View v, int index, List<Integer> chooseQidList) {
                /*存入数据*/
                String temp = "";
                for (int i : chooseQidList) {
                    temp += i + ",";
                }
                temp = RCJavaUtil.subLastComma(temp);
                if (temp.equals("")) {
                    temp = null;
                }
                putDataJson(dto.getqId(), temp);
//                String childTopic[] = dto.getOptionsList().get(index).getChild_topic();
//                loadChildView(multiSelectLinearLayout, childTopic);
                reLoadSaveBut();
            }
        });
        return view;
    }

    /**
     * 填空问题UI加载
     *
     * @param dto 问题数据
     * @return VIEW对象
     */
    private View getGapFillingView(final HealthHistoryQuestionDTO dto) {
        if (dto == null) return null;
        final View view = layoutInflater.inflate(R.layout.view_question_gap_filling, null);
        ((TextView) view.findViewById(R.id.view_question_gap_filling_title)).setText(
                dto.getTopic_name()
        );
        EditText editText = (EditText) view.findViewById(R.id.view_question_gap_filling_et);
        if (saveObject.has(dto.getqId()) && !saveObject.optString(dto.getqId()).equals("")) {
            editText.setText(saveObject.optString(dto.getqId()));
        }
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().trim().equals("")) {
                    putDataJson(dto.getqId(), null);
                } else
                    putDataJson(dto.getqId(), s.toString().trim());
                reLoadSaveBut();
            }
        });
        return view;
    }

    public boolean equalsArray(String[] a, String[] a2) {
        if (a == a2)
            return true;
        if (a == null || a2 == null)
            return false;
        int length = a.length;
        if (a2.length != length)
            return false;
        for (int i = 0; i < length; i++)
            if (a[i].equals(a2[i]))
                return false;
        return true;
    }


}

package com.rocedar.sdk.assessment.request.impl;

import android.app.Activity;

import com.rocedar.lib.base.network.IRCPostListener;
import com.rocedar.lib.base.network.IResponseData;
import com.rocedar.lib.base.network.RCBean;
import com.rocedar.lib.base.network.RCRequestNetwork;
import com.rocedar.sdk.assessment.dto.RCAssessmentInfoDTO;
import com.rocedar.sdk.assessment.dto.RCAssessmentListDTO;
import com.rocedar.sdk.assessment.dto.RCAssessmentOptionsDTO;
import com.rocedar.sdk.assessment.dto.RCAssessmentResultDTO;
import com.rocedar.sdk.assessment.dto.RCAssessmentTopicsDTO;
import com.rocedar.sdk.assessment.request.IRCAssessmentRequest;
import com.rocedar.sdk.assessment.request.bean.BeanGetAssessmentList;
import com.rocedar.sdk.assessment.request.bean.BeanPostQuestionData;
import com.rocedar.sdk.assessment.request.listener.RCAssessmentInfoListener;
import com.rocedar.sdk.assessment.request.listener.RCAssessmentListListener;
import com.rocedar.sdk.assessment.request.listener.RCAssessmentResultListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 项目名称：瑰柏SDK-基础库
 * <p>
 * 作者：phj
 * 日期：2018/6/26 下午12:15
 * 版本：V1.0.00
 * 描述：瑰柏SDK-
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCAssessmentRequestImpl implements IRCAssessmentRequest {


    private Activity mContext;

    public RCAssessmentRequestImpl(Activity context) {
        this.mContext = context;
    }

    /**
     * 问卷详情 请求
     *
     * @param question_id
     * @param listener
     */
    @Override
    public void getEvaluationParticulars(int question_id, final RCAssessmentInfoListener listener) {
        RCBean mBean = new RCBean();
        mBean.setActionName("p/health/questionnaire/" + question_id + "/");
        RCRequestNetwork.NetWorkGetData(mContext, mBean, RCRequestNetwork.Method.Get,
                new IResponseData() {
                    @Override
                    public void getDataSucceedListener(JSONObject data) {
                        JSONObject result = data.optJSONObject("result");
                        RCAssessmentInfoDTO evaluationDataDTO = new RCAssessmentInfoDTO();
                        evaluationDataDTO.setQuestionnaire_desc(result.optString("questionnaire_desc"));
                        evaluationDataDTO.setQuestionnaire_id(result.optInt("questionnaire_id"));
                        evaluationDataDTO.setQuestionnaire_name(result.optString("questionnaire_name"));
                        evaluationDataDTO.setStart_topic_id(result.optInt("start_topic_id"));
                        List<RCAssessmentTopicsDTO> mList = new ArrayList<>();
                        JSONObject topics =result.optJSONObject("topics");
                        if (topics != null) {
                            Iterator<String> it = topics.keys();
                            while (it.hasNext()) {
                                RCAssessmentTopicsDTO topicsDTO = new RCAssessmentTopicsDTO();
                                String key = it.next();
                                topicsDTO.setTopics_id(key);
                                JSONObject mjo = topics.optJSONObject(key);
                                topicsDTO.setDefault_text(mjo.optString("default"));
                                topicsDTO.setType_id(mjo.optInt("type_id"));
                                topicsDTO.setTopic_name(mjo.optString("topic_name"));
                                JSONArray mja = mjo.optJSONArray("options");
                                List<RCAssessmentOptionsDTO> mOptionList = new ArrayList<>();
                                for (int i = 0; i < mja.length(); i++) {
                                    JSONObject opt_mjo = mja.optJSONObject(i);
                                    RCAssessmentOptionsDTO optionsDTO = new RCAssessmentOptionsDTO();
                                    optionsDTO.setNext_topic(opt_mjo.optString("next_topic"));
                                    optionsDTO.setExclusive(opt_mjo.optInt("exclusive"));
                                    optionsDTO.setOption_id(opt_mjo.optInt("option_id"));
                                    optionsDTO.setOption_name(opt_mjo.optString("option_name"));
                                    optionsDTO.setSelect(false);
                                    mOptionList.add(optionsDTO);
                                }
                                topicsDTO.setOptionsDTOList((ArrayList<RCAssessmentOptionsDTO>) mOptionList);
                                mList.add(topicsDTO);
                            }
                        }
                        evaluationDataDTO.setTopicsDTOList(mList);
                        listener.getDataSuccess(evaluationDataDTO);
                    }

                    @Override
                    public void getDataErrorListener(String msg, int status) {
                        listener.getDataError(status, msg);
                    }
                });
    }


    /**
     * 问卷列表 请求
     *
     * @param listListener
     */
    @Override
    public void getEvaluationListData(final RCAssessmentListListener listListener) {
        BeanGetAssessmentList mBean = new BeanGetAssessmentList();
        mBean.setQuestionnaire_type("3");
        mBean.setActionName("/p/health/questionnaire/");
        RCRequestNetwork.NetWorkGetData(mContext, mBean, RCRequestNetwork.Method.Get,
                new IResponseData() {
                    @Override
                    public void getDataSucceedListener(JSONObject data) {
                        List<RCAssessmentListDTO> mList = new ArrayList<>();
                        JSONArray mja = data.optJSONObject("result").optJSONArray("questionnaires");
                        for (int i = 0; i < mja.length(); i++) {
                            RCAssessmentListDTO mDTO = new RCAssessmentListDTO();
                            JSONObject mjo = mja.optJSONObject(i);
                            mDTO.setQuestionnaire_id(mjo.optInt("questionnaire_id"));
                            mDTO.setQuestionnaire_name(mjo.optString("questionnaire_name"));
                            mDTO.setQuestionnaire_desc(mjo.optString("questionnaire_desc"));
                            mDTO.setThumbnail(mjo.optString("thumbnail"));
                            mDTO.setFill_in(mjo.optInt("fill_in"));
                            mList.add(mDTO);
                        }
                        listListener.getDataSuccess(mList);
                    }

                    @Override
                    public void getDataErrorListener(String msg, int status) {
                        listListener.getDataError(status, msg);
                    }
                });
    }

    /**
     * 保存问卷结果
     *
     * @param listener
     * @param questionnaireId 问卷id
     * @param answer          用户问卷结果(String)
     */
    @Override
    public void saveEvaluationQuestionSubmit(int questionnaireId, String answer, final IRCPostListener listener) {
        BeanPostQuestionData bean = new BeanPostQuestionData();
        bean.setActionName("/p/health/questionnaire/" + questionnaireId + "/");
        bean.setAnswer(answer);
        RCRequestNetwork.NetWorkGetData(mContext, bean, RCRequestNetwork.Method.Post, new IResponseData() {
            @Override
            public void getDataSucceedListener(JSONObject data) {
                listener.getDataSuccess();
            }

            @Override
            public void getDataErrorListener(String msg, int status) {
                listener.getDataError(status, msg);
            }
        });
    }

    /**
     * 问卷结果数据请求
     *
     * @param question_id
     * @param listener
     */
    @Override
    public void getEvaluationResultData(int question_id, final RCAssessmentResultListener listener) {
        RCBean mBean = new RCBean();
        mBean.setActionName("p/health/questionnaire/" + question_id + "/" + "answer/");
        RCRequestNetwork.NetWorkGetData(mContext, mBean, RCRequestNetwork.Method.Get,
                new IResponseData() {
                    @Override
                    public void getDataSucceedListener(JSONObject data) {
                        JSONObject mjo = data.optJSONObject("result");
                        RCAssessmentResultDTO dto = new RCAssessmentResultDTO();
                        dto.setQuestionnaireName(mjo.optString("questionnaire_name"));
                        dto.setTitle(mjo.optJSONObject("result").optString("title"));
                        dto.setBulletin(mjo.optJSONObject("result").optString("bulletin"));
                        listener.getDataSuccess(dto);
                    }

                    @Override
                    public void getDataErrorListener(String msg, int status) {
                        listener.getDataError(status, msg);
                    }
                });
    }
}

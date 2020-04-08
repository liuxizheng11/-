package com.rocedar.sdk.familydoctor.request.impl;

import android.app.Activity;

import com.rocedar.lib.base.network.IRCPostListener;
import com.rocedar.lib.base.network.IResponseData;
import com.rocedar.lib.base.network.RCBean;
import com.rocedar.lib.base.network.RCRequestNetwork;
import com.rocedar.lib.base.unit.RCLog;
import com.rocedar.sdk.familydoctor.dto.questionnaire.RCFDQuestionListDTO;
import com.rocedar.sdk.familydoctor.request.IRCQuestionnaireRequest;
import com.rocedar.sdk.familydoctor.request.bean.BeanGetQuestionnaieDetails;
import com.rocedar.sdk.familydoctor.request.listener.question.RCQuestionnaireDetailsListener;
import com.rocedar.sdk.familydoctor.request.listener.question.RCQuestionnaireListListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称：瑰柏SDK-基础库
 * <p>
 * 作者：phj
 * 日期：2018/6/3 下午9:06
 * 版本：V1.0.00
 * 描述：瑰柏SDK-
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCQuestionnaireRequestImpl implements IRCQuestionnaireRequest {


    private static String TAG = "RCQuestionnaire_Request";

    private Activity mContext;

    public RCQuestionnaireRequestImpl(Activity context) {
        this.mContext = context;
    }

    /**
     * 问卷类型列表
     *
     * @param listener
     */
    @Override
    public void loadQuestionnaireList(final RCQuestionnaireListListener listener) {
        RCBean bean = new RCBean();
        bean.setActionName("/p/health/questionnaire/");
        RCRequestNetwork.NetWorkGetData(mContext, bean, RCRequestNetwork.Method.Get, new IResponseData() {
            @Override
            public void getDataSucceedListener(JSONObject data) {
                JSONArray array = data.optJSONObject("result").optJSONArray("questionnaires");
                List<RCFDQuestionListDTO> mDatas = new ArrayList<>();
                RCFDQuestionListDTO dto;
                for (int i = 0; i < array.length() && array.length() > 0; i++) {
                    JSONObject object = array.optJSONObject(i);
                    dto = new RCFDQuestionListDTO();
                    dto.setQuestionnaire_id(object.optInt("questionnaire_id"));
                    dto.setQuestionnaire_name(object.optString("questionnaire_name"));
                    dto.setQuestionnaire_desc(object.optString("questionnaire_desc"));
                    dto.setThumbnail(object.optString("thumbnail"));
                    dto.setQuestionnaire_url(object.optString("questionnaire_url"));
                    dto.setMiss_params(object.optString("miss_params"));
                    dto.setFill_in(object.optInt("fill_in"));
                    mDatas.add(dto);
                }
                listener.getDataSuccess(mDatas);
                RCLog.d(TAG, "获取问卷类型列表成功");
            }

            @Override
            public void getDataErrorListener(String msg, int status) {
                listener.getDataError(status, msg);
                RCLog.d(TAG, "获取问卷类型列表失败" + msg);
            }
        });
    }

    /**
     * 问卷详情
     *
     * @param listener
     * @param questionnaireId 问卷id
     * @param publicsh_time   问题发布时间
     */
    @Override
    public void loadQuestionnaireDetails(final RCQuestionnaireDetailsListener listener, long userID, int questionnaireId, long publicsh_time) {
        BeanGetQuestionnaieDetails bean = new BeanGetQuestionnaieDetails();
        bean.setActionName("/p/health/questionnaire/" + questionnaireId + "/");
        bean.setPublicsh_time(publicsh_time + "");
        if (userID > 0) {
            bean.setOther_user(userID + "");
        }
        RCRequestNetwork.NetWorkGetData(mContext, bean, RCRequestNetwork.Method.Get, new IResponseData() {
            @Override
            public void getDataSucceedListener(JSONObject data) {
                JSONObject result = data.optJSONObject("result");
                listener.getDataSuccess(result);
                RCLog.d(TAG, "获取问卷详情成功");

            }

            @Override
            public void getDataErrorListener(String msg, int status) {
                listener.getDataError(status, msg);
                RCLog.d(TAG, "获取问卷详情失败" + msg);
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
    public void saveQuestionnaireResult(final IRCPostListener listener, long userID,
                                        int questionnaireId, String answer) {
        BeanGetQuestionnaieDetails bean = new BeanGetQuestionnaieDetails();
        bean.setActionName("/p/health/questionnaire/" + questionnaireId + "/");
        bean.setAnswer(answer);
        if (userID > 0)
            bean.setOther_user(userID + "");
        RCRequestNetwork.NetWorkGetData(mContext, bean, RCRequestNetwork.Method.Post, new IResponseData() {
            @Override
            public void getDataSucceedListener(JSONObject data) {
                listener.getDataSuccess();
                RCLog.d(TAG, "保存问卷结果成功");
            }

            @Override
            public void getDataErrorListener(String msg, int status) {
                listener.getDataError(status, msg);
                RCLog.d(TAG, "保存问卷结果失败" + msg);
            }
        });
    }


}

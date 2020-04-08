package com.rocedar.deviceplatform.request.impl;

import android.content.Context;

import com.rocedar.base.RCLog;
import com.rocedar.base.network.IResponseData;
import com.rocedar.base.network.RequestData;
import com.rocedar.deviceplatform.dto.questionnaire.HealthHistoryQuestionListDTO;
import com.rocedar.deviceplatform.request.RCQuestionnaireRequest;
import com.rocedar.deviceplatform.request.bean.BasePlatformBean;
import com.rocedar.deviceplatform.request.bean.BeanGetQuestionnaieDetails;
import com.rocedar.deviceplatform.request.listener.RCQuestionnaireDetailsListener;
import com.rocedar.deviceplatform.request.listener.RCQuestionnaireListListener;
import com.rocedar.deviceplatform.request.listener.RCRequestSuccessListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liuyi
 * @date 2017/2/28
 * @desc 问卷相关实现类（请求后台接口）
 * @veison V3.3.30(动吖)
 */

public class RCQuestionnaireRequestImpl implements RCQuestionnaireRequest {

    private static String TAG = "RCQuestionnaire_Request";
    private static RCQuestionnaireRequestImpl ourInstance;

    public static RCQuestionnaireRequestImpl getInstance(Context context) {
        if (ourInstance == null)
            ourInstance = new RCQuestionnaireRequestImpl(context);
        return ourInstance;
    }

    private Context mContext;

    private RCQuestionnaireRequestImpl(Context context) {
        this.mContext = context;
    }

    /**
     * 问卷类型列表
     *
     * @param listener
     */
    @Override
    public void loadQuestionnaireList(final RCQuestionnaireListListener listener) {
        BasePlatformBean bean = new BasePlatformBean();
        bean.setActionName("/p/health/questionnaire/");
        RequestData.NetWorkGetData(mContext, bean, RequestData.Method.Get, new IResponseData() {
            @Override
            public void getDataSucceedListener(JSONObject data) {
                JSONArray array = data.optJSONObject("result").optJSONArray("questionnaires");
                List<HealthHistoryQuestionListDTO> mDatas = new ArrayList<>();
                HealthHistoryQuestionListDTO dto;
                for (int i = 0; i < array.length() && array.length() > 0; i++) {
                    JSONObject object = array.optJSONObject(i);
                    dto = new HealthHistoryQuestionListDTO();
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
    public void loadQuestionnaireDetails(final RCQuestionnaireDetailsListener listener, int questionnaireId, long publicsh_time) {
        BeanGetQuestionnaieDetails bean = new BeanGetQuestionnaieDetails();
        bean.setActionName("/p/health/questionnaire/" + questionnaireId + "/");
        bean.setPublicsh_time(publicsh_time+"");
        RequestData.NetWorkGetData(mContext, bean, RequestData.Method.Get, new IResponseData() {
            @Override
            public void getDataSucceedListener(JSONObject data) {
                JSONObject result = data.optJSONObject("result");
                listener.getDataSuccess(result);
                RCLog.d(TAG,"获取问卷详情成功");

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
    public void saveQuestionnaireResult(final RCRequestSuccessListener listener, int questionnaireId, String answer) {
        BeanGetQuestionnaieDetails bean = new BeanGetQuestionnaieDetails();
        bean.setActionName("/p/health/questionnaire/"+questionnaireId+"/");
        bean.setAnswer(answer);
        RequestData.NetWorkGetData(mContext, bean, RequestData.Method.Post, new IResponseData() {
            @Override
            public void getDataSucceedListener(JSONObject data) {
                listener.requestSuccess();
                RCLog.d(TAG,"保存问卷结果成功");
            }

            @Override
            public void getDataErrorListener(String msg, int status) {
                listener.requestError(status, msg);
                RCLog.d(TAG, "保存问卷结果失败" + msg);
            }
        });
    }

}

package com.rocedar.deviceplatform.questionnaire;

import com.rocedar.deviceplatform.dto.questionnaire.HealthHistoryQuestionDTO;
import com.rocedar.deviceplatform.dto.questionnaire.HealthHistoryQuestionFirstDTO;
import com.rocedar.deviceplatform.dto.questionnaire.HealthHistoryQuestionOptionsDTO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by phj on 2016/10/27.
 * <p>
 * 健康史问卷数据解析
 *
 * @version v3.2.00 增加健康史功能
 */

public class HealthHistoryData {

    public interface Type {

        static final int Radio = 1;
        static final int MultiSelect = 2;
        static final int GapFilling = 3;


    }

    public static List<HealthHistoryQuestionFirstDTO> getQuestionnaireFisrt(JSONObject result) {
        List<HealthHistoryQuestionFirstDTO> historyQuestionFisrtDTOs = new ArrayList<>();
        try {
            JSONArray array = new JSONObject(String.valueOf(result)).
                    optJSONArray("questionnaire");
            for (int i = 0; i < array.length(); i++) {
                JSONObject object1 = array.getJSONObject(i);
                HealthHistoryQuestionFirstDTO dto = new HealthHistoryQuestionFirstDTO();
                dto.setGroup_id(object1.optString("group_id"));
                dto.setGroup_name(object1.optString("group_name"));
                JSONArray array1 = object1.optJSONArray("topics");
                int temp[] = new int[array1.length()];
                for (int j = 0; j < array1.length(); j++) {
                    temp[j] = array1.getInt(j);
                }
                dto.setTopics(temp);
                historyQuestionFisrtDTOs.add(dto);
            }
        } catch (JSONException e) {
            return null;
        }
        return historyQuestionFisrtDTOs;
    }

    /**
     * "1": {
     * "topic_name": "您的父母或者兄弟姐妹是否患有明确诊断的疾病？",
     * "type_id": 1,
     * "options": [
     * {
     * "option_id": 1,
     * "option_name": "是",
     * "child_topic": "2,3"
     * },
     * {
     * "option_id": 2,
     * "option_name": "否",
     * "child_topic": ""
     * }
     * ]
     * },
     *
     * @param qId
     * @return
     */
    public static HealthHistoryQuestionDTO getAllQuestionnaire(JSONObject result,String qId) {
        HealthHistoryQuestionDTO dto = new HealthHistoryQuestionDTO();
        try {
            JSONObject object = new JSONObject(String.valueOf(result)).
                    optJSONObject("topics").optJSONObject(qId + "");
            dto.setqId(qId + "");
            dto.setTopic_name(object.optString("topic_name"));
            dto.setType_id(object.optInt("type_id"));
            List<HealthHistoryQuestionOptionsDTO> optionsList = new ArrayList<>();
            JSONArray array = object.optJSONArray("options");
            for (int i = 0; i < array.length(); i++) {
                HealthHistoryQuestionOptionsDTO options = new HealthHistoryQuestionOptionsDTO();
                JSONObject object1 = array.getJSONObject(i);
                options.setOption_id(object1.optInt("option_id"));
                options.setOption_name(object1.optString("option_name"));
                if (!object1.optString("child_topic").equals("")) {
                    options.setChild_topic(object1.optString("child_topic").split(","));
                }
                if (object1.has("option_type")) {
                    options.setOption_type(object1.optInt("option_type"));
                }
                optionsList.add(options);
            }
            dto.setAnswer_template(object.optString("answer_template"));
            dto.setOptionsList(optionsList);
        } catch (JSONException e) {
            return null;
        }
        return dto;
    }

}

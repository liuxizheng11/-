package com.rocedar.deviceplatform.request.impl;

import android.content.Context;

import com.rocedar.base.RCLog;
import com.rocedar.base.network.IResponseData;
import com.rocedar.base.network.RequestData;
import com.rocedar.deviceplatform.config.RCDeviceIndicatorID;
import com.rocedar.deviceplatform.dto.record.RCHealthDoctorRecordListDTO;
import com.rocedar.deviceplatform.dto.record.RCHealthFamilyRecordDTO;
import com.rocedar.deviceplatform.dto.record.RCHealthHomeRecordDTO;
import com.rocedar.deviceplatform.dto.record.RCHealthRecordDataDTO;
import com.rocedar.deviceplatform.dto.record.RCHealthReportPlanDTO;
import com.rocedar.deviceplatform.dto.record.RCHealthRxclusiveRecordDTO;
import com.rocedar.deviceplatform.request.RCHealthRecordRequest;
import com.rocedar.deviceplatform.request.bean.BasePlatformBean;
import com.rocedar.deviceplatform.request.bean.BeanGetFamilyRecord;
import com.rocedar.deviceplatform.request.bean.BeanGetHealthRecordData;
import com.rocedar.deviceplatform.request.listener.RCHealthDoctorRecordListListener;
import com.rocedar.deviceplatform.request.listener.RCHealthFamilyRecordListener;
import com.rocedar.deviceplatform.request.listener.RCHealthHomeRecordListener;
import com.rocedar.deviceplatform.request.listener.RCHealthRecordDataListener;
import com.rocedar.deviceplatform.request.listener.RCHealthReportPlanListener;
import com.rocedar.deviceplatform.request.listener.RCHealthRxclusiveRecordListener;
import com.rocedar.deviceplatform.request.listener.RCPostListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liuyi
 * @date 2017/3/4
 * @desc 健康档案的实现类(请求后台接口)
 * @veison V3.3.30(动吖)
 */

public class RCHealthRecordRequestImpl implements RCHealthRecordRequest {

    private static String TAG = "RCHealthRecord_Request";
    private static RCHealthRecordRequestImpl ourInstance;

    public static RCHealthRecordRequestImpl getInstance(Context context) {
        if (ourInstance == null)
            ourInstance = new RCHealthRecordRequestImpl(context);
        return ourInstance;
    }

    private Context mContext;

    private RCHealthRecordRequestImpl(Context context) {
        this.mContext = context;
    }

    /**
     * 医师报告
     */
    public static final int TYPE_REPORT = 1;
    /**
     * 健康方案
     */
    public static final int TYPE_HEALTH = 2;

    /**
     * 我的健康档案
     */
    @Override
    public void loadMyHealthRecord(final RCHealthHomeRecordListener listener) {
        BasePlatformBean bean = new BasePlatformBean();
        bean.setActionName("/p/health/");
        RequestData.NetWorkGetData(mContext, bean, RequestData.Method.Get, new IResponseData() {
            @Override
            public void getDataSucceedListener(JSONObject data) {
                JSONObject result = data.optJSONObject("result");
                RCHealthHomeRecordDTO dto = new RCHealthHomeRecordDTO();

                RCHealthHomeRecordDTO.UserDTO userDTO = new RCHealthHomeRecordDTO.UserDTO();
                JSONObject user = result.optJSONObject("user");
                userDTO.setUser_name(user.optString("user_name"));
                userDTO.setBirthday(user.optLong("birthday"));
                userDTO.setPortrait(user.optString("portrait"));
                dto.setUser(userDTO);

                RCHealthHomeRecordDTO.BmiDTO bmiDTO = new RCHealthHomeRecordDTO.BmiDTO();
                JSONObject bmi = result.optJSONObject("bmi");
                bmiDTO.setBmi(bmi.optDouble("bmi"));
                bmiDTO.setWeight(bmi.optDouble("weight"));
                bmiDTO.setHeight(bmi.optInt("height"));
                bmiDTO.setException_name(bmi.optString("exception_name"));
                dto.setBmi(bmiDTO);

                RCHealthHomeRecordDTO.PlanDTO planDTO = new RCHealthHomeRecordDTO.PlanDTO();
                JSONObject plan = result.optJSONObject("plan");
                planDTO.setReport(plan.optString("report"));
                dto.setPlan(planDTO);

                RCHealthHomeRecordDTO.RecordDoctorDTO recordDoctorDTO = new RCHealthHomeRecordDTO.RecordDoctorDTO();
                JSONObject record_doctor = result.optJSONObject("record_doctor");
                recordDoctorDTO.setRecord(record_doctor.optString("record"));
                dto.setRecord_doctor(recordDoctorDTO);

                List<RCHealthHomeRecordDTO.ConductsDTO> conductsDTOs = new ArrayList<>();
                RCHealthHomeRecordDTO.ConductsDTO conductsDTO;
                JSONArray conducts = result.optJSONArray("conducts");
                for (int i = 0; i < conducts.length() && conducts.length() > 0; i++) {
                    JSONObject object = conducts.optJSONObject(i);
                    conductsDTO = new RCHealthHomeRecordDTO.ConductsDTO();
                    conductsDTO.setConduct_id(object.optInt("id"));

                    conductsDTO.setConduct_name(object.optString("name"));
                    conductsDTO.setConduct_time(object.optLong("time"));
                    conductsDTO.setConduct_color(object.optString("color"));
                    conductsDTO.setHistory_url(object.optString("url"));
                    conductsDTO.setIndicatorId(object.optInt("indicatorId"));

                    List<RCHealthHomeRecordDTO.ConductsDTO.IndicatorsDTO> indicatorsDTOs = new ArrayList<>();
                    RCHealthHomeRecordDTO.ConductsDTO.IndicatorsDTO indicatorsDTO;
                    JSONArray indicators = object.optJSONArray("indicators");
                    for (int j = 0; j < indicators.length() && indicators.length() > 0; j++) {
                        JSONObject object1 = indicators.optJSONObject(j);
                        indicatorsDTO = new RCHealthHomeRecordDTO.ConductsDTO.IndicatorsDTO();
                        indicatorsDTO.setIndicator_name(object1.optString("name"));
                        indicatorsDTO.setIndicator_value(object1.optString("value"));
                        indicatorsDTO.setIndicator_unit(object1.optString("unit"));
                        indicatorsDTOs.add(indicatorsDTO);
                    }
                    conductsDTO.setIndicators(indicatorsDTOs);
                    conductsDTOs.add(conductsDTO);
                }
                dto.setConducts(conductsDTOs);
                dto.setLabels("");
                if (result.has("labels") && result.optJSONArray("labels").length() > 0) {
                    dto.setLabels(result.optJSONArray("labels").toString().replace("[", "").replace("]", "")
                            .replace("\"", ""));
                }
                listener.getDataSuccess(dto);
                RCLog.d(TAG, "获取我的健康档案成功");

            }

            @Override
            public void getDataErrorListener(String msg, int status) {
                listener.getDataError(status, msg);
                RCLog.d(TAG, "获取我的健康档案失败" + msg);
            }
        });
    }

    /**
     * 我的专属健康报告／医生报告
     *
     * @param record_type 类型id （医生报告--> 1，专属健康报告-->2）
     * @param record_id   报告id（专属健康报告传-1，医生报告在app进入传-1）
     */
    @Override
    public void loadMyExclusiveHealthRecord(int record_type, long record_id, long user_id, final RCHealthRxclusiveRecordListener listener) {
        BeanGetFamilyRecord bean = new BeanGetFamilyRecord();
        if (record_type == TYPE_HEALTH) {
            bean.setActionName("/p/plan/");
        } else {
            bean.setActionName("/p/plan/report/doctor/" + record_id + "/");
        }
        if (user_id > 0)
            bean.setUser_id(user_id + "");
        RequestData.NetWorkGetData(mContext, bean, RequestData.Method.Get, new IResponseData() {
            @Override
            public void getDataSucceedListener(JSONObject data) {
                JSONObject result = data.optJSONObject("result");
                RCHealthRxclusiveRecordDTO dto = new RCHealthRxclusiveRecordDTO();
                dto.setReport(result.optString("report"));
                dto.setHead_title(result.optString("head_title"));
                dto.setHead_url(result.optString("head_url"));
                dto.setStart_time(result.optLong("start_time"));
                dto.setEnd_time(result.optLong("end_time"));
                dto.setStatus(result.optInt("status"));
                dto.setNext_time(result.optString("next_time"));

                if (result.has("owner")) {
                    JSONObject owner = result.optJSONObject("owner");
                    RCHealthRxclusiveRecordDTO.Owners owners = new RCHealthRxclusiveRecordDTO.Owners();
                    if (owner.has("type_name")) {
                        owners.setType_name(owner.optString("type_name"));
                        dto.setOwner(owners);
                    }
                }
                if (result.has("tasks")) {
                    JSONArray tasks = result.optJSONArray("tasks");
                    List<RCHealthRxclusiveRecordDTO.TasksDTO> tasksDTOs = new ArrayList<>();
                    RCHealthRxclusiveRecordDTO.TasksDTO tasksDTO;
                    for (int i = 0; tasks != null && i < tasks.length() && tasks.length() > 0; i++) {
                        JSONObject task = tasks.optJSONObject(i);
                        tasksDTO = new RCHealthRxclusiveRecordDTO.TasksDTO();
                        tasksDTO.setUrl(task.optString("url"));
                        tasksDTO.setIcon(task.optString("icon"));
                        tasksDTO.setName(task.optString("name"));
                        tasksDTO.setCoin(task.optInt("coin"));
                        tasksDTO.setRecommend(task.optInt("recommend"));
                        tasksDTO.setHave(task.optInt("have"));
                        tasksDTO.setDay(task.optInt("day"));
                        tasksDTO.setTaskId(task.optInt("ti"));
                        tasksDTOs.add(tasksDTO);
                    }
                    dto.setTasks(tasksDTOs);
                }
                JSONArray suggests = result.optJSONArray("suggests");
                List<RCHealthRxclusiveRecordDTO.SuggestsDTO> suggestsDTOs = new ArrayList<>();
                RCHealthRxclusiveRecordDTO.SuggestsDTO suggestsDTO;
                for (int i = 0; i < suggests.length() && suggests.length() > 0; i++) {
                    JSONObject suggest = suggests.optJSONObject(i);
                    suggestsDTO = new RCHealthRxclusiveRecordDTO.SuggestsDTO();
                    suggestsDTO.setQuestionnaire(suggest.optString("questionnaire"));
                    suggestsDTO.setSuggest(suggest.optString("suggest"));
                    suggestsDTO.setSuggest_icon(suggest.optString("suggest_icon"));
                    suggestsDTO.setSuggest_name(suggest.optString("suggest_name"));
                    suggestsDTO.setSuggest_color(suggest.optString("suggest_color"));
                    suggestsDTO.setSuggest_image(suggest.optString("suggest_image"));
                    suggestsDTOs.add(suggestsDTO);
                }
                dto.setSuggests(suggestsDTOs);

                listener.getDataSuccess(dto);
                RCLog.d(TAG, "获取我的专属健康报告成功");

            }

            @Override
            public void getDataErrorListener(String msg, int status) {
                listener.getDataError(status, msg);
                RCLog.d(TAG, "获取我的专属健康报告失败" + msg);
            }
        });
    }

    /**
     * 医生报告列表
     */
    @Override
    public void loadDoctorRecordList(final RCHealthDoctorRecordListListener listener, long user_id) {
        BeanGetFamilyRecord bean = new BeanGetFamilyRecord();
        bean.setActionName("/p/plan/report/doctor/");
        bean.setUser_id(user_id + "");
        RequestData.NetWorkGetData(mContext, bean, RequestData.Method.Get, new IResponseData() {
            @Override
            public void getDataSucceedListener(JSONObject data) {
                JSONObject result = data.optJSONObject("result");
                JSONArray reports = result.optJSONArray("reports");
                List<RCHealthDoctorRecordListDTO> listDTOs = new ArrayList<>();
                RCHealthDoctorRecordListDTO dto;
                for (int i = 0; i < reports.length() && reports.length() > 0; i++) {
                    JSONObject object = reports.optJSONObject(i);
                    dto = new RCHealthDoctorRecordListDTO();
                    dto.setReport_name(object.optString("report_name"));
                    dto.setStart_time(object.optLong("start_time"));
                    dto.setEnd_time(object.optLong("end_time"));
                    dto.setDetail_url(object.optString("detail_url"));
                    listDTOs.add(dto);
                }

                listener.getDataSuccess(listDTOs);
                RCLog.d(TAG, "获取医生报告列表成功");

            }

            @Override
            public void getDataErrorListener(String msg, int status) {
                listener.getDataError(status, msg);
                RCLog.d(TAG, "获取医生报告列表失败" + msg);
            }
        });
    }

    /**
     * 家人档案
     *
     * @param user_id  家人用户ID
     * @param listener
     */
    @Override
    public void loadFamilyHealthRecord(long user_id, final RCHealthFamilyRecordListener listener) {
        BeanGetFamilyRecord bean = new BeanGetFamilyRecord();
        bean.setActionName("/p/health/family/");
        bean.setUser_id(user_id + "");
        RequestData.NetWorkGetData(mContext, bean, RequestData.Method.Get, new IResponseData() {
            @Override
            public void getDataSucceedListener(JSONObject data) {
                JSONObject result = data.optJSONObject("result");

                JSONArray indicators = result.optJSONArray("indicators");
                List<RCHealthFamilyRecordDTO> listDTOs = new ArrayList<>();
                RCHealthFamilyRecordDTO dto;
                for (int i = 0; i < indicators.length() && indicators.length() > 0; i++) {
                    JSONObject object = indicators.optJSONObject(i);
                    dto = new RCHealthFamilyRecordDTO();
                    dto.setId(object.optInt("id"));
                    dto.setName(object.optString("name"));
                    dto.setValue(object.optString("value"));
                    dto.setUnit(object.optString("unit"));
                    dto.setDevice_remind(object.optString("device_remind"));
                    dto.setDevice_title(object.optString("device_title"));
                    dto.setDevice_num(object.optInt("device_num"));
                    dto.setTime(object.optLong("time"));
                    dto.setUser_id(object.optLong("user_id"));
                    listDTOs.add(dto);
                }
                listener.getDataSuccess(listDTOs);
                RCLog.d(TAG, "获取家人档案成功");

            }

            @Override
            public void getDataErrorListener(String msg, int status) {
                listener.getDataError(status, msg);
                RCLog.d(TAG, "获取家人档案失败" + msg);
            }
        });
    }

    /***
     * 5.1 体检报告列表查询
     * @param pn
     * @param listener
     */
    @Override
    public void getReportPlanData(String pn, final RCHealthReportPlanListener listener) {
        BeanGetHealthRecordData mBean = new BeanGetHealthRecordData();
        mBean.setActionName("/p/plan/report/all/");
        mBean.setPn(pn);
        RequestData.NetWorkGetData(mContext, mBean, RequestData.Method.Get,
                new IResponseData() {
                    @Override
                    public void getDataSucceedListener(JSONObject data) {
                        JSONObject mjo = data.optJSONObject("result");
                        JSONArray mja = mjo.optJSONArray("data");
                        List<RCHealthReportPlanDTO> dtoList = new ArrayList<>();
                        for (int i = 0; mja != null && i < mja.length(); i++) {
                            JSONObject data_mjo = mja.optJSONObject(i);
                            RCHealthReportPlanDTO mDTO = new RCHealthReportPlanDTO();
                            mDTO.setTitle(data_mjo.optString("title"));
                            mDTO.setHtml(data_mjo.optString("html"));
                            mDTO.setReport_id(data_mjo.optInt("report_id"));
                            mDTO.setUser_id(data_mjo.optLong("user_id"));
                            mDTO.setType_id(data_mjo.optInt("type_id"));
                            mDTO.setReport_time(data_mjo.optLong("report_time"));
                            mDTO.setDevice_id(data_mjo.optInt("device_id"));
                            mDTO.setStatus(data_mjo.optInt("status"));
                            dtoList.add(mDTO);
                        }
                        listener.getDataSuccess(dtoList);
                    }

                    @Override
                    public void getDataErrorListener(String msg, int status) {
                        listener.getDataError(status, msg);
                    }
                });
    }

    /**
     * 5.3用户上传体检报告
     *
     * @param title      体检报告标题
     * @param imgUrl     上传图片地址，多个以;分开
     * @param remarks    备注
     * @param reportDate 体检报告时间
     * @param listener
     */
    @Override
    public void postReportPlanData(String title, String imgUrl, String remarks, String reportDate, final RCPostListener listener) {
        BeanGetHealthRecordData mBean = new BeanGetHealthRecordData();
        mBean.setActionName("/p/plan/report/upload/");
        mBean.setTitle(title);
        mBean.setImg_url(imgUrl);
        mBean.setRemarks(remarks);
        mBean.setReport_date(reportDate);
        RequestData.NetWorkGetData(mContext, mBean, RequestData.Method.Post,
                new IResponseData() {
                    @Override
                    public void getDataSucceedListener(JSONObject data) {
                        listener.getDataSuccess();
                    }

                    @Override
                    public void getDataErrorListener(String msg, int status) {

                    }
                });
    }

    /**
     * 档案接口查询
     *
     * @param listener
     */
    @Override
    public void getHealthRecordData(final RCHealthRecordDataListener listener) {
        BasePlatformBean mBean = new BasePlatformBean();
        mBean.setActionName("/p/health/user/");
        RequestData.NetWorkGetData(mContext, mBean, RequestData.Method.Get,
                new IResponseData() {
                    @Override
                    public void getDataSucceedListener(JSONObject data) {
                        JSONObject result = data.optJSONObject("result");
                        RCHealthRecordDataDTO dto = new RCHealthRecordDataDTO();

                        /**个人资料*/
                        RCHealthRecordDataDTO.UserDTO userDTO = new RCHealthRecordDataDTO.UserDTO();
                        JSONObject user = result.optJSONObject("user");
                        userDTO.setUser_name(user.optString("user_name"));
                        userDTO.setBirthday(user.optLong("birthday"));
                        userDTO.setPortrait(user.optString("portrait"));
                        dto.setUser(userDTO);
                        /**bmi*/
                        RCHealthRecordDataDTO.BmiDTO bmiDTO = new RCHealthRecordDataDTO.BmiDTO();
                        JSONObject bmi = result.optJSONObject("bmi");
                        bmiDTO.setBmi(bmi.optDouble("bmi"));
                        bmiDTO.setWeight(bmi.optDouble("weight"));
                        bmiDTO.setHeight(bmi.optInt("height"));
                        bmiDTO.setException_name(bmi.optString("exception_name"));
                        dto.setBmi(bmiDTO);
                        /**健康计划*/
                        RCHealthRecordDataDTO.PlanDTO planDTO = new RCHealthRecordDataDTO.PlanDTO();
                        JSONObject plan = result.optJSONObject("plan");
                        planDTO.setReport(plan.optString("report"));
                        dto.setPlan(planDTO);
                        /**健康医生*/
                        RCHealthRecordDataDTO.RecordDoctorDTO recordDoctorDTO = new RCHealthRecordDataDTO.RecordDoctorDTO();
                        JSONObject record_doctor = result.optJSONObject("record_doctor");
                        recordDoctorDTO.setRecord(record_doctor.optString("record"));
                        dto.setRecord_doctor(recordDoctorDTO);
                        /**病史*/
                        List<RCHealthRecordDataDTO.LabelsDTO> lableList = new ArrayList<>();
                        JSONArray labels_mja = result.optJSONArray("labels");
                        for (int i = 0; i < labels_mja.length(); i++) {
                            RCHealthRecordDataDTO.LabelsDTO labelsDTO = new RCHealthRecordDataDTO.LabelsDTO();
                            JSONObject mjo = labels_mja.optJSONObject(i);
                            labelsDTO.setLabel_id(mjo.optInt("label_id"));
                            labelsDTO.setLabel_name(mjo.optString("label_name"));
                            labelsDTO.setLabel_type_id(mjo.optInt("label_type_id"));
                            lableList.add(labelsDTO);
                        }
                        dto.setLabels(lableList);
                        /**指标数据*/
                        JSONArray data_mja = result.optJSONArray("indicators");
                        JSONObject rule_mjo = result.optJSONObject("rule");
                        List<RCHealthRecordDataDTO.SignsDataDTO> signsList = new ArrayList<>();
                        for (int i = 0; i < data_mja.length(); i++) {
                            JSONObject data_mjo = data_mja.optJSONObject(i);
                            RCHealthRecordDataDTO.SignsDataDTO mDTO = new RCHealthRecordDataDTO.SignsDataDTO();
                            mDTO.setIndicator_id(data_mjo.optInt("indicator_id"));
                            mDTO.setValue(data_mjo.optString("value"));
                            mDTO.setName(data_mjo.optString("name"));
                            mDTO.setUnit(data_mjo.optString("unit"));
                            mDTO.setSub_value(data_mjo.optString("sub_value"));
                            JSONArray rule_mja = rule_mjo.optJSONArray(data_mjo.optString("indicator_id") + ":value");
                            List<RCHealthRecordDataDTO.SignsValueDTO> valueList = new ArrayList<>();
                            List<RCHealthRecordDataDTO.SignsValueDTO> subValueList = new ArrayList<>();
                            for (int j = 0; j < rule_mja.length(); j++) {
                                JSONObject rule_data_mjo = rule_mja.optJSONObject(j);
                                RCHealthRecordDataDTO.SignsValueDTO valueDTO = new RCHealthRecordDataDTO.SignsValueDTO();
                                valueDTO.setUnit(rule_data_mjo.optString("unit"));
                                valueDTO.setException_name(rule_data_mjo.optString("standard_name"));
                                valueDTO.setMax_value((float) rule_data_mjo.optDouble("max_value"));
                                valueDTO.setMin_value((float) rule_data_mjo.optDouble("min_value"));
                                valueList.add(valueDTO);
                            }
                            /**血压 附属值*/
                            if (data_mjo.optInt("indicator_id") == RCDeviceIndicatorID.Blood_Pressure) {

                                JSONArray rule_sub_mja = rule_mjo.optJSONArray(data_mjo.optString("indicator_id") + ":sub_value");
                                for (int j = 0; rule_sub_mja != null && j < rule_sub_mja.length(); j++) {
                                    JSONObject sub_data_mjo = rule_sub_mja.optJSONObject(j);
                                    RCHealthRecordDataDTO.SignsValueDTO valueDTO = new RCHealthRecordDataDTO.SignsValueDTO();
                                    valueDTO.setUnit(sub_data_mjo.optString("unit"));
                                    valueDTO.setException_name(sub_data_mjo.optString("standard_name"));
                                    valueDTO.setMax_value(sub_data_mjo.optInt("max_value"));
                                    valueDTO.setMin_value(sub_data_mjo.optInt("min_value"));
                                    subValueList.add(valueDTO);
                                }
                            }
                            if (subValueList.size() > 0) {
                                mDTO.setSubList(subValueList);
                            }
                            mDTO.setmList(valueList);
                            signsList.add(mDTO);
                        }
                        dto.setmSignsList(signsList);

                        /**行为数据*/
                        List<RCHealthRecordDataDTO.ConductsDTO> conductsDTOs = new ArrayList<>();
                        RCHealthRecordDataDTO.ConductsDTO conductsDTO;
                        JSONArray conducts = result.optJSONArray("conducts");
                        for (int i = 0; i < conducts.length() && conducts.length() > 0; i++) {
                            JSONObject object = conducts.optJSONObject(i);
                            conductsDTO = new RCHealthRecordDataDTO.ConductsDTO();
                            conductsDTO.setId(object.optInt("id"));
                            conductsDTO.setName(object.optString("name"));
                            conductsDTO.setTime(object.optLong("time"));
                            conductsDTO.setColor(object.optString("color"));
                            conductsDTO.setUrl(object.optString("url"));
                            conductsDTO.setIndicatorId(object.optInt("indicatorId"));

                            List<RCHealthRecordDataDTO.ConductsDTO.IndicatorsDTO> indicatorsDTOs = new ArrayList<>();
                            RCHealthRecordDataDTO.ConductsDTO.IndicatorsDTO indicatorsDTO;
                            JSONArray indicators = object.optJSONArray("indicators");
                            for (int j = 0; j < indicators.length() && indicators.length() > 0; j++) {
                                JSONObject object1 = indicators.optJSONObject(j);
                                indicatorsDTO = new RCHealthRecordDataDTO.ConductsDTO.IndicatorsDTO();
                                indicatorsDTO.setName(object1.optString("name"));
                                indicatorsDTO.setIndicator_id(object1.optInt("indicator_id"));
                                indicatorsDTO.setValue(object1.optString("value"));
                                indicatorsDTO.setSub_value(object1.optString("sub_value"));
                                indicatorsDTO.setUnit(object1.optString("unit"));
                                indicatorsDTO.setTime(object1.optLong("time"));
                                indicatorsDTO.setException_level(object1.optInt("exception_level"));
                                indicatorsDTO.setSub_exception_level(object1.optInt("sub_exception_level"));

                                indicatorsDTOs.add(indicatorsDTO);
                            }
                            conductsDTO.setIndicators(indicatorsDTOs);
                            conductsDTOs.add(conductsDTO);
                        }
                        dto.setConducts(conductsDTOs);

                        listener.getDataSuccess(dto);
                        RCLog.d(TAG, "获取我的健康档案成功");
                    }

                    @Override
                    public void getDataErrorListener(String msg, int status) {

                    }
                });

    }

}

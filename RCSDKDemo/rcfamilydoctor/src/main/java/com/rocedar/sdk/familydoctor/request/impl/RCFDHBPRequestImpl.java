package com.rocedar.sdk.familydoctor.request.impl;

import android.app.Activity;

import com.google.gson.Gson;
import com.rocedar.lib.base.network.IRCPostListener;
import com.rocedar.lib.base.network.IResponseData;
import com.rocedar.lib.base.network.RCBean;
import com.rocedar.lib.base.network.RCRequestNetwork;
import com.rocedar.sdk.familydoctor.dto.RCFDPatientListDTO;
import com.rocedar.sdk.familydoctor.dto.hdp.RCHBPDoctorDTO;
import com.rocedar.sdk.familydoctor.dto.hdp.RCHBPOrganizationListDTO;
import com.rocedar.sdk.familydoctor.dto.hdp.RCHBPRecordListDTO;
import com.rocedar.sdk.familydoctor.dto.hdp.RCHBPVideoInstituteDTO;
import com.rocedar.sdk.familydoctor.request.IRCFDHBPRequest;
import com.rocedar.sdk.familydoctor.request.bean.BeanFamilyDoctor;
import com.rocedar.sdk.familydoctor.request.bean.BeanGetQuestionId;
import com.rocedar.sdk.familydoctor.request.bean.BeanPostPatient;
import com.rocedar.sdk.familydoctor.request.bean.BeanPostSaveOthersInfo;
import com.rocedar.sdk.familydoctor.request.listener.RCFDPatientListListener;
import com.rocedar.sdk.familydoctor.request.listener.hdp.RCHBPGetDoctorDetailsListener;
import com.rocedar.sdk.familydoctor.request.listener.hdp.RCHBPGetDoctorListener;
import com.rocedar.sdk.familydoctor.request.listener.hdp.RCHBPGetNewSListener;
import com.rocedar.sdk.familydoctor.request.listener.hdp.RCHBPGetOrganizationListListener;
import com.rocedar.sdk.familydoctor.request.listener.hdp.RCHBPGetRecordListListener;
import com.rocedar.sdk.familydoctor.request.listener.hdp.RCHBPGetVideoInstituListener;
import com.rocedar.sdk.familydoctor.request.listener.hdp.RCHBPSaveConsultRecordListener;
import com.rocedar.sdk.familydoctor.request.listener.hdp.RCHBPSaveOthersInfoListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称：瑰柏SDK-基础库
 * <p>
 * 作者：phj
 * 日期：2018/5/31 下午2:48
 * 版本：V1.0.00
 * 描述：瑰柏SDK-
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCFDHBPRequestImpl implements IRCFDHBPRequest {

    private Activity mContext;

    public RCFDHBPRequestImpl(Activity mContext) {
        this.mContext = mContext;
    }


    @Override
    public void getVideoInstituteList(String pn, final RCHBPGetVideoInstituListener listener) {
        BeanFamilyDoctor bean = new BeanFamilyDoctor();
        bean.setActionName("/p/server/sti/cathedra/");
        bean.setPn(pn);
        RCRequestNetwork.NetWorkGetData(mContext, bean, RCRequestNetwork.Method.Get,
                new IResponseData() {
                    @Override
                    public void getDataSucceedListener(JSONObject data) {
                        JSONArray result = data.optJSONArray("result");
                        List<RCHBPVideoInstituteDTO> list = new ArrayList<>();
                        for (int i = 0; i < result.length(); i++) {
                            Object obj = result.opt(i);

                            list.add(new Gson().fromJson(obj.toString(), RCHBPVideoInstituteDTO.class));
                        }

                        listener.getDataSuccess(list);

                    }

                    @Override
                    public void getDataErrorListener(String msg, int status) {
                        listener.getDataError(status, msg);
                    }
                });
    }

    @Override
    public void getHBPDoctor(String pn, final RCHBPGetDoctorListener listener) {
        BeanFamilyDoctor bean = new BeanFamilyDoctor();
        bean.setActionName("/p/server/sti/doctor/");
        bean.setPn(pn);
        RCRequestNetwork.NetWorkGetData(mContext, bean, RCRequestNetwork.Method.Get,
                new IResponseData() {
                    @Override
                    public void getDataSucceedListener(JSONObject data) {
                        JSONArray result = data.optJSONArray("result");
                        List<RCHBPDoctorDTO> list = new ArrayList<>();
                        for (int i = 0; i < result.length(); i++) {
                            Object obj = result.opt(i);

                            list.add(new Gson().fromJson(obj.toString(), RCHBPDoctorDTO.class));
                        }

                        listener.getDataSuccess(list);

                    }

                    @Override
                    public void getDataErrorListener(String msg, int status) {
                        listener.getDataError(status, msg);
                    }
                });
    }


    @Override
    public void getIsNews(int org_id, final RCHBPGetNewSListener listener) {
        BeanFamilyDoctor bean = new BeanFamilyDoctor();
        bean.setActionName("/p/server/sti/");
        bean.setOrg_id(org_id + "");
        RCRequestNetwork.NetWorkGetData(mContext, bean, RCRequestNetwork.Method.Get,
                new IResponseData() {
                    @Override
                    public void getDataSucceedListener(JSONObject data) {
                        int news = data.optJSONObject("result").optInt("new");
                        listener.getDataSuccess(news);

                    }

                    @Override
                    public void getDataErrorListener(String msg, int status) {
                        listener.getDataError(status, msg);
                    }
                });
    }


    @Override
    public void getHBPDoctorDetails(int doctorId, final RCHBPGetDoctorDetailsListener listener) {
        RCBean bean = new RCBean();
        bean.setActionName("/p/server/sti/doctor/" + doctorId + "/");

        RCRequestNetwork.NetWorkGetData(mContext, bean, RCRequestNetwork.Method.Get,
                new IResponseData() {
                    @Override
                    public void getDataSucceedListener(JSONObject data) {
                        JSONObject result = data.optJSONObject("result");

                        RCHBPDoctorDTO dto = new Gson().fromJson(result.toString(), RCHBPDoctorDTO.class);

                        listener.getDataSuccess(dto);

                    }

                    @Override
                    public void getDataErrorListener(String msg, int status) {
                        listener.getDataError(status, msg);
                    }
                });
    }


    /*查询/保存/消息列表，结束咨询*/
    private static final String HBP_MSG_LIST = "/p/server/sti/advice/record/";
    /*查询/保存病人列表*/
    private static final String HBP_ADCVICE = "/p/server/sti/advice/";

    @Override
    public void getHBPRecordList(String pn, int org_id, final RCHBPGetRecordListListener listener) {
        BeanFamilyDoctor bean = new BeanFamilyDoctor();
        bean.setActionName(HBP_MSG_LIST);
        bean.setPn(pn);
        bean.setOrg_id(org_id + "");
        RCRequestNetwork.NetWorkGetData(mContext, bean, RCRequestNetwork.Method.Get,
                new IResponseData() {
                    @Override
                    public void getDataSucceedListener(JSONObject data) {
                        JSONArray result = data.optJSONArray("result");
                        List<RCHBPRecordListDTO> dtoList = new ArrayList<>();
                        for (int i = 0; result != null && i < result.length(); i++) {
                            JSONObject object = result.optJSONObject(i);
                            RCHBPRecordListDTO dto = new RCHBPRecordListDTO();
                            dto.setChoose(object.optLong("choose"));
                            dto.setIcon(object.optString("icon"));
                            dto.setImg_url(object.optString("img_url"));
                            dto.setOrg_id(object.optInt("org_id"));
                            dto.setQuestion_id(object.optInt("question_id"));
                            dto.setRecord(object.optString("record"));
                            dto.setSpeaker(object.optInt("speaker"));
                            dto.setStatus(object.optInt("status"));
                            dto.setType(object.optInt("type"));
                            dto.setUpdate_time(object.optLong("update_time"));
                            ArrayList<RCHBPRecordListDTO.SicksDTO> list = new ArrayList<>();
                            JSONArray array = object.optJSONArray("sicks");
                            for (int j = 0; array != null && j < array.length(); j++) {
                                JSONObject jsonObject = array.optJSONObject(j);
                                RCHBPRecordListDTO.SicksDTO sicksDTO = new RCHBPRecordListDTO.SicksDTO();
                                sicksDTO.setChoose(jsonObject.optInt("choose"));
                                sicksDTO.setQuestionnaire_id(jsonObject.optInt("questionnaire_id"));
                                sicksDTO.setSick_id(jsonObject.optLong("sick_id"));
                                sicksDTO.setSick_name(jsonObject.optString("sick_name"));
                                list.add(sicksDTO);
                            }
                            dto.setSicks(list);
                            dtoList.add(dto);
                        }
                        listener.getDataSuccess(dtoList);
                    }

                    @Override
                    public void getDataErrorListener(String msg, int status) {
                        listener.getDataError(status, msg);
                    }
                });
    }

    @Override
    public void saveOthersInfo(String sick_name, String sick_sex, String sick_birthday, String sick_height,
                               String sick_weight, final RCHBPSaveOthersInfoListener listener) {
        BeanPostSaveOthersInfo bean = new BeanPostSaveOthersInfo();
        bean.setActionName(HBP_ADCVICE);
        bean.setSick_name(sick_name);
        bean.setSick_sex(sick_sex);
        bean.setSick_birthday(sick_birthday);
        bean.setSick_height(sick_height);
        bean.setSick_weight(sick_weight);
        RCRequestNetwork.NetWorkGetData(mContext, bean, RCRequestNetwork.Method.Post,
                new IResponseData() {
                    @Override
                    public void getDataSucceedListener(JSONObject data) {
                        listener.getDataSuccess(data.optLong("result"));
                    }

                    @Override
                    public void getDataErrorListener(String msg, int status) {
                        listener.getDataError(status, msg);
                    }
                });
    }

    @Override
    public void saveConsultRecord(String question_id, String sick_id, String type, String speaker, String record, String img_url, int org_id, final RCHBPSaveConsultRecordListener listener) {
        BeanGetQuestionId bean = new BeanGetQuestionId();
        bean.setActionName(HBP_MSG_LIST);
        bean.setQuestion_id(question_id);
        bean.setSick_id(sick_id);
        bean.setType(type);
        bean.setSpeaker(speaker);
        bean.setRecord(record);
        bean.setImg_url(img_url);
        bean.setOrg_id(org_id + "");
        RCRequestNetwork.NetWorkGetData(mContext, bean, RCRequestNetwork.Method.Post,
                new IResponseData() {
                    @Override
                    public void getDataSucceedListener(JSONObject data) {
                        JSONObject result = data.optJSONObject("result");
                        listener.getDataSuccess(result.optInt("question_id"), result.optString("auto_record"));
                    }

                    @Override
                    public void getDataErrorListener(String msg, int status) {
                        listener.getDataError(status, msg);
                    }
                });
    }

    @Override
    public void finishConsultRecord(String question_id, int org_id, final IRCPostListener listener) {
        BeanGetQuestionId bean = new BeanGetQuestionId();
        bean.setActionName(HBP_MSG_LIST);
        bean.setQuestion_id(question_id);
        bean.setOrg_id(org_id + "");
        RCRequestNetwork.NetWorkGetData(mContext, bean, RCRequestNetwork.Method.Put,
                new IResponseData() {
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

    @Override
    public void getOrganizationList(final RCHBPGetOrganizationListListener listener) {
        RCBean bean = new RCBean();
        bean.setActionName("/p/server/org/");
        RCRequestNetwork.NetWorkGetData(mContext, bean, RCRequestNetwork.Method.Get,
                new IResponseData() {
                    @Override
                    public void getDataSucceedListener(JSONObject data) {
                        JSONArray org = data.optJSONObject("result").optJSONArray("org");
                        List<RCHBPOrganizationListDTO> list = new ArrayList<>();
                        for (int i = 0; i < org.length(); i++) {
                            RCHBPOrganizationListDTO dto = new RCHBPOrganizationListDTO();
                            JSONObject object = org.optJSONObject(i);
                            dto.setOrg_id(object.optInt("org_id"));
                            dto.setOrg_icon(object.optString("org_icon"));
                            dto.setOrg_name(object.optString("org_name"));
                            dto.setAndroid_url(object.optString("android_url"));
                            list.add(dto);
                        }
                        listener.getDataSuccess(list);
                    }

                    @Override
                    public void getDataErrorListener(String msg, int status) {
                        listener.getDataError(status, msg);
                    }
                });
    }

    private static String Patient = "/p/patient/";

    @Override
    public void getPatientList(final RCFDPatientListListener listener) {
        RCBean bean = new RCBean();
        bean.setActionName(Patient);
        RCRequestNetwork.NetWorkGetData(mContext, bean, RCRequestNetwork.Method.Get,
                new IResponseData() {
                    @Override
                    public void getDataSucceedListener(JSONObject data) {
                        JSONArray org = data.optJSONArray("result");
                        List<RCFDPatientListDTO> list = new ArrayList<>();
                        for (int i = 0; i < org.length(); i++) {
                            list.add(new Gson().fromJson(org.optJSONObject(i).toString(),
                                    RCFDPatientListDTO.class));
                        }
                        listener.getDataSuccess(list);
                    }

                    @Override
                    public void getDataErrorListener(String msg, int status) {
                        listener.getDataError(status, msg);
                    }
                });
    }

    @Override
    public void savePatientInfo(String patient_id, String name, String sex, String birthday,
                                String height, String weight, final IRCPostListener listener) {
        BeanPostPatient bean = new BeanPostPatient();
        bean.setActionName(Patient);
        bean.setName(name);
        bean.setSex(sex);
        bean.setPatient_id(birthday);
        bean.setHeight(height);
        bean.setWeight(weight);
        RCRequestNetwork.NetWorkGetData(mContext, bean, RCRequestNetwork.Method.Post,
                new IResponseData() {
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


}

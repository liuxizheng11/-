package com.rocedar.sdk.familydoctor.request.impl;

import android.content.Context;

import com.rocedar.lib.base.network.IRCPostListener;
import com.rocedar.lib.base.network.IResponseData;
import com.rocedar.lib.base.network.RCRequestNetwork;
import com.rocedar.sdk.familydoctor.dto.RCFDRecordDetailDTO;
import com.rocedar.sdk.familydoctor.dto.RCFDRecordListDTO;
import com.rocedar.sdk.familydoctor.dto.RCFDServiceStatusInfoDTO;
import com.rocedar.sdk.familydoctor.dto.RCFDSpecificCommentsDTO;
import com.rocedar.sdk.familydoctor.request.IRCFDRecordRequest;
import com.rocedar.sdk.familydoctor.request.bean.BeanFamilyDoctor;
import com.rocedar.sdk.familydoctor.request.bean.BeanGetServiceStatus;
import com.rocedar.sdk.familydoctor.request.bean.BeanPostBindPhoneNumber;
import com.rocedar.sdk.familydoctor.request.listener.fd.RCFDGetRecordDetailListener;
import com.rocedar.sdk.familydoctor.request.listener.fd.RCFDGetRecordListListener;
import com.rocedar.sdk.familydoctor.request.listener.fd.RCFDGetServerStatusListener;
import com.rocedar.sdk.familydoctor.request.listener.fd.RCFDGetSpecificCommnetsListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称：瑰柏SDK-基础库
 * <p>
 * 作者：phj
 * 日期：2018/5/23 下午3:24
 * 版本：V1.0.00
 * 描述：瑰柏SDK-
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCFDRecordRequestImpl implements IRCFDRecordRequest {

    private Context mContext;

    public RCFDRecordRequestImpl(Context mContext) {
        this.mContext = mContext;
    }


    @Override
    public void getRecordList(int pn, final RCFDGetRecordListListener listener) {
        BeanFamilyDoctor bean = new BeanFamilyDoctor();
        bean.setActionName("/p/server/1308002/record/");
        bean.setPn(pn + "");
        RCRequestNetwork.NetWorkGetData(mContext, bean, RCRequestNetwork.Method.Get,
                new IResponseData() {
                    @Override
                    public void getDataSucceedListener(JSONObject data) {
                        List<RCFDRecordListDTO> dtoList = new ArrayList<>();
                        JSONArray jsonArray = data.optJSONObject("result").optJSONArray("records");
                        for (int i = 0; jsonArray != null && i < jsonArray.length(); i++) {
                            JSONObject obj = jsonArray.optJSONObject(i);
                            RCFDRecordListDTO dto = new RCFDRecordListDTO();
                            dto.setTitle_name(obj.optString("title_name"));
                            dto.setPortrait(obj.optString("portrait"));
                            dto.setDoctor_id(obj.optString("doctor_id"));
                            dto.setDoctor_name(obj.optString("doctor_name"));
                            dto.setEnd_time(obj.optString("end_time"));
                            dto.setRecord_id(obj.optString("record_id"));
                            dto.setStart_time(obj.optString("start_time"));
                            dto.setSymptom(obj.optString("symptom"));
                            dto.setTotal_time(obj.optString("total_time"));
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
    public void getRecordDetail(String recordId, final RCFDGetRecordDetailListener listener) {
        BeanFamilyDoctor bean = new BeanFamilyDoctor();
        bean.setActionName("/p/server/1308002/record/" + recordId + "/");
        RCRequestNetwork.NetWorkGetData(mContext, bean, RCRequestNetwork.Method.Get,
                new IResponseData() {
                    @Override
                    public void getDataSucceedListener(JSONObject data) {
                        JSONObject obj = data.optJSONObject("result");
                        RCFDRecordDetailDTO dto = new RCFDRecordDetailDTO();
                        dto.setStart_time(obj.optString("start_time"));
                        dto.setSymptom(obj.optString("symptom"));
                        dto.setDoctor_name(obj.optString("doctor_name"));
                        dto.setDoctor_id(obj.optString("doctor_id"));
                        dto.setAudio_url(obj.optString("audio_url"));
                        dto.setDepartment_name(obj.optString("department_name"));
                        dto.setSuggest(obj.optString("suggest"));
                        dto.setHospital_name(obj.optString("hospital_name"));
                        dto.setPortrait(obj.optString("portrait"));
                        dto.setTitle_name(obj.optString("title_name"));
                        dto.setTotal_time(obj.optString("total_time"));
                        if (obj.optInt("focus") == 1) {
                            dto.setFocus(true);
                        } else {
                            dto.setFocus(false);
                        }
                        dto.setComment(obj.optString("comment"));
                        dto.setComment_time(obj.optLong("comment_time"));
                        dto.setGrade(obj.optString("grade"));
                        dto.setUser_name(obj.optString("user_name"));
                        listener.getDataSuccess(dto);
                    }

                    @Override
                    public void getDataErrorListener(String msg, int status) {
                        listener.getDataError(status, msg);
                    }
                });
    }

    @Override
    public void saveRecord(String recordId, final IRCPostListener postListener) {
        BeanFamilyDoctor bean = new BeanFamilyDoctor();
        bean.setActionName("/p/server/1308002/record/" + recordId + "/");
        RCRequestNetwork.NetWorkGetData(mContext, bean, RCRequestNetwork.Method.Post,
                new IResponseData() {
                    @Override
                    public void getDataSucceedListener(JSONObject data) {
                        postListener.getDataSuccess();
                    }

                    @Override
                    public void getDataErrorListener(String msg, int status) {
                        postListener.getDataError(status, msg);
                    }
                });
    }

    @Override
    public void getFDSpecificDoctor(final RCFDGetRecordDetailListener listener) {
        BeanFamilyDoctor bean = new BeanFamilyDoctor();
        bean.setActionName("/p/server/1308002/doctor/specific/");
        RCRequestNetwork.NetWorkGetData(mContext, bean, RCRequestNetwork.Method.Get,
                new IResponseData() {
                    @Override
                    public void getDataSucceedListener(JSONObject data) {
                        JSONObject obj = data.optJSONObject("result").optJSONObject("doctors");
                        RCFDRecordDetailDTO dto = new RCFDRecordDetailDTO();
                        dto.setDoctor_id(obj.optString("doctor_id"));
                        dto.setDoctor_name(obj.optString("doctor_name"));
                        dto.setDepartment_name(obj.optString("department_name"));
                        dto.setPortrait(obj.optString("portrait"));
                        dto.setTitle_name(obj.optString("title_name"));
                        listener.getDataSuccess(dto);
                    }

                    @Override
                    public void getDataErrorListener(String msg, int status) {
                        listener.getDataError(status, msg);
                    }
                });
    }

    @Override
    public void getFDSpecificComments(int pn, final RCFDGetSpecificCommnetsListener listener) {
        BeanFamilyDoctor bean = new BeanFamilyDoctor();
        bean.setActionName("/p/server/1308002/doctor/specific/comments/");
        bean.setPn(pn + "");
        RCRequestNetwork.NetWorkGetData(mContext, bean, RCRequestNetwork.Method.Get,
                new IResponseData() {
                    @Override
                    public void getDataSucceedListener(JSONObject data) {
                        List<RCFDSpecificCommentsDTO> dtoList = new ArrayList<>();
                        JSONArray jsonArray = data.optJSONObject("result").optJSONArray("comments");
                        for (int i = 0; jsonArray != null && i < jsonArray.length(); i++) {
                            JSONObject obj = jsonArray.optJSONObject(i);
                            RCFDSpecificCommentsDTO dto = new RCFDSpecificCommentsDTO();
                            dto.setComment(obj.optString("comment"));
                            dto.setDoctor_title(obj.optString("doctor_title"));
                            dto.setUser_phone(obj.optString("user_phone"));
                            dto.setDoctor_name(obj.optString("doctor_name"));
                            dto.setHospital_name(obj.optString("hospital_name"));
                            dto.setGrade(obj.optString("grade"));
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

    //家庭医生-查询服务状态接口
    private final static String QueryServiceStatus = "/p/server/1308002/user/";

    @Override
    public void getDoctorServerStatus(String serviceId, String device_no, final RCFDGetServerStatusListener listener) {
        BeanGetServiceStatus bean = new BeanGetServiceStatus();
        bean.setActionName(String.format(QueryServiceStatus, serviceId));
        bean.setDevice_no(device_no);
        RCRequestNetwork.NetWorkGetData(mContext, bean, RCRequestNetwork.Method.Get,
                new IResponseData() {
                    @Override
                    public void getDataSucceedListener(JSONObject data) {
                        JSONObject mja = data.optJSONObject("result");
                        RCFDServiceStatusInfoDTO dto = new RCFDServiceStatusInfoDTO();
                        dto.setStartTime(mja.optString("start_time"));
                        dto.setEndTime(mja.optString("end_time"));
                        dto.setValid(mja.optInt("status") >= 0);
                        try {
                            dto.setPhoneNo(mja.optJSONObject("user").optString("phone"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        listener.getDataSuccess(dto);
                    }

                    @Override
                    public void getDataErrorListener(String msg, int status) {
                        listener.getDataError(status, msg);
                    }
                });
    }


    private final static String BindPhoneNumber = "/p/auth/user/phone/";

    @Override
    public void doBindPhoneNumber(String phoneNumber, final IRCPostListener listener) {
        BeanPostBindPhoneNumber bean = new BeanPostBindPhoneNumber();
        bean.setActionName(BindPhoneNumber);
        bean.setPhone(phoneNumber);
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

}

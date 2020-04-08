package com.rocedar.deviceplatform.request.impl;

import android.content.Context;

import com.rocedar.base.network.IResponseData;
import com.rocedar.base.network.RequestData;
import com.rocedar.deviceplatform.dto.familydoctor.RCFDDepartmentDTO;
import com.rocedar.deviceplatform.dto.familydoctor.RCFDDoctorListDTO;
import com.rocedar.deviceplatform.dto.familydoctor.RCFDRecordDetailDTO;
import com.rocedar.deviceplatform.dto.familydoctor.RCFDRecordListDTO;
import com.rocedar.deviceplatform.request.RCFamilyDoctorRequest;
import com.rocedar.deviceplatform.request.bean.BeanFamilyDoctorWWZBean;
import com.rocedar.deviceplatform.request.listener.familydoctor.RCFDGetDepartmentListener;
import com.rocedar.deviceplatform.request.listener.familydoctor.RCFDGetDoctorListListener;
import com.rocedar.deviceplatform.request.listener.familydoctor.RCFDGetRecordDetailListener;
import com.rocedar.deviceplatform.request.listener.familydoctor.RCFDGetRecordListListener;
import com.rocedar.deviceplatform.request.listener.familydoctor.RCFDPostListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称：设备管理平台
 * <p>
 * 作者：phj
 * 日期：2017/4/20 上午11:29
 * 版本：V1.0.01
 * 描述：家庭医生微问诊实现
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public class RCFamilyDoctorWWZImpl implements RCFamilyDoctorRequest {


    private Context mContext;

    public RCFamilyDoctorWWZImpl(Context mContext) {
        this.mContext = mContext;
    }


    @Override
    public void getDoctorDepartment(final RCFDGetDepartmentListener listener) {
        BeanFamilyDoctorWWZBean bean = new BeanFamilyDoctorWWZBean();
        bean.setActionName("/p/server/1308002/department/");
        RequestData.NetWorkGetData(mContext, bean, RequestData.Method.Get,
                new IResponseData() {
                    @Override
                    public void getDataSucceedListener(JSONObject data) {
                        List<RCFDDepartmentDTO> dtoList = new ArrayList<>();
                        JSONArray jsonArray = data.optJSONObject("result").optJSONArray("departments");
                        for (int i = 0; jsonArray != null && i < jsonArray.length(); i++) {
                            JSONObject obj = jsonArray.optJSONObject(i);
                            RCFDDepartmentDTO dto = new RCFDDepartmentDTO();
                            dto.setDepartment_id(obj.optInt("department_id"));
                            dto.setDepartment_name(obj.optString("department_name"));
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
    public void getDoctorListFormDepartment(String departmentId, int pn, final RCFDGetDoctorListListener listener) {
        BeanFamilyDoctorWWZBean bean = new BeanFamilyDoctorWWZBean();
        bean.setActionName("/p/server/1308002/doctor/");
        bean.setDepartment_id(departmentId + "");
        bean.setPn(pn + "");
        RequestData.NetWorkGetData(mContext, bean, RequestData.Method.Get,
                new IResponseData() {
                    @Override
                    public void getDataSucceedListener(JSONObject data) {
                        List<RCFDDoctorListDTO> dtoList = new ArrayList<>();
                        JSONArray jsonArray = data.optJSONObject("result").optJSONArray("doctors");
                        for (int i = 0; jsonArray != null && i < jsonArray.length(); i++) {
                            JSONObject obj = jsonArray.optJSONObject(i);
                            RCFDDoctorListDTO dto = new RCFDDoctorListDTO();
                            dto.setDoctor_id(obj.optString("doctor_id"));
                            dto.setDoctor_name(obj.optString("doctor_name"));
                            dto.setHospital_name(obj.optString("hospital_name"));
                            dto.setPortrait(obj.optString("portrait"));
                            dto.setSkilled(obj.optString("skilled"));
                            dto.setTitle_name(obj.optString("title_name"));
                            dto.setDepartment_name(obj.optString("department_name"));
                            dto.setServer_time(obj.optString("server_time"));
                            if (obj.optInt("status") == 0) {
                                dto.setStatus(RCFDDoctorListDTO.DOCTOR_STATUS_ONLINE);
                            } else if (obj.optInt("status") == 2) {
                                dto.setStatus(RCFDDoctorListDTO.DOCTOR_STATUS_OFFLINE);
                            } else {
                                dto.setStatus(RCFDDoctorListDTO.DOCTOR_STATUS_BUSY);
                            }
                            if (obj.optInt("focus") == 1) {
                                dto.setFocus(true);
                            } else {
                                dto.setFocus(false);
                            }
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

    @Override
    public void getMyDoctorList(int pn, final RCFDGetDoctorListListener listener) {
        BeanFamilyDoctorWWZBean bean = new BeanFamilyDoctorWWZBean();
        bean.setActionName("/p/server/1308002/focus/");
        bean.setPn(pn + "");
        RequestData.NetWorkGetData(mContext, bean, RequestData.Method.Get,
                new IResponseData() {
                    @Override
                    public void getDataSucceedListener(JSONObject data) {
                        List<RCFDDoctorListDTO> dtoList = new ArrayList<>();
                        JSONArray jsonArray = data.optJSONObject("result").optJSONArray("focuses");
                        for (int i = 0; jsonArray != null && i < jsonArray.length(); i++) {
                            JSONObject obj = jsonArray.optJSONObject(i);
                            RCFDDoctorListDTO dto = new RCFDDoctorListDTO();
                            dto.setDoctor_id(obj.optString("doctor_id"));
                            dto.setDoctor_name(obj.optString("doctor_name"));
                            dto.setHospital_name(obj.optString("hospital_name"));
                            dto.setPortrait(obj.optString("portrait"));
                            dto.setSkilled(obj.optString("skilled"));
                            dto.setTitle_name(obj.optString("title_name"));
                            dto.setDepartment_name(obj.optString("department_name"));
                            dto.setServer_time(obj.optString("server_time"));
                            if (obj.optInt("status") == 0) {
                                dto.setStatus(RCFDDoctorListDTO.DOCTOR_STATUS_ONLINE);
                            } else if (obj.optInt("status") == 2) {
                                dto.setStatus(RCFDDoctorListDTO.DOCTOR_STATUS_OFFLINE);
                            } else {
                                dto.setStatus(RCFDDoctorListDTO.DOCTOR_STATUS_BUSY);
                            }
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

    @Override
    public void addMyDoctor(String doctor_id, final RCFDPostListener postListener) {
        BeanFamilyDoctorWWZBean bean = new BeanFamilyDoctorWWZBean();
        bean.setActionName("/p/server/1308002/focus/");
        bean.setDoctor_id(doctor_id + "");
        RequestData.NetWorkGetData(mContext, bean, RequestData.Method.Post,
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
    public void deleteMyDoctor(String doctor_id, final RCFDPostListener postListener) {
        BeanFamilyDoctorWWZBean bean = new BeanFamilyDoctorWWZBean();
        bean.setActionName("/p/server/1308002/focus/");
        bean.setDoctor_id(doctor_id + "");
        RequestData.NetWorkGetData(mContext, bean, RequestData.Method.Delete,
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
    public void getRecordList(int pn, final RCFDGetRecordListListener listener) {
        BeanFamilyDoctorWWZBean bean = new BeanFamilyDoctorWWZBean();
        bean.setActionName("/p/server/1308002/record/");
        bean.setPn(pn + "");
        RequestData.NetWorkGetData(mContext, bean, RequestData.Method.Get,
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
        BeanFamilyDoctorWWZBean bean = new BeanFamilyDoctorWWZBean();
        bean.setActionName("/p/server/1308002/record/" + recordId + "/");
        RequestData.NetWorkGetData(mContext, bean, RequestData.Method.Get,
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
    public void saveRecord(String recordId, final RCFDPostListener postListener) {
        BeanFamilyDoctorWWZBean bean = new BeanFamilyDoctorWWZBean();
        bean.setActionName("/p/server/1308002/record/" + recordId + "/");
        RequestData.NetWorkGetData(mContext, bean, RequestData.Method.Post,
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
}

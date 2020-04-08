package com.rocedar.sdk.familydoctor.request.impl;

import android.content.Context;

import com.google.gson.Gson;
import com.rocedar.lib.base.network.IRCPostListener;
import com.rocedar.lib.base.network.IResponseData;
import com.rocedar.lib.base.network.RCRequestNetwork;
import com.rocedar.sdk.familydoctor.dto.RCFDDepartmentDTO;
import com.rocedar.sdk.familydoctor.dto.RCFDDoctorCommentsDTO;
import com.rocedar.sdk.familydoctor.dto.RCFDDoctorIntroduceDTO;
import com.rocedar.sdk.familydoctor.dto.RCFDDoctorListDTO;
import com.rocedar.sdk.familydoctor.request.IRCFDDoctorRequest;
import com.rocedar.sdk.familydoctor.request.bean.BeanEvaluation;
import com.rocedar.sdk.familydoctor.request.bean.BeanFamilyDoctor;
import com.rocedar.sdk.familydoctor.request.listener.fd.RCFDGetDepartmentListener;
import com.rocedar.sdk.familydoctor.request.listener.fd.RCFDGetDoctorCommentsListener;
import com.rocedar.sdk.familydoctor.request.listener.fd.RCFDGetDoctorIntroduceListener;
import com.rocedar.sdk.familydoctor.request.listener.fd.RCFDGetDoctorListListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称：瑰柏SDK-基础库
 * <p>
 * 作者：phj
 * 日期：2018/5/21 下午5:09
 * 版本：V1.0.00
 * 描述：瑰柏SDK-
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCFDDoctorRequestImpl implements IRCFDDoctorRequest {


    private Context mContext;

    public RCFDDoctorRequestImpl(Context mContext) {
        this.mContext = mContext;
    }

    //家庭医生-科室列表
    private final static String DoctorDepartmentList = "/p/server/1308002/department/";
    //家庭医生-根据科室查询医生列表
    private final static String DoctorListFormDepartment = "/p/server/1308002/doctor/";
    //家庭医生-我的医生列表（关注的医生列表）
    private final static String MyDoctorList = "/p/server/1308002/focus/";
    //家庭医生-添加我的医生（关注医生）
    private final static String AddMyDoctor = "/p/server/1308002/focus/";
    //家庭医生-删除我的医生（取消关注医生）
    private final static String DeleteMyDoctor = "/p/server/1308002/focus/";


    @Override
    public void getDoctorDepartment(final RCFDGetDepartmentListener listener) {
        BeanFamilyDoctor bean = new BeanFamilyDoctor();
        bean.setActionName(DoctorDepartmentList);
        RCRequestNetwork.NetWorkGetData(mContext, bean, RCRequestNetwork.Method.Get,
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
        BeanFamilyDoctor bean = new BeanFamilyDoctor();
        bean.setActionName(DoctorListFormDepartment);
        bean.setDepartment_id(departmentId + "");
        bean.setPn(pn + "");
        RCRequestNetwork.NetWorkGetData(mContext, bean, RCRequestNetwork.Method.Get,
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
        BeanFamilyDoctor bean = new BeanFamilyDoctor();
        bean.setActionName(MyDoctorList);
        bean.setPn(pn + "");
        RCRequestNetwork.NetWorkGetData(mContext, bean, RCRequestNetwork.Method.Get,
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
    public void addMyDoctor(String doctor_id, final IRCPostListener postListener) {
        BeanFamilyDoctor bean = new BeanFamilyDoctor();
        bean.setActionName(AddMyDoctor);
        bean.setDoctor_id(doctor_id + "");
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
    public void deleteMyDoctor(String doctor_id, final IRCPostListener postListener) {
        BeanFamilyDoctor bean = new BeanFamilyDoctor();
        bean.setActionName(DeleteMyDoctor);
        bean.setDoctor_id(doctor_id + "");
        RCRequestNetwork.NetWorkGetData(mContext, bean, RCRequestNetwork.Method.Delete,
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


    //家庭医生-保存用户评价
    private final static String SaveEvaluation = "/p/server/1308002/comment/";
    //家庭医生-查询医生评价列表
    private final static String QueryComment = "/p/server/1308002/doctor/comments/";
    //家庭医生-查询医生详情
    private final static String QueryIntroduce = "/p/server/1308002/doctor/detail/";

    @Override
    public void saveUserEvaluation(String record_id, String doctor_id, String comment, String grade, final IRCPostListener listener) {
        BeanEvaluation bean = new BeanEvaluation();
        bean.setActionName(SaveEvaluation);
        bean.setRecord_id(record_id);
        bean.setDoctor_id(doctor_id);
        bean.setComment(comment);
        bean.setGrade(grade);
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

    @Override
    public void getDoctorCommentList(String doctor_id, int page, final RCFDGetDoctorCommentsListener listener) {
        BeanFamilyDoctor bean = new BeanFamilyDoctor();
        bean.setActionName(QueryComment);
        bean.setDoctor_id(doctor_id);
        bean.setPn(page + "");
        RCRequestNetwork.NetWorkGetData(mContext, bean, RCRequestNetwork.Method.Get,
                new IResponseData() {
                    @Override
                    public void getDataSucceedListener(JSONObject data) {
                        JSONArray mja = data.optJSONObject("result").optJSONArray("comments");
                        List<RCFDDoctorCommentsDTO> mList = new ArrayList<>();
                        for (int i = 0; i < mja.length(); i++) {
                            mList.add(new Gson().fromJson(mja.optJSONObject(i).toString(), RCFDDoctorCommentsDTO.class));
                        }
                        listener.getDataSuccess(mList);
                    }

                    @Override
                    public void getDataErrorListener(String msg, int status) {
                        listener.getDataError(status, msg);
                    }
                });
    }


    @Override
    public void getDoctorIntroduce(String doctor_id, final RCFDGetDoctorIntroduceListener listener) {
        BeanFamilyDoctor bean = new BeanFamilyDoctor();
        bean.setActionName(QueryIntroduce);
        bean.setDoctor_id(doctor_id);
        RCRequestNetwork.NetWorkGetData(mContext, bean, RCRequestNetwork.Method.Get,
                new IResponseData() {
                    @Override
                    public void getDataSucceedListener(JSONObject data) {
                        JSONObject mja = data.optJSONObject("result").optJSONObject("doctor");
                        RCFDDoctorIntroduceDTO dto = new RCFDDoctorIntroduceDTO();
                        if (mja != null) {
                            dto = new Gson().fromJson(mja.toString(), RCFDDoctorIntroduceDTO.class);
                        }
                        listener.getDataSuccess(dto);
                    }

                    @Override
                    public void getDataErrorListener(String msg, int status) {
                        listener.getDataError(status, msg);
                    }
                });
    }



}

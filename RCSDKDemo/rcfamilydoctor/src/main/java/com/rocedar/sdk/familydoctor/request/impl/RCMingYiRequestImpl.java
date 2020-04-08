package com.rocedar.sdk.familydoctor.request.impl;

import android.content.Context;

import com.google.gson.Gson;
import com.rocedar.lib.base.network.IRCPostListener;
import com.rocedar.lib.base.network.IResponseData;
import com.rocedar.lib.base.network.RCBean;
import com.rocedar.lib.base.network.RCRequestNetwork;
import com.rocedar.lib.base.unit.RCJavaUtil;
import com.rocedar.sdk.familydoctor.dto.mingyi.RCMIngYiDoctorListDTO;
import com.rocedar.sdk.familydoctor.dto.mingyi.RCMingYiDoctorDetailDTO;
import com.rocedar.sdk.familydoctor.dto.mingyi.RCMingYiDoctorListSelectlDTO;
import com.rocedar.sdk.familydoctor.dto.mingyi.RCMingYiMaterialDTO;
import com.rocedar.sdk.familydoctor.request.IRCMingYiRequest;
import com.rocedar.sdk.familydoctor.request.bean.BeanFamilyDoctor;
import com.rocedar.sdk.familydoctor.request.bean.BeanGetDoctorListData;
import com.rocedar.sdk.familydoctor.request.bean.BeanMingYiMaterial;
import com.rocedar.sdk.familydoctor.request.bean.BeanPostMingYiOrder;
import com.rocedar.sdk.familydoctor.request.listener.mingyi.RCMingYiDoctorDetailListener;
import com.rocedar.sdk.familydoctor.request.listener.mingyi.RCMingYiDoctorListListener;
import com.rocedar.sdk.familydoctor.request.listener.mingyi.RCMingYiDoctorListSelectListener;
import com.rocedar.sdk.familydoctor.request.listener.mingyi.RCMingYiPostMaterialListener;
import com.rocedar.sdk.familydoctor.request.listener.mingyi.RCMingYiPostOrderListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称：瑰柏SDK-基础库
 * <p>
 * 作者：phj
 * 日期：2018/7/20 下午3:28
 * 版本：V1.0.00
 * 描述：瑰柏SDK-
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCMingYiRequestImpl implements IRCMingYiRequest {

    private Context mContext;

    public RCMingYiRequestImpl(Context mContext) {
        this.mContext = mContext;
    }


    private final String GET_RECOMMEND_DOCTOR_LIST = "/p/server/1311001/doctor/recommend/";
    //获取医生信息详情接口
    private final String GET_DOCTOR_DETAIL = "/p/server/1311001/doctor/detail/";
    //提交订单
    private final String POST_ORDER = "/p/server/order/";
    //病人资料
    private final String Patient = "/p/patient/medical/";


    @Override
    public void getRecommendDoctor(final RCMingYiDoctorListListener listener) {
        RCBean bean = new RCBean();
        bean.setActionName(GET_RECOMMEND_DOCTOR_LIST);
        RCRequestNetwork.NetWorkGetData(mContext, bean, RCRequestNetwork.Method.Get,
                new IResponseData() {
                    @Override
                    public void getDataSucceedListener(JSONObject data) {
                        JSONArray mja = data.optJSONArray("result");
                        listener.getDataSuccess(doctorDataList(mja));
                    }

                    @Override
                    public void getDataErrorListener(String msg, int status) {

                    }
                });
    }

    @Override
    public void getDoctorDetail(String doctor_id, final RCMingYiDoctorDetailListener listener) {
        BeanFamilyDoctor bean = new BeanFamilyDoctor();
        bean.setActionName(GET_DOCTOR_DETAIL);
        bean.setDoctor_id(doctor_id);
        RCRequestNetwork.NetWorkGetData(mContext, bean, RCRequestNetwork.Method.Get,
                new IResponseData() {
                    @Override
                    public void getDataSucceedListener(JSONObject data) {
                        RCMingYiDoctorDetailDTO dto = null;
                        try {
                            dto = new Gson().fromJson(data.optJSONObject("result").toString(),
                                    RCMingYiDoctorDetailDTO.class);
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

    /**
     * 获取 医生列表科室、价格、医院数据
     *
     * @param status   0:科室数据  1：价格数据 2：医院数据
     * @param listener
     */
    @Override
    public void getDoctorListSelectData(int status, final RCMingYiDoctorListSelectListener listener) {
        RCBean mBean = new RCBean();
        switch (status) {
            case 0:
                mBean.setActionName("/p/server/1311001/department/");
                break;
            case 1:
                mBean.setActionName("/p/server/1311001/fee/");
                break;
            case 2:
                mBean.setActionName("/p/server/1311001/hospital/");
                break;
        }
        RCRequestNetwork.NetWorkGetData(mContext, mBean, RCRequestNetwork.Method.Get,
                new IResponseData() {
                    @Override
                    public void getDataSucceedListener(JSONObject data) {

                        JSONArray root_mja = data.optJSONArray("result");
                        List<RCMingYiDoctorListSelectlDTO> mList = new ArrayList<>();
                        for (int i = 0; i < root_mja.length(); i++) {
                            JSONObject root_mjo = root_mja.optJSONObject(i);
                            RCMingYiDoctorListSelectlDTO selectlDTO = new RCMingYiDoctorListSelectlDTO();
                            selectlDTO.setId(root_mjo.optInt("id"));
                            selectlDTO.setName(root_mjo.optString("name"));
                            JSONArray child_mja = root_mjo.optJSONArray("childs");
                            List<RCMingYiDoctorListSelectlDTO.childsDTO> childsDTOS = new ArrayList<>();
                            if (child_mja.length() > 0) {
                                for (int j = 0; j < child_mja.length(); j++) {
                                    JSONObject child_mjo = child_mja.optJSONObject(j);
                                    RCMingYiDoctorListSelectlDTO.childsDTO childsDTO = new RCMingYiDoctorListSelectlDTO.childsDTO();
                                    childsDTO.setId(child_mjo.optInt("id"));
                                    childsDTO.setName(child_mjo.optString("name"));
                                    childsDTOS.add(childsDTO);
                                }
                                selectlDTO.setChildsDTOS(childsDTOS);
                            }
                            mList.add(selectlDTO);
                        }

                        listener.getDataSuccess(mList);
                    }

                    @Override
                    public void getDataErrorListener(String msg, int status) {
                        listener.getDataError(status, msg);
                    }
                });
    }

    /**
     * 获取 医生列表 数据
     *
     * @param department 科室：<一级科室ID>,<二级科室ID>
     * @param fee        价格
     * @param hospital   医院：<一级医院ID>,<二级医院ID>
     * @param pn
     * @param listener
     */
    @Override
    public void getDoctorListData(String department, String fee, String hospital, int pn, final RCMingYiDoctorListListener listener) {
        BeanGetDoctorListData mBean = new BeanGetDoctorListData();
        mBean.setActionName("/p/server/1311001/doctor/");
        mBean.setDepartment(department);
        mBean.setFee(fee);
        mBean.setHospital(hospital);
        mBean.setPn(pn + "");
        RCRequestNetwork.NetWorkGetData(mContext, mBean, RCRequestNetwork.Method.Get,
                new IResponseData() {
                    @Override
                    public void getDataSucceedListener(JSONObject data) {
                        JSONArray mja = data.optJSONArray("result");
                        listener.getDataSuccess(doctorDataList(mja));
                    }

                    @Override
                    public void getDataErrorListener(String msg, int status) {
                        listener.getDataError(status, msg);
                    }
                });

    }

    private List<RCMIngYiDoctorListDTO> doctorDataList(JSONArray mja) {
        List<RCMIngYiDoctorListDTO> mList = new ArrayList<>();
        for (int i = 0; i < mja.length(); i++) {
            JSONObject mjo = mja.optJSONObject(i);
            RCMIngYiDoctorListDTO mDTO = new RCMIngYiDoctorListDTO();
            mDTO.setDoctor_id(mjo.optString("doctor_id"));
            mDTO.setDoctor_name(mjo.optString("doctor_name"));
            mDTO.setPortrait(mjo.optString("portrait"));
            mDTO.setOpen(true);
            mDTO.setHasOpen(RCJavaUtil.textCount(mjo.optString("skilled"), 1, 109, 12));
            mDTO.setTitle_name(mjo.optString("title_name"));
            mDTO.setDepartment_name(mjo.optString("department_name"));
            mDTO.setHospital_level(mjo.optString("hospital_level"));
            mDTO.setHospital_name(mjo.optString("hospital_name"));
            mDTO.setSkilled(mjo.optString("skilled"));
            mDTO.setServer_time(mjo.optInt("server_time"));
            mDTO.setFee((float) mjo.optDouble("fee"));
            mDTO.setFee_time(mjo.optInt("fee_time"));
            mList.add(mDTO);
        }
        return mList;
    }

    @Override
    public void postConsultOrder(String doctor_id, String service_type, String patient_id, String phone,
                                 String reservation_time, final RCMingYiPostOrderListener listener) {
        BeanPostMingYiOrder bean = new BeanPostMingYiOrder();
        bean.setDoctor_id(doctor_id);
        bean.setPatient_id(patient_id);
        bean.setPhone(phone);
        bean.setService_type(service_type);
        bean.setReservation_time(reservation_time);
        bean.setActionName(POST_ORDER);
        RCRequestNetwork.NetWorkGetData(mContext, bean, RCRequestNetwork.Method.Post,
                new IResponseData() {
                    @Override
                    public void getDataSucceedListener(JSONObject data) {
                        listener.getDataSuccess(data.optJSONObject("result").optInt("order_id"));
                    }

                    @Override
                    public void getDataErrorListener(String msg, int status) {
                        listener.getDataError(status, msg);
                    }
                });


    }


    @Override
    public void postMaterial(RCMingYiMaterialDTO dto, final IRCPostListener listener) {
        BeanMingYiMaterial bean = new BeanMingYiMaterial();
        bean.setCase_data(dto.getCase_data());
        bean.setExpect_help(dto.getExpect_help());
        bean.setHospital(dto.getHospital());
        bean.setInspection(dto.getInspection());
        bean.setMedical_time(dto.getMedical_time() + "");
        bean.setOrder_id(dto.getOrder_id() + "");
        bean.setPatient_id(dto.getPatient_id() + "");
        bean.setProfession(dto.getProfession());
        bean.setResult(dto.getResult());
        bean.setSymptom(dto.getSymptom());
        bean.setActionName(Patient);
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
    public void getMaterial(String orderId, String patientId, final RCMingYiPostMaterialListener listener) {
        BeanMingYiMaterial bean = new BeanMingYiMaterial();
        bean.setOrder_id(orderId);
        bean.setPatient_id(patientId);
        bean.setActionName(Patient);
        RCRequestNetwork.NetWorkGetData(mContext, bean, RCRequestNetwork.Method.Get,
                new IResponseData() {
                    @Override
                    public void getDataSucceedListener(JSONObject data) {
                        try {
                            listener.getDataSuccess(new Gson().fromJson(data.optJSONObject("result").toString(),
                                    RCMingYiMaterialDTO.class));
                        } catch (Exception e) {
                            listener.getDataSuccess(null);
                        }
                    }

                    @Override
                    public void getDataErrorListener(String msg, int status) {
                        listener.getDataError(status, msg);
                    }
                });
    }

}

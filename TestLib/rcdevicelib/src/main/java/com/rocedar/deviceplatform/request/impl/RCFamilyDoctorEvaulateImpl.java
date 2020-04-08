package com.rocedar.deviceplatform.request.impl;

import android.content.Context;

import com.google.gson.Gson;
import com.rocedar.base.network.IResponseData;
import com.rocedar.base.network.RequestData;
import com.rocedar.deviceplatform.dto.familydoctor.RCFDDoctorCommentsDTO;
import com.rocedar.deviceplatform.dto.familydoctor.RCFDDoctorIntroduceDTO;
import com.rocedar.deviceplatform.request.RCFamilyDoctorEvaulateRequest;
import com.rocedar.deviceplatform.request.bean.BeanGetFamilyDoctorComment;
import com.rocedar.deviceplatform.request.bean.BeanPostFDSaveEvaulateBean;
import com.rocedar.deviceplatform.request.listener.familydoctor.RCFDGetDoctorCommnetsListener;
import com.rocedar.deviceplatform.request.listener.familydoctor.RCFDGetDoctorIntroduceListener;
import com.rocedar.deviceplatform.request.listener.familydoctor.RCFDPostListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liuyi
 * @date 2017/11/7
 * @desc 家庭医生评价有关请求接口类
 * @veison V3431
 */

public class RCFamilyDoctorEvaulateImpl implements RCFamilyDoctorEvaulateRequest {
    private Context mContext;

    public RCFamilyDoctorEvaulateImpl(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void saveUserEvaulate(String record_id, String doctor_id, String comment, String grade, final RCFDPostListener listener) {
        BeanPostFDSaveEvaulateBean bean = new BeanPostFDSaveEvaulateBean();
        bean.setActionName("p/server/1308002/comment/");
        bean.setRecord_id(record_id);
        bean.setDoctor_id(doctor_id);
        bean.setComment(comment);
        bean.setGrade(grade);
        RequestData.NetWorkGetData(mContext, bean, RequestData.Method.Post,
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
    public void getDoctorCommentList(String doctor_id, int page, final RCFDGetDoctorCommnetsListener listener) {
        BeanGetFamilyDoctorComment bean = new BeanGetFamilyDoctorComment();
        bean.setActionName("p/server/1308002/doctor/comments/");
        bean.setDoctor_id(doctor_id);
        bean.setPn(page + "");
        RequestData.NetWorkGetData(mContext, bean, RequestData.Method.Get,
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
    public void getDoctorIntroduced(String doctor_id, final RCFDGetDoctorIntroduceListener listener) {
        BeanGetFamilyDoctorComment bean = new BeanGetFamilyDoctorComment();
        bean.setActionName("p/server/1308002/doctor/detail/");
        bean.setDoctor_id(doctor_id);
        RequestData.NetWorkGetData(mContext, bean, RequestData.Method.Get,
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

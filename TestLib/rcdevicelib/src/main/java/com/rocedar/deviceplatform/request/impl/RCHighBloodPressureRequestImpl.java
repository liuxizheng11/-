package com.rocedar.deviceplatform.request.impl;

import android.content.Context;

import com.google.gson.Gson;
import com.rocedar.base.network.IResponseData;
import com.rocedar.base.network.RequestData;
import com.rocedar.deviceplatform.dto.highbloodpressure.RCHBPDoctorDTO;
import com.rocedar.deviceplatform.dto.highbloodpressure.RCHBPVideoInstituteDTO;
import com.rocedar.deviceplatform.request.RCHighBloodPressureRequest;
import com.rocedar.deviceplatform.request.bean.BasePlatformBean;
import com.rocedar.deviceplatform.request.bean.BeanFamilyDoctorWWZBean;
import com.rocedar.deviceplatform.request.listener.highbloodpressure.RCHBPGetDoctorDetailsListener;
import com.rocedar.deviceplatform.request.listener.highbloodpressure.RCHBPGetDoctorListener;
import com.rocedar.deviceplatform.request.listener.highbloodpressure.RCHBPGetVideoInstituListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liuyi
 * @date 2017/11/27
 * @desc 高血压大数据有关请求接口实现类
 * @veison V3.4.32
 */

public class RCHighBloodPressureRequestImpl implements RCHighBloodPressureRequest {

    private Context mContext;

    public RCHighBloodPressureRequestImpl(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void getVideoInstituteList(String pn, final RCHBPGetVideoInstituListener listener) {
        BeanFamilyDoctorWWZBean bean = new BeanFamilyDoctorWWZBean();
        bean.setActionName("/p/server/sti/cathedra/");
        bean.setPn(pn);
        RequestData.NetWorkGetData(mContext, bean, RequestData.Method.Get,
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
    public void getHBPDoctor(String pn,final RCHBPGetDoctorListener listener) {
        BeanFamilyDoctorWWZBean bean = new BeanFamilyDoctorWWZBean();
        bean.setActionName("/p/server/sti/doctor/");
        bean.setPn(pn);
        RequestData.NetWorkGetData(mContext, bean, RequestData.Method.Get,
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
    public void getHBPDoctorDetails(int doctorId,final RCHBPGetDoctorDetailsListener listener) {
        BasePlatformBean bean = new BasePlatformBean();
        bean.setActionName("/p/server/sti/doctor/"+doctorId+"/");

        RequestData.NetWorkGetData(mContext, bean, RequestData.Method.Get,
                new IResponseData() {
                    @Override
                    public void getDataSucceedListener(JSONObject data) {
                        JSONObject result = data.optJSONObject("result");

                        listener.getDataSuccess(new Gson().fromJson(result.toString(), RCHBPDoctorDTO.class));

                    }

                    @Override
                    public void getDataErrorListener(String msg, int status) {
                        listener.getDataError(status, msg);
                    }
                });
    }
}

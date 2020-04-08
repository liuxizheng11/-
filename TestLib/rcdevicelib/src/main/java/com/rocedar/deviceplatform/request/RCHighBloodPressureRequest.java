package com.rocedar.deviceplatform.request;

import com.rocedar.deviceplatform.request.listener.highbloodpressure.RCHBPGetDoctorDetailsListener;
import com.rocedar.deviceplatform.request.listener.highbloodpressure.RCHBPGetDoctorListener;
import com.rocedar.deviceplatform.request.listener.highbloodpressure.RCHBPGetVideoInstituListener;

/**
 * @author liuyi
 * @date 2017/11/27
 * @desc 高血压大数据有关请求接口类
 * @veison V3.4.32
 */

public interface RCHighBloodPressureRequest {
    /**
     * 视频讲座查询
     *
     * @param pn       页码
     * @param listener
     */
    void getVideoInstituteList(String pn, RCHBPGetVideoInstituListener listener);

    /**
     * 高血压大数据医生
     *
     * @param pn
     * @param listener
     */
    void getHBPDoctor(String pn, RCHBPGetDoctorListener listener);

    /**
     * 高血压大数据医生详情
     * @param doctorId
     * @param listener
     */
    void getHBPDoctorDetails(int doctorId, RCHBPGetDoctorDetailsListener listener);
}

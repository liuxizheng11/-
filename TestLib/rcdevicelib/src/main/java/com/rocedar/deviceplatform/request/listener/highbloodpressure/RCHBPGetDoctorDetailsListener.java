package com.rocedar.deviceplatform.request.listener.highbloodpressure;

import com.rocedar.deviceplatform.dto.highbloodpressure.RCHBPDoctorDTO;

/**
 * @author liuyi
 * @date 2017/11/27
 * @desc
 * @veison
 */

public interface RCHBPGetDoctorDetailsListener {

    void getDataSuccess(RCHBPDoctorDTO dto);

    void getDataError(int status, String msg);
}

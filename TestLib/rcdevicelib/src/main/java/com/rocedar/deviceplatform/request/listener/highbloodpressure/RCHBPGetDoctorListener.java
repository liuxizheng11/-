package com.rocedar.deviceplatform.request.listener.highbloodpressure;

import com.rocedar.deviceplatform.dto.highbloodpressure.RCHBPDoctorDTO;

import java.util.List;

/**
 * @author liuyi
 * @date 2017/11/27
 * @desc
 * @veison
 */

public interface RCHBPGetDoctorListener {

    void getDataSuccess(List<RCHBPDoctorDTO> dto);

    void getDataError(int status, String msg);
}

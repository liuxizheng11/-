package com.rocedar.sdk.familydoctor.request.listener.hdp;


import com.rocedar.sdk.familydoctor.dto.hdp.RCHBPDoctorDTO;

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

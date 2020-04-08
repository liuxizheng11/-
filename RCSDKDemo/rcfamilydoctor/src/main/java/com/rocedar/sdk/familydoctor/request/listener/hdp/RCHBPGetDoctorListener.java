package com.rocedar.sdk.familydoctor.request.listener.hdp;


import com.rocedar.sdk.familydoctor.dto.hdp.RCHBPDoctorDTO;

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

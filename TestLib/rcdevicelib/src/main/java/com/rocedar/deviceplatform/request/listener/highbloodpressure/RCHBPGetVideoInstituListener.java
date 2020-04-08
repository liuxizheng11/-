package com.rocedar.deviceplatform.request.listener.highbloodpressure;

import com.rocedar.deviceplatform.dto.highbloodpressure.RCHBPVideoInstituteDTO;

import java.util.List;

/**
 * @author liuyi
 * @date 2017/11/27
 * @desc
 * @veison
 */

public interface RCHBPGetVideoInstituListener {

    void getDataSuccess(List<RCHBPVideoInstituteDTO> dto);

    void getDataError(int status, String msg);
}

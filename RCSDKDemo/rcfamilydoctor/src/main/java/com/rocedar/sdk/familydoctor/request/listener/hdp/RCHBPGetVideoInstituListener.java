package com.rocedar.sdk.familydoctor.request.listener.hdp;


import com.rocedar.sdk.familydoctor.dto.hdp.RCHBPVideoInstituteDTO;

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

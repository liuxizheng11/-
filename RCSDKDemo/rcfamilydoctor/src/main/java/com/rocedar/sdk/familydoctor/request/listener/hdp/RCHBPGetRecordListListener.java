package com.rocedar.sdk.familydoctor.request.listener.hdp;


import com.rocedar.sdk.familydoctor.dto.hdp.RCHBPRecordListDTO;

import java.util.List;

/**
 * @author liuyi
 * @date 2017/11/28
 * @desc
 * @veison
 */

public interface RCHBPGetRecordListListener {

    void getDataSuccess(List<RCHBPRecordListDTO> dto);

    void getDataError(int status, String msg);
}

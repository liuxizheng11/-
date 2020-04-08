package com.rocedar.deviceplatform.request.listener;

import com.rocedar.deviceplatform.dto.record.RCHealthRecordDataDTO;

/**
 * @author liuyi
 * @date 2017/3/4
 * @desc 我的健康档案监听
 * @veison V3.3.30(动吖)
 */

public interface RCHealthRecordDataListener {

    void getDataSuccess(RCHealthRecordDataDTO dto);

    void getDataError(int status, String msg);
}

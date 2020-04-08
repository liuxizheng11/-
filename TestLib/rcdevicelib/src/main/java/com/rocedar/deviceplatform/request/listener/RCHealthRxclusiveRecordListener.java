package com.rocedar.deviceplatform.request.listener;

import com.rocedar.deviceplatform.dto.record.RCHealthRxclusiveRecordDTO;

/**
 * @author liuyi
 * @date 2017/3/7
 * @desc 我的专属健康报告监听
 * @veison V3.3.30(动吖)
 */

public interface RCHealthRxclusiveRecordListener {

    void getDataSuccess(RCHealthRxclusiveRecordDTO dto);

    void getDataError(int status, String msg);
}

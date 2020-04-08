package com.rocedar.deviceplatform.request.listener;

import com.rocedar.deviceplatform.dto.record.RCHealthHomeRecordDTO;

/**
 * @author liuyi
 * @date 2017/3/4
 * @desc 我的健康档案监听
 * @veison V3.3.30(动吖)
 */

public interface RCHealthHomeRecordListener {

    void getDataSuccess(RCHealthHomeRecordDTO dto);

    void getDataError(int status, String msg);
}

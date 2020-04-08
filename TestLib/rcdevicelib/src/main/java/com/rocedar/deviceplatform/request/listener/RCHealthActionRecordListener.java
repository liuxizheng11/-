package com.rocedar.deviceplatform.request.listener;

import com.rocedar.deviceplatform.dto.record.RCHealthActionRecordDTO;

/**
 * @author liuyi
 * @date 2017/3/7
 * @desc 近三月行为报告监听
 * @veison V3.3.30(动吖)
 */

public interface RCHealthActionRecordListener {

    void getDataSuccess(RCHealthActionRecordDTO dto);

    void getDataError(int status, String msg);
}

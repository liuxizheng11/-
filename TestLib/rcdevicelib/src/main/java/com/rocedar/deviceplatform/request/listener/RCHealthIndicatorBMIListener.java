package com.rocedar.deviceplatform.request.listener;

import com.rocedar.deviceplatform.dto.record.RCHealthIndicatorBMIDTO;

/**
 * @author liuyi
 * @date 2017/3/11
 * @desc 用户最新BMI监听
 * @veison V3.3.30(动吖)
 */

public interface RCHealthIndicatorBMIListener {

    void getDataSuccess(RCHealthIndicatorBMIDTO bmidto);

    void getDataError(int status, String msg);
}

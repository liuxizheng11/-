package com.rocedar.deviceplatform.request.listener;

import org.json.JSONObject;

/**
 * @author liuyi
 * @date 2017/3/4
 * @desc 用户设备数据详情监听
 * @veison V3.3.30(动吖)
 */

public interface RCDeviceDataDetailsListener {

    void getDataSuccess(JSONObject result);

    void getDataError(int status, String msg);


}

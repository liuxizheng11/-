package com.rocedar.sdk.iting.device;

import com.rocedar.sdk.iting.device.listener.ITingBindListener;
import com.rocedar.sdk.iting.device.listener.ITingConnectListener;
import com.rocedar.sdk.iting.request.dto.RCITingWatchInfoDTO;

import cn.appscomm.bluetoothsdk.interfaces.ResultCallBack;

/**
 * 项目名称：瑰柏SDK-商城
 * <p>
 * 作者：phj
 * 日期：2019/7/25 3:13 PM
 * 版本：V1.1.00
 * 描述：瑰柏SDK-服务商品
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public interface ITingDeviceConnectFunction {

    int[] CONNECT_FUNCTION_TYPE = {
            ResultCallBack.TYPE_CONNECT,
            ResultCallBack.TYPE_TRANSPARENT_PASSAGE_DATA,
            ResultCallBack.TYPE_DISCONNECT,
            ResultCallBack.TYPE_BIND_START_QR_CODE,
            ResultCallBack.TYPE_BIND_END,
            ResultCallBack.TYPE_CHECK_INIT,
            ResultCallBack.TYPE_SET_LOCK_TIME,
            ResultCallBack.TYPE_SET_UNLOCK_TIME,
            ResultCallBack.TYPE_SET_DEVICE_TIME
    };

    void doConnect(RCITingWatchInfoDTO dto, ITingConnectListener connectListener);

    void doBind(RCITingWatchInfoDTO dto, ITingBindListener bindListener);

    void disconnect();

    boolean isConnect();

    boolean isConnectIng();

    RCITingWatchInfoDTO getLastConnectDeviceDTO();

    void doBandWatchSetting(String uid);

    void unlockTime();

    void lockTime();

    void photoTimeOver();

    //返回值处理
    void parsingData(int result, Object[] objects);

    void parsingError(int result);
}

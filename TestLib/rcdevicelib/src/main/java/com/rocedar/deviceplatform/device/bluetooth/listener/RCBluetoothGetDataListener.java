package com.rocedar.deviceplatform.device.bluetooth.listener;

import org.json.JSONArray;

/**
 * 项目名称：设备管理平台
 * <p>
 * 作者：phj
 * 日期：2017/1/10 下午4:14
 * 版本：V1.0
 * 描述：蓝牙设备获取到数据监听
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public interface RCBluetoothGetDataListener {

    void getDataError(int status, String msg);

    void getDataStart();

    void dataInfo(JSONArray array);

}

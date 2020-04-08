package com.rocedar.deviceplatform.device.wifi;

import com.rocedar.deviceplatform.device.wifi.listener.RCWifiConfigListener;

/**
 * 项目名称：设备管理平台
 * <p>
 * 作者：phj
 * 日期：2017/2/5 下午8:57
 * 版本：V1.0
 * 描述：wifi类设备接口
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public interface RCWifi {

    void configWifi(RCWifiConfigListener configListener, String wifiName, String password);

}

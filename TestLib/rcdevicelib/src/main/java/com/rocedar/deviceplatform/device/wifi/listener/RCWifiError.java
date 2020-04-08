package com.rocedar.deviceplatform.device.wifi.listener;

/**
 * 项目名称：设备管理平台
 * <p>
 * 作者：phj
 * 日期：2017/2/5 下午9:12
 * 版本：V1.0
 * 描述：WIFI配置错误码
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public interface RCWifiError {


    //设备不支持
    int ERROR_PHONE_NOT_SUPPORT = 300000;

    //数据获取超时
    int ERROR_CONFIG_TIME_OUT = 300001;

    //设备WIFI配置失败
    int ERROR_WIFI_CONFIG = 300002;


}

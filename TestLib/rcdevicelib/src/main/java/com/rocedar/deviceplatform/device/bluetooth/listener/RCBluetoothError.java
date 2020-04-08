package com.rocedar.deviceplatform.device.bluetooth.listener;

/**
 * 项目名称：设备管理平台
 * <p>
 * 作者：phj
 * 日期：2017/1/16 下午5:26
 * 版本：V1.0
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public interface RCBluetoothError {

    //设备不支持
    int ERROR_PHONE_NOT_SUPPORT = 100001;

    //设备连接失败
    int ERROR_CONNECT = 100002;

    //设备连接失败多次
    int ERROR_CONNECT_MORE = 100006;

    //找不到设备
    int ERROR_CANNOT_FIND = 100004;

    //设备正在获取数据
    int ERROR_DEVICE_BUSY = 100003;

    //设备选择错误
    int ERROR_DEVICE_CHOOSE = 100005;

    //数据获取超时
    int ERROR_GET_DATA_TIME_OUT = 200001;

    //血压测量出错
    int ERROR_TEST_BLOOD_PRESSURE = 200101;

    //时间同步失败
    int ERROR_SETTING_TIME = 200102;

    //获取睡眠数据为空
    int ERROR_SLEEP_DATA_NULL = 200201;


}

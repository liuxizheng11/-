package com.rocedar.deviceplatform.config;

/**
 * 项目名称：设备管理平台
 * <p>
 * 作者：phj
 * 日期：2017/1/13 下午8:23
 * 版本：V1.0
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public interface RCBluetoothDataType {

    int DATATYPE_MAX = 19999;

    final int DATATYPE_STEP_TODAY = 10101;
    final int DATATYPE_STEP_HISTORY = 10102;
    final int DATATYPE_SLEPP_TODAY = 10201;
    final int DATATYPE_SLEPP_HISTORY = 10202;
    final int DATATYPE_BLOOD_PRESSURE_HISTORY = 10301;
    final int DATATYPE_HEARTR_ATE_TODAY = 10401;
    final int DATATYPE_HEARTR_ATE_HISTORY = 10402;

    //获取步行和跑步数据（魔集客耳机）
    final int DATATYPE_STEP_AND_RUN_NOW = 10103;
    //获取所有数据(缘渡手串)
    final int DATATYPE_TOADY_All = 10001;

}

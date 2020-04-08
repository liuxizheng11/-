package com.rocedar.deviceplatform.config;

/**
 * 项目名称：设备管理平台
 * <p>
 * 作者：phj
 * 日期：2017/1/14 下午1:11
 * 版本：V1.0
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public interface RCBluetoothDoType {

    //设置时间
    final int DO_SETTING_TIME = 20000;
    //设置打开心率自动测量
    final int DO_SETTING_OPEN_HEART_TEST = 20001;
    //绑定
    final int DO_BINDING = 20002;
    //同步
    final int DO_SYNC = 20003;
    //运动时同步
    final int DO_SYNC_SCENE = 20004;
    //连接
    final int DO_CONNECT = 20005;

    //开始获取实时步数
    final int DO_GET_REALTIME_STEP_START = 20101;
    //结束获取实时步数
    final int DO_GET_REALTIME_STEP_STOP = 20102;
    //开始获取实时跑步数据
    final int DO_GET_REALTIME_RUN_START = 20103;
    final int DO_GET_REALTIME_RUN_START_GPS = 20104;
    //结束获取实时跑步数据
    final int DO_GET_REALTIME_RUN_STOP = 20105;
    final int DO_GET_REALTIME_RUN_STOP_GPS = 20106;
    //获取实时跑步步行数据
    final int DO_GET_REALTIME_STRP_AND_RUN_START = 20107;
    //开始获取实时骑行数据
    final int DO_GET_REALTIME_RIDING_START = 20108;
    final int DO_GET_REALTIME_RIDING_START_GPS = 20109;
    //结束获取实时骑行数据
    final int DO_GET_REALTIME_RIDING_STOP = 20110;
    final int DO_GET_REALTIME_RIDING_STOP_GPS = 20111;


    //开始测量血压
    final int DO_TEST_BLODD_PRESSURE_START = 20301;
    //结束测量血压
    final int DO_TEST_BLODD_PRESSURE_STOP = 20302;


    //开始测量心率
    final int DO_TEST_HEART_RATE_START = 20401;
    //结束测量心率
    final int DO_TEST_HEART_RATE_STOP = 20402;

}

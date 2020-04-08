package com.rocedar.deviceplatform.db.bong;


/**
 * 项目名称：设备管理平台
 * <p>
 * 作者：phj
 * 日期：2017/6/28 下午2:47
 * 版本：V1.0.01
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public interface IBongHeartInfo {

    String BONG_TB_NAME = "bongHeartData";

    String BONG_ID = "id";
    String BONG_DATE = "date";
    String BONG_DATETIME = "date_time";
    String BONG_DEVICEID = "device_id";
    String BONG_DEVICEMAC = "device_mac";
    String BONG_HEARTINFO = "heart";


    String INITIALIZE_BONG_HEART_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
            + BONG_TB_NAME + " ("
            + BONG_ID + " INTEGER PRIMARY KEY AUTOINCREMENT , "
            + BONG_DATETIME + " LONG, "
            + BONG_DATE + " INTEGER, "
            + BONG_DEVICEID + " INTEGER, "
            + BONG_DEVICEMAC + " VARCHAR, "
            + BONG_HEARTINFO + " VARCHAR "
            + "); ";
}

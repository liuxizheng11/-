package com.rocedar.deviceplatform.db.step;

/**
 * Created by phj on 16/3/23.
 */
public interface IStepInfo {

    //表名-计步
    String TB_NAME_STEP = "StepCount";

    String STEP_INFO_ID = "_id";
    String TIMES = "times";             //时间yyyyMMddHHmmss
    String STEP = "step";             //步数
    String SENSOR_TYPE = "sensor_type";//传感器类型
    String AES_STEP = "aes_step";//加密的
    String DATE = "date";//日期yyyyMMdd
    String OTHER_ONE = "other_one";//备用字段一


}

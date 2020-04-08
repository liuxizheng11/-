package com.rocedar.deviceplatform.config;

/**
 * 项目名称：设备管理平台
 * <p>
 * 作者：phj
 * 日期：2017/1/11 下午3:06
 * 版本：V1.0
 * 描述：指标数据ID配置
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public interface RCDeviceIndicatorID {

    //步数  步数主指标
    int STEP = 4000;
    //距离
    int STEP_DISTANCE = 4026;
    //卡路里
    int STEP_KCAL = 4027;
    //走路时间
    int STEP_TIME = 4037;

    //    1001:1-室内/2-室外
    int SCENE_TYPE = 1001;

    //"4001": 跑步距离(千米两位小数)   跑步主指标
    int RUN_DISTANCE = 4001;
    //"4024": 跑步时长(分钟)
    int RUN_TIME = 4024;
    //    "4025": 跑步卡路里(千卡两位小数)
    int RUN_KCAL = 4025;
    //    "4036": 跑步步数
    int RUN_STEP = 4036;
    // "4102":跑步运动轨迹列表-分段
    int RUN_GPS_LIST = 4102;
    //4095:跑步心率明细列表
    int RUN_HEART_LIST = 4095;
    //    4120:跑步开始时间
    int RUN_START_TIME = 4120;
    //    4121:跑步结束时间
    int RUN_STOP_TIME = 4121;
    //    4101:跑步有效时长(分钟)
    int RUN_VALID_TIME = 4101;
    //    4051:跑步速度(千米/小时)
    int RUN_SPEED = 4051;
    //    4052:跑步平均心率(次/分)
    int RUN_AVERAGE_HEARTRATE = 4052;
    //    4092:跑步配速(秒)
    int RUN_PACE = 4092;
    //    4114:跑步速度明细列表
    int RUN_SPEED_LIST = 4114;
    //    4093:跑步配速明细列表
    int RUN_PACE_LIST = 4093;


    //     1001:1-室内/2-室外
    //骑行
    // 4122:骑行开始时间
    int RIDING_START_TIME = 4122;
    // 4123:骑行结束时间
    int RIDING_STOP_TIME = 4123;
    // 4002:骑行距离(千米)
    int RIDING_DISTANCE = 4002;
    // 4034:骑行时长(分钟)  骑行主指标
    int RIDING_TIME = 4034;
    // 4098:骑行有效时长(分钟)
    int RIDING_EFFECTIVE_TIME = 4098;
    // 4035:骑行卡路里(千卡)
    int RIDING_CAL = 4035;
    // 4054:骑行速度(千米/小时)
    int RIDING_SPEED = 4054;
    // 4055:骑行平均心率(次/分)
    int RIDING_AVERAGE_HEARTRATE = 4055;
    // 4096:骑行配速(秒)
    int RIDING_PACE = 4096;
    // 4115:骑行速度明细列表
    int RIDING_SPEED_LIST = 4115;
    // 4097:骑行配速明细列表
    int RIDING_PACE_LIST = 4097;
    // 4099:骑行心率明细列表
    int RIDING_HEART_LIST = 4099;
    // 4100:骑行运动轨迹列表-分段
    int RIDING_GPS_LIST = 4100;


    //4009:入睡(时间到秒)
    int SLEEP_IN = 4009;
    //4028:醒来(时间到秒)
    int SLEEP_WAKE = 4028;
    //4030:深睡(分钟)
    int SLEEP_DEEP = 4030;
    //4031:浅睡(分钟)
    int SLEEP_SHALLOW = 4031;
    //4029:睡眠时长(分钟)睡眠主指标
    int SLEEP_TIME = 4029;
    //4032:清醒时长(分钟)
    int SLEEP_SOBER_TIME = 4032;
    //4033:清醒次数(次)
    int SLEEP_SOBER_ORDER = 4033;
    //4103:入睡时长(分钟)
    int SLEEP_IN_TIME = 4103;
    //4104:环境噪音(分贝)
    int SLEEP_ENVIRONMENT_NOISE = 4104;
    //4105:做梦时长(分钟)
    int SLEEP_DREAM_TIME = 4105;
    //4106:翻身次数(次)
    int SLEEP_MOVE_NUMBER = 4106;
    //4107:睡前状态(1-饮酒/2-压力大/3-陌生床/4-吃宵夜)
    int SLEEP_BEFORE_STATUS = 4107;
    //4108:梦境状态(1-噩梦/2-美梦/3-无梦)
    int SLEEP_DREAM_STATUS = 4108;
    //4109:睡眠备注(备注)
    int SLEEP_NOTE = 4109;
    //4110:睡眠心率明细类
    int SLEEP_HEART = 4110;
    //4111:睡眠轨迹
    int SLEEP_GPS = 4111;


    // 血氧
    int Blood_Oxygen = 4011;

    //血压
    int Blood_Pressure = 4012;

    //心率
    int Heart_Rate = 4013;


    //BMI
    int BMI = 4014;
    //血脂
    int Blood_Lipid = 4015;
    //餐前血糖
    int Blood_Sugar = 4016;
    //餐后血糖
    int After_Blood_Sugar = 4088;


    /**
     * * `weight` float(5,2) DEFAULT NULL COMMENT '体重',
     * `BMI` float(5,2) unsigned DEFAULT NULL COMMENT '身体质量指数',
     * `BFP` float(5,2) unsigned DEFAULT NULL COMMENT '脂肪',
     * `TBW` float(5,2) unsigned DEFAULT NULL COMMENT '体水分',
     * `MMP` float(5,2) unsigned DEFAULT NULL COMMENT '肌肉量',
     * `BM` float(5,2) unsigned DEFAULT NULL COMMENT '骨量',
     * `VF` float(5,2) unsigned DEFAULT NULL COMMENT '内脏脂肪',
     * `BMR` float(5,2) unsigned DEFAULT NULL COMMENT '基础代谢率',
     * `bodyAge` int(3) unsigned DEFAULT NULL COMMENT '身体年龄',
     * <p>
     * "4014": "BMI",
     * "4018": "体脂肪率(%-1位小数)",
     * "4019": "骨骼量(kg-1位小数)",
     * "4020": "肌肉率(%-1位小数)",
     * "4021": "体水分率(%-1位小数)",
     * "4043": "体重(kg-1位小数)",
     * "4045": "内脏脂肪(整数)",
     * "4046": "基础代谢(千卡-整数)",
     * "4221": "身体年龄(整数)"
     */
    // "4018": "体脂(%-整数)",
    int BLOOD_BODY_FAT = 4018;
    // "4019": "骨骼量(kg-1位小数)",
    int BLOOD_BODY_BM = 4019;
    // "4020": "肌肉率(%-1位小数)",
    int BLOOD_BODY_MMP = 4020;
    // "4021": "水分率(%-整数)",
    int BLOOD_BODY_TBW = 4021;
    // "4043": "体重(kg-1位小数)",
    int BLOOD_BODY_WEIGHT = 4043;
    // "4045": "内脏脂肪(整数)",
    int BLOOD_BODY_VF = 4045;
    // "4046": "基础代谢(千卡-整数)"
    int BLOOD_BODY_BMR = 4046;
    // "4221": "身体年龄(整数)"
    int BLOOD_BODY_AGE = 4221;


    // "4041": "生物电阻(Ω)"
    int BLOOD_BODY_BR = 4041;


    //早餐
    int BreakFast = 4056;
    //午餐
    int Lunch = 4057;
    //晚餐
    int Dinner = 4058;
    //加餐
    int Snacks = 4116;
}

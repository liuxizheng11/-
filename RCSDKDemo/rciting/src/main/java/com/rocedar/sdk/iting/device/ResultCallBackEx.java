package com.rocedar.sdk.iting.device;


import cn.appscomm.bluetoothsdk.interfaces.ResultCallBack;

/**
 * 项目名称：瑰柏SDK-商城
 * <p>
 * 作者：phj
 * 日期：2019/6/30 5:26 PM
 * 版本：V1.1.00
 * 描述：瑰柏SDK-服务商品
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public interface ResultCallBackEx extends ResultCallBack{


    int TYPE_SET_WEATHER_INFO = TYPE_SEND_WEATHER_EX;

    int TYPE_GET_REMIND_COUNT = TYPE_GET_REMINDER_COUNT_EX;

    int TYPE_GET_REMIND = TYPE_GET_REMINDER_EX;

    int TYPE_SET_REMIND_ADD = TYPE_NEW_REMINDER_EX;

    int TYPE_SET_REMIND_CHANGE = TYPE_CHANGE_REMINDER_EX;

    int TYPE_SET_REMIND_DELETE_ONE = TYPE_DELETE_A_REMINDER_EX;

    int TYPE_SET_REMIND_DELETE = TYPE_DELETE_ALL_REMINDER_EX;

}

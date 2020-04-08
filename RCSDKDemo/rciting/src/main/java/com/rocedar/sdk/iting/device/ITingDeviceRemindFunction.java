package com.rocedar.sdk.iting.device;

import com.rocedar.sdk.iting.device.dto.RCITingRemindExDTO;

import cn.appscomm.bluetoothsdk.interfaces.ResultCallBack;

/**
 * 项目名称：瑰柏SDK-ITING
 * <p>
 * 作者：phj
 * 日期：2019/4/15 4:23 PM
 * 版本：V1.0.00
 * 描述：设备基本方法功能定义及方法接口
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public interface ITingDeviceRemindFunction {

    int[] REMIND_FUNCTION_TYPE = {
            ResultCallBack.TYPE_GET_REMINDER,
            ResultCallBack.TYPE_NEW_REMINDER,
            ResultCallBack.TYPE_CHANGE_REMINDER,
            ResultCallBack.TYPE_DELETE_A_REMINDER,
            ResultCallBackEx.TYPE_SET_REMIND_ADD,
            ResultCallBackEx.TYPE_SET_REMIND_CHANGE,
            ResultCallBackEx.TYPE_SET_REMIND_DELETE_ONE,
            ResultCallBackEx.TYPE_GET_REMIND,
            ResultCallBackEx.TYPE_GET_REMIND_COUNT,
            ResultCallBackEx.TYPE_SET_REMIND_DELETE
    };

    void getRemind();

    void addRemind(RCITingRemindExDTO remindDTO);

    void deleteRemind(int remindId);

    void deleteAllRemind();

    void changeRemind(int remindId, RCITingRemindExDTO newRemind);

    //返回值处理
    void parsingData(int result, Object[] objects);

    void parsingError(int result);

}

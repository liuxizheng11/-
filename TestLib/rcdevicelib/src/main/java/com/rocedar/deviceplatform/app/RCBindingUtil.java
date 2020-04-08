package com.rocedar.deviceplatform.app;

import android.app.Activity;
import android.content.Intent;

import com.rocedar.base.RCDialog;
import com.rocedar.deviceplatform.device.bluetooth.BluetoothUtil;
import com.rocedar.deviceplatform.dto.data.RCDeviceAlreadyBindDTO;
import com.rocedar.deviceplatform.request.impl.RCDeviceOperationRequestImpl;
import com.rocedar.deviceplatform.request.listener.RCDeviceAlreadyBindListener;
import com.rocedar.deviceplatform.sharedpreferences.RCSPDeviceInfo;

import java.util.List;

/**
 * 项目名称：设备管理平台
 * <p>
 * 作者：phj
 * 日期：2017/3/7 下午12:53
 * 版本：V1.0
 * 描述：设备绑定，解绑工具类 ，对应用
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public class RCBindingUtil {


    private static RCDialog rcDialog;

    public static void getBindingBluetoothDeviceList(final Activity mContext) {
        RCDeviceOperationRequestImpl.getInstance(mContext).
                queryDeviceAlreadyBindList(new RCDeviceAlreadyBindListener() {
                    @Override
                    public void getDataSuccess(List<RCDeviceAlreadyBindDTO> dtoList) {
                        RCSPDeviceInfo.saveBlueToothInfo(dtoList);
                        if (dtoList.size() > 0) {
                            // 如果本地蓝牙没有开启，则开启
                            if (!BluetoothUtil.checkBluetoothIsOpen()) {
                                BluetoothUtil.openBluetoothDialog(mContext);
                            } else {
                                Intent intent = new Intent(RCServiceUtil.BR_OPEN_GET_DATA);
                                mContext.sendBroadcast(intent);
                            }
                        }
                    }

                    @Override
                    public void getDataError(int status, String msg) {

                    }
                });
    }


    /**
     * 绑定列表中是否存在指定的设备
     *
     * @return
     */
    public static boolean isHasInListOfBindingsFromDeviceId(int deviceId) {
        return !RCSPDeviceInfo.getBlueToothMac(deviceId).equals("");
    }


}

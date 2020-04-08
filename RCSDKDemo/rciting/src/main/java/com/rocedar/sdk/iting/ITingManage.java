package com.rocedar.sdk.iting;

import android.content.Context;

import com.rocedar.sdk.iting.device.TingMessageType;

import cn.appscomm.bluetoothsdk.app.BluetoothSDK;
import cn.appscomm.ota.util.OtaAppContext;

/**
 * 项目名称：瑰柏SDK-ITING
 * <p>
 * 作者：phj
 * 日期：2019/3/8 5:31 PM
 * 版本：V1.1.00
 * 描述：瑰柏SDK-服务商品
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class ITingManage {

    public static void init(Context context) {
        BluetoothSDK.initSDK(context);
        OtaAppContext.INSTANCE.init(context);
        TingMessageType.init();
    }

    public static int TIME_TEST = 0;

}

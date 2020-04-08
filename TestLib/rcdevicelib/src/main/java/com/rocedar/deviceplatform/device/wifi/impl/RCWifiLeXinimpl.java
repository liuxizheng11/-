package com.rocedar.deviceplatform.device.wifi.impl;

import android.content.Context;

import com.lifesense.wificonfig.LSWifiConfig;
import com.lifesense.wificonfig.LSWifiConfigCode;
import com.lifesense.wificonfig.LSWifiConfigDelegate;
import com.rocedar.base.RCLog;
import com.rocedar.deviceplatform.R;
import com.rocedar.deviceplatform.device.wifi.RCWifi;
import com.rocedar.deviceplatform.device.wifi.listener.RCWifiConfigListener;
import com.rocedar.deviceplatform.device.wifi.listener.RCWifiError;

/**
 * 项目名称：设备管理平台
 * <p>
 * 作者：phj
 * 日期：2017/2/5 下午9:14
 * 版本：V1.0
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public class RCWifiLeXinimpl implements RCWifi {

    private static String TAG = "RCDevice_LXWifi";


    private static RCWifiLeXinimpl instance;

    public static RCWifiLeXinimpl getInstance(Context context) {
        if (instance == null) {
            instance = new RCWifiLeXinimpl(context.getApplicationContext());
        }
        return instance;
    }

    private Context mContext;

    public RCWifiLeXinimpl(Context context) {
        this.mContext = context;
    }


    @Override
    public void configWifi(final RCWifiConfigListener configListener, String wifiName, String password) {
        RCLog.i(TAG, "开始配置乐心Wifi：" + wifiName + "<-->" + password);
        LSWifiConfig.shared().setDelegate(new LSWifiConfigDelegate() {
            @Override
            public void wifiConfigCallBack(LSWifiConfig lsWifiConfig, LSWifiConfigCode lsWifiConfigCode) {
                RCLog.i(TAG, "配置乐心Wif收到返回值");
                if (lsWifiConfigCode != null) {
                    if (lsWifiConfigCode == LSWifiConfigCode.Success) {
                        RCLog.i(TAG, "配置乐心Wif成功");
                        configListener.configOk();
                    } else if (lsWifiConfigCode == LSWifiConfigCode.TimeOut) {
                        RCLog.i(TAG, "配置乐心Wifi超时");
                        configListener.error(RCWifiError.ERROR_CONFIG_TIME_OUT,
                                mContext.getString(R.string.rcdevice_lexin_config_time_out));
                    } else {
                        RCLog.i(TAG, "配置乐心Wifi出错"+lsWifiConfigCode);
                        configListener.error(RCWifiError.ERROR_WIFI_CONFIG,
                                mContext.getString(R.string.rcdevice_lexin_config_error));
                    }
                }
            }
        });
        LSWifiConfig.shared().startConfig(wifiName.replace("\"", ""), password);
    }
}

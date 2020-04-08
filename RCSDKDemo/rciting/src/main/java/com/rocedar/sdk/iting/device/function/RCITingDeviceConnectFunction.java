package com.rocedar.sdk.iting.device.function;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.os.Handler;

import com.rocedar.lib.base.manage.RCSDKManage;
import com.rocedar.lib.base.unit.RCLog;
import com.rocedar.lib.base.unit.RCUtilEncode;
import com.rocedar.sdk.iting.ITingManage;
import com.rocedar.sdk.iting.R;
import com.rocedar.sdk.iting.RCITingDeviceUtil;
import com.rocedar.sdk.iting.Util;
import com.rocedar.sdk.iting.device.ITingDeviceConnectFunction;
import com.rocedar.sdk.iting.device.envet.RCITingConnectEvent;
import com.rocedar.sdk.iting.device.envet.RCITingDeviceEvent;
import com.rocedar.sdk.iting.device.envet.RCITingDeviceEventType;
import com.rocedar.sdk.iting.device.listener.ITingBindListener;
import com.rocedar.sdk.iting.device.listener.ITingConnectListener;
import com.rocedar.sdk.iting.request.dto.RCITingWatchInfoDTO;

import org.greenrobot.eventbus.EventBus;

import java.util.Calendar;
import java.util.Date;

import cn.appscomm.bluetoothsdk.app.BluetoothSDK;
import cn.appscomm.bluetoothsdk.interfaces.BluetoothScanCallBack;
import cn.appscomm.bluetoothsdk.interfaces.ResultCallBack;

/**
 * 项目名称：瑰柏SDK-商城
 * <p>
 * 作者：phj
 * 日期：2019/7/25 3:16 PM
 * 版本：V1.1.00
 * 描述：瑰柏SDK-服务商品
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCITingDeviceConnectFunction implements ITingDeviceConnectFunction {
    /**
     * 设备链接逻辑
     * 1.设备第一次绑定
     * 1）扫描蓝牙设备，扫描到后链接设备
     * 2）链接完设备后，同步时间
     * 3）时间同步完成后回调链接成功方法
     * 4）APP执行后台绑定接口请求
     * 4-1）APP绑定失败，调用断开方法
     * 4-2）APP绑定成功，调用手表开始绑定方法 ，设置UID
     * 4-3）执行手表绑定成功
     * 4-4）执行拍照校时
     * 4-5）执行获取数据
     *
     * <p>
     * <p>
     * 2.设备链接
     * 1）扫描蓝牙设备，扫描到后链接设备
     * 2）链接完设备后，同步时间
     * 3）同步成功后，判断设备是否初始化
     * 3-1）如果已经初始化执行4
     * 3-2）如果没有初始化
     * 3-2-1）执行绑定开始和绑定成功方法
     * 4）开始获取数据
     */

    private String TAG = "ITING-CONNECT";

    public Context context;

    private ResultCallBack callBack;


    public RCITingDeviceConnectFunction(Context context, ResultCallBack callBack) {
        this.context = context;
        this.callBack = callBack;
    }


    @Override
    public void doConnect(RCITingWatchInfoDTO dto, ITingConnectListener connectListener) {
        this.connectListener = connectListener;
        doConnectDevice(dto);
    }


    @Override
    public void doBind(RCITingWatchInfoDTO dto, ITingBindListener bindListener) {
        this.bindListener = bindListener;
        doBindDevice(dto);
    }

    @Override
    public void disconnect() {
        BluetoothSDK.disConnect(callBack);
        isConnect = false;
        isConnecting = false;
        lastConnectDeviceDTO = null;
    }

    @Override
    public boolean isConnect() {
        if (lastConnectDeviceDTO == null) {
            return false;
        }
        return isConnect;
    }

    @Override
    public boolean isConnectIng() {
        if (!isConnect())
            return isConnecting;
        return false;
    }

    @Override
    public RCITingWatchInfoDTO getLastConnectDeviceDTO() {
        return lastConnectDeviceDTO;
    }


    /**
     * 执行第一次绑定的设置
     */
    @Override
    public void doBandWatchSetting(String uid) {
        BluetoothSDK.setUid(new ResultCallBack() {
            @Override
            public void onSuccess(int i, Object[] objects) {
                if (i == ResultCallBack.TYPE_SET_UID)
                    bindEnd();
            }

            @Override
            public void onFail(int i) {

            }
        }, RCUtilEncode.getMd5StrLower16(uid));
    }

    @Override
    public void unlockTime() {
        lastFunctionTime = new Date().getTime();
        lastFunctionName = "解锁设置时间-拍照校时开始";
        BluetoothSDK.setUnlockTimeCameraCalibration(callBack);
    }

    @Override
    public void lockTime() {
        lastFunctionTime = new Date().getTime();
        lastFunctionName = "锁定时间-拍照校时取消";
        BluetoothSDK.setLockTime(callBack);
    }

    @Override
    public void photoTimeOver() {
        //拍照校时完成
        RCITingDeviceUtil.getInstance(context).doGetWatchData();
    }

    private void startQRCodeBind() {
        lastFunctionTime = new Date().getTime();
        lastFunctionName = "开始绑定";
        BluetoothSDK.bindStartQRCode(callBack);
    }


    private void bindEnd() {
        lastFunctionTime = new Date().getTime();
        lastFunctionName = "绑定完成";
        BluetoothSDK.bindEnd(callBack);
    }

    private void checkInit() {
        lastFunctionTime = new Date().getTime();
        lastFunctionName = "检查是否init";
        BluetoothSDK.checkInit(callBack);
    }


    private String lastFunctionName = "";
    private long lastFunctionTime = -1;

    @Override
    public void parsingData(int result, Object[] objects) {
        RCLog.i(TAG, "本次请求[%s]方法开始时间：%d 结束时间：%d 总耗时：%d", lastFunctionName,
                lastFunctionTime, new Date().getTime(), lastFunctionTime - new Date().getTime());
        lastFunctionTime = -1;
        switch (result) {
            case ResultCallBack.TYPE_CONNECT:
                isConnecting = false;
                isConnect = true;
                EventBus.getDefault().post(new RCITingConnectEvent(RCITingConnectEvent.TYPE_CONNECTED));
                RCLog.i(TAG, "设备连接成功-01");
                //链接成功后同步系统的时间（不包含物理指针）
                syncTime();
                break;
            case ResultCallBack.TYPE_TRANSPARENT_PASSAGE_DATA:
                byte[] bytes = (byte[]) objects[0];
                String str = Util.byteArrayToHexString(bytes);
                RCLog.i("连接成功-获取到的结果是:" + str);
                break;
            case ResultCallBack.TYPE_DISCONNECT:
                RCLog.i("断开连接-disconnected");
                EventBus.getDefault().post(new RCITingConnectEvent(RCITingConnectEvent.TYPE_DISCONNECT));
                isConnect = false;
                isConnecting = false;
                if (connectListener != null)
                    connectListener.disConnect();
                break;
            case ResultCallBack.TYPE_SET_DEVICE_TIME:
                //时间同步成功
                if (connectIsBind) {
                    //如果是绑定，执行回调绑定成功
                    if (bindListener != null) {
                        bindListener.startBind();
                    }
                    startQRCodeBind();
                } else {
                    //判断是否已经初始化（如果用户手动恢复出厂值会停留在二维码页面），如果初始化
                    checkInit();
                }
                break;
            case ResultCallBack.TYPE_CHECK_INIT:
                RCLog.i(TAG, "check init");
                boolean b = (boolean) objects[0];
                if (b) {
                    if (connectListener != null) {
                        connectListener.onConnect();
                    }
                    RCITingDeviceUtil.getInstance(context).doGetWatchData();
                } else {
                    if (connectListener != null)
                        connectListener.notInit();
                }
                break;
            case ResultCallBack.TYPE_BIND_START_QR_CODE:
                if (!connectIsBind) {
                    bindEnd();
                }
                break;
            case ResultCallBack.TYPE_BIND_END:
                if (connectIsBind) {
                    if (bindListener != null) {
                        //绑定完成，下一步设置时间
                        bindListener.endBind();
                    }
                } else {
                    if (connectListener != null) {
                        connectListener.onConnect();
                    }
                    RCITingDeviceUtil.getInstance(context).doGetWatchData();
                }
                break;
            case ResultCallBack.TYPE_SET_UNLOCK_TIME:
                RCLog.i(TAG, "unlock time success!!!");
                EventBus.getDefault().post(new RCITingDeviceEvent
                        (RCITingDeviceEventType.SETTING_UNLOCK_TIME, true));
                break;
            case ResultCallBack.TYPE_SET_LOCK_TIME:
                RCLog.i(TAG, "lock time success!!! ");
                EventBus.getDefault().post(new RCITingDeviceEvent
                        (RCITingDeviceEventType.SETTING_LOCK_TIME, true));
                break;

        }
    }

    @Override
    public void parsingError(int result) {
        RCLog.i(TAG, "[error]本次请求[%s]方法开始时间：%d 结束时间：%d 总耗时：%d", lastFunctionName,
                lastFunctionTime, new Date().getTime(), lastFunctionTime - new Date().getTime());
        lastFunctionTime = -1;
        switch (result) {
            case ResultCallBack.TYPE_CONNECT:
                RCLog.e(TAG, "设备-TYPE_CONNECT");
                if (connectIsBind) {
                    if (bindListener != null) {
                        bindListener.onError(-2, context.getString(R.string.rc_iting_connect_error));
                    }
                } else {
                    if (connectListener != null) {
                        connectListener.onError(-2, context.getString(R.string.rc_iting_connect_error));
                    }
                }
                EventBus.getDefault().post(new RCITingConnectEvent(RCITingConnectEvent.TYPE_DISCONNECT));
                isConnect = false;
                break;
            case ResultCallBack.TYPE_TRANSPARENT_PASSAGE_DATA:
                RCLog.e(TAG, "设备-TYPE_TRANSPARENT_PASSAGE_DATA");
                break;
            case ResultCallBack.TYPE_DISCONNECT:
                RCLog.i("断开连接-disconnected");
                EventBus.getDefault().post(new RCITingConnectEvent(RCITingConnectEvent.TYPE_DISCONNECT));
                isConnect = false;
                break;
            case ResultCallBack.TYPE_SET_DEVICE_TIME:

                break;
            case ResultCallBack.TYPE_CHECK_INIT:

                break;
            case ResultCallBack.TYPE_BIND_START_QR_CODE:

                break;
            case ResultCallBack.TYPE_BIND_END:

                break;
        }
    }

    //是否连接
    private boolean isConnect = false;
    //是否是绑定设备 true为绑定，false为非第一次绑定
    private boolean connectIsBind = false;
    //是否正在连接设备
    private boolean isConnecting = false;
    //设备链接监听，用于app服务中
    private ITingConnectListener connectListener;
    //设备绑定监听，用于app扫描绑定
    private ITingBindListener bindListener;
    //
    private String uid = "";

    //最后连接的设备信息
    private RCITingWatchInfoDTO lastConnectDeviceDTO;


    private void doBindDevice(RCITingWatchInfoDTO dto) {
        connectIsBind = true;
        if (isConnect) {
            disconnect();
        }
        lastConnectDeviceDTO = dto;
        isConnecting = true;
        if (bindListener != null) {
            bindListener.startConnect();
        }
        doScan();
    }

    private void doConnectDevice(final RCITingWatchInfoDTO dto) {
        if (isConnecting) {
            RCLog.w(TAG, "设备正在连接中");
            return;
        }
        connectIsBind = false;
        EventBus.getDefault().post(new RCITingConnectEvent(RCITingConnectEvent.TYPE_CONNECTTING));
        if (isConnect) {
            //如果当前已经链接并且链接的设备和需要链接的设备一致，返回链接成功，如果不一致则断开链接后重新链接
            if (lastConnectDeviceDTO != null && lastConnectDeviceDTO.getDeviceNo().toUpperCase().equals(dto.getDeviceNo().toUpperCase())) {
                if (connectListener != null) {
                    connectListener.onConnect();
                }
            } else {
                disconnect();
            }
        }
        lastConnectDeviceDTO = dto;
        isConnecting = true;
        if (connectListener != null) {
            connectListener.startConnect();
        }
        doScan();
    }


    private void doScan() {
        BluetoothSDK.startScan(scanCallBack, "");
        number = 60;
        handler.post(runnable);
    }


    public BluetoothScanCallBack scanCallBack = new BluetoothScanCallBack() {
        @Override
        public void onLeScan(BluetoothDevice bluetoothDevice, int i) {
            if (lastConnectDeviceDTO == null) {
                isConnecting = false;
                RCLog.w(TAG, "没有要连接的设备");
                return;
            }
            boolean tempBoolean1 = bluetoothDevice.getAddress().trim().toUpperCase().equals(lastConnectDeviceDTO.getDeviceNo().toUpperCase());
            boolean tempBoolean2 = bluetoothDevice.getName().trim().toUpperCase().equals(lastConnectDeviceDTO.getDeviceNo().toUpperCase());
            RCLog.i(TAG, "扫描到的设备信息为：%s[%s];\\r\\t需要配对的设备信息为：%s;\\r\\t;比较结果为：%b[%b]",
                    bluetoothDevice.getAddress(), bluetoothDevice.getName(), lastConnectDeviceDTO.getDeviceNo(), tempBoolean1, tempBoolean2);
            if (bluetoothDevice.getAddress().trim().toUpperCase().equals(lastConnectDeviceDTO.getDeviceNo().toUpperCase())
                    || bluetoothDevice.getName().trim().toUpperCase().equals(lastConnectDeviceDTO.getDeviceNo().toUpperCase())) {
                lastConnectDeviceDTO.setDeviceNo(bluetoothDevice.getName());
                lastConnectDeviceDTO.setDeviceMac(bluetoothDevice.getAddress());
                isConnecting = false;
                //扫描到设备，停止扫描
                BluetoothSDK.stopScan();
                //开始连接
                BluetoothSDK.connectByMAC(callBack, bluetoothDevice.getAddress());
            }
            return;
        }

        @Override
        public void onStopScan(boolean b) {
            if (isConnecting) {
                if (connectIsBind) {
                    if (bindListener != null)
                        bindListener.onError(-1, context.getString(R.string.rc_iting_connect_timeout));
                } else {
                    if (connectListener != null)
                        connectListener.onError(-1, context.getString(R.string.rc_iting_connect_timeout));
                }
                number = 0;
                isConnecting = false;
                RCLog.e(TAG, "设备链接超时+stopscan");
            }
        }
    };

    private Handler handler = new Handler();

    private int number = 60;

    /**
     * 链接计时器，60s未链接为超时
     */
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (!isConnecting) {
                RCLog.i(TAG, "设备不是正在链接中");
                return;
            }
            RCLog.i(TAG, "设备正在链接中，超时计时器：" + number);
            if (number > 0) {
                number -= 2;
                handler.postDelayed(runnable, 2000);
            }
            if (number <= 0) {
                isConnecting = false;
                if (connectIsBind) {
                    if (bindListener != null)
                        bindListener.onError(-1, context.getString(R.string.rc_iting_connect_timeout));
                } else {
                    if (connectListener != null)
                        connectListener.onError(-1, context.getString(R.string.rc_iting_connect_timeout));
                }
                RCLog.e(TAG, "设备链接超时+runnable");
                BluetoothSDK.stopScan();
            }
        }
    };

    /**
     * 同步系统时间（非物理指针时间）
     */
    private void syncTime() {
        Calendar calendar = Calendar.getInstance();
        if (RCSDKManage.getDebug()) {
            calendar.add(Calendar.HOUR_OF_DAY, ITingManage.TIME_TEST);
        }
        RCLog.i("设置的时间是：" + (calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE)));
        BluetoothSDK.setDeviceTime(callBack, calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH),
                calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),
                calendar.get(Calendar.SECOND));
    }
}

package com.rocedar.deviceplatform.device.bluetooth.impl.bong;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;

import com.ginshell.ble.BLEInitCallback;
import com.ginshell.ble.GattState;
import com.ginshell.sdk.Bong;
import com.ginshell.sdk.BongManager;
import com.ginshell.sdk.ResultCallback;
import com.ginshell.sdk.command.OneResultCallback;
import com.rocedar.base.RCHandler;
import com.rocedar.base.RCLog;
import com.rocedar.base.RCToast;
import com.rocedar.base.shareprefernces.RCSPBaseInfo;
import com.rocedar.deviceplatform.R;
import com.rocedar.deviceplatform.app.RCUploadDevceData;
import com.rocedar.deviceplatform.config.RCBluetoothDoType;
import com.rocedar.deviceplatform.config.RCDeviceDeviceID;
import com.rocedar.deviceplatform.device.bluetooth.listener.RCBluetoothError;
import com.rocedar.deviceplatform.device.bluetooth.listener.RCBluetoothGetDataListener;
import com.rocedar.deviceplatform.dto.device.RCDeviceFitDataDTO;
import com.rocedar.deviceplatform.request.listener.RCRequestSuccessListener;
import com.rocedar.deviceplatform.sharedpreferences.RCSPDeviceSaveTime;

import org.json.JSONArray;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.ginshell.sdk.common.Constant;
import cn.ginshell.sdk.model.BongFit;

import static com.rocedar.deviceplatform.config.RCBluetoothDoType.DO_BINDING;

/**
 * 项目名称：DongYa3.0
 * <p>
 * 作者：phj
 * 日期：2017/9/26 下午5:12
 * 版本：V2.2.00
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public class BongFitUtil {

    private final static String TAG = "BongFitUtil";


    private Context mContext;

    private RCHandler handler;


    public BongFitUtil(Context mContext) {
        this.mContext = mContext;
        handler = new RCHandler(mContext);
        getDataListener = new HashMap<>();
    }

    /*手环支持的指令集*/
    private int[] supportInstructType = {
            //绑定
            RCBluetoothDoType.DO_BINDING, RCBluetoothDoType.DO_CONNECT, RCBluetoothDoType.DO_SYNC
    };

    /**
     * 检测是否支持指定的指令
     *
     * @param t 需要检测的指令
     * @return
     */
    private boolean binarySearch(int[] supportInstructType, int t) {
        for (int i = 0; i < supportInstructType.length; i++) {
            if (supportInstructType[i] == t) {
                return true;
            }
        }
        return false;
    }


    //设备连接状态存储
    private boolean isConnect = false;

    public boolean isConnect() {
        return isConnect;
    }


    private Bong mBong;
    private BongManager mBongManager;

    /**
     * 初始化BONGSDK
     */
    private void initBong() {
        if (mBong != null) {
            return;
        }
        mBong = new Bong((Application) mContext);
        LocalBroadcastManager.getInstance(mContext).
                registerReceiver(mBleStateReceiver, new IntentFilter(GattState.BLE_STATE_CHANGE));
    }


    //连接尝试次数
    private int connectNumber = 0;

    //最后连接的设备MAC地址
    private String lastConnectMac = "";
    //最后执行的指令
    private int lastInstruct = -1;


    private Map<Integer, RCBluetoothGetDataListener> getDataListener;

    /**
     * 执行指令第一步，判断连接情况
     *
     * @param mac
     * @param instructType
     */
    public void doInstructTypeFirst(String mac, int instructType, RCBluetoothGetDataListener getDataListener) {
        //判断时候支持该协议
        if (!binarySearch(supportInstructType, instructType)) {
            RCLog.i(TAG, "BONGFit不支持该协议，" + instructType);
            getDataListener.getDataError(RCBluetoothError.ERROR_PHONE_NOT_SUPPORT,
                    mContext.getString(R.string.rcdevice_error_not_support));
            return;
        }
        getDataListener.getDataStart();
        this.lastInstruct = instructType;
        this.getDataListener.put(instructType, getDataListener);
        //如果设备没有连接或上次连接的设备mac地址和本次不一致，执行连接设备
        //如果之前已经连接设备，判断连接的设备和获取数据的设备是否是同一个
        if (!isConnect || (!lastConnectMac.equals("") && !mac.equals(lastConnectMac))) {
            RCLog.i(TAG, "BONGFIT（%s）设备没有连接或mac地址变动,上一次连接的设备为%s", mac, lastConnectMac);
            isConnect = false;
            doConnect(mac);
        } else {
            if (instructType == RCBluetoothDoType.DO_BINDING) {
                getDataListener.dataInfo(new JSONArray());
            } else {
                readHistoryData();
            }
        }
    }


    /**
     * 连接设备
     *
     * @param mCurMac 连接的设备ID
     */
    private void doConnect(String mCurMac) {
        //赋值最后连接的设备mac地址
        lastConnectMac = mCurMac;
        //初始化Bong
        initBong();
        //连接设备前先断开连接
        unConnect("执行连接设备，当前连接状态为：" + isConnect);
        //连接设备
        mBong.connect(mCurMac, new BLEInitCallback() {
            @Override
            public void onSuccess() {
                RCToast.TestCenter(mContext, "连接设备开始成功");
            }

            @Override
            public boolean onFailure(int i) {
                RCToast.TestCenter(mContext, "连接设备失败" + i);
                return false;
            }
        });
        //尝试次数,用于多次连接时提示
        connectNumber++;
        RCToast.TestCenter(mContext, "连接设备第" + connectNumber + "次," + mCurMac);
        //如果是绑定设备，添加超时
        if (lastInstruct == DO_BINDING) {
            handler.postDelayed(connectRunnable, 30 * 1000);
        }
    }

    /**
     * 连接设备超时处理
     */
    private Runnable connectRunnable = new Runnable() {
        @Override
        public void run() {
            //如果没有连接成功，
            if (connectNumber != 0 && !isConnect) {
                bindingError();
            }
        }
    };

    /**
     * 绑定出错
     */
    private void bindingError() {
        handler.removeCallbacks(connectRunnable);
        if (getDataListener.containsKey(DO_BINDING)) {
            if (connectNumber > 2) {
                getDataListener.get(DO_BINDING).getDataError(
                        RCBluetoothError.ERROR_CONNECT_MORE, "多次连接失败，请尝试重启蓝牙或重启应用后再尝试连接"
                );
            } else
                getDataListener.get(DO_BINDING).getDataError(
                        RCBluetoothError.ERROR_CONNECT, "设备连接失败，请确认设备是否在附近后重试。"
                );
            getDataListener.remove(DO_BINDING);
        }
    }


    /**
     * 连接状态会以广播的形式通知出来
     * 蓝牙连接state
     */
    private BroadcastReceiver mBleStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String state = intent.getStringExtra("state");
            Log.i(TAG, "onReceive: state = " + state);
            if (TextUtils.equals(state, "CONNECTED")) {
                //设备连接成功
                connectNumber = 0;
                //标记连接状态为已连接
                if (!lastConnectMac.equals(""))
                    isConnect = true;
                if (lastInstruct == DO_BINDING && getDataListener.containsKey(DO_BINDING)) {
                    getDataListener.get(DO_BINDING).dataInfo(new JSONArray());
                    getDataListener.remove(DO_BINDING);
                }
                RCToast.TestCenter(mContext, "bong fit 连接(" + lastConnectMac + ")成功");
                mBongManager = mBong.fetchBongManager();
                //连接成功后同步时间
                mBongManager.syncBongFitTime(new ResultCallback() {
                    @Override
                    public void finished() {
                        //时间同步成功后，添加用户
                        add_user();
                    }

                    @Override
                    public void onError(Throwable throwable) {

                    }
                });
            } else if (TextUtils.equals(state, "DISCONNECTING")) {
                unConnect("收到断开连接的广播，当前连接状态为：" + isConnect);
                if (!lastConnectMac.equals(""))
                    isConnect = false;
                if (lastInstruct == RCBluetoothDoType.DO_BINDING) {
                    bindingError();
                } else {
                    if (getDataListener.containsKey(lastInstruct))
                        getDataListener.get(lastInstruct).getDataError(
                                RCBluetoothError.ERROR_CONNECT, "设备连接失败，请确认设备是否在附近后重试。"
                        );
                }
                RCToast.TestCenter(mContext, "设备(" + lastConnectMac + ")断开连接");
            }
        }
    };


    /**
     * 添加主账号的某个成员
     */
    public void add_user() {
        /**
         * accountId 账户id
         * userId 用户id 0~15 每个主账号最多能添加15个子用户
         *  sex:性别   1:girl, 0:boy
         *  age:年龄  比如22
         *  height:身高 比如178(cm)
         * weight:KG
         * 返回值:0代表成功,1代表defeat
         */
        mBongManager.addFitUser(mAccountId, mUserId,
                RCSPBaseInfo.getLastUserBaseInfoSex() == 0 ? 1 : 0,//sex
                RCSPBaseInfo.getLastUserBaseInfoAge(),
                RCSPBaseInfo.getLastUserBaseInfoStature(),
                RCSPBaseInfo.getLastUserBaseInfoWeight(), new OneResultCallback<Integer>() {
                    @Override
                    public void onResultValue(Integer value) {
                        RCLog.i(TAG, "性别：%s；年龄：%d；身高：%d；体重：%d"
                                , RCSPBaseInfo.getLastUserBaseInfoSex() == 0 ? "女" : "男"
                                , RCSPBaseInfo.getLastUserBaseInfoAge()
                                , RCSPBaseInfo.getLastUserBaseInfoStature()
                                , RCSPBaseInfo.getLastUserBaseInfoWeight());
                        if (value == 0) {
                            RCToast.TestCenter(mContext, "添加成员success");
                        } else {
                            RCToast.TestCenter(mContext, "添加成员defeat");
                        }
                        //用户添加成功后获取历史数据开启广播
                        readHistoryData();
                        LocalBroadcastManager.getInstance(mContext)
                                .registerReceiver(mBongFitReceiver, new IntentFilter(Constant.BONG_FIT));
                    }


                    @Override
                    public void finished() {

                    }

                    @Override
                    public void onError(Throwable t) {

                    }
                });
    }


    private BongFit lastData = null;


    /**
     * 体重测量的各种数据将已广播的形式通知出来
     */
    private BroadcastReceiver mBongFitReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            BongFit bongFitModel = (BongFit) intent.getSerializableExtra(Constant.BONG_FIT_VALUE);
            Log.i(TAG, "onReceive: ..state = " + bongFitModel.getStatus() + ", weight = " + bongFitModel.getWeight());
            if (bongFitModel.getWeight() > 0 && bongFitModel.getStatus() == 1) {
                Log.i(TAG, "bongFit:" + bongFitModel.toString());
                RCToast.TestCenter(mContext, bongFitModel.toString());
                //如果不想该条体重数据在历史数据中还存在,则发送该命令即可,不发生默认为记录
//                mBongManager.ignoreThisFitRecord(new ResultCallbackX());
                if (lastData == null) {
                    lastData = bongFitModel;
                } else {
                    if (lastData.getWeight() == bongFitModel.getWeight() &&
                            lastData.getBfp() == bongFitModel.getBfp() &&
                            lastData.getBmr() == bongFitModel.getBmr() &&
                            lastData.getMmp() == bongFitModel.getMmp()) {
                        return;
                    }
                    lastData = bongFitModel;
                }
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
                final RCDeviceFitDataDTO fitDataDTO = new RCDeviceFitDataDTO();
                fitDataDTO.setBm(bongFitModel.getBm());
                fitDataDTO.setBmr(bongFitModel.getBmr());
                fitDataDTO.setBodyAge(bongFitModel.getBodyAge());
                fitDataDTO.setDate(dateFormat.format(new Date(bongFitModel.getDataTime())));
                fitDataDTO.setDeviceId(RCDeviceDeviceID.BONG_FIT);
                fitDataDTO.setFat(bongFitModel.getBfp());
                fitDataDTO.setMmp(bongFitModel.getMmp());
                fitDataDTO.setTbw((int) bongFitModel.getTbw());
                fitDataDTO.setVf((int) bongFitModel.getVf());
                fitDataDTO.setWeight(bongFitModel.getWeight());
                //保存数据到上传队列
                RCUploadDevceData.postDeviceData(mContext, new JSONArray().put(fitDataDTO.getJSON()),
                        new RCRequestSuccessListener() {
                            @Override
                            public void requestSuccess() {
                                RCToast.TestCenter(mContext, "数据上传成功：" + fitDataDTO.getJSON());
                            }

                            @Override
                            public void requestError(int status, String msg) {

                            }
                        });
            }
        }
    };


    private int mAccountId = 1000;//主账号测试默认值id
    private int mUserId = 0;//成员id号  0~15 最多16个


    /**
     * 读取历史体重数据
     */
    public void readHistoryData() {
        /**
         * 参数1:accountId
         * 参数2:开始时间
         * 参数3:结束时间
         */
        long endTime = System.currentTimeMillis();
        long startTime = getLastSycnTime();
        /**
         * 比如 取最近7天的数据
         */
        mBongManager.readFitHistoryData(mAccountId, startTime, endTime, new OneResultCallback<List<BongFit>>() {

            @Override
            public void onResultValue(List<BongFit> value) {
                if (value == null) {
                    RCToast.TestCenter(mContext, "历史数据为空");
                } else {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
                    JSONArray array = new JSONArray();
                    for (int i = 0; i < value.size(); i++) {
                        RCDeviceFitDataDTO fitDataDTO = new RCDeviceFitDataDTO();
                        fitDataDTO.setBm(value.get(i).getBm());
                        fitDataDTO.setBmr(value.get(i).getBmr());
                        fitDataDTO.setBodyAge(value.get(i).getBodyAge());
                        fitDataDTO.setDate(dateFormat.format(new Date(value.get(i).getDataTime())));
                        fitDataDTO.setDeviceId(RCDeviceDeviceID.BONG_FIT);
                        fitDataDTO.setFat(value.get(i).getBfp());
                        fitDataDTO.setMmp(value.get(i).getMmp());
                        fitDataDTO.setTbw((int) value.get(i).getTbw());
                        fitDataDTO.setVf((int) value.get(i).getVf());
                        fitDataDTO.setWeight(value.get(i).getWeight());
                        array.put(fitDataDTO.getJSON());
                    }
                    //保存数据到上传队列
                    RCUploadDevceData.saveBlueDeviceData(array);
                    RCLog.i(TAG, "获取的历史数据为：" + array.toString());
                    //保存最后上传数据时间
                    saveLastSycnTime();
                }
                if (lastInstruct != RCBluetoothDoType.DO_BINDING) {
                    getDataListener.get(lastInstruct).dataInfo(new JSONArray());
                }
            }

            @Override
            public void finished() {

            }

            @Override
            public void onError(Throwable t) {

            }
        });
    }


    /**
     * 断开连接
     */
    public void unConnect(String form) {
        RCToast.TestCenter(mContext, "执行断开设备操作:" + form);
        if (mBong != null)
            mBong.disconnect();
        try {
            LocalBroadcastManager.getInstance(mContext).unregisterReceiver(mBongFitReceiver);//移除对fit的注册
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 保存最后同步数据时间
     */
    private void saveLastSycnTime() {
        SharedPreferences.Editor editor = RCSPDeviceSaveTime.getSharedPreferencesEditor();
        editor.putLong(RCSPDeviceSaveTime.getMd5String(RCDeviceDeviceID.BONG_FIT + " last sync time ", lastConnectMac)
                , System.currentTimeMillis());
        editor.commit();
    }

    /**
     * 读取最后存储数据的时间，如果没有则取七天前的时间
     *
     * @return
     */
    private long getLastSycnTime() {
        long time = RCSPDeviceSaveTime.getSharedPreferences().getLong(
                RCSPDeviceSaveTime.getMd5String(RCDeviceDeviceID.BONG_FIT + " last sync time ", lastConnectMac), -1
        );
        if (time > 0) return time;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.DAY_OF_MONTH, -7);
        return calendar.getTimeInMillis();
    }


}

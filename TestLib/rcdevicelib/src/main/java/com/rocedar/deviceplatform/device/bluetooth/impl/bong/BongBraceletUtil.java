package com.rocedar.deviceplatform.device.bluetooth.impl.bong;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.View;

import com.ginshell.ble.BLEInitCallback;
import com.ginshell.ble.GattState;
import com.ginshell.ble.x.request.XResponse;
import com.ginshell.ble.x.request.XWriteRequest;
import com.ginshell.sdk.Bong;
import com.ginshell.sdk.BongManager;
import com.ginshell.sdk.ResultCallback;
import com.ginshell.sdk.command.HeartCallback;
import com.rocedar.base.RCBaseManage;
import com.rocedar.base.RCDialog;
import com.rocedar.base.RCHandler;
import com.rocedar.base.RCLog;
import com.rocedar.base.RCToast;
import com.rocedar.deviceplatform.R;
import com.rocedar.deviceplatform.app.scene.RCSceneUtil;
import com.rocedar.deviceplatform.app.scene.SceneSPInfo;
import com.rocedar.deviceplatform.config.RCBluetoothDataType;
import com.rocedar.deviceplatform.config.RCBluetoothDoType;
import com.rocedar.deviceplatform.config.RCDeviceDeviceID;
import com.rocedar.deviceplatform.device.bluetooth.SPDeviceDataBong;
import com.rocedar.deviceplatform.device.bluetooth.impl.RCBluetoothBongImpl;
import com.rocedar.deviceplatform.device.bluetooth.listener.RCBluetoothError;
import com.rocedar.deviceplatform.device.bluetooth.listener.RCBluetoothGetDataListener;
import com.rocedar.deviceplatform.dto.device.RCDeviceHeartRateDataDTO;
import com.rocedar.deviceplatform.unit.DateUtil;

import org.json.JSONArray;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import cn.ginshell.sdk.BongSdk;
import cn.ginshell.sdk.CommandApi;
import cn.ginshell.sdk.model.SportType;


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

public class BongBraceletUtil {

    private String TAG = "BongBraceletUtil";


    private RCBluetoothBongImpl rcBluetoothBong;

    private Context mContext;

    private RCHandler handler;

    //设备ID
    private int mDeviceId;

    public BongBraceletUtil(RCBluetoothBongImpl rcBluetoothBong, Context mContext, int mDeviceId) {
        this.rcBluetoothBong = rcBluetoothBong;
        this.mContext = mContext;
        this.mDeviceId = mDeviceId;
        handler = new RCHandler(mContext);
    }

    /*手环支持的指令集*/
    private int[] supportInstructType = {
            //绑定，同步，场景同步
            RCBluetoothDoType.DO_BINDING, RCBluetoothDoType.DO_SYNC, RCBluetoothDoType.DO_SYNC_SCENE,
            //开始室内跑，开始室外跑
            RCBluetoothDoType.DO_GET_REALTIME_RUN_START, RCBluetoothDoType.DO_GET_REALTIME_RUN_START_GPS,
            //开始室内骑行，开始室外骑行
            RCBluetoothDoType.DO_GET_REALTIME_RIDING_START, RCBluetoothDoType.DO_GET_REALTIME_RIDING_START_GPS,
            //结束室内跑，结束室外跑
            RCBluetoothDoType.DO_GET_REALTIME_RUN_STOP, RCBluetoothDoType.DO_GET_REALTIME_RUN_STOP_GPS,
            //结束室内骑行，结束室外骑行
            RCBluetoothDoType.DO_GET_REALTIME_RIDING_STOP, RCBluetoothDoType.DO_GET_REALTIME_RIDING_STOP_GPS,
            //开始实时心率获取，结束实时心率获取
            RCBluetoothDoType.DO_TEST_HEART_RATE_START, RCBluetoothDoType.DO_TEST_HEART_RATE_STOP,
            //获取今天步数
            RCBluetoothDataType.DATATYPE_STEP_TODAY
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


    //是否正在同步
    private boolean doSnycIn;

    //最后执行的指令
    private int lastInstruct = -1;

    //设备连接状态存储
    private boolean isConnect = false;

    public boolean isConnect() {
        return isConnect;
    }

    //最后连接的设备MAC地址
    private String lastConnectMac = "";

    //数据监听<指令ID，监听>
    private Map<Integer, RCBluetoothGetDataListener> mGetDataListenerMap = new HashMap<>();


    /**
     * 执行指令第一步，判断连接情况
     *
     * @param mac
     * @param instructType
     */
    public void doInstructTypeFirst(String mac, int instructType, RCBluetoothGetDataListener getDataListener) {
        //判断时候支持该协议
        if (!binarySearch(supportInstructType, instructType)) {
            RCLog.i(TAG, "BONG不支持该协议，" + instructType);
            getDataListener.getDataError(RCBluetoothError.ERROR_PHONE_NOT_SUPPORT,
                    mContext.getString(R.string.rcdevice_error_not_support));
            return;
        }
        mGetDataListenerMap.put(instructType, getDataListener);
        getDataListener.getDataStart();
        this.lastInstruct = instructType;
        //如果设备没有连接或上次连接的设备mac地址和本次不一致，执行连接设备
        //如果之前已经连接设备，判断连接的设备和获取数据的设备是否是同一个
        if (!isConnect || (!lastConnectMac.equals("") && !mac.equals(lastConnectMac))) {
            RCLog.i(TAG, "BONG（%s）设备没有连接或mac地址变动,上一次连接的设备为%s", mac, lastConnectMac);
            isConnect = false;
            doConnect(mac);
        } else {
            RCLog.i(TAG, "BONG（%s）设备已经连接", mac);
            //如果是运动模式，不用判断是否正在同步（运动模式下，线程中不取数据
            if (isSceneDoing) {
                //运动模式下,直接获取对应数据（）
                if (new Date().getTime() - SPDeviceDataBong.getSyncTime(mac) > 10 * 1000) {
                    syncData(instructType);
                } else {
                    doInstructTypeFunction(instructType);
                }
            } else {
                //f非运动模式下，先判断是否正在同步数据
                if (doSnycIn) {
                    if (mGetDataListenerMap.containsKey(instructType)) {
                        RCLog.i(TAG, "判断是否正在同步，正在同步数据需要等待");
                        mGetDataListenerMap.get(instructType).getDataError(RCBluetoothError.ERROR_DEVICE_BUSY, "正在同步数据");
                        return;
                    }
                }
                RCLog.i(TAG, "上一次同步数据的时间为:" + SPDeviceDataBong.getSyncTime(mac));
                if (SPDeviceDataBong.getSyncTime(mac) < 0) {
                    syncData(instructType);
                } else {
                    //1分钟同步一次
                    if (new Date().getTime() - SPDeviceDataBong.getSyncTime(mac) > 1 * 60 * 1000) {
                        RCLog.i(TAG, "上一次同步数据的时间大于1分钟，再次同步");
                        syncData(instructType);
                    } else {
                        RCLog.i(TAG, "上一次同步数据的时间小于1分钟，不同步数据");
                        doInstructTypeFunction(instructType);
                    }
                }
            }
        }
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
        if (lastInstruct == RCBluetoothDoType.DO_BINDING) {
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
        if (mGetDataListenerMap.get(lastInstruct) != null) {
            if (connectNumber > 2) {
                mGetDataListenerMap.get(lastInstruct).getDataError(
                        RCBluetoothError.ERROR_CONNECT_MORE, "多次连接失败，请尝试重启蓝牙或重启应用后再尝试连接"
                );
            } else
                mGetDataListenerMap.get(lastInstruct).getDataError(
                        RCBluetoothError.ERROR_CONNECT, "设备连接失败，请确认设备是否在附近后重试。"
                );

        }
    }


    /**
     * 断开连接
     */
    public void unConnect(String form) {
        RCToast.TestCenter(mContext, "执行断开设备操作:" + form);
        if (mBong != null)
            mBong.disconnect();
    }


    /*bong手环数据处理工具类*/
    private BongUtilDeviceData mBongUtilDeviceData;

    public BongUtilDeviceData getmBongUtilDeviceData() {
        return mBongUtilDeviceData;
    }


    /**
     * 连接状态会以广播的形式通知出来
     * 蓝牙连接state
     */
    private BroadcastReceiver mBleStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String state = intent.getStringExtra("state");
            RCLog.i(TAG, "连接设备状态：" + state);
            if (TextUtils.equals(state, "CONNECTED")) {
                //设备连接成功
                connectNumber = 0;
                //标记连接状态为已连接
                if (!lastConnectMac.equals(""))
                    isConnect = true;
                mBongManager = mBong.fetchBongManager();
                //手环数据处理工具类
                if (mBongUtilDeviceData == null)
                    mBongUtilDeviceData = new BongUtilDeviceData(mContext,
                            mBongManager, mDeviceId);
                RCToast.TestCenter(mContext, "连接(" + lastConnectMac + ")成功");
                //如果执行的是绑定指令，进行绑定时提示。
                if (lastInstruct == RCBluetoothDoType.DO_BINDING) {
                    bindingVibrate(lastInstruct);
                } else {
                    mBongManager.syncBongTime(new ResultCallback() {
                        @Override
                        public void finished() {
                            syncData(lastInstruct);
                        }

                        @Override
                        public void onError(Throwable throwable) {

                        }
                    });
                }
            } else if (TextUtils.equals(state, "DISCONNECTING")) {
                unConnect("收到断开连接的广播，当前连接状态为：" + isConnect);
                if (!lastConnectMac.equals(""))
                    isConnect = false;
                if (lastInstruct == RCBluetoothDoType.DO_BINDING) {
                    bindingError();
                }
                if (lastInstruct == RCBluetoothDoType.DO_SYNC) {
                    mGetDataListenerMap.get(lastInstruct).getDataError(
                            RCBluetoothError.ERROR_CONNECT, "设备连接失败，请确认设备是否在附近后重试。"
                    );
                }
                if (lastInstruct == RCBluetoothDoType.DO_SYNC_SCENE) {
                    mGetDataListenerMap.get(lastInstruct).getDataError(
                            RCBluetoothError.ERROR_CONNECT, "设备连接失败，请确认设备是否在附近后重试。"
                    );
                }
                RCToast.TestCenter(mContext, "设备(" + lastConnectMac + ")断开连接");
            }
        }
    };


    private RCDialog dialog;

    /**
     * 绑定时提示
     * 1.bong2PH绑定时震动
     * 2.bong4绑定时亮屏
     */
    private void bindingVibrate(final int lastInstructType) {
        String str = "";
        if (mDeviceId == RCDeviceDeviceID.BONG_2PH) {
            /**
             * 命令 bong 系列产品 通用
             */
            byte[] cmd = CommandApi.encodeVibrate(6);
            //类型一  案例：震动两次
            mBong.getBleManager().addRequest(new XWriteRequest(cmd, new XResponse() {
                @Override
                public void onError(Exception e) {
                    RCToast.TestCenter(mContext, "震动出错");
                }

                @Override
                public void onCommandSuccess() {
                    RCToast.TestCenter(mContext, "震动完成");

                }
            }));
            str = "请确认设备是否震动";
        } else {
            str = "请确认设备是否被点亮";
            mBongManager.showBindStart(new ResultCallback() {
                @Override
                public void finished() {

                }

                @Override
                public void onError(Throwable throwable) {

                }
            });
        }
        handler.sendMessage(RCHandler.GETDATA_OK);
        dialog = new RCDialog(RCBaseManage.getScreenManger().currentActivity(),
                new String[]{null, str, "没有", "有"}, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                unConnect("绑定错误");
                if (mGetDataListenerMap.containsKey(lastInstructType)) {
                    mGetDataListenerMap.get(lastInstructType).
                            getDataError(RCBluetoothError.ERROR_DEVICE_CHOOSE, "请重新选择设备。");
                }
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (mGetDataListenerMap.containsKey(lastInstructType)) {
                    mGetDataListenerMap.get(lastInstructType).dataInfo(new JSONArray());
                }
                mBongManager.showBindSuccess(new ResultCallback() {
                    @Override
                    public void finished() {
                        //绑定成功，同步数据
                        mBongManager.syncBongTime(new ResultCallback() {
                            @Override
                            public void finished() {
                                syncData(lastInstructType);
                            }

                            @Override
                            public void onError(Throwable throwable) {

                            }
                        });
                    }

                    @Override
                    public void onError(Throwable throwable) {

                    }
                });
            }
        });
        dialog.setmAllowedcancel(false);
        dialog.show();
    }


    /**
     * 同步手环数据
     */
    private void syncData(final int lastInstructType) {
        //判断上次同步数据的设备是不是和本次一致，如果不一致，清空数据库
        if (!SPDeviceDataBong.getLastConnectDeviceMac().equals("")) {
            if (!lastConnectMac.equals(SPDeviceDataBong.getLastConnectDeviceMac())) {
                BongSdk.clearDatabase();
            }
        }
        SPDeviceDataBong.saveLastConnectDeviceMac(lastConnectMac);
        doSnycIn = true;
        ResultCallback resultCallback = new ResultCallback() {
            @Override
            public void finished() {
                RCLog.i(TAG, "同步数据完成:" + lastInstructType);
                //同步数据完成
                doSnycIn = false;
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        mBongUtilDeviceData.syncOver(lastConnectMac, new BongUtilDeviceData.ParseDataListener() {
                            @Override
                            public void over() {
                                RCLog.i(TAG, "同步数据解析完成:" + lastInstructType);
                                SPDeviceDataBong.saveSyncTime(new Date().getTime(), lastConnectMac);
                                //同步数据完成后，执行对应的指令
                                doInstructTypeFunction(lastInstructType);
                            }
                        });
                    }
                });
            }

            @Override
            public void onError(Throwable t) {
                //同步数据出错
                doSnycIn = false;
            }
        };
        mBongManager.syncAuto(resultCallback);
    }


    //是否正在跑步或骑行
    private boolean isSceneDoing = false;

    /**
     * 执行指令对应的功能
     */
    private void doInstructTypeFunction(final int lastInstructType) {
        RCLog.i(TAG, "获取数据指令" + lastInstructType);
        if (lastInstructType < 0) return;
        switch (lastInstructType) {
            //绑定，同步，场景同步
            case RCBluetoothDoType.DO_BINDING:
            case RCBluetoothDoType.DO_SYNC:
            case RCBluetoothDoType.DO_SYNC_SCENE:
                if (mGetDataListenerMap.containsKey(lastInstructType)) {
                    mGetDataListenerMap.get(lastInstructType).dataInfo(new JSONArray());
                    RCToast.TestCenter(mContext, "连接绑定或数据同步指令成功" + lastInstructType);
                }
                break;
            //开始室内跑，开始室外跑
            //开始室内骑行，开始室外骑行
            case RCBluetoothDoType.DO_GET_REALTIME_RUN_START:
            case RCBluetoothDoType.DO_GET_REALTIME_RUN_START_GPS:
            case RCBluetoothDoType.DO_GET_REALTIME_RIDING_START:
            case RCBluetoothDoType.DO_GET_REALTIME_RIDING_START_GPS:
                //开始跑步
                if (mDeviceId == RCDeviceDeviceID.BONG_2PH) {
                    mBongManager.setStartSportModel(new ResultCallback() {
                        @Override
                        public void finished() {
                            isSceneDoing = true;
                            mGetDataListenerMap.get(lastInstructType).dataInfo(new JSONArray());
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            isSceneDoing = true;
                        }
                    });
                } else {
                    SportType temp = SportType.Run;
                    switch (lastInstructType) {
                        case RCBluetoothDoType.DO_GET_REALTIME_RUN_START:
                            temp = SportType.GpsRun;
                            break;
                        case RCBluetoothDoType.DO_GET_REALTIME_RUN_START_GPS:
                            temp = SportType.GpsRun;
                            break;
                        case RCBluetoothDoType.DO_GET_REALTIME_RIDING_START:
                            temp = SportType.GpsCycle;
                            break;
                        case RCBluetoothDoType.DO_GET_REALTIME_RIDING_START_GPS:
                            temp = SportType.GpsCycle;
                            break;
                    }
                    mBongManager.setStartSportModel(temp,
                            (int) TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()), new ResultCallback() {
                                @Override
                                public void finished() {
                                    isSceneDoing = true;
                                    mGetDataListenerMap.get(lastInstructType).dataInfo(new JSONArray());
                                }

                                @Override
                                public void onError(Throwable throwable) {
                                    isSceneDoing = true;
                                }
                            });
                }
                break;
            //结束室内跑，结束室外跑
            //结束室内骑行，结束室外骑行
            case RCBluetoothDoType.DO_GET_REALTIME_RUN_STOP:
            case RCBluetoothDoType.DO_GET_REALTIME_RUN_STOP_GPS:
            case RCBluetoothDoType.DO_GET_REALTIME_RIDING_STOP:
            case RCBluetoothDoType.DO_GET_REALTIME_RIDING_STOP_GPS:
                //结束跑步或骑行
                if (mDeviceId == RCDeviceDeviceID.BONG_2PH) {
                    mBongManager.setStopSportModel(new ResultCallback() {
                        @Override
                        public void finished() {
                            isSceneDoing = false;
                            mGetDataListenerMap.get(lastInstructType).dataInfo(new JSONArray());
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            isSceneDoing = false;
                        }
                    });
                } else {
                    SportType temp = SportType.Run;
                    switch (lastInstructType) {
                        case RCBluetoothDoType.DO_GET_REALTIME_RUN_STOP:
                            temp = SportType.GpsRun;
                            break;
                        case RCBluetoothDoType.DO_GET_REALTIME_RUN_STOP_GPS:
                            temp = SportType.GpsRun;
                            break;
                        case RCBluetoothDoType.DO_GET_REALTIME_RIDING_STOP:
                            temp = SportType.GpsCycle;
                            break;
                        case RCBluetoothDoType.DO_GET_REALTIME_RIDING_STOP_GPS:
                            temp = SportType.GpsCycle;
                            break;
                    }
                    mBongManager.setStopSportModel(temp, SceneSPInfo.getSceneStartTimeLong(), new ResultCallback() {
                        @Override
                        public void finished() {
                            isSceneDoing = false;
                            mGetDataListenerMap.get(lastInstructType).dataInfo(new JSONArray());
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            isSceneDoing = false;
                        }
                    });
                }
                break;
            //开始实时心率获取
            case RCBluetoothDoType.DO_TEST_HEART_RATE_START:
                mBongManager.readHeartValue(20, new HeartCallback() {
                    @Override
                    public void onReadBatter(final int value) {
                        JSONArray jsonArray = new JSONArray();
                        RCDeviceHeartRateDataDTO dto = new RCDeviceHeartRateDataDTO();
                        dto.setDate(DateUtil.getFormatNow("yyyyMMddHHmmss"));
                        dto.setDeviceId(mDeviceId);
                        dto.setNumber(value);
                        jsonArray.put(dto.getJSON());
                        if (mGetDataListenerMap.containsKey(lastInstructType)) {
                            mGetDataListenerMap.get(lastInstructType).dataInfo(jsonArray);
                        }
                    }

                    @Override
                    public void finished() {

                    }

                    @Override
                    public void onError(Throwable t) {

                    }
                });
                break;
            //结束实时心率获取
            case RCBluetoothDoType.DO_TEST_HEART_RATE_STOP:
                RCToast.TestCenter(mContext, "运动模式结束后，心率获取自动停止");
                break;
            //获取今天步数
            case RCBluetoothDataType.DATATYPE_STEP_TODAY:
                mGetDataListenerMap.get(lastInstructType).dataInfo(new JSONArray().put(
                        mBongUtilDeviceData.getTodayStep(lastConnectMac))
                );
                break;
        }

    }


    /**
     * 会显数据到手环（bong4支持）
     *
     * @param distance
     * @param speed
     */
    public void showDistance(double distance, double speed) {
        SportType temp = SportType.GpsRun;
        switch (RCSceneUtil.doSceneType()) {
            case RUNGPS:
            case RUN:
                temp = SportType.GpsRun;
                break;
            case CYCLING:
            case CYCLINGGPS:
                temp = SportType.GpsCycle;
                break;
        }
        //speed 为速度，bong 4需要传，bong 3 不需要传，可以为0.
        mBongManager.sendSportModelStatus(temp,
                SceneSPInfo.getSceneStartTimeLong(),
                (int) (distance * 1000), (int) (speed / 3.6), new ResultCallback() {
                    @Override
                    public void finished() {

                    }

                    @Override
                    public void onError(Throwable throwable) {

                    }
                });


    }


}

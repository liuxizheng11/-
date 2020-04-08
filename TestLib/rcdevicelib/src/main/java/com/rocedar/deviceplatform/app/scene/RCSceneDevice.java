package com.rocedar.deviceplatform.app.scene;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.rocedar.base.RCDialog;
import com.rocedar.base.RCHandler;
import com.rocedar.base.RCLog;
import com.rocedar.base.RCToast;
import com.rocedar.base.shareprefernces.RCSPBaseInfo;
import com.rocedar.deviceplatform.app.RCUploadDevceData;
import com.rocedar.deviceplatform.config.RCBluetoothDataType;
import com.rocedar.deviceplatform.config.RCBluetoothDoType;
import com.rocedar.deviceplatform.config.RCDeviceDeviceID;
import com.rocedar.deviceplatform.config.RCDeviceIndicatorID;
import com.rocedar.deviceplatform.db.bong.DBDataBongHeart;
import com.rocedar.deviceplatform.device.bluetooth.RCDeviceConfigUtil;
import com.rocedar.deviceplatform.device.bluetooth.RCBlueTooth;
import com.rocedar.deviceplatform.device.bluetooth.impl.RCBluetoothBongImpl;
import com.rocedar.deviceplatform.device.bluetooth.listener.RCBluetoothGetDataListener;
import com.rocedar.deviceplatform.device.phone.RCPhoneStepUtil;
import com.rocedar.deviceplatform.dto.data.RCDeviceAlreadyBindDTO;
import com.rocedar.deviceplatform.request.listener.RCRequestSuccessListener;
import com.rocedar.deviceplatform.unit.DateUtil;
import com.rocedar.deviceplatform.unit.RCCountDataRinding;
import com.rocedar.deviceplatform.unit.RCCountDataRunAndStep;

import org.json.JSONArray;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 项目名称：TestLib
 * <p>
 * 作者：phj
 * 日期：2017/7/18 下午4:57
 * 版本：V1.0.06
 * 描述：场景工具类（目前支持bong2PH，bong4）
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public class RCSceneDevice {

    private final String TAG = "RCSceneDevice";

    private Context mContext;

    //运动类型
    private SceneType mSceneType;

    //使用的蓝牙设备
    private RCDeviceAlreadyBindDTO mChooseDevice;

    //设备连接工具（设备）
    private RCBlueTooth rcBlueTooth;

    //设备工具类（手机）
    private RCPhoneStepUtil rcPhoneStepUtl;

    //设备的mac地址
    private String mac = "phone";

    //
    private RCHandler rcHandler;


    /**
     * 不使用设备
     *
     * @param mContext
     * @param sceneType 运动类型
     */
    public RCSceneDevice(Context mContext, SceneType sceneType) {
        this(mContext, sceneType, RCDeviceDeviceID.Phone);
    }

    /**
     * 使用设备
     *
     * @param mContext
     * @param mSceneType 运动类型
     * @param mChooseId  设备ID
     */
    public RCSceneDevice(Context mContext, SceneType mSceneType, int mChooseId) {
        this.mContext = mContext;
        this.mSceneType = mSceneType;
        rcHandler = new RCHandler(mContext);
        if (mChooseId < 0 || mChooseId == RCDeviceDeviceID.Phone) {
            RCDeviceAlreadyBindDTO dto = new RCDeviceAlreadyBindDTO();
            dto.setDevice_id(RCDeviceDeviceID.Phone);
            rcPhoneStepUtl = RCPhoneStepUtil.getInstance(mContext);
            this.mChooseDevice = dto;
        } else {
            this.mChooseDevice = RCSceneUtil.getDeviceFromId(mChooseId);
            rcBlueTooth = RCDeviceConfigUtil.getBluetoothImpl(mContext, mChooseDevice.getDevice_id());
            mac = mChooseDevice.getDevice_no();
        }
        if (RCSceneUtil.isDoSceneIn() && RCSceneUtil.doSceneStatus() != SceneStatus.PAUSE
                && mSceneType != SceneType.SLEEP) {
            timerUploads = new Timer();
            timerUploads.schedule(new TaskUpload(), 1000, 1000);
            if (rcBlueTooth != null) startGetHeart();
        }
        if (mSceneType == SceneType.SLEEP) {
            IntentFilter filter = new IntentFilter();
            filter.addAction(Intent.ACTION_TIME_TICK);
            mContext.registerReceiver(receiver, filter);
        }
    }

    public void onDestroy() {
        if (timerUploads != null)
            timerUploads.cancel();
        if (mSceneType == SceneType.SLEEP)
            mContext.unregisterReceiver(receiver);
    }


    //⬇ 定时任务，更新线程

    /**
     * 定时任务
     */
    private Timer timerUploads;

    //缓存场景执行的总时长
    private long doSceneTime = 0;


    private class TaskUpload extends TimerTask {

        @Override
        public void run() {
            Message message = new Message();
            message.what = 0;
            timeHandler.sendMessage(message);
        }
    }

    private Handler timeHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    if (doSceneTime == 0) {
                        doSceneTime = SceneSPInfo.getLastSceneDoTime();
                    } else {
                        doSceneTime++;
                    }
                    lastInfoDTO.setAllTime(doSceneTime);
                    lastInfoDTO.setShowTime(fromatTime(doSceneTime));
                    dataChange();
                    //室内跑步计算kcal
                    if (mSceneType == SceneType.RUN) {
                        doGetLastStepInfo();
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    };


    //⬇场景变化执行

    //运动状态
    private SceneStatus mSceneStatus;

    /**
     * 开始运动
     */
    public void doStartScene(RCSceneDeviceListener listener) {
        doStart(listener);
        if (mSceneType != SceneType.SLEEP) {
            timerUploads = new Timer();
            timerUploads.schedule(new TaskUpload(), 1000, 1000);
        }
    }

    /**
     * 结束运动
     */
    public void doStopScene(RCSceneDeviceListener listener) {
        doStop(listener);
        if (mSceneType != SceneType.SLEEP && timerUploads != null)
            timerUploads.cancel();
    }

    /**
     * 暂停运动
     */
    public void doPauseScene(RCSceneDeviceListener listener) {
        saveStatusAndData(SceneStatus.PAUSE, listener);
        if (mSceneType != SceneType.SLEEP && timerUploads != null)
            timerUploads.cancel();
        doSceneTime = 0;
    }

    /**
     * 恢复运动
     */
    public void doRestartScene(RCSceneDeviceListener listener) {
        saveStatusAndData(SceneStatus.START, listener);
        if (mSceneType != SceneType.SLEEP) {
            timerUploads = new Timer();
            timerUploads.schedule(new TaskUpload(), 1000, 1000);
        }
    }


    //⬇设备操作相关


    private int deviceModeIsOpen = MODE_TYPE_NONE;
    private int deviceModeIsClose = MODE_TYPE_NONE;
    private int deviceModeIsGetData = MODE_TYPE_NONE;

    private static int MODE_TYPE_NONE = -1;
    private static int MODE_TYPE_OPEN = 0;
    private static int MODE_TYPE_ERROR = 1;

    /**
     * 开启设备运动模式
     *
     * @param listener
     */
    private void doStart(final RCSceneDeviceListener listener) {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        lastDoTime = format.format(new Date(new Date().getTime() + 3000));//当前时间
        // 如果有第三方设备（也可以理解为设备不为手机），开启运动模式
        if (rcBlueTooth != null) {
            int type = -1;
            switch (mSceneType) {
                case RUN:
                    type = RCBluetoothDoType.DO_GET_REALTIME_RUN_START;
                    break;
                case RUNGPS:
                    type = RCBluetoothDoType.DO_GET_REALTIME_RUN_START_GPS;
                    break;
                case CYCLING:
                    type = RCBluetoothDoType.DO_GET_REALTIME_RIDING_START;
                    break;
                case CYCLINGGPS:
                    type = RCBluetoothDoType.DO_GET_REALTIME_RIDING_START_GPS;
                    break;
            }
            deviceModeIsOpen = MODE_TYPE_NONE;
            rcBlueTooth.sendInstruct(new RCBluetoothGetDataListener() {
                @Override
                public void getDataError(int status, String msg) {
                    if (deviceModeIsOpen == MODE_TYPE_ERROR) {
                        return;
                    }
                    deviceModeIsOpen = MODE_TYPE_OPEN;
                    RCToast.Center(mContext, msg + ",请重试");
                    listener.error(msg);
                }

                @Override
                public void getDataStart() {

                }

                @Override
                public void dataInfo(JSONArray array) {
                    if (deviceModeIsOpen == MODE_TYPE_ERROR) {
                        return;
                    }
                    deviceModeIsOpen = MODE_TYPE_OPEN;
                    RCToast.TestCenter(mContext, "设备运动模式开启成功");
                    saveStatusAndData(SceneStatus.START, listener, lastDoTime);
                }
            }, mac, type);
            rcHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (deviceModeIsOpen != MODE_TYPE_OPEN) {
                        deviceModeIsOpen = MODE_TYPE_ERROR;
                        rcBlueTooth.doDisconnect();
                        dialog = new RCDialog(mContext, new String[]{null, "设备断开连接，是否继续？继续将无法记录您的运动心率",
                                "取消", "继续"}, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                listener.error("取消");
                                dialog.dismiss();
                            }
                        }, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                RCDeviceAlreadyBindDTO dto = new RCDeviceAlreadyBindDTO();
                                dto.setDevice_id(RCDeviceDeviceID.Phone);
                                rcPhoneStepUtl = RCPhoneStepUtil.getInstance(mContext);
                                mChooseDevice = dto;
                                sceneDataChangeListener = null;
                                rcBlueTooth = null;
                                if (listener != null) {
                                    listener.error("手机");
                                }
                                saveStatusAndData(SceneStatus.START, null, lastDoTime);
                                dialog.dismiss();
                            }
                        });
                        if (!dialog.isShowing())
                            dialog.show();
                    }
                }
            }, 4000);
        } else
            saveStatusAndData(SceneStatus.START, listener, lastDoTime);
    }

    private RCDialog dialog;

    /**
     * 关闭设备运动模式
     *
     * @param listener
     */
    private void doStop(final RCSceneDeviceListener listener) {
        if (rcBlueTooth != null) {
            int type = -1;
            switch (mSceneType) {
                case RUN:
                    type = RCBluetoothDoType.DO_GET_REALTIME_RUN_STOP;
                    break;
                case RUNGPS:
                    type = RCBluetoothDoType.DO_GET_REALTIME_RUN_STOP_GPS;
                    break;
                case CYCLING:
                    type = RCBluetoothDoType.DO_GET_REALTIME_RIDING_STOP;
                    break;
                case CYCLINGGPS:
                    type = RCBluetoothDoType.DO_GET_REALTIME_RIDING_STOP_GPS;
                    break;
            }
            deviceModeIsClose = MODE_TYPE_NONE;
            rcBlueTooth.sendInstruct(new RCBluetoothGetDataListener() {
                @Override
                public void getDataError(int status, String msg) {
                    if (deviceModeIsOpen == MODE_TYPE_ERROR) {
                        return;
                    }
                    deviceModeIsOpen = MODE_TYPE_OPEN;
                    RCToast.Center(mContext, msg + ",请重试");
                }

                @Override
                public void getDataStart() {

                }

                @Override
                public void dataInfo(JSONArray array) {
                    if (deviceModeIsClose == MODE_TYPE_ERROR) return;
                    deviceModeIsClose = MODE_TYPE_OPEN;
                    saveStatusAndData(SceneStatus.STOP, listener);
                }
            }, mac, type);
            rcHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (deviceModeIsClose == MODE_TYPE_OPEN) {
                        return;
                    }
                    deviceModeIsClose = MODE_TYPE_ERROR;
                    rcBlueTooth.doDisconnect();
                    dialog = new RCDialog(mContext, new String[]{null, "设备断开连接，将会丢失部分数据，是否保存？",
                            "保存", "尝试连接"}, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            rcBlueTooth = null;
                            saveStatusAndData(SceneStatus.STOP, listener);
                            dialog.dismiss();
                        }
                    }, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            rcBlueTooth.sendInstruct(new RCBluetoothGetDataListener() {
                                @Override
                                public void getDataError(int status, String msg) {
                                    if (!dialog.isShowing())
                                        dialog.show();
                                }

                                @Override
                                public void getDataStart() {

                                }

                                @Override
                                public void dataInfo(JSONArray array) {
                                    doStop(listener);
                                }
                            }, mac, RCBluetoothDoType.DO_SYNC_SCENE);
                        }
                    });
                    if (!dialog.isShowing())
                        dialog.show();
                }
            }, 4000);
        } else
            saveStatusAndData(SceneStatus.STOP, listener);
    }


    /**
     * 开始获取实时心率
     */
    public void startGetHeart() {
        if (rcBlueTooth != null)
            rcBlueTooth.sendInstruct(new RCBluetoothGetDataListener() {
                @Override
                public void getDataError(int status, String msg) {

                }

                @Override
                public void getDataStart() {

                }

                @Override
                public void dataInfo(JSONArray array) {
                    int heartRate = 0;
                    if (array.length() >= 0) {
                        heartRate = array.optJSONObject(0).optJSONObject("value").optInt(RCDeviceIndicatorID.Heart_Rate + "");
                        if (sceneDataChangeListener != null)
                            sceneDataChangeListener.heartRateChange(heartRate);
                    }
                }
            }, mac, RCBluetoothDoType.DO_TEST_HEART_RATE_START);

    }

    /**
     * 如果是bong4手环需要显示回显距离
     */
    public void updataShowDistance(double distance, double speed) {
        if (mChooseDevice.getDevice_id() == RCDeviceDeviceID.BONG_4) {
            if (rcBlueTooth instanceof RCBluetoothBongImpl) {
                if (((RCBluetoothBongImpl) rcBlueTooth).getBongBraceletUtil()!=null) {
                    ((RCBluetoothBongImpl) rcBlueTooth).getBongBraceletUtil().showDistance(
                            distance, speed
                    );
                }
            }
        }

    }


    //用于累加30秒获取一次最新的步数(手机计步不用)
    private int countTime = 0;

    /**
     * 最后的步数(室内跑步取步数)
     */
    public void doGetLastStepInfo() {
        if (rcBlueTooth != null) {
            countTime++;
            if (countTime < 30) {
                return;
            }
            countTime = 0;
            rcBlueTooth.sendInstruct(new RCBluetoothGetDataListener() {
                @Override
                public void getDataError(int status, String msg) {

                }

                @Override
                public void getDataStart() {

                }

                @Override
                public void dataInfo(JSONArray array) {
                    int step = 0;
                    if (array.length() > 0)
                        step = array.optJSONObject(0).optJSONObject("value").optInt(RCDeviceIndicatorID.STEP + "");
                    countStepInfo(SceneSPInfo.getSceneALLStep(step));

                }
            }, mac, RCBluetoothDataType.DATATYPE_STEP_TODAY);
        } else {
            countStepInfo(SceneSPInfo.getSceneALLStep(rcPhoneStepUtl.calculationTodaySteps()));
        }
    }

    //⬇数据计算

    /**
     * 根据步数计算消耗的kacl、距离(室内跑才有)，用于更新显示数据
     *
     * @param step
     */
    public void countStepInfo(int step) {
        double kacl = RCCountDataRunAndStep.getStepKcal(step, RCSPBaseInfo.getLastUserBaseInfoStature(),
                RCSPBaseInfo.getLastUserBaseInfoWeight());
        double distance = RCCountDataRunAndStep.getRunDistance(
                RCSPBaseInfo.getLastUserBaseInfoStature(),
                RCSPBaseInfo.getLastUserBaseInfoSex(),
                step, (int) lastInfoDTO.getAllTime()
        );
        if (lastInfoDTO != null) {
            lastInfoDTO.setCalorie(getBigDecimal(kacl));
            lastInfoDTO.setAllDistance(getBigDecimal(distance));
            RCLog.e(TAG, "跑步的步数：" + step + "跑步的距离：" + distance + "跑步的时间：" + lastInfoDTO.getAllTime());
            lastInfoDTO.setSpeed(distance / (lastInfoDTO.getAllTime() / 3600.00f));
            RCLog.e(TAG, "跑步的速度：" + lastInfoDTO.getSpeed() + "跑步的配速：" + lastInfoDTO.getPace()
                    + "跑步的配速：" + lastInfoDTO.getPaceShow());
            dataChange();
        }
    }


    //⬇数据存储相关


    private String lastDoTime;


    private void saveStatusAndData(final SceneStatus sceneStatus, final RCSceneDeviceListener listener) {
        saveStatusAndData(sceneStatus, listener, null);
    }

    /**
     * 保存场景状态和数据
     *
     * @param sceneStatus
     */
    private void saveStatusAndData(final SceneStatus sceneStatus, final RCSceneDeviceListener listener,
                                   final String lastDoTimes) {
        RCToast.TestCenter(mContext, "当前状态：" + sceneStatus.name());
        if (sceneStatus != SceneStatus.START && SceneSPInfo.getLastSceneStatus() != SceneStatus.PAUSE)
            rcHandler.sendMessage(RCHandler.START);
        final String lastDoTime = (lastDoTimes == null) ? DateUtil.getFormatNow("yyyyMMddHHmmss") : lastDoTimes;//当前时间
        if (mSceneType == SceneType.RUN || mSceneType == SceneType.RUNGPS || mSceneType == SceneType.CYCLING
                || mSceneType == SceneType.CYCLINGGPS) {
            //先同步，再获取步数
            if (rcBlueTooth != null) {
                //同步数据
                deviceModeIsGetData = MODE_TYPE_NONE;
                rcBlueTooth.sendInstruct(new RCBluetoothGetDataListener() {
                    @Override
                    public void getDataError(int status, String msg) {

                    }

                    @Override
                    public void getDataStart() {

                    }

                    @Override
                    public void dataInfo(JSONArray array) {
                        if (deviceModeIsGetData == MODE_TYPE_ERROR) return;
                        deviceModeIsGetData = MODE_TYPE_OPEN;
                        //获取步数
                        rcBlueTooth.sendInstruct(new RCBluetoothGetDataListener() {
                            @Override
                            public void getDataError(int status, String msg) {
                                if (listener != null)
                                    listener.error("");
                            }

                            @Override
                            public void getDataStart() {

                            }

                            @Override
                            public void dataInfo(JSONArray array) {
                                //保存状态和步数
                                int step = 0;
                                if (array != null && array.length() > 0)
                                    step = array.optJSONObject(0).optJSONObject("value").optInt(RCDeviceIndicatorID.STEP + "");
                                saveStatusAndData(sceneStatus, lastDoTime, step, listener);

                                if (sceneStatus == SceneStatus.START)
                                    dataChange();
                            }
                        }, mac, RCBluetoothDataType.DATATYPE_STEP_TODAY);
                    }
                }, mac, RCBluetoothDoType.DO_SYNC_SCENE);
                if (sceneStatus != SceneStatus.START) {
                    rcHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (deviceModeIsGetData == MODE_TYPE_OPEN) {
                                return;
                            }
                            rcHandler.sendMessage(RCHandler.GETDATA_OK);
                            deviceModeIsGetData = MODE_TYPE_ERROR;
                            if (rcBlueTooth != null)
                                rcBlueTooth.doDisconnect();
                            dialog = new RCDialog(mContext, new String[]{null, "设备断开连接，将会丢失部分数据，是否保存？",
                                    "保存", "尝试连接"}, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                    if (sceneStatus == SceneStatus.STOP || sceneStatus == SceneStatus.PAUSE) {
                                        int step = 0;
                                        saveStatusAndData(sceneStatus, lastDoTime, step, listener);
                                    } else {
                                        saveStatusAndData(sceneStatus, lastDoTime, SceneSPInfo.getSceneALLStep(0), listener);
                                    }
                                    if (sceneStatus == SceneStatus.START) {
                                        dataChange();
                                    }
                                }
                            }, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                    rcHandler.sendMessage(RCHandler.START);
                                    rcBlueTooth.sendInstruct(new RCBluetoothGetDataListener() {
                                        @Override
                                        public void getDataError(int status, String msg) {
                                            if (!dialog.isShowing())
                                                dialog.show();
                                        }

                                        @Override
                                        public void getDataStart() {

                                        }

                                        @Override
                                        public void dataInfo(JSONArray array) {
                                            saveStatusAndData(sceneStatus, listener, lastDoTimes);
                                        }
                                    }, mac, RCBluetoothDoType.DO_SYNC_SCENE);
                                }
                            });
                            if (!dialog.isShowing())
                                dialog.show();
                        }
                    }, 20 * 1000);
                }
            } else {
                int step = 0;
                if (rcPhoneStepUtl != null)
                    step = rcPhoneStepUtl.calculationTodaySteps();
                saveStatusAndData(sceneStatus, lastDoTime, step, listener);
            }
        } else {
            saveStatusAndData(sceneStatus, lastDoTime, 0, listener);
        }
    }

    /**
     * 保存数据的执行保存的方法
     *
     * @param sceneStatus
     * @param time
     * @param step
     * @param listener
     */
    private void saveStatusAndData(SceneStatus sceneStatus, String time, int step, final RCSceneDeviceListener listener) {
        switch (sceneStatus) {
            case STOP:
                if (SceneSPInfo.getLastSceneStatus() != SceneStatus.PAUSE) {
                    SceneSPInfo.saveSceneStopData(time, step);
                }
                SceneSPInfo.doStop(mSceneType);
                RCLog.i(TAG, "结束" + mSceneType.name() + "，步数" + step + "，时间" + time);
                JSONArray array = SceneSPInfo.getSceneData(mChooseDevice.getDevice_id()
                        , mac, mChooseDevice.getDevice_id() == RCDeviceDeviceID.Phone ? null : new DBDataBongHeart(mContext));
//              setInfo("mSceneType.name()数据为：" + SceneSPInfo.getSceneData(deviceId, mac, new DBDataBongHeart(mActivity)).toString());
                if (array != null) {
                    //如果包含错误标识，表示数据异常
                    if (array.toString().contains(SceneSPInfo.ERROR_TAG)) {
                        if (listener != null) {
                            listener.error(array.optString(0).replace(SceneSPInfo.ERROR_TAG, ""));
                            rcHandler.sendMessage(RCHandler.GETDATA_OK);
                        }
                    } else {
                        saveAndUploadData(array, new RCSceneDeviceListener() {
                            @Override
                            public void success(SceneStatus sceneStatus, JSONArray overInfo) {
                                listener.success(sceneStatus, overInfo);
                                rcHandler.sendMessage(RCHandler.GETDATA_OK);
                            }

                            @Override
                            public void error(String msg) {
                                listener.error(msg);
                                rcHandler.sendMessage(RCHandler.GETDATA_OK);
                            }
                        });
                    }
                } else {
                    if (listener != null)
                        listener.error("数据保存失败");
                    rcHandler.sendMessage(RCHandler.GETDATA_OK);
                }
                break;
            case PAUSE:
                SceneSPInfo.saveSceneStopData(time, step);
                SceneSPInfo.doPause(mSceneType);
                if (listener != null)
                    listener.success(mSceneStatus, null);
                RCLog.i(TAG, "暂停" + mSceneType.name() + "，步数" + step + "，时间" + time);
                rcHandler.sendMessage(RCHandler.GETDATA_OK);
                break;
            case START:
                if (SceneSPInfo.getLastSceneStatus() == SceneStatus.PAUSE) {
                    SceneSPInfo.saveSceneStartData(time, step);
                    RCLog.i(TAG, "恢复" + mSceneType.name() + "，步数" + step + "，时间" + time);
                } else {
                    //清除数据
                    SceneSPInfo.clearSceneData();
                    RCLog.i(TAG, "开始" + mSceneType.name() + "，步数" + step + "，时间" + time);
                    SceneSPInfo.saveSceneStartData(time, step);
                    startGetHeart();
                }
                SceneSPInfo.doStart(mSceneType, mChooseDevice.getDevice_id());
                if (listener != null)
                    listener.success(mSceneStatus, null);
                rcHandler.sendMessage(RCHandler.GETDATA_OK);
                break;
        }

    }


    /**
     * 保存并上传数据
     *
     * @param json
     */
    private void saveAndUploadData(final JSONArray json, final RCSceneDeviceListener listener) {
        RCUploadDevceData.postDeviceData(mContext, json, new RCRequestSuccessListener() {
            @Override
            public void requestSuccess() {
                if (listener != null)
                    listener.success(mSceneStatus, json);
            }

            @Override
            public void requestError(int status, String msg) {
                listener.error("数据上传失败");
                RCUploadDevceData.saveBlueDeviceData(json);
            }
        });

    }


    /**
     * 保存GPS信息
     */
    public void saveGpsInfo(SceneGPSDTO sceneGPSDTO) {
        if (!RCSceneUtil.isDoSceneIn()) return;
        lastInfoDTO.setSpeed(getBigDecimal(sceneGPSDTO.getSpeed() * 3.6));
        dataChange();
        if (!SceneSPInfo.getLastSceneStartTime().equals("")) {
            SceneSPInfo.saveGPSData(SceneSPInfo.getLastSceneStartTime(), sceneGPSDTO);
            saveGpsDistance(sceneGPSDTO.getDistance());
        }
    }

    /**
     * 获取所有GPS点
     *
     * @return
     */
    public List<List<SceneGPSDTO>> getAllGps(SceneGPSDTO gpsdto) {
        List<List<SceneGPSDTO>> lists = new ArrayList<>();
        List<SceneGPSListDTO> temp = SceneSPInfo.getAllGspInfo();
        if (temp != null)
            for (int i = 0; i < temp.size(); i++) {
                if (i == temp.size() - 1) {
                    List<SceneGPSDTO> listtemp = temp.get(i).getSceneGPSDTOs();
                    listtemp.add(gpsdto);
                    lists.add(listtemp);
                } else {
                    lists.add(temp.get(i).getSceneGPSDTOs());
                }
            }
        return lists;
    }

    /**
     * 获取所有GPS点
     *
     * @return
     */
    public List<List<SceneGPSDTO>> getAllGps() {
        List<List<SceneGPSDTO>> lists = new ArrayList<>();
        List<SceneGPSListDTO> temp = SceneSPInfo.getAllGspInfo();
        if (temp != null)
            for (int i = 0; i < temp.size(); i++) {
                lists.add(temp.get(i).getSceneGPSDTOs());
            }
        return lists;
    }


    /**
     * 保存GPS运动的距离
     * <p>
     * 保存时需要更新距离
     * 如果是室外跑或者室外骑行，需要根据距离计算kcal
     * 如果是bong4设备需要回显数据
     *
     * @param distance 距离(m)
     */
    private void saveGpsDistance(double distance) {
        if (RCSceneUtil.doSceneStatus() == SceneStatus.START) {
            //保存距离到存储
            SceneSPInfo.saveGPSDistance(distance);
            lastInfoDTO.setAllDistance(distance / 1000);
            //计算卡路里⬇
            //如果是室外跑步骑行计算kcal
            if (mSceneType == SceneType.CYCLINGGPS) {
//                lastInfoDTO.setCalorie(getBigDecimal(
//                        RCCountDataRinding.getRidingKcal(RCSPBaseInfo.getLastUserBaseInfoSex(),
//                                RCSPBaseInfo.getLastUserBaseInfoWeight(),
//                                lastInfoDTO.getAllDistance(), doSceneTime / 60.00)));
                lastInfoDTO.setCalorie(getBigDecimal(
                        RCCountDataRinding.getRidingKcal(RCSPBaseInfo.getLastUserBaseInfoWeight(),
                                lastInfoDTO.getAllDistance())
                ));
            }
            if (mSceneType == SceneType.RUNGPS) {
                lastInfoDTO.setCalorie(getBigDecimal(
                        RCCountDataRunAndStep.getRunKcal(
                                RCSPBaseInfo.getLastUserBaseInfoWeight(), distance / 1000
                        )
                ));
            }
            updataShowDistance(distance / 1000, lastInfoDTO.getSpeed());
            dataChange();
        }
    }


    private double getBigDecimal(double d) {
        BigDecimal bg = new BigDecimal(d);
        return bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }


    //⬇监听相关

    //返回的时间格式
    private String listenerFormat = "HH:mm";

    /**
     * 设置系统时间监听返回的格式（默认为HH:mm）,只支持到分钟
     *
     * @param listenerFormat
     */
    public void setListenerFormat(String listenerFormat) {
        this.listenerFormat = listenerFormat;
    }

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_TIME_TICK)) {
                if (sceneDataChangeListener != null) {
                    sceneDataChangeListener.timeChange(DateUtil.getFormatNow(listenerFormat));
                }
            }
        }
    };


    public RCSceneDataChangeListener sceneDataChangeListener;

    //最后保存的活动数据（因为活动数据来源不一样，变更时间不一致，改变量用于存储最后状态，保证回调的数据是最新数据）
    public SceneListenerDTO lastInfoDTO = new SceneListenerDTO();

    /**
     * 设置数据变化监听（包括心率，时间，速度，距离，运动持续时间）
     *
     * @param sceneDataChangeListener
     */
    public void setSceneDataChangeListener(RCSceneDataChangeListener sceneDataChangeListener) {
        this.sceneDataChangeListener = sceneDataChangeListener;
        lastInfoDTO = new SceneListenerDTO();
        lastInfoDTO.setAllDistance(0.0);
        lastInfoDTO.setSpeed(0.0);
        lastInfoDTO.setAllTime(0);
        lastInfoDTO.setShowTime("0:0");
        if (RCSceneUtil.doSceneStatus() == SceneStatus.START ||
                RCSceneUtil.doSceneStatus() == SceneStatus.RESTART ||
                RCSceneUtil.doSceneStatus() == SceneStatus.STOP) {
            if (SceneSPInfo.getGPSDistance() > 0) {
                lastInfoDTO.setAllDistance(getBigDecimal(SceneSPInfo.getGPSDistance() / 1000));
            }
            if (SceneSPInfo.getLastSceneDoTime() > 0) {
                lastInfoDTO.setAllTime(SceneSPInfo.getLastSceneDoTime());
                lastInfoDTO.setShowTime(fromatTime(lastInfoDTO.getAllTime()));
            }
        }
        dataChange();
    }


    /**
     * 执行数据变更回调
     */
    private void dataChange() {
        if (RCSceneUtil.isDoSceneIn())
            if (sceneDataChangeListener != null) {
                sceneDataChangeListener.doSceneChange(lastInfoDTO);
            }
    }


    public interface RCSceneDataChangeListener {

        /**
         * 心率变化
         *
         * @param heartRate
         */
        void heartRateChange(int heartRate);


        /**
         * 系统时间变化
         *
         * @param nowTime
         */
        void timeChange(String nowTime);

        /**
         * 执行场景的数据变化
         *
         * @param dto
         */
        void doSceneChange(SceneListenerDTO dto);

    }

    public interface RCSceneDeviceListener {

        void success(SceneStatus sceneStatus, JSONArray overInfo);

        void error(String msg);
    }


    //⬇工具类

    /**
     * 格式化时间（时:分:秒）
     *
     * @param time
     * @return
     */
    private String fromatTime(long time) {
        long h = time / 3600;
        long m = (time % 3600) / 60;
        long s = (time % 3600) % 60;
        if (h > 0)
            return (h > 9 ? h + "" : "0" + h) + ":" +
                    (m > 9 ? m + "" : "0" + m) + ":" +
                    (s > 9 ? s + "" : "0" + s);
        else
            return (m > 9 ? m + "" : "0" + m) + ":" +
                    (s > 9 ? s + "" : "0" + s);
    }
}

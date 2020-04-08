package com.rocedar.deviceplatform.app.scene;

import android.content.Context;
import android.view.View;

import com.rocedar.base.RCDialog;
import com.rocedar.deviceplatform.app.RCUploadDevceData;
import com.rocedar.deviceplatform.config.RCDeviceDeviceID;
import com.rocedar.deviceplatform.device.bluetooth.RCDeviceConfigUtil;
import com.rocedar.deviceplatform.device.bluetooth.RCBlueTooth;
import com.rocedar.deviceplatform.dto.data.RCDeviceAlreadyBindDTO;
import com.rocedar.deviceplatform.dto.device.RCDeviceSleepDataDTO;
import com.rocedar.deviceplatform.request.listener.RCRequestSuccessListener;
import com.rocedar.deviceplatform.sharedpreferences.RCSPDeviceInfo;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称：DongYa3.0
 * <p>
 * 作者：phj
 * 日期：2017/7/17 下午5:16
 * 版本：V2.2.00
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public class RCSceneUtil {

    private static String TAG = "RCSceneUtil";

    //暂时多长时间后自动结束
    public static final int PAUSE_OVER_TIME = 10;


    private static final int[] DEVICE_LIST = {
            RCDeviceDeviceID.BONG_2PH, RCDeviceDeviceID.BONG_4
    };


    /**
     * 是否有正在进行的场景
     *
     * @return true有正在进行的场景
     */
    public static boolean isDoSceneIn() {
        return (SceneSPInfo.getLastSceneStatus() == SceneStatus.PAUSE ||
                SceneSPInfo.getLastSceneStatus() == SceneStatus.RESTART ||
                SceneSPInfo.getLastSceneStatus() == SceneStatus.START);
    }

    /**
     * 正在执行的场景类型
     *
     * @return
     */
    public static SceneType doSceneType() {
        return SceneSPInfo.getLastSceneType();
    }

    /**
     * 正在执行的场景状态
     *
     * @return
     */
    public static SceneStatus doSceneStatus() {
        return SceneSPInfo.getLastSceneStatus();
    }

    /**
     * 正在执行的场景使用的设备ID
     *
     * @return
     */
    public static int doSceneDeviceId() {
        return SceneSPInfo.getLastSceneDeviceId();
    }

    /**
     * @param context
     * @param sleepListener 睡眠跳转页面
     * @param runListener   跑步骑行跳转页面
     */

    public static void getRunningCyclingStatus(final Context context,
                                               final View.OnClickListener sleepListener,
                                               final View.OnClickListener runListener) {
        if (isDoSceneIn()) {
            View.OnClickListener onClickListener;
            if (sleepListener != null && doSceneType() == SceneType.SLEEP) {
                onClickListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sleepListener.onClick(v);
                        dialog.dismiss();
                    }
                };
            } else {
                onClickListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        runListener.onClick(v);
                        dialog.dismiss();
                    }
                };
            }

            dialog = new RCDialog(context, new String[]{null, "您有一个之前尚未结束的" +
                    SceneType.getString(doSceneType()) +
                    "行为，是否继续", "否", "是"}, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SceneSPInfo.doStop(doSceneType());
                    dialog.dismiss();
                }
            }, onClickListener);
            dialog.show();
        }

    }

    /**
     * @return 已经绑定的有效设备个数
     */
    public static int getDeviceNumber() {
        List<RCDeviceAlreadyBindDTO> bindDTOs = RCSPDeviceInfo.getBlueToothInfo();
        int temp = 0;
        for (RCDeviceAlreadyBindDTO dto : bindDTOs) {
            for (int i : DEVICE_LIST) {
                if (dto.getDevice_id() == i) {
                    temp++;
                }
            }
        }
        return temp;
    }

    /**
     * @return 已经绑定的并且已经连接的有效设备列表
     */
    public static List<RCDeviceAlreadyBindDTO> getBindDeviceList(Context context) {
        List<RCDeviceAlreadyBindDTO> bindDTOs = new ArrayList<>();
        for (RCDeviceAlreadyBindDTO dto : RCSPDeviceInfo.getBlueToothInfo()) {
            for (int i : DEVICE_LIST) {
                if (dto.getDevice_id() == i) {
                    bindDTOs.add(dto);
                }
            }
        }
        return bindDTOs;
    }

    /**
     * 设备是否连接
     *
     * @param context
     * @param dto
     * @return
     */
    public static boolean deviceIsConnect(Context context, RCDeviceAlreadyBindDTO dto) {
        RCBlueTooth rcBlueTooth = RCDeviceConfigUtil.getBluetoothImpl(context, dto.getDevice_id());
        if (rcBlueTooth != null && rcBlueTooth.isConnect()) {
            return true;
        }
        return false;
    }


    /**
     * @return 已经绑定的指定ID有效设备
     */
    public static RCDeviceAlreadyBindDTO getDeviceFromId(int id) {
        List<RCDeviceAlreadyBindDTO> bindDTOs = RCSPDeviceInfo.getBlueToothInfo();
        for (RCDeviceAlreadyBindDTO dto : bindDTOs) {
            if (dto.getDevice_id() == id) {
                return dto;
            }
        }
        return null;
    }


//    /**
//     * 开始运动（单个设备或者无设备）
//     *
//     * @param sceneType 运行类型
//     */
//    public static void doStartScene(Context context, SceneType sceneType) {
//        //有效设备列表
//        List<RCDeviceAlreadyBindDTO> bindDTOs = getDeviceList();
//        if (bindDTOs.size() == 1) {
//            doStartScene(context, sceneType, bindDTOs.get(0));
//        } else {
//            RCToast.Center(context, "当前连接多个设备");
//            return;
//        }
//    }
//
//    /**
//     * 开始运动(多个设备时选择一个设备)
//     *
//     * @param sceneType    运行类型
//     * @param chooseDevice 选择的设备（不选择时传null）
//     */
//    public static void doStartScene(Context context, SceneType sceneType, RCDeviceAlreadyBindDTO chooseDevice) {
//        if (SceneSPInfo.getLastSceneStatus() != SceneStatus.STOP) {
//            RCLog.i(TAG, "上次运动没有停止");
//            showDialog(context, "上次运动未结束，是否继续？", new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                }
//            });
//            return;
//        }
//        if (chooseDevice == null) {
//            SceneSPInfo.doStart(sceneType, chooseDevice.getDevice_id());
//        }
//    }


    /**
     * 保存睡眠数据（编辑）
     *
     * @param info
     */
    public static void saveSleepInfo(Context context, JSONArray info, int beforeStatus, int dreamStatus,
                                     String note, RCRequestSuccessListener listener) {
        if (info.length() > 0) {
            RCDeviceSleepDataDTO dto = RCDeviceSleepDataDTO.fromJson(info.optJSONObject(0));
            dto.setSleepNote(note);
            dto.setSleepBeforeStatus(beforeStatus);
            dto.setSleepDreamStatus(dreamStatus);
            RCUploadDevceData.postDeviceData(context,
                    new JSONArray().put(dto.getJSON()), listener);
        }
    }

    /**
     * 保存编辑后的睡眠数据
     *
     * @param startTime
     * @param endTime
     * @param beforeStatus
     * @param dreamStatus
     * @param note
     */
    public static void saveSleepInfo(Context context, String startTime, String endTime, int beforeStatus,
                                     int dreamStatus, String note, RCRequestSuccessListener listener) {
        RCDeviceSleepDataDTO dto = new RCDeviceSleepDataDTO();
        dto.setStartTime(Long.parseLong(startTime));
        dto.setStopTime(Long.parseLong(endTime));
        dto.setSleepNote(note);
        dto.setSleepBeforeStatus(beforeStatus);
        dto.setSleepDreamStatus(dreamStatus);
        RCUploadDevceData.postDeviceData(context,
                new JSONArray().put(dto.getJSON()), listener);
    }


    private static RCDialog dialog;

    private static void showDialog(Context context, String msg, View.OnClickListener onClickListener) {
        dialog = new RCDialog(context, new String[]{null, msg, "", ""}, null, onClickListener);
        dialog.show();
    }


//    /**
//     * 连接状态
//     */
//    public enum DeviceConnectStatus {
//        //连接一个设备，没有连接设备，连接多个设备
//        ONECONNECT(R.string.rcdevice_scene_connect_info, false),
//        ONEUNCONNECT(R.string.rcdevice_scene_unconnect_info, true),
//        MOREDEVICE(R.string.rcdevice_scene_connect_devices, false),
//        NODEVICE(R.string.rcdevice_scene_no_device, false);
//
//        DeviceConnectStatus(int infoId, boolean hasConnect) {
//            this.infoId = infoId;
//            this.hasConnect = hasConnect;
//        }
//
//
//        public int infoId;
//
//        public boolean hasConnect;
//
//        public String info;
//
//        public int getInfoId() {
//            return infoId;
//        }
//
//        public void setInfo(Context context) {
//            this.info = context.getString(getInfoId());
//        }
//
//        public void setInfo(Context context, RCDeviceAlreadyBindDTO dto) {
//            this.info = context.getString(getInfoId(), dto.getDevice_name());
//        }
//
//        /**
//         * 没有连接设备，连接多个设备信息
//         *
//         * @param context
//         */
//        public String getInfo(Context context) {
//            if (info == null)
//                info = context.getString(getInfoId());
//            return info;
//        }
//
//        public String getInfo(Context context, RCDeviceAlreadyBindDTO dto) {
//            if (info == null)
//                info = context.getString(getInfoId(), dto.getDevice_name());
//            return info;
//        }
//
//        public boolean isHasConnect() {
//            return hasConnect;
//        }
//
//        public void setHasConnect(boolean hasConnect) {
//            this.hasConnect = hasConnect;
//        }
//    }

}

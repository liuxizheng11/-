package com.rocedar.deviceplatform.sharedpreferences;

import android.content.SharedPreferences;

import com.rocedar.base.RCBaseManage;
import com.rocedar.base.RCUtilEncode;
import com.rocedar.base.shareprefernces.RCSPBaseInfo;
import com.rocedar.deviceplatform.dto.data.RCDeviceAlreadyBindDTO;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author liuyi
 * @date 2017/2/11
 * @desc 存储用户已经绑定的蓝牙设备
 * @veison V1.0
 */

public abstract class RCSPDeviceInfo {

    private static final String USER_DEVICE_INFO = "user_device_binding_list";

    public static SharedPreferences getSharedPreferences() {
        return RCBaseManage.getInstance().getContext().getSharedPreferences(
                RCUtilEncode.getMd5StrUpper16(USER_DEVICE_INFO + RCSPBaseInfo.getLastUserId()), 0);
    }

    public static SharedPreferences.Editor getSharedPreferencesEditor() {
        return getSharedPreferences().edit();
    }

    private static final String USER_BLUETOOTH_INFO = "user_binding_bluetooth_device_no_";

    /**
     * 保存已绑定的蓝牙设备信息
     *
     * @return
     */
    public static boolean saveBlueToothInfo(List<RCDeviceAlreadyBindDTO> mInfo) {
        SharedPreferences.Editor editorTemp = getSharedPreferencesEditor();
        editorTemp.clear();
        for (int i = 0; i < mInfo.size(); i++) {
            if (RCDeviceAlreadyBindDTO.toJSON(mInfo.get(i)) != null)
                editorTemp.putString(USER_BLUETOOTH_INFO + mInfo.get(i).getDevice_id(),
                        RCDeviceAlreadyBindDTO.toJSON(mInfo.get(i)).toString());
        }
        return editorTemp.commit();
    }

    /**
     * 获取所有已绑定的蓝牙设备信息
     *
     * @return
     */
    public static List<RCDeviceAlreadyBindDTO> getBlueToothInfo() {
        SharedPreferences s = getSharedPreferences();
        ArrayList<RCDeviceAlreadyBindDTO> mInfos = new ArrayList<>();
        Map<String, ?> map = s.getAll();
        for (Map.Entry<String, ?> entry : map.entrySet()) {
            if (entry.getKey().startsWith(USER_BLUETOOTH_INFO) && !entry.getValue().equals("")) {
                try {
                    JSONObject obj = new JSONObject(entry.getValue().toString());
                    if (obj != null && RCDeviceAlreadyBindDTO.toDTO(obj) != null) {
                        mInfos.add(RCDeviceAlreadyBindDTO.toDTO(obj));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return mInfos;
    }

    /**
     * 根据蓝牙设备的deviceID返回设备的MAC地址
     *
     * @param device_id 设备ID
     * @return 查询不到返回 -->""
     */
    public static String getBlueToothMac(int device_id) {
        List<RCDeviceAlreadyBindDTO> mInfo = getBlueToothInfo();
        for (int i = 0; i < mInfo.size(); i++) {
            if (mInfo.get(i).getDevice_id() == (device_id)) {
                return mInfo.get(i).getDevice_no();
            }
        }
        return "";
    }

    /**
     * 根据蓝牙设备的deviceID删除当前设备
     *
     * @param device_id 设备ID
     */
    public static void removeBlueToothInfo(int device_id) {
        List<RCDeviceAlreadyBindDTO> mInfo = getBlueToothInfo();
        for (int i = 0; i < mInfo.size(); i++) {
            if (mInfo.get(i).getDevice_id() == (device_id)) {
                mInfo.remove(i);
            }
        }
        saveBlueToothInfo(mInfo);
    }

    /**
     * 根据蓝牙设备的deviceID添加或新加一个设备
     *
     * @param device_id 设备ID
     */
    public static void addBlueToothInfo(int device_id, String mac) {
        List<RCDeviceAlreadyBindDTO> mInfo = getBlueToothInfo();
        RCDeviceAlreadyBindDTO dto = new RCDeviceAlreadyBindDTO();
        dto.setDevice_id(device_id);
        dto.setDevice_no(mac);
        ArrayList<RCDeviceAlreadyBindDTO> newInfo = new ArrayList<>();
        for (int i = 0; i < mInfo.size(); i++) {
            if (mInfo.get(i).getDevice_id() == (device_id)) {
                newInfo.add(dto);
            } else {
                newInfo.add(mInfo.get(i));
            }

        }
        saveBlueToothInfo(newInfo);

    }

    /**
     * 根据设备的mac找到绑定的设备ID，没有返回-1
     *
     * @param mac 设备mac地址
     * @return 设备的设备ID，没有返回-1
     */
    public static int getBluetoothDeviceIdFormMac(String mac) {
        List<RCDeviceAlreadyBindDTO> mInfo = getBlueToothInfo();
        for (int i = 0; i < mInfo.size(); i++) {
            if (mInfo.get(i).getDevice_no().equals(mac)) {
                return mInfo.get(i).getDevice_id();
            }
        }
        return -1;
    }

}

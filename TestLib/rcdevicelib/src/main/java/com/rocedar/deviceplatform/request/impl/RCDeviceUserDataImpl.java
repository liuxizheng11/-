package com.rocedar.deviceplatform.request.impl;

import android.content.Context;

import com.rocedar.base.RCLog;
import com.rocedar.base.network.IResponseData;
import com.rocedar.base.network.RequestData;
import com.rocedar.deviceplatform.dto.data.RCDeviceMultiDataDetailsDTO;
import com.rocedar.deviceplatform.request.RCDeviceUserDataRequest;
import com.rocedar.deviceplatform.request.bean.BasePlatformBean;
import com.rocedar.deviceplatform.request.bean.BeanDeleteUnBind;
import com.rocedar.deviceplatform.request.listener.RCDeviceDataDetailsListener;
import com.rocedar.deviceplatform.request.listener.RCDeviceMultiDataDetailsListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liuyi
 * @date 2017/3/4
 * @desc 用户设备数据实现类(请求后台接口)
 * @veison V3.3.30(动吖)
 */

public class RCDeviceUserDataImpl implements RCDeviceUserDataRequest{

    private static String TAG = "RCDeviceUserData_Request";
    private static RCDeviceUserDataImpl ourInstance;

    public static RCDeviceUserDataImpl getInstance(Context context) {
        if (ourInstance == null)
            ourInstance = new RCDeviceUserDataImpl(context);
        return ourInstance;
    }

    private Context mContext;

    private RCDeviceUserDataImpl(Context context) {
        this.mContext = context;
    }

    /**
     * 设备数据详情
     *
     * @param device_id 设备id
     */
    @Override
    public void loadDeviceDataDetails(int device_id,final RCDeviceDataDetailsListener listener) {
        BasePlatformBean bean = new BasePlatformBean();
        bean.setActionName("/p/health/device/data/"+device_id+"/");
        RequestData.NetWorkGetData(mContext, bean, RequestData.Method.Get, new IResponseData() {
            @Override
            public void getDataSucceedListener(JSONObject data) {
                JSONObject result = data.optJSONObject("result");

                listener.getDataSuccess(result);
                RCLog.d(TAG, "获取设备数据详情成功");

            }

            @Override
            public void getDataErrorListener(String msg, int status) {
                listener.getDataError(status, msg);
                RCLog.d(TAG, "获取设备数据详情失败" + msg);
            }
        });
    }

    /**
     * 多角色设备详情
     *
     * @param device_id 设备id
     * @param device_no 设备sn号
     */
    @Override
    public void loadMultiDeviceDataDetails(int device_id, String device_no, final RCDeviceMultiDataDetailsListener listener) {
        BeanDeleteUnBind bean = new BeanDeleteUnBind();
        bean.setActionName("/p/health/device/multi/data/"+device_id+"/");
        bean.setDevice_no(device_no);
        RequestData.NetWorkGetData(mContext, bean, RequestData.Method.Get, new IResponseData() {
            @Override
            public void getDataSucceedListener(JSONObject data) {
                JSONObject result = data.optJSONObject("result");
                RCDeviceMultiDataDetailsDTO dto = new RCDeviceMultiDataDetailsDTO();
                dto.setDevice_name(result.optString("device_name"));

                JSONArray users = result.optJSONArray("users");
                List<RCDeviceMultiDataDetailsDTO.UsersDTO> usersDTOs = new ArrayList<>();
                RCDeviceMultiDataDetailsDTO.UsersDTO usersDTO;
                for (int i = 0; i < users.length() && users.length() > 0; i++) {
                    JSONObject object = users.optJSONObject(i);
                    usersDTO = new RCDeviceMultiDataDetailsDTO.UsersDTO();
                    usersDTO.setDevice_id(object.optInt("device_id"));
                    usersDTO.setIcon(object.optString("icon"));
                    usersDTO.setTest_time(object.optLong("test_time"));
                    usersDTO.setType_name(object.optString("type_name"));
                    usersDTO.setUnit(object.optString("unit"));
                    usersDTO.setUrl(object.optString("url"));
                    usersDTO.setUser_id(object.optLong("user_id"));
                    usersDTO.setValue(object.optString("value"));
                    usersDTO.setType_id(object.optInt("type_id"));
                    usersDTO.setRights(object.optInt("rights"));
                    usersDTOs.add(usersDTO);
                }
                dto.setUsers(usersDTOs);
                listener.getDataSuccess(dto);
                RCLog.d(TAG, "获取多角色设备详情成功");

            }

            @Override
            public void getDataErrorListener(String msg, int status) {
                listener.getDataError(status, msg);
                RCLog.d(TAG, "获取多角色设备详情失败" + msg);
            }
        });
    }
}

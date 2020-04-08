package com.rocedar.sdk.iting.request.impl;

import android.content.Context;

import com.rocedar.lib.base.network.IRCPostListener;
import com.rocedar.lib.base.network.IResponseData;
import com.rocedar.lib.base.network.RCBean;
import com.rocedar.lib.base.network.RCRequestNetwork;
import com.rocedar.sdk.iting.device.ITingDeviceInfoUtil;
import com.rocedar.sdk.iting.request.IItingDeviceRequest;
import com.rocedar.sdk.iting.request.bean.BeanDeviceBind;
import com.rocedar.sdk.iting.request.dto.RCITingWatchInfoDTO;
import com.rocedar.sdk.iting.request.listener.ITingMineBindingListener;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * 项目名称：瑰柏SDK-ITING
 * <p>
 * 作者：phj
 * 日期：2019/3/30 2:44 PM
 * 版本：V1.1.00
 * 描述：瑰柏SDK-服务商品
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class ITingDeviceRequestImpl implements IItingDeviceRequest {


    private Context mContext;

    public ITingDeviceRequestImpl(Context context) {
        this.mContext = context;
    }


    private static final String Binding = "/p/device/bluetooth/";
    private static final String BindingMine = "/p/device/mine/app/";
    private static final String UnBinding = "/p/device/";

    @Override
    public void bindingDevice(String mac, int deviceId, final IRCPostListener listener) {
        BeanDeviceBind bean = new BeanDeviceBind();
        bean.setActionName(Binding + deviceId + "/");
        bean.setMac(mac);
        RCRequestNetwork.NetWorkGetData(mContext, bean, RCRequestNetwork.Method.Post,
                new IResponseData() {
                    @Override
                    public void getDataSucceedListener(JSONObject data) {
                        listener.getDataSuccess();
                    }

                    @Override
                    public void getDataErrorListener(String msg, int status) {
                        listener.getDataError(status, msg);
                    }
                });
    }

    @Override
    public void unBindingDevice(int deviceId, String deviceNo, final IRCPostListener listener) {
        BeanDeviceBind bean = new BeanDeviceBind();
        bean.setActionName(UnBinding + deviceId + "/");
        bean.setDevice_no(deviceNo);
        RCRequestNetwork.NetWorkGetData(mContext, bean, RCRequestNetwork.Method.Delete,
                new IResponseData() {
                    @Override
                    public void getDataSucceedListener(JSONObject data) {
                        listener.getDataSuccess();
                    }

                    @Override
                    public void getDataErrorListener(String msg, int status) {
                        listener.getDataError(status, msg);
                    }
                });


    }

    @Override
    public void getBindingDevice(final ITingMineBindingListener listener) {
        RCBean bean = new RCBean();
        bean.setActionName(BindingMine);
        RCRequestNetwork.NetWorkGetData(mContext, bean, RCRequestNetwork.Method.Get,
                new IResponseData() {
                    @Override
                    public void getDataSucceedListener(JSONObject data) {
                        //"bluetooth":[{"device_id":1224004,"device_no":"Helio #03721","device_name":"De-Watch3","bind_time":"20190416114427"}]
                        JSONArray array = data.optJSONObject("result").optJSONArray("bluetooth");
                        if (array != null && array.length() > 0) {
                            RCITingWatchInfoDTO dto =
                                    new ITingDeviceInfoUtil(array.optJSONObject(0).optString("device_no")).getDeviceDTO();
                            dto.setDeviceId(array.optJSONObject(0).optInt("device_id"));
                            dto.setDeviceName(array.optJSONObject(0).optString("device_name"));
                            listener.getDataSuccess(dto);
                        } else {
                            listener.getDataSuccess(null);
                        }
                    }

                    @Override
                    public void getDataErrorListener(String msg, int status) {
                        listener.getDataError(status, msg);
                    }
                });
    }


}

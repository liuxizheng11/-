package com.rocader.sdk.data.request;

import android.content.Context;

import com.rocader.sdk.data.request.bean.BeanPostUpload;
import com.rocedar.lib.base.network.IRCPostListener;
import com.rocedar.lib.base.network.IResponseData;
import com.rocedar.lib.base.network.RCRequestNetwork;
import com.rocedar.lib.base.unit.RCLog;

import org.json.JSONObject;

/**
 * 项目名称：设备管理平台
 * <p>
 * 作者：phj
 * 日期：2017/2/6 下午12:36
 * 版本：V1.0
 * 描述：设备操作请求后台接口的实现类
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCDeviceDataRequestImpl {

    private static String TAG = "RCDevice_Request";

    private Context mContext;

    public RCDeviceDataRequestImpl(Context context) {
        this.mContext = context;
    }


    /**
     * 数据上传接口
     *
     * @param listener
     * @param data     上传的设备数据（需要将jsonarray转换成string）
     */
    public void doUploading(final IRCPostListener listener, String data) {
        BeanPostUpload bean = new BeanPostUpload();
        bean.setActionName("/p/device/data/");
        bean.setData(data);
        RCRequestNetwork.NetWorkGetData(mContext, bean, RCRequestNetwork.Method.Post, new IResponseData() {
            @Override
            public void getDataSucceedListener(JSONObject data) {
                listener.getDataSuccess();
                RCLog.d(TAG, "数据上传成功");
            }

            @Override
            public void getDataErrorListener(String msg, int status) {
                listener.getDataError(status, msg);
                RCLog.d(TAG, "数据上传失败" + msg);
            }
        });
    }

}

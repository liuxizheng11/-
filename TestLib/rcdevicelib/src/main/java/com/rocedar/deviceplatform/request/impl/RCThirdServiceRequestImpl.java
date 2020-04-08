package com.rocedar.deviceplatform.request.impl;

import android.content.Context;

import com.rocedar.base.network.IResponseData;
import com.rocedar.base.network.RequestData;
import com.rocedar.deviceplatform.request.RCThirdServiceRequest;
import com.rocedar.deviceplatform.request.bean.BeanGetServiceStatus;
import com.rocedar.deviceplatform.request.listener.RCThirdServiceStatusListener;

import org.json.JSONObject;

/**
 * @author liuyi
 * @date 2017/4/26
 * @desc 第三方服务状态查询实现类
 * @veison V3.4.00(新增)
 */

public class RCThirdServiceRequestImpl implements RCThirdServiceRequest {

    private Context mContext;

    public RCThirdServiceRequestImpl(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void serviceStatusQuery(String serviceId, final RCThirdServiceStatusListener listener) {
        BeanGetServiceStatus bean = new BeanGetServiceStatus();
        bean.setActionName("/p/server/user/");
        bean.setServer_id(serviceId);
        RequestData.NetWorkGetData(mContext, bean, RequestData.Method.Get,
                new IResponseData() {
                    @Override
                    public void getDataSucceedListener(JSONObject data) {
                        int status = data.optJSONObject("result").optInt("status");
                        listener.getDataSuccess(status);
                    }

                    @Override
                    public void getDataErrorListener(String msg, int status) {
                        listener.getDataError(status, msg);
                    }
                });
    }
}

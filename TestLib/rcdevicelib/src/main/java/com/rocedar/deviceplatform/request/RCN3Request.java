package com.rocedar.deviceplatform.request;

import android.content.Context;

import com.rocedar.base.network.IResponseData;
import com.rocedar.base.network.RequestData;
import com.rocedar.deviceplatform.request.bean.BasePlatformBean;

/**
 * 项目名称：设备管理平台
 * <p>
 * 作者：phj
 * 日期：2017/5/6 下午4:45
 * 版本：V1.0.01
 * 描述：健康立方请求平台转发（业务逻辑在应用）
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public class RCN3Request {

    public static void resend(Context mContext, String actionName, int method, IResponseData responseData) {
        BasePlatformBean mBean = new BasePlatformBean();
        mBean.setActionName(actionName);
        RequestData.NetWorkGetData(mContext, mBean, method, responseData);
    }


}

package com.rocedar.lib.base.network;

import com.rocedar.lib.base.manage.RCSDKManage;
import com.rocedar.lib.base.unit.RCAndroid;

/**
 * 项目名称：瑰柏SDK-基础库
 * <p>
 * 作者：phj
 * 日期：2018/6/11 上午11:32
 * 版本：V1.0.00
 * 描述：瑰柏SDK-
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public enum NetworkMethod {

    SDK, API;

    public String getSecretKey() {
        switch (this) {
            case SDK:
                return RCSDKManage.getInstance().getAppKey();
            case API:
                String key = RCAndroid.getMetaData(
                        RCSDKManage.getInstance().getContext(),
                        "APPSECRET");
                return key != null ? key : "";
        }
        return "";
    }
}

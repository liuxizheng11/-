package com.rocedar.deviceplatform.request.bean;

import com.rocedar.base.RCBaseConfig;
import com.rocedar.base.network.RCBean;
import com.rocedar.base.shareprefernces.RCSPBaseInfo;

/**
 * 项目名称：设备管理平台
 * <p>
 * 作者：phj
 * 日期：2017/2/15 下午5:02
 * 版本：V1.0
 * 描述：平台用的基础网络Bean对象，在平台中所有的网络请求使用改bean为父类
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public class BasePlatformBean extends RCBean {

    public BasePlatformBean() {
        setBaseNetUrl(RCBaseConfig.APP_PT_NETWORK_URL);
    }

    public String p_token;

    public String getP_token() {
        return RCSPBaseInfo.getLastToken();
    }

}

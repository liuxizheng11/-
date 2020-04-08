package com.rocedar.lib.base.network.dto;

import com.rocedar.lib.base.network.IResponseData;
import com.rocedar.lib.base.network.RCBean;

import java.util.Map;

/**
 * 项目名称：瑰柏SDK-基础库
 * <p>
 * 作者：phj
 * 日期：2018/6/10 下午1:34
 * 版本：V1.0.00
 * 描述：瑰柏SDK-
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class NetworkGetDataDTO<NetworkBean extends RCBean> {

    private String contextName;
    private NetworkBean bean;
    private int method;
    private IResponseData iResponseData;
    private Map<String, String> headInfo;

    public Map<String, String> getHeadInfo() {
        return headInfo;
    }

    public void setHeadInfo(Map<String, String> headInfo) {
        this.headInfo = headInfo;
    }

    public String getContextName() {
        return contextName;
    }

    public void setContextName(String contextName) {
        this.contextName = contextName;
    }

    public NetworkBean getBean() {
        return bean;
    }

    public void setBean(NetworkBean bean) {
        this.bean = bean;
    }

    public int getMethod() {
        return method;
    }

    public void setMethod(int method) {
        this.method = method;
    }

    public IResponseData getiResponseData() {
        return iResponseData;
    }

    public void setiResponseData(IResponseData iResponseData) {
        this.iResponseData = iResponseData;
    }
}

package com.rocedar.sdk.familydoctor.request.bean;

import com.rocedar.lib.base.network.RCBean;

/**
 * 项目名称：瑰柏SDK-家庭医生
 * <p>
 * 作者：phj
 * 日期：2018/6/5 下午5:45
 * 版本：V1.0.00
 * 描述：瑰柏SDK-服务状态查询
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class BeanGetServiceStatus extends RCBean {

    public String device_no;

    public String getDevice_no() {
        return device_no;
    }

    public void setDevice_no(String device_no) {
        this.device_no = device_no;
    }
}

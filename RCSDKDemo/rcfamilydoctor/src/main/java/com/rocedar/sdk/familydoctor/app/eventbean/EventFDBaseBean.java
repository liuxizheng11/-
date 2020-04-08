package com.rocedar.sdk.familydoctor.app.eventbean;

/**
 * 项目名称：平台库-行为数据
 * <p>
 * 作者：phj
 * 日期：2018/2/26 上午10:29
 * 版本：V1.0.00
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public class EventFDBaseBean {


    public static final int FD_EVENT_TYPE_SHOW_OFFICE = 0;
    public static final int FD_EVENT_TYPE_SHOW_RECOED = 1;
    public static final int FD_EVENT_TYPE_SHOW_MY_DOCTOR = 2;
    public static final int FD_EVENT_TYPE_DO_START_ADVISORY = 3;
    public static final int FD_EVENT_TYPE_DO_REFRESH_MY_DOCTOR = 4;
    public static final int FD_EVENT_TYPE_DO_DELETE_MY_DOCTOR = 5;
    public static final int FD_EVENT_TYPE_DO_ADD_MY_DOCTOR = 6;

    public EventFDBaseBean(int type) {
        this.type = type;
    }


    public int type;


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}

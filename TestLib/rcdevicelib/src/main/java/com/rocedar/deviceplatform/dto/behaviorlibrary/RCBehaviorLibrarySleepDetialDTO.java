package com.rocedar.deviceplatform.dto.behaviorlibrary;

/**
 * 作者：lxz
 * 日期：17/7/29 下午7:51
 * 版本：V1.0
 * 描述：睡眠详情
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCBehaviorLibrarySleepDetialDTO {
    private String time;
    private String exception_name;
    private String title;
    private String exception_level;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getException_name() {
        return exception_name;
    }

    public void setException_name(String exception_name) {
        this.exception_name = exception_name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getException_level() {
        return exception_level;
    }

    public void setException_level(String exception_level) {
        this.exception_level = exception_level;
    }
}

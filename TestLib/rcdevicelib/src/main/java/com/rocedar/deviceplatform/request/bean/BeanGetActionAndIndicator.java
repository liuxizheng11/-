package com.rocedar.deviceplatform.request.bean;

/**
 * @author liuyi
 * @date 2017/3/7
 * @desc 行为／指标
 * @veison V3.3.30(动吖)
 */

public class BeanGetActionAndIndicator extends BasePlatformBean {
    /**
     * 开始时间
     */
    public String start_time;

    /**
     * 结束时间
     */
    public String end_time;

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }
}

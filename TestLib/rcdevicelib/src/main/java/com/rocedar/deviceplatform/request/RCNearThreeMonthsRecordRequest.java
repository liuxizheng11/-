package com.rocedar.deviceplatform.request;

import com.rocedar.deviceplatform.request.listener.RCHealthActionAndIndicatorListener;
import com.rocedar.deviceplatform.request.listener.RCHealthActionRecordListener;

/**
 * @author liuyi
 * @date 2017/3/7
 * @desc 近三月数据报告的接口类(请求后台接口)
 * @veison V3.3.30(动吖)
 */

public interface RCNearThreeMonthsRecordRequest {

    /**
     * 行为、指标
     *
     * @param start_time 开始时间
     * @param end_time   结束时间
     */
    void loadActionAndIndicator(long start_time, long end_time, RCHealthActionAndIndicatorListener listener);

    /**
     * 近三月行为/指标报告
     *
     * @param indicatorId 指标id
     * @param conductId   行为id
     * @param start_time  开始时间
     * @param end_time    结束时间
     */
    void loadActionRecord(int indicatorId, int conductId, long start_time, long end_time, RCHealthActionRecordListener listener);

}

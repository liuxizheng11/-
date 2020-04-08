package com.rocedar.deviceplatform.request;

import com.rocedar.deviceplatform.request.listener.RCResultListener;
import com.rocedar.deviceplatform.request.listener.behaviorlibrary.RCBehaviorChartsListener;
import com.rocedar.deviceplatform.request.listener.behaviorlibrary.RCBehaviorHealthHeartRateListener;
import com.rocedar.deviceplatform.request.listener.behaviorlibrary.RCBehaviorLibraryListener;
import com.rocedar.deviceplatform.request.listener.behaviorlibrary.RCBehaviorRecordListener;

/**
 * 作者：lxz
 * 日期：17/7/28 上午11:11
 * 版本：V1.0
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public interface RCBehaviorLibraryDataReqeust {

    /**
     * 3.1 行为列表数据查询
     *
     * @param deviceId  设备id  默认-1
     * @param conductId 行为id
     * @param liistener
     */
//    void getBehaviorListData(String deviceId, String conductId, RCBehaviorLibraryListener liistener);

    /**
     * 3.2行为数据查看详情
     *
     * @param deviceId  设备id  默认-1
     * @param conductId 行为id
     * @param dataTime  数据时间
     * @param liistener
     */
    void getBehaviorListDetailData(String deviceId, String conductId, String dataTime,
                                   RCBehaviorLibraryListener liistener);

    /**
     * 4.1.4 行为历史记录
     *
     * @param deviceId  设备id  默认-1
     * @param pn        页码
     * @param conductId 行为id
     * @param liistener
     */
    void getHealthRecordBehaviorData(String deviceId, String pn, String conductId,
                                     RCBehaviorLibraryListener liistener);

    /**
     * 1.3每日当前步数
     *
     * @param listener
     */
    void getTodayStepData(RCResultListener listener);

    /**
     * 4.5 获取行为记录数据
     *
     * @param conductId
     * @param end_date  日期
     * @param type      日 周 月
     * @param listener
     */
    void getBehaviorRecordtData(String conductId, String end_date, String type,
                                RCBehaviorRecordListener listener);

    /**
     * 行为图表数据
     *
     * @param conductId 指标ID
     * @param date      周期（日、月、年）
     * @param time      数据时间（yyyyMMdd）
     * @param listener
     */
    void getBehaviorDate(String conductId, String date, String time,
                         RCBehaviorChartsListener listener);

    /**
     * 3.4 心率查询(查询有效心率和目标心率)
     * 接口名称：/p/health/heart/rate/
     * 请求方式：GET
     *
     * @param listener
     */
    void getHealthHeartRate(RCBehaviorHealthHeartRateListener listener);
}

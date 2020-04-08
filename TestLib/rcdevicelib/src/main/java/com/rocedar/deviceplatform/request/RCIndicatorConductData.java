package com.rocedar.deviceplatform.request;

import android.content.Context;

import com.rocedar.deviceplatform.request.listener.RCGetConductIndicatorHistoryDataListener;
import com.rocedar.deviceplatform.request.listener.RCGetIndicatorInfoDataListener;
import com.rocedar.deviceplatform.request.listener.RCGetIndicatorMoreInfoListener;
import com.rocedar.deviceplatform.request.listener.RCGetTaskMoreInfoListener;
import com.rocedar.deviceplatform.request.listener.RCHealthIndicatorBMIListener;
import com.rocedar.deviceplatform.request.listener.indicator.RCHealthHeartRateDateListener;

/**
 * 项目名称：设备管理平台
 * <p>
 * 作者：phj
 * 日期：2017/3/8 下午3:00
 * 版本：V1.0
 * 描述：行为指标数据相关的接口
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public interface RCIndicatorConductData {

    /**
     * 获取家人的指标数据
     *
     * @param context     上下文
     * @param indicatorId 指标ID
     * @param pn          页码
     * @param userId      家人用户ID，查看自己时不传或传-1
     * @param device_id   设备id（没有传-1或不传）
     * @param listener    数据监听
     */
    void getIndicatorData(Context context, int indicatorId, int pn, long userId,
                          int device_id, RCGetIndicatorInfoDataListener listener);

    /**
     * 获取家人的指标数据
     *
     * @param indicatorId 指标ID
     * @param pn          页码
     * @param userId      用户ID
     */
    void getIndicatorData(Context context, int indicatorId, int pn,
                          long userId, RCGetIndicatorInfoDataListener listener);

    /**
     * 获取自己的指标数据
     *
     * @param context     上下文
     * @param indicatorId 指标ID
     * @param pn          页码
     * @param listener    数据监听
     */
    void getIndicatorData(Context context, int indicatorId, int pn, RCGetIndicatorInfoDataListener listener);

    /**
     * 获取家人的指标\行为历史数据
     *
     * @param context   上下文
     * @param indicator 指标ID
     * @param ConductId 行为ID
     * @param device_id 设备ID
     * @param userId    用户ID
     * @param pn        页码
     * @param listener  数据监听
     */
    void getConductIndicatorHistoryData(Context context, int indicator, int ConductId,
                                        int device_id, long userId, int pn
            , RCGetConductIndicatorHistoryDataListener listener);

    /**
     * 获取自己的指标\行为历史数据
     *
     * @param indicator 指标ID
     * @param ConductId 行为\
     * @param device_id 设备ID
     */
    void getConductIndicatorHistoryData(Context context, int indicator, int ConductId, int device_id, int pn
            , RCGetConductIndicatorHistoryDataListener listener);

    /**
     * 用户最新BMI
     *
     * @param context  上下文
     * @param listener 数据监听
     */
    void getIndicatorBMIData(Context context, RCHealthIndicatorBMIListener listener);

    /**
     * 指标数据查看更多，30天数据
     *
     * @param context   上下文
     * @param indicator 指标ID
     * @param listener  监听
     */
    void getIndicatorMoreThirtyData(Context context, int indicator, RCGetIndicatorMoreInfoListener listener);

    /**
     * 指标数据查看更多
     *
     * @param context   上下文
     * @param indicator 指标ID
     * @param month     月份（查看指标月报格式YYYYMM）,查看最近30天数据传-1
     * @param listener  监听
     */
    void getIndicatorMoreMonthData(Context context, int indicator, String month, RCGetIndicatorMoreInfoListener listener);

    /**
     * 任务查看更多
     *
     * @param context   上下文
     * @param device_id 设备Id（没有设备则传  -1）
     * @param pn        页码（从0开始）
     * @param conductId 行为ID
     * @param listener  监听
     */
    void getTaskMoreData(Context context, int device_id, int pn, int conductId, RCGetTaskMoreInfoListener listener);

    /**
     * 6.1  心率某天的数据
     *
     * @param context
     * @param date    yyyyMMddHHmmss
     */
    void getHealthHeartRateData(Context context, String date, RCHealthHeartRateDateListener listener);
}

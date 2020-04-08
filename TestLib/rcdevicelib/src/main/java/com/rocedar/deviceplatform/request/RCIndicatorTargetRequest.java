package com.rocedar.deviceplatform.request;


import com.rocedar.deviceplatform.request.listener.RCPostListener;
import com.rocedar.deviceplatform.request.listener.target.RCTargetEditDataListener;
import com.rocedar.deviceplatform.request.listener.target.RCTargetLoginDataListener;
import com.rocedar.deviceplatform.request.listener.target.RCUserTargetDayDataListener;
import com.rocedar.deviceplatform.request.listener.target.RCUserTargetWeekDataListener;
import com.rocedar.deviceplatform.request.listener.target.RCUserTargetWeekTypeListener;
import com.rocedar.deviceplatform.request.listener.target.RCUserWeekTargetDaySevenListener;
import com.rocedar.deviceplatform.request.listener.target.RCUserWeekTargetProgressListener;

/**
 * 项目名称：平台
 * <p>
 * 作者：phj
 * 日期：2017/7/7 下午3:11
 * 版本：V2.2.00
 * 描述：健康目标接口 1.1 目标查询 1.2 更新目标
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public interface RCIndicatorTargetRequest {


    /**
     * 1.1.1  查询标签目标 (用户注册页面 获取健康目标)
     *
     * @param monday         周一的日期（yyyyMMdd） 健康目标不传
     * @param target_type_id 目标类型ID   健康目标不传
     */
    void getUserTargetData(String monday, String target_type_id, RCTargetLoginDataListener listener);

    /**
     * 1.1.2 查询用户日目标
     *
     * @param record_date 日期
     */
    void getUserDayTargetData(String record_date, RCUserTargetDayDataListener listener);

    /**
     * 1.1.8 获取用户目标
     *
     * @param monday         周一的日期（yyyyMMdd） 健康目标不传
     * @param target_type_id 目标类型ID   健康目标不传
     */
    void getUserTargetUserData(String monday, String target_type_id, RCTargetEditDataListener listener);

    /**
     * 1.1.4 获取目标类型
     */
    void getUserTargetType(RCUserTargetWeekTypeListener listener);

    /**
     * 1.1.5 查询周目标
     */
    void getUserWeekTargetData(String monday, RCUserTargetWeekDataListener listener);


    /**
     * 1.1.6 查询用户这一周目标
     */
    void getUserWeekTargetDaySeven(String monday, RCUserWeekTargetDaySevenListener listener);


    /**
     * 1.1.7 查询周目标进程
     *
     * @param record_date
     * @param listener
     */
    void getUserWeekTargetProgress(String record_date, RCUserWeekTargetProgressListener listener);


    /**
     * 1.2.1 保存标签目标
     *
     * @param listener
     * @param values   目标值
     */
    void postTargetLabel(String values, RCPostListener listener);

    /**
     * 1.2.2 更新用户目标
     *
     * @param listener
     * @param values   目标值
     */
    void putUserTarget(String values, RCPostListener listener);

    /**
     * 1.2.3 更新用户目标状态
     *
     * @param listener
     * @param status    状态值
     * @param targetTypeId    目标类型ID
     * @param target_id 目标ID
     */
    void putUserTargetStatus(String target_id,String targetTypeId, String status, RCPostListener listener);
}

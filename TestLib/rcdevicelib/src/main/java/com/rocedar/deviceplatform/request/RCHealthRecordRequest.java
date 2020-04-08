package com.rocedar.deviceplatform.request;

import com.rocedar.deviceplatform.request.listener.RCHealthDoctorRecordListListener;
import com.rocedar.deviceplatform.request.listener.RCHealthFamilyRecordListener;
import com.rocedar.deviceplatform.request.listener.RCHealthHomeRecordListener;
import com.rocedar.deviceplatform.request.listener.RCHealthRecordDataListener;
import com.rocedar.deviceplatform.request.listener.RCHealthReportPlanListener;
import com.rocedar.deviceplatform.request.listener.RCHealthRxclusiveRecordListener;
import com.rocedar.deviceplatform.request.listener.RCPostListener;

/**
 * @author liuyi
 * @date 2017/3/4
 * @desc 健康档案的接口类(请求后台接口)
 * @veison V3.3.30(动吖)
 */

public interface RCHealthRecordRequest {

    /**
     * 我的健康档案
     */
    void loadMyHealthRecord(RCHealthHomeRecordListener listener);

    /**
     * 我的专属健康报告／医生报告
     *
     * @param record_type 类型id （医生报告--> 1，专属健康报告-->2）
     * @param record_id   报告id（专属健康报告传-1，医生报告在app进入传-1）
     * @param user_id     用户userid
     */
    void loadMyExclusiveHealthRecord(int record_type, long record_id, long user_id, RCHealthRxclusiveRecordListener listener);

    /**
     * 医生报告列表
     */
    void loadDoctorRecordList(RCHealthDoctorRecordListListener listener, long user_id);

    /**
     * 家人档案
     *
     * @param user_id  家人用户ID
     * @param listener
     */
    void loadFamilyHealthRecord(long user_id, RCHealthFamilyRecordListener listener);

    /***
     * 5.1 体检报告列表查询
     * @param pn
     * @param listener
     */
    void getReportPlanData(String pn, RCHealthReportPlanListener listener);


    /**
     * 5.3用户上传体检报告
     *
     * @param title      体检报告标题
     * @param imgUrl     上传图片地址，多个以;分开
     * @param remarks    备注
     * @param reportDate 体检报告时间
     * @param listener
     */
    void postReportPlanData(String title, String imgUrl, String remarks, String reportDate, RCPostListener listener);

    /**
     * 档案首页 接口
     *
     * @param listener
     */
    void getHealthRecordData(RCHealthRecordDataListener listener);
}

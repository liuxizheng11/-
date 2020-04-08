package com.rocedar.sdk.familydoctor.request;

import com.rocedar.lib.base.network.IRCPostListener;
import com.rocedar.sdk.familydoctor.dto.mingyi.RCMingYiMaterialDTO;
import com.rocedar.sdk.familydoctor.request.listener.mingyi.RCMingYiDoctorDetailListener;
import com.rocedar.sdk.familydoctor.request.listener.mingyi.RCMingYiDoctorListListener;
import com.rocedar.sdk.familydoctor.request.listener.mingyi.RCMingYiDoctorListSelectListener;
import com.rocedar.sdk.familydoctor.request.listener.mingyi.RCMingYiPostMaterialListener;
import com.rocedar.sdk.familydoctor.request.listener.mingyi.RCMingYiPostOrderListener;

/**
 * 项目名称：瑰柏SDK-家庭医生
 * <p>
 * 作者：phj
 * 日期：2018/7/20 下午3:27
 * 版本：V1.0.00
 * 描述：瑰柏SDK-家庭医生，名医接口
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public interface IRCMingYiRequest {


    /**
     * 获取推荐医生列表
     */
    void getRecommendDoctor(RCMingYiDoctorListListener listener);

    /**
     * 医生详情
     *
     * @param doctor_id 医生ID
     * @param listener
     */
    void getDoctorDetail(String doctor_id, RCMingYiDoctorDetailListener listener);


    /**
     * 获取 医生列表科室、价格、医院数据
     *
     * @param status
     * @param listener
     */
    void getDoctorListSelectData(int status, RCMingYiDoctorListSelectListener listener);

    /**
     * 获取 医生列表 数据
     *
     * @param department 科室：<一级科室ID>,<二级科室ID>
     * @param fee        价格
     * @param hospital   医院：<一级医院ID>,<二级医院ID>
     * @param pn
     * @param listener
     */
    void getDoctorListData(String department, String fee, String hospital, int pn, RCMingYiDoctorListListener listener);


    /**
     * 保存订单
     *
     * @param doctor_id        医生id
     * @param service_type     服务类型
     * @param patient_id       病人id
     * @param phone            手机号
     * @param reservation_time 期望时间
     */
    void postConsultOrder(String doctor_id, String service_type, String patient_id,
                          String phone, String reservation_time, RCMingYiPostOrderListener listener);




    /**
     * 保存问诊记录
     */
    void postMaterial(RCMingYiMaterialDTO dto, IRCPostListener listener);

    /**
     * 获取问诊记录
     */
    void getMaterial(String orderId, String patientId, RCMingYiPostMaterialListener listener);

}



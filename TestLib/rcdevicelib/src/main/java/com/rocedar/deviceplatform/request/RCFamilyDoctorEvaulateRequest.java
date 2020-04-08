package com.rocedar.deviceplatform.request;

import com.rocedar.deviceplatform.request.listener.familydoctor.RCFDGetDoctorCommnetsListener;
import com.rocedar.deviceplatform.request.listener.familydoctor.RCFDGetDoctorIntroduceListener;
import com.rocedar.deviceplatform.request.listener.familydoctor.RCFDPostListener;

/**
 * @author liuyi
 * @date 2017/11/7
 * @desc 家庭医生评价有关请求接口类
 * @veison V3431
 */

public interface RCFamilyDoctorEvaulateRequest {

    /**
     * 接口名称：保存用户评价
     *
     * @param record_id 问诊记录id
     * @param doctor_id 医生Id
     * @param comment   评价
     * @param grade     评分
     *                  请求方式：POST
     */
    void saveUserEvaulate(String record_id, String doctor_id, String comment, String grade, RCFDPostListener listener);


    /**
     * 接口名称：查询医生评价列表
     *
     * @param doctor_id 医生id
     * @param page      第几页
     */
    void getDoctorCommentList(String doctor_id, int page, RCFDGetDoctorCommnetsListener listener);


    /**
     * 接口名称：查询医生详情
     *
     * @param doctor_id 医生id
     */
    void getDoctorIntroduced(String doctor_id, RCFDGetDoctorIntroduceListener listener);

}

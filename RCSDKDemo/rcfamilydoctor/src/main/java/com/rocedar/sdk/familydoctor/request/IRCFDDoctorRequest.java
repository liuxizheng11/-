package com.rocedar.sdk.familydoctor.request;

import com.rocedar.lib.base.network.IRCPostListener;
import com.rocedar.sdk.familydoctor.request.listener.fd.RCFDGetDepartmentListener;
import com.rocedar.sdk.familydoctor.request.listener.fd.RCFDGetDoctorCommentsListener;
import com.rocedar.sdk.familydoctor.request.listener.fd.RCFDGetDoctorIntroduceListener;
import com.rocedar.sdk.familydoctor.request.listener.fd.RCFDGetDoctorListListener;
import com.rocedar.sdk.familydoctor.request.listener.fd.RCFDGetServerStatusListener;

/**
 * 项目名称：瑰柏SDK-家庭医生
 * <p>
 * 作者：phj
 * 日期：2018/5/18 下午5:26
 * 版本：V1.0.00
 * 描述：瑰柏SDK-医生数据接口
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public interface IRCFDDoctorRequest {

    /**
     * 接口名称：查询科室列表
     * 请求方式：GET
     */
    void getDoctorDepartment(RCFDGetDepartmentListener listener);


    /**
     * 接口名称：根据科室获取医生列表（有分页）
     * 请求方式：GET
     *
     * @param departmentId 科室ID
     * @param pn           页码
     */
    void getDoctorListFormDepartment(String departmentId, int pn, RCFDGetDoctorListListener listener);


    /**
     * 接口名称：我的医生列表（有分页）
     * 请求方式：GET
     *
     * @param pn 页码
     */
    void getMyDoctorList(int pn, RCFDGetDoctorListListener listener);


    /**
     * 接口名称：添加关注医生
     * 请求方式：POST
     *
     * @param doctor_id 医生ID
     */
    void addMyDoctor(String doctor_id, IRCPostListener postListener);

    /**
     * 接口名称：取消关注医生
     * 请求方式：DELETE
     *
     * @param doctor_id 医生ID
     */
    void deleteMyDoctor(String doctor_id, IRCPostListener postListener);

    /**
     * 接口名称：查询医生详情
     *
     * @param doctor_id 医生id
     */
    void getDoctorIntroduce(String doctor_id, RCFDGetDoctorIntroduceListener listener);

    /**
     * 接口名称：保存用户评价
     *
     * @param record_id 问诊记录id
     * @param doctor_id 医生Id
     * @param comment   评价
     * @param grade     评分
     *                  请求方式：POST
     */
    void saveUserEvaluation(String record_id, String doctor_id, String comment, String grade,
                            IRCPostListener listener);


    /**
     * 接口名称：查询医生评价列表
     *
     * @param doctor_id 医生id
     * @param page      第几页
     */
    void getDoctorCommentList(String doctor_id, int page, RCFDGetDoctorCommentsListener listener);



}

package com.rocedar.deviceplatform.request;

import com.rocedar.deviceplatform.request.listener.familydoctor.RCFDGetDepartmentListener;
import com.rocedar.deviceplatform.request.listener.familydoctor.RCFDGetDoctorListListener;
import com.rocedar.deviceplatform.request.listener.familydoctor.RCFDGetRecordDetailListener;
import com.rocedar.deviceplatform.request.listener.familydoctor.RCFDGetRecordListListener;
import com.rocedar.deviceplatform.request.listener.familydoctor.RCFDPostListener;

/**
 * 项目名称：设备管理平台
 * <p>
 * 作者：phj
 * 日期：2017/4/20 上午10:41
 * 版本：V1.0.01
 * 描述：家庭医生接口
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public interface RCFamilyDoctorRequest {


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
    void addMyDoctor(String doctor_id, RCFDPostListener postListener);

    /**
     * 接口名称：取消关注医生
     * 请求方式：DELETE
     *
     * @param doctor_id 医生ID
     */
    void deleteMyDoctor(String doctor_id, RCFDPostListener postListener);


    /**
     * 接口名称：问诊记录列表
     * 请求方式：GET
     *
     * @param pn 页码
     */
    void getRecordList(int pn, RCFDGetRecordListListener listener);

    /**
     * 接口名称：问诊记录详情
     * 请求方式：GET
     *
     * @param recordId 记录ID
     */
    void getRecordDetail(String recordId, RCFDGetRecordDetailListener listener);

    /**
     * 接口名称：保存问诊记录
     * 请求方式：Post
     *
     * @param recordId 记录ID
     */
    void saveRecord(String recordId, RCFDPostListener postListener);


}

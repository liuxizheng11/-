package com.rocedar.sdk.familydoctor.request;

import com.rocedar.lib.base.network.IRCPostListener;
import com.rocedar.sdk.familydoctor.request.listener.fd.RCFDGetRecordDetailListener;
import com.rocedar.sdk.familydoctor.request.listener.fd.RCFDGetRecordListListener;
import com.rocedar.sdk.familydoctor.request.listener.fd.RCFDGetServerStatusListener;
import com.rocedar.sdk.familydoctor.request.listener.fd.RCFDGetSpecificCommnetsListener;

/**
 * 项目名称：瑰柏SDK-家庭医生
 * <p>
 * 作者：phj
 * 日期：2018/5/18 下午5:28
 * 版本：V1.0.00
 * 描述：瑰柏SDK-咨询记录及评价
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public interface IRCFDRecordRequest {


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
    void saveRecord(String recordId, IRCPostListener postListener);

    /**
     * 接口名称：查询瑰柏专属医生(点视频咨询调用)
     * 请求方式GET
     */
    void getFDSpecificDoctor(RCFDGetRecordDetailListener listener);

    /**
     * 接口名称：查询瑰柏专属医生评价列表
     * 请求方式GET
     */
    void getFDSpecificComments(int pn, RCFDGetSpecificCommnetsListener listener);

    /**
     * 接口名称：查询用户服务状态查询
     * 请求方式：GET
     *
     * @param listener
     */
    void getDoctorServerStatus(String serviceId, String device_no, RCFDGetServerStatusListener listener);

    /**
     * 接口名称：绑定手机号
     * 请求方式：GET
     *
     * @param listener
     */
    void doBindPhoneNumber(String phoneNumber, IRCPostListener listener);


}

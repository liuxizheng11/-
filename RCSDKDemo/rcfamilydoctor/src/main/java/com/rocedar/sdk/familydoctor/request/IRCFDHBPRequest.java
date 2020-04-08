package com.rocedar.sdk.familydoctor.request;

import com.rocedar.lib.base.network.IRCPostListener;
import com.rocedar.sdk.familydoctor.request.listener.RCFDPatientListListener;
import com.rocedar.sdk.familydoctor.request.listener.hdp.RCHBPGetDoctorDetailsListener;
import com.rocedar.sdk.familydoctor.request.listener.hdp.RCHBPGetDoctorListener;
import com.rocedar.sdk.familydoctor.request.listener.hdp.RCHBPGetNewSListener;
import com.rocedar.sdk.familydoctor.request.listener.hdp.RCHBPGetOrganizationListListener;
import com.rocedar.sdk.familydoctor.request.listener.hdp.RCHBPGetRecordListListener;
import com.rocedar.sdk.familydoctor.request.listener.hdp.RCHBPGetVideoInstituListener;
import com.rocedar.sdk.familydoctor.request.listener.hdp.RCHBPSaveConsultRecordListener;
import com.rocedar.sdk.familydoctor.request.listener.hdp.RCHBPSaveOthersInfoListener;

/**
 * @author liuyi
 * @date 2017/11/27
 * @desc 高血压大数据有关请求接口类
 * @veison V3.4.32
 */

public interface IRCFDHBPRequest {

    /**
     * 视频讲座查询
     *
     * @param pn       页码
     * @param listener
     */
    void getVideoInstituteList(String pn, RCHBPGetVideoInstituListener listener);

    /**
     * 高血压大数据医生
     *
     * @param pn
     * @param listener
     */
    void getHBPDoctor(String pn, RCHBPGetDoctorListener listener);

    /**
     * 高血压大数据是否有新消息
     *
     * @param org_id   机构id
     * @param listener
     */
    void getIsNews(int org_id, RCHBPGetNewSListener listener);

    /**
     * 高血压大数据医生详情
     *
     * @param doctorId
     * @param listener
     */
    void getHBPDoctorDetails(int doctorId, RCHBPGetDoctorDetailsListener listener);

    /**
     * 查询消息列表
     *
     * @param listener
     * @param pn       页码
     * @param org_id   机构id
     */
    void getHBPRecordList(String pn, int org_id, RCHBPGetRecordListListener listener);

    /**
     * 保存其他人基本信息
     *
     * @param sick_name     病人名称
     * @param sick_sex      病人性别
     * @param sick_birthday 病人生日
     * @param sick_height   病人身高
     * @param sick_weight   病人体重
     * @param listener
     */
    void saveOthersInfo(String sick_name, String sick_sex, String sick_birthday,
                        String sick_height, String sick_weight, RCHBPSaveOthersInfoListener listener);

    /**
     * 保存咨询记录
     *
     * @param question_id 问题id
     * @param sick_id     病人id
     * @param type        0，功能；1，文本；2图片
     * @param speaker     0，系统消息；1， app用户
     * @param record      文字消息
     * @param img_url     图片消息
     * @param org_id      机构id
     * @param listener
     */
    void saveConsultRecord(String question_id, String sick_id, String type,
                           String speaker, String record, String img_url, int org_id,
                           RCHBPSaveConsultRecordListener listener);

    /**
     * 结束咨询
     *
     * @param question_id 问题id
     * @param org_id      机构id
     * @param listener
     */
    void finishConsultRecord(String question_id, int org_id, IRCPostListener listener);


    /**
     * 机构列表
     * 请求方式：GET
     *
     * @param listener
     */
    void getOrganizationList(RCHBPGetOrganizationListListener listener);


    /**
     * 病人列表
     */
    void getPatientList(RCFDPatientListListener listener);


    /**
     * 保存病人数据
     */
    void savePatientInfo(String patient_id, String name, String sex, String birthday, String height,
                         String weight, IRCPostListener listener);
}

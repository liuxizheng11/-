package com.rocedar.sdk.familydoctor.request.listener.fd;


import com.rocedar.sdk.familydoctor.dto.RCFDDoctorIntroduceDTO;

/**
 * 项目名称：设备管理平台
 * <p>
 * 作者：phj
 * 日期：2017/4/20 上午11:40
 * 版本：V1.0.01
 * 描述：获取医生简介（医生详情）
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public interface RCFDGetDoctorIntroduceListener {

    void getDataSuccess(RCFDDoctorIntroduceDTO dto);

    void getDataError(int status, String msg);
}

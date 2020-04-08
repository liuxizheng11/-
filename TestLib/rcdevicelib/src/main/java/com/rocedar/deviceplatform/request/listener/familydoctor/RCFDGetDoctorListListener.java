package com.rocedar.deviceplatform.request.listener.familydoctor;

import com.rocedar.deviceplatform.dto.familydoctor.RCFDDoctorListDTO;

import java.util.List;

/**
 * 项目名称：设备管理平台
 * <p>
 * 作者：phj
 * 日期：2017/4/20 上午11:40
 * 版本：V1.0.01
 * 描述：获取医生列表监听方法
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public interface RCFDGetDoctorListListener {

    void getDataSuccess(List<RCFDDoctorListDTO> doctorListDTO);

    void getDataError(int status, String msg);
}

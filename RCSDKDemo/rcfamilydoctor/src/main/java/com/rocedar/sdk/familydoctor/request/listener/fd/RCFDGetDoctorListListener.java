package com.rocedar.sdk.familydoctor.request.listener.fd;

import com.rocedar.sdk.familydoctor.dto.RCFDDoctorListDTO;

import java.util.List;

/**
 * 项目名称：瑰柏SDK-家庭医生
 * <p>
 * 作者：phj
 * 日期：2018/5/21 下午3:38
 * 版本：V1.0.00
 * 描述：瑰柏SDK-家庭医生获取医生列表监听
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public interface RCFDGetDoctorListListener {
    void getDataSuccess(List<RCFDDoctorListDTO> doctorListDTO);

    void getDataError(int status, String msg);

}

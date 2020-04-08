package com.rocedar.deviceplatform.request.listener;

import com.rocedar.deviceplatform.dto.record.RCHealthDoctorRecordListDTO;

import java.util.List;

/**
 * @author liuyi
 * @date 2017/3/8
 * @desc 医生报告列表监听
 * @veison V3.3.30(动吖)
 */

public interface RCHealthDoctorRecordListListener {

    void getDataSuccess(List<RCHealthDoctorRecordListDTO> dtoList);

    void getDataError(int status, String msg);
}

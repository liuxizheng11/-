package com.rocedar.deviceplatform.request.listener;

import com.rocedar.deviceplatform.dto.record.RCHealthFamilyRecordDTO;

import java.util.List;

/**
 * @author liuyi
 * @date 2017/3/11
 * @desc 家人档案监听
 * @veison V3.3.30(动吖)
 */

public interface RCHealthFamilyRecordListener {

    void getDataSuccess(List<RCHealthFamilyRecordDTO> dtoList);

    void getDataError(int status, String msg);
}

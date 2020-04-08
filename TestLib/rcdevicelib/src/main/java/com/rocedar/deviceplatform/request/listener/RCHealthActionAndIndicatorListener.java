package com.rocedar.deviceplatform.request.listener;

import com.rocedar.deviceplatform.dto.record.RCHealthActionAndIndicatorDTO;

import java.util.List;

/**
 * @author liuyi
 * @date 2017/3/7
 * @desc 行为／指标监听
 * @veison V3.3.30(动吖)
 */

public interface RCHealthActionAndIndicatorListener {

    void getDataSuccess(List<RCHealthActionAndIndicatorDTO> dtoList);

    void getDataError(int status, String msg);
}

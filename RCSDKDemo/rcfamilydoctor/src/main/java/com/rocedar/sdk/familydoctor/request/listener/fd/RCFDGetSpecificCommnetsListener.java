package com.rocedar.sdk.familydoctor.request.listener.fd;


import com.rocedar.sdk.familydoctor.dto.RCFDSpecificCommentsDTO;

import java.util.List;

/**
 * @author liuyi
 * @date 2018/4/27
 * @desc 查询瑰柏专属医生评价列表
 * @veison 3700
 */

public interface RCFDGetSpecificCommnetsListener {

    void getDataSuccess(List<RCFDSpecificCommentsDTO> dto);

    void getDataError(int status, String msg);

}
